package org.shark.kai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shark.kai.dto.AreaDTO;
import org.shark.kai.organization.Area;
import org.shark.kai.organization.Organization;
import org.shark.kai.repository.AreaRepository;
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
public class AreaService {
    
    private final AreaRepository areaRepository;
    private final OrganizationRepository organizationRepository;
    private final UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public Page<AreaDTO> findAll(Pageable pageable) {
        log.debug("Finding all areas with pagination: {}", pageable);
        return areaRepository.findAll(pageable)
                .map(userMapper::toAreaDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<AreaDTO> findById(UUID id) {
        log.debug("Finding area by id: {}", id);
        return areaRepository.findById(id)
                .map(userMapper::toAreaDTO);
    }
    
    @Transactional(readOnly = true)
    public List<AreaDTO> findByOrganizationId(UUID organizationId) {
        log.debug("Finding areas by organization id: {}", organizationId);
        return areaRepository.findByOrganizationId(organizationId)
                .stream()
                .map(userMapper::toAreaDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AreaDTO> findByName(String name) {
        log.debug("Finding areas by name containing: {}", name);
        return areaRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(userMapper::toAreaDTO)
                .collect(Collectors.toList());
    }
    
    public AreaDTO create(AreaDTO dto) {
        log.debug("Creating new area with name: {}", dto.getName());
        
        Organization organization = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found with id: " + dto.getOrganizationId()));
        
        Area area = Area.builder()
                .name(dto.getName())
                .organization(organization)
                .build();
        
        Area savedArea = areaRepository.save(area);
        log.info("Created area with id: {}", savedArea.getId());
        
        return userMapper.toAreaDTO(savedArea);
    }
    
    public AreaDTO update(UUID id, AreaDTO dto) {
        log.debug("Updating area with id: {}", id);
        
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Area not found with id: " + id));
        
        Organization organization = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found with id: " + dto.getOrganizationId()));
        
        area.setName(dto.getName());
        area.setOrganization(organization);
        
        Area savedArea = areaRepository.save(area);
        log.info("Updated area with id: {}", savedArea.getId());
        
        return userMapper.toAreaDTO(savedArea);
    }
    
    public void delete(UUID id) {
        log.debug("Deleting area with id: {}", id);
        
        if (!areaRepository.existsById(id)) {
            throw new IllegalArgumentException("Area not found with id: " + id);
        }
        
        areaRepository.deleteById(id);
        log.info("Deleted area with id: {}", id);
    }
}