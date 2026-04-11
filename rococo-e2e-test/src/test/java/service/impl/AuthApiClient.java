package service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import api.AuthApi;
import core.CodeInterceptor;
import core.ThreadSafeCookieStore;
import config.Config;
import jupiter.extension.ApiLoginExtension;

import service.RestClient;
import utils.OAuthUtils;
import lombok.SneakyThrows;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public final class AuthApiClient extends RestClient {

    private static final Config CFG = Config.getInstance();
    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl(), true, new CodeInterceptor());
        this.authApi = create(AuthApi.class);
    }

    @SneakyThrows
    public String login(String username, String password) {
        final String codeVerifier = OAuthUtils.generateCodeVerifier();
        final String codeChallenge = OAuthUtils.generateCodeChallenge(codeVerifier);
        final String redirectUri = CFG.frontUrl() + "authorized";
        final String clientId = "client";

        // Шаг 1: Авторизация - получаем форму логина
        authApi.authorize(
                "code",
                clientId,
                "openid",
                redirectUri,
                codeChallenge,
                "S256"
        ).execute();

        // Шаг 2: Логин - отправляем учетные данные
        authApi.login(
                username,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        ).execute();

        // Шаг 3: Получаем токен
        Response<JsonNode> tokenResponse = authApi.token(
                clientId,
                redirectUri,
                "authorization_code",
                ApiLoginExtension.getCode(),
                codeVerifier
        ).execute();

        if (tokenResponse.isSuccessful() && tokenResponse.body() != null) {
            return tokenResponse.body().get("id_token").asText();
        }

        throw new RuntimeException("Failed to get token. Response code: " + tokenResponse.code());
    }

    public Response<Void> register(String username, String password) throws IOException {
        // Шаг 1: Получаем форму регистрации
        authApi.requestRegisterForm().execute();

        // Шаг 2: Регистрируем пользователя
        return authApi.register(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        ).execute();
    }

    public Response<Void> logout(String bearerToken) throws IOException {
        return authApi.logout(bearerToken).execute();
    }
}
