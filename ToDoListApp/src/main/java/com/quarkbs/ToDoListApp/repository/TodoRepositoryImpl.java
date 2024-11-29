package com.quarkbs.ToDoListApp.repository;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the TodoRepository interface.
 */
@Repository
public class TodoRepositoryImpl implements TodoRepository {
    private final Map<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private final TodoMetrics metrics = new TodoMetrics();

    /**
     * Retrieves all todos.
     *
     * @return a list of all todos
     */
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    /**
     * Retrieves todo metrics.
     *
     * @param allTotal the total elapsed time for all todos
     * @param allTotalLow the total elapsed time for low priority todos
     * @param allTotalMedium the total elapsed time for medium priority todos
     * @param allTotalHigh the total elapsed time for high priority todos
     * @return the todo metrics
     */
    public TodoMetrics getMetrics(double allTotal, double allTotalLow, double allTotalMedium, double allTotalHigh) {
        int todosElapsedNullsSize = todos.values().stream().filter(todo -> todo.getElapsedTime() == null).toList().size();
        int todosLowsSize = todos.values().stream().filter(todo -> todo.getPriority() == 1).filter(todo -> todo.getElapsedTime() != null).toList().size();
        int todosMediumSize = todos.values().stream().filter(todo -> todo.getPriority() == 2).filter(todo -> todo.getElapsedTime() != null).toList().size();
        int todosHighSize = todos.values().stream().filter(todo -> todo.getPriority() == 3).filter(todo -> todo.getElapsedTime() != null).toList().size();

        allTotal = todos.size() - todosElapsedNullsSize == 0 ? 0L : (allTotal / (todos.size() - todosElapsedNullsSize)) / 60;
        allTotalLow = todosLowsSize == 0 ? 0L : (allTotalLow / todosLowsSize) / 60;
        allTotalMedium = todosMediumSize == 0 ? 0L : (allTotalMedium / todosMediumSize) / 60;
        allTotalHigh = todosHighSize == 0 ? 0L : (allTotalHigh / todosHighSize) / 60;

        metrics.setAvgTime(allTotal);
        metrics.setAvgTimeLow(allTotalLow);
        metrics.setAvgTimeMedium(allTotalMedium);
        metrics.setAvgTimeHigh(allTotalHigh);


        return metrics;
    }

    /**
     * Retrieves a todo by its ID.
     *
     * @param id the ID of the todo
     * @return an Optional containing the todo if found, or empty if not found
     */
    public Optional<Todo> findById(Long id) {
        return todos.values().stream().filter(todo -> todo.getId().equals(id)).findFirst();
    }

    /**
     * Saves a todo.
     *
     * @param todo the todo to save
     * @return the saved todo
     */
    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(nextId.getAndIncrement());
        } else {
            deleteById(todo.getId());
        }
        if (todo.getCreationDate() == null) {
            todo.setCreationDate(LocalDateTime.now());
        }
        todos.put(todo.getId(), todo);
        return todo;
    }

    /**
     * Deletes a todo by its ID.
     *
     * @param id the ID of the todo to delete
     */
    public void deleteById(Long id) {
        todos.remove(id);
    }

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
    public Map<String, Object> findByFilter(PageRequest pageable, Boolean status, String text, Integer priority, String sortBy, String directionPriority, String directionDueDate) {
        List<Todo> filteredTodos = todos.values().stream()
                .filter(todo -> status == null || todo.getStatus().equals(status))
                .filter(todo -> text == null || todo.getText().toLowerCase().contains(text.toLowerCase()))
                .filter(todo -> priority == null || todo.getPriority() == priority)
                .collect(Collectors.toList());

        if (!sortBy.isEmpty()) {
            Comparator<Todo> comparator = Comparator.comparingInt(todo -> 0);
            comparator = switch (sortBy) {
                case "priority" ->
                        comparator.thenComparing(Objects.equals(directionPriority, "ASC") ? Comparator.comparingInt(Todo::getPriority) : Comparator.comparingInt(Todo::getPriority).reversed());
                case "dueDate" ->
                        comparator.thenComparing(Objects.equals(directionDueDate, "ASC") ? Comparator.comparing(Todo::getDueDate) : Comparator.comparing(Todo::getDueDate).reversed());
                case "priorityDueDate" ->
                        comparator.thenComparing(Objects.equals(directionPriority, "ASC") ? Comparator.comparingInt(Todo::getPriority) : Comparator.comparingInt(Todo::getPriority).reversed())
                                .thenComparing(Objects.equals(directionDueDate, "ASC") ? Comparator.comparing(Todo::getDueDate) : Comparator.comparing(Todo::getDueDate).reversed());
                case "creationDate" ->
                        comparator.thenComparing(Objects.equals(directionPriority, "ASC") ? Comparator.comparing(Todo::getCreationDate) : Comparator.comparing(Todo::getCreationDate).reversed());
                default -> comparator;
            };
            filteredTodos.sort(comparator);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredTodos.size());
        List<Todo> paginatedTodos = filteredTodos.subList(start, end);

        return Map.of(
                "todosList", new PageImpl<>(paginatedTodos, pageable, filteredTodos.size()).getContent(),
                "total", filteredTodos.size()
        );

    }

}
