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
public class WithAnAuthorizedUserTest {
    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
    private final LoginPage loginPage = new LoginPage();

//    @Test
//    @DisplayName("Редактирование картин")
//    public void pictureEditing(){
//        mainPage.switchingToTheAuthorizationForm();
//        sleep(1000);
//        loginPage.loginTest("admin", "123");
//        mainPage.checkBackHome()
//                .paintingSection()
//                .addAPicture();
//    }

    @Test
    @DisplayName("Проверка разлогинивания")
    public void cheklogOut(){
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome()
                .logOut()
                .сheckingTheVisibilityOfTheLoginButton();
    }


    @Test
    @DisplayName("Редактирование профиля")
    public void profileEditing(){
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome()
                .goingToTheUserProfile()
                .changingTheUserFirstAndLastName("Ivan", "Ivanov")
                .updateYourFotoProfile()
                .updateYourProfile();
    }

}
