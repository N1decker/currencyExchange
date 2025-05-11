package ru.nidecker.currencyExchange.currency;

import ru.nidecker.currencyExchange.core.service.Service;

public interface CurrencyService extends Service<Currency, CurrencyResponse> {
    CurrencyResponse findByCode(String code);
}
