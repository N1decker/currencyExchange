package ru.nidecker.currencyExchange.exchangeRate.impl;

import ru.nidecker.currencyExchange.exceptions.*;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateRepository;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRateRequest;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateService;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRateResponse;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeResponse;
import ru.nidecker.currencyExchange.mapper.ExchangeRateMapper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository repository;
    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;

    public ExchangeRateServiceImpl() {
        this.repository = new ExchangeRateRepositoryImpl();
    }

    @Override
    public List<ExchangeRateResponse> findAll() {
        List<ExchangeRate> list = repository.findAll();
        return mapper.toExchangeRateResponseList(list);
    }

    @Override
    public ExchangeRateResponse findById(Integer id) {
        ExchangeRate rate = repository.findById(id).orElseThrow(() -> new CouldNotFetchData("Не удалось получить обменный курс"));
        return mapper.toExchangeRateResponse(rate);
    }

    @Override
    public ExchangeRateResponse findByPair(String code1, String code2) {
        ExchangeRate rate = repository.findByPair(code1, code2).orElseThrow(() -> new CouldNotFetchData("Не удалось получить обменный курс"));
        return mapper.toExchangeRateResponse(rate);
    }

    @Override
    public ExchangeRateResponse create(ExchangeRateRequest exchangeRateRequest) {
        ExchangeRate rate = repository.create(exchangeRateRequest).orElseThrow(() -> new CouldNotSaveEntity("Не удалось сохранить обменный курс"));
        return mapper.toExchangeRateResponse(rate);
    }

    @Override
    public ExchangeRateResponse update(String code1, String code2, BigDecimal rate) {
        ExchangeRate exchangeRate = repository.update(code1, code2, rate).orElseThrow(() -> new CouldNotUpdateEntity("Не удалось обновить обменный курс"));
        return mapper.toExchangeRateResponse(exchangeRate);
    }

    @Override
    public ExchangeRateResponse update(ExchangeRate exchangeRate) {
        ExchangeRate rate = repository.update(exchangeRate).orElseThrow(() -> new CouldNotUpdateEntity("Не удалось обновить обменный курс"));
        return mapper.toExchangeRateResponse(rate);
    }

    @Override
    public ExchangeRateResponse create(ExchangeRate exchangeRate) {
        ExchangeRate rate = repository.create(exchangeRate).orElseThrow(() -> new CouldNotSaveEntity("Не удалось сохранить обменный курс"));
        return mapper.toExchangeRateResponse(rate);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Override
    public ExchangeRateRequest parseRequestParams(String line) {
        String baseCurrencyCode = "", targetCurrencyCode = "";
        BigDecimal rate = BigDecimal.ZERO;
        try {
            String[] params = URLDecoder.decode(line, StandardCharsets.UTF_8.name()).split("&");
            for (String param : params) {
                if (param.contains("baseCurrencyCode")) {
                    baseCurrencyCode = param.substring(param.indexOf("=") + 1);
                } else if (param.contains("targetCurrencyCode")) {
                    targetCurrencyCode = param.substring(param.indexOf("=") + 1);
                } else if (param.contains("rate")) {
                    rate = BigDecimal.valueOf(
                            Double.parseDouble(
                                    param.substring(param.indexOf("=") + 1)
                            )
                    );
                }
            }

            if (rate.doubleValue() <= 0d) {
                throw new InvalidParameterException("Курс не может быть меньше 0.001");
            }

            if (baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty() || rate.equals(BigDecimal.ZERO)) {
                throw new InvalidParameterException("Отсутствует нужное поле формы");
            }

            if (baseCurrencyCode.length() > 3 || targetCurrencyCode.length() > 3) {
                throw new InvalidParameterException("Передано некорректное значение валют(ы)");
            }

            if (baseCurrencyCode.equalsIgnoreCase(targetCurrencyCode)) {
                throw new InvalidParameterException("Валюта должна быть различна");
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("В поле rate передано неправильное значение");
        }

        return new ExchangeRateRequest(baseCurrencyCode, targetCurrencyCode, rate);
    }

    @Override
    public ExchangeResponse calculateExchange(String code1, String code2, String amount) {
        if (amount == null || amount.isEmpty()) throw new InvalidParameterException("Отсутствует нужное поле формы");

        BigDecimal convertedAmount;
        try {
            convertedAmount = BigDecimal.valueOf(Double.parseDouble(amount));
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("В поле amount передано неправильное значение");
        }

        if (code1 == null || code1.isEmpty()
        || code2 == null || code2.isEmpty()
        || convertedAmount.doubleValue() < 0d) {
            throw new InvalidParameterException("Отсутствует нужное поле формы");
        } else if (code1.length() > 3 || code2.length() > 3) {
            throw new InvalidParameterException("Передано некорректное значение валют(ы)");
        }

        if (code1.equalsIgnoreCase(code2)) {
            throw new InvalidParameterException("Передана одинаковая валюта в параметры");
        }

        Optional<ExchangeRate> byPair;
        ExchangeRate rate;
        try {
            byPair = repository.findByPair(code1, code2);
            if (byPair.isPresent()) {
                rate = byPair.get();
                return new ExchangeResponse(
                        rate.getBaseCurrency(),
                        rate.getTargetCurrency(),
                        rate.getRate(),
                        convertedAmount,
                        rate.getRate().multiply(convertedAmount)
                );
            }
        } catch (NotFoundException e) {
            try {
                byPair = repository.findByPair(code2, code1);
                if (byPair.isPresent()) {
                    rate = byPair.get();
                    rate.setRate(BigDecimal.ONE.divide(rate.getRate(), 3, RoundingMode.HALF_UP));
                    return new ExchangeResponse(
                            rate.getTargetCurrency(),
                            rate.getBaseCurrency(),
                            rate.getRate(),
                            convertedAmount,
                            rate.getRate().multiply(convertedAmount)
                    );
                }
            } catch (NotFoundException ex) {
                try {
                    Optional<ExchangeRate> usdToBase = repository.findByPair("USD", code1);
                    Optional<ExchangeRate> usdToTarget = repository.findByPair("USD", code2);
                    if (!usdToBase.isPresent() || !usdToTarget.isPresent()) {
                        throw new CouldNotFetchData("Не удалось вычислить курс");
                    }

                    BigDecimal newRate = usdToTarget.get().getRate()
                            .divide(usdToBase.get().getRate(), 3, RoundingMode.HALF_UP);
                    return new ExchangeResponse(
                            usdToBase.get().getBaseCurrency(),
                            usdToTarget.get().getTargetCurrency(),
                            newRate,
                            convertedAmount,
                            newRate.multiply(convertedAmount)
                    );

                } catch (NotFoundException exception) {
                    throw new CouldNotFetchData("Не удалось вычислить курс");
                }
            }
        }

        throw new CouldNotFetchData("Не удалось вычислить курс");
    }
}
