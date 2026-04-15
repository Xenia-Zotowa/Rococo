package service;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private static final String BASE_URL = "http://localhost:9000";
    private static SessionFilter sessionFilter;

    @BeforeAll
    public static void init() {
        RestAssured.baseURI = BASE_URL;
        sessionFilter = new SessionFilter();
    }

    public static void registerUserIfNotExists(String username, String password) {
        // Получаем страницу регистрации для инициализации сессии
        given()
                .filter(sessionFilter)
                .when()
                .get("/register")
                .then()
                .statusCode(200);

        // Отправляем форму регистрации
        Response response = given()
                .filter(sessionFilter)
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", username)
                .formParam("password", password)
                .formParam("passwordSubmit", password)
                .when()
                .post("/register")
                .then()
                .extract()
                .response();

        // Проверяем результат
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            if (response.getBody().asString().contains("already exists")) {
                System.out.println("User already exists: " + username);
            } else {
                System.out.println("User registered: " + username);
            }
        } else {
            System.out.println("Registration response status: " + response.statusCode());
            System.out.println("Response body: " + response.getBody().asString());
        }
    }
}