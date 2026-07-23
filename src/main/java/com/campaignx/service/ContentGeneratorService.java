package com.campaignx.service;

import com.campaignx.dto.CampaignAIRequest;
import com.campaignx.dto.CampaignAIResponse;
import com.campaignx.dto.CampaignRequest;
import com.campaignx.dto.GeneratedContentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentGeneratorService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String AI_URL = "http://localhost:8000/generate";

    public GeneratedContentDto generate(CampaignRequest req) {

        // Prepare request for Python AI
        CampaignAIRequest aiRequest = new CampaignAIRequest();
        aiRequest.setProduct(req.getProduct());
        aiRequest.setBrand_name(req.getBrandName());
        aiRequest.setBrand_target(req.getGoal());
        aiRequest.setAudience(req.getAudience());
        aiRequest.setTone(req.getTone());

        // Call FastAPI
        CampaignAIResponse aiResponse =
                restTemplate.postForObject(
                        AI_URL,
                        aiRequest,
                        CampaignAIResponse.class
                );

        if (aiResponse == null) {
            throw new RuntimeException("Failed to get response from AI service.");
        }

        // Convert keywords String -> List<String>
        List<String> keywords = Arrays.stream(aiResponse.getKeywords().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        // Prepare response for frontend
        GeneratedContentDto dto = new GeneratedContentDto();

        dto.setLinkedinPost(aiResponse.getContent());

        // Same content for Instagram for now
        dto.setInstagramCaption(aiResponse.getContent());

        dto.setSeoKeywords(keywords);

        dto.setImagePrompt(aiResponse.getImagePrompt());

        return dto;
    }
}