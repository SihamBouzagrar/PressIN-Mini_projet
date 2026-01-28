package com.example.demo.utilisateur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.utilisateur.entity.Commande;

import com.example.demo.utilisateur.repository.CommandeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    /*
     * =========================
     * READ - ALL
     * =========================
     */
    public List<Commande> findAllCommandes() {
        return commandeRepository.findAll();
    }

    /*
     * =========================
     * READ - BY ID
     * =========================
     */
    public Optional<Commande> findById(Integer id) {
        return commandeRepository.findById(id);
    }

    public List<Commande> findByUserId(Integer userId) {
        return commandeRepository.findByUserId(userId);
    }

    /*
     * =========================
     * DELETE
     * =========================
     */
    @Transactional
    public void deleteCommande(Integer id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("Commande introuvable avec id = " + id);
        }
        commandeRepository.deleteById(id);
    }

@Transactional
public Commande saveCommande(Commande commande) {
    return commandeRepository.save(commande);
}

  
}
