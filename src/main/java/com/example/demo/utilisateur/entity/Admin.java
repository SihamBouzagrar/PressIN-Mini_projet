package com.example.demo.utilisateur.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ADMIN")
@Data
@SuperBuilder
@NoArgsConstructor 
@EqualsAndHashCode(callSuper = true)
public class Admin extends Users {
    // ✅ niveauAcces supprimé
}
