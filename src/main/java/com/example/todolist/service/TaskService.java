package com.example.todolist.service;

import com.example.todolist.model.Task;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskService {

    private Map<String, Task> tasks = new HashMap<>();

    public TaskService() {
        // Initialisation des tâches
        addTask(new Task("task1", "Finish the report", "user1", "company1"));
        addTask(new Task("task2", "Prepare the presentation", "user2", "company1"));
        addTask(new Task("task3", "Organize the meeting", "user3", null));
    }

    // Méthode pour vider la map des tâches
    public void clearTasks() {
        tasks.clear();
    }

    // Méthode pour ajouter une tâche
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // Méthode pour obtenir toutes les tâches
    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    // Méthode pour trouver une tâche par ID
    public Optional<Task> getTaskById(String taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }

    // Méthode pour mettre à jour une tâche
    public void updateTask(Task updatedTask) {
        tasks.put(updatedTask.getId(), updatedTask);
    }

    // Méthode pour supprimer une tâche
    public void deleteTask(String taskId) {
        tasks.remove(taskId);
    }
}
