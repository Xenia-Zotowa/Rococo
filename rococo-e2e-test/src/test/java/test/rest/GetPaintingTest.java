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

public class GetPaintingTest {
    private final DatabaseHelper gatewayDb = new DatabaseHelper(DatabaseType.GATEWAY);

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:9001";
        RestAssured.basePath = "/api";
    }

    @Test
    @DisplayName("Проверка получения информации о картинах")
    void getPaintingTest() {
        Response response = given()
                .log().all()
                .get("/painting")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Map<String, Object>> apiPaintings = response.jsonPath().getList("content");

        List<Map<String, Object>> dbPaintings = gatewayDb.getAllPaintings();

        assertThat(apiPaintings.size())
                .as("Количество картин в API не совпадает с БД")
                .isEqualTo(dbPaintings.size());

        Map<String, Map<String, Object>> dbPaintingsMap = dbPaintings.stream()
                .collect(Collectors.toMap(
                        painting -> (String) painting.get("title"),
                        painting -> painting,
                        (existing, replacement) -> existing
                ));

        for (Map<String, Object> apiPainting : apiPaintings) {
            String apiTitle = (String) apiPainting.get("title");
            Map<String, Object> dbPainting = dbPaintingsMap.get(apiTitle);

            assertThat(dbPainting)
                    .as("Картина с названием '%s' не найдена в базе данных", apiTitle)
                    .isNotNull();

            assertThat(apiPainting.get("description"))
                    .as("Описание не совпадает для картины '%s'", apiTitle)
                    .isEqualTo(dbPainting.get("description"));

            if (apiPainting.containsKey("id")) {
                assertThat(apiPainting.get("id"))
                        .as("ID не совпадает для картины '%s'", apiTitle)
                        .isEqualTo(dbPainting.get("id"));
            }
        }
    }

    @Test
    @DisplayName("Проверка получения кода ответа 401")
    void getPaintingTestError() {
        Response response = given()
                .log().all()
                .get("/paintin")
                .then()
                .log().all()
                .statusCode(401)
                .extract().response();
    }
}
