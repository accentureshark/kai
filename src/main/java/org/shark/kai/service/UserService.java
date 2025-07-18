package org.shark.kai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shark.kai.dto.CreateUserRequest;
import org.shark.kai.dto.UserDTO;
import org.shark.kai.model.person.NaturalPerson;
import org.shark.kai.model.person.Role;
import org.shark.kai.repository.NaturalPersonRepository;
import org.shark.kai.repository.RoleRepository;
import org.shark.kai.repository.UserRepository;
import org.shark.kai.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NaturalPersonRepository naturalPersonRepository;
    private final UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        log.debug("Finding all users with pagination: {}", pageable);
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(UUID id) {
        log.debug("Finding user by id: {}", id);
        return userRepository.findByIdWithDetails(id)
                .map(userMapper::toDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO);
    }
    
    public UserDTO create(CreateUserRequest request) {
        log.debug("Creating new user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }
        
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword()) // TODO: Encrypt password
                .active(request.getActive())
                .build();
        
        // Set role if provided
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + request.getRoleId()));
            user.setRole(role);
        }
        
        // Set person if provided
        if (request.getPersonId() != null) {
            NaturalPerson person = naturalPersonRepository.findById(request.getPersonId())
                    .orElseThrow(() -> new IllegalArgumentException("Person not found with id: " + request.getPersonId()));
            user.setPerson(person);
        }
        
        User savedUser = userRepository.save(user);
        log.info("Created user with id: {}", savedUser.getId());
        
        return userMapper.toDTO(savedUser);
    }
    
    public UserDTO update(UUID id, CreateUserRequest request) {
        log.debug("Updating user with id: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        // Check email uniqueness if changed
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }
        
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(request.getPassword()); // TODO: Encrypt password
        }
        user.setActive(request.getActive());
        
        // Update role if provided
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + request.getRoleId()));
            user.setRole(role);
        } else {
            user.setRole(null);
        }
        
        // Update person if provided
        if (request.getPersonId() != null) {
            NaturalPerson person = naturalPersonRepository.findById(request.getPersonId())
                    .orElseThrow(() -> new IllegalArgumentException("Person not found with id: " + request.getPersonId()));
            user.setPerson(person);
        } else {
            user.setPerson(null);
        }
        
        User savedUser = userRepository.save(user);
        log.info("Updated user with id: {}", savedUser.getId());
        
        return userMapper.toDTO(savedUser);
    }
    
    public void delete(UUID id) {
        log.debug("Deleting user with id: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }
}