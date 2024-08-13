package com.quarkbs.ToDoListApp;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quarkbs.ToDoListApp.controller.TodoController;
import com.quarkbs.ToDoListApp.entity.Todo;
import com.quarkbs.ToDoListApp.entity.TodoMetrics;
import com.quarkbs.ToDoListApp.service.TodoService;

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
    private TodoMetrics metrics;

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

        metrics = new TodoMetrics();
        metrics.setAvgTime(120L);
        metrics.setAvgTimeHigh(120L);
        metrics.setAvgTimeMedium(60L);
        metrics.setAvgTimeLow(180L);
    }

    @Test
    public void testAddTodo() throws Exception{
        Mockito.when(todoService.addTodo(todoA)).thenReturn(todoA);

        ResultActions response = mockMvc.perform(post("/api/todos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(todoA))
                                );

        response
        .andDo(print())
        .andExpect(jsonPath("$.text").value("TEST FOR TODO A"));

    }

    @Test
    public void testGetAllTodos() throws Exception{
        List<Todo> todos = Arrays.asList(todoA, todoB);
        Mockito.when(todoService.getAllTodos(anyInt(), anyInt(), anyString(), anyString(), eq(null), eq(null), eq(null))).thenReturn(todos);

        MvcResult response = mockMvc.perform(get("/api/todos")).andReturn();

        response.getResponse();
        List<Todo> result = objectMapper.readValue(response.getResponse().getContentAsByteArray(), new TypeReference<List<Todo>>(){});

        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getText(), todos.get(0).getText());
        assertEquals(result.get(1).getText(), todos.get(1).getText());

    }

    @Test
    public void testDoneTodo() throws Exception{
        Todo updatedTodo = todoA;
        updatedTodo.setStatus(true);
        Mockito.when(todoService.markDone(todoA.getId())).thenReturn(updatedTodo);

        ResultActions response = mockMvc.perform(post("/api/todos/{id}/done", updatedTodo.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedTodo))
                                );

        response
        .andDo(print())
        .andExpect(jsonPath("$.status").value(true));

    }

    @Test
    public void testUndoneTodo() throws Exception{
        Todo updatedTodo = todoB;
        updatedTodo.setStatus(false);
        Mockito.when(todoService.markUndone(todoB.getId())).thenReturn(updatedTodo);

        ResultActions response = mockMvc.perform(put("/api/todos/{id}/undone", updatedTodo.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedTodo))
                                );

        response
        .andDo(print())
        .andExpect(jsonPath("$.status").value(false));

    }

    @Test
    public void testTodoMetrics() throws Exception{
        TodoMetrics todoMetrics = metrics;
        Mockito.when(todoService.getMetrics()).thenReturn(todoMetrics);

        MvcResult response = mockMvc.perform(get("/api/todos/metrics")).andReturn();

        response.getResponse();
        TodoMetrics result = objectMapper.readValue(response.getResponse().getContentAsByteArray(), new TypeReference<TodoMetrics>(){});

        assertEquals(result.getAvgTime(), todoMetrics.getAvgTime());
        assertEquals(result.getAvgTimeLow(), todoMetrics.getAvgTimeLow());
        assertEquals(result.getAvgTimeMedium(), todoMetrics.getAvgTimeMedium());
        assertEquals(result.getAvgTimeHigh(), todoMetrics.getAvgTimeHigh());
    }


}