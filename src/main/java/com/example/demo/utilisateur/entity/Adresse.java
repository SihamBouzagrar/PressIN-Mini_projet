package com.example.demo.utilisateur.entity;

import javax.persistence.Embeddable;

import lombok.*;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {

    private String rue;
    private String ville;
    private String codePostal;
     private String pays = "Maroc";
}