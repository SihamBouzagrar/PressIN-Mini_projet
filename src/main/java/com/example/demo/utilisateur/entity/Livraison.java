package com.example.demo.utilisateur.entity;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "livraisons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Livraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Users livreur;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rue",        column = @Column(name = "collecte_rue")),
        @AttributeOverride(name = "ville",      column = @Column(name = "collecte_ville")),
        @AttributeOverride(name = "codePostal", column = @Column(name = "collecte_code_postal")),
        @AttributeOverride(name = "pays",       column = @Column(name = "collecte_pays"))
    })
    private Adresse adresseCollecte;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rue",        column = @Column(name = "dest_rue")),
        @AttributeOverride(name = "ville",      column = @Column(name = "dest_ville")),
        @AttributeOverride(name = "codePostal", column = @Column(name = "dest_code_postal")),
        @AttributeOverride(name = "pays",       column = @Column(name = "dest_pays"))
    })
    private Adresse adresseLivraison;

    @CreationTimestamp
    private LocalDateTime dateCollectePrevue;
    private LocalDateTime dateCollecteEffective;
    private LocalDateTime dateLivraisonPrevue;
    private LocalDateTime dateLivraisonEffective;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutLivraison statut = StatutLivraison.PLANIFIEE; // ← plus besoin de @PrePersist

    public enum StatutLivraison {
        PLANIFIEE,
        EN_COURS_DE_COLLECTE,
        COLLECTEE,
        EN_COURS_DE_LIVRAISON,
        LIVREE,
        ANNULEE
    }
}