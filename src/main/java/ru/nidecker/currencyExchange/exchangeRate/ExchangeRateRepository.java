package ru.nidecker.currencyExchange.exchangeRate;

import ru.nidecker.currencyExchange.core.repository.CRUDRepository;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateRepository extends CRUDRepository<ExchangeRate> {
    Optional<ExchangeRate> findByPair(String code1 , String code2);

    Optional<ExchangeRate> create(ExchangeRateRequest exchangeRateRequest);

    Optional<ExchangeRate> update(String code1, String code2, BigDecimal rate);
}
