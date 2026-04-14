package test.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.qameta.allure.Step;
import org.springframework.dao.EmptyResultDataAccessException;
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



    @Step("Проверка существования пользователя")
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM `user` WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
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

    @Step("Получение информации по Музею по названию")
    public Map<String, Object> getMuseumByName(String title) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getMuseumByTitle only works with GATEWAY database");
        }
        String sql = """
                SELECT id, title, description, geo, photo
                FROM museum 
                WHERE title = ?
                """;
        try {
            return jdbcTemplate.queryForMap(sql, title);
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Museum not found: " + title);
            return Map.of();
        } catch (Exception e) {
            System.err.println("Error getting museum by title: " + e.getMessage());
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

    @Step("Получение информации по Художнику по имени")
    public Map<String, Object> getArtistByN(String name) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getArtistByName only works with GATEWAY database");
        }
        String sql = """
                SELECT id, name, biography, photo
                FROM artist 
                WHERE name = ?
                """;
        try {
            return jdbcTemplate.queryForMap(sql, name);
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Artist not found: " + name);
            return Map.of();
        } catch (Exception e) {
            System.err.println("Error getting artist by name: " + e.getMessage());
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