package com.campaignx.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CampaignRequest {
    @NotBlank(message = "Brand name is required")
    private String brandName;

    @NotBlank(message = "Product is required")
    private String product;

    @NotBlank(message = "Audience is required")
    private String audience;

    @NotBlank(message = "Goal is required")
    private String goal;

    @NotBlank(message = "Tone is required")
    private String tone;
}
