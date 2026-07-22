package com.campaignx.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "generated_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String linkedinPost;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String instagramCaption;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String seoKeywords; // comma-separated, split/join in service layer

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imagePrompt;
}
