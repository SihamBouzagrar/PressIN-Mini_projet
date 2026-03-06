package com.example.demo.utilisateur.entity;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("LIVREUR")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Livreur extends Users {

    @Column(name = "numero_permis")
    private String numeroPermis;

    @Column(name = "type_vehicule")
    private String typeVehicule; // ex: VELO, MOTO, VOITURE

    @Column(name = "zone_livraison")
    private String zoneLivraison;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_livreur")
    @Builder.Default
    private StatutLivreur statutLivreur = StatutLivreur.DISPONIBLE;

   // @Column(name = "note_moyenne")
    //private Double noteMoyenne;

    public enum StatutLivreur {
        DISPONIBLE, EN_LIVRAISON, HORS_SERVICE
    }
}
