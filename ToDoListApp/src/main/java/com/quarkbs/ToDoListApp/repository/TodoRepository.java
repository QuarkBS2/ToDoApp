package com.quarkbs.ToDoListApp.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.quarkbs.ToDoListApp.entity.Todo;

@Repository
public class TodoRepository {
    private final List<Todo> todos = new ArrayList<>();
    private long nextId = 1;

    public List<Todo> findAll(){
        return new ArrayList<>(todos);
    }

    public Optional<Todo> findById(Long id){
        return todos.stream().filter(todo -> todo.getId().equals(id)).findFirst();
    }

    public Todo save(Todo todo){
        if(todo.getId() == null){
            todo.setId(nextId++);
        } else{
            deleteById(todo.getId());
        }
        if (todo.getCreationDate() == null){
            todo.setCreationDate(LocalDateTime.now());
        }
        todos.add(todo);
        return todo;
    }

    public void deleteById(Long id){
        todos.removeIf(todo -> todo.getId().equals(id));
    }

    public List<Todo> findByFilter(Boolean status, String text, Integer priority){
        return todos.stream()
        .filter(todo -> status == null || todo.getStatus().equals(status))
        .filter(todo -> text == null || todo.getText().toLowerCase().contains(text.toLowerCase()))
        .filter(todo -> priority == null || todo.getPriority() == priority)
        .collect(Collectors.toList());
    }

}
