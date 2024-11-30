package com.example.todolist.service;

import com.example.todolist.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private Map<String, User> users = new HashMap<>();

    public UserService() {
        // Initialisation des utilisateurs
        addUser(new User("user1", "Alice", Role.STANDARD, "company1"));
        addUser(new User("user2", "Bob", Role.COMPANY_ADMIN, "company1"));
        addUser(new User("user3", "Charlie", Role.SUPER_USER, null));
    }

    // Méthode pour vider la map des utilisateurs
    public void clearUsers() {
        users.clear();
    }

    // Méthode pour ajouter un utilisateur
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    // Méthode pour obtenir un utilisateur par ID
    public Optional<User> getUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    // Méthode pour obtenir tous les utilisateurs
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
