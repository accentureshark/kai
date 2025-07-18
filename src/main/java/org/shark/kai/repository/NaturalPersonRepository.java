package org.shark.kai.repository;

import org.shark.kai.model.person.NaturalPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, UUID> {
    
    List<NaturalPerson> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    Optional<NaturalPerson> findByNationalId(String nationalId);
    
    @Query("SELECT np FROM NaturalPerson np LEFT JOIN FETCH np.areaAssignments WHERE np.id = :id")
    Optional<NaturalPerson> findByIdWithAssignments(UUID id);
}