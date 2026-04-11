package service.impl;

import api.GatewayApi;
import config.Config;
import io.qameta.allure.Step;
import model.ArtistJson;
import org.springframework.data.domain.Page;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class GatewayApiClient {

    private static final Config CFG = Config.getInstance();
    private final GatewayApi gatewayApi;
    private final String baseUrl;

    public GatewayApiClient() {
        this.baseUrl = CFG.gatewayUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.gatewayApi = retrofit.create(GatewayApi.class);
    }

    @Step("Send REST GET('/api/artist') request to rococo-gateway")
    @Nonnull
    public Page<ArtistJson> searchArtists(String bearerToken,
                                          String name,
                                          int page,
                                          int size) {
        try {
            Call<Page<ArtistJson>> call = gatewayApi.searchArtists(
                    "Bearer " + bearerToken, name, page, size
            );
            Response<Page<ArtistJson>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
            throw new RuntimeException("Failed to search artists. Code: " + response.code());
        } catch (IOException e) {
            throw new RuntimeException("Error calling searchArtists", e);
        }
    }

    @Step("Send REST GET('/api/artist/{id}') request to rococo-gateway")
    @Nonnull
    public ArtistJson getArtistById(String bearerToken, String id) {
        try {
            Call<ArtistJson> call = gatewayApi.getArtistById("Bearer " + bearerToken, id);
            Response<ArtistJson> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
            throw new RuntimeException("Failed to get artist by id. Code: " + response.code());
        } catch (IOException e) {
            throw new RuntimeException("Error calling getArtistById", e);
        }
    }
}