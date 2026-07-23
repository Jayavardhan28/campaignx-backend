package com.campaignx.controller;

import com.campaignx.dto.AdminStatsDto;
import com.campaignx.dto.CampaignDto;
import com.campaignx.dto.UserDto;
import com.campaignx.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(adminService.updateUserRole(id, body));
    }

    @GetMapping("/campaigns")
    public ResponseEntity<List<CampaignDto>> getAllCampaigns() {
        return ResponseEntity.ok(adminService.getAllCampaigns());
    }

    @GetMapping("/analytics")
    public ResponseEntity<AdminStatsDto> getAnalytics() {
        return ResponseEntity.ok(adminService.getAnalytics());
    }
    @DeleteMapping("/campaigns/{id}")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long id) {
        adminService.deleteCampaign(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
