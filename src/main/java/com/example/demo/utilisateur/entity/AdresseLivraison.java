package com.example.demo.utilisateur.entity;

import javax.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdresseLivraison {

    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
}
