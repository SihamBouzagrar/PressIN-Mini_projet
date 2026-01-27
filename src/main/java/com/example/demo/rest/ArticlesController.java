package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.utilisateur.entity.Articles;
import com.example.demo.utilisateur.service.ArticlesService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/articles")
@CrossOrigin("*")
public class ArticlesController {

    @Autowired
    private ArticlesService articlesService;

    /*
     * =========================
     * POST - Créer un article
     * =========================
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Articles> createArticle(@RequestBody Articles article) {
        Articles savedArticle = articlesService.saveArticle(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    /*
     * =========================
     * PUT - Modifier un article
     * =========================
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Articles> updateArticle(
            @PathVariable Integer id,
            @RequestBody Articles articleDetails) {

        Optional<Articles> optionalArticle = articlesService.findById(id);

        if (optionalArticle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Articles article = optionalArticle.get();

        if (articleDetails.getTypeVetement() != null)
            article.setTypeVetement(articleDetails.getTypeVetement());

        if (articleDetails.getMatiere() != null)
            article.setMatiere(articleDetails.getMatiere());

        if (articleDetails.getEtatEntree() != null)
            article.setEtatEntree(articleDetails.getEtatEntree());

        // Si tu veux modifier la relation commande (optionnel)
        if (articleDetails.getCommande() != null)
            article.setCommande(articleDetails.getCommande());

        Articles updatedArticle = articlesService.saveArticle(article);
        return ResponseEntity.ok(updatedArticle);
    }

    /*
     * =========================
     * GET - Tous les articles
     * =========================
     */
    @GetMapping("/all")
    public ResponseEntity<List<Articles>> getAllArticles() {
        List<Articles> articles = articlesService.findAllArticles();
        return ResponseEntity.ok(articles);
    }

    /*
     * =========================
     * GET - Article par ID
     * =========================
     */
    @GetMapping("/{id}")
    public ResponseEntity<Articles> getArticleById(@PathVariable Integer id) {
        return articlesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * =========================
     * DELETE - Supprimer un article
     * =========================
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer id) {
        articlesService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
