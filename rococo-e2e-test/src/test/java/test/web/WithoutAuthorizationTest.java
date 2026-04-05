package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.MainPage;

import static com.codeborne.selenide.Selenide.sleep;

@WebTest
public class WithoutAuthorizationTest {
    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);


    @Test
    @DisplayName("Проверка невозможности добавления картинки без авторизации")
    public void checkingIfItIsImpossibleToAddAnImageWithoutAuthorization(){

        mainPage.chekBackHome()
                .paintingSection()
                .chekAddAPicture();
    }

    @Test
    @DisplayName("Проверка невозможности добавления художника без авторизации")
    public void checkingIfItIsImpossibleToAddAnArtistWithoutAuthorization(){

        mainPage.chekBackHome()
                .chekAddAArtist()
                .chekAddAArtist();
    }
}
