package test.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.qameta.allure.Step;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseHelper {

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseType databaseType;


    public DatabaseHelper(DatabaseType databaseType) {
        this.databaseType = databaseType;
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseType.getJdbcUrl());
        config.setUsername("rococo_admin");
        config.setPassword("rococo_admin123");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(30000);

        DataSource dataSource = new HikariDataSource(config);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        initDatabase();
        System.out.println("✅ Connected to database: " + databaseType.getDatabaseName());
    }

    @Step("инициализация базы данных")
    private void initDatabase() {
        createTables();
    }

    @Step("Создание таблицы")
    private void createTables() {
        if (databaseType == DatabaseType.AUTH) {
            createAuthTables();
        } else if (databaseType == DatabaseType.GATEWAY) {
            createGatewayTables();
        }
    }

    @Step("Создание таблицы авторизации")
    private void createAuthTables() {
        String createUserTable = """
                    CREATE TABLE IF NOT EXISTS `user` (
                        id BINARY(16) UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), true)),
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        enabled BOOLEAN NOT NULL,
                        account_non_expired BOOLEAN NOT NULL,
                        account_non_locked BOOLEAN NOT NULL,
                        credentials_non_expired BOOLEAN NOT NULL,
                        PRIMARY KEY (id, username)
                    )
                """;

        String createAuthorityTable = """
                    CREATE TABLE IF NOT EXISTS `authority` (
                        id BINARY(16) UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), true)),
                        user_id BINARY(16) NOT NULL,
                        authority ENUM('read', 'write') NOT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_authorities_users 
                            FOREIGN KEY (user_id) REFERENCES `user`(id) 
                            ON DELETE CASCADE
                    )
                """;

        try {
            jdbcTemplate.execute(createUserTable);
            jdbcTemplate.execute(createAuthorityTable);
            System.out.println("Auth tables created successfully");
        } catch (Exception e) {
            System.out.println("Auth tables already exist: " + e.getMessage());
        }
    }

    private void createGatewayTables() {
        String createUserTable = """
                    CREATE TABLE IF NOT EXISTS `user` (
                        id BINARY(16) UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), true)),
                        username VARCHAR(50) UNIQUE NOT NULL,
                        firstname VARCHAR(255),
                        lastname VARCHAR(255),
                        avatar LONGBLOB,
                        PRIMARY KEY (id)
                    )
                """;

        String createCountryTable = """
                    CREATE TABLE IF NOT EXISTS `country` (
                        id BINARY(16) UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), true)),
                        name VARCHAR(255) UNIQUE NOT NULL,
                        PRIMARY KEY (id)
                    )
                """;

        String createMuseumTable = """
                    CREATE TABLE IF NOT EXISTS `museum` (
                        id BINARY(16) UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), true)),
                        title VARCHAR(255) UNIQUE NOT NULL,
                        description VARCHAR(1000),
                        city VARCHAR(255),
                        photo LONGBLOB,
                        country_id BINARY(16) NOT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_country_id FOREIGN KEY (country_id) REFERENCES `country`(id)
                    )
                """;

        String createArtistTable = """
                    CREATE TABLE IF NOT EXISTS `artist` (
                        id BINARY(16) UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), true)),
                        name VARCHAR(255) UNIQUE NOT NULL,
                        biography VARCHAR(2000) NOT NULL,
                        photo LONGBLOB,
                        PRIMARY KEY (id)
                    )
                """;

        String createPaintingTable = """
                    CREATE TABLE IF NOT EXISTS `painting` (
                        id BINARY(16) UNIQUE NOT NULL DEFAULT (UUID_TO_BIN(UUID(), true)),
                        title VARCHAR(255) NOT NULL,
                        description VARCHAR(1000),
                        artist_id BINARY(16) NOT NULL,
                        museum_id BINARY(16),
                        content LONGBLOB,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_artist_id FOREIGN KEY (artist_id) REFERENCES `artist`(id),
                        CONSTRAINT fk_museum_id FOREIGN KEY (museum_id) REFERENCES `museum`(id)
                    )
                """;

        try {
            jdbcTemplate.execute(createUserTable);
            jdbcTemplate.execute(createCountryTable);
            jdbcTemplate.execute(createMuseumTable);
            jdbcTemplate.execute(createArtistTable);
            jdbcTemplate.execute(createPaintingTable);
            System.out.println("Gateway tables created successfully");
        } catch (Exception e) {
            System.out.println("Gateway tables already exist: " + e.getMessage());
        }
    }

    @Step("Очистка БД")
    public void clearDatabase() {
        try {
            if (databaseType == DatabaseType.AUTH) {
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
                jdbcTemplate.execute("TRUNCATE TABLE authority");
                jdbcTemplate.execute("TRUNCATE TABLE `user`");
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
            } else if (databaseType == DatabaseType.GATEWAY) {
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
                jdbcTemplate.execute("TRUNCATE TABLE painting");
                jdbcTemplate.execute("TRUNCATE TABLE museum");
                jdbcTemplate.execute("TRUNCATE TABLE artist");
                jdbcTemplate.execute("TRUNCATE TABLE country");
                jdbcTemplate.execute("TRUNCATE TABLE `user`");
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
            }
            System.out.println("Database cleared successfully: " + databaseType.getDatabaseName());
        } catch (Exception e) {
            System.out.println("Error clearing database: " + e.getMessage());
        }
    }

    @Step("Вставка пользователя")
    public UUID insertUser(String username, String password, boolean enabled) {
        UUID userId = UUID.randomUUID();

        if (databaseType == DatabaseType.AUTH) {
            String sql = """
                        INSERT INTO `user` (id, username, password, enabled, 
                            account_non_expired, account_non_locked, credentials_non_expired)
                        VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?)
                    """;
            jdbcTemplate.update(sql, userId, username, password, enabled, enabled, enabled, enabled);
        } else if (databaseType == DatabaseType.GATEWAY) {
            String sql = """
                        INSERT INTO `user` (id, username, firstname, lastname)
                        VALUES (UUID_TO_BIN(?), ?, ?, ?)
                    """;
            jdbcTemplate.update(sql, userId, username, "", "");
        }

        System.out.println("User inserted into " + databaseType.getDatabaseName() + " with ID: " + userId);
        return userId;
    }

    @Step("Вставка Authority (только для AUTH)")
    public void insertAuthority(UUID userId, String authority) {
        if (databaseType != DatabaseType.AUTH) {
            throw new UnsupportedOperationException("insertAuthority only works with AUTH database");
        }
        String sql = "INSERT INTO authority (user_id, authority) VALUES (UUID_TO_BIN(?), ?)";
        jdbcTemplate.update(sql, userId, authority);
        System.out.println("Authority inserted for user: " + userId);
    }

    @Step("Удаление пользователя по ID")
    public void deleteUserById(UUID userId) {
        String sql = "DELETE FROM `user` WHERE id = UUID_TO_BIN(?)";
        int rows = jdbcTemplate.update(sql, userId);
        if (rows > 0) {
            System.out.println("User deleted with ID: " + userId);
        } else {
            System.out.println("User not found with ID: " + userId);
        }
    }

    @Step("Удаление пользователя по имени")
    public void deleteUserByUsername(String username) {
        String sql = "DELETE FROM `user` WHERE username = ?";
        int rows = jdbcTemplate.update(sql, username);
        if (rows > 0) {
            System.out.println("User deleted from " + databaseType.getDatabaseName() + ": " + username);
        } else {
            System.out.println("User not found in " + databaseType.getDatabaseName() + ": " + username);
        }
    }

    @Step("Получение пользователя по имени")
    public Map<String, Object> getUserByUsername(String username) {
        String sql;
        if (databaseType == DatabaseType.AUTH) {
            sql = """
                        SELECT BIN_TO_UUID(id) as id, username, password, enabled, 
                               account_non_expired, account_non_locked, credentials_non_expired
                        FROM `user` 
                        WHERE username = ?
                    """;
        } else {
            sql = """
                        SELECT BIN_TO_UUID(id) as id, username, firstname, lastname
                        FROM `user` 
                        WHERE username = ?
                    """;
        }
        return jdbcTemplate.queryForMap(sql, username);
    }

    @Step("Отключение пользователя (только для AUTH)")
    public void disableUser(UUID userId) {
        if (databaseType != DatabaseType.AUTH) {
            throw new UnsupportedOperationException("disableUser only works with AUTH database");
        }
        String sql = "UPDATE `user` SET enabled = false WHERE id = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql, userId);
        System.out.println("User disabled with ID: " + userId);
    }

    @Step("Включение пользователя (только для AUTH)")
    public void enableUser(UUID userId) {
        if (databaseType != DatabaseType.AUTH) {
            throw new UnsupportedOperationException("enableUser only works with AUTH database");
        }
        String sql = "UPDATE `user` SET enabled = true WHERE id = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql, userId);
        System.out.println("User enabled with ID: " + userId);
    }

    @Step("Проверка существования пользователя")
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM `user` WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public List<String> getUserAuthorities(UUID userId) {
        if (databaseType != DatabaseType.AUTH) {
            throw new UnsupportedOperationException("getUserAuthorities only works with AUTH database");
        }
        String sql = "SELECT authority FROM authority WHERE user_id = UUID_TO_BIN(?)";
        return jdbcTemplate.queryForList(sql, String.class, userId);
    }


    public void updateUserFirstAndLastName(String username, String firstname, String lastname) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("updateUserFirstAndLastName only works with GATEWAY database");
        }
        String sql = "UPDATE `user` SET firstname = ?, lastname = ? WHERE username = ?";
        jdbcTemplate.update(sql, firstname, lastname, username);
        System.out.println("User profile updated in gateway: " + username + " -> " + firstname + " " + lastname);
    }

    @Step("Получение профиля пользователя (только для GATEWAY)")
    public Map<String, Object> getUserProfile(String username) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getUserProfile only works with GATEWAY database");
        }
        String sql = """
                    SELECT firstname, lastname, avatar 
                    FROM `user` 
                    WHERE username = ?
                """;
        try {
            return jdbcTemplate.queryForMap(sql, username);
        } catch (Exception e) {
            return Map.of();
        }
    }

    @Step("Получение информации по Художнику")
    public Map<String, Object> getArtistByName(String name) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getArtistByName only works with GATEWAY database");
        }
        String sql = """
                    SELECT biography
                     FROM `artist` 
                    WHERE `name` = ?
                """;
        try {
            return jdbcTemplate.queryForMap(sql, name);
        } catch (Exception e) {
            return Map.of();
        }
    }

    @Step("Получение информации по всем Художникам")
    public List<Map<String, Object>> getAllArtists() {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getAllArtists only works with GATEWAY database");
        }
        String sql = """
                SELECT id, name, biography, photo
                FROM artist
                ORDER BY name
                """;
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.err.println("Error getting all artists: " + e.getMessage());
            return List.of();
        }
    }

    @Step("Получение информации по Музею")
    public Map<String, Object> getMuseumByTitle(String title) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getArtistByName only works with GATEWAY database");
        }
        String sql = """
                    SELECT description
                     FROM `museum` 
                    WHERE `title` = ?
                """;
        try {
            return jdbcTemplate.queryForMap(sql, title);
        } catch (Exception e) {
            return Map.of();
        }
    }

    @Step("Получение информации по Картине")
    public Map<String, Object> getPaintingByTitle(String title) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getArtistByName only works with GATEWAY database");
        }
        String sql = """
                    SELECT description
                     FROM `painting` 
                    WHERE `title` = ?
                """;
        try {
            return jdbcTemplate.queryForMap(sql, title);
        } catch (Exception e) {
            return Map.of();
        }
    }

    @Step("Удаление картины по ID")
    public void deletePaintingById(String id) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("deletePaintingById only works with GATEWAY database");
        }
        String sql = "DELETE FROM painting WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Step("Удаление художника по ID")
    public void deleteArtistById(String id) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("deleteArtistById only works with GATEWAY database");
        }
        String sql = "DELETE FROM artist WHERE id = ?";
        jdbcTemplate.update(sql, id);
        System.out.println("✅ Artist deleted with id: " + id);
    }

    @Step("Удаление музея по ID")
    public void deleteMuseumById(String id) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("deleteMuseumById only works with GATEWAY database");
        }
        String sql = "DELETE FROM museum WHERE id = ?";
        jdbcTemplate.update(sql, id);
        System.out.println("✅ Museum deleted with id: " + id);
    }

    @Step("Получение JdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }


    public DatabaseType getDatabaseType() {
        return databaseType;
    }
}