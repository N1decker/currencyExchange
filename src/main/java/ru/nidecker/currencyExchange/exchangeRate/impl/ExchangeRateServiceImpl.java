package ru.nidecker.currencyExchange.exchangeRate.impl;

import ru.nidecker.currencyExchange.exchangeRate.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateResponse;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateService;

import java.util.Collections;
import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Override
    public List<ExchangeRateResponse> findAll() {
        return Collections.emptyList();
    }

    @Override
    public ExchangeRateResponse findById(Integer id) {
        return null;
    }

    @Override
    public ExchangeRateResponse create(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public ExchangeRateResponse update(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
