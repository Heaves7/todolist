package com.example.todolist;

import com.example.todolist.model.Role;
import com.example.todolist.model.Task;
import com.example.todolist.model.User;
import com.example.todolist.service.TaskService;
import com.example.todolist.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodolistApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        // Réinitialiser les données avant chaque test
        taskService.clearTasks();
        userService.clearUsers();

        // Ajouter des utilisateurs
        userService.addUser(new User("user1", "Alice", Role.STANDARD, "company1"));
        userService.addUser(new User("user2", "Bob", Role.COMPANY_ADMIN, "company1"));
        userService.addUser(new User("user3", "Charlie", Role.SUPER_USER, null));

        // Ajouter des tâches
        taskService.addTask(new Task("task1", "Compléter le rapport", "user1", "company1"));
        taskService.addTask(new Task("task2", "Préparer la présentation", "user2", "company1"));
        taskService.addTask(new Task("task3", "Organiser une réunion", "user3", "company2"));
    }

    // **GET /tasks?userId={userId}**

    @Test
    public void testGetTasksAsStandardUser() throws Exception {
        mockMvc.perform(get("/tasks")
                        .param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("task1"))
                .andExpect(jsonPath("$[0].description").value("Compléter le rapport"));
    }

    @Test
    public void testGetTasksAsCompanyAdmin() throws Exception {
        mockMvc.perform(get("/tasks")
                        .param("userId", "user2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].companyId", everyItem(is("company1"))));
    }

    @Test
    public void testGetTasksAsSuperUser() throws Exception {
        mockMvc.perform(get("/tasks")
                        .param("userId", "user3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    // **GET /tasks/{taskId}?userId={userId}**

    @Test
    public void testGetSpecificTaskAsStandardUser() throws Exception {
        mockMvc.perform(get("/tasks/task1")
                        .param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("task1"))
                .andExpect(jsonPath("$.description").value("Compléter le rapport"));
    }

    @Test
    public void testStandardUserCannotAccessOthersTasks() throws Exception {
        mockMvc.perform(get("/tasks/task2")
                        .param("userId", "user1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCompanyAdminCanAccessCompanyTasks() throws Exception {
        mockMvc.perform(get("/tasks/task1")
                        .param("userId", "user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("task1"));
    }

    @Test
    public void testSuperUserCanAccessAnyTask() throws Exception {
        mockMvc.perform(get("/tasks/task3")
                        .param("userId", "user3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("task3"));
    }

    // **POST /tasks?userId={userId}**

    @Test
    public void testCreateTaskAsStandardUser() throws Exception {
        String newTaskJson = "{ \"id\": \"task4\", \"description\": \"Nouvelle tâche\", \"assignedToUserId\": \"user1\", \"companyId\": \"company1\" }";
        mockMvc.perform(post("/tasks")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTaskJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateTaskAssignedToOthersAsStandardUser() throws Exception {
        String newTaskJson = "{ \"id\": \"task5\", \"description\": \"Tâche pour un autre utilisateur\", \"assignedToUserId\": \"user2\", \"companyId\": \"company1\" }";
        mockMvc.perform(post("/tasks")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTaskJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateTaskAsCompanyAdmin() throws Exception {
        String newTaskJson = "{ \"id\": \"task6\", \"description\": \"Tâche pour employé\", \"assignedToUserId\": \"user1\", \"companyId\": \"company1\" }";
        mockMvc.perform(post("/tasks")
                        .param("userId", "user2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTaskJson))
                .andExpect(status().isCreated());
    }

    // **PUT /tasks/{taskId}?userId={userId}**

    @Test
    public void testUpdateTaskAsStandardUser() throws Exception {
        String updatedTaskJson = "{ \"id\": \"task1\", \"description\": \"Mise à jour de la tâche\", \"assignedToUserId\": \"user1\", \"companyId\": \"company1\" }";
        mockMvc.perform(put("/tasks/task1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTaskAsCompanyAdmin() throws Exception {
        String updatedTaskJson = "{ \"id\": \"task1\", \"description\": \"Mise à jour par l'admin\", \"assignedToUserId\": \"user1\", \"companyId\": \"company1\" }";
        mockMvc.perform(put("/tasks/task1")
                        .param("userId", "user2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk());
    }

    // **DELETE /tasks/{taskId}?userId={userId}**

    @Test
    public void testDeleteTaskAsStandardUser() throws Exception {
        mockMvc.perform(delete("/tasks/task1")
                        .param("userId", "user1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTaskAssignedToOthersAsStandardUser() throws Exception {
        mockMvc.perform(delete("/tasks/task2")
                        .param("userId", "user1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteTaskAsSuperUser() throws Exception {
        mockMvc.perform(delete("/tasks/task3")
                        .param("userId", "user3"))
                .andExpect(status().isOk());
    }
}