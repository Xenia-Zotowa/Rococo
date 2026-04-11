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

@WebTest
public class WithAnAuthorizedUserTest {
    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = Selenide.open(CFG.spendUrl(), MainPage.class);
    private final LoginPage loginPage = new LoginPage();
    private final DatabaseHelper gatewayDb = new DatabaseHelper(DatabaseType.GATEWAY);



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
        String username = "admin";
        String newFirstname = "Ivan";
        String newLastname = "Ivanov";
        assertThat(gatewayDb.userExists(username)).isTrue();
        Map<String, Object> userBefore = gatewayDb.getUserByUsername(username);
        System.out.println("Before update - Firstname: " + userBefore.get("firstname") +
                ", Lastname: " + userBefore.get("lastname"));
        mainPage.switchingToTheAuthorizationForm();
        sleep(1000);
        loginPage.loginTest("admin", "123");
        mainPage.checkBackHome()
                .goingToTheUserProfile()
                .changingTheUserFirstAndLastName("Ivan", "Ivanov")
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
