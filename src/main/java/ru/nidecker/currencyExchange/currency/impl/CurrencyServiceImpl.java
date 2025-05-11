package ru.nidecker.currencyExchange.currency.impl;

import ru.nidecker.currencyExchange.currency.Currency;
import ru.nidecker.currencyExchange.currency.CurrencyRepository;
import ru.nidecker.currencyExchange.currency.CurrencyResponse;
import ru.nidecker.currencyExchange.currency.CurrencyService;
import ru.nidecker.currencyExchange.mapper.CurrencyMapper;

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
        Currency currency = repository.findById(id);
        return mapper.toCurrencyResponse(currency);
    }

    @Override
    public CurrencyResponse findByCode(String code) {
        Currency currency = repository.findByCode(code);
        return mapper.toCurrencyResponse(currency);
    }

    @Override
    public CurrencyResponse create(Currency currency) {
        currency = repository.create(currency);
        return mapper.toCurrencyResponse(currency);
    }

    @Override
    public CurrencyResponse update(Currency currency) {
        currency = repository.update(currency);
        return mapper.toCurrencyResponse(currency);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
