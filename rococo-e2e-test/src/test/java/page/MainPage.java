package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private final SelenideElement enterButton = $x("//button[text()='Войти']");
    private final SelenideElement homePage = $("#page-content");
    private final SelenideElement paintingButton = $(".flex.items-baseline a[href=\"/painting\"]");
    private final SelenideElement artistButton = $("a[href=\"/artist\"]");
    private final SelenideElement addAPictureButton = $x("//*[text()=\"Добавить картину\"]");
    private final SelenideElement addAArtistButton = $x("//*[text()=\"Добавить художника\"]");


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

    @Step("Переход в раздел картины")
    public MainPage paintingSection(){
        paintingButton.click();
        return this;
    }

    @Step("Переход в раздел художники")
    public MainPage artistSection(){
        artistButton.click();
        return this;
    }

    @Step("Добавление картинки")
    public MainPage addAPicture(){
        addAPictureButton.click();
        return this;
    }

    @Step("Проверка невидимости кнопки Добавить картинки")
    public MainPage chekAddAPicture(){
        addAPictureButton.shouldBe(hidden);
        return this;
    }

    @Step("Проверка невидимости кнопки Добавить художника")
    public MainPage chekAddAArtist(){
        addAArtistButton.shouldBe(hidden);
        return this;
    }
}
