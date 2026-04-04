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


@WebTest

public class RegisterTest {
    private final Config CFG = Config.getInstance();
    private final String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
    private final LoginPage loginPage = new LoginPage();
    private final RegisterPage registerPage = new RegisterPage();


    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registerNewUser() {

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(randomUsername, "123", "123")
                .chekRegister();
        closeWebDriver();
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    public void registeranAnExistingUser() {

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser("admin", "123", "123")
                .errorUsernameRegister();
        closeWebDriver();
    }

    @Test
    @DisplayName("Регистрация с несовпадающими паролями")
    public void registrationWithMismatchedPasswords() {

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(randomUsername, "123", "321")
                .errorPasswordRegister();
        closeWebDriver();
    }

}
