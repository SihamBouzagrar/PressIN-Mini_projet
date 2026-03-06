package com.example.demo.utilisateur.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.utilisateur.entity.Commande;
import com.example.demo.utilisateur.repository.CommandeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    
    private CommandeRepository commandeRepository;

    public List<Commande> findAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> findById(Long id) {
        return commandeRepository.findById(id);
    }

    @Transactional
    public void deleteCommande(Long id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("Commande introuvable avec id = " + id);
        }
        commandeRepository.deleteById(id);
    }

@Transactional
public Commande saveCommande(Commande commande) {
    return commandeRepository.save(commande);
}


public List<Commande> findByClientId(Long clientId) {
    return commandeRepository.findByClientId(clientId);
}

public Object save(Commande cmd) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
}
  
}
