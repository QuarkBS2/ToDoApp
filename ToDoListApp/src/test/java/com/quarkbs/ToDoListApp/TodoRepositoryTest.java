package com.quarkbs.ToDoListApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import com.quarkbs.ToDoListApp.repository.TodoRepository;

@ExtendWith(MockitoExtension.class)
public class TodoRepositoryTest {
    @InjectMocks
    private TodoRepository todoRepository;

    private Todo todoA;
    private Todo todoB;
    private Todo todoC;

    @BeforeEach
    public void setUp(){
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
        todoC.setId(2L);
        todoC.setText("TEST FOR TODO C");
        todoC.setDueDate(LocalDate.now().plusDays(20));
        todoC.setStatus(true);
        todoC.setPriority(1);
        todoC.setCreationDate(LocalDateTime.now());
        todoC.setDoneDate(LocalDateTime.now().plusMinutes(240));
        todoC.setElapsedTime(240L);
    }

    @Test
    public void testSave() {
        Todo response = todoRepository.save(todoA);

        Assertions.assertNotNull(response);
    }

    @Test
    public void testFindByFilter(){
        todoRepository.save(todoA);
        todoRepository.save(todoB);

        //By status = true
        List<Todo> todos = todoRepository.findByFilter(true, null, null);
        Assertions.assertEquals(todoA, todos.get(0));

        //By text
        todos = todoRepository.findByFilter(null, "TEST", null);
        Assertions.assertEquals(Arrays.asList(todoA, todoB), todos);

        //By text
        todos = todoRepository.findByFilter(null, null, todoA.getPriority());
        Assertions.assertEquals(todoA, todos.get(0));
    }

    @Test
    public void testGetMetrics(){
        todoRepository.save(todoA);
        todoRepository.save(todoC);

        Long allTodo = todoA.getElapsedTime() + todoB.getElapsedTime() + todoC.getElapsedTime();

        TodoMetrics metrics = todoRepository.getMetrics(allTodo, todoC.getElapsedTime(), 0L, todoA.getElapsedTime());

        Assertions.assertEquals(allTodo / todoRepository.findAll().size() / 60, metrics.getAvgTime());
        Assertions.assertEquals(todoA.getElapsedTime()  / 60, metrics.getAvgTimeHigh());
        Assertions.assertEquals(0L, metrics.getAvgTimeMedium());
        Assertions.assertEquals(todoC.getElapsedTime()  / 60, metrics.getAvgTimeLow());
        
    }

}