package com.example.demo.utilisateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.utilisateur.entity.Articles;
import com.example.demo.utilisateur.repository.ArticlesRepository;


@Service
public class ArticlesService {
   @Autowired
    private ArticlesRepository articlesRepository;

    public Articles saveArticle(Articles article) {
        return articlesRepository.save(article);
    }

    public Optional<Articles> findById(Integer id) {
        return articlesRepository.findById(id);
    }

    public List<Articles> findAllArticles() {
        return articlesRepository.findAll();
    }

    public void deleteArticle(Integer id) {
        articlesRepository.deleteById(id);
    }
}
