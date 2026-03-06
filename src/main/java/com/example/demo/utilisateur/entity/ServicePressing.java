package com.example.demo.utilisateur.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicePressing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(nullable = false)
    private String nom;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private CategorieService categorie;
    
    private Double prixBase;
    private Integer dureeEstimeeHeures; // Durée estimée du traitement
    
 
    
    @ElementCollection
    @CollectionTable(name = "service_options", joinColumns = @JoinColumn(name = "service_id"))
    @Builder.Default
    private List<OptionService> options = new ArrayList<>();
    
    public enum CategorieService {
        LAVAGE, REPASSAGE, NETTOYAGE_SEC, DETACHAGE, RETOUCHE
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OptionService {
        private String nom;
        private Double supplementPrix;
        private Integer supplementDureeHeures;
    }
}