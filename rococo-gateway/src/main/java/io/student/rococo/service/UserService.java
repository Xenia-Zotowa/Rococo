package io.student.rococo.service;

import io.student.rococo.data.UserEntity;
import io.student.rococo.data.repository.UserRepository;
import io.student.rococo.dto.UserDTO;
import io.student.rococo.exception.ResourceNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Преобразует JWT токен в UserDTO.
     */
    public UserDTO fromJwt(Jwt jwt) {
        var builder = UserDTO.builder()
                .id(getUUID(jwt.getClaims(), "sub"))
                .username(jwt.getClaimAsString("preferred_username"));

        String email = jwt.getClaimAsString("email");
        if (email != null) {
            builder.email(email);
            Object emailVerified = jwt.getClaim("email_verified");
            builder.emailVerified(emailVerified != null ? String.valueOf(emailVerified) : "false");
        }

        return builder.build();
    }

    /**
     * Обновляет данные пользователя.
     */
    @Transactional
    public UserDTO update(UUID id, io.student.rococo.dto.UserPatchDTO patch) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not 	found", id));

        if (patch.getUsername() != null) user.setUsername(patch.getUsername());
        if (patch.getEmail() != null) user.setEmail(patch.getEmail());

        userRepository.save(user);

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .emailVerified(String.valueOf(user.isEmailVerified()))
                .build();
    }

    private UUID getUUID(Map<String, Object> claims, String claimName) {
        try {
            var claim = claims.get(claimName);
            if (claim != null) {
                return UUID.fromString(claim.toString());
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}