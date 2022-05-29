package com.authService.controller;

import com.authService.model.dto.LoginDTO;
import com.authService.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class AuthController {

    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO data) {
        String username = data.getUsername();
        appUserService.loadUserByUsername(username);
        Map<Object, Object> model = new HashMap<>();
        model.put("username", username);
        return ResponseEntity.ok(model);
    }
}
