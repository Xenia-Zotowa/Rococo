package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.MainPage;

@WebTest
public class WithoutAuthorizationTest {
    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);


    @Test
    @DisplayName("Проверка невозможности добавления картинки без авторизации")
    public void checkingIfItIsImpossibleToAddAnImageWithoutAuthorization(){

        mainPage.checkBackHome()
                .paintingSection()
                .checkAddAPicture();
    }

    @Test
    @DisplayName("Проверка невозможности добавления художника без авторизации")
    public void checkingIfItIsImpossibleToAddAnArtistWithoutAuthorization(){

        mainPage.checkBackHome()
                .artistSection()
                .checkAddAArtist();
    }

    @Test
    @DisplayName("Проверка невозможности добавления музея без авторизации")
    public void checkingIfItIsImpossibleToAddAnMuseumWithoutAuthorization(){

        mainPage.checkBackHome()
                .museumSection()
                .checkAddAMuseum();
    }

    @Test
    @DisplayName("Проверка переключения тогла")
    public void checkingtheToggleSwitch(){

        mainPage.checkBackHome()
                .switchingToggle()
                .checkToggleDark()
                .switchingToggle()
                .checkToggleLight()
                .switchingToggle();
    }

    @Test
    @DisplayName("Проверка перехода в раздел картин")
    public void checkingTheTransitionToThePicturesSection(){

        mainPage.checkBackHome()
                .paintingSectionFoto()
                .checkingThePictureTitle();
    }

    @Test
    @DisplayName("Проверка перехода в раздел художники")
    public void checkingTheTransitionToTheArtistSection(){

        mainPage.checkBackHome()
                .artistSectionFoto()
                .checkingTheArtistTitle();
    }

    @Test
    @DisplayName("Проверка перехода в раздел музеи")
    public void checkingTheTransitionToTheMuseumSection(){

        mainPage.checkBackHome()
                .museumSectionFoto()
                .checkingTheMuseumTitle();
    }
}
