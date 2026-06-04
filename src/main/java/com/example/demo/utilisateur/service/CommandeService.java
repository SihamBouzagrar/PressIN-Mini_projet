package com.example.demo.utilisateur.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.utilisateur.entity.Article;
import com.example.demo.utilisateur.entity.Commande;
import com.example.demo.utilisateur.repository.CommandeRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;   
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
public Commande saveCommande(Commande cmd) {

    if (cmd.getArticles() != null) {
        for (Article a : cmd.getArticles()) {
            a.setCommande(cmd); // 🔥 OBLIGATOIRE
        }
    }

    return commandeRepository.save(cmd);
}


public List<Commande> findByClientId(Long clientId) {
    return commandeRepository.findByClientId(clientId);
}

public List<Commande> findByLivreurId(Long livreurId) {
    return commandeRepository.findByLivreurId(livreurId);
}

public List<Commande> findByStatut(Commande.StatutCommande statut) {
    return commandeRepository.findByStatut(statut);
}
}
