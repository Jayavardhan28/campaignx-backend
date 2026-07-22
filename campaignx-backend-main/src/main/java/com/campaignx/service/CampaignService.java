package com.campaignx.service;

import com.campaignx.dto.*;
import com.campaignx.entity.Campaign;
import com.campaignx.entity.GeneratedContent;
import com.campaignx.entity.User;
import com.campaignx.repository.CampaignRepository;
import com.campaignx.repository.GeneratedContentRepository;
import com.campaignx.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final GeneratedContentRepository generatedContentRepository;
    private final UserRepository userRepository;
    private final ContentGeneratorService contentGeneratorService;

    public CampaignService(CampaignRepository campaignRepository,
            GeneratedContentRepository generatedContentRepository,
            UserRepository userRepository,
            ContentGeneratorService contentGeneratorService) {
        this.campaignRepository = campaignRepository;
        this.generatedContentRepository = generatedContentRepository;
        this.userRepository = userRepository;
        this.contentGeneratorService = contentGeneratorService;
    }

    @Transactional
    public GeneratedContentDto generateAndSaveCampaign(CampaignRequest req, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        Campaign campaign = new Campaign();
        campaign.setUser(user);
        campaign.setBrandName(req.getBrandName());
        campaign.setProduct(req.getProduct());
        campaign.setAudience(req.getAudience());
        campaign.setGoal(req.getGoal());
        campaign.setTone(req.getTone());
        campaign = campaignRepository.save(campaign);

        GeneratedContentDto genDto = contentGeneratorService.generate(req);

        GeneratedContent content = new GeneratedContent();
        content.setCampaign(campaign);
        content.setLinkedinPost(genDto.getLinkedinPost());
        content.setInstagramCaption(genDto.getInstagramCaption());
        content.setSeoKeywords(String.join(",", genDto.getSeoKeywords()));
        content.setImagePrompt(genDto.getImagePrompt());
        generatedContentRepository.save(content);

        genDto.setCampaignId(String.valueOf(campaign.getId()));
        return genDto;
    }

    public List<CampaignDto> getMyCampaigns(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        return campaignRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CampaignDetailDto getCampaignDetails(Long id, String email) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        if (!campaign.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("Unauthorized to view this campaign");
        }

        GeneratedContent content = generatedContentRepository.findByCampaignId(id)
                .orElseThrow(() -> new IllegalArgumentException("Content not generated for this campaign"));

        CampaignDetailDto detailDto = new CampaignDetailDto();
        detailDto.setId(String.valueOf(campaign.getId()));
        detailDto.setUserId(String.valueOf(campaign.getUser().getId()));
        detailDto.setBrandName(campaign.getBrandName());
        detailDto.setProduct(campaign.getProduct());
        detailDto.setAudience(campaign.getAudience());
        detailDto.setGoal(campaign.getGoal());
        detailDto.setTone(campaign.getTone());
        detailDto.setCreatedAt(campaign.getCreatedAt());

        detailDto.setLinkedinPost(content.getLinkedinPost());
        detailDto.setInstagramCaption(content.getInstagramCaption());
        if (content.getSeoKeywords() != null && !content.getSeoKeywords().isBlank()) {
            detailDto.setSeoKeywords(List.of(content.getSeoKeywords().split(",")));
        } else {
            detailDto.setSeoKeywords(List.of());
        }
        detailDto.setImagePrompt(content.getImagePrompt());

        return detailDto;
    }

    @Transactional
    public void deleteCampaign(Long id, String email) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        if (!campaign.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("Unauthorized to delete this campaign");
        }

        generatedContentRepository.findByCampaignId(id).ifPresent(generatedContentRepository::delete);
        campaignRepository.delete(campaign);
    }

    private CampaignDto mapToDto(Campaign campaign) {
        CampaignDto dto = new CampaignDto();
        dto.setId(String.valueOf(campaign.getId()));
        dto.setUserId(String.valueOf(campaign.getUser().getId()));
        dto.setBrandName(campaign.getBrandName());
        dto.setProduct(campaign.getProduct());
        dto.setAudience(campaign.getAudience());
        dto.setGoal(campaign.getGoal());
        dto.setTone(campaign.getTone());
        dto.setCreatedAt(campaign.getCreatedAt());
        return dto;
    }
}
