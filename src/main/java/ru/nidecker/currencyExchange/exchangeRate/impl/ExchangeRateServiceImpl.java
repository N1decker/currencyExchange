package ru.nidecker.currencyExchange.exchangeRate.impl;

import ru.nidecker.currencyExchange.exchangeRate.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateService;

import java.util.Collections;
import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Override
    public List<ExchangeRate> findAll() {
        return Collections.emptyList();
    }

    @Override
    public ExchangeRate findById(Integer id) {
        return null;
    }

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public ExchangeRate update(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
