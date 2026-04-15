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
    private final String login = "admin";
    private final String password = "123";

    private void logTestStart(String testName, Object... params) {
        System.out.println("========================================");
        System.out.println("🚀 Starting test: " + testName);
        System.out.println("⏰ Time: " + new java.util.Date());
        if (params.length > 0) {
            System.out.println("📝 Test data:");
            for (int i = 0; i < params.length; i += 2) {
                System.out.println("   " + params[i] + ": " + params[i + 1]);
            }
        }
        System.out.println("========================================");
    }

    private void logTestEnd(String testName) {
        System.out.println("========================================");
        System.out.println("✅ Test completed: " + testName);
        System.out.println("========================================");
    }

    @Test
    @DisplayName("Добавление картины")
    public void pictureEditing() {
        String paintingTitle = "Утро в сосновом лесу";
        String paintingDescription = "Картина русских живописцев Ивана Шишкина и Константина Савицкого, написанная в 1889 году в технике масляной живописи. Это одно из наиболее популярных полотен Шишкина, один из самых известных пейзажей в истории русского искусства. Выставляется в Третьяковской галерее.";

        logTestStart("Добавление картины",
                "Title", paintingTitle,
                "Description length", paintingDescription.length());

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(login, password);
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

        System.out.println("🔍 Checking database for painting: " + paintingTitle);
        Map<String, Object> dbPainting = gatewayDb.getPaintingByTitle(paintingTitle);

        System.out.println("📊 Database query result:");
        System.out.println("   Painting found: " + (dbPainting != null && !dbPainting.isEmpty()));
        if (dbPainting != null && !dbPainting.isEmpty()) {
            System.out.println("   Title: " + dbPainting.get("title"));
            System.out.println("   Description length: " + ((String) dbPainting.get("description")).length());
        }

        assertThat(dbPainting).isNotEmpty();
        assertThat(dbPainting.get("title")).isEqualTo(paintingTitle);
        assertThat(dbPainting.get("description")).isEqualTo(paintingDescription);

        logTestEnd("Добавление картины");
    }

    @Test
    @DisplayName("Добавление художника")
    public void artistEditing() {
        String artistName = "Леонардо да Винчи";
        String artistBiography = "Величайший итальянский художник, ученый и изобретатель эпохи Возрождения, ставший символом «универсального человека». Он создал такие шедевры, как «Мона Лиза» и «Тайная вечеря», опередил время своими чертежами летательных аппаратов и исследованиями в анатомии. Родился в Винчи, обучался во Флоренции у Верроккьо.";

        logTestStart("Добавление художника",
                "Name", artistName,
                "Biography length", artistBiography.length());

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(login, password);
        mainPage.checkBackHome()
                .artistSectionFoto()
                .addAArtist()
                .inputNameArtist(artistName)
                .loadingAArtist()
                .fillingOutAnArtistsBiography(artistBiography)
                .addButton();
        sleep(2000);

        System.out.println("🔍 Checking database for artist: " + artistName);
        Map<String, Object> dbArtist = gatewayDb.getArtistByN(artistName);

        System.out.println("📊 Database query result:");
        System.out.println("   Artist found: " + (dbArtist != null && !dbArtist.isEmpty()));
        if (dbArtist != null && !dbArtist.isEmpty()) {
            System.out.println("   Name: " + dbArtist.get("name"));
            System.out.println("   Biography length: " + ((String) dbArtist.get("biography")).length());
        }

        assertThat(dbArtist)
                .as("Художник '%s' не найден в базе данных", artistName)
                .isNotEmpty();

        assertThat(dbArtist.get("name"))
                .as("Имя художника не совпадает")
                .isEqualTo(artistName);

        assertThat(dbArtist.get("biography"))
                .as("Биография художника не совпадает")
                .isEqualTo(artistBiography);

        logTestEnd("Добавление художника");
    }

    @Test
    @DisplayName("Добавление музея")
    public void artistMuseum() {
        String museumTitle = "Национальный художественный музей Республики Беларусь";
        String museumDescription = "Музей основан 24 января 1939 года как Государственная картинная галерея Белорусской ССР. Первоначально располагалась в здании бывшей Минской женской гимназии (сейчас — улица Карла Маркса, 29). Директором стал художник-керамист Николай Михолап. К началу Великой Отечественной войны коллекция насчитывала более 2700 произведений, включая работы из исторических музеев Минска, Витебска, Могилёва и Гомеля, а также подарки от Третьяковской галереи, Русского музея, Эрмитажа и Музея изобразительных искусств им. А. С. Пушкина. Во время оккупации большая часть коллекции была вывезена в Германию, и после войны её пришлось восстанавливать практически с нуля. Современное здание музея построено в 1957 году по проекту архитектора М. И. Бакланова. Оно вдохновлено особняком Румянцева в Санкт-Петербурге и главным корпусом Пушкинского музея в Москве.";
        String cityName = "Минск";

        logTestStart("Добавление музея",
                "Title", museumTitle,
                "City", cityName,
                "Description length", museumDescription.length());

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(login, password);
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

        System.out.println("🔍 Checking database for museum: " + museumTitle);
        Map<String, Object> dbMuseum = gatewayDb.getMuseumByName(museumTitle);

        System.out.println("📊 Database query result:");
        System.out.println("   Museum found: " + (dbMuseum != null && !dbMuseum.isEmpty()));
        if (dbMuseum != null && !dbMuseum.isEmpty()) {
            System.out.println("   Title: " + dbMuseum.get("title"));
            System.out.println("   City: " + dbMuseum.get("city"));
            System.out.println("   Description length: " + ((String) dbMuseum.get("description")).length());
        }

        assertThat(dbMuseum)
                .as("Музей '%s' не найден в базе данных", museumTitle)
                .isNotEmpty();

        assertThat(dbMuseum.get("title"))
                .as("Название музея не совпадает")
                .isEqualTo(museumTitle);

        assertThat(dbMuseum.get("description"))
                .as("Описание музея не совпадает")
                .isEqualTo(museumDescription);

        assertThat(dbMuseum.get("city"))
                .as("Город музея не совпадает")
                .isEqualTo(cityName);

        logTestEnd("Добавление музея");
    }

    @Test
    @DisplayName("Редактирование информации по музею")
    public void museumChange() {
        Faker faker = new Faker();
        String museumTitle = "Лувр";
        String museumDescription = faker.company().name() + " - " + faker.address().city();
        String cityName = faker.address().city();

        logTestStart("Редактирование музея",
                "Title", museumTitle,
                "New City", cityName,
                "New Description", museumDescription);

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(login, password);
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

        System.out.println("🔍 Checking database for museum after update: " + museumTitle);
        Map<String, Object> dbMuseum = gatewayDb.getMuseumByName(museumTitle);

        System.out.println("📊 Database query result:");
        System.out.println("   Museum found: " + (dbMuseum != null && !dbMuseum.isEmpty()));
        if (dbMuseum != null && !dbMuseum.isEmpty()) {
            System.out.println("   ID: " + dbMuseum.get("id"));
            System.out.println("   Title: " + dbMuseum.get("title"));
            System.out.println("   City: " + dbMuseum.get("city"));
            System.out.println("   Description: " + dbMuseum.get("description"));
        }

        assertThat(dbMuseum)
                .as("Музей '%s' не найден в базе данных", museumTitle)
                .isNotEmpty();

        assertThat(dbMuseum.get("title"))
                .as("Название музея не совпадает")
                .isEqualTo(museumTitle);

        assertThat(dbMuseum.get("description"))
                .as("Описание музея не совпадает")
                .isEqualTo(museumDescription);

        assertThat(dbMuseum.get("city"))
                .as("Город музея не совпадает")
                .isEqualTo(cityName);

        logTestEnd("Редактирование музея");
    }

    @Test
    @DisplayName("Проверка разлогинивания")
    public void cheklogOut() {
        logTestStart("Проверка разлогинивания");

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(login, password);
        mainPage.checkBackHome()
                .logOut()
                .сheckingTheVisibilityOfTheLoginButton();

        System.out.println("✅ Logout successful, login button is visible");
        logTestEnd("Проверка разлогинивания");
    }

    @Test
    @DisplayName("Редактирование профиля")
    public void profileEditing() {
        String newFirstname = "Ivan";
        String newLastname = "Ivanov";

        logTestStart("Редактирование профиля",
                "Username", login,
                "New Firstname", newFirstname,
                "New Lastname", newLastname);

        System.out.println("🔍 Checking user exists in database: " + login);
        assertThat(gatewayDb.userExists(login)).isTrue();

        Map<String, Object> userBefore = gatewayDb.getUserByUsername(login);
        System.out.println("📊 Before update:");
        System.out.println("   Firstname: " + userBefore.get("firstname"));
        System.out.println("   Lastname: " + userBefore.get("lastname"));

        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest(login, password);
        mainPage.checkBackHome()
                .goingToTheUserProfile()
                .changingTheUserFirstAndLastName(newFirstname, newLastname)
                .updateYourFotoProfile()
                .updateYourProfile();
        sleep(2000);

        Map<String, Object> userAfter = gatewayDb.getUserByUsername(login);
        String actualFirstname = (String) userAfter.get("firstname");
        String actualLastname = (String) userAfter.get("lastname");

        System.out.println("📊 After update:");
        System.out.println("   Firstname: " + actualFirstname);
        System.out.println("   Lastname: " + actualLastname);

        assertThat(actualFirstname).isEqualTo(newFirstname);
        assertThat(actualLastname).isEqualTo(newLastname);

        logTestEnd("Редактирование профиля");
    }
}