package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private final SelenideElement enterButton = $x("//button[text()='Войти']");
    private final SelenideElement homePage = $("#page-content");


    @Step("Переход в форму авторизации")
    public LoginPage switchingToTheAuthorizationForm (){
        enterButton.click();

        return new LoginPage();

    }
    @Step("Проверка перехода на главную страницу")
    public MainPage chekBackHome (){
        homePage.shouldBe(visible);

        return this;

    }
}
