package com.example.demo.utilisateur.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
public class Article {

    @Id
    @GeneratedValue
    private Long id;

    private String nom; // pantalon, costume...
    private Double price;

@ManyToOne
@JoinColumn(name = "commande_id")
@JsonBackReference
private Commande commande;
 
}
