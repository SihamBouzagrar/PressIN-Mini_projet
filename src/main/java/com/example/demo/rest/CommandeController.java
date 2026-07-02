package com.example.demo.rest;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.CommandeDTO;
import com.example.demo.utilisateur.entity.*;
import com.example.demo.utilisateur.service.CommandeService;
import com.example.demo.utilisateur.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    // =========================
    // CREATE COMMANDE (SIMPLIFIÉ)
    // =========================
 @PostMapping("/create/{clientId}")
public ResponseEntity<?> createCommande(
        @PathVariable Long clientId,
        @RequestBody CommandeDTO dto) {

    Optional<Users> optionalClient = userService.findById(clientId);

    if (optionalClient.isEmpty()) {
        return ResponseEntity.badRequest()
                .body("Client introuvable avec id = " + clientId);
    }

    Users client = optionalClient.get();

    if (client.getRole() != Role.ROLE_CLIENT) {
        return ResponseEntity.badRequest()
                .body("L'utilisateur n'est pas un client");
    }

    try {
        Commande cmd = new Commande();
        cmd.setClient(client);

        // DATES
        cmd.setDateCollecteSouhaitee(
                dto.getDateCollecteSouhaitee() != null
                        ? dto.getDateCollecteSouhaitee()
                        : LocalDate.now()
        );

        cmd.setDateLivraisonSouhaitee(
                dto.getDateLivraisonSouhaitee() != null
                        ? dto.getDateLivraisonSouhaitee()
                        : LocalDate.now().plusDays(3)
        );

        // TYPE LIVRAISON
        if (dto.getTypeLivraison() != null && !dto.getTypeLivraison().isBlank()) {
            try {
                cmd.setTypeLivraison(
                        Commande.TypeLivraison.valueOf(dto.getTypeLivraison())
                );
            } catch (Exception e) {
                cmd.setTypeLivraison(Commande.TypeLivraison.EN_MAGASIN);
            }
        } else {
            cmd.setTypeLivraison(Commande.TypeLivraison.EN_MAGASIN);
        }

        // MONTANTS
        Double total = dto.getMontantTotal() != null ? dto.getMontantTotal() : 0.0;
        Double remise = dto.getMontantRemise() != null ? dto.getMontantRemise() : 0.0;

        cmd.setMontantTotal(total);
        cmd.setMontantRemise(remise);
        cmd.setMontantFinal(total - remise);

        // ADRESSE
        cmd.setAdresseLivraison(
                dto.getAdresseLivraison() != null
                        ? dto.getAdresseLivraison()
                        : new AdresseLivraison("", "", "", "Maroc")
        );

        // ARTICLES 🔥 FIX IMPORTANT
        List<Article> articles = new ArrayList<>();

        if (dto.getArticles() != null) {
            for (ArticleDTO a : dto.getArticles()) {

                Article article = new Article();
                article.setNom(a.getNom());
                article.setPrice(a.getPrice());

                article.setCommande(cmd); // ❗ FIX ICI

                articles.add(article);
            }
        }

        cmd.setArticles(articles);

        // SAVE
        Commande saved = commandeService.saveCommande(cmd);

        return ResponseEntity.status(201).body(saved);

    } catch (Exception e) {
        return ResponseEntity.status(500)
                .body("Erreur serveur : " + e.getMessage());
    }
}
    // =========================
    // UPDATE
    // =========================
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

                    if (dto.getTypeLivraison() != null) {
                        commande.setTypeLivraison(
                                Commande.TypeLivraison.valueOf(dto.getTypeLivraison().toUpperCase())
                        );
                    }

                    if (dto.getMontantRemise() != null) {
                        commande.setMontantRemise(dto.getMontantRemise());
                    }

                    if (dto.getMontantTotal() != null) {
                        commande.setMontantTotal(dto.getMontantTotal());
                    }

                    if (dto.getStatut() != null) {
                        commande.setStatut(
                                Commande.StatutCommande.valueOf(dto.getStatut().toUpperCase())
                        );
                    }

                    if (dto.getAdresseLivraison() != null) {
                        commande.setAdresseLivraison(dto.getAdresseLivraison());
                    }

                    Commande updated = commandeService.saveCommande(commande);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // GET
    // =========================
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

    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<List<Commande>> getByLivreur(@PathVariable Long livreurId) {
        return ResponseEntity.ok(commandeService.findByLivreurId(livreurId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Commande>> getByStatut(
            @PathVariable Commande.StatutCommande statut) {
        return ResponseEntity.ok(commandeService.findByStatut(statut));
    }

@PutMapping("/statut/{id}")
public ResponseEntity<Commande> updateStatut(
        @PathVariable Long id,
        @RequestParam Commande.StatutCommande statut) {

    return ResponseEntity.ok(commandeService.updateStatut(id, statut));
}
    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }
    
}