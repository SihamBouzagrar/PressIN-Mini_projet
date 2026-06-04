package com.example.demo.utilisateur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.utilisateur.entity.Article;


public interface ArticleRepository extends JpaRepository<Article, Long> {
     List<Article> findByCommandeId(Long commandeId);

    List<Article> findByCommandeClientId(Long clientId);
   
}