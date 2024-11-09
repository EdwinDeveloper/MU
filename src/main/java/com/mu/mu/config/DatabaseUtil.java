package com.mu.mu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = ApplicationConfig.getDbUrl();
    private static final String USER = ApplicationConfig.getDbUsername();
    private static final String PASSWORD = ApplicationConfig.getDbPassword();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
