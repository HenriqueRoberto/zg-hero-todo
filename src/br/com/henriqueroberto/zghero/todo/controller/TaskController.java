package br.com.henriqueroberto.zghero.todo.controller;

import br.com.henriqueroberto.zghero.todo.model.Task;
import br.com.henriqueroberto.zghero.todo.model.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskController {
  private List<Task> listTasks = new ArrayList<>();
  private int nextId = 1;

  //CRUD

  //Create
  public Task createTask(String name, String description, LocalDate endDate, Integer priority, String category, TaskStatus status){

    validateRequired(name, priority);
    validatePriority(priority);

    if (status == null) status = TaskStatus.TODO;

    Task task = new Task(null, name, description, endDate, priority, category,status);
    task.setId(nextId);
    nextId ++;
    insertByPriority(task);

    return task;
  }

  //Read
   public List<Task> listAll(){
    return listTasks;
   }

   public Task findById(Integer id){
    if (id == null) {
      return null;
    }
    for (Task task : listTasks){
      if(task.getId() != null && task.getId().equals(id)){
        return task;
      }
    }
     return null;
   }

   //Update
   public Task update(Integer id, Task updateTask) {
     if (id == null || updateTask == null) return null;

     Task task = findById(id);
     if (task == null) return null;

     boolean priorityChanged = false;

     // NAME (não pode ser null)
     if (updateTask.getName() != null &&
         !updateTask.getName().equals(task.getName())) {
       task.setName(updateTask.getName());
     }

     // DESCRIPTION (pode ser null)
     if (!Objects.equals(updateTask.getDescription(), task.getDescription())) {
       task.setDescription(updateTask.getDescription());
     }

     // END DATE (pode ser null)
     if (!Objects.equals(updateTask.getEndDate(), task.getEndDate())) {
       task.setEndDate(updateTask.getEndDate());
     }

     // PRIORITY (não pode ser null)
     if (updateTask.getPriority() != null &&
         !updateTask.getPriority().equals(task.getPriority())) {
       validatePriority(updateTask.getPriority());
       task.setPriority(updateTask.getPriority());
       priorityChanged = true;
     }

     // CATEGORY (pode ser null)
     if (!Objects.equals(updateTask.getCategory(), task.getCategory())) {
       task.setCategory(updateTask.getCategory());
     }

     // STATUS ( pode ser null e vira TODO)
     if (!Objects.equals(updateTask.getStatus(), task.getStatus())) {
       if (updateTask.getStatus() == null) {
         task.setStatus(TaskStatus.TODO);
       } else {
         task.setStatus(updateTask.getStatus());
       }
     }

     if (priorityChanged) {
       listTasks.remove(task);
       insertByPriority(task);
     }

     return task;
   }



  //Delete
  public boolean delete(Integer id){
    Task task = findById(id);

    if (task == null) {
      return false;
    }
    return listTasks.remove(task);
  }

  //Filters
  public List<Task> listByStatus(TaskStatus status) {
    List<Task> result = new ArrayList<>();
    if (status == null) status = TaskStatus.TODO;


    for (Task task : listTasks) {
      if (status.equals(task.getStatus())) result.add(task);
    }
    return result;
  }

  public List<Task> listByCategory(String category) {
    List<Task> result = new ArrayList<>();
    if (category == null) return result;

    for (Task task : listTasks) {
      if (category.equals(task.getCategory())) result.add(task);
    }
    return result;
  }

  public List<Task> listByPriority(Integer priority) {
    List<Task> result = new ArrayList<>();
    if (priority == null) return result;

    for (Task task : listTasks) {
      if (priority.equals(task.getPriority())) result.add(task);
    }
    return result;
  }

  //Filters date
  public List<Task> listByEndDate(LocalDate date) {
    List<Task> result = new ArrayList<>();

    for (Task task : listTasks) {
      if (task.getEndDate() != null && task.getEndDate().equals(date)) {
        result.add(task);
      }
    }
    return result;
  }

  public List<Task> listUntilEndDate(LocalDate date) {
    List<Task> result = new ArrayList<>();

    for (Task task : listTasks) {
      if (task.getEndDate() != null && !task.getEndDate().isAfter(date)) {
        result.add(task);
      }
    }
    return result;
  }


  //Helpers

  // Insere a tarefa na posição correta, mantendo a lista ordenada por prioridade.
  // Regra: 1 = maior prioridade (vem antes), 5 = menor (vem depois).
  private void insertByPriority(Task task) {
    for (int i = 0; i < listTasks.size(); i++) {
      Task current = listTasks.get(i);

      Integer currentPriority = current.getPriority();
      Integer newPriority = task.getPriority();

      // Se a tarefa atual não tem prioridade (não deveria acontecer), coloca antes dela.
      if (currentPriority == null || newPriority <= currentPriority) {
        listTasks.add(i, task);
        return;
      }
    }
    // se não achou posição, entra no final
    listTasks.add(task);
  }

  private void validatePriority(Integer priority) {
    if (priority == null || priority < 1 || priority > 5) {
      throw new IllegalArgumentException("Priority must be between 1 and 5.");
    }
  }


  private void validateRequired(String name, Integer priority) {

    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name is required.");
    }

    if (priority == null) {
      throw new IllegalArgumentException("Priority is required.");
    }

  }


}
