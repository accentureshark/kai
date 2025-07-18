package org.shark.kai.repository;

import org.shark.kai.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    
    List<Organization> findByNameContainingIgnoreCase(String name);
    
    Optional<Organization> findByTaxId(String taxId);
    
    @Query("SELECT o FROM Organization o WHERE o.businessName LIKE %:businessName%")
    List<Organization> findByBusinessNameContaining(String businessName);
}