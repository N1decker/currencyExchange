package ru.nidecker.currencyExchange.exchangeRate.impl;

import org.sqlite.SQLiteErrorCode;
import ru.nidecker.currencyExchange.core.Database;
import ru.nidecker.currencyExchange.currency.CurrencyRepository;
import ru.nidecker.currencyExchange.currency.entity.Currency;
import ru.nidecker.currencyExchange.currency.impl.CurrencyRepositoryImpl;
import ru.nidecker.currencyExchange.exceptions.*;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateRepository;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRateRequest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    private final CurrencyRepository currencyRepository;

    public ExchangeRateRepositoryImpl() {
        this.currencyRepository = new CurrencyRepositoryImpl();
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> list = new ArrayList<>();
        try(Connection connection = Database.getConnection()) {
            ResultSet resultSet = connection.prepareStatement(
                    "select e.id, e.rate, " +
                        "base.id baseId, base.code baseCode, base.fullname baseName, base.sign baseSign, " +
                        "target.id targetId, target.code targetCode, target.fullname targetName, target.sign targetSign " +
                        "from exchangeRates e " +
                        "left join currencies base on base.id = e.BaseCurrencyId " +
                        "left join currencies target on target.id = e.TargetCurrencyId")
                    .executeQuery();

            while (resultSet.next()) {
                list.add(new ExchangeRate(
                    resultSet.getInt("id"),
                    new Currency(
                            resultSet.getInt("baseId"),
                            resultSet.getString("baseName"),
                            resultSet.getString("baseCode"),
                            resultSet.getString("baseSign")
                        ),
                    new Currency(
                            resultSet.getInt("targetId"),
                            resultSet.getString("targetName"),
                            resultSet.getString("targetCode"),
                            resultSet.getString("targetSign")
                        ),
                    resultSet.getBigDecimal("rate")
                    )
                );
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return list;
    }

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRate> findByPair(String code1, String code2) {
        Optional<ExchangeRate> rate;
        try(Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "select e.id, e.rate, " +
                    "base.id baseId, base.code baseCode, base.fullname baseName, base.sign baseSign, " +
                    "target.id targetId, target.code targetCode, target.fullname targetName, target.sign targetSign " +
                    "from exchangeRates e " +
                    "left join currencies base on base.id = e.BaseCurrencyId " +
                    "left join currencies target on target.id = e.TargetCurrencyId " +
                    "where lower(base.Code) like lower(?) and lower(target.Code) like lower(?)");
            preparedStatement.setString(1, code1);
            preparedStatement.setString(2, code2);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                rate = Optional.of(
                        new ExchangeRate(
                                resultSet.getInt("id"),
                                new Currency(
                                        resultSet.getInt("baseId"),
                                        resultSet.getString("baseName"),
                                        resultSet.getString("baseCode"),
                                        resultSet.getString("baseSign")
                                ),
                                new Currency(
                                        resultSet.getInt("targetId"),
                                        resultSet.getString("targetName"),
                                        resultSet.getString("targetCode"),
                                        resultSet.getString("targetSign")
                                ),
                                resultSet.getBigDecimal("rate")
                        )
                );
            } else throw new NotFoundException(String.format("Пары %s/%s не найдено", code1, code2));
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return rate;
    }

    @Override
    public Optional<ExchangeRate> create(ExchangeRateRequest exchangeRateRequest) {
        Optional<Currency> baseCurrency;
        Optional<Currency> targetCurrency;
        try(Connection connection = Database.getConnection()) {
            baseCurrency = currencyRepository.findByCode(exchangeRateRequest.getBaseCurrencyCode());
            targetCurrency = currencyRepository.findByCode(exchangeRateRequest.getTargetCurrencyCode());

            if (!baseCurrency.isPresent() || !targetCurrency.isPresent()) {
                throw new NotFoundException("Одна (или обе) валюта из валютной пары не существует в БД");
            }

            try {
                Optional<ExchangeRate> byPair = findByPair(exchangeRateRequest.getBaseCurrencyCode(), exchangeRateRequest.getTargetCurrencyCode());
                if (byPair.isPresent()) throw new DuplicationException("Валютная пара с таким кодом уже существует");
            } catch (NotFoundException ignored) {
            }

            PreparedStatement preparedStatement = connection.prepareStatement("insert into exchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) values (?, ?, ?)");
            preparedStatement.setInt(1, baseCurrency.get().getId());
            preparedStatement.setInt(2, targetCurrency.get().getId());
            preparedStatement.setBigDecimal(3, exchangeRateRequest.getRate());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                throw new DuplicationException("Валютная пара с таким кодом уже существует");
            } else throw new CouldNotSaveEntity(e.getLocalizedMessage());
        }

        return findByPair(baseCurrency.get().getCode(), targetCurrency.get().getCode());
    }

    @Override
    public Optional<ExchangeRate> update(String code1, String code2, BigDecimal rate) {
        Optional<Currency> baseCurrency;
        Optional<Currency> targetCurrency;
        try(Connection connection = Database.getConnection()) {
            baseCurrency = currencyRepository.findByCode(code1);
            targetCurrency = currencyRepository.findByCode(code2);

            if (!baseCurrency.isPresent() || !targetCurrency.isPresent()) {
                throw new NotFoundException("Одна (или обе) валюта из валютной пары не существует в БД");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("update exchangeRates set rate = ? where BaseCurrencyId = ? and TargetCurrencyId = ?");
            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setInt(2, baseCurrency.get().getId());
            preparedStatement.setInt(3, targetCurrency.get().getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return findByPair(baseCurrency.get().getCode(), targetCurrency.get().getCode());
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        throw new MethodNotImplemented("Обновление обменного курса не реализовано");
    }

    @Override
    public Optional<ExchangeRate> create(ExchangeRate exchangeRate) {
        throw new MethodNotImplemented("Создание обменного курса не реализовано");
    }

    @Override
    public void delete(Integer id) {
        throw new MethodNotImplemented("Удаление не реализовано");
    }
}
