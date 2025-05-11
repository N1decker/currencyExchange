package ru.nidecker.currencyExchange.currency.impl;

import ru.nidecker.currencyExchange.core.connection.BasicConnectionPool;
import ru.nidecker.currencyExchange.core.connection.ConnectionPool;
import ru.nidecker.currencyExchange.currency.Currency;
import ru.nidecker.currencyExchange.currency.CurrencyRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    private final ConnectionPool connectionPool = BasicConnectionPool.INSTANCE;

    @Override
    public List<Currency> findAll() {
        Connection connection = connectionPool.getConnection();
        List<Currency> list = new ArrayList<>();
        try {
            ResultSet resultSet = connection.prepareStatement("select * from currencies").executeQuery();
            while (resultSet.next()) {
                list.add(
                        new Currency(
                                resultSet.getInt("id"),
                                resultSet.getString("fullName"),
                                resultSet.getString("code"),
                                resultSet.getString("sign")
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return list;
    }

    @Override
    public Currency findById(Integer id) {
        return null;
    }

    @Override
    public Currency findByCode(String code) {
        return null;
    }

    @Override
    public Currency create(Currency currency) {
        return null;
    }

    @Override
    public Currency update(Currency currency) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Удаление не реализовано");
    }
}
