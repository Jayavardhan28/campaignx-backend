package com.campaignx.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
