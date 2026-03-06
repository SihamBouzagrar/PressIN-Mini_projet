package com.example.demo.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CommandeDTO;
import com.example.demo.utilisateur.entity.Client;
import com.example.demo.utilisateur.entity.Commande;

import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.repository.CommandeRepository;
import com.example.demo.utilisateur.service.CommandeService;
import com.example.demo.utilisateur.service.UserService;

@RestController
@RequestMapping("/rest/commandes")
@CrossOrigin("*")
public class CommandeController {

    @Autowired
    private  CommandeService commandeService;
     @Autowired
    private  UserService userService;
    
    @PostMapping("/create/{clientId}")
    public ResponseEntity<Commande> createCommande(
            @PathVariable Long clientId,
            @RequestBody CommandeDTO dto) {
                 // ✅ Récupérer le Client (pas Users)
        Users user = userService.findClientById(clientId )
                .orElseThrow(() -> new RuntimeException("Client introuvable id=" + clientId));

        if (!(user instanceof Client)) {
            return ResponseEntity.badRequest().build();
        }
       
        Commande cmd = new Commande();
        cmd.setClient((Client) user);
        cmd.setDateCollecteSouhaitee(
            dto.getDateCollecteSouhaitee() != null
            ? dto.getDateCollecteSouhaitee() : LocalDate.now()
        );
        cmd.setDateLivraisonSouhaitee(
            dto.getDateLivraisonSouhaitee() != null
            ? dto.getDateLivraisonSouhaitee() : LocalDate.now().plusDays(3)
        );
       if (dto.getTypeLivraison() != null)
    cmd.setTypeLivraison(Commande.TypeLivraison.valueOf(dto.getTypeLivraison()));
        cmd.setMontantRemise(dto.getMontantRemise());

        return ResponseEntity.status(201).body(commandeService.saveCommande(cmd));
    }   


    /*
     * =========================
     * PUT - Modifier une commande
     * =========================
     */
   @PutMapping("/update/{id}")
    public ResponseEntity<Commande> updateCommande(
            @PathVariable Long id,
            @RequestBody CommandeDTO dto) {   

        Optional<Commande> optionalCommande = commandeService.findById(id);
        if (optionalCommande.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Commande commande = optionalCommande.get();
 if (dto.getDateCollecteSouhaitee() != null)
            commande.setDateCollecteSouhaitee(dto.getDateCollecteSouhaitee());
        if (dto.getDateLivraisonSouhaitee() != null)
            commande.setDateLivraisonSouhaitee(dto.getDateLivraisonSouhaitee());
        if (dto.getTypeLivraison() != null)
            commande.setTypeLivraison(Commande.TypeLivraison.valueOf(dto.getTypeLivraison()));
        if (dto.getMontantRemise() != null) {
            commande.setMontantRemise(dto.getMontantRemise());
            commande.calculerMontantTotal();  // ✅ recalcul automatique
        }

        return ResponseEntity.ok(commandeService.saveCommande(commande));
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        return commandeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Commande>> getCommandesByUser(@PathVariable Long userId) {
        List<Commande> commandes = commandeService.findByClientId(userId);
        return ResponseEntity.ok(commandes);
    }

    /*
     * =========================
     * DELETE - Supprimer commande
     * =========================
     */
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Commande>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(commandeService.findByClientId(clientId));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

}