package api;


import model.ArtistJson;
import org.springframework.data.domain.Page;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GatewayApi {

    @GET("/api/artist")
    Call<Page<ArtistJson>> searchArtists(
            @Header("Authorization") String bearerToken,
            @Query("name") String name,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("/api/artist/{id}")
    Call<ArtistJson> getArtistById(
            @Header("Authorization") String bearerToken,
            @Path("id") String id
    );
}