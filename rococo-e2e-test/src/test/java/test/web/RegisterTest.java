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
//    private final DatabaseHelper gatewayDb = new DatabaseHelper(DatabaseType.GATEWAY);


    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registerNewUser() {

        authDb.clearDatabase();
//            gatewayDb.clearDatabase();
        String username = "admin";
        String password = "123";

        assertThat(authDb.userExists(username)).isFalse();
//            assertThat(gatewayDb.userExists(username)).isFalse();

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.switchingToTheAuthorizationForm();
        sleep(1000);
        registerPage.registerUser(username, password, password)
                .checkRegister();
        assertThat(authDb.userExists(username)).isTrue();
//            assertThat(gatewayDb.userExists(username)).isTrue();

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
                .errorePasswordRegister();
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
                .erroreUsernameRegister();
        closeWebDriver();
    }

}

