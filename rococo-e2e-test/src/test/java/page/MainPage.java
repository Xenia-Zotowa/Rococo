package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private final SelenideElement enterButton = $x("//button[text()='Войти']");


    @Step("Переход в форму авторизации")
    public LoginPage switchingToTheAuthorizationForm (){
        enterButton.click();

        return new LoginPage();

    }
}
