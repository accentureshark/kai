package org.shark.kai.model.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.shark.kai.model.person.LegalPerson;


import java.util.UUID;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Organization extends LegalPerson {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;
}
