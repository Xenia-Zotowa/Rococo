package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class RegisterPage {
    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement formParagraphSuccess =  $(".form .form__subheader");
    private final SelenideElement logInToTheSystem = $(".form__submit");
    private final SelenideElement errorUsername = $(".form__element .form__error.error__username");
    private final SelenideElement errorPassword = $(".form__element .form__error.error__password");



    @Step("Регистрация пользователя")
    public RegisterPage registerUser(String username, String password, String passwordSubmit) {
        logger.info("Редактирование музея");
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);
        submitButton.click();
        return this;
    }

    @Step("Проверка успешной регистрации")
    public RegisterPage checkRegister(){
        logger.info("Проверка успешной регистрации");
        sleep(50);
        formParagraphSuccess.shouldBe(visible)
                .shouldHave(text("Добро пожаловать в Ro"));
        logInToTheSystem.click();
        return this;
    }

    @Step("Проверка ошибки регистрации существующего пользователя")
    public RegisterPage erroreUsernameRegister(){
        logger.info("Проверка ошибки регистрации существующего пользователя");
        sleep(500);
        errorUsername.shouldBe(visible)
                .shouldHave(text("already exists"));
        return this;
    }

    @Step("Проверка ошибки регистрации с несовпадающими паролями")
    public RegisterPage errorePasswordRegister(){
        logger.info("Проверка ошибки регистрации с несовпадающими паролями");
        sleep(50);
        errorPassword.shouldBe(visible)
                .shouldHave(text("Passwords should be equal"));
        return this;
    }
}
