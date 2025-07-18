package org.shark.kai.repository;

import org.shark.kai.model.person.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
    List<Role> findByAreaId(UUID areaId);
    
    List<Role> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.area WHERE r.id = :id")
    Role findByIdWithArea(UUID id);
}