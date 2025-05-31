package ru.nidecker.currencyExchange.currency.impl;

import ru.nidecker.currencyExchange.currency.entity.Currency;
import ru.nidecker.currencyExchange.currency.CurrencyRepository;
import ru.nidecker.currencyExchange.currency.entity.CurrencyResponse;
import ru.nidecker.currencyExchange.currency.CurrencyService;
import ru.nidecker.currencyExchange.exceptions.CouldNotFetchData;
import ru.nidecker.currencyExchange.exceptions.CouldNotSaveEntity;
import ru.nidecker.currencyExchange.exceptions.CouldNotUpdateEntity;
import ru.nidecker.currencyExchange.exceptions.InvalidParameterException;
import ru.nidecker.currencyExchange.mapper.CurrencyMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository repository;
    private final CurrencyMapper mapper = CurrencyMapper.INSTANCE;

    public CurrencyServiceImpl() {
        this.repository = new CurrencyRepositoryImpl();
    }

        @Override
    public List<CurrencyResponse> findAll() {
        List<Currency> list = repository.findAll();
        return mapper.toCurrencyResponseList(list);
    }

    @Override
    public CurrencyResponse findById(Integer id) {
        Currency currency = repository.findById(id).orElseThrow(() -> new CouldNotFetchData("Не удалось получить описание валюты"));
        return mapper.toCurrencyResponse(currency);
    }

    @Override
    public CurrencyResponse findByCode(String code) {
        Currency currency = repository.findByCode(code).orElseThrow(() -> new CouldNotFetchData("Не удалось получить описание валюты"));
        return mapper.toCurrencyResponse(currency);
    }

    @Override
    public CurrencyResponse create(Currency currency) {
        currency = repository.create(currency).orElseThrow(() -> new CouldNotSaveEntity("Не удалось создать валюту"));
        return mapper.toCurrencyResponse(currency);
    }

    @Override
    public CurrencyResponse update(Currency currency) {
        currency = repository.update(currency).orElseThrow(() -> new CouldNotUpdateEntity("Не удалось обновить описание валюты"));
        return mapper.toCurrencyResponse(currency);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Override
    public Currency parseToCurrency(String line) {
        String name = "", code = "", sign = "";
        try {
            String[] params = URLDecoder.decode(line, StandardCharsets.UTF_8.name()).split("&");
            for (String param : params) {
                if (param.contains("code")) {
                    code = param.substring(param.indexOf("=") + 1);
                } else if (param.contains("name")) {
                    name = param.substring(param.indexOf("=") + 1);
                } else if (param.contains("sign")) {
                    sign = param.substring(param.indexOf("=") + 1);
                }
            }

            if (code.isEmpty() || name.isEmpty() || sign.isEmpty()) {
                throw new InvalidParameterException("Отсутствует нужное поле формы");
            }
        } catch (UnsupportedEncodingException e) {
            throw new CouldNotSaveEntity(e.getMessage());
        }

        return new Currency(name, code, sign);
    }
}
