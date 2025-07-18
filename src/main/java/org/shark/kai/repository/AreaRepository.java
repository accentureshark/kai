package org.shark.kai.repository;

import org.shark.kai.model.organization.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {
    
    List<Area> findByOrganizationId(UUID organizationId);
    
    List<Area> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT a FROM Area a LEFT JOIN FETCH a.organization WHERE a.id = :id")
    Area findByIdWithOrganization(UUID id);
}