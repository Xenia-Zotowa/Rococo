package io.student.rococo.service;

import io.student.rococo.data.UserEntity;
import io.student.rococo.data.repository.UserRepository;
import io.student.rococo.dto.UserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO fromJwt(Jwt jwt) {
        var builder = UserDTO.builder()
                .id(getUUID(jwt.getClaims(), "sub"))
                .username(jwt.getClaimAsString("preferred_username"));

        String email = jwt.getClaimAsString("email");
        if (email != null) {
            builder.email(email);
            builder.emailVerified(String.valueOf(jwt.getClaim("email_verified")));
        }

        return builder.build();
    }

    private java.util.UUID getUUID(Object claims, String claimName) {
        try {
            var claim = (String) ((java.util.Map)claims).get(claimName);
            if (claim != null) {
                return java.util.UUID.fromString(claim);
            }
        } catch (Exception ignored) {}
        return null;
    }
}