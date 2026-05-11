package com.example.demo.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LivraisonDTO;
import com.example.demo.utilisateur.entity.Livraison;
import com.example.demo.utilisateur.service.LivraisonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/livraisons")
@CrossOrigin("*")
@RequiredArgsConstructor
public class LivraisonController {

    private final LivraisonService livraisonService;

    // ADMIN
    @GetMapping("/all")
    public ResponseEntity<List<Livraison>> getAll() {
        return ResponseEntity.ok(livraisonService.findAll());
    }

    // ADMIN — créer livraison
    @PostMapping("/create/{commandeId}/{livreurId}")
    public ResponseEntity<?> create(
            @PathVariable Long commandeId,
            @PathVariable Long livreurId) {
        Livraison livraison = livraisonService.creerLivraison(commandeId, livreurId);
        return ResponseEntity.status(201).body(LivraisonDTO.from(livraison));
    }

    // ADMIN ou LIVREUR — changer statut
    @PutMapping("/statut/{id}")
    public ResponseEntity<?> changerStatut(
            @PathVariable Long id,
            @RequestParam String statut) {
        return ResponseEntity.ok(livraisonService.changerStatut(
                id, Livraison.StatutLivraison.valueOf(statut.toUpperCase())));
    }

    // LIVREUR — mettre à jour position GPS
    @PutMapping("/dates/{id}")
    public ResponseEntity<?> mettreAJourDates(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime dateCollecte,
            @RequestParam(required = false) LocalDateTime dateLivraison) {
        return ResponseEntity.ok(
                livraisonService.mettreAJourDates(id, dateCollecte, dateLivraison));
    }

    // LIVREUR — ses livraisons

    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<List<LivraisonDTO>> getByLivreur(@PathVariable Long livreurId) {
        return ResponseEntity.ok(
                livraisonService.findByLivreur(livreurId)
                        .stream()
                        .map(LivraisonDTO::from)
                        .collect(Collectors.toList()));
    }

    // CLIENT — livraison de sa commande
    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<?> getByCommande(@PathVariable Long commandeId) {
        return livraisonService.findByCommande(commandeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ADMIN — par statut
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Livraison>> getByStatut(
            @PathVariable Livraison.StatutLivraison statut) {
        return ResponseEntity.ok(livraisonService.findByStatut(statut));
    }
}
