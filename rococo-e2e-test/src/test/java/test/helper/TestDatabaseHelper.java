package test.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class TestDatabaseHelper {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TestDatabaseHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        initDatabase();
    }

    public TestDatabaseHelper() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/rococo-auth?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        config.setUsername("rococo_admin");
        config.setPassword("rococo_admin123");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(30000);

        DataSource dataSource = new HikariDataSource(config);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        initDatabase();
    }

    private void initDatabase() {
        createTables();
    }

    private void createTables() {
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
            System.out.println("Tables created successfully");
        } catch (Exception e) {
            System.out.println("Tables already exist or error: " + e.getMessage());
        }
    }

    public void clearDatabase() {
        try {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
            jdbcTemplate.execute("TRUNCATE TABLE authority");
            jdbcTemplate.execute("TRUNCATE TABLE `user`");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("Database cleared successfully");
        } catch (Exception e) {
            System.out.println("Error clearing database: " + e.getMessage());
        }
    }

    public UUID insertUser(String username, String password, boolean enabled) {
        UUID userId = UUID.randomUUID();
        String sql = """
            INSERT INTO `user` (id, username, password, enabled, 
                account_non_expired, account_non_locked, credentials_non_expired)
            VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql, userId, username, password, enabled,
                enabled, enabled, enabled);

        System.out.println("User inserted with ID: " + userId);
        return userId;
    }

    public void insertAuthority(UUID userId, String authority) {
        String sql = "INSERT INTO authority (user_id, authority) VALUES (UUID_TO_BIN(?), ?)";
        jdbcTemplate.update(sql, userId, authority);
        System.out.println("Authority inserted for user: " + userId);
    }

    public void deleteUserById(UUID userId) {
        String sql = "DELETE FROM `user` WHERE id = UUID_TO_BIN(?)";
        int rows = jdbcTemplate.update(sql, userId);
        if (rows > 0) {
            System.out.println("User deleted with ID: " + userId);
        } else {
            System.out.println("User not found with ID: " + userId);
        }
    }

    public void deleteUserByUsername(String username) {
        String sql = "DELETE FROM `user` WHERE username = ?";
        int rows = jdbcTemplate.update(sql, username);
        if (rows > 0) {
            System.out.println("User deleted with username: " + username);
        } else {
            System.out.println("User not found with username: " + username);
        }
    }

    public Map<String, Object> getUserByUsername(String username) {
        String sql = """
            SELECT id, username, password, enabled, 
                   account_non_expired, account_non_locked, credentials_non_expired
            FROM `user` 
            WHERE username = ?
        """;

        return jdbcTemplate.queryForMap(sql, username);
    }

    public void disableUser(UUID userId) {
        String sql = "UPDATE `user` SET enabled = false WHERE id = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql, userId);
        System.out.println("User disabled with ID: " + userId);
    }

    public void enableUser(UUID userId) {
        String sql = "UPDATE `user` SET enabled = true WHERE id = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql, userId);
        System.out.println("User enabled with ID: " + userId);
    }

    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM `user` WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public List<String> getUserAuthorities(UUID userId) {
        String sql = "SELECT authority FROM authority WHERE user_id = UUID_TO_BIN(?)";
        return jdbcTemplate.queryForList(sql, String.class, userId);
    }
    public void updateUserFirstAndLastName(String username, String firstname, String lastname) {
        String sql = "UPDATE `user` SET firstname = ?, lastname = ? WHERE username = ?";
        jdbcTemplate.update(sql, firstname, lastname, username);
        System.out.println("User profile updated: " + username + " -> " + firstname + " " + lastname);
    }

    public Map<String, Object> getUserProfile(String username) {
        String sql = """
        SELECT firstname, lastname 
        FROM `user` 
        WHERE username = ?
    """;
        try {
            return jdbcTemplate.queryForMap(sql, username);
        } catch (Exception e) {
            // Если полей нет в user, пробуем profile
            String sql2 = """
            SELECT p.firstname, p.lastname 
            FROM `user` u 
            JOIN profile p ON u.id = p.user_id 
            WHERE u.username = ?
        """;
            return jdbcTemplate.queryForMap(sql2, username);
        }
    }
}