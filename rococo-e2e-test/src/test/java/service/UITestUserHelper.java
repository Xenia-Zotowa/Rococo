package service;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class UITestUserHelper {

    public static void registerUserViaUI(String username, String password) {
        open("http://localhost:9000/register");

        $("#username").setValue(username);
        $("#password").setValue(password);
        $("#passwordSubmit").setValue(password);
        $("button[type='submit']").click();

        // Проверяем успешность регистрации
        if ($(".form__error").exists()) {
            String error = $(".form__error").getText();
            if (!error.contains("already exists")) {
                throw new RuntimeException("Registration failed: " + error);
            }
        }
    }
}