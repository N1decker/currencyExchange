package ru.nidecker.currencyExchange.currency.impl;

import ru.nidecker.currencyExchange.currency.Currency;
import ru.nidecker.currencyExchange.currency.CurrencyRepository;

import java.util.Collections;
import java.util.List;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    @Override
    public List<Currency> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Currency findById(Integer id) {
        return null;
    }

    @Override
    public Currency create(Currency currency) {
        return null;
    }

    @Override
    public Currency update(Currency currency) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Удаление не реализовано");
    }
}
