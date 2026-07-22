package com.campaignx.service;

import com.campaignx.dto.AdminStatsDto;
import com.campaignx.dto.CampaignDto;
import com.campaignx.dto.UserDto;
import com.campaignx.entity.Campaign;
import com.campaignx.entity.Role;
import com.campaignx.entity.User;
import com.campaignx.repository.CampaignRepository;
import com.campaignx.repository.GeneratedContentRepository;
import com.campaignx.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final GeneratedContentRepository generatedContentRepository;
    private final UserService userService;

    public AdminService(UserRepository userRepository,
            CampaignRepository campaignRepository,
            GeneratedContentRepository generatedContentRepository,
            UserService userService) {
        this.userRepository = userRepository;
        this.campaignRepository = campaignRepository;
        this.generatedContentRepository = generatedContentRepository;
        this.userService = userService;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userService::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Campaign> campaigns = campaignRepository.findByUserId(id);
        for (Campaign campaign : campaigns) {
            generatedContentRepository.findByCampaignId(campaign.getId()).ifPresent(generatedContentRepository::delete);
            campaignRepository.delete(campaign);
        }
        userRepository.delete(user);
    }

    @Transactional
    public UserDto updateUserRole(Long id, Map<String, String> body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String roleStr = body.get("role");
        if (roleStr == null || roleStr.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }

        Role role = Role.valueOf(roleStr.toUpperCase());
        user.setRole(role);
        User updated = userRepository.save(user);
        return userService.mapToDto(updated);
    }
    @Transactional
    public void deleteCampaign(Long id) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        generatedContentRepository.findByCampaignId(id)
                .ifPresent(generatedContentRepository::delete);

        campaignRepository.delete(campaign);
    }

    public List<CampaignDto> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(campaign -> {
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
                })
                .collect(Collectors.toList());
    }

    public AdminStatsDto getAnalytics() {
        long totalUsers = userRepository.count();
        long totalCampaigns = campaignRepository.count();

        // Calculate unique users who created campaigns in the last 7 days
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        long activeUsersThisWeek = campaignRepository.findAll().stream()
                .filter(campaign -> campaign.getCreatedAt() != null && campaign.getCreatedAt().isAfter(oneWeekAgo))
                .map(campaign -> campaign.getUser().getId())
                .distinct()
                .count();

        AdminStatsDto stats = new AdminStatsDto();
        stats.setTotalUsers(totalUsers);
        stats.setTotalCampaigns(totalCampaigns);
        stats.setActiveUsersThisWeek(activeUsersThisWeek);
        return stats;
    }
}
