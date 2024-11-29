package com.quarkbs.ToDoListApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.quarkbs.ToDoListApp.repository.TodoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TodoRepository.
 */
@ExtendWith(MockitoExtension.class)
public class TodoRepositoryTest {
    private TodoRepositoryImpl todoRepository;

    private Todo todoA;
    private Todo todoB;
    private Todo todoC;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    public void setUp(){
        todoRepository = new TodoRepositoryImpl();
        todoA = new Todo();
        todoA.setId(1L);
        todoA.setText("TEST FOR TODO A");
        todoA.setDueDate(LocalDate.now().plusDays(2L));
        todoA.setStatus(true);
        todoA.setPriority(3);
        todoA.setCreationDate(LocalDateTime.now());
        todoA.setDoneDate(LocalDateTime.now().plusMinutes(60));
        todoA.setElapsedTime(60L);

        todoB = new Todo();
        todoB.setId(2L);
        todoB.setText("TEST FOR TODO B");
        todoB.setDueDate(LocalDate.now().plusDays(8));
        todoB.setStatus(true);
        todoB.setPriority(2);
        todoB.setCreationDate(LocalDateTime.now());
        todoB.setDoneDate(LocalDateTime.now().plusMinutes(120));
        todoB.setElapsedTime(120L);

        todoC = new Todo();
        todoC.setId(3L);
        todoC.setText("TEST FOR TODO C");
        todoC.setDueDate(LocalDate.now().plusDays(20));
        todoC.setStatus(true);
        todoC.setPriority(1);
        todoC.setCreationDate(LocalDateTime.now());
        todoC.setDoneDate(LocalDateTime.now().plusMinutes(240));
        todoC.setElapsedTime(240L);
    }

    /**
     * Tests the save method of TodoRepository.
     */
    @Test
    public void testSave() {
        Todo savedTodo = todoRepository.save(todoA);
        assertNotNull(savedTodo);
        assertEquals(todoA.getText(), savedTodo.getText());
    }

    /**
     * Tests the findById method of TodoRepository.
     */
    @Test
    public void testFindById() {
        Todo savedTodo = todoRepository.save(todoA);
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());
        assertTrue(foundTodo.isPresent());
        assertEquals(savedTodo.getText(), foundTodo.get().getText());
    }

    /**
     * Tests the getMetrics method of TodoRepository.
     */
    @Test
    public void testGetMetrics() {
        todoRepository.save(todoA);
        todoA.setElapsedTime(60L);
        todoRepository.save(todoB);
        todoB.setElapsedTime(120L);

        double allTotal = todoA.getElapsedTime() + todoB.getElapsedTime();
        TodoMetrics metrics = todoRepository.getMetrics(allTotal, 0, todoB.getElapsedTime(), todoA.getElapsedTime());

        assertEquals(allTotal / 2 / 60, metrics.getAvgTime());
        assertEquals(todoA.getElapsedTime() / 60, metrics.getAvgTimeHigh());
        assertEquals(todoB.getElapsedTime() / 60, metrics.getAvgTimeMedium());
        assertEquals(0, metrics.getAvgTimeLow());
    }

    /**
     * Tests the findByFilter method of TodoRepository with different parameters.
     */
    @Test
    public void testFindByFilterWithDifferentParameters() {
        todoRepository.save(todoA);
        todoRepository.save(todoB);
        todoRepository.save(todoC);

        // Test with status filter
        PageRequest pageable = PageRequest.of(0, 10);
        Map<String, Object> result = todoRepository.findByFilter(pageable, true, "", null, "", "ASC", "ASC");
        List<Todo> todos = (List<Todo>) result.get("todosList");
        int total = (int) result.get("total");
        assertEquals(3, todos.size());
        assertEquals(3, total);

        // Test with text filter
        result = todoRepository.findByFilter(pageable, null, "TEST FOR TODO A", null, "", "ASC", "ASC");
        todos = (List<Todo>) result.get("todosList");
        total = (int) result.get("total");
        assertEquals(1, todos.size());
        assertEquals(1, total);
        assertEquals(todoA.getText(), todos.get(0).getText());

        // Test with priority filter
        result = todoRepository.findByFilter(pageable, null, "", 2, "", "ASC", "ASC");
        todos = (List<Todo>) result.get("todosList");
        total = (int) result.get("total");
        assertEquals(1, todos.size());
        assertEquals(1, total);
        assertEquals(todoB.getText(), todos.get(0).getText());

        // Test with combined filters
        result = todoRepository.findByFilter(pageable, true, "TEST FOR TODO B", 2, "", "ASC", "ASC");
        todos = (List<Todo>) result.get("todosList");
        total = (int) result.get("total");
        assertEquals(1, todos.size());
        assertEquals(1, total);
        assertEquals(todoB.getText(), todos.get(0).getText());
    }

}