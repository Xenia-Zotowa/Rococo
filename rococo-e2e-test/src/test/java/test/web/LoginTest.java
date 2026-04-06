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

    @Test
    @DisplayName("Авторизация с зарегистрированным пользователем")
    public void chekLogin() {

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome();

        closeWebDriver();
    }

    @Test
    @DisplayName("Авторизация с незарегистрированным пользователем")
    public void chekErrorLogin() {

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginErrorTest(randomUsername, "123")
                .checkLoginErrorTest ();

        closeWebDriver();
    }
    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void chekErrorPassword() {

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginErrorTest("admin", "321")
                .checkLoginErrorTest ();

        closeWebDriver();
    }


    @Test
    @DisplayName("Проверка видимости пароля")
    public void chekPassword() {

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.checkVisiblePassword ("123");

        closeWebDriver();
    }

}
