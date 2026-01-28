package com.example.demo.rest;

public class CommandeDTO {
    private String orderDetails; // JSON de tes articles
    private Double prixTotal;

    public String getOrderDetails() { return orderDetails; }
    public void setOrderDetails(String orderDetails) { this.orderDetails = orderDetails; }

    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }

    
}
