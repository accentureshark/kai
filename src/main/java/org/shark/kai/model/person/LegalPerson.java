package org.shark.kai.model.person;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("LEGAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class LegalPerson extends Person {

    private String businessName;

    private String taxId;

    private String registrationNumber;
}
