package com.example.demo.utilisateur.entity;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

public class Livraison {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "livraison")
    private Commande commande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;
    
    @Embedded
    private Adresse adresseCollecte;
    
    @Embedded
    private Adresse adresseLivraison;
    
    private LocalDateTime dateCollectePrevue;
    private LocalDateTime dateCollecteEffective;
    private LocalDateTime dateLivraisonPrevue;
    private LocalDateTime dateLivraisonEffective;
    
    @Enumerated(EnumType.STRING)
    private StatutLivraison statut;
    
    private Double latitudeActuelle;
    private Double longitudeActuelle;
    private LocalDateTime derniereMiseAJourPosition;
    
    public enum StatutLivraison {
        PLANIFIEE, EN_COURS_DE_COLLECTE, COLLECTEE, EN_COURS_DE_LIVRAISON, LIVREE, ANNULEE
    }
}