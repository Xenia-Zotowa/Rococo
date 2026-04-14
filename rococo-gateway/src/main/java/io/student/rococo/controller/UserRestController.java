package io.student.rococo.controller;

import io.student.rococo.dto.PageableResponse;
import io.student.rococo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<io.student.rococo.dto.UserDTO> getUser(Authentication auth) {
        var userDto = userService.fromJwt(((org.springframework.security.oauth2.jwt.Jwt) auth.getPrincipal()));
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/user")
    public ResponseEntity<io.student.rococo.dto.UserDTO> updateUser(Authentication auth, @Valid @RequestBody io.student.rococo.dto.UserPatchDTO patch) {
        var username = ((org.springframework.security.oauth2.jwt.Jwt) auth.getPrincipal()).getSubject();
        var updatedUser = userService.update(username, patch);
        return ResponseEntity.ok(updatedUser);
    }
}