package com.example.demo.utilisateur.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Client extends Users {

    @Column(name = "numero_client", unique = true)
    private String numeroClient;

  //  @Column(name = "programme_fidelite")
    //private String programmeFidelite; // ex: BRONZE, SILVER, GOLD

  //  @Column(name = "points_fidelite")
    //@Builder.Default
    //private Integer pointsFidelite = 0;

    // Relation future vers Commande
    // @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    // private List<Commande> commandes;
} 