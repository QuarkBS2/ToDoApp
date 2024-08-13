package com.quarkbs.ToDoListApp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import com.quarkbs.ToDoListApp.repository.TodoRepository;

@Service
public class TodoService{
    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getAllTodos(int page, int size, String sortBy, String direction, Boolean status, String text, Integer priority){
        List<Todo> filteredTodos = todoRepository.findByFilter(status, text, priority);

        Comparator<Todo> comparator;
        comparator = switch (sortBy) {
            case "dueDate" -> Comparator.comparing(Todo::getDueDate, Comparator.nullsFirst(Comparator.naturalOrder()));
            case "priority" -> Comparator.comparing(Todo::getPriority);
            default -> Comparator.comparing(Todo::getCreationDate);
        };
        if ("DESC".equalsIgnoreCase(direction)){
            comparator = comparator.reversed();
        }
        List<Todo> sortedTodos = filteredTodos.stream()
        .sorted(comparator)
        .collect(Collectors.toList());

        return sortedTodos;
    }

    public Todo addTodo(Todo todo){
        if(todo.getDueDate() != null){
            todo.setPriority(calculatePriority(todo.getDueDate()));
        }
        todo.setStatus(false);
        return todoRepository.save(todo);

    }

    public Todo updateTodo(Long id, Todo updatedTodo) {
            Todo existingTodo = todoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("To Do not found"));
            existingTodo.setText(updatedTodo.getText());
            existingTodo.setDueDate(updatedTodo.getDueDate());
            existingTodo.setPriority(calculatePriority(updatedTodo.getDueDate()));
            existingTodo.setStatus(updatedTodo.getStatus());

            return todoRepository.save(existingTodo);
    }

    public Todo markDone(Long id){
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new RuntimeException("To Do not found"));
        todo.setStatus(true);
        todo.setDoneDate(LocalDateTime.now());
        Long difftime = ChronoUnit.SECONDS.between(todo.getCreationDate(), todo.getDoneDate());
        todo.setElapsedTime(difftime);
        return todoRepository.save(todo);
    }

    public Todo markUndone(Long id){
        Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("To Do not found"));
        todo.setStatus(false);
        todo.setDoneDate(null);
        todo.setElapsedTime(null);
        return todoRepository.save(todo);
    }


    private int calculatePriority(LocalDate dueDate) {
        LocalDate today = LocalDate.now();

        if (dueDate != null){
            if (((int)(today.until(dueDate).getDays()) > 14)){
                return 1;
            }
    
            if(((int)(today.until(dueDate).getDays()) > 7) && ((int)(today.until(dueDate).getDays()) <=14)){
                return 2;
            }
            if(((int)(today.until(dueDate).getDays()) <= 7)){
                return 3;
            }
        }
        return 0;
    }

    public TodoMetrics getMetrics(){
        List<Todo> todos = todoRepository.findAll();
        Long allTotal = 0L;
        Long allTotalLow = 0L;
        Long allTotalMedium = 0L;
        Long allTotalHigh = 0L;
        
        for (Todo todo: todos){
            if(todo.getElapsedTime() != null){
                if(todo.getPriority() == 1){
                    allTotalLow += todo.getElapsedTime();
                }
                if(todo.getPriority() == 2){
                    allTotalMedium += todo.getElapsedTime();
                }
                if(todo.getPriority() == 3){
                    allTotalHigh += todo.getElapsedTime();
                }
                allTotal += todo.getElapsedTime();
            }
        }

        return todoRepository.getMetrics(allTotal, allTotalLow, allTotalMedium, allTotalHigh);
    }

}
