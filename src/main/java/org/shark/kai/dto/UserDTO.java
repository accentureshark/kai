package org.shark.kai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User data transfer object")
public class UserDTO {
    
    @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User email address", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "User password (only for creation/update)", example = "password123")
    private String password;
    
    @NotNull(message = "Active status is required")
    @Schema(description = "Whether the user is active", example = "true")
    private Boolean active;
    
    @Schema(description = "Associated role information")
    private RoleDTO role;
    
    @Schema(description = "Associated person information")
    private NaturalPersonDTO person;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Creation timestamp", example = "2023-01-01 10:00:00")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last update timestamp", example = "2023-01-01 10:00:00")
    private LocalDateTime updatedAt;
}