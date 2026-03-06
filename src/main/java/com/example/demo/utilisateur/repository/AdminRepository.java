package com.example.demo.utilisateur.repository;


import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.utilisateur.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

 
}