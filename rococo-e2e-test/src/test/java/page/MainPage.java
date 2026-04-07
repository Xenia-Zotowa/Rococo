package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import java.io.File;

public class MainPage {
    private final SelenideElement enterButton = $x("//button[text()='Войти']");
    private final SelenideElement homePage = $("#page-content");
    private final SelenideElement paintingButton = $(".flex.items-baseline a[href=\"/painting\"]");
    private final SelenideElement paintingFoto = $(".mx-auto li a[href=\"/painting\"]");
    private final SelenideElement artistButton = $(".flex.items-baseline a[href=\"/artist\"]");
    private final SelenideElement museumButton = $(".flex.items-baseline a[href=\"/museum\"]");
    private final SelenideElement addAPictureButton = $x("//*[text()=\"Добавить картину\"]");
    private final SelenideElement addAArtistButton = $x("//*[text()=\"Добавить художника\"]");
    private final SelenideElement addAMuseumButton = $x("//*[text()=\"Добавить музей\"]");
    private final SelenideElement toggle = $(".lightswitch-track");
    private final SelenideElement toggleLight = $("[title='Toggle Light Mode']");
    private final SelenideElement toggleDark = $("[title='Toggle Dark Mode']");
    private final SelenideElement avatar = $(".avatar");
    private final SelenideElement exitButton = $(".btn.variant-ghost");
    private final SelenideElement firstnameInput = $("input[name='firstname']");
    private final SelenideElement surnameInput = $("input[name='surname']");
    private final SelenideElement updatProfile = $(".btn.variant-filled-primary");
    private final SelenideElement fotoProfile = $("input[name='content']");


    @Step("Переход в форму авторизации")
    public LoginPage switchingToTheAuthorizationForm (){
        enterButton.click();
        return new LoginPage();

    }
    @Step("Проверка перехода на главную страницу")
    public MainPage checkBackHome (){
        homePage.shouldBe(visible);
        return this;
    }

    @Step("Переход в раздел картины")
    public MainPage paintingSection(){
        paintingButton.click();
        return this;
    }


    @Step("Переход в раздел картины через фото")
    public MainPage paintingSectionFoto(){
        paintingFoto.click();
        return this;
    }

    @Step("Переход в раздел художники")
    public MainPage artistSection(){
        artistButton.click();
        return this;
    }

    @Step("Переход в раздел музеи")
    public MainPage museumSection(){
        museumButton.click();
        return this;
    }

    @Step("Добавление картинки")
    public MainPage addAPicture(){
        addAPictureButton.click();
        return this;
    }

    @Step("Проверка невидимости кнопки Добавить картинки")
    public MainPage checkAddAPicture(){
        addAPictureButton.shouldBe(hidden);
        return this;
    }

    @Step("Проверка невидимости кнопки Добавить художника")
    public MainPage checkAddAArtist(){
        addAArtistButton.shouldBe(hidden);
        return this;
    }

    @Step("Проверка невидимости кнопки Добавить музей")
    public MainPage checkAddAMuseum(){
        addAMuseumButton.shouldBe(hidden);
        return this;
    }

    @Step("Переключение тогла")
    public MainPage switchingToggle(){
        toggle.click();
        return this;
    }

    @Step("Проверка смена режима на тёмный")
    public MainPage checkToggleDark(){
        toggleLight.shouldBe(visible);
        return this;
    }

    @Step("Проверка смена режима на светлый")
    public MainPage checkToggleLight(){
        toggleDark.shouldBe(visible);
        return this;
    }

    @Step("Разлогинится")
    public MainPage logOut(){
        avatar.click();
        exitButton.click();
        return this;
    }


    @Step("Изменении имени и фамилии пользователя")
    public MainPage changingTheUserFirstAndLastName(String firstname, String surname){
        firstnameInput.clear();
        firstnameInput.setValue(firstname);
        surnameInput.clear();
        surnameInput.setValue(surname);
        return this;
    }


    @Step("Переход в профиль пользователя")
    public MainPage goingToTheUserProfile(){
        avatar.click();
        return this;
    }

    @Step("Проверка видимости кнопки войти")
    public MainPage сheckingTheVisibilityOfTheLoginButton(){
        enterButton.shouldBe(visible).shouldHave(text("Войти"));
        return this;
    }

    @Step("Обновление профиля")
    public MainPage updateYourProfile(){
        updatProfile.click();
        return this;
    }

    @Step("Обновление аватарки профиля")
    public MainPage updateYourFotoProfile(){
        File imageFile = new File("src/test/resources/images/kapi.jpg");
        fotoProfile.uploadFile(imageFile);

        return this;
    }
}
