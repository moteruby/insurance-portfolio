package com.insurance.service;

import com.insurance.exception.DuplicateResourceException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.User;
import com.insurance.model.UserRole;
import com.insurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<User> getAllUsers(UserRole role, Boolean active) {
        if (role != null && active != null) {
            return userRepository.findAll().stream()
                    .filter(u -> u.getRole() == role && u.getActive().equals(active))
                    .toList();
        } else if (role != null) {
            return userRepository.findByRole(role);
        } else if (active != null) {
            return userRepository.findByActive(active);
        }
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
    
    public User createUser(User user) {
        // Проверка на дубликат username
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("User", "username", user.getUsername());
        }
        
        // Проверка на дубликат email
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("User", "email", user.getEmail());
        }
        
        // Хеширование пароля
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Установка значений по умолчанию
        if (user.getActive() == null) {
            user.setActive(true);
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Проверка на дубликат username (если username изменился)
        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new DuplicateResourceException("User", "username", userDetails.getUsername());
            }
        }
        
        // Проверка на дубликат email (если email изменился)
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new DuplicateResourceException("User", "email", userDetails.getEmail());
            }
        }
        
        if (userDetails.getUsername() != null) user.setUsername(userDetails.getUsername());
        if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
        if (userDetails.getFullName() != null) user.setFullName(userDetails.getFullName());
        if (userDetails.getRole() != null) user.setRole(userDetails.getRole());
        if (userDetails.getActive() != null) user.setActive(userDetails.getActive());
        
        // Обновление пароля только если он предоставлен
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }
    
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    public User toggleUserActive(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.setActive(!user.getActive());
        return userRepository.save(user);
    }
    
    public void updateLastLogin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
}
