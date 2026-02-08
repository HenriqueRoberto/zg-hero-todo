package br.com.henriqueroberto.zghero.todo.model;

import java.time.LocalDate;

public class Task {
  //atributos

  private Integer id;
  private String name;
  private String description;
  private LocalDate endDate;
  private Integer priority;
  private String category;
  private TaskStatus status;

  // construtores
  public Task(){
  }

  public Task(Integer id, String name, String description, LocalDate  endDate, Integer priority, String category, TaskStatus status){
    this.id = id;
    this.name = name;
    this.description = description;
    this.endDate = endDate;
    this.priority = priority;
    this.category = category;
    this.status = status;
  }

  //getters e setters
  public Integer getId(){
    return id;
  }

  public void setId(Integer id){
    this.id = id;
  }

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  public String getDescription(){
    return description;
  }

  public void setDescription(String description){
    this.description = description;
  }

  public LocalDate getEndDate(){
    return endDate;
  }

  public void setEndDate(LocalDate endDate){
    this.endDate = endDate;
  }

  public Integer getPriority(){
    return priority;
  }

  public void setPriority(Integer priority){
    this.priority = priority;
  }

  public String getCategory(){
    return category;
  }

  public void setCategory(String category){
    this.category = category;
  }

  public TaskStatus getStatus(){
    return status;
  }

  public void setStatus(TaskStatus status){
    this.status = status;
  }

}
