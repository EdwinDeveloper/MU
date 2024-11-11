package com.mu.mu.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {

    private static final Properties properties = new Properties();
    private static final boolean isProduction = "production".equalsIgnoreCase(System.getenv("ENVIRONMENT"));

    static {
        if (!isProduction) {
            try (InputStream inputStream = ApplicationConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (inputStream != null) {
                    properties.load(inputStream);
                } else {
                    System.out.println("Properties file not found.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getProperty(String key) {
        return isProduction ? System.getenv(key) : properties.getProperty(key);
    }

    public static String getDbUrl() {
        // For production, build the URL using the environment variables
        if (isProduction) {
            String dbName = System.getenv("POSTGRES_DB");
            String dbHost = System.getenv("POSTGRES_HOST");
            String dbPort = System.getenv("POSTGRES_PORT");
            System.out.println("POSTGRES_HOST : " + dbHost);
            return String.format("jdbc:postgresql://%s:%s/%s", dbHost != null ? dbHost : "postgres", dbPort != null ? dbPort : "5432", dbName);
        }
        return getProperty("db.url");
    }

    public static String getDbUsername() {
        return isProduction ? System.getenv("DATABASE_USER") : getProperty("db.username");
    }

    public static String getDbPassword() {
        return isProduction ? System.getenv("DATABASE_PASSWORD") : getProperty("db.password");
    }

    public static int getServerPort() {
        String port = getProperty("server.port");
        return port != null ? Integer.parseInt(port) : 8086;
    }

}
