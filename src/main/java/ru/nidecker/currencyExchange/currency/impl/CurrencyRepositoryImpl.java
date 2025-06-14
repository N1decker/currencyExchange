package ru.nidecker.currencyExchange.currency.impl;

import org.sqlite.SQLiteErrorCode;
import ru.nidecker.currencyExchange.core.Database;
import ru.nidecker.currencyExchange.currency.CurrencyRepository;
import ru.nidecker.currencyExchange.currency.entity.Currency;
import ru.nidecker.currencyExchange.exceptions.CouldNotSaveEntity;
import ru.nidecker.currencyExchange.exceptions.DuplicationException;
import ru.nidecker.currencyExchange.exceptions.MethodNotImplemented;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepositoryImpl implements CurrencyRepository {

    @Override
    public List<Currency> findAll() {
        List<Currency> list = new ArrayList<>();
        try(Connection connection = Database.getConnection()) {
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
        }

        return list;
    }

    @Override
    public Optional<Currency> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        try(Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from currencies where lower(Code) like lower(?)");
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Currency> create(Currency currency) {
        Optional<Currency> byCode = findByCode(currency.getCode());
        if (byCode.isPresent())
            throw new DuplicationException("Валюта с таким кодом уже существует");

        try(Connection connection = Database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into currencies (FullName, Code, Sign) values (?, upper(?), ?)");
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
