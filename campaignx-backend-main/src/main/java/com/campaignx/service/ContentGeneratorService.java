package com.campaignx.service;

import com.campaignx.dto.CampaignRequest;
import com.campaignx.dto.GeneratedContentDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContentGeneratorService {

    public GeneratedContentDto generate(CampaignRequest req) {
        String brand = req.getBrandName();
        String product = req.getProduct();
        String audience = req.getAudience();
        String goal = req.getGoal();
        String tone = req.getTone();

        String linkedinPost = String.format(
                "🚀 Introducing %s! Experience %s, designed specifically for %s. This %s campaign is built around %s — join us on the journey. #%s",
                brand, product, audience, tone.toLowerCase(), goal.toLowerCase(), brand.replaceAll("\\s", ""));

        String instagramCaption = String.format(
                "✨ %s x %s — made for %s. #%s #%s",
                brand, product, audience, goal.replaceAll("\\s", ""), tone.replaceAll("\\s", ""));

        List<String> seoKeywords = List.of(
                brand.toLowerCase(), product.toLowerCase(), goal.toLowerCase(),
                tone.toLowerCase(), audience.toLowerCase(), "social media marketing", "brand campaign");

        String imagePrompt = String.format(
                "A %s, high-quality marketing image for %s's %s, targeting %s, evoking a %s mood, vibrant brand colors, professional studio lighting",
                tone.toLowerCase(), brand, product, audience, goal.toLowerCase());

        GeneratedContentDto dto = new GeneratedContentDto();
        dto.setLinkedinPost(linkedinPost);
        dto.setInstagramCaption(instagramCaption);
        dto.setSeoKeywords(seoKeywords);
        dto.setImagePrompt(imagePrompt);
        return dto;
    }
}
