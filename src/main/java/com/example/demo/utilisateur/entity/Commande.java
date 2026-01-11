package com.example.demo.utilisateur.entity;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relation avec utilisateur
    @ManyToOne
    @JoinColumn(name = "id_users", nullable = false)
    private Users user;

    @Column(name = "date_reception")
    private LocalDateTime dateReception = LocalDateTime.now();

    @Column(name = "date_remise_prevue")
    private LocalDateTime dateRemisePrevue;

    @Column(length = 50)
    private String statut = "En cours de lavage";

    @Column(name = "prix_total", precision = 10, scale = 2)
    private Double prixTotal = 0.0;

    @Column(name = "code_ocr", length = 100)
    private String codeOcr;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Relation avec Articles
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Articles> articles;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Users getClient() { return user; }
    public void setClient(Users user) { this.user = user; }
    public LocalDateTime getDateReception() { return dateReception; }
    public void setDateReception(LocalDateTime dateReception) { this.dateReception = dateReception; }
    public LocalDateTime getDateRemisePrevue() { return dateRemisePrevue; }
    public void setDateRemisePrevue(LocalDateTime dateRemisePrevue) { this.dateRemisePrevue = dateRemisePrevue; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }
    public String getCodeOcr() { return codeOcr; }
    public void setCodeOcr(String codeOcr) { this.codeOcr = codeOcr; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public List<Articles> getArticles() { return articles; }
    public void setArticles(List<Articles> articles) { this.articles = articles; }
}

