package com.example.todolist.controller;

import com.example.todolist.model.Role;
import com.example.todolist.model.Task;
import com.example.todolist.model.User;
import com.example.todolist.service.TaskService;
import com.example.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    /**
     * Endpoint pour créer une nouvelle tâche.
     *
     * @param userId ID de l'utilisateur qui effectue la requête.
     * @param task   Objet Task à créer.
     * @return ResponseEntity avec le statut approprié.
     */
    @PostMapping
    public ResponseEntity<String> createTask(@RequestParam String userId, @RequestBody Task task) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (canUserCreateTask(user, task)) {
                taskService.addTask(task);
                return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to create this task.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    /**
     * Méthode privée pour vérifier si l'utilisateur a le droit de créer la tâche.
     *
     * @param user Utilisateur qui tente de créer la tâche.
     * @param task Tâche à créer.
     * @return true si l'utilisateur a le droit, false sinon.
     */
    private boolean canUserCreateTask(User user, Task task) {
        Role role = user.getRole();

        if (role == Role.SUPER_USER) {
            // Super User peut créer n'importe quelle tâche
            return true;
        } else if (role == Role.COMPANY_ADMIN) {
            // Company-Admin peut créer des tâches pour sa compagnie
            return task.getCompanyId().equals(user.getCompanyId());
        } else if (role == Role.STANDARD) {
            // Standard User peut créer des tâches assignées à lui-même dans sa compagnie
            return task.getAssignedToUserId().equals(user.getId()) && task.getCompanyId().equals(user.getCompanyId());
        } else {
            return false;
        }
    }

    /**
     * Endpoint pour récupérer toutes les tâches accessibles pour un utilisateur.
     *
     * @param userId ID de l'utilisateur.
     * @return Liste des tâches accessibles.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam String userId) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User user = optionalUser.get();

        List<Task> tasks = taskService.getAllTasks().stream()
                .filter(task -> {
                    if (user.getRole() == Role.SUPER_USER) {
                        return true;
                    } else if (user.getRole() == Role.COMPANY_ADMIN) {
                        return task.getCompanyId().equals(user.getCompanyId());
                    } else { // STANDARD
                        return task.getAssignedToUserId().equals(userId);
                    }
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(tasks);
    }

    /**
     * Endpoint pour récupérer une tâche spécifique.
     *
     * @param userId ID de l'utilisateur.
     * @param id     ID de la tâche.
     * @return La tâche si accessible.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@RequestParam String userId, @PathVariable String id) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User user = optionalUser.get();

        Optional<Task> optionalTask = taskService.getTaskById(id);
        if (!optionalTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Task task = optionalTask.get();

        if (canUserAccessTask(user, task)) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    /**
     * Endpoint pour mettre à jour une tâche existante.
     *
     * @param userId ID de l'utilisateur qui effectue la requête.
     * @param taskId ID de la tâche à mettre à jour.
     * @param task   Objet Task contenant les nouvelles données.
     * @return ResponseEntity avec le statut approprié.
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<String> updateTask(@RequestParam String userId, @PathVariable String taskId, @RequestBody Task task) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = optionalUser.get();

        Optional<Task> optionalExistingTask = taskService.getTaskById(taskId);
        if (!optionalExistingTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }

        Task existingTask = optionalExistingTask.get();

        if (canUserUpdateTask(user, existingTask)) {
            // Mettre à jour les informations de la tâche existante
            existingTask.setDescription(task.getDescription());
            existingTask.setAssignedToUserId(task.getAssignedToUserId());
            existingTask.setCompanyId(task.getCompanyId());

            taskService.updateTask(existingTask);
            return ResponseEntity.ok("Task updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to update this task.");
        }
    }

    /**
     * Endpoint pour supprimer une tâche existante.
     *
     * @param userId ID de l'utilisateur qui effectue la requête.
     * @param taskId ID de la tâche à supprimer.
     * @return ResponseEntity avec le statut approprié.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@RequestParam String userId, @PathVariable String taskId) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = optionalUser.get();

        Optional<Task> optionalTask = taskService.getTaskById(taskId);
        if (!optionalTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }

        Task task = optionalTask.get();

        if (canUserDeleteTask(user, task)) {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok("Task deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this task.");
        }
    }

    /**
     * Méthode privée pour vérifier si l'utilisateur a le droit d'accéder à la tâche.
     *
     * @param user Utilisateur qui tente d'accéder à la tâche.
     * @param task Tâche à accéder.
     * @return true si l'utilisateur a le droit, false sinon.
     */
    private boolean canUserAccessTask(User user, Task task) {
        Role role = user.getRole();

        if (role == Role.SUPER_USER) {
            // Super User peut accéder à n'importe quelle tâche
            return true;
        } else if (role == Role.COMPANY_ADMIN) {
            // Company-Admin peut accéder aux tâches de sa compagnie
            return task.getCompanyId().equals(user.getCompanyId());
        } else if (role == Role.STANDARD) {
            // Standard User peut accéder uniquement à ses propres tâches
            return task.getAssignedToUserId().equals(user.getId());
        } else {
            return false;
        }
    }

    /**
     * Méthode privée pour vérifier si l'utilisateur a le droit de mettre à jour la tâche.
     *
     * @param user Utilisateur qui tente de mettre à jour la tâche.
     * @param task Tâche à mettre à jour.
     * @return true si l'utilisateur a le droit, false sinon.
     */
    private boolean canUserUpdateTask(User user, Task task) {
        // La logique est la même que pour l'accès
        return canUserAccessTask(user, task);
    }

    /**
     * Méthode privée pour vérifier si l'utilisateur a le droit de supprimer la tâche.
     *
     * @param user Utilisateur qui tente de supprimer la tâche.
     * @param task Tâche à supprimer.
     * @return true si l'utilisateur a le droit, false sinon.
     */
    private boolean canUserDeleteTask(User user, Task task) {
        // La logique est la même que pour l'accès
        return canUserAccessTask(user, task);
    }
}