package com.campaignx.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.campaignx.dto.CampaignAIRequest;
import com.campaignx.dto.CampaignAIResponse;

@Service
public class AIService {

    private final RestTemplate restTemplate;

    // AI FastAPI URL
    private static final String AI_URL = "http://localhost:8000/generate";

    public AIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CampaignAIResponse generateCampaign(CampaignAIRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CampaignAIRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<CampaignAIResponse> response =
                restTemplate.postForEntity(
                        AI_URL,
                        entity,
                        CampaignAIResponse.class
                );

        return response.getBody();
    }
}