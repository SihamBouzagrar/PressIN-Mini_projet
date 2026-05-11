package com.example.demo.utilisateur.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties({"commande"})
    private List<Article> articles = new ArrayList<>();

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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    private Double montantTotal;
    private Double montantRemise;
    private Double montantFinal;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rue",        column = @Column(name = "livraison_rue")),
        @AttributeOverride(name = "ville",      column = @Column(name = "livraison_ville")),
        @AttributeOverride(name = "codePostal", column = @Column(name = "livraison_code_postal")),
        @AttributeOverride(name = "pays",       column = @Column(name = "livraison_pays"))
    })
    private Adresse adresseLivraison;

    @PrePersist
    protected void onCreate() {
        this.genererNumeroCommande();
        if (this.statut == null)
            this.statut = StatutCommande.RECUE;
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

    public void assignerLivreur(Users livreur) {
        this.livreur = livreur;
        this.statut = StatutCommande.EN_LIVRAISON;
    }

    public void marquerLivree() {
        this.statut = StatutCommande.LIVREE;
    }

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