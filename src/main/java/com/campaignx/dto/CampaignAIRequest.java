package com.campaignx.dto;

import lombok.Data;

@Data
public class CampaignAIRequest {

    private String product;

    private String brand_name;

    private String brand_target;

    private String audience;

    private String tone;
}