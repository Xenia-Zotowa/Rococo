package test.web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.Config;
import io.qameta.allure.selenide.AllureSelenide;
import jupiter.annotation.meta.WebTest;
import jupiter.utils.TestUserUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.MainPage;
import page.RegisterPage;
import service.UITestUserHelper;
import test.helper.DatabaseHelper;
import test.helper.DatabaseType;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.sleep;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
public class RegisterTest {

    private final Config CFG = Config.getInstance();
    private final String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
    private final LoginPage loginPage = new LoginPage();
    private final RegisterPage registerPage = new RegisterPage();
    private final DatabaseHelper authDb = new DatabaseHelper(DatabaseType.AUTH);
    private final DatabaseHelper gatewayDb = new DatabaseHelper(DatabaseType.GATEWAY);
    private static final String TEST_USERNAME = "user_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String TEST_PASSWORD = "testpass123";

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
                        .includeSelenideSteps(true));

        UITestUserHelper.registerUserViaUI(TEST_USERNAME, TEST_PASSWORD);
    }

    @BeforeEach
    public void setUp() {
        System.out.println("========================================");
        System.out.println("Starting test: " + getClass().getSimpleName());
        System.out.println("========================================");
    }

    private void logDatabaseCheck(String dbType, String operation, String username, boolean result) {
        System.out.println("Database check (" + dbType + "):");
        System.out.println("Operation: " + operation);
        System.out.println("Username: " + username);
        System.out.println("Result: " + (result ? "EXISTS" : "NOT EXISTS"));
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registerNewUser() {
        String username = "admin";
        String password = "123";
        authDb.clearDatabase();
        boolean authUserExists = authDb.userExists(username);
        boolean gatewayUserExists = gatewayDb.userExists(username);
        logDatabaseCheck("AUTH", "Check before registration", username, authUserExists);
        logDatabaseCheck("GATEWAY", "Check before registration", username, gatewayUserExists);
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(username, password, password)
                .checkRegister();
        sleep(2000);
        authUserExists = authDb.userExists(username);
        gatewayUserExists = gatewayDb.userExists(username);
        assertThat(authUserExists).isTrue();
        assertThat(gatewayUserExists).isTrue();
        if (authUserExists) {
            var userData = authDb.getUserByUsername(username);
            System.out.println("   AUTH DB - Username: " + userData.get("username"));
            System.out.println("   AUTH DB - Enabled: " + userData.get("enabled"));
        }
        if (gatewayUserExists) {
            var userData = gatewayDb.getUserByUsername(username);
            System.out.println("   GATEWAY DB - Username: " + userData.get("username"));
            System.out.println("   GATEWAY DB - Firstname: " + userData.get("firstname"));
            System.out.println("   GATEWAY DB - Lastname: " + userData.get("lastname"));
        }
        closeWebDriver();
    }

    @Test
    @DisplayName("Регистрация с несовпадающими паролями")
    public void registrationWithMismatchedPasswords() {
        String password1 = "123";
        String password2 = "321";
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(randomUsername, password1, password2)
                .errorePasswordRegister();
        sleep(1000);
        boolean authUserExists = authDb.userExists(randomUsername);
        boolean gatewayUserExists = gatewayDb.userExists(randomUsername);
        logDatabaseCheck("AUTH", "Check after failed registration", randomUsername, authUserExists);
        logDatabaseCheck("GATEWAY", "Check after failed registration", randomUsername, gatewayUserExists);
        assertThat(authUserExists).isFalse();
        assertThat(gatewayUserExists).isFalse();
        closeWebDriver();
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    public void registeranAnExistingUser() {
        boolean authUserExists = authDb.userExists(TEST_USERNAME);
//        boolean gatewayUserExists = gatewayDb.userExists(TEST_USERNAME);
        logDatabaseCheck("AUTH", "Check before registration", TEST_USERNAME, authUserExists);
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD)
                .erroreUsernameRegister();
        closeWebDriver();
    }
}