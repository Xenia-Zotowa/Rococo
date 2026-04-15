package test.rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.helper.DatabaseHelper;
import test.helper.DatabaseType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class GetArtistTest {

    private final DatabaseHelper gatewayDb = new DatabaseHelper(DatabaseType.GATEWAY);

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:9001";
        RestAssured.basePath = "/api";
    }

    @Test
    void getArtistTest() {
        Response response = given()
                .log().all()
                .get("/artist")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Map<String, Object>> apiArtists = response.jsonPath().getList("content");
        List<Map<String, Object>> dbArtists = gatewayDb.getAllArtists();

        assertThat(apiArtists.size())
                .as("Number of artists doesn't match database")
                .isEqualTo(dbArtists.size());

        Map<String, Map<String, Object>> dbArtistsMap = dbArtists.stream()
                .collect(Collectors.toMap(
                        artist -> (String) artist.get("name"),
                        artist -> artist
                ));

        for (Map<String, Object> apiArtist : apiArtists) {
            String apiName = (String) apiArtist.get("name");
            Map<String, Object> dbArtist = dbArtistsMap.get(apiName);

            assertThat(dbArtist)
                    .as("Artist with name '%s' not found in database", apiName)
                    .isNotNull();

            assertThat(apiArtist.get("biography"))
                    .as("Biography mismatch for artist '%s'", apiName)
                    .isEqualTo(dbArtist.get("biography"));
        }
    }

    @Test
    void getArtistTestError() {
        Response response = given()
                .log().all()
                .get("/artis")
                .then()
                .log().all()
                .statusCode(401)
                .extract().response();


    }
}
