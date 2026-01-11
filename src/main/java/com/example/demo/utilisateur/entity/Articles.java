package com.example.demo.utilisateur.entity;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "articles")
public class Articles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type_vetement", nullable = false, length = 100)
    private String typeVetement;

    @Column(nullable = false, length = 50)
    private String matiere;

    @Column(name = "etat_entree", nullable = false, length = 50)
    private String etatEntree;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Relation avec Commande
    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTypeVetement() { return typeVetement; }
    public void setTypeVetement(String typeVetement) { this.typeVetement = typeVetement; }
    public String getMatiere() { return matiere; }
    public void setMatiere(String matiere) { this.matiere = matiere; }
    public String getEtatEntree() { return etatEntree; }
    public void setEtatEntree(String etatEntree) { this.etatEntree = etatEntree; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }
}
