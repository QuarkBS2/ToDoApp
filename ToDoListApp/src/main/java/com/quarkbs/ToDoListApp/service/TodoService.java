package com.quarkbs.ToDoListApp.service;

import com.quarkbs.ToDoListApp.dto.TodoDTO;
import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import com.quarkbs.ToDoListApp.exception.TodoNotFoundException;
import com.quarkbs.ToDoListApp.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing Todo entities.
 */
@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    /**
     * Retrieves a paginated list of todos with optional filters.
     *
     * @param pageable the pagination information
     * @param status the status filter (optional)
     * @param text the text filter (optional)
     * @param priority the priority filter (optional)
     * @param sortBy the field to sort by
     * @param directionPriority the sort direction for priority
     * @param directionDueDate the sort direction for due date
     * @return a map containing the paginated list of todos and additional metadata
     */
    public Map<String, Object> getAllTodos(PageRequest pageable, Boolean status, String text, Integer priority, String sortBy, String directionPriority, String directionDueDate) {
        directionPriority = directionPriority.toUpperCase();
        directionDueDate = directionDueDate.toUpperCase();
        return todoRepository.findByFilter(pageable, status, text, priority, sortBy, directionPriority, directionDueDate);
    }

    /**
     * Creates a new todo.
     *
     * @param todoDTO the todo to create
     * @return the created todo
     */
    public Todo addTodo(TodoDTO todoDTO) {
        Todo todo = convertToEntity(todoDTO);
        if (todo.getDueDate() != null) {
            todo.setPriority(calculatePriority(todo.getDueDate()));
        }
        todo.setStatus(false);
        return todoRepository.save(todo);
    }

    /**
     * Updates an existing todo.
     *
     * @param id the ID of the todo to update
     * @param todoDTO the updated todo data
     * @return the updated todo
     * @throws TodoNotFoundException if the todo is not found
     */
    public Todo updateTodo(Long id, TodoDTO todoDTO) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("To Do not found"));
        existingTodo.setText(todoDTO.getText());
        existingTodo.setDueDate(todoDTO.getDueDate());
        existingTodo.setPriority(calculatePriority(todoDTO.getDueDate()));
        existingTodo.setStatus(todoDTO.getStatus());

        return todoRepository.save(existingTodo);
    }

    /**
     * Marks a todo as done.
     *
     * @param id the ID of the todo to mark as done
     * @return the updated todo
     * @throws TodoNotFoundException if the todo is not found
     */
    public Todo markDone(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException("To Do not found"));
        todo.setStatus(true);
        todo.setDoneDate(LocalDateTime.now());
        Long diffTime = ChronoUnit.SECONDS.between(todo.getCreationDate(), todo.getDoneDate());
        todo.setElapsedTime(diffTime);
        return todoRepository.save(todo);
    }

    /**
     * Marks a todo as undone.
     *
     * @param id the ID of the todo to mark as undone
     * @return the updated todo
     * @throws TodoNotFoundException if the todo is not found
     */
    public Todo markUndone(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("To Do not found"));
        todo.setStatus(false);
        todo.setDoneDate(null);
        todo.setElapsedTime(null);
        return todoRepository.save(todo);
    }

    /**
     * Calculates the priority of a todo based on its due date.
     *
     * @param dueDate the due date of the todo
     * @return the calculated priority
     */
    private int calculatePriority(LocalDate dueDate) {
        LocalDate today = LocalDate.now();

        if (dueDate != null) {
            if ((today.until(dueDate, ChronoUnit.DAYS) > 14)) {
                return 1;
            }

            if ((today.until(dueDate, ChronoUnit.DAYS) > 7) && (today.until(dueDate, ChronoUnit.DAYS) <= 14)) {
                return 2;
            }
            if ((today.until(dueDate, ChronoUnit.DAYS) <= 7)) {
                return 3;
            }
        }
        return 0;
    }

    /**
     * Retrieves todo metrics.
     *
     * @return the todo metrics
     */
    public TodoMetrics getMetrics() {
        List<Todo> todos = todoRepository.findAll();
        double allTotal = 0;
        double allTotalLow = 0;
        double allTotalMedium = 0;
        double allTotalHigh = 0;

        for (Todo todo : todos) {
            if (todo.getElapsedTime() != null) {
                if (todo.getPriority() == 1) {
                    allTotalLow += todo.getElapsedTime();
                }
                if (todo.getPriority() == 2) {
                    allTotalMedium += todo.getElapsedTime();
                }
                if (todo.getPriority() == 3) {
                    allTotalHigh += todo.getElapsedTime();
                }
                allTotal += todo.getElapsedTime();
            }
        }

        return todoRepository.getMetrics(allTotal, allTotalLow, allTotalMedium, allTotalHigh);
    }

    /**
     * Converts a TodoDTO to a Todo entity.
     *
     * @param todoDTO the TodoDTO to convert
     * @return the converted Todo entity
     */
    private Todo convertToEntity(TodoDTO todoDTO) {
        Todo todo = new Todo();
        todo.setText(todoDTO.getText());
        todo.setStatus(todoDTO.getStatus());
        todo.setDueDate(todoDTO.getDueDate());
        todo.setPriority(todoDTO.getPriority());
        todo.setCreationDate(todoDTO.getCreationDate());
        todo.setDoneDate(todoDTO.getDoneDate());
        return todo;
    }
}
