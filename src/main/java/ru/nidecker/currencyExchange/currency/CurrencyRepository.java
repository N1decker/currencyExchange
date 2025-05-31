package ru.nidecker.currencyExchange.currency;

import ru.nidecker.currencyExchange.core.repository.CRUDRepository;
import ru.nidecker.currencyExchange.currency.entity.Currency;

import java.util.Optional;

public interface CurrencyRepository extends CRUDRepository<Currency> {
    Optional<Currency> findByCode(String code);
}

