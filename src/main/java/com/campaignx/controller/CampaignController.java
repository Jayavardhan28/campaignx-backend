package com.campaignx.controller;

import com.campaignx.dto.CampaignDetailDto;
import com.campaignx.dto.CampaignDto;
import com.campaignx.dto.CampaignRequest;
import com.campaignx.dto.GeneratedContentDto;
import com.campaignx.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GeneratedContentDto> generate(@Valid @RequestBody CampaignRequest req) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        GeneratedContentDto response = campaignService.generateAndSaveCampaign(req, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CampaignDto>> getMyCampaigns() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CampaignDto> response = campaignService.getMyCampaigns(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDetailDto> getCampaignDetails(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CampaignDetailDto response = campaignService.getCampaignDetails(id, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        campaignService.deleteCampaign(id, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
