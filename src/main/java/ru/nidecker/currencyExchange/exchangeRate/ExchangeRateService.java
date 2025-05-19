package ru.nidecker.currencyExchange.exchangeRate;

import ru.nidecker.currencyExchange.core.service.Service;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRateResponse;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeResponse;

import java.math.BigDecimal;

public interface ExchangeRateService extends Service<ExchangeRate, ExchangeRateResponse> {
    ExchangeRateResponse findByPair(String code1, String code2);

    ExchangeRateResponse create(ExchangeRateRequest exchangeRateRequest);

    ExchangeRateRequest parseRequestParams(String line);

    ExchangeRateResponse update(String code1, String code2, BigDecimal rate);

    ExchangeResponse calculateExchange(String code1, String code2, String amount);
}
