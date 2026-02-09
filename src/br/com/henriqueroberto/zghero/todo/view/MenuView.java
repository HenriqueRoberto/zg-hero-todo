package br.com.henriqueroberto.zghero.todo.view;

import br.com.henriqueroberto.zghero.todo.controller.TaskController;
import br.com.henriqueroberto.zghero.todo.model.Task;
import br.com.henriqueroberto.zghero.todo.model.TaskStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuView {

  private final TaskController controller;
  private final Scanner sc = new Scanner(System.in);
  private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public MenuView(TaskController controller) {
    this.controller = controller;
  }

  public void start() {
    int option;

    do {
      printMenu();
      option = readInt("Escolha uma opção: ");

      try {
        switch (option) {
          case 1 -> createTaskFlow();
          case 2 -> listMenuFlow(); // novo
          case 3 -> updateFlow();
          case 4 -> deleteFlow();
          case 0 -> System.out.println("Saindo...");
          default -> System.out.println("Opção inválida.");
        }

      } catch (IllegalArgumentException e) {
        System.out.println("Erro: " + e.getMessage());
      }
      System.out.println();

    } while (option != 0);
  }

  private void printMenu() {
    System.out.println("=== TODO LIST (CLI) ===");
    System.out.println("1 - Criar tarefa");
    System.out.println("2 - Listar");
    System.out.println("3 - Atualizar tarefa");
    System.out.println("4 - Deletar tarefa");
    System.out.println("0 - Sair");
  }

  private void listMenuFlow() {
    int opt;

    do {
      System.out.println("== LISTAR ==");
      System.out.println("1 - Todas");
      System.out.println("2 - Por status");
      System.out.println("3 - Por categoria");
      System.out.println("4 - Por prioridade");
      System.out.println("5 - Por data exata");
      System.out.println("6 - Até data");
      System.out.println("0 - Voltar");

      opt = readInt("Escolha: ");

      switch (opt) {
        case 1 -> listAllFlow();
        case 2 -> listByStatusFlow();
        case 3 -> listByCategoryFlow();
        case 4 -> listByPriorityFlow();
        case 5 -> listByEndDateExactFlow();
        case 6 -> listUntilEndDateFlow();
        case 0 -> {}
        default -> System.out.println("Opção inválida.");
      }

      System.out.println();

    } while (opt != 0);
  }



  // -------- FLOWS --------

  private void createTaskFlow() {
    System.out.println("== Criar tarefa ==");

    String name = readLine("Nome (obrigatório): ");
    String description = readOptionalLine("Descrição (opcional, enter para vazio): ");
    LocalDate endDate = readOptionalDate("Data término (dd/MM/yyyy) (opcional, enter para vazio): ");
    Integer priority = readInt("Prioridade 1-5 (obrigatório): ");
    String category = readOptionalLine("Categoria (opcional, enter para vazio): ");

    TaskStatus status = readOptionalStatus("Status (TODO/DOING/DONE) (opcional, enter = TODO): ");

    if (description != null && description.isBlank()) description = null;
    if (category != null && category.isBlank()) category = null;

    Task created = controller.createTask(
        name,
        description,
        endDate,
        priority,
        category,
        status
    );

    System.out.println("Criada: ID " + created.getId());
  }

  private void listAllFlow() {
    System.out.println("== Listar todas ==");
    printTasks(controller.listAll());
  }

  private void updateFlow() {
    System.out.println("== Atualizar tarefa (parcial) ==");

    Integer id = readInt("ID: ");
    if (controller.findById(id) == null) {
      System.out.println("ID não encontrado.");
      return;
    }

    String name = readOptionalLine("Novo nome (enter para ignorar): ");
    String description = readOptionalLine("Nova descrição (enter para ignorar): ");
    LocalDate endDate = readOptionalDate("Nova data (dd/MM/yyyy) (enter para ignorar): ");
    Integer priority = readOptionalInt("Nova prioridade 1-5 (enter para ignorar): ");
    String category = readOptionalLine("Nova categoria (enter para ignorar): ");
    TaskStatus status = readOptionalStatus("Novo status (TODO/DOING/DONE) (enter para ignorar): ");

    Task patch = new Task(null, null, null, null, null, null, null);

    // obrigatórios: só seta se vier algo
    if (!name.isBlank()) patch.setName(name);

    // opcionais: se o usuário só apertar enter, fica "" -> vamos ignorar update

    if (!description.isBlank()) patch.setDescription(description);
    if (endDate != null) patch.setEndDate(endDate);
    if (priority != null) patch.setPriority(priority);
    if (!category.isBlank()) patch.setCategory(category);
    if (status != null) patch.setStatus(status);

    Task updated = controller.update(id, patch);

    if (updated == null) {
      System.out.println("ID não encontrado.");
      return;
    }

    System.out.println("Atualizada.");
  }

  private void deleteFlow() {
    System.out.println("== Deletar tarefa ==");
    Integer id = readInt("ID: ");
    boolean ok = controller.delete(id);

    if (ok) System.out.println("Deletada.");
    else System.out.println("ID não encontrado.");
  }

  private void listByStatusFlow() {
    System.out.println("== Listar por status ==");
    TaskStatus status = readStatusRequired("Status (TODO/DOING/DONE): ");
    printTasks(controller.listByStatus(status));
  }

  private void listByCategoryFlow() {
    System.out.println("== Listar por categoria ==");
    String category = readLine("Categoria: ");
    printTasks(controller.listByCategory(category));
  }

  private void listByPriorityFlow() {
    System.out.println("== Listar por prioridade ==");
    Integer priority = readInt("Prioridade 1-5: ");
    printTasks(controller.listByPriority(priority));
  }

  private void listByEndDateExactFlow() {
    System.out.println("== Filtrar por data exata ==");
    LocalDate date = readDateRequired("Data (dd/MM/yyyy): ");
    printTasks(controller.listByEndDate(date));
  }

  private void listUntilEndDateFlow() {
    System.out.println("== Filtrar até data ==");
    LocalDate date = readDateRequired("Data (dd/MM/yyyy): ");
    printTasks(controller.listUntilEndDate(date));
  }

  // -------- HELPERS DE MENU --------

  private void printTasks(List<Task> tasks) {
    if (tasks.isEmpty()) {
      System.out.println("(vazio)");
      return;
    }
    for (Task t : tasks) {
      System.out.println(
          "ID: " + t.getId() +
              " | Name: " + t.getName() +
              " | Description: " + t.getDescription() +
              " | Priority: " + t.getPriority() +
              " | Status: " + t.getStatus() +
              " | EndDate: " + t.getEndDate() +
              " | Category: " + t.getCategory()
      );
    }
  }

  private int readInt(String label) {
    while (true) {
      System.out.print(label);
      String s = sc.nextLine();
      try {
        return Integer.parseInt(s.trim());
      } catch (NumberFormatException e) {
        System.out.println("Digite um número inteiro.");
      }
    }
  }

  private Integer readOptionalInt(String label) {
    System.out.print(label);
    String s = sc.nextLine().trim();
    if (s.isEmpty()) return null;
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      System.out.println("Valor inválido. Ignorando.");
      return null;
    }
  }

  private String readLine(String label) {
    while (true) {
      System.out.print(label);
      String s = sc.nextLine();
      if (!s.isBlank()) return s;
      System.out.println("Campo obrigatório.");
    }
  }

  private String readOptionalLine(String label) {
    System.out.print(label);
    return sc.nextLine();
  }

  private LocalDate readDateRequired(String label) {
    while (true) {
      System.out.print(label);
      String s = sc.nextLine().trim();
      try {
        return LocalDate.parse(s, fmt);
      } catch (DateTimeParseException e) {
        System.out.println("Data inválida. Use dd/MM/yyyy.");
      }
    }
  }

  private LocalDate readOptionalDate(String label) {
    System.out.print(label);
    String s = sc.nextLine().trim();
    if (s.isEmpty()) return null;

    try {
      return LocalDate.parse(s, fmt);
    } catch (DateTimeParseException e) {
      System.out.println("Data inválida. Ignorando.");
      return null;
    }
  }

  private TaskStatus readStatusRequired(String label) {
    while (true) {
      System.out.print(label);
      String s = sc.nextLine().trim().toUpperCase();
      try {
        return TaskStatus.valueOf(s);
      } catch (IllegalArgumentException e) {
        System.out.println("Status inválido. Use TODO, DOING ou DONE.");
      }
    }
  }

  private TaskStatus readOptionalStatus(String label) {
    System.out.print(label);
    String s = sc.nextLine().trim();
    if (s.isEmpty()) return null;

    try {
      return TaskStatus.valueOf(s.toUpperCase());
    } catch (IllegalArgumentException e) {
      System.out.println("Status inválido. Ignorando.");
      return null;
    }
  }
}
