package io.student.rococo.controller;

import io.student.rococo.dto.UserDTO;
import io.student.rococo.dto.SessionDTO;
import io.student.rococo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SessionRestController {
    private final UserService userService;

    public SessionRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/session")
    public ResponseEntity<SessionDTO> getSession(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(SessionDTO.builder()
                .id(jwt.getClaimAsString("jti"))
                .userId(jwt.getClaimAsString("sub"))
                .createdAt(String.valueOf(System.currentTimeMillis()))
                .expiresAt(String.valueOf(System.currentTimeMillis() + 3600000))
                .build());
    }

    @GetMapping("/session/user")
    public ResponseEntity<UserDTO> getUser(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("sub");
        return ResponseEntity.ok(UserDTO.builder()
                .username(username)
                .email(email)
                .build());
    }
}