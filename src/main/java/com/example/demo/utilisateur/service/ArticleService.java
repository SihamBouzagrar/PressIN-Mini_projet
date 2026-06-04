package com.example.demo.utilisateur.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.utilisateur.entity.Article;
import com.example.demo.utilisateur.entity.Commande;
import com.example.demo.utilisateur.repository.ArticleRepository;
import com.example.demo.utilisateur.repository.CommandeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CommandeRepository commandeRepository;
    public List<Article> getArticlesByCommande(Long commandeId) {
    return articleRepository.findByCommandeId(commandeId);
}

  public Article ajouterArticle(Long commandeId, String nom) {

    Commande commande = commandeRepository.findById(commandeId)
            .orElseThrow(() -> new RuntimeException("Commande introuvable"));

    Article article = new Article();
    article.setNom(nom);
    article.setCommande(commande); // 🔥 OBLIGATOIRE

    return articleRepository.save(article);
}
}