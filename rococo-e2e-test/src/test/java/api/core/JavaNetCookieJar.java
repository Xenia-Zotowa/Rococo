package api.core;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import javax.annotation.Nonnull;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaNetCookieJar implements CookieJar {

    private final CookieManager cookieManager;

    public JavaNetCookieJar() {
        this.cookieManager = new CookieManager();
        this.cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }

    public JavaNetCookieJar(CookieStore cookieStore) {
        this.cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
    }

    @Override
    public void saveFromResponse(@Nonnull HttpUrl url, @Nonnull List<Cookie> cookies) {
        URI uri = url.uri();
        List<HttpCookie> httpCookies = cookies.stream()
                .map(this::toHttpCookie)
                .collect(Collectors.toList());
        cookieManager.getCookieStore().add(uri, httpCookies);
    }

    @Override
    @Nonnull
    public List<Cookie> loadForRequest(@Nonnull HttpUrl url) {
        URI uri = url.uri();
        List<HttpCookie> httpCookies = cookieManager.getCookieStore().get(uri);

        return httpCookies.stream()
                .map(this::toOkCookie)
                .collect(Collectors.toList());
    }

    private HttpCookie toHttpCookie(Cookie cookie) {
        HttpCookie httpCookie = new HttpCookie(cookie.name(), cookie.value());
        httpCookie.setDomain(cookie.domain());
        httpCookie.setPath(cookie.path());
        httpCookie.setSecure(cookie.secure());
        httpCookie.setHttpOnly(cookie.httpOnly());
        httpCookie.setMaxAge(cookie.expiresAt() / 1000);
        httpCookie.setVersion(0);
        return httpCookie;
    }

    private Cookie toOkCookie(HttpCookie httpCookie) {
        return new Cookie.Builder()
                .name(httpCookie.getName())
                .value(httpCookie.getValue())
                .domain(httpCookie.getDomain())
                .path(httpCookie.getPath())
                .secure(httpCookie.getSecure())
                .httpOnly(httpCookie.isHttpOnly())
                .expiresAt(System.currentTimeMillis() + (httpCookie.getMaxAge() * 1000))
                .build();
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }
}
