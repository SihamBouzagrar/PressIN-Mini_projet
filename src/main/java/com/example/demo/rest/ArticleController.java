package com.example.demo.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.utilisateur.entity.Article;
import com.example.demo.utilisateur.service.ArticleService;
import com.example.demo.utilisateur.service.CommandeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;


    @PostMapping("/ajouter")
    public Article ajouterArticle(@RequestParam Long commandeId,
                                  @RequestParam String nom) {

        return articleService.ajouterArticle(commandeId, nom);
    }
@GetMapping("/commande/{id}")
public List<Article> getByCommande(@PathVariable Long id) {
    return articleService.getArticlesByCommande(id);
}
}
