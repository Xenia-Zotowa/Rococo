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


    @Test
    @DisplayName("Проверка невозможности добавления картинки без авторизации")
    public void checkingIfItIsImpossibleToAddAnImageWithoutAuthorization() {

        mainPage.checkBackHome()
                .paintingSection()
                .checkAddAPicture();
    }

    @Test
    @DisplayName("Проверка невозможности добавления художника без авторизации")
    public void checkingIfItIsImpossibleToAddAnArtistWithoutAuthorization() {

        mainPage.checkBackHome()
                .artistSection()
                .checkAddAArtist();
    }

    @Test
    @DisplayName("Проверка невозможности добавления музея без авторизации")
    public void checkingIfItIsImpossibleToAddAnMuseumWithoutAuthorization() {

        mainPage.checkBackHome()
                .museumSection()
                .checkAddAMuseum();
    }

    @Test
    @DisplayName("Проверка переключения тогла")
    public void checkingtheToggleSwitch() {

        mainPage.checkBackHome()
                .switchingToggle()
                .checkToggleDark()
                .switchingToggle()
                .checkToggleLight()
                .switchingToggle();
    }

    @Test
    @DisplayName("Проверка перехода в раздел картин")
    public void checkingTheTransitionToThePicturesSection() {

        mainPage.checkBackHome()
                .paintingSectionFoto()
                .checkingThePictureTitle();
    }

    @Test
    @DisplayName("Проверка перехода в раздел художники")
    public void checkingTheTransitionToTheArtistSection() {

        mainPage.checkBackHome()
                .artistSectionFoto()
                .checkingTheArtistTitle();
    }

    @Test
    @DisplayName("Проверка перехода в раздел музеи")
    public void checkingTheTransitionToTheMuseumSection() {

        mainPage.checkBackHome()
                .museumSectionFoto()
                .checkingTheMuseumTitle();
    }

    @Test
    @DisplayName("Проверка поиска в разделе музеи")
    public void checkingTheSearchInTheMuseumsSection() {

        mainPage.checkBackHome()
                .museumSectionFoto()
                .search("Третьяковка")
                .testingTheSearchByMuseum("Третьяковка");
    }

    @Test
    @DisplayName("Проверка поиска в разделе музеи - нет данных")
    public void checkingTheSearchInTheMuseumsSectionNoContent() {
        String museumTitle = "Несуществующий музей";
        mainPage.checkBackHome()
                .museumSectionFoto()
                .search(museumTitle)
                .testingNoComtent("Музеи не найдены");
        Map<String, Object> dbMuseum = gatewayDb.getMuseumByTitle(museumTitle);
        assertThat(dbMuseum).isEqualTo(Map.of());
    }

    @Test
    @DisplayName("Проверка поиска в разделе картины")
    public void checkingTheSearchInThePicturesSection() {

        mainPage.checkBackHome()
                .paintingSectionFoto()
                .search("Над вечным покоем")
                .testingTheSearchByImage("Над вечным покоем");
    }

    @Test
    @DisplayName("Проверка поиска в разделе картины - нет данных")
    public void checkingTheSearchInThePicturesSectionNoContent() {
        String paintingTitle = "Несуществующая картина";
        mainPage.checkBackHome()
                .paintingSectionFoto()
                .search(paintingTitle)
                .testingNoComtent("Картины не найдены");
        Map<String, Object> dbPainting = gatewayDb.getPaintingByTitle(paintingTitle);
        assertThat(dbPainting).isEqualTo(Map.of());
    }

    @Test
    @DisplayName("Проверка поиска в разделе художники")
    public void checkingTheSearchInTheArtistSection() {

        mainPage.checkBackHome()
                .artistSectionFoto()
                .search("Шишкин")
                .testingTheSearchByArtist("Шишкин");
    }

    @Test
    @DisplayName("Проверка поиска в разделе художники - нет данных")
    public void checkingTheSearchInTheArtistSectionNoContent() {
        String artistName = "Несуществующий художник";
        mainPage.checkBackHome()
                .artistSectionFoto()
                .search(artistName)
                .testingNoComtent("Художники не найдены");
        Map<String, Object> dbArtist = gatewayDb.getArtistByName(artistName);
        assertThat(dbArtist).isEqualTo(Map.of());
    }

    @Test
    @DisplayName("Сверка описания художника с фронта и БД")
    public void verifyArtistDescription() {
        String artistName = "Шишкин";

        mainPage.checkBackHome()
                .goToTheArtistsSectionByClickingOnTheTextBelowThePhoto()
                .goToTheShishkinInformationSection()
                .getCurrentPageHtml();

        String frontendDescription = mainPage.getArtistDescriptionFromCurrentPage();

        Map<String, Object> dbArtist = gatewayDb.getArtistByName(artistName);
        String dbDescription = (String) dbArtist.getOrDefault("biography", "");

        assertThat(frontendDescription).isEqualTo(dbDescription);
    }

    @Test
    @DisplayName("Сверка описания музея с фронта и БД")
    public void verifyMuseumDescription() {
        String museumTitle = "Третьяковка";

        mainPage.checkBackHome()
                .goToTheMuseumSectionByClickingOnTheTextBelowThePhoto()
                .goToTheTretyakovGalleryInformationSection()
                .getCurrentPageHtml();

        String frontendDescription = mainPage.getMuseumDescriptionFromCurrentPage();

        Map<String, Object> dbMuseum = gatewayDb.getMuseumByTitle(museumTitle);
        String dbDescription = (String) dbMuseum.getOrDefault("description", "");

        assertThat(frontendDescription).isEqualTo(dbDescription);
    }

    @Test
    @DisplayName("Сверка описания картины с фронта и БД")
    public void verifyPaintingDescription() {
        String museumTitle = "Над вечным покоем";

        mainPage.checkBackHome()
                .goToThePaintingSectionByClickingOnTheTextBelowThePhoto()
                .goToTheAboveEternalPeaceInformationSection()
                .getCurrentPageHtml();

        String frontendDescription = mainPage.getPaintingDescriptionFromCurrentPage();

        Map<String, Object> dbPainting = gatewayDb.getPaintingByTitle(museumTitle);
        String dbDescription = (String) dbPainting.getOrDefault("description", "");

        assertThat(frontendDescription).isEqualTo(dbDescription);
    }


}
