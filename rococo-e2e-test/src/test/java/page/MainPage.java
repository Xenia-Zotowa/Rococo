package page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;

import javax.lang.model.element.Element;
import javax.swing.text.Document;
import java.io.File;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    private final SelenideElement enterButton = $x("//button[text()='Войти']");
    private final SelenideElement homePage = $("#page-content");
    private final SelenideElement paintingButton = $(".flex.items-baseline a[href=\"/painting\"]");
    private final SelenideElement paintingFoto = $("img[src=\"/piones-dtuchs.jpeg\"]");
    private final SelenideElement artistButton = $(".flex.items-baseline a[href=\"/artist\"]");
    private final SelenideElement artistFoto = $("img[src=\"/renuar.jpeg\"]");
    private final SelenideElement museumFoto = $("img[src=\"/hermitage.jpg\"]");
    private final SelenideElement museumButton = $(".flex.items-baseline a[href=\"/museum\"]");
    private final SelenideElement addAPictureButton = $x("//*[text()=\"Добавить картину\"]");
    private final SelenideElement addAArtistButton = $x("//*[text()=\"Добавить художника\"]");
    private final SelenideElement addAMuseumButton = $x("//*[text()=\"Добавить музей\"]");
    private final SelenideElement toggle = $(".lightswitch-track");
    private final SelenideElement toggleLight = $("[title='Toggle Light Mode']");
    private final SelenideElement toggleDark = $("[title='Toggle Dark Mode']");
    private final SelenideElement avatar = $(".avatar");
    private final SelenideElement exitButton = $(".w-full.text-left.text-error-500");
    private final SelenideElement firstnameInput = $("input[name='firstname']");
    private final SelenideElement surnameInput = $("input[name='lastname']");
    private final SelenideElement updatProfile = $(".btn.variant-filled-primary");
    private final SelenideElement fotoProfile = $("input[name='content']");
    private final SelenideElement title = $("#page-content .flex.items-center");
    private final SelenideElement inputSearch = $(".input");
    private final SelenideElement buttonSearch = $(".flex.justify-center button[type=\"button\"]");
    private final SelenideElement artistFotoDiv = $("a[href=\"/artist\"] .text-bold");
    private final SelenideElement museumFotoDiv = $("a[href=\"/museum\"] .text-bold");
    private final SelenideElement paintingFotoDiv = $("a[href=\"/painting\"] .text-bold");
    private final SelenideElement artistShishkin = $("a[href=\"/artist/49d40508-104f-76ce-8967-fdf1ebb8cf45\"] .flex-auto");
    private final SelenideElement tretyakovGallery = $("a[href=\"/museum/43280d5b-3b78-5453-8380-5f226cb4dd5a\"] .text-center");
    private final SelenideElement aboveEternalPeace = $("a[href=\"/painting/4a370c8c-9450-ff00-9eeb-a1d0b5c3d85b\"] .text-center");
    private final SelenideElement paintingChack = $("div.m-4 div.m-4");
    private final SelenideElement noContent = $(".text-xl");
    private final SelenideElement inputNamePicture = $("input[name=\"title\"]");
    private final SelenideElement choiceArtistShishkin = $(".select option[value=\"49d40508-104f-76ce-8967-fdf1ebb8cf45\"]");
    private final SelenideElement choicetretyakovGallery = $(".select option[value=\"43280d5b-3b78-5453-8380-5f226cb4dd5a\"]");
    private final SelenideElement choiceBelarus = $("option[value='11f13840-1c78-1a28-948b-66c967e2727d']");
    private final SelenideElement descriptionPicture = $(".textarea");
    private final SelenideElement addButton = $(".btn.variant-filled-primary");
    private final SelenideElement inputArtist = $("input[name=\"name\"]");
    private final SelenideElement fotoArtist = $("input[name=\"photo\"]");
    private final SelenideElement inputBiography = $(".textarea");
    private final SelenideElement inputSity = $("input[name=\"city\"]");
    private final SelenideElement editButton = $("button[data-testid=\"edit-museum\"]");
    private final SelenideElement museumEditing = $("a[href=\"/museum/43280d5b-3b78-5453-8380-5f226cb4dd5a\"] .max-w-full");
    private final SelenideElement convertToBasicLatin = $x("//button[text()=\"Редактировать профиль\"]");


    @Step("Переход в форму авторизации")
    public LoginPage switchingToTheAuthorizationForm() {
        enterButton.click();
        return new LoginPage();

    }

    @Step("Проверка перехода на главную страницу")
    public MainPage checkBackHome() {
        homePage.shouldBe(visible);
        return this;
    }

    @Step("Добавить")
    public MainPage addButton() {
        addButton.click();
        return this;
    }

    @Step("Переход в раздел картины")
    public MainPage paintingSection() {
        paintingButton.click();
        return this;
    }


    @Step("Переход в раздел картины через фото")
    public MainPage paintingSectionFoto() {
        paintingFoto.click();
        return this;
    }


    @Step("Переход в раздел художники через фото")
    public MainPage artistSectionFoto() {
        artistFoto.click();
        return this;
    }


    @Step("Переход в раздел музеи через фото")
    public MainPage museumSectionFoto() {
        museumFoto.click();
        return this;
    }

    @Step("Проверка наличия заголовка Картины")
    public MainPage checkingThePictureTitle() {
        title.shouldBe(visible).shouldHave(text("Картины"));
        return this;
    }

    @Step("Проверка наличия заголовка Художники")
    public MainPage checkingTheArtistTitle() {
        title.shouldBe(visible).shouldHave(text("Художники"));
        return this;
    }

    @Step("Проверка наличия заголовка Музеи")
    public MainPage checkingTheMuseumTitle() {
        title.shouldBe(visible).shouldHave(text("Музеи"));
        return this;
    }

    @Step("Переход в раздел художники")
    public MainPage artistSection() {
        artistButton.click();
        return this;
    }

    @Step("Переход в раздел музеи")
    public MainPage museumSection() {
        museumButton.click();
        return this;
    }

    @Step("Добавление картины")
    public MainPage addAPicture() {
        addAPictureButton.click();
        return this;
    }

    @Step("Добавление художника")
    public MainPage addAArtist() {
        addAArtistButton.click();
        return this;
    }

    @Step("Проверка невидимости кнопки Добавить картинки")
    public MainPage checkAddAPicture() {
        addAPictureButton.shouldBe(hidden);
        return this;
    }

    @Step("Проверка невидимости кнопки Добавить художника")
    public MainPage checkAddAArtist() {
        addAArtistButton.shouldBe(hidden);
        return this;
    }

    @Step("Проверка невидимости кнопки Добавить музей")
    public MainPage checkAddAMuseum() {
        addAMuseumButton.shouldBe(hidden);
        return this;
    }

    @Step("Добавить музей")
    public MainPage AddAMuseum() {
        addAMuseumButton.click();
        return this;
    }

    @Step("Переключение тогла")
    public MainPage switchingToggle() {
        toggle.click();
        return this;
    }

    @Step("Проверка смена режима на тёмный")
    public MainPage checkToggleDark() {
        toggleLight.shouldBe(visible);
        return this;
    }

    @Step("Проверка смена режима на светлый")
    public MainPage checkToggleLight() {
        toggleDark.shouldBe(visible);
        return this;
    }

    @Step("Разлогинится")
    public MainPage logOut() {
        avatar.click();
        exitButton.click();
        return this;
    }


    @Step("Изменении имени и фамилии пользователя")
    public MainPage changingTheUserFirstAndLastName(String firstname, String surname) {
        firstnameInput.clear();
        firstnameInput.setValue(firstname);
        surnameInput.clear();
        surnameInput.setValue(surname);
        return this;
    }

    @Step("Заполнение описания картины")
    public MainPage fillingOutTheDescriptionOfThePainting(String value) {
        descriptionPicture.setValue(value);
        return this;
    }

    @Step("Заполнение города")
    public MainPage inputSityName(String value) {
        inputSity.setValue(value);
        return this;
    }

    @Step("Заполнение биографии")
    public MainPage fillingOutAnArtistsBiography(String value) {
        inputBiography.setValue(value);
        return this;
    }

    @Step("Заполнение имени художника")
    public MainPage inputNameArtist(String value) {
        inputArtist.setValue(value);
        return this;
    }


    @Step("Переход в профиль пользователя")
    public MainPage goingToTheUserProfile() {
        avatar.click();
        convertToBasicLatin.click();
        return this;
    }

    @Step("Проверка видимости кнопки войти")
    public MainPage сheckingTheVisibilityOfTheLoginButton() {
        enterButton.shouldBe(visible).shouldHave(text("Войти"));
        return this;
    }

    @Step("Проверка корректности работы поиска по картине")
    public MainPage testingTheSearchByImage(String value) {
        aboveEternalPeace.shouldBe(visible).shouldHave(text(value));
        return this;
    }

    @Step("Проверка отсутствия данных")
    public MainPage testingNoComtent(String value) {
        noContent.shouldBe(visible).shouldHave(text(value));
        return this;
    }

    @Step("Проверка корректности работы поиска по музею")
    public MainPage testingTheSearchByMuseum(String value) {
        tretyakovGallery.shouldBe(visible).shouldHave(text(value));
        return this;
    }

    @Step("Проверка корректности работы поиска по художнику")
    public MainPage testingTheSearchByArtist(String value) {
        artistShishkin.shouldBe(visible).shouldHave(text(value));
        return this;
    }

    @Step("Обновление профиля")
    public MainPage updateYourProfile() {
        updatProfile.click();
        return this;
    }

    @Step("Обновление аватарки профиля")
    public MainPage updateYourFotoProfile() {
        File imageFile = new File("src/test/resources/images/kapi.jpg");
        fotoProfile.uploadFile(imageFile);
        return this;
    }

    @Step("Загрузка картины")
    public MainPage loadingAPainting() {
        File imageFile = new File("src/test/resources/images/morningInAPineForest.jpg");
        fotoProfile.uploadFile(imageFile);
        return this;
    }

    @Step("Загрузка художника")
    public MainPage loadingAArtist() {
        File imageFile = new File("src/test/resources/images/Leonardo_da_Vinci.jpg");
        fotoArtist.uploadFile(imageFile);
        return this;
    }

    @Step("Изменение фото музея")
    public MainPage changingTheMuseumPhoto() {
        File imageFile = new File("src/test/resources/images/tretyakovka_YB_1.jpg");
        fotoArtist.uploadFile(imageFile);
        return this;
    }

    @Step("Загрузка фото музея")
    public MainPage loadingAMuseum() {
        File imageFile = new File("src/test/resources/images/Share.jpg");
        fotoArtist.uploadFile(imageFile);
        return this;
    }

    @Step("Поиск")
    public MainPage search(String value) {
        inputSearch.setValue(value);
        buttonSearch.click();
        return this;
    }

    @Step("Ввод названия")
    public MainPage inputNamePicture(String value) {
        inputNamePicture.setValue(value);
        return this;
    }

    @Step("Переход в раздел художники по тексту под фото")
    public MainPage goToTheArtistsSectionByClickingOnTheTextBelowThePhoto() {
        artistFotoDiv.click();
        return this;
    }

    @Step("Переход в раздел музеи по тексту под фото")
    public MainPage goToTheMuseumSectionByClickingOnTheTextBelowThePhoto() {
        museumFotoDiv.click();
        return this;
    }

    @Step("Переход в раздел картины по тексту под фото")
    public MainPage goToThePaintingSectionByClickingOnTheTextBelowThePhoto() {
        paintingFotoDiv.click();
        return this;
    }

    @Step("Переход в раздел информации по Шишкину")
    public MainPage goToTheShishkinInformationSection() {
        artistShishkin.click();
        return this;
    }

    @Step("Переход в раздел информации по Третьяковке")
    public MainPage goToTheTretyakovGalleryInformationSection() {
        tretyakovGallery.click();
        return this;
    }

    @Step("Переход в раздел информации по картине Над вечным покоем")
    public MainPage goToTheAboveEternalPeaceInformationSection() {
        aboveEternalPeace.click();
        return this;
    }

    @Step("Получение HTML текущей страницы")
    public String getCurrentPageHtml() {
        return WebDriverRunner.getWebDriver().getPageSource();
    }

    @Step("Получение описания художника с текущей страницы")
    public String getArtistDescriptionFromCurrentPage() {
        return $("section.grid p.col-span-2").getText();
    }

    @Step("Получение описания музея с текущей страницы")
    public String getMuseumDescriptionFromCurrentPage() {
        return $x("//div[text()=\"Государственная Третьяковская галерея — российский государственный художественный музей в Москве, созданный на основе исторических коллекций купцов братьев Павла и Сергея Михайловичей Третьяковых; одно из крупнейших в мире собраний русского изобразительного искусства.\"]").getText();
    }

    @Step("Получение описания картины с текущей страницы")
    public String getPaintingDescriptionFromCurrentPage() {
        return paintingChack.getText();
    }

    @Step("Выбор художника Шишкина")
    public String choiceArtistShishkin() {
        choiceArtistShishkin.click();
        return choiceArtistShishkin.getText();
    }

    @Step("Выбор Третьяковской галереи")
    public String choiceTyakovGallery() {
        choicetretyakovGallery.click();
        return choicetretyakovGallery.getText();
    }

    @Step("Выбор Белорусии")
    public String choiceBelarus() {
        choiceBelarus.scrollTo();
        choiceBelarus.click();
        return choiceBelarus.getText();
    }

    @Step("Редактирование")
    public MainPage editButtonClick() {
        editButton.click();
        return this;
    }

    @Step("Редактирование музея")
    public MainPage museumEditingClick() {
        museumEditing.click();
        return this;
    }
}
