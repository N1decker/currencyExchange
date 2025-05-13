package ru.nidecker.currencyExchange.exchangeRate.impl;

import ru.nidecker.currencyExchange.exceptions.CouldNotFetchData;
import ru.nidecker.currencyExchange.exceptions.CouldNotSaveEntity;
import ru.nidecker.currencyExchange.exceptions.CouldNotUpdateEntity;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateRepository;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateResponse;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateService;
import ru.nidecker.currencyExchange.mapper.ExchangeRateMapper;

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
    public ExchangeRateResponse create(ExchangeRate exchangeRate) {
        ExchangeRate rate = repository.create(exchangeRate).orElseThrow(() -> new CouldNotSaveEntity("Не удалось сохранить обменный курс"));
        return mapper.toExchangeRateResponse(rate);
    }

    @Override
    public ExchangeRateResponse update(ExchangeRate exchangeRate) {
        ExchangeRate rate = repository.update(exchangeRate).orElseThrow(() -> new CouldNotUpdateEntity("Не удалось обновить обменный курс"));
        return mapper.toExchangeRateResponse(rate);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
