package test.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import lombok.Getter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

    private final JdbcTemplate jdbcTemplate;
    @Getter
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
            logSql(createUserTable);
            jdbcTemplate.execute(createUserTable);
            logSql(createAuthorityTable);
            jdbcTemplate.execute(createAuthorityTable);
            System.out.println("Auth tables created successfully");
        } catch (Exception e) {
            System.out.println("Auth tables already exist: " + e.getMessage());
        }
    }

    @Step("Создание таблиц Gateway")
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
            logSql(createUserTable);
            jdbcTemplate.execute(createUserTable);
            logSql(createCountryTable);
            jdbcTemplate.execute(createCountryTable);
            logSql(createMuseumTable);
            jdbcTemplate.execute(createMuseumTable);
            logSql(createArtistTable);
            jdbcTemplate.execute(createArtistTable);
            logSql(createPaintingTable);
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
                String sql1 = "SET FOREIGN_KEY_CHECKS = 0";
                String sql2 = "TRUNCATE TABLE authority";
                String sql3 = "TRUNCATE TABLE `user`";
                String sql4 = "SET FOREIGN_KEY_CHECKS = 1";

                logSql(sql1);
                jdbcTemplate.execute(sql1);
                logSql(sql2);
                jdbcTemplate.execute(sql2);
                logSql(sql3);
                jdbcTemplate.execute(sql3);
                logSql(sql4);
                jdbcTemplate.execute(sql4);
            } else if (databaseType == DatabaseType.GATEWAY) {
                String sql1 = "SET FOREIGN_KEY_CHECKS = 0";
                String sql2 = "TRUNCATE TABLE painting";
                String sql3 = "TRUNCATE TABLE museum";
                String sql4 = "TRUNCATE TABLE artist";
                String sql5 = "TRUNCATE TABLE country";
                String sql6 = "TRUNCATE TABLE `user`";
                String sql7 = "SET FOREIGN_KEY_CHECKS = 1";

                logSql(sql1);
                jdbcTemplate.execute(sql1);
                logSql(sql2);
                jdbcTemplate.execute(sql2);
                logSql(sql3);
                jdbcTemplate.execute(sql3);
                logSql(sql4);
                jdbcTemplate.execute(sql4);
                logSql(sql5);
                jdbcTemplate.execute(sql5);
                logSql(sql6);
                jdbcTemplate.execute(sql6);
                logSql(sql7);
                jdbcTemplate.execute(sql7);
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
        logSql(sql, username);
        return jdbcTemplate.queryForMap(sql, username);
    }

    @Step("Проверка существования пользователя")
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM `user` WHERE username = ?";
        logSql(sql, username);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Step("Получение информации по Художнику по имени (старый метод)")
    public Map<String, Object> getArtistByName(String name) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getArtistByName only works with GATEWAY database");
        }
        String sql = """
                    SELECT biography
                    FROM artist 
                    WHERE name = ?
                """;
        logSql(sql, name);
        try {
            return jdbcTemplate.queryForMap(sql, name);
        } catch (Exception e) {
            return Map.of();
        }
    }

    @Step("Получение информации по Музею по названию (полная информация)")
    public Map<String, Object> getMuseumByName(String title) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getMuseumByName only works with GATEWAY database");
        }
        String sql = """
                SELECT BIN_TO_UUID(id) as id, title, description, city, photo
                FROM museum 
                WHERE title = ?
                """;
        logSql(sql, title);
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
                SELECT BIN_TO_UUID(id) as id, name, biography, photo
                FROM artist
                ORDER BY name
                """;
        logSql(sql);
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.err.println("Error getting all artists: " + e.getMessage());
            return List.of();
        }
    }

    @Step("Получение описания музея по названию")
    public Map<String, Object> getMuseumByTitle(String title) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getMuseumByTitle only works with GATEWAY database");
        }
        String sql = """
                    SELECT description
                    FROM museum 
                    WHERE title = ?
                """;
        logSql(sql, title);
        try {
            return jdbcTemplate.queryForMap(sql, title);
        } catch (Exception e) {
            return Map.of();
        }
    }

    @Step("Получение информации по Художнику по имени (полная информация)")
    public Map<String, Object> getArtistByN(String name) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getArtistByN only works with GATEWAY database");
        }
        String sql = """
                SELECT BIN_TO_UUID(id) as id, name, biography, photo
                FROM artist 
                WHERE name = ?
                """;
        logSql(sql, name);
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
            throw new UnsupportedOperationException("getPaintingByTitle only works with GATEWAY database");
        }
        String sql = """
                    SELECT description
                    FROM painting 
                    WHERE title = ?
                """;
        logSql(sql, title);
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
        String sql = "DELETE FROM painting WHERE id = UUID_TO_BIN(?)";
        logSql(sql, id);
        jdbcTemplate.update(sql, id);
        System.out.println("✅ Painting deleted with id: " + id);
    }

    @Step("Удаление художника по ID")
    public void deleteArtistById(String id) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("deleteArtistById only works with GATEWAY database");
        }
        String sql = "DELETE FROM artist WHERE id = UUID_TO_BIN(?)";
        logSql(sql, id);
        jdbcTemplate.update(sql, id);
        System.out.println("✅ Artist deleted with id: " + id);
    }

    @Step("Удаление музея по ID")
    public void deleteMuseumById(String id) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("deleteMuseumById only works with GATEWAY database");
        }
        String sql = "DELETE FROM museum WHERE id = UUID_TO_BIN(?)";
        logSql(sql, id);
        jdbcTemplate.update(sql, id);
        System.out.println("✅ Museum deleted with id: " + id);
    }

    @Step("Получение всех музеев")
    public List<Map<String, Object>> getAllMuseums() {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("getAllMuseums only works with GATEWAY database");
        }
        String sql = "SELECT BIN_TO_UUID(id) as id, title, description, city FROM museum";
        logSql(sql);
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            System.err.println("Error getting all museums: " + e.getMessage());
            return List.of();
        }
    }

    @Step("Получение JdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Step("Логирование SQL запроса")
    private void logSql(String sql, Object... params) {
        String formattedSql = sql;
        for (Object param : params) {
            if (param != null) {
                formattedSql = formattedSql.replaceFirst("\\?", "'" + param + "'");
            } else {
                formattedSql = formattedSql.replaceFirst("\\?", "NULL");
            }
        }

        System.out.println("========================================");
        System.out.println("Database: " + databaseType.getDatabaseName());
        System.out.println("SQL Query: " + formattedSql);
        System.out.println("========================================");

        attachSqlLog(formattedSql);
    }

    @Attachment(value = "SQL Query", type = "text/html")
    public static String attachSqlLog(String sql) {
        return "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset = UTF-8\">\n" +
                "    <link type=\"text/css\" href=\"https://yandex.st/highlightjs/8.0/styles/github.min.css\" rel=\"stylesheet\"/>\n" +
                "    <script type=\"text/javascript\" src=\"https://yandex.st/highlightjs/8.0/highlight.min.js\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"https://yandex.st/highlightjs/8.0/languages/sql.min.js\"></script>\n" +
                "    <script type=\"text/javascript\">hljs.initHighlightingOnLoad();</script>\n" +
                "    <style>pre { white-space: pre-wrap; }</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h5>SQL Query</h5>\n" +
                "<div>\n" +
                "    <pre><code>" + sql + "</code></pre>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }
}