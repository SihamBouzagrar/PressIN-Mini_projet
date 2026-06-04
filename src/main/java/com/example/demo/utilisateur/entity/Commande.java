package com.example.demo.utilisateur.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroCommande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passWord"})
    private Users client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users livreur;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "livraison_id")
    @JsonIgnoreProperties({"commande"})
    private Livraison livraison;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    @Enumerated(EnumType.STRING)
    private TypeLivraison typeLivraison;

    private LocalDate dateCollecteSouhaitee;
    private LocalDate dateLivraisonSouhaitee;
@OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference
private List<Article> articles = new ArrayList<>();
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    private Double montantTotal;
    private Double montantRemise;
    private Double montantFinal;

   @Embedded
private AdresseLivraison adresseLivraison;

@PrePersist
@PreUpdate
public void preSave() {

    if (this.numeroCommande == null) {
        this.numeroCommande = "CMD-" + System.currentTimeMillis();
    }

    if (this.statut == null) {
        this.statut = StatutCommande.RECUE;
    }

    double total = (this.montantTotal != null) ? this.montantTotal : 0.0;
    double remise = (this.montantRemise != null) ? this.montantRemise : 0.0;

    this.montantFinal = total - remise;
}
    public void assignerLivreur(Users livreur) {
        this.livreur = livreur;
        this.statut = StatutCommande.EN_LIVRAISON;
    }

    public void marquerLivree() {
        this.statut = StatutCommande.LIVREE;
    }

    public enum StatutCommande {
        RECUE, EN_LAVAGE, EN_REPASSAGE, EN_LIVRAISON, PRETE, LIVREE, ANNULEE
    }

    public enum TypeLivraison {
        EN_MAGASIN, A_DOMICILE
    }
}