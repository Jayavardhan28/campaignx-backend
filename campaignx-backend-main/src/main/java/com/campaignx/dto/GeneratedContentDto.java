package com.campaignx.dto;

import lombok.Data;
import java.util.List;

@Data
public class GeneratedContentDto {
    private String campaignId;
    private String linkedinPost;
    private String instagramCaption;
    private List<String> seoKeywords;
    private String imagePrompt;
}
