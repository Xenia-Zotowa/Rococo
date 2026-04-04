package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.MainPage;
import page.RegisterPage;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.sleep;

//
@WebTest

public class RegisterTest {
    private final Config CFG = Config.getInstance();
    private final String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);

    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registerNewUser() {

        MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        LoginPage loginPage = new LoginPage();
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        RegisterPage registerPage = new RegisterPage();
        registerPage.registerUser("randomUsername", "123", "123")
                .checRegister();
        closeWebDriver();
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    public void registeranAnExistingUser() {

        MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        LoginPage loginPage = new LoginPage();
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        RegisterPage registerPage = new RegisterPage();
        registerPage.registerUser("admin", "123", "123")
                .errorUsernameRegister();
        closeWebDriver();
    }

    @Test
    @DisplayName("Регистрация с несовпадающими паролями")
    public void registrationWithMismatchedPasswords() {

        MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        LoginPage loginPage = new LoginPage();
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        RegisterPage registerPage = new RegisterPage();
        registerPage.registerUser("randomUsername", "123", "321")
                .errorPasswordRegister();
        closeWebDriver();
    }

}
