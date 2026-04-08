package io.student.rococo.controller;

import io.student.rococo.dto.SessionDTO;
import io.student.rococo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SessionRestController {
    private final UserService userService;

    public SessionRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/session")
    public ResponseEntity<SessionDTO> getSession(Authentication auth) {
        Jwt jwt = ((Jwt) auth.getPrincipal());
        return ResponseEntity.ok(SessionDTO.builder()
                .id(java.util.UUID.fromString(jwt.getClaimAsString("jti")))
                .userId(java.util.UUID.fromString(jwt.getClaimAsString("sub")))
                .createdAt(String.valueOf(System.currentTimeMillis()))
                .expiresAt(String.valueOf(System.currentTimeMillis() + 3600000))
                .build());
    }

}