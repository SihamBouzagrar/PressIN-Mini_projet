package com.example.demo.utilisateur.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(unique = true, nullable = false)
    private String numeroCommande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;


    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Article> articles = new ArrayList<>();
    

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCommande statut;

    @Enumerated(EnumType.STRING)
    private TypeLivraison typeLivraison;

    private LocalDate dateCollecteSouhaitee;
    private LocalDate dateLivraisonSouhaitee;
    
     @Column(updatable = false)
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    private Double montantTotal;
    private Double montantRemise;
    private Double montantFinal;

  //  private String notesClient;
  //  private String instructionsSpeciales;

    // ✅ Adresse de livraison si A_DOMICILE
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rue",        column = @Column(name = "livraison_rue")),
        @AttributeOverride(name = "ville",      column = @Column(name = "livraison_ville")),
        @AttributeOverride(name = "codePostal", column = @Column(name = "livraison_code_postal")),
        @AttributeOverride(name = "pays",       column = @Column(name = "livraison_pays",
                                                             nullable = false, 
                                                             columnDefinition = "varchar(255) default 'Maroc'")
        )
    })
    private Adresse adresseLivraison;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;

 @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
        this.statut = StatutCommande.RECUE;
        this.genererNumeroCommande();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    private void genererNumeroCommande() {
        this.numeroCommande = "CMD-" + System.currentTimeMillis();
    }

    public void calculerMontantTotal() {
        this.montantTotal = articles.stream()
                .mapToDouble(Article::getPrixTotal)
                .sum();
        this.montantFinal = montantTotal - (montantRemise != null ? montantRemise : 0);
    }

    // ✅ Méthode utilitaire : assigner un livreur
    public void assignerLivreur(Livreur livreur) {
        this.livreur = livreur;
        this.statut = StatutCommande.EN_LIVRAISON;
        livreur.setStatutLivreur(Livreur.StatutLivreur.EN_LIVRAISON);
    }

    // ✅ Méthode utilitaire : marquer comme livrée
    public void marquerLivree() {
        this.statut = StatutCommande.LIVREE;
        if (this.livreur != null) {
            this.livreur.setStatutLivreur(Livreur.StatutLivreur.DISPONIBLE);
        }
    }

    // ------- Enums -------

    public enum StatutCommande {
        RECUE("Reçue"),
        EN_LAVAGE("En cours de lavage"),
        EN_REPASSAGE("Repassage"),
        EN_LIVRAISON("En livraison"),
        PRETE("Prête à être retirée"),
        LIVREE("Livrée"),
        ANNULEE("Annulée");

        private final String libelle;

        StatutCommande(String libelle) { this.libelle = libelle; }

        public String getLibelle() { return libelle; }
    }

    public enum TypeLivraison {
        EN_MAGASIN, A_DOMICILE
    }

}