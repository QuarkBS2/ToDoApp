# ToDoApp

## Overview
ToDoApp is a simple task management application built with Java, Spring Boot, and React. It allows users to create, update, and manage their tasks efficiently. The application also provides metrics for task completion times.

## Features
- Create and update tasks
- Mark tasks as done or undone
- Filter and paginate tasks
- View metrics for task completion times

## Technologies Used
- Java
- Spring Boot
- Maven
- React
- NPM

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6.0 or higher
- Node.js 14 or higher
- NPM 6 or higher

### Backend Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/QuarkBS2/ToDoApp.git
    cd ToDoApp
    ```

2. Navigate to the backend directory:
    ```sh
    cd ToDoListApp
    ```

3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

4. Run the Spring Boot application:
    ```sh
    mvn spring-boot:run
    ```

### Frontend Setup
1. Navigate to the frontend directory:
    ```sh
    cd frontend
    ```

2. Install the dependencies:
    ```sh
    npm install
    ```

3. Start the React application:
    ```sh
    npm start
    ```

## API Endpoints

### Todo Endpoints
- **GET /api/todos**: Retrieve all todos with optional filters and pagination.
- **POST /api/todos**: Create a new todo.
- **PUT /api/todos/{id}**: Update an existing todo.
- **POST /api/todos/{id}/done**: Mark a todo as done.
- **PUT /api/todos/{id}/undone**: Mark a todo as undone.
- **GET /api/todos/metrics**: Retrieve todo metrics.

## Example Requests

### Get All Todos
```sh
curl -X GET "http://localhost:8080/api/todos?page=1&size=10&sortBy=creationDate&directionPriority=ASC&directionDueDate=ASC"
```

### Create a Todo
```sh
curl -X POST "http://localhost:8080/api/todos" -H "Content-Type: application/json" -d '{
  "text": "New Task",
  "dueDate": "2023-12-31",
  "priority": 2
}'
````

### Update a Todo
```sh
curl -X PUT "http://localhost:8080/api/todos/1" -H "Content-Type: application/json" -d '{
  "text": "Updated Task",
  "dueDate": "2023-12-31",
  "priority": 1
}'
```

### Mark a Todo as Done
```sh
curl -X POST "http://localhost:8080/api/todos/1/done"
```

### Mark a Todo as Undone
```sh
curl -X POST "http://localhost:8080/api/todos/1/undone"
```

### Get Todo Metrics
```sh
curl -X GET "http://localhost:8080/api/todos/metrics"
```