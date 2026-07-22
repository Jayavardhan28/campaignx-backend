package com.campaignx.controller;

import com.campaignx.dto.UserDto;
import com.campaignx.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto profile = userService.getProfile(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(@RequestBody Map<String, String> updates) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto updated = userService.updateProfile(email, updates);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> req) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String currentPassword = req.get("currentPassword");
        String newPassword = req.get("newPassword");
        userService.changePassword(email, currentPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
