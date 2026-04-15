package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.MainPage;
import test.helper.DatabaseHelper;
import test.helper.DatabaseType;

import java.util.Map;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
public class WithoutAuthorizationTest {
    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
    private final DatabaseHelper gatewayDb = new DatabaseHelper(DatabaseType.GATEWAY);

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
    @DisplayName("Проверка невозможности добавления картинки без авторизации")
    public void checkingIfItIsImpossibleToAddAnImageWithoutAuthorization() {
        logTestStart("Проверка невозможности добавления картинки без авторизации");

        System.out.println("📝 Attempting to add picture without authorization...");
        System.out.println("   Expected result: Add button should be disabled or not visible");

        mainPage.checkBackHome()
                .paintingSection()
                .checkAddAPicture();

        System.out.println("✅ Add picture button is correctly blocked without authorization");
        logTestEnd("Проверка невозможности добавления картинки без авторизации");
    }

    @Test
    @DisplayName("Проверка невозможности добавления художника без авторизации")
    public void checkingIfItIsImpossibleToAddAnArtistWithoutAuthorization() {
        logTestStart("Проверка невозможности добавления художника без авторизации");

        System.out.println("📝 Attempting to add artist without authorization...");
        System.out.println("   Expected result: Add button should be disabled or not visible");

        mainPage.checkBackHome()
                .artistSection()
                .checkAddAArtist();

        System.out.println("✅ Add artist button is correctly blocked without authorization");
        logTestEnd("Проверка невозможности добавления художника без авторизации");
    }

    @Test
    @DisplayName("Проверка невозможности добавления музея без авторизации")
    public void checkingIfItIsImpossibleToAddAnMuseumWithoutAuthorization() {
        logTestStart("Проверка невозможности добавления музея без авторизации");

        System.out.println("📝 Attempting to add museum without authorization...");
        System.out.println("   Expected result: Add button should be disabled or not visible");

        mainPage.checkBackHome()
                .museumSection()
                .checkAddAMuseum();

        System.out.println("✅ Add museum button is correctly blocked without authorization");
        logTestEnd("Проверка невозможности добавления музея без авторизации");
    }

    @Test
    @DisplayName("Проверка переключения тогла")
    public void checkingtheToggleSwitch() {
        logTestStart("Проверка переключения тогла");

        System.out.println("📝 Testing dark/light mode toggle...");
        System.out.println("   Expected result: Theme should switch between dark and light");

        mainPage.checkBackHome()
                .switchingToggle()
                .checkToggleDark()
                .switchingToggle()
                .checkToggleLight()
                .switchingToggle();

        System.out.println("✅ Theme toggle works correctly");
        logTestEnd("Проверка переключения тогла");
    }

    @Test
    @DisplayName("Проверка перехода в раздел картин")
    public void checkingTheTransitionToThePicturesSection() {
        logTestStart("Проверка перехода в раздел картин");

        System.out.println("📝 Navigating to Pictures section...");
        System.out.println("   Expected result: Page title should be 'Картины'");

        mainPage.checkBackHome()
                .paintingSectionFoto()
                .checkingThePictureTitle();

        System.out.println("✅ Successfully navigated to Pictures section");
        logTestEnd("Проверка перехода в раздел картин");
    }

    @Test
    @DisplayName("Проверка перехода в раздел художники")
    public void checkingTheTransitionToTheArtistSection() {
        logTestStart("Проверка перехода в раздел художники");

        System.out.println("📝 Navigating to Artists section...");
        System.out.println("   Expected result: Page title should be 'Художники'");

        mainPage.checkBackHome()
                .artistSectionFoto()
                .checkingTheArtistTitle();

        System.out.println("✅ Successfully navigated to Artists section");
        logTestEnd("Проверка перехода в раздел художники");
    }

    @Test
    @DisplayName("Проверка перехода в раздел музеи")
    public void checkingTheTransitionToTheMuseumSection() {
        logTestStart("Проверка перехода в раздел музеи");

        System.out.println("📝 Navigating to Museums section...");
        System.out.println("   Expected result: Page title should be 'Музеи'");

        mainPage.checkBackHome()
                .museumSectionFoto()
                .checkingTheMuseumTitle();

        System.out.println("✅ Successfully navigated to Museums section");
        logTestEnd("Проверка перехода в раздел музеи");
    }

