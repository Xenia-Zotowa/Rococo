package test.web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.Config;
import io.qameta.allure.selenide.AllureSelenide;
import jupiter.annotation.meta.WebTest;
import model.UserJson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.MainPage;
import service.UITestUserHelper;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.sleep;


@WebTest

public class LoginTest {

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

    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
    private final LoginPage loginPage = new LoginPage();
    private final String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);


    @Test
    @DisplayName("Авторизация с зарегистрированным пользователем")
    public void checkLogin() {
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(TEST_USERNAME, TEST_PASSWORD);
        mainPage.checkBackHome();
        closeWebDriver();
    }

    @Test
    @DisplayName("Авторизация с незарегистрированным пользователем")
    public void chekErrorLogin() {

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginErrorTest(randomUsername, TEST_PASSWORD)
                .checkLoginErrorTest();
        closeWebDriver();
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void chekErrorPassword() {
        String wrongPassword = "321";

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginErrorTest(TEST_USERNAME, wrongPassword)
                .checkLoginErrorTest();
        closeWebDriver();
    }

    @Test
    @DisplayName("Проверка видимости пароля")
    public void chekPassword() {
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.checkVisiblePassword("password");
        closeWebDriver();
    }
}