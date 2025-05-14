package ru.nidecker.currencyExchange.currency.impl;

import org.sqlite.SQLiteErrorCode;
import ru.nidecker.currencyExchange.core.connection.BasicConnectionPool;
import ru.nidecker.currencyExchange.core.connection.ConnectionPool;
import ru.nidecker.currencyExchange.currency.Currency;
import ru.nidecker.currencyExchange.currency.CurrencyRepository;
import ru.nidecker.currencyExchange.exceptions.CouldNotSaveEntity;
import ru.nidecker.currencyExchange.exceptions.DuplicationException;
import ru.nidecker.currencyExchange.exceptions.MethodNotImplemented;
import ru.nidecker.currencyExchange.exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<Currency> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        Connection connection = connectionPool.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from currencies where lower(code) like lower(?)");
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(
                        new Currency(
                                resultSet.getInt("id"),
                                resultSet.getString("fullName"),
                                resultSet.getString("code"),
                                resultSet.getString("sign")
                        )
                );
            } else throw new NotFoundException("Валюта не найдена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Currency> create(Currency currency) {
        Connection connection = connectionPool.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into currencies (FullName, Code, Sign) values (?, ?, ?) ");
            preparedStatement.setString(1, currency.getName());
            preparedStatement.setString(2, currency.getCode());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                throw new DuplicationException("Валюта с таким кодом уже существует");
            } else throw new CouldNotSaveEntity(e.getLocalizedMessage());
        }
        return findByCode(currency.getCode());
    }

    @Override
    public Optional<Currency> update(Currency currency) {
        return Optional.empty();
    }

    @Override
    public void delete(Integer id) {
        throw new MethodNotImplemented("Удаление не реализовано");
    }
}