    @Test
    @DisplayName("Проверка поиска в разделе музеи")
    public void checkingTheSearchInTheMuseumsSection() {
        String searchQuery = "Третьяковка";
        logTestStart("Проверка поиска в разделе музеи", "Search query", searchQuery);

        System.out.println("📝 Searching for museum: " + searchQuery);
        System.out.println("   Expected result: Museum should be found and displayed");

        mainPage.checkBackHome()
                .museumSectionFoto()
                .search(searchQuery)
                .testingTheSearchByMuseum(searchQuery);

        System.out.println("✅ Museum search works correctly");
        logTestEnd("Проверка поиска в разделе музеи");
    }

    @Test
    @DisplayName("Проверка поиска в разделе музеи - нет данных")
    public void checkingTheSearchInTheMuseumsSectionNoContent() {
        String museumTitle = "Несуществующий музей";
        logTestStart("Проверка поиска в разделе музеи - нет данных", "Search query", museumTitle);

        System.out.println("📝 Searching for non-existent museum: " + museumTitle);
        System.out.println("   Expected result: 'Музеи не найдены' message");

        mainPage.checkBackHome()
                .museumSectionFoto()
                .search(museumTitle)
                .testingNoComtent("Музеи не найдены");

        System.out.println("🔍 Verifying museum does not exist in database...");
        Map<String, Object> dbMuseum = gatewayDb.getMuseumByTitle(museumTitle);
        assertThat(dbMuseum).isEqualTo(Map.of());
        System.out.println("✅ Museum not found in database (as expected)");

        logTestEnd("Проверка поиска в разделе музеи - нет данных");
    }

    @Test
    @DisplayName("Проверка поиска в разделе картины")
    public void checkingTheSearchInThePicturesSection() {
        String searchQuery = "Над вечным покоем";
        logTestStart("Проверка поиска в разделе картины", "Search query", searchQuery);

        System.out.println("📝 Searching for painting: " + searchQuery);
        System.out.println("   Expected result: Painting should be found and displayed");

        mainPage.checkBackHome()
                .paintingSectionFoto()
                .search(searchQuery)
                .testingTheSearchByImage(searchQuery);

        System.out.println("✅ Painting search works correctly");
        logTestEnd("Проверка поиска в разделе картины");
    }

    @Test
    @DisplayName("Проверка поиска в разделе картины - нет данных")
    public void checkingTheSearchInThePicturesSectionNoContent() {
        String paintingTitle = "Несуществующая картина";
        logTestStart("Проверка поиска в разделе картины - нет данных", "Search query", paintingTitle);

        System.out.println("📝 Searching for non-existent painting: " + paintingTitle);
        System.out.println("   Expected result: 'Картины не найдены' message");

        mainPage.checkBackHome()
                .paintingSectionFoto()
                .search(paintingTitle)
                .testingNoComtent("Картины не найдены");

        System.out.println("🔍 Verifying painting does not exist in database...");
        Map<String, Object> dbPainting = gatewayDb.getPaintingByTitle(paintingTitle);
        assertThat(dbPainting).isEqualTo(Map.of());
        System.out.println("✅ Painting not found in database (as expected)");

        logTestEnd("Проверка поиска в разделе картины - нет данных");
    }

    @Test
    @DisplayName("Проверка поиска в разделе художники")
    public void checkingTheSearchInTheArtistSection() {
        String searchQuery = "Шишкин";
        logTestStart("Проверка поиска в разделе художники", "Search query", searchQuery);

        System.out.println("📝 Searching for artist: " + searchQuery);
        System.out.println("   Expected result: Artist should be found and displayed");

        mainPage.checkBackHome()
                .artistSectionFoto()
                .search(searchQuery)
                .testingTheSearchByArtist(searchQuery);

        System.out.println("✅ Artist search works correctly");
        logTestEnd("Проверка поиска в разделе художники");
    }

