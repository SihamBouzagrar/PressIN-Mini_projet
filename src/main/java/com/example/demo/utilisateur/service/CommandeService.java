package com.example.demo.utilisateur.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.utilisateur.entity.Commande;
import com.example.demo.utilisateur.repository.CommandeRepository;

import java.util.List;
import java.util.Optional;
@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    /* =========================
       CREATE / UPDATE
       ========================= */
    public Commande saveCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    /* =========================
       READ - ALL
       ========================= */
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }

    /* =========================
       READ - BY ID
       ========================= */
    public Optional<Commande> findById(Integer id) {
        return commandeRepository.findById(id);
    }

    /* =========================
       DELETE
       ========================= */
    public void deleteCommande(Integer id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("Commande introuvable avec id = " + id);
        }
        commandeRepository.deleteById(id);
    }
}
