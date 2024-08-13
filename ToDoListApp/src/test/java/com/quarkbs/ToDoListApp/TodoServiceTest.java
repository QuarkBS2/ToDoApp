package com.quarkbs.ToDoListApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import com.quarkbs.ToDoListApp.repository.TodoRepository;
import com.quarkbs.ToDoListApp.service.TodoService;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    private Todo todoA;
    private Todo todoB;
    private TodoMetrics metrics;
    private Optional<Todo> findByIdResponse;

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

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

        metrics = new TodoMetrics();
        metrics.setAvgTime(120L);
        metrics.setAvgTimeHigh(120L);
        metrics.setAvgTimeMedium(60L);
        metrics.setAvgTimeLow(180L);
    }

    @Test
    public void testGetTodo(){
        List<Todo> response = Arrays.asList(todoA, todoB);

        Mockito.when(todoRepository.findByFilter(anyBoolean(), anyString(), anyInt())).thenReturn(response);

        todoService.addTodo(todoA);
        todoService.addTodo(todoB);

        List<Todo> todos = todoService.getAllTodos(1, 10, "creationDate", "ASC", false, "", 3);

        Assertions.assertEquals(response, todos);
    }

    @Test
    public void testAddTodo(){
        Todo response = new Todo();
        response.setId(1L);
        response.setCreationDate(LocalDateTime.now());
        response.setText("TEST FOR ADDING VALUE");

        Mockito.when(todoRepository.save(todoA)).thenReturn(response);

        final Todo newTodo = todoService.addTodo(todoA);

        Assertions.assertEquals(response.getText(), newTodo.getText());
    }

    @Test
    public void testUpdateTodo(){
        Todo response = new Todo();
        response.setId(1L);
        response.setCreationDate(LocalDateTime.now());
        response.setText("TEST FOR ADDING VALUE");
        response.setDueDate(LocalDate.now());

        Mockito.when(todoRepository.save(todoA)).thenReturn(response);
        final Todo toUpdateTodo = todoService.addTodo(todoA);

        findByIdResponse = Optional.of(todoA);
        toUpdateTodo.setText("TEST FOR TODO A");
        toUpdateTodo.setDueDate(LocalDate.now().plusDays(2L));

        Mockito.when(todoRepository.findById(anyLong())).thenReturn(findByIdResponse);
        final Todo updatedTodo = todoService.updateTodo(toUpdateTodo.getId(), toUpdateTodo);

        Assertions.assertEquals(todoA.getText(), updatedTodo.getText());
        Assertions.assertEquals(todoA.getDueDate(), updatedTodo.getDueDate());
    }

    @Test
    public void testMarkDone(){
        Mockito.when(todoRepository.save(todoA)).thenReturn(todoA);
        final Todo toUpdateTodo = todoService.addTodo(todoA);

        findByIdResponse = Optional.of(todoA);
        
        Mockito.when(todoRepository.findById(anyLong())).thenReturn(findByIdResponse);
        final Todo updatedTodo = todoService.markDone(toUpdateTodo.getId());

        Assertions.assertTrue(updatedTodo.getStatus());
    }

    @Test
    public void testMarkUndone(){
        Mockito.when(todoRepository.save(todoB)).thenReturn(todoB);
        final Todo toUpdateTodo = todoService.addTodo(todoB);

        findByIdResponse = Optional.of(todoB);
        
        Mockito.when(todoRepository.findById(anyLong())).thenReturn(findByIdResponse);
        final Todo updatedTodo = todoService.markUndone(toUpdateTodo.getId());

        Assertions.assertFalse(updatedTodo.getStatus());
        Assertions.assertNull(updatedTodo.getDoneDate());
    }

    @Test
    public void testGetMetrics(){
        TodoMetrics todoMetrics = metrics;

        Mockito.when(todoRepository.getMetrics(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(todoMetrics);
        final TodoMetrics metricsResponse = todoService.getMetrics();

        Assertions.assertEquals(todoMetrics.getAvgTime(), metricsResponse.getAvgTime());
        Assertions.assertEquals(todoMetrics.getAvgTimeLow(), metricsResponse.getAvgTimeLow());
        Assertions.assertEquals(todoMetrics.getAvgTimeMedium(), metricsResponse.getAvgTimeMedium());
        Assertions.assertEquals(todoMetrics.getAvgTimeHigh(), metricsResponse.getAvgTimeHigh());
    }

}