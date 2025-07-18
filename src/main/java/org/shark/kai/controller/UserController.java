package org.shark.kai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shark.kai.dto.CreateUserRequest;
import org.shark.kai.dto.UserDTO;
import org.shark.kai.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management operations")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "email")
            @RequestParam(defaultValue = "email") String sortBy,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDTO> users = userService.findAll(pageable);
        
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID id) {
        
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve a specific user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(description = "User email", required = true)
            @PathVariable String email) {
        
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User with email already exists")
    })
    public ResponseEntity<UserDTO> createUser(
            @Parameter(description = "User creation data", required = true)
            @Valid @RequestBody CreateUserRequest request) {
        
        try {
            UserDTO createdUser = userService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "User update data", required = true)
            @Valid @RequestBody CreateUserRequest request) {
        
        try {
            UserDTO updatedUser = userService.update(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to update user {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID id) {
        
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Failed to delete user {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}