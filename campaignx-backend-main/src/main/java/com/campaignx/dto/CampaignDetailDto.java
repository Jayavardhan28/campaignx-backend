package com.campaignx.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CampaignDetailDto extends CampaignDto {
    private String linkedinPost;
    private String instagramCaption;
    private List<String> seoKeywords;
    private String imagePrompt;
}
