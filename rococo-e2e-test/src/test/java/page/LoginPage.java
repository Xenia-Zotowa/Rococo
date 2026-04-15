package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);
    private final SelenideElement register = $(".form__paragraph .form__link");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement enterButton = $(".form__submit");
    private final SelenideElement errorLogin = $( ".form .form__error.login__error");
    private final SelenideElement visibleButton = $(".form__element .form__password-button");

    @Step("Переход в форму регистрации")
    public RegisterPage switchingToTheAuthorizationForm (){
        logger.info("Переход в форму регистрации");
        register.shouldBe(visible)
            .click();
        return new RegisterPage();
    }

    @Step("Авторизация")
    public MainPage loginTest (String username, String password){
        logger.info("Авторизация");
        usernameInput.val(username);
        passwordInput.val(password);
        enterButton.click();
        return new MainPage();
    }

    @Step("Авторизация с ошибкой")
    public LoginPage loginErrorTest (String username, String password){
        logger.info("Авторизация с ошибкой");
        usernameInput.val(username);
        passwordInput.val(password);
        enterButton.click();
        return this;
    }


    @Step("Проверка ошибки авторизации с неправильными данными")
    public LoginPage checkLoginErrorTest() {
        logger.info("Проверка ошибки авторизации с неправильными данными");
        errorLogin.shouldBe(visible)
                .shouldHave(text("Неверные учетные данные пользователя"), Duration.ofSeconds(10));
        return this;
    }


    @Step("Проверка видимости пароля")
    public LoginPage checkVisiblePassword (String password){
        logger.info("Проверка видимости пароля");
        passwordInput.val(password);
        visibleButton.click();
        return this;
    }

}
