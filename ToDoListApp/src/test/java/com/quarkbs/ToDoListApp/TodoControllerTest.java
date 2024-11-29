package com.quarkbs.ToDoListApp;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.quarkbs.ToDoListApp.dto.TodoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quarkbs.ToDoListApp.controller.TodoController;
import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import com.quarkbs.ToDoListApp.service.TodoService;

/**
 * Test class for TodoController.
 */
@WebMvcTest(value=TodoController.class)
@ExtendWith(MockitoExtension.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    private Todo todoA;
    private Todo todoB;
    private TodoDTO todoDTOA;
    private TodoDTO todoDTOB;
    private TodoMetrics metrics;

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

        todoB = new Todo();
        todoB.setId(2L);
        todoB.setText("TEST FOR TODO B");
        todoB.setDueDate(LocalDate.now().minusDays(2L));
        todoB.setStatus(true);

        todoDTOA = new TodoDTO();
        todoDTOA.setText("TEST FOR TODO A");
        todoDTOA.setDueDate(LocalDate.now().plusDays(2L));
        todoDTOA.setStatus(false);
        todoDTOA.setPriority(3);

        todoDTOB = new TodoDTO();
        todoDTOB.setText("TEST FOR TODO B");
        todoDTOB.setDueDate(LocalDate.now().minusDays(2L));
        todoDTOB.setStatus(true);
        todoDTOB.setPriority(3);

        metrics = new TodoMetrics();
        metrics.setAvgTime(120L);
        metrics.setAvgTimeHigh(120L);
        metrics.setAvgTimeMedium(60L);
        metrics.setAvgTimeLow(180L);
    }

    /**
     * Tests the addTodo method of TodoController.
     */
    @Test
    public void testAddTodo() throws Exception {
        Mockito.when(todoService.addTodo(Mockito.any(TodoDTO.class))).thenReturn(todoA);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTOA)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value(todoA.getText()))
                .andExpect(jsonPath("$.status").value(todoA.getStatus()))
                .andExpect(jsonPath("$.dueDate").value(todoA.getDueDate().toString()))
                .andExpect(jsonPath("$.priority").value(todoA.getPriority()));
    }

    /**
     * Tests the updateTodo method of TodoController.
     */
    @Test
    public void testUpdateTodo() throws Exception {
        Todo updatedTodo = todoA;
        updatedTodo.setText("Updated Text");
        Mockito.when(todoService.updateTodo(Mockito.anyLong(), Mockito.any(TodoDTO.class))).thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todos/{id}", updatedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTOA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated Text"));
    }

    /**
     * Tests the getAllTodos method of TodoController.
     */
    @Test
    public void testGetAllTodos() throws Exception {
        List<Todo> todos = Arrays.asList(todoA, todoB);
        Mockito.when(todoService.getAllTodos(Mockito.any(PageRequest.class), Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(Map.of("todos", todos));

        MvcResult response = mockMvc.perform(get("/api/todos")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sortBy", "creationDate")
                        .param("directionPriority", "ASC")
                        .param("directionDueDate", "ASC")
                        .param("status", "false")
                        .param("text", "")
                        .param("priority", "3"))
                .andReturn();

        response.getResponse();
        Map<String, Object> result = objectMapper.readValue(response.getResponse().getContentAsByteArray(), new TypeReference<Map<String, Object>>() {});
        List<Todo> todosResult = objectMapper.convertValue(result.get("todos"), new TypeReference<List<Todo>>() {});

        assertEquals(2, todosResult.size());
        assertEquals(todos.get(0).getText(), todosResult.get(0).getText());
        assertEquals(todos.get(1).getText(), todosResult.get(1).getText());
    }

    /**
     * Tests the markDone method of TodoController.
     */
    @Test
    public void testDoneTodo() throws Exception {
        Todo updatedTodo = todoA;
        updatedTodo.setStatus(true);
        Mockito.when(todoService.markDone(todoA.getId())).thenReturn(updatedTodo);

        mockMvc.perform(post("/api/todos/{id}/done", updatedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }

    /**
     * Tests the markUndone method of TodoController.
     */
    @Test
    public void testUndoneTodo() throws Exception {
        Todo updatedTodo = todoB;
        updatedTodo.setStatus(false);
        Mockito.when(todoService.markUndone(todoB.getId())).thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todos/{id}/undone", updatedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false));
    }

    /**
     * Tests the getMetrics method of TodoController.
     */
    @Test
    public void testTodoMetrics() throws Exception {
        Mockito.when(todoService.getMetrics()).thenReturn(metrics);

        MvcResult response = mockMvc.perform(get("/api/todos/metrics")).andReturn();

        response.getResponse();
        TodoMetrics result = objectMapper.readValue(response.getResponse().getContentAsByteArray(), TodoMetrics.class);

        assertEquals(result.getAvgTime(), metrics.getAvgTime());
        assertEquals(result.getAvgTimeLow(), metrics.getAvgTimeLow());
        assertEquals(result.getAvgTimeMedium(), metrics.getAvgTimeMedium());
        assertEquals(result.getAvgTimeHigh(), metrics.getAvgTimeHigh());
    }


}