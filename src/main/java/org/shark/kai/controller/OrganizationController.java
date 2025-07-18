package org.shark.kai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shark.kai.dto.OrganizationDTO;
import org.shark.kai.service.OrganizationService;
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
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Organizations", description = "Organization management operations")
public class OrganizationController {
    
    private final OrganizationService organizationService;
    
    @GetMapping
    @Operation(summary = "Get all organizations", description = "Retrieve all organizations with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organizations"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Page<OrganizationDTO>> getAllOrganizations(
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
        Page<OrganizationDTO> organizations = organizationService.findAll(pageable);
        
        return ResponseEntity.ok(organizations);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get organization by ID", description = "Retrieve a specific organization by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organization"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<OrganizationDTO> getOrganizationById(
            @Parameter(description = "Organization ID", required = true)
            @PathVariable UUID id) {
        
        return organizationService.findById(id)
                .map(organization -> ResponseEntity.ok(organization))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search organizations by name", description = "Search organizations by name containing specified text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organizations")
    })
    public ResponseEntity<List<OrganizationDTO>> searchOrganizationsByName(
            @Parameter(description = "Name to search for", required = true)
            @RequestParam String name) {
        
        List<OrganizationDTO> organizations = organizationService.findByName(name);
        return ResponseEntity.ok(organizations);
    }
    
    @GetMapping("/tax-id/{taxId}")
    @Operation(summary = "Get organization by tax ID", description = "Retrieve a specific organization by their tax ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organization"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<OrganizationDTO> getOrganizationByTaxId(
            @Parameter(description = "Tax ID", required = true)
            @PathVariable String taxId) {
        
        return organizationService.findByTaxId(taxId)
                .map(organization -> ResponseEntity.ok(organization))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create organization", description = "Create a new organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Organization created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Organization with tax ID already exists")
    })
    public ResponseEntity<OrganizationDTO> createOrganization(
            @Parameter(description = "Organization creation data", required = true)
            @Valid @RequestBody OrganizationDTO request) {
        
        try {
            OrganizationDTO createdOrganization = organizationService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrganization);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create organization: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update organization", description = "Update an existing organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Organization not found"),
            @ApiResponse(responseCode = "409", description = "Tax ID already in use")
    })
    public ResponseEntity<OrganizationDTO> updateOrganization(
            @Parameter(description = "Organization ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Organization update data", required = true)
            @Valid @RequestBody OrganizationDTO request) {
        
        try {
            OrganizationDTO updatedOrganization = organizationService.update(id, request);
            return ResponseEntity.ok(updatedOrganization);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to update organization {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete organization", description = "Delete an organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organization deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<Void> deleteOrganization(
            @Parameter(description = "Organization ID", required = true)
            @PathVariable UUID id) {
        
        try {
            organizationService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Failed to delete organization {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}