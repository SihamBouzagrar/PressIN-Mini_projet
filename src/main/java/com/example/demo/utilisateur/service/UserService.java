package com.example.demo.utilisateur.service;

import com.example.demo.utilisateur.entity.Role;
import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<Users> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }

    public Users save(Users user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}