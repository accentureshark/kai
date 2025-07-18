package org.shark.kai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Area data transfer object")
public class AreaDTO {
    
    @Schema(description = "Area ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @NotBlank(message = "Name is required")
    @Schema(description = "Area name", example = "Technology")
    private String name;
    
    @NotNull(message = "Organization is required")
    @Schema(description = "Organization ID", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID organizationId;
    
    @Schema(description = "Organization information")
    private OrganizationDTO organization;
}