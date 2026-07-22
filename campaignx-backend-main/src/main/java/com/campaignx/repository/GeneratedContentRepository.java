package com.campaignx.repository;

import com.campaignx.entity.GeneratedContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GeneratedContentRepository extends JpaRepository<GeneratedContent, Long> {
    Optional<GeneratedContent> findByCampaignId(Long campaignId);
}
