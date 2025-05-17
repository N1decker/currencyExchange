package ru.nidecker.currencyExchange.exchangeRate.impl;

import ru.nidecker.currencyExchange.exceptions.CouldNotFetchData;
import ru.nidecker.currencyExchange.exceptions.CouldNotSaveEntity;
import ru.nidecker.currencyExchange.exceptions.CouldNotUpdateEntity;
import ru.nidecker.currencyExchange.exceptions.InvalidParameterException;
import ru.nidecker.currencyExchange.exchangeRate.*;
import ru.nidecker.currencyExchange.mapper.ExchangeRateMapper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

            if (baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty() || rate.equals(BigDecimal.ZERO)) {
                throw new InvalidParameterException("Отсутствует нужное поле формы");
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
}
