package com.example.demo.utilisateur.repository;

import com.example.demo.utilisateur.entity.Role;
import com.example.demo.utilisateur.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    List<Users> findByRole(Role role); // ← remplace findAllClients/Admins/Livreurs
}