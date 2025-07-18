package org.shark.kai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Organization data transfer object")
public class OrganizationDTO {
    
    @Schema(description = "Organization ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @NotBlank(message = "Name is required")
    @Schema(description = "Organization name", example = "Accenture")
    private String name;
    
    @NotBlank(message = "Display name is required")
    @Schema(description = "Display name", example = "Accenture PLC")
    private String displayName;
    
    @Schema(description = "Business name", example = "Accenture Public Limited Company")
    private String businessName;
    
    @Schema(description = "Tax identification number", example = "123456789")
    private String taxId;
    
    @Schema(description = "Registration number", example = "REG001")
    private String registrationNumber;
}