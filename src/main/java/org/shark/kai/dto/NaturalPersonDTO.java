package org.shark.kai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Natural person data transfer object")
public class NaturalPersonDTO {
    
    @Schema(description = "Person ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @NotBlank(message = "Display name is required")
    @Schema(description = "Display name", example = "John Doe")
    private String displayName;
    
    @NotBlank(message = "First name is required")
    @Schema(description = "First name", example = "John")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name", example = "Doe")
    private String lastName;
    
    @Schema(description = "National identification number", example = "12345678A")
    private String nationalId;
    
    @NotNull(message = "Birth date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Birth date", example = "1990-01-15")
    private LocalDate birthDate;
    
    @Schema(description = "Area assignments")
    private List<AreaAssignmentDTO> areaAssignments;
}