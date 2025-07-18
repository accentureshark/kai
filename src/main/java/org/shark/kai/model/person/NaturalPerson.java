package org.shark.kai.model.person;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.shark.kai.model.organization.AreaAssignment;

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("NATURAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class NaturalPerson extends Person {

    private String firstName;

    private String lastName;

    private String nationalId;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaAssignment> areaAssignments;

}
