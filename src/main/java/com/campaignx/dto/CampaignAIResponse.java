package com.campaignx.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignAIResponse {

    private String content;

    private String keywords;

    private String hashtags;

    private String imagePrompt;

    private String review;

    private String status;
}