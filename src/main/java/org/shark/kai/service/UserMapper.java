package org.shark.kai.service;

import org.shark.kai.dto.*;
import org.shark.kai.model.organization.Area;
import org.shark.kai.model.organization.AreaAssignment;
import org.shark.kai.model.organization.Organization;
import org.shark.kai.model.person.NaturalPerson;
import org.shark.kai.model.person.Role;
import org.shark.kai.model.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .active(user.isActive())
                .role(toRoleDTO(user.getRole()))
                .person(toNaturalPersonDTO(user.getPerson()))
                .build();
    }
    
    public RoleDTO toRoleDTO(Role role) {
        if (role == null) return null;
        
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .areaId(role.getArea() != null ? role.getArea().getId() : null)
                .area(toAreaDTO(role.getArea()))
                .build();
    }
    
    public AreaDTO toAreaDTO(Area area) {
        if (area == null) return null;
        
        return AreaDTO.builder()
                .id(area.getId())
                .name(area.getName())
                .organizationId(area.getOrganization() != null ? area.getOrganization().getId() : null)
                .organization(toOrganizationDTO(area.getOrganization()))
                .build();
    }
    
    public OrganizationDTO toOrganizationDTO(Organization organization) {
        if (organization == null) return null;
        
        return OrganizationDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .displayName(organization.getDisplayName())
                .businessName(organization.getBusinessName())
                .taxId(organization.getTaxId())
                .registrationNumber(organization.getRegistrationNumber())
                .build();
    }
    
    public NaturalPersonDTO toNaturalPersonDTO(NaturalPerson person) {
        if (person == null) return null;
        
        return NaturalPersonDTO.builder()
                .id(person.getId())
                .displayName(person.getDisplayName())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .nationalId(person.getNationalId())
                .birthDate(person.getBirthDate())
                .areaAssignments(toAreaAssignmentDTOList(person.getAreaAssignments()))
                .build();
    }
    
    public AreaAssignmentDTO toAreaAssignmentDTO(AreaAssignment assignment) {
        if (assignment == null) return null;
        
        return AreaAssignmentDTO.builder()
                .id(assignment.getId())
                .areaId(assignment.getArea() != null ? assignment.getArea().getId() : null)
                .personId(assignment.getPerson() != null ? assignment.getPerson().getId() : null)
                .assignedAt(assignment.getAssignedAt())
                .localRole(assignment.getLocalRole())
                .area(toAreaDTO(assignment.getArea()))
                // Note: Not including person to avoid circular reference
                .build();
    }
    
    public List<AreaAssignmentDTO> toAreaAssignmentDTOList(List<AreaAssignment> assignments) {
        if (assignments == null) return null;
        
        return assignments.stream()
                .map(this::toAreaAssignmentDTO)
                .collect(Collectors.toList());
    }
    
    public Organization toOrganizationEntity(OrganizationDTO dto) {
        if (dto == null) return null;
        
        Organization organization = new Organization();
        organization.setId(dto.getId());
        organization.setName(dto.getName());
        organization.setDisplayName(dto.getDisplayName());
        organization.setBusinessName(dto.getBusinessName());
        organization.setTaxId(dto.getTaxId());
        organization.setRegistrationNumber(dto.getRegistrationNumber());
        
        return organization;
    }
    
    public Area toAreaEntity(AreaDTO dto) {
        if (dto == null) return null;
        
        Area area = Area.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
        
        if (dto.getOrganization() != null) {
            area.setOrganization(toOrganizationEntity(dto.getOrganization()));
        }
        
        return area;
    }
    
    public Role toRoleEntity(RoleDTO dto) {
        if (dto == null) return null;
        
        Role role = Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
        
        if (dto.getArea() != null) {
            role.setArea(toAreaEntity(dto.getArea()));
        }
        
        return role;
    }
    
    public NaturalPerson toNaturalPersonEntity(NaturalPersonDTO dto) {
        if (dto == null) return null;
        
        NaturalPerson person = NaturalPerson.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .nationalId(dto.getNationalId())
                .birthDate(dto.getBirthDate())
                .build();
        
        person.setId(dto.getId()); // Set ID from parent class
        person.setDisplayName(dto.getDisplayName()); // Set displayName from parent class
        
        return person;
    }
}