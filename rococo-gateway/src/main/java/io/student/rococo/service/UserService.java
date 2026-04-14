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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Преобразует JWT токен в UserDTO.
     */
    public UserDTO fromJwt(Jwt jwt) {
        var builder = UserDTO.builder()
                .username(jwt.getClaimAsString("sub"));

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
    public UserDTO update(String username, io.student.rococo.dto.UserPatchDTO patch) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setUsername(username);
                    newUser.setEmail(patch.getEmail()); // Assuming email from patch is valid if new
                    newUser.setEmailVerified(true);
                    return newUser;
                });

        if (patch.getUsername() != null) user.setUsername(patch.getUsername());
        if (patch.getEmail() != null) user.setEmail(patch.getEmail());
        if (patch.getFirstname() != null) user.setFirstname(patch.getFirstname());
        if (patch.getLastname() != null) user.setLastname(patch.getLastname());
        if (patch.getAvatar() != null) user.setAvatar(patch.getAvatar().getBytes());

        userRepository.save(user);

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .avatar(user.getAvatar())
                .emailVerified(String.valueOf(user.isEmailVerified()))
                .build();
    }

    private UUID getUUID(Map<String, Object> claims, String claimName) {
        try {
            var claim = claims.get(claimName);
            if (claim != null) {
                return UUID.fromString(claim.toString());
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to parse UUID from claim: " + claimName, e);
        }
        return null;
    }
}