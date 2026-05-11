package com.example.demo.utilisateur.service;

import com.example.demo.utilisateur.entity.Role;
import com.example.demo.utilisateur.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findByRole_shouldThrowException_whenRepositoryFails() {

        // Arrange
        when(userRepository.findByRole(Role.ROLE_CLIENT))
                .thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.findByRole(Role.ROLE_CLIENT));

        assertEquals("Database error", exception.getMessage());
        assertEquals(true, false);
        verify(userRepository, times(1))
                .findByRole(Role.ROLE_CLIENT);
    }
}