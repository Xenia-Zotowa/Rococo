package io.student.rococo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import io.student.rococo.service.cors.CorsCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final CorsCustomizer corsCustomizer;
    private final String frontUri;

    @Autowired
    public SecurityConfiguration(CorsCustomizer corsCustomizer,
                                 @Value("${rococo-front.base-uri}") String frontUri) {
        this.corsCustomizer = corsCustomizer;
        this.frontUri = frontUri;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/api/session").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/artist/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/museum/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/painting/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(cookieTokenResolver())
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        corsCustomizer.corsCustomizer(http);
        return http.build();
    }

    @Bean
    public BearerTokenResolver cookieTokenResolver() {
        return request -> {
            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                Cookie[] cookies = httpRequest.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("access_token_lf".equals(cookie.getName())) {
                            return cookie.getValue();
                        }
                    }
                }
            }
            return new DefaultBearerTokenResolver().resolve(request);
        };
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}