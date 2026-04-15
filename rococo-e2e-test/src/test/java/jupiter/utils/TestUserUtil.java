package jupiter.utils;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.RegistrationForm;

import static io.restassured.RestAssured.given;

public class TestUserUtil {
    private static final String REGISTER_ENDPOINT = "http://localhost:9000/register";

    public static Response register(RegistrationForm registrationForm) {
        return given()
                .contentType(ContentType.JSON)
                .body(registrationForm)
                .when()
                .post(REGISTER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public static void createTestUser(String username, String password) {
        RegistrationForm form = new RegistrationForm(username, password, null);
        Response response = register(form);

        if (response.statusCode() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getBody().asString());
        }
    }
}
