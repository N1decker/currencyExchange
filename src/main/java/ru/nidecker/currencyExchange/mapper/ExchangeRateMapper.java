package ru.nidecker.currencyExchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateResponse;

import java.util.List;

@Mapper
public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateResponse toExchangeRateResponse(ExchangeRate rate);
    List<ExchangeRateResponse> toExchangeRateResponseList(List<ExchangeRate> rates);
}
