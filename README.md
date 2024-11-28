# To-Do List Application

## Table of Contents
- [Description](#description)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [Accessing Swagger UI](#accessing-swagger-ui)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Description
The **To-Do List Application** is a robust task management system developed using Spring Boot. It allows users to create, view, update, and delete tasks with role-based access control. The application supports three types of users:

- **Standard Users**: Can manage their own tasks.
- **Company-Admin Users**: Can manage tasks for themselves and their company’s employees.
- **Super Users**: Have access to all tasks across all companies.

This application is designed to demonstrate best practices in API development, including secure authentication, comprehensive testing, and detailed documentation.

## Features
- **User Authentication and Authorization**: Secure login with role-based permissions.
- **CRUD Operations**: Create, Read, Update, and Delete tasks.
- **Role-Based Access Control**: Different levels of access for Standard, Company-Admin, and Super Users.
- **In-Memory Data Storage**: Utilizes in-memory `Map` structures for managing users and tasks.
- **Swagger UI Integration**: Interactive API documentation for easy testing and exploration.
- **Automated Testing**: Comprehensive unit and integration tests to ensure reliability.

## Technologies Used
- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Springdoc OpenAPI (Swagger UI)**
- **JUnit 5**
- **Maven**
- **Git & GitHub**

## Getting Started

### Prerequisites
Ensure you have the following installed on your machine:
- **Java 17** or higher
- **Maven 3.6** or higher
- **Git**
- **An IDE** (e.g., IntelliJ IDEA, Eclipse, VS Code)

### Installation
1. **Clone the Repository**
   ```bash
   git clone https://github.com/Heaves7/todolist.git
2.	**Navigate to the Project Directory
     cd todolist

3. Build the Project with Maven
     mvn clean install
   
4. Run the Application
     mvn spring-boot:run
   
5. Access Swagger UI
      Open your browser and navigate to http://localhost:8080/swagger-ui/index.html to explore and interact with the API
6.	Explore Endpoints :
	•	Create Task: POST /tasks?userId={userId}
	•	Get All Tasks: GET /tasks?userId={userId}
	•	Get Task by ID: GET /tasks/{id}?userId={userId}
	•	Update Task: PUT /tasks/{taskId}?userId={userId}
	•	Delete Task: DELETE /tasks/{taskId}?userId={userId}
7.	Interact with the API
  Use the Swagger UI to send requests and view responses directly from your browser.

API Documentation

The API is fully documented using Swagger. Below is a brief overview of the main endpoints:
	•	POST /tasks?userId={userId}
	    Description: Create a new task.
	    •	Parameters:
	        •	userId (query): ID of the user creating the task.
	•	Request Body: Task object.
	•	Responses:
	    201 Created: Task created successfully.
	    403 Forbidden: Permission denied.
	    404 Not Found: User not found.
	•	GET /tasks?userId={userId}
	    Description: Retrieve all accessible tasks for a user.
	    •	Parameters:
	        userId (query): ID of the user.
	    •	Responses:
	        200 OK: List of tasks.
	        404 Not Found: User not found.
	•	GET /tasks/{id}?userId={userId}
	    Description: Retrieve a specific task by ID.
	    •	Parameters:
	        id (path): ID of the task.
	        userId (query): ID of the user.
	•	Responses:
	    200 OK: Task details.
	    403 Forbidden: Permission denied.
	    404 Not Found: Task or user not found.
	•	PUT /tasks/{taskId}?userId={userId}
	    Description: Update an existing task.
	    •	Parameters:
	        taskId (path): ID of the task.
	        userId (query): ID of the user.
	    Request Body: Updated Task object.
	•	Responses:
	    200 OK: Task updated successfully.
	    403 Forbidden: Permission denied.
	    404 Not Found: Task or user not found.
	•	DELETE /tasks/{taskId}?userId={userId}
	    Description: Delete an existing task.
	    •	Parameters:
	        taskId (path): ID of the task.
	        userId (query): ID of the user.
	•	Responses:
	    200 OK: Task deleted successfully.
	    403 Forbidden: Permission denied.
	    404 Not Found: Task or user not found.

For detailed information, refer to the Swagger UI.

8. Running Tests
   mvn test

Test Coverage

Ensure that all critical components are covered by tests, including:
	•	TaskController
	•	TaskService
	•	UserService
	•	Role-Based Access Control

Contact

For any questions or feedback, feel free to reach out:
	•	GitHub: Heaves7
	•	Email: jemekoka95@gmail.com







