package org.shark.kai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User creation request object")
public class CreateUserRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User email address", example = "john.doe@example.com")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "User password", example = "password123")
    private String password;
    
    @NotNull(message = "Active status is required")
    @Schema(description = "Whether the user is active", example = "true")
    private Boolean active;
    
    @Schema(description = "Role ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID roleId;
    
    @Schema(description = "Person ID", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID personId;
}