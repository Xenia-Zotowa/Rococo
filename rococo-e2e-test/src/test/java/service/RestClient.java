package service;

import api.core.JavaNetCookieJar;
import api.core.ThreadSafeCookieStore;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class RestClient {

    protected final Retrofit retrofit;
    protected final String baseUrl;

    public RestClient(String baseUrl) {
        this(baseUrl, false);
    }

    public RestClient(String baseUrl, boolean followRedirects) {
        this(baseUrl, followRedirects, null);
    }

    public RestClient(String baseUrl, boolean followRedirects, okhttp3.Interceptor interceptor) {
        this.baseUrl = baseUrl;

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(ThreadSafeCookieStore.INSTANCE))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .followRedirects(followRedirects)
                .followSslRedirects(followRedirects);

        if (interceptor != null) {
            httpClientBuilder.addInterceptor(interceptor);
        }

        OkHttpClient okHttpClient = httpClientBuilder.build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    protected <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    protected <T> T executeForBody(Call<T> call, int expectedStatusCode) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful() && response.code() == expectedStatusCode && response.body() != null) {
                return response.body();
            }
            throw new RuntimeException("Request failed. Expected status: " + expectedStatusCode +
                    ", but was: " + response.code());
        } catch (IOException e) {
            throw new RuntimeException("Error executing request", e);
        }
    }

    protected void executeNoBody(Call<Void> call, int expectedStatusCode) {
        try {
            Response<Void> response = call.execute();
            if (!response.isSuccessful() || response.code() != expectedStatusCode) {
                throw new RuntimeException("Request failed. Expected status: " + expectedStatusCode +
                        ", but was: " + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error executing request", e);
        }
    }
}