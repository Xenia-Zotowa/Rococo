package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.MainPage;
import page.RegisterPage;
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

    private void logTestStart(String testName, Object... params) {
        System.out.println("========================================");
        System.out.println("🚀 Starting test: " + testName);
        System.out.println("⏰ Time: " + new java.util.Date());
        if (params.length > 0) {
            System.out.println("📝 Test data:");
            for (int i = 0; i < params.length; i += 2) {
                System.out.println("   " + params[i] + ": " + params[i + 1]);
            }
        }
        System.out.println("========================================");
    }

    private void logTestEnd(String testName) {
        System.out.println("========================================");
        System.out.println("✅ Test completed: " + testName);
        System.out.println("========================================");
    }

    private void logDatabaseCheck(String dbType, String operation, String username, boolean result) {
        System.out.println("📊 Database check (" + dbType + "):");
        System.out.println("   Operation: " + operation);
        System.out.println("   Username: " + username);
        System.out.println("   Result: " + (result ? "✅ EXISTS" : "❌ NOT EXISTS"));
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registerNewUser() {
        String username = "admin";
        String password = "123";

        logTestStart("Регистрация нового пользователя",
                "Username", username,
                "Password", "***",
                "Random username (unused)", randomUsername);

        System.out.println("🗑️ Clearing databases before test...");
        authDb.clearDatabase();
        gatewayDb.clearDatabase();
        System.out.println("✅ Databases cleared");

        System.out.println("🔍 Checking user does NOT exist before registration...");
        boolean authUserExists = authDb.userExists(username);
        boolean gatewayUserExists = gatewayDb.userExists(username);

        logDatabaseCheck("AUTH", "Check before registration", username, authUserExists);
        logDatabaseCheck("GATEWAY", "Check before registration", username, gatewayUserExists);

        assertThat(authUserExists).isFalse();
        assertThat(gatewayUserExists).isFalse();

        System.out.println("📝 Registering new user...");
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(username, password, password)
                .checkRegister();
        System.out.println("✅ Registration completed");

        sleep(2000);

        System.out.println("🔍 Checking user EXISTS after registration...");
        authUserExists = authDb.userExists(username);
        gatewayUserExists = gatewayDb.userExists(username);

        logDatabaseCheck("AUTH", "Check after registration", username, authUserExists);
        logDatabaseCheck("GATEWAY", "Check after registration", username, gatewayUserExists);

        assertThat(authUserExists).isTrue();
        assertThat(gatewayUserExists).isTrue();

        System.out.println("📊 User data verification:");
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
        logTestEnd("Регистрация нового пользователя");
    }

    @Test
    @DisplayName("Регистрация с несовпадающими паролями")
    public void registrationWithMismatchedPasswords() {
        String password1 = "123";
        String password2 = "321";

        logTestStart("Регистрация с несовпадающими паролями",
                "Username", randomUsername,
                "Password 1", password1,
                "Password 2", password2);

        System.out.println("📝 Attempting to register with mismatched passwords...");
        System.out.println("   Expected result: Error message should appear");

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(randomUsername, password1, password2)
                .errorePasswordRegister();

        System.out.println("✅ Password mismatch error verified");

        sleep(1000);

        System.out.println("🔍 Verifying user was NOT created in database...");
        boolean authUserExists = authDb.userExists(randomUsername);
        boolean gatewayUserExists = gatewayDb.userExists(randomUsername);

        logDatabaseCheck("AUTH", "Check after failed registration", randomUsername, authUserExists);
        logDatabaseCheck("GATEWAY", "Check after failed registration", randomUsername, gatewayUserExists);

        assertThat(authUserExists).isFalse();
        assertThat(gatewayUserExists).isFalse();

        System.out.println("✅ User was not created in database (as expected)");

        closeWebDriver();
        logTestEnd("Регистрация с несовпадающими паролями");
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    public void registeranAnExistingUser() {
        String existingUsername = "admin";
        String password = "123";

        logTestStart("Регистрация существующего пользователя",
                "Username", existingUsername,
                "Password", password);

        System.out.println("🔍 Verifying user already exists in database...");
        boolean authUserExists = authDb.userExists(existingUsername);
        boolean gatewayUserExists = gatewayDb.userExists(existingUsername);

        logDatabaseCheck("AUTH", "Check before registration", existingUsername, authUserExists);
        logDatabaseCheck("GATEWAY", "Check before registration", existingUsername, gatewayUserExists);

        System.out.println("📝 Attempting to register existing user...");
        System.out.println("   Expected result: Error message about existing username");

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(existingUsername, password, password)
                .erroreUsernameRegister();

        System.out.println("✅ Existing username error verified");

        closeWebDriver();
        logTestEnd("Регистрация существующего пользователя");
    }
}