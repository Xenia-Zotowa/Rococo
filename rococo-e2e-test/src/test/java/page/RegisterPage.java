package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement formParagraphSuccess =  $(".form__subheader");
    private final SelenideElement logInToTheSystem = $(".form__submit");
    private final SelenideElement errorUsername = $(".form__element .form__error.error__username");
    private final SelenideElement errorPassword = $(".form__element .form__error.error__password");



    @Step("Регистрация пользователя")
    public RegisterPage registerUser(String username, String password, String passwordSubmit) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);
        submitButton.click();

        return this;

    }

    @Step("Проверка успешной регистрации")
    public RegisterPage checRegister(){
        sleep(50);
        formParagraphSuccess.shouldBe(visible)
                .shouldHave(text("Добро пожаловать в Ro"));
        logInToTheSystem.click();
        return this;
    }

    @Step("Проверка ошибки регистрации существующего пользователя")
    public RegisterPage errorUsernameRegister(){
        sleep(50);
        errorUsername.shouldBe(visible)
                .shouldHave(text("already exists"));
        return this;
    }

    @Step("Проверка ошибки регистрации с несовпадающими паролями")
    public RegisterPage errorPasswordRegister(){
        sleep(50);
        errorPassword.shouldBe(visible)
                .shouldHave(text("Passwords should be equal"));
        return this;
    }
}
