package ru.nidecker.currencyExchange.exchangeRate.impl;

import ru.nidecker.currencyExchange.exchangeRate.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateRepository;

import java.util.Collections;
import java.util.List;

public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
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
        throw new UnsupportedOperationException("Удаление не реализовано");
    }
}
