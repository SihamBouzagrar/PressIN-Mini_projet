package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.utilisateur.entity.Commande;
import com.example.demo.utilisateur.service.CommandeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/commandes")
@CrossOrigin("*")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    /*
     * =========================
     * POST - Créer une commande
     * =========================
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Commande> createCommande(@RequestBody Commande commande) {
        Commande savedCommande = commandeService.saveCommande(commande);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCommande);
    }

    /*
     * =========================
     * PUT - Modifier une commande
     * =========================
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Commande> updateCommande(
            @PathVariable Integer id,
            @RequestBody Commande commandeDetails) {

        Optional<Commande> optionalCommande = commandeService.findById(id);

        if (optionalCommande.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Commande commande = optionalCommande.get();

        // ✅ Mise à jour sécurisée (uniquement si non null)
        if (commandeDetails.getDateReception() != null)
            commande.setDateReception(commandeDetails.getDateReception());

        if (commandeDetails.getDateRemisePrevue() != null)
            commande.setDateRemisePrevue(commandeDetails.getDateRemisePrevue());

        if (commandeDetails.getStatut() != null)
            commande.setStatut(commandeDetails.getStatut());

        if (commandeDetails.getPrixTotal() != null)
            commande.setPrixTotal(commandeDetails.getPrixTotal());

        if (commandeDetails.getCodeOcr() != null)
            commande.setCodeOcr(commandeDetails.getCodeOcr());
        // ⚡ Mettre à jour l'utilisateur de la commande si fourni
        if (commandeDetails.getUser() != null)
            commande.setUser(commandeDetails.getUser());

        Commande updatedCommande = commandeService.saveCommande(commande);
        return ResponseEntity.ok(updatedCommande);
    }

    /*
     * =========================
     * GET - Toutes les commandes
     * =========================
     */
    @GetMapping("/all")
    public ResponseEntity<List<Commande>> getAllCommandes() {
        List<Commande> commandes = commandeService.findAllCommandes();
        return ResponseEntity.ok(commandes);
    }

    /*
     * =========================
     * GET - Commande par ID
     * =========================
     */
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Integer id) {
        return commandeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Commande>> getCommandesByUser(@PathVariable Integer userId) {
        List<Commande> commandes = commandeService.findByUserId(userId);
        return ResponseEntity.ok(commandes);
    }

    /*
     * =========================
     * DELETE - Supprimer commande
     * =========================
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Integer id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }
}
