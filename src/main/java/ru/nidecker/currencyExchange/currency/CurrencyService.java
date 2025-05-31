package ru.nidecker.currencyExchange.currency;

import ru.nidecker.currencyExchange.core.service.Service;
import ru.nidecker.currencyExchange.currency.entity.Currency;
import ru.nidecker.currencyExchange.currency.entity.CurrencyResponse;

public interface CurrencyService extends Service<Currency, CurrencyResponse> {
    CurrencyResponse findByCode(String code);
    Currency parseToCurrency(String line);
}
