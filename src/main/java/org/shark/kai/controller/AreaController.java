package org.shark.kai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shark.kai.dto.AreaDTO;
import org.shark.kai.service.AreaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Areas", description = "Area management operations")
public class AreaController {
    
    private final AreaService areaService;
    
    @GetMapping
    @Operation(summary = "Get all areas", description = "Retrieve all areas with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved areas"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Page<AreaDTO>> getAllAreas(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AreaDTO> areas = areaService.findAll(pageable);
        
        return ResponseEntity.ok(areas);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get area by ID", description = "Retrieve a specific area by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved area"),
            @ApiResponse(responseCode = "404", description = "Area not found")
    })
    public ResponseEntity<AreaDTO> getAreaById(
            @Parameter(description = "Area ID", required = true)
            @PathVariable UUID id) {
        
        return areaService.findById(id)
                .map(area -> ResponseEntity.ok(area))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get areas by organization", description = "Retrieve all areas for a specific organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved areas")
    })
    public ResponseEntity<List<AreaDTO>> getAreasByOrganization(
            @Parameter(description = "Organization ID", required = true)
            @PathVariable UUID organizationId) {
        
        List<AreaDTO> areas = areaService.findByOrganizationId(organizationId);
        return ResponseEntity.ok(areas);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search areas by name", description = "Search areas by name containing specified text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved areas")
    })
    public ResponseEntity<List<AreaDTO>> searchAreasByName(
            @Parameter(description = "Name to search for", required = true)
            @RequestParam String name) {
        
        List<AreaDTO> areas = areaService.findByName(name);
        return ResponseEntity.ok(areas);
    }
    
    @PostMapping
    @Operation(summary = "Create area", description = "Create a new area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Area created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<AreaDTO> createArea(
            @Parameter(description = "Area creation data", required = true)
            @Valid @RequestBody AreaDTO request) {
        
        try {
            AreaDTO createdArea = areaService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArea);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create area: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update area", description = "Update an existing area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Area updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Area or organization not found")
    })
    public ResponseEntity<AreaDTO> updateArea(
            @Parameter(description = "Area ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Area update data", required = true)
            @Valid @RequestBody AreaDTO request) {
        
        try {
            AreaDTO updatedArea = areaService.update(id, request);
            return ResponseEntity.ok(updatedArea);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to update area {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete area", description = "Delete an area by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Area deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Area not found")
    })
    public ResponseEntity<Void> deleteArea(
            @Parameter(description = "Area ID", required = true)
            @PathVariable UUID id) {
        
        try {
            areaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Failed to delete area {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}