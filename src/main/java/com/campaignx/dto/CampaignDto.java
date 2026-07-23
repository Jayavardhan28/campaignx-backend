package com.campaignx.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CampaignDto {
    private String id;
    private String userId;
    private String brandName;
    private String product;
    private String audience;
    private String goal;
    private String tone;
    private LocalDateTime createdAt;
}
