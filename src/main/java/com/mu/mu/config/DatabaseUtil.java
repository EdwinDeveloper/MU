package com.mu.mu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = ApplicationConfig.getDbUrl();
    private static final String USER = ApplicationConfig.getDbUsername();
    private static final String PASSWORD = ApplicationConfig.getDbPassword();
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY = 1500;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                attempt++;
                System.out.println("Connection attempt " + attempt + " failed. Retrying in " + RETRY_DELAY / 1000 + " seconds...");
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new SQLException("Unable to connect to the database after " + MAX_RETRIES + " attempts : url " + URL, e);
                }
            }
        }
        throw new SQLException("Unable to connect to the database after " + MAX_RETRIES + " attempts : url" + URL);
    }
}
