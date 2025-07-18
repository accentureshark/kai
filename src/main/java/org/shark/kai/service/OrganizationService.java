package org.shark.kai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shark.kai.dto.OrganizationDTO;
import org.shark.kai.organization.Organization;
import org.shark.kai.repository.OrganizationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrganizationService {
    
    private final OrganizationRepository organizationRepository;
    private final UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAll(Pageable pageable) {
        log.debug("Finding all organizations with pagination: {}", pageable);
        return organizationRepository.findAll(pageable)
                .map(userMapper::toOrganizationDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<OrganizationDTO> findById(UUID id) {
        log.debug("Finding organization by id: {}", id);
        return organizationRepository.findById(id)
                .map(userMapper::toOrganizationDTO);
    }
    
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findByName(String name) {
        log.debug("Finding organizations by name containing: {}", name);
        return organizationRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(userMapper::toOrganizationDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<OrganizationDTO> findByTaxId(String taxId) {
        log.debug("Finding organization by tax id: {}", taxId);
        return organizationRepository.findByTaxId(taxId)
                .map(userMapper::toOrganizationDTO);
    }
    
    public OrganizationDTO create(OrganizationDTO dto) {
        log.debug("Creating new organization with name: {}", dto.getName());
        
        if (dto.getTaxId() != null && organizationRepository.findByTaxId(dto.getTaxId()).isPresent()) {
            throw new IllegalArgumentException("Organization with tax ID " + dto.getTaxId() + " already exists");
        }
        
        Organization organization = userMapper.toOrganizationEntity(dto);
        organization.setId(null); // Ensure new entity
        
        Organization savedOrganization = organizationRepository.save(organization);
        log.info("Created organization with id: {}", savedOrganization.getId());
        
        return userMapper.toOrganizationDTO(savedOrganization);
    }
    
    public OrganizationDTO update(UUID id, OrganizationDTO dto) {
        log.debug("Updating organization with id: {}", id);
        
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found with id: " + id));
        
        // Check tax ID uniqueness if changed
        if (dto.getTaxId() != null && !dto.getTaxId().equals(organization.getTaxId())) {
            if (organizationRepository.findByTaxId(dto.getTaxId()).isPresent()) {
                throw new IllegalArgumentException("Organization with tax ID " + dto.getTaxId() + " already exists");
            }
        }
        
        organization.setName(dto.getName());
        organization.setDisplayName(dto.getDisplayName());
        organization.setBusinessName(dto.getBusinessName());
        organization.setTaxId(dto.getTaxId());
        organization.setRegistrationNumber(dto.getRegistrationNumber());
        
        Organization savedOrganization = organizationRepository.save(organization);
        log.info("Updated organization with id: {}", savedOrganization.getId());
        
        return userMapper.toOrganizationDTO(savedOrganization);
    }
    
    public void delete(UUID id) {
        log.debug("Deleting organization with id: {}", id);
        
        if (!organizationRepository.existsById(id)) {
            throw new IllegalArgumentException("Organization not found with id: " + id);
        }
        
        organizationRepository.deleteById(id);
        log.info("Deleted organization with id: {}", id);
    }
}