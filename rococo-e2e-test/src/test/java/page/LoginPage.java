package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private final SelenideElement register = $(".form__paragraph .form__link");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement enterButton = $(".form__submit");
    private final SelenideElement errorLogin = $( " .form__error.login__error");
    private final SelenideElement visibleButton = $(".form__element .form__password-button");

    @Step("Переход в форму регистрации")
    public RegisterPage switchingToTheAuthorizationForm (){
        register.shouldBe(visible)
            .click();
        return new RegisterPage();
    }

    @Step("Авторизация")
    public MainPage loginTest (String username, String password){
        usernameInput.val(username);
        passwordInput.val(password);
        enterButton.click();
        return new MainPage();
    }

    @Step("Авторизация с ошибкой")
    public LoginPage loginErrorTest (String username, String password){
        usernameInput.val(username);
        passwordInput.val(password);
        enterButton.click();
        return this;
    }


    @Step("Проверка ошибки авторизации с неправильным логином")
    public LoginPage checkLoginErrorTest (){
        errorLogin.shouldBe(visible)
                .shouldHave(text("Неверные учетные данные пользователя"));
        return this;
    }


    @Step("Проверка видимости пароля")
    public LoginPage checkVisiblePassword (String password){
        passwordInput.val(password);
        visibleButton.click();
        return this;
    }

}
