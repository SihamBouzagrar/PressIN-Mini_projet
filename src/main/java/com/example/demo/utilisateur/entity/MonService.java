package com.example.demo.utilisateur.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Entity
@Table(name = "services")
public class MonService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String nom;

   
    @Column(nullable = false)
    private Double prix;


    @Column(name = "delai_jours", nullable = false)
    private Integer delaiJours;

    // === Constructeurs ===
    public MonService() {}

    public MonService(String nom, Double prix, Integer delaiJours) {
        this.nom = nom;
        this.prix = prix;
        this.delaiJours = delaiJours;
    }

    // === Getters & Setters ===
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public Integer getDelaiJours() { return delaiJours; }
    public void setDelaiJours(Integer delaiJours) { this.delaiJours = delaiJours; }
}