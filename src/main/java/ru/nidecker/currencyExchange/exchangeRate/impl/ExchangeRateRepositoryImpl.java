package ru.nidecker.currencyExchange.exchangeRate.impl;

import ru.nidecker.currencyExchange.core.connection.BasicConnectionPool;
import ru.nidecker.currencyExchange.core.connection.ConnectionPool;
import ru.nidecker.currencyExchange.currency.Currency;
import ru.nidecker.currencyExchange.exceptions.DatabaseException;
import ru.nidecker.currencyExchange.exceptions.NotFoundException;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRate;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    private final ConnectionPool connectionPool = BasicConnectionPool.INSTANCE;

    @Override
    public List<ExchangeRate> findAll() {
        Connection connection = connectionPool.getConnection();
        List<ExchangeRate> list = new ArrayList<>();
        try {
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
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return list;
    }

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRate> findByPair(String code1, String code2) {
        Connection connection = connectionPool.getConnection();
        Optional<ExchangeRate> rate;
        try {
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
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return rate;
    }

    @Override
    public Optional<ExchangeRate> create(ExchangeRate exchangeRate) {
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
        return Optional.empty();
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Удаление не реализовано");
    }
}
