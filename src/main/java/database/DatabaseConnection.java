package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String DEFAULT_URL =
            "jdbc:sqlserver://localhost:1433;databaseName=FoodDeliveryDB;" +
            "encrypt=true;trustServerCertificate=true";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
        String username = requireEnvironmentVariable("DB_USER");
        String password = requireEnvironmentVariable("DB_PASSWORD");
        return DriverManager.getConnection(url, username, password);
    }

    private static String requireEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required environment variable: " + name +
                    ". See .env.example and README.md for setup instructions."
            );
        }
        return value;
    }
}
