package com.campaignx.dto;

import lombok.Data;

@Data
public class AdminStatsDto {
    private Long totalUsers;
    private Long totalCampaigns;
    private Long activeUsersThisWeek;
}
