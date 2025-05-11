package ru.nidecker.currencyExchange.currency;

import ru.nidecker.currencyExchange.core.repository.CRUDRepository;

public interface CurrencyRepository extends CRUDRepository<Currency> {
    Currency findByCode(String code);
}

