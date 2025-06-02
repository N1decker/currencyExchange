package ru.nidecker.currencyExchange.core;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static HikariDataSource dataSource;

    public static void setDataSource(DataSource ds) {
        dataSource = (HikariDataSource) ds;
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not initialized!");
        }
        return dataSource.getConnection();
    }

    public static void shutdown() {
        dataSource.close();
    }
}
