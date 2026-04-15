package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.MainPage;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.sleep;

@WebTest
public class LoginTest {

    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
    private final LoginPage loginPage = new LoginPage();
    private final String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);

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

    @Test
    @DisplayName("Авторизация с зарегистрированным пользователем")
    public void chekLogin() {
        String username = "admin";
        String password = "123";

        logTestStart("Авторизация с зарегистрированным пользователем",
                "Username", username,
                "Password", "***");

        System.out.println("📝 Attempting to login with registered user...");
        System.out.println("   Expected result: Successful login");

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(username, password);
        mainPage.checkBackHome();

        System.out.println("✅ Login successful! User redirected to main page");

        closeWebDriver();
        logTestEnd("Авторизация с зарегистрированным пользователем");
    }

    @Test
    @DisplayName("Авторизация с незарегистрированным пользователем")
    public void chekErrorLogin() {
        String password = "123";

        logTestStart("Авторизация с незарегистрированным пользователем",
                "Username", randomUsername,
                "Password", "***");

        System.out.println("📝 Attempting to login with unregistered user...");
        System.out.println("   Username: " + randomUsername);
        System.out.println("   Expected result: Error message should appear");

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginErrorTest(randomUsername, password)
                .checkLoginErrorTest();

        System.out.println("✅ Error message displayed correctly for unregistered user");

        closeWebDriver();
        logTestEnd("Авторизация с незарегистрированным пользователем");
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void chekErrorPassword() {
        String username = "admin";
        String wrongPassword = "321";
        String correctPassword = "123";

        logTestStart("Авторизация с неверным паролем",
                "Username", username,
                "Wrong Password", wrongPassword,
                "Correct Password", correctPassword);

        System.out.println("📝 Attempting to login with wrong password...");
        System.out.println("   Username: " + username);
        System.out.println("   Provided password: " + wrongPassword);
        System.out.println("   Correct password: " + correctPassword);
        System.out.println("   Expected result: Error message should appear");

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginErrorTest(username, wrongPassword)
                .checkLoginErrorTest();

        System.out.println("✅ Error message displayed correctly for wrong password");

        closeWebDriver();
        logTestEnd("Авторизация с неверным паролем");
    }

    @Test
    @DisplayName("Проверка видимости пароля")
    public void chekPassword() {
        String password = "123";

        logTestStart("Проверка видимости пароля",
                "Password", "***");

        System.out.println("📝 Testing password visibility toggle...");
        System.out.println("   Expected result: Password should be hidden initially,");
        System.out.println("   then become visible after clicking the eye icon");

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.checkVisiblePassword(password);

        System.out.println("✅ Password visibility toggle works correctly");

        closeWebDriver();
        logTestEnd("Проверка видимости пароля");
    }
}