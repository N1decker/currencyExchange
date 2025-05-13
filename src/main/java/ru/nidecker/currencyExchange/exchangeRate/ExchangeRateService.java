package ru.nidecker.currencyExchange.exchangeRate;

import ru.nidecker.currencyExchange.core.service.Service;

public interface ExchangeRateService extends Service<ExchangeRate, ExchangeRateResponse> {
    ExchangeRateResponse findByPair(String code1, String code2);
}
