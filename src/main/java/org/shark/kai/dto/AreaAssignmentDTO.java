package org.shark.kai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Area assignment data transfer object")
public class AreaAssignmentDTO {
    
    @Schema(description = "Assignment ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @NotNull(message = "Area is required")
    @Schema(description = "Area ID", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID areaId;
    
    @NotNull(message = "Person is required")
    @Schema(description = "Person ID", example = "550e8400-e29b-41d4-a716-446655440002")
    private UUID personId;
    
    @NotNull(message = "Assignment date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Assignment date", example = "2023-01-01")
    private LocalDate assignedAt;
    
    @Schema(description = "Local role in the area", example = "Team Lead")
    private String localRole;
    
    @Schema(description = "Area information")
    private AreaDTO area;
    
    @Schema(description = "Person information")
    private NaturalPersonDTO person;
}