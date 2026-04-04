package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private final SelenideElement register = $(".form__paragraph .form__link");

    @Step("Переход в форму регистрации")
    public RegisterPage switchingToTheAuthorizationForm (){
        register.shouldBe(visible)
            .click();

        return new RegisterPage();

    }

}
