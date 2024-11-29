// Custom Exception
package com.quarkbs.ToDoListApp.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(String message) {
        super(message);
    }
}