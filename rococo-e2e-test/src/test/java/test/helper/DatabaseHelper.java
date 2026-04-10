package test.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseHelper {

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseType databaseType;

    // Конструктор с указанием типа БД
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

    private void initDatabase() {
        createTables();
    }

    private void createTables() {
        if (databaseType == DatabaseType.AUTH) {
            createAuthTables();
        } else if (databaseType == DatabaseType.GATEWAY) {
            createGatewayTables();
        }
    }

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

    // Очистка БД
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

    // Вставка пользователя
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

    // Вставка Authority (только для AUTH)
    public void insertAuthority(UUID userId, String authority) {
        if (databaseType != DatabaseType.AUTH) {
            throw new UnsupportedOperationException("insertAuthority only works with AUTH database");
        }
        String sql = "INSERT INTO authority (user_id, authority) VALUES (UUID_TO_BIN(?), ?)";
        jdbcTemplate.update(sql, userId, authority);
        System.out.println("Authority inserted for user: " + userId);
    }

    // Удаление пользователя по ID
    public void deleteUserById(UUID userId) {
        String sql = "DELETE FROM `user` WHERE id = UUID_TO_BIN(?)";
        int rows = jdbcTemplate.update(sql, userId);
        if (rows > 0) {
            System.out.println("User deleted with ID: " + userId);
        } else {
            System.out.println("User not found with ID: " + userId);
        }
    }

    // Удаление пользователя по имени
    public void deleteUserByUsername(String username) {
        String sql = "DELETE FROM `user` WHERE username = ?";
        int rows = jdbcTemplate.update(sql, username);
        if (rows > 0) {
            System.out.println("User deleted from " + databaseType.getDatabaseName() + ": " + username);
        } else {
            System.out.println("User not found in " + databaseType.getDatabaseName() + ": " + username);
        }
    }

    // Получение пользователя по имени
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

    // Отключение пользователя (только для AUTH)
    public void disableUser(UUID userId) {
        if (databaseType != DatabaseType.AUTH) {
            throw new UnsupportedOperationException("disableUser only works with AUTH database");
        }
        String sql = "UPDATE `user` SET enabled = false WHERE id = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql, userId);
        System.out.println("User disabled with ID: " + userId);
    }

    // Включение пользователя (только для AUTH)
    public void enableUser(UUID userId) {
        if (databaseType != DatabaseType.AUTH) {
            throw new UnsupportedOperationException("enableUser only works with AUTH database");
        }
        String sql = "UPDATE `user` SET enabled = true WHERE id = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql, userId);
        System.out.println("User enabled with ID: " + userId);
    }

    // Проверка существования пользователя
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM `user` WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    // Получение прав пользователя (только для AUTH)
    public List<String> getUserAuthorities(UUID userId) {
        if (databaseType != DatabaseType.AUTH) {
            throw new UnsupportedOperationException("getUserAuthorities only works with AUTH database");
        }
        String sql = "SELECT authority FROM authority WHERE user_id = UUID_TO_BIN(?)";
        return jdbcTemplate.queryForList(sql, String.class, userId);
    }

    // Обновление имени и фамилии (только для GATEWAY)
    public void updateUserFirstAndLastName(String username, String firstname, String lastname) {
        if (databaseType != DatabaseType.GATEWAY) {
            throw new UnsupportedOperationException("updateUserFirstAndLastName only works with GATEWAY database");
        }
        String sql = "UPDATE `user` SET firstname = ?, lastname = ? WHERE username = ?";
        jdbcTemplate.update(sql, firstname, lastname, username);
        System.out.println("User profile updated in gateway: " + username + " -> " + firstname + " " + lastname);
    }

    // Получение профиля пользователя (только для GATEWAY)
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
            System.out.println("Error getting user profile: " + e.getMessage());
            return Map.of();
        }
    }

    // Получение JdbcTemplate
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    // Получение типа БД
    public DatabaseType getDatabaseType() {
        return databaseType;
    }
}