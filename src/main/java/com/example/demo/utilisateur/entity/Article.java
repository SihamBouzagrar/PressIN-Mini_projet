package com.example.demo.utilisateur.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Column(nullable = false)
    private String designation; // ex: chemise, pantalon

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private Double prixUnitaire;

    @Enumerated(EnumType.STRING)
    private TypeTraitement typeTraitement; // LAVAGE, REPASSAGE, etc.

  //  private String remarque;

    // ✅ Méthode manquante — corrige l'erreur mapToDouble
    public Double getPrixTotal() {
        return prixUnitaire * quantite;
    }

    public enum TypeTraitement {
        LAVAGE, REPASSAGE, LAVAGE_REPASSAGE, NETTOYAGE_SEC
    }
}
