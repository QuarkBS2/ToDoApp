package com.quarkbs.ToDoListApp.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity class representing a Todo item.
 */
public class Todo {
    /**
     * The unique identifier of the todo.
     */
    private Long id;

    /**
     * The text description of the todo.
     * Cannot be blank.
     */
    @NotBlank(message = "You can't leave the task empty!")
    private String text;

    /**
     * The status of the todo.
     * True if the todo is completed, false otherwise.
     */
    private Boolean status;

    /**
     * The due date of the todo.
     * Cannot be null.
     */
    @NotNull(message = "You can't leave the due date empty!")
    private LocalDate dueDate;

    /**
     * The priority of the todo.
     * Must be between 0 and 3.
     */
    @Max(3)
    private int priority;

    /**
     * The creation date of the todo.
     */
    private LocalDateTime creationDate;

    /**
     * The date when the todo was marked as done.
     */
    private LocalDateTime doneDate;

    /**
     * The elapsed time in seconds from creation to completion.
     */
    private Long elapsedTime;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

}

