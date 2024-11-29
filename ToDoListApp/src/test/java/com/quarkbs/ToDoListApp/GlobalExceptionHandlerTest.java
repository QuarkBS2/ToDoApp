package com.quarkbs.ToDoListApp;

import com.quarkbs.ToDoListApp.exception.GlobalExceptionHandler;
import com.quarkbs.ToDoListApp.exception.TodoNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleTodoNotFoundException() {
        TodoNotFoundException exception = new TodoNotFoundException("Todo not found");

        ResponseEntity<String> response = globalExceptionHandler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Todo not found", response.getBody());
    }
}