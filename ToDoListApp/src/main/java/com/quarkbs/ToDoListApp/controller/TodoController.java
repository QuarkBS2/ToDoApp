package com.quarkbs.ToDoListApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.service.TodoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/todos")
public class TodoController{
    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getallTodos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "priority") String sortBy,
        @RequestParam(defaultValue = "ASC") String direction,
        @RequestParam(required=false) Boolean status,
        @RequestParam(required=false) String text,
        @RequestParam(required=false) Integer priority){

        return todoService.getAllTodos(page, size, sortBy, direction, status, text, priority);
    }

    @PostMapping
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo todo){
        todoService.addTodo(todo);
        return ResponseEntity.ok(todo);
    }
    
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @Valid @RequestBody Todo todo){
        return todoService.updateTodo(id, todo);
    }

    @PostMapping("/{id}/done")
    public Todo doneTodo(@PathVariable Long id){
        return todoService.markDone(id);
    }

    @PutMapping("/{id}/undone")
    public Todo undoneTodo(@PathVariable Long id){
        return todoService.markUndone(id);
    }

}