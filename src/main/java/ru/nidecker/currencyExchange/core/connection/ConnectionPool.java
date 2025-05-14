package ru.nidecker.currencyExchange.core.connection;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
    Connection getConnection();
    boolean releaseConnection(Connection connection);
    String getUrl();
    String getUser();
    String getPassword();
    default void shutdown() throws SQLException {
        throw new NotImplementedException();
    }
}
