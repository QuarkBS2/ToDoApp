package com.quarkbs.ToDoListApp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Todo {
    private Long id;
    @NotNull
    @NotEmpty(message="You can't leave the task empty!")
    private String text;
    private Boolean status;
    private LocalDate dueDate;
    @Max(3)
    private int priority;
    private LocalDateTime creationDate;
    private LocalDateTime doneDate;
    private Long elapsedTime;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public Boolean getStatus(){
        return status;
    }

    public void setStatus(Boolean status){
        this.status = status;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }

    public void setDueDate(LocalDate duedaDate){
        this.dueDate = duedaDate;
    }

    public int getPriority(){
        return priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public LocalDateTime getCreationDate(){
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate){
        this.creationDate = creationDate;
    }

    public LocalDateTime getDoneDate(){
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate){
        this.doneDate = doneDate;
    }

    public Long getElapsedTime(){
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime){
        this.elapsedTime = elapsedTime;
    }

}

