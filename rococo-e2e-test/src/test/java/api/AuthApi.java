package api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    @GET("/oauth2/authorize")
    Call<Void> authorize(
            @Query("response_type") String responseType,
            @Query("client_id") String clientId,
            @Query("scope") String scope,
            @Query("redirect_uri") String redirectUri,
            @Query("code_challenge") String codeChallenge,
            @Query("code_challenge_method") String codeChallengeMethod
    );

    @FormUrlEncoded
    @POST("/login")
    Call<Void> login(
            @Field("username") String username,
            @Field("password") String password,
            @Header("X-XSRF-TOKEN") String xsrfToken
    );

    @FormUrlEncoded
    @POST("/register")
    Call<Void> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("confirmedPassword") String confirmedPassword,
            @Header("X-XSRF-TOKEN") String xsrfToken
    );

    @POST("/logout")
    Call<Void> logout(
            @Header("Authorization") String bearerToken
    );

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<JsonNode> token(
            @Field("client_id") String clientId,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("code_verifier") String codeVerifier
    );

    @GET("/register")
    Call<Void> requestRegisterForm();
}
