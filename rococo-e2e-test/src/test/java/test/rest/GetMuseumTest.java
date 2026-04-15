package test.rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.helper.DatabaseHelper;
import test.helper.DatabaseType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class GetMuseumTest {
    private final DatabaseHelper gatewayDb = new DatabaseHelper(DatabaseType.GATEWAY);

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:9001";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Проверка получения информации по музеям")
    void getMuseumTest() {
        Response response = given()
                .log().all()
                .get("/museum")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Map<String, Object>> apiMuseums = response.jsonPath().getList("content");
        List<Map<String, Object>> dbMuseums = gatewayDb.getAllMuseums();

        assertThat(apiMuseums.size())
                .as("Количество музеев в API не совпадает с БД")
                .isEqualTo(dbMuseums.size());

        Map<String, Map<String, Object>> dbMuseumsMap = dbMuseums.stream()
                .collect(Collectors.toMap(
                        museum -> (String) museum.get("title"),
                        museum -> museum,
                        (existing, replacement) -> existing
                ));

        for (Map<String, Object> apiMuseum : apiMuseums) {
            String apiTitle = (String) apiMuseum.get("title");
            Map<String, Object> dbMuseum = dbMuseumsMap.get(apiTitle);

            assertThat(dbMuseum)
                    .as("Музей с названием '%s' не найден в базе данных", apiTitle)
                    .isNotNull();

            assertThat(apiMuseum.get("description"))
                    .as("Описание не совпадает для музея '%s'", apiTitle)
                    .isEqualTo(dbMuseum.get("description"));


            if (apiMuseum.containsKey("id")) {
                assertThat(apiMuseum.get("id"))
                        .as("ID не совпадает для музея '%s'", apiTitle)
                        .isEqualTo(dbMuseum.get("id"));
            }
        }
    }

    @Test
    @DisplayName("Проверка получения кода ответа 401")
    void getMuseumTestError() {
        Response response = given()
                .log().all()
                .get("/muse")
                .then()
                .log().all()
                .statusCode(401)
                .extract().response();
    }
}