    @Test
    @DisplayName("Проверка поиска в разделе художники - нет данных")
    public void checkingTheSearchInTheArtistSectionNoContent() {
        String artistName = "Несуществующий художник";
        logTestStart("Проверка поиска в разделе художники - нет данных", "Search query", artistName);

        System.out.println("📝 Searching for non-existent artist: " + artistName);
        System.out.println("   Expected result: 'Художники не найдены' message");

        mainPage.checkBackHome()
                .artistSectionFoto()
                .search(artistName)
                .testingNoComtent("Художники не найдены");

        System.out.println("🔍 Verifying artist does not exist in database...");
        Map<String, Object> dbArtist = gatewayDb.getArtistByName(artistName);
        assertThat(dbArtist).isEqualTo(Map.of());
        System.out.println("✅ Artist not found in database (as expected)");

        logTestEnd("Проверка поиска в разделе художники - нет данных");
    }

    @Test
    @DisplayName("Сверка описания художника с фронта и БД")
    public void verifyArtistDescription() {
        String artistName = "Шишкин";
        logTestStart("Сверка описания художника с фронта и БД", "Artist name", artistName);

        System.out.println("📝 Navigating to artist details page...");
        mainPage.checkBackHome()
                .goToTheArtistsSectionByClickingOnTheTextBelowThePhoto()
                .goToTheShishkinInformationSection();

        System.out.println("📄 Getting artist description from frontend...");
        String frontendDescription = mainPage.getArtistDescriptionFromCurrentPage();
        System.out.println("   Frontend description length: " + frontendDescription.length());

        System.out.println("🔍 Getting artist description from database...");
        Map<String, Object> dbArtist = gatewayDb.getArtistByName(artistName);
        String dbDescription = (String) dbArtist.getOrDefault("biography", "");
        System.out.println("   Database description length: " + dbDescription.length());

        System.out.println("📊 Comparing descriptions...");
        assertThat(frontendDescription).isEqualTo(dbDescription);
        System.out.println("✅ Artist descriptions match!");

        logTestEnd("Сверка описания художника с фронта и БД");
    }

    @Test
    @DisplayName("Сверка описания музея с фронта и БД")
    public void verifyMuseumDescription() {
        String museumTitle = "Третьяковка";
        logTestStart("Сверка описания музея с фронта и БД", "Museum title", museumTitle);

        System.out.println("📝 Navigating to museum details page...");
        mainPage.checkBackHome()
                .goToTheMuseumSectionByClickingOnTheTextBelowThePhoto()
                .goToTheTretyakovGalleryInformationSection();

        System.out.println("📄 Getting museum description from frontend...");
        String frontendDescription = mainPage.getMuseumDescriptionFromCurrentPage();
        System.out.println("   Frontend description length: " + frontendDescription.length());

        System.out.println("🔍 Getting museum description from database...");
        Map<String, Object> dbMuseum = gatewayDb.getMuseumByTitle(museumTitle);
        String dbDescription = (String) dbMuseum.getOrDefault("description", "");
        System.out.println("   Database description length: " + dbDescription.length());

        System.out.println("📊 Comparing descriptions...");
        assertThat(frontendDescription).isEqualTo(dbDescription);
        System.out.println("✅ Museum descriptions match!");

        logTestEnd("Сверка описания музея с фронта и БД");
    }

    @Test
    @DisplayName("Сверка описания картины с фронта и БД")
    public void verifyPaintingDescription() {
        String paintingTitle = "Над вечным покоем";
        logTestStart("Сверка описания картины с фронта и БД", "Painting title", paintingTitle);

        System.out.println("📝 Navigating to painting details page...");
        mainPage.checkBackHome()
                .goToThePaintingSectionByClickingOnTheTextBelowThePhoto()
                .goToTheAboveEternalPeaceInformationSection();

        System.out.println("📄 Getting painting description from frontend...");
        String frontendDescription = mainPage.getPaintingDescriptionFromCurrentPage();
        System.out.println("   Frontend description length: " + frontendDescription.length());

        System.out.println("🔍 Getting painting description from database...");
        Map<String, Object> dbPainting = gatewayDb.getPaintingByTitle(paintingTitle);
        String dbDescription = (String) dbPainting.getOrDefault("description", "");
        System.out.println("   Database description length: " + dbDescription.length());

        System.out.println("📊 Comparing descriptions...");
        assertThat(frontendDescription).isEqualTo(dbDescription);
        System.out.println("✅ Painting descriptions match!");

        logTestEnd("Сверка описания картины с фронта и БД");
    }
}