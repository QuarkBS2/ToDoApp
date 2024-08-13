package com.quarkbs.ToDoListApp.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;

@Repository
public class TodoRepository {
    private final List<Todo> todos = new ArrayList<>();
    private final TodoMetrics metrics = new TodoMetrics();
    private long nextId = 1;

    public List<Todo> findAll(){
        return new ArrayList<>(todos);
    }

    public TodoMetrics getMetrics(Long allTotal, Long allTotalLow, Long allTotalMedium, Long allTotalHigh){
        List<Todo> todosElapsedNulls = todos.stream().filter(todo -> todo.getElapsedTime() == null).collect(Collectors.toList());
        List<Todo> todosLows = todos.stream().filter(todo -> todo.getPriority() == 1).filter(todo -> todo.getElapsedTime() != null).collect(Collectors.toList());
        List<Todo> todosMedium = todos.stream().filter(todo -> todo.getPriority() == 2).filter(todo -> todo.getElapsedTime() != null).collect(Collectors.toList());
        List<Todo> todosHigh = todos.stream().filter(todo -> todo.getPriority() == 3).filter(todo -> todo.getElapsedTime() != null).collect(Collectors.toList());

        allTotal = todos.size() - todosElapsedNulls.size() == 0 ? 0L :(allTotal / (todos.size() - todosElapsedNulls.size())) / 60;
        allTotalLow = todosLows.isEmpty() ? 0L : (allTotalLow / todosLows.size()) / 60;
        allTotalMedium = todosMedium.isEmpty() ? 0L : (allTotalMedium / todosMedium.size()) / 60;
        allTotalHigh= todosHigh.isEmpty() ? 0L : (allTotalHigh / todosHigh.size()) / 60;
        
        metrics.setAvgTime(allTotal);
        metrics.setAvgTimeLow(allTotalLow);
        metrics.setAvgTimeMedium(allTotalMedium);
        metrics.setAvgTimeHigh(allTotalHigh);


        return metrics;
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
