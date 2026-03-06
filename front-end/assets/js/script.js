let filters = {
  priority: null,
  category: null,
  status: null,
};

// DROPDOWN
const dropdownButtons = document.querySelectorAll(".btn-dropdown");
const dropdownMenus = document.querySelectorAll(".dropdown-menu");

dropdownButtons.forEach((btn) => {
  btn.addEventListener("click", () => {
    dropdownMenus.forEach((menu) => {
      menu.classList.remove("active-dropdown");
    });
    btn.nextElementSibling.classList.add("active-dropdown");
  });
});

document.addEventListener("click", (e) => {
  if (!e.target.closest(".dropdown")) {
    dropdownMenus.forEach((menu) => {
      menu.classList.remove("active-dropdown");
    });
  }
});

// FILTERS
//prettier-ignore
document.querySelectorAll(".dropdown-menu")[1].querySelectorAll(".dropdown-item").forEach((item) => {
  item.addEventListener("click", (e) => {
    e.preventDefault();

    const value = item.textContent.trim();

    if (filters.priority == value) {
      filters.priority = null;
    } else {
      filters.priority = value;
    }

    renderTasks();
  });
});

//POPULATE CATEGORIES FILTER
function updateCategoryDropdown() {
  const menu = document.querySelectorAll(".dropdown-menu")[0];
  menu.innerHTML = "";

  const categories = [
    ...new Set(
      workspaces[currentWorkspace].map((t) => t.category).filter(Boolean),
    ),
  ];

  categories.forEach((cat) => {
    const li = document.createElement("li");
    const a = document.createElement("a");

    a.classList.add("dropdown-item");
    a.href = "#";
    a.textContent = cat;

    a.addEventListener("click", (e) => {
      e.preventDefault();

      if (filters.category === cat) {
        filters.category = null;
      } else {
        filters.category = cat;
      }

      renderTasks();
    });

    li.appendChild(a);
    menu.appendChild(li);
  });
}

// ASIDE (WORKSPACES)

const btnAddAside = document.getElementById("btn-add-aside");
const workspaceArea = document.getElementById("workspace-area");

let workspaces = {};
let currentWorkspace = null;

// CRIAR WORKSPACE
function createWorkspace() {
  const div = document.createElement("div");
  div.classList.add("workspace-area-group");

  const input = document.createElement("input");
  input.type = "text";
  input.value = "Workspace";
  input.placeholder = "Nova área de trabalho";
  input.maxLength = 20;
  input.classList.add("workspace-task");
  input.readOnly = true;

  const span = document.createElement("span");
  span.textContent = "|";

  div.appendChild(input);
  div.appendChild(span);

  workspaceArea.appendChild(div);

  const workspaceId = Date.now().toString();

  workspaces[workspaceId] = [];

  addWorkspaceEvents(div, workspaceId);

  if (!currentWorkspace) {
    selectWorkspace(workspaceId);
  }
}

// CRIAR AREA DE TASKS

// SELECIONAR WORKSPACE
function selectWorkspace(id) {
  currentWorkspace = id;

  renderTasks();
}

// EVENTOS DO WORKSPACE
function addWorkspaceEvents(element, id) {
  const input = element.querySelector("input");

  element.addEventListener("click", () => {
    selectWorkspace(id);
  });

  element.addEventListener("dblclick", () => {
    input.readOnly = false;
    input.focus();
  });

  input.addEventListener("blur", () => {
    input.readOnly = true;
  });

  input.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      input.blur();
    }
  });
}

// BOTÃO CRIAR WORKSPACE
btnAddAside.addEventListener("click", createWorkspace);

// CRIA PRIMEIRO WORKSPACE
createWorkspace();

//OPEN MODAL CREATE TASK
const btnCreateTask = document.getElementById("btn-create-task");
const overlayCreateTask = document.getElementById("create-overlay");
const modalCreateTask = document.getElementById("create-task");

btnCreateTask.addEventListener("click", () => {
  overlayCreateTask.classList.add("active-display-flex");
  modalCreateTask.classList.add("active-display-flex");
});

overlayCreateTask.addEventListener("click", (e) => {
  if (!modalCreateTask.contains(e.target)) {
    overlayCreateTask.classList.remove("active-display-flex");
    modalCreateTask.classList.remove("active-display-flex");
  }
});

//CREATE TASK
function createTaskCard(task) {
  const card = buildCard();

  fillCardData(card, task);

  applyStatusColor(card, task.status);

  attachCardEvents(card, task);

  return card;
}

function buildCard() {
  const template = document.getElementById("task-template");
  const card = template.cloneNode(true);

  card.removeAttribute("id");
  card.classList.remove("task-template");
  card.style.display = "block";

  return card;
}

function fillCardData(card, task) {
  card.querySelector(".task-title").textContent = task.name;

  card.querySelector(".task-date").innerHTML = task.deadline
    ? `📅 ${task.deadline}`
    : `📅`;

  card.querySelector(".task-priority").textContent = task.priority;
}

function applyStatusColor(card, status) {
  const header = card.querySelector(".task-card-header");

  if (status === "todo") header.style.background = "#0b3c53";
  if (status === "doing") header.style.background = "#d4aa28";
  if (status === "done") header.style.background = "#2e8b57";
}

function attachCardEvents(card, task) {
  card.addEventListener("click", () => {
    if (deleteMode) {
      const confirmDelete = confirm("Deseja excluir esta tarefa?");

      if (confirmDelete) {
        deleteTask(task.id);
      }

      return;
    }

    openTask(task.id);
  });
}

//GET DATA TASK
const form = document.getElementById("task-form");

form.addEventListener("submit", (e) => {
  e.preventDefault();

  const task = {
    id: Date.now(),
    name: document.getElementById("task-name").value,
    description: document.getElementById("description").value,
    deadline: document.getElementById("task-deadline").value,
    priority: Number(document.getElementById("task-priority").value),
    category: document.getElementById("task-category").value,
    status: document.getElementById("task-status").value,
  };

  saveTask(task);
  renderTasks();
  form.reset();

  // FECHAR MODAL
  overlayCreateTask.classList.remove("active-display-flex");
  modalCreateTask.classList.remove("active-display-flex");
});

function getTasks() {
  return workspaces[currentWorkspace] || [];
}

function saveTask(task) {
  if (!currentWorkspace) return;

  workspaces[currentWorkspace].push(task);
}

// RENDER TASKS
function renderTasks() {
  if (!currentWorkspace) return;

  const todo = document.getElementById("todo");
  const doing = document.getElementById("doing");
  const done = document.getElementById("done");

  todo.innerHTML = "";
  doing.innerHTML = "";
  done.innerHTML = "";

  let tasks = getTasks();

  // FILTROS
  if (filters.priority) {
    tasks = tasks.filter((t) => t.priority == filters.priority);
  }

  if (filters.category) {
    tasks = tasks.filter((t) => t.category === filters.category);
  }

  if (filters.status) {
    tasks = tasks.filter((t) => t.status === filters.status);
  }

  // ordenar por prioridade
  tasks.sort((a, b) => a.priority - b.priority);

  tasks.forEach((task) => {
    const card = createTaskCard(task);

    if (task.status === "todo") {
      todo.appendChild(card);
    }

    if (task.status === "doing") {
      doing.appendChild(card);
    }

    if (task.status === "done") {
      done.appendChild(card);
    }
  });
  updateCategoryDropdown();
}

//FILTER STATUS
//prettier-ignore
document.querySelectorAll(".dropdown-menu")[2]
  .querySelectorAll(".dropdown-item")
  .forEach((item) => {
    item.addEventListener("click", (e) => {
      e.preventDefault();

      const status = item.textContent.toLowerCase();

      if (filters.status === status) {
        filters.status = null;
        resetColumns();
      } else {
        filters.status = status;
        expandColumn(status);
      }

      renderTasks();
    });
  });

function expandColumn(status) {
  const columns = document.querySelectorAll(".column-task");

  columns.forEach((col) => {
    col.style.display = "none";
  });

  const column = document.querySelector("." + status);

  column.style.display = "flex";
  column.style.width = "100%";

  const board = column.querySelector(".task-board");
  board.style.display = "flex";
  board.style.flexWrap = "wrap";
  board.style.gap = "1rem";

  board.classList.add("expanded-board");
}

function resetColumns() {
  const columns = document.querySelectorAll(".column-task");

  columns.forEach((col) => {
    col.style.display = "flex";
    col.style.width = "30%";

    const board = col.querySelector(".task-board");
    board.style.display = "block";
    board.classList.remove("expanded-board");
  });
}

// BUTTON DELETE TASK

const btnTrash = document.getElementById("btn-trash");
let deleteMode = false;

btnTrash.addEventListener("click", () => {
  deleteMode = !deleteMode;

  document.body.classList.toggle("delete-mode");
});

// DELETE TAKS
function deleteTask(id) {
  workspaces[currentWorkspace] = workspaces[currentWorkspace].filter(
    (t) => t.id !== id,
  );

  renderTasks();
}

//OPEN TASK
function openTask(id) {
  const task = workspaces[currentWorkspace].find((t) => t.id == id);

  const overlay = document.getElementById("view-overlay");
  const modal = document.getElementById("view-task-modal");

  document.getElementById("view-title").value = task.name;

  document.getElementById("view-description").value = task.description || "";

  document.getElementById("view-deadline").value = task.deadline || "";

  document.getElementById("view-priority").value = task.priority;

  document.getElementById("view-category").value = task.category || "";

  document.getElementById("view-status").value = task.status;

  const header = modal.querySelector(".task-card-header");

  if (task.status === "todo") header.style.background = "#0b3c53";
  if (task.status === "doing") header.style.background = "#d4aa28";
  if (task.status === "done") header.style.background = "#2e8b57";

  overlay.style.display = "flex";

  modal.dataset.id = id;
}

//FECHAR MODAL VIEW TASK

const overlayView = document.getElementById("view-overlay");

overlayView.addEventListener("click", (e) => {
  if (e.target === overlayView) {
    overlayView.style.display = "none";
  }
});

//SALVAR EDIÇÕES MODAL VIEW TASK
const btnSave = document.getElementById("save-task");

btnSave.addEventListener("click", () => {
  const id = document.getElementById("view-task-modal").dataset.id;

  const task = workspaces[currentWorkspace].find((t) => t.id == id);

  task.description = document.getElementById("view-description").value;

  task.deadline = document.getElementById("view-deadline").value;

  task.priority = Number(document.getElementById("view-priority").value);

  task.category = document.getElementById("view-category").value;

  task.status = document.getElementById("view-status").value;

  renderTasks();

  document.getElementById("view-overlay").style.display = "none";
});
