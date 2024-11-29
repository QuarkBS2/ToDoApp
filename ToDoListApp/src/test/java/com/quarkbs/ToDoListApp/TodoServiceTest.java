package com.quarkbs.ToDoListApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.quarkbs.ToDoListApp.dto.TodoDTO;
import com.quarkbs.ToDoListApp.exception.TodoNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import com.quarkbs.ToDoListApp.repository.TodoRepository;
import com.quarkbs.ToDoListApp.service.TodoService;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Test class for TodoService.
 */
@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    private Todo todoA;
    private Todo todoB;
    private TodoDTO todoDTOA;
    private TodoDTO todoDTOB;
    private TodoMetrics metrics;
    private Optional<Todo> findByIdResponse;

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    public void setUp(){
        todoA = new Todo();
        todoA.setId(1L);
        todoA.setText("TEST FOR TODO A");
        todoA.setDueDate(LocalDate.now().plusDays(2L));
        todoA.setStatus(false);
        todoA.setPriority(3);
        todoA.setCreationDate(LocalDateTime.now());

        todoB = new Todo();
        todoB.setId(2L);
        todoB.setText("TEST FOR TODO B");
        todoB.setDueDate(LocalDate.now().minusDays(2L));
        todoB.setStatus(true);
        todoB.setPriority(3);
        todoB.setCreationDate(LocalDateTime.now());
        todoB.setDoneDate(LocalDateTime.now().plusMinutes(60));

        todoDTOA = new TodoDTO();
        todoDTOA.setText("TEST FOR TODO A");
        todoDTOA.setDueDate(LocalDate.now().plusDays(2L));
        todoDTOA.setStatus(false);
        todoDTOA.setPriority(3);
        todoDTOA.setCreationDate(LocalDateTime.now());

        todoDTOB = new TodoDTO();
        todoDTOB.setText("TEST FOR TODO B");
        todoDTOB.setDueDate(LocalDate.now().minusDays(2L));
        todoDTOB.setStatus(true);
        todoDTOB.setPriority(3);
        todoDTOB.setCreationDate(LocalDateTime.now());
        todoDTOB.setDoneDate(LocalDateTime.now().plusMinutes(60));

        metrics = new TodoMetrics();
        metrics.setAvgTime(120L);
        metrics.setAvgTimeHigh(120L);
        metrics.setAvgTimeMedium(60L);
        metrics.setAvgTimeLow(180L);
    }

    /**
     * Tests the getAllTodos method of TodoService.
     */
    @Test
    public void testGetTodos() {
        List<Todo> response = Arrays.asList(todoA, todoB);

        Mockito.when(todoRepository.findByFilter(any(PageRequest.class), anyBoolean(), anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(Map.of("todos", response));

        Map<String, Object> todos = todoService.getAllTodos(PageRequest.of(0, 10), false, "", 3, "creationDate", "ASC", "ASC");

        Assertions.assertEquals(response, todos.get("todos"));
    }

    /**
     * Tests the addTodo method of TodoService.
     */
    @Test
    public void testAddTodo() {
        Todo response = new Todo();
        response.setId(1L);
        response.setCreationDate(LocalDateTime.now());
        response.setText("TEST FOR ADDING A");

        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(response);

        final Todo newTodo = todoService.addTodo(todoDTOA);

        Assertions.assertEquals(response.getText(), newTodo.getText());
    }

    /**
     * Tests the updateTodo method of TodoService.
     */
    @Test
    public void testUpdateTodo() {
        Todo existingTodo = new Todo();
        existingTodo.setId(1L);
        existingTodo.setText("Existing Todo");
        existingTodo.setDueDate(LocalDate.now().plusDays(5));
        existingTodo.setStatus(false);
        existingTodo.setPriority(2);
        existingTodo.setCreationDate(LocalDateTime.now());

        TodoDTO updatedTodoDTO = new TodoDTO();
        updatedTodoDTO.setText("Updated Todo");
        updatedTodoDTO.setDueDate(LocalDate.now().plusDays(10));
        updatedTodoDTO.setStatus(true);
        updatedTodoDTO.setPriority(2);
        updatedTodoDTO.setCreationDate(LocalDateTime.now());

        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo);

        Todo updatedTodo = todoService.updateTodo(1L, updatedTodoDTO);

        Assertions.assertEquals(updatedTodoDTO.getText(), updatedTodo.getText());
        Assertions.assertEquals(updatedTodoDTO.getDueDate(), updatedTodo.getDueDate());
        Assertions.assertEquals(updatedTodoDTO.getStatus(), updatedTodo.getStatus());
        Assertions.assertEquals(updatedTodoDTO.getPriority(), updatedTodo.getPriority());
    }

    /**
     * Tests the markDone method of TodoService.
     */
    @Test
    public void testMarkDone() {
        Mockito.when(todoRepository.findById(todoA.getId())).thenReturn(Optional.of(todoA));
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(todoA);

        Todo updatedTodo = todoService.markDone(todoA.getId());

        Assertions.assertTrue(updatedTodo.getStatus());
        Assertions.assertNotNull(updatedTodo.getDoneDate());
        Assertions.assertNotNull(updatedTodo.getElapsedTime());
        Mockito.verify(todoRepository, Mockito.times(1)).findById(todoA.getId());
        Mockito.verify(todoRepository, Mockito.times(1)).save(todoA);
    }

    /**
     * Tests the markUndone method of TodoService.
     */
    @Test
    public void testMarkUndone() {
        Mockito.when(todoRepository.findById(todoB.getId())).thenReturn(Optional.of(todoB));
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(todoB);

        Todo updatedTodo = todoService.markUndone(todoB.getId());

        Assertions.assertFalse(updatedTodo.getStatus());
        Assertions.assertNull(updatedTodo.getDoneDate());
        Assertions.assertNull(updatedTodo.getElapsedTime());
        Mockito.verify(todoRepository, Mockito.times(1)).findById(todoB.getId());
        Mockito.verify(todoRepository, Mockito.times(1)).save(todoB);
    }

    /**
     * Tests the getMetrics method of TodoService.
     */
    @Test
    public void testGetMetrics() {
        TodoMetrics todoMetrics = metrics;

        Mockito.when(todoRepository.getMetrics(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(todoMetrics);
        final TodoMetrics metricsResponse = todoService.getMetrics();

        Assertions.assertEquals(todoMetrics.getAvgTime(), metricsResponse.getAvgTime());
        Assertions.assertEquals(todoMetrics.getAvgTimeLow(), metricsResponse.getAvgTimeLow());
        Assertions.assertEquals(todoMetrics.getAvgTimeMedium(), metricsResponse.getAvgTimeMedium());
        Assertions.assertEquals(todoMetrics.getAvgTimeHigh(), metricsResponse.getAvgTimeHigh());
    }

    /**
     * Tests the updateTodo method of TodoService to throw TodoNotFoundException.
     */
    @Test
    public void testUpdateTodoThrowsException() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        TodoDTO updatedTodo = new TodoDTO();
        updatedTodo.setText("Updated Text");

        assertThrows(TodoNotFoundException.class, () -> {
            todoService.updateTodo(1L, updatedTodo);
        });
    }

    /**
     * Tests the markDone method of TodoService to throw TodoNotFoundException.
     */
    @Test
    public void testMarkDoneThrowsException() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> {
            todoService.markDone(1L);
        });
    }

    /**
     * Tests the markUndone method of TodoService to throw TodoNotFoundException.
     */
    @Test
    public void testMarkUndoneThrowsException() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> {
            todoService.markUndone(1L);
        });
    }

}