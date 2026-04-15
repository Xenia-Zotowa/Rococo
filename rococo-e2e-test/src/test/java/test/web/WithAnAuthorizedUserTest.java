package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.MainPage;
import test.helper.DatabaseHelper;
import test.helper.DatabaseType;

import java.util.Map;

import static com.codeborne.selenide.Selenide.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.javafaker.Faker;



@WebTest
public class WithAnAuthorizedUserTest {
    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
    private final LoginPage loginPage = new LoginPage();
    private final DatabaseHelper gatewayDb = new DatabaseHelper(DatabaseType.GATEWAY);


    @Test
    @DisplayName("Добавление картины")
    public void pictureEditing() {
        String paintingTitle = "Утро в сосновом лесу";
        String paintingDescription = "Картина русских живописцев Ивана Шишкина и Константина Савицкого, написанная в 1889 году в технике масляной живописи. Это одно из наиболее популярных полотен Шишкина, один из самых известных пейзажей в истории русского искусства. Выставляется в Третьяковской галерее.";

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome()
                .paintingSection()
                .addAPicture()
                .inputNamePicture(paintingTitle)
                .loadingAPainting()
                .choiceArtistShishkin();
        mainPage.fillingOutTheDescriptionOfThePainting(paintingDescription)
                .choiceTyakovGallery();
        mainPage.addButton();

        sleep(2000);

        Map<String, Object> dbPainting = gatewayDb.getPaintingByTitle(paintingTitle);
        assertThat(dbPainting).isNotEmpty();
        assertThat(dbPainting.get("title")).isEqualTo(paintingTitle);
        assertThat(dbPainting.get("description")).isEqualTo(paintingDescription);

    }

    @Test
    @DisplayName("Добавление художника")
    public void artistEditing() {
        String artistName = "Леонардо да Винчи";
        String artistBiography = "Величайший итальянский художник, ученый и изобретатель эпохи Возрождения, ставший символом «универсального человека». Он создал такие шедевры, как «Мона Лиза» и «Тайная вечеря», опередил время своими чертежами летательных аппаратов и исследованиями в анатомии. Родился в Винчи, обучался во Флоренции у Верроккьо.";

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome()
                .artistSectionFoto()
                .addAArtist()
                .inputNameArtist(artistName)
                .loadingAArtist()
                .fillingOutAnArtistsBiography(artistBiography)
                .addButton();
        sleep(2000);

        Map<String, Object> dbArtist = gatewayDb.getArtistByN(artistName);

        assertThat(dbArtist)
                .as("Художник '%s' не найден в базе данных", artistName)
                .isNotEmpty();

        assertThat(dbArtist.get("name"))
                .as("Имя художника не совпадает")
                .isEqualTo(artistName);

        assertThat(dbArtist.get("biography"))
                .as("Биография художника не совпадает")
                .isEqualTo(artistBiography);

    }


    @Test
    @DisplayName("Добавление музея")
    public void artistMuseum() {
        String museumTitle = "Национальный художественный музей Республики Беларусь";
        String museumDescription = "Музей основан 24 января 1939 года как Государственная картинная галерея Белорусской ССР. Первоначально располагалась в здании бывшей Минской женской гимназии (сейчас — улица Карла Маркса, 29). Директором стал художник-керамист Николай Михолап. К началу Великой Отечественной войны коллекция насчитывала более 2700 произведений, включая работы из исторических музеев Минска, Витебска, Могилёва и Гомеля, а также подарки от Третьяковской галереи, Русского музея, Эрмитажа и Музея изобразительных искусств им. А. С. Пушкина. Во время оккупации большая часть коллекции была вывезена в Германию, и после войны её пришлось восстанавливать практически с нуля. Современное здание музея построено в 1957 году по проекту архитектора М. И. Бакланова. Оно вдохновлено особняком Румянцева в Санкт-Петербурге и главным корпусом Пушкинского музея в Москве.";
        String cityName = "Минск";

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome()
                .museumSectionFoto()
                .AddAMuseum()
                .inputNamePicture(museumTitle)
                .choiceBelarus();
        mainPage.inputSityName(cityName)
                .loadingAMuseum()
                .fillingOutAnArtistsBiography(museumDescription)
                .addButton();

        sleep(2000);

        Map<String, Object> dbMuseum = gatewayDb.getMuseumByName(museumTitle);

        assertThat(dbMuseum)
                .as("Музей '%s' не найден в базе данных", museumTitle)
                .isNotEmpty();

        assertThat(dbMuseum.get("title"))
                .as("Название музея не совпадает")
                .isEqualTo(museumTitle);

        assertThat(dbMuseum.get("description"))
                .as("Описание музея не совпадает")
                .isEqualTo(museumDescription);

        assertThat(dbMuseum.get("geo"))
                .as("Город музея не совпадает")
                .isEqualTo(cityName);

    }


    @Test
    @DisplayName("Редактирование информации по музею")
    public void museumChange() {
        Faker faker = new Faker();
        String museumTitle = "Лувр";
        String museumDescription = faker.company().name() + " - " + faker.address().city();
        String cityName = faker.address().city();

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome()
                .museumSectionFoto()
                .museumEditingClick()
                .editButtonClick()
                .changingTheMuseumPhoto()
                .changingTheTitleOfThePainting(museumTitle)
                .inputSityName(cityName)
                .fillingOutTheDescriptionOfThePainting(museumDescription)
                .updateYourProfile();

        sleep(2000);

        Map<String, Object> dbMuseum = gatewayDb.getMuseumByTitle(museumTitle);

        assertThat(dbMuseum)
                .as("Музей '%s' не найден в базе данных", museumTitle)
                .isNotEmpty();

        assertThat(dbMuseum.get("title"))
                .as("Название музея не совпадает")
                .isEqualTo(museumTitle);

        assertThat(dbMuseum.get("description"))
                .as("Описание музея не совпадает")
                .isEqualTo(museumDescription);

        assertThat(dbMuseum.get("geo"))
                .as("Город музея не совпадает")
                .isEqualTo(cityName);

    }


    @Test
    @DisplayName("Проверка разлогинивания")
    public void cheklogOut() {
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome()
                .logOut()
                .сheckingTheVisibilityOfTheLoginButton();
    }


    @Test
    @DisplayName("Редактирование профиля")
    public void profileEditing() {
        String username = "admin";
        String newFirstname = "Ivan";
        String newLastname = "Ivanov";
        assertThat(gatewayDb.userExists(username)).isTrue();
        Map<String, Object> userBefore = gatewayDb.getUserByUsername(username);
        System.out.println("Before update - Firstname: " + userBefore.get("firstname") +
                ", Lastname: " + userBefore.get("lastname"));
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(username, "123");
        mainPage.checkBackHome()
                .goingToTheUserProfile()
                .changingTheUserFirstAndLastName(newFirstname, newLastname)
                .updateYourFotoProfile()
                .updateYourProfile();
        sleep(2000);
        Map<String, Object> userAfter = gatewayDb.getUserByUsername(username);
        String actualFirstname = (String) userAfter.get("firstname");
        String actualLastname = (String) userAfter.get("lastname");
        assertThat(actualFirstname).isEqualTo(newFirstname);
        assertThat(actualLastname).isEqualTo(newLastname);
    }

}
