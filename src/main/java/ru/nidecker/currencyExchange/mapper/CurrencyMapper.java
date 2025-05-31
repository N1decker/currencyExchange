package ru.nidecker.currencyExchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nidecker.currencyExchange.currency.entity.Currency;
import ru.nidecker.currencyExchange.currency.entity.CurrencyResponse;

import java.util.List;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyResponse toCurrencyResponse(Currency currency);
    List<CurrencyResponse> toCurrencyResponseList(List<Currency> currencies);
}
