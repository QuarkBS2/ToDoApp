package com.quarkbs.ToDoListApp.controller;

import com.quarkbs.ToDoListApp.dto.TodoDTO;
import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import com.quarkbs.ToDoListApp.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for managing Todo entities.
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    /**
     * Retrieves a paginated list of todos with optional filters.
     *
     * @param page the page number (default is 1)
     * @param size the page size (default is 10)
     * @param sortBy the field to sort by (default is creationDate)
     * @param directionPriority the sort direction for priority (default is ASC)
     * @param directionDueDate the sort direction for due date (default is ASC)
     * @param status the status filter (optional)
     * @param text the text filter (optional)
     * @param priority the priority filter (optional)
     * @return a ResponseEntity containing the paginated list of todos
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTodos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "ASC") String directionPriority,
            @RequestParam(defaultValue = "ASC") String directionDueDate,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Integer priority) {

        page = Math.max(page - 1, 0);
        PageRequest pageable = PageRequest.of(page, size);
        Map<String, Object> response = todoService.getAllTodos(pageable, status, text, priority, sortBy, directionPriority, directionDueDate);
        return ResponseEntity.ok(response);
    }

    /**
     * Adds a new todo.
     *
     * @param todoDTO the data transfer object containing the new todo data
     * @return a ResponseEntity containing the created todo as a TodoDTO
     */
    @PostMapping
    public ResponseEntity<TodoDTO> addTodo(@RequestBody TodoDTO todoDTO) {
        Todo createdTodo = todoService.addTodo(todoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdTodo));
    }

    /**
     * Updates an existing todo.
     *
     * @param id the ID of the todo to update
     * @param todoDTO the updated todo data
     * @return a ResponseEntity containing the updated todo as a TodoDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoDTO todoDTO) {
        Todo updatedTodo = todoService.updateTodo(id, todoDTO);
        return ResponseEntity.ok(convertToDTO(updatedTodo));
    }

    /**
     * Marks a todo as done.
     *
     * @param id the ID of the todo to mark as done
     * @return a ResponseEntity containing the updated todo as a TodoDTO
     */
    @PostMapping("/{id}/done")
    public ResponseEntity<TodoDTO> doneTodo(@PathVariable Long id) {
        Todo doneTodo = todoService.markDone(id);
        return ResponseEntity.ok(convertToDTO(doneTodo));
    }

    /**
     * Marks a todo as undone.
     *
     * @param id the ID of the todo to mark as undone
     * @return a ResponseEntity containing the updated todo as a TodoDTO
     */
    @PutMapping("/{id}/undone")
    public ResponseEntity<TodoDTO> undoneTodo(@PathVariable Long id) {
        Todo undoneTodo = todoService.markUndone(id);
        return ResponseEntity.ok(convertToDTO(undoneTodo));
    }

    /**
     * Retrieves todo metrics.
     *
     * @return a ResponseEntity containing the todo metrics
     */
    @GetMapping("/metrics")
    public ResponseEntity<TodoMetrics> metrics() {
        TodoMetrics metrics = todoService.getMetrics();
        return ResponseEntity.ok(metrics);
    }

    /**
     * Converts a Todo entity to a TodoDTO.
     *
     * @param todo the Todo entity to convert
     * @return the converted TodoDTO
     */
    private TodoDTO convertToDTO(Todo todo) {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setText(todo.getText());
        todoDTO.setStatus(todo.getStatus());
        todoDTO.setDueDate(todo.getDueDate());
        todoDTO.setPriority(todo.getPriority());
        todoDTO.setCreationDate(todo.getCreationDate());
        todoDTO.setDoneDate(todo.getDoneDate());
        return todoDTO;
    }
}