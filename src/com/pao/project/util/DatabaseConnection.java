package com.pao.project.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url;
    private final String user;
    private final String password;

    private DatabaseConnection() {
        try {
            Properties properties = new Properties();
            try (InputStream input = getClass().getResourceAsStream("/com/pao/project/resources/db.properties")) {
                if (input == null) {
                    throw new RuntimeException("Nu s-a putut găsi db.properties în folderul de resurse!");
                }
                properties.load(input);
            }

            String driver = properties.getProperty("db.driver");
            if (driver != null && !driver.isBlank()) {
                Class.forName(driver);
            }

            this.url = properties.getProperty("db.url");
            this.user = properties.getProperty("db.user", "");
            this.password = properties.getProperty("db.password", "");

            ensureSqliteDirectoryExists(this.url);
            initializeSchema();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Eroare la încărcarea configurărilor bazei de date", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = user.isBlank()
                ? DriverManager.getConnection(url)
                : DriverManager.getConnection(url, user, password);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    private void ensureSqliteDirectoryExists(String jdbcUrl) throws IOException {
        if (!jdbcUrl.startsWith("jdbc:sqlite:")) {
            return;
        }
        String filePath = jdbcUrl.substring("jdbc:sqlite:".length());
        if (filePath.startsWith("//") || filePath.equals(":memory:")) {
            return;
        }
        Path dbPath = Paths.get(filePath);
        Path parent = dbPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private void initializeSchema() {
        try (InputStream input = getClass().getResourceAsStream("/com/pao/project/schema.sql")) {
            if (input == null) {
                throw new RuntimeException("Nu s-a putut găsi schema.sql!");
            }
            String schema = new String(input.readAllBytes());
            try (Connection connection = getConnection(); Statement stmt = connection.createStatement()) {
                for (String sql : schema.split(";")) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Eroare la inițializarea bazei de date SQLite.", e);
        }
    }
}
