package com.quarkbs.ToDoListApp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Todo entity.
 */
public class TodoDTO {
    private String text;
    private Boolean status;
    private LocalDate dueDate;
    private int priority;
    private LocalDateTime creationDate;
    private LocalDateTime doneDate;

    /**
     * Gets the text of the todo.
     *
     * @return the text of the todo
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the todo.
     *
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the status of the todo.
     *
     * @return the status of the todo
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * Sets the status of the todo.
     *
     * @param status the status to set
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * Gets the due date of the todo.
     *
     * @return the due date of the todo
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date of the todo.
     *
     * @param dueDate the due date to set
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Gets the priority of the todo.
     *
     * @return the priority of the todo
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority of the todo.
     *
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the creation date of the todo.
     *
     * @return the creation date of the todo
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the todo.
     *
     * @param creationDate the creation date to set
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the done date of the todo.
     *
     * @return the done date of the todo
     */
    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    /**
     * Sets the done date of the todo.
     *
     * @param doneDate the done date to set
     */
    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }
}