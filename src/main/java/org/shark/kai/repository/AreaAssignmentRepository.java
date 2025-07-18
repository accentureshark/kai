package org.shark.kai.repository;

import org.shark.kai.organization.AreaAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AreaAssignmentRepository extends JpaRepository<AreaAssignment, UUID> {
    
    List<AreaAssignment> findByAreaId(UUID areaId);
    
    List<AreaAssignment> findByPersonId(UUID personId);
    
    @Query("SELECT aa FROM AreaAssignment aa LEFT JOIN FETCH aa.area LEFT JOIN FETCH aa.person WHERE aa.id = :id")
    AreaAssignment findByIdWithDetails(UUID id);
    
    boolean existsByAreaIdAndPersonId(UUID areaId, UUID personId);
}