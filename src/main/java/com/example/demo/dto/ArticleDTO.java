package com.example.demo.dto;
import lombok.Data;

@Data
public class ArticleDTO {

    private String nom;
    private Double price;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}