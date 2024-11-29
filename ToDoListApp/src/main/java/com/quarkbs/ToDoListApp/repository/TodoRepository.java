package com.quarkbs.ToDoListApp.repository;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository interface for managing Todo entities.
 */
public interface TodoRepository {
    /**
     * Retrieves all todos.
     *
     * @return a list of all todos
     */
    List<Todo> findAll();

    /**
     * Retrieves a todo by its ID.
     *
     * @param id the ID of the todo
     * @return an Optional containing the todo if found, or empty if not found
     */
    Optional<Todo> findById(Long id);

    /**
     * Saves a todo.
     *
     * @param todo the todo to save
     * @return the saved todo
     */
    Todo save(Todo todo);

    /**
     * Deletes a todo by its ID.
     *
     * @param id the ID of the todo to delete
     */
    void deleteById(Long id);

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
    Map<String, Object> findByFilter(PageRequest pageable, Boolean status, String text, Integer priority, String sortBy, String directionPriority, String directionDueDate);

    /**
     * Retrieves todo metrics.
     *
     * @param allTotal the total elapsed time for all todos
     * @param allTotalLow the total elapsed time for low priority todos
     * @param allTotalMedium the total elapsed time for medium priority todos
     * @param allTotalHigh the total elapsed time for high priority todos
     * @return the todo metrics
     */
    TodoMetrics getMetrics(double allTotal, double allTotalLow, double allTotalMedium, double allTotalHigh);
}
