import jupiter.annotation.ApiLogin;
import jupiter.annotation.Token;
import jupiter.annotation.User;
import jupiter.annotation.meta.RestTest;
import model.ArtistJson;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import service.impl.GatewayApiClient;


import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@RestTest
public class ArtistRestTest {

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @Test
    @User
    @ApiLogin
    void shouldFindArtistByName(@Token String bearerToken) {
        // Given
        String searchName = "Shishkin";
        int page = 0;
        int size = 18;

        // When
        Page<ArtistJson> artists = gatewayApiClient.searchArtists(
                bearerToken,
                searchName,
                page,
                size
        );

        // Then
        assertNotNull(artists, "Response should not be null");
        assertNotNull(artists.getContent(), "Content should not be null");

        if (!artists.isEmpty()) {
            ArtistJson firstArtist = artists.getContent().get(0);
            assertNotNull(firstArtist.name(), "Artist name should not be null");
        }
    }

    @Test
    @User(username = "custom_user", password = "custom_pass")
    @ApiLogin(username = "custom_user", password = "custom_pass")
    void shouldFindArtistWithCustomUser(@Token String bearerToken) {
        // Given
        String searchName = "Renoir";

        // When
        Page<ArtistJson> artists = gatewayApiClient.searchArtists(
                bearerToken,
                searchName,
                0,
                10
        );

        // Then
        assertNotNull(artists);
    }
}