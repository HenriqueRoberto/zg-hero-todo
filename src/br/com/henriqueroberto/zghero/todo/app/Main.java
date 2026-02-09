package br.com.henriqueroberto.zghero.todo.app;

import br.com.henriqueroberto.zghero.todo.controller.TaskController;
import br.com.henriqueroberto.zghero.todo.view.MenuView;

public class Main {
  public static void main(String[] args) {
    TaskController controller = new TaskController();
    MenuView menu = new MenuView(controller);
    menu.start();
  }
}
