package com.example.demo.rest;

import com.example.demo.dto.CommandeDTO;
import com.example.demo.utilisateur.entity.*;
import com.example.demo.utilisateur.service.CommandeService;
import com.example.demo.utilisateur.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/commandes")
@CrossOrigin("*")

public class CommandeController {
 private final CommandeService commandeService;
    private final UserService userService;

    public CommandeController(CommandeService commandeService,
                              UserService userService) {
        this.commandeService = commandeService;
        this.userService = userService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Commande>> getAllCommandes() {
        return ResponseEntity.ok(commandeService.findAllCommandes());
    }

    @PostMapping("/create/{clientId}")
    public ResponseEntity<?> createCommande(
            @PathVariable Long clientId,
            @RequestBody CommandeDTO dto) {

        // ✅ findById au lieu de findClientById
        Optional<Users> optionalClient = userService.findById(clientId);
        if (optionalClient.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Client introuvable avec id = " + clientId);
        }

        // ✅ vérifier que c'est bien un CLIENT
        Users client = optionalClient.get();
        if (client.getRole() != Role.ROLE_CLIENT) {
            return ResponseEntity.badRequest()
                    .body("L'utilisateur n'est pas un client");
        }

        try {
            Commande cmd = new Commande();
            cmd.setClient(client); // ✅ Users au lieu de Client
            cmd.setArticles(new ArrayList<>());

            cmd.setDateCollecteSouhaitee(
                    dto.getDateCollecteSouhaitee() != null
                            ? dto.getDateCollecteSouhaitee()
                            : LocalDate.now());
            cmd.setDateLivraisonSouhaitee(
                    dto.getDateLivraisonSouhaitee() != null
                            ? dto.getDateLivraisonSouhaitee()
                            : LocalDate.now().plusDays(3));

            if (dto.getTypeLivraison() != null && !dto.getTypeLivraison().isEmpty()) {
                try {
                    cmd.setTypeLivraison(
                            Commande.TypeLivraison.valueOf(dto.getTypeLivraison().toUpperCase()));
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Type livraison invalide");
                }
            }

            cmd.setMontantRemise(dto.getMontantRemise());

            cmd.setAdresseLivraison(
                    dto.getAdresseLivraison() != null
                            ? dto.getAdresseLivraison()
                            : new Adresse("", "", "", "Maroc"));

            Commande saved = commandeService.saveCommande(cmd);
            return ResponseEntity.status(201).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Commande> updateCommande(
            @PathVariable Long id,
            @RequestBody CommandeDTO dto) {
        return commandeService.findById(id)
                .map(commande -> {
                    if (dto.getDateCollecteSouhaitee() != null)
                        commande.setDateCollecteSouhaitee(dto.getDateCollecteSouhaitee());
                    if (dto.getDateLivraisonSouhaitee() != null)
                        commande.setDateLivraisonSouhaitee(dto.getDateLivraisonSouhaitee());
                    if (dto.getTypeLivraison() != null)
                        commande.setTypeLivraison(
                                Commande.TypeLivraison.valueOf(dto.getTypeLivraison()));
                    if (dto.getMontantRemise() != null) {
                        commande.setMontantRemise(dto.getMontantRemise());
                        commande.calculerMontantTotal();
                    }

  if (dto.getStatut() != null) {
                    commande.setStatut(dto.getStatut());
                }

                return ResponseEntity.ok(commandeService.saveCommande(commande));
            })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        return commandeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Commande>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(commandeService.findByClientId(clientId));
    }

    // ✅ retourne les commandes d'un livreur
    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<List<Commande>> getByLivreur(@PathVariable Long livreurId) {
        return ResponseEntity.ok(commandeService.findByLivreurId(livreurId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Commande>> getByStatut(
            @PathVariable Commande.StatutCommande statut) {
        return ResponseEntity.ok(commandeService.findByStatut(statut));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Changer le statut (ADMIN)
    @PutMapping("/statut/{id}")
    public ResponseEntity<?> changerStatut(
            @PathVariable Long id,
            @RequestParam String statut) {
        return commandeService.findById(id)
                .map(commande -> {
                    try {
                        commande.setStatut(
                                Commande.StatutCommande.valueOf(statut.toUpperCase()));
                        return ResponseEntity.ok(commandeService.saveCommande(commande));
                    } catch (Exception e) {
                        return ResponseEntity.badRequest()
                                .body("Statut invalide : " + statut);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Assigner un livreur (ADMIN)
    @PutMapping("/assigner-livreur/{commandeId}/{livreurId}")
    public ResponseEntity<?> assignerLivreur(
            @PathVariable Long commandeId,
            @PathVariable Long livreurId) {

        Optional<Commande> optCommande = commandeService.findById(commandeId);
        Optional<Users> optLivreur = userService.findById(livreurId);

        if (optCommande.isEmpty())
            return ResponseEntity.badRequest().body("Commande introuvable");
        if (optLivreur.isEmpty())
            return ResponseEntity.badRequest().body("Livreur introuvable");

        // ✅ vérifier que c'est bien un LIVREUR
        Users livreur = optLivreur.get();
        if (livreur.getRole() != Role.ROLE_LIVREUR)
            return ResponseEntity.badRequest().body("Cet utilisateur n'est pas un livreur");

        Commande commande = optCommande.get();
        commande.assignerLivreur(livreur);
        return ResponseEntity.ok(commandeService.saveCommande(commande));
    }

    // ✅ Marquer comme livrée (ADMIN ou LIVREUR)
    @PutMapping("/livree/{id}")
    public ResponseEntity<?> marquerLivree(@PathVariable Long id) {
        return commandeService.findById(id)
                .map(commande -> {
                    commande.marquerLivree();
                    return ResponseEntity.ok(commandeService.saveCommande(commande));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
