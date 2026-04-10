package test.DB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import test.TestApplication;
import test.helper.TestDatabaseHelper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class)  // Указываем тестовый конфиг
public class UserRepositoryTest {

    @Autowired
    private TestDatabaseHelper dbHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        dbHelper.clearDatabase();
    }

    @Test
    void testConnection() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertThat(result).isEqualTo(1);
        System.out.println("✅ Database connection successful!");
    }


}