# ZG-Hero TODO List (Java CLI)

Aplicação de **Lista de Tarefas (TODO List)** desenvolvida em **Java**, executada via **terminal (CLI)**.  

---

## Funcionalidades

### CRUD de Tarefas
- Criar tarefa
- Listar tarefas
- Atualizar tarefa (parcial)
- Deletar tarefa

### Filtros
- Por **Status** (TODO, DOING, DONE)
- Por **Categoria**
- Por **Prioridade** (1–5)
- Por **Data exata**
- Até uma **Data limite**

---

## Regras de Negócio
- **ID autogerado**
- **Nome obrigatório**
- **Prioridade obrigatória (1–5)**
- **Status padrão = TODO** quando não informado
- **Descrição, Categoria e Data podem ser nulas**
- **Rebalanceamento automático por prioridade**
- **Data de término não pode estar no passado**
- **Update parcial** (altera apenas campos alterados)

---

## Front-end Web

Também foi desenvolvido um **front-end web** para a aplicação, responsável pela interface visual de gerenciamento das tarefas. A interface segue um modelo **Kanban**, com colunas **TODO, DOING e DONE**, permitindo **criar, visualizar, editar, filtrar e excluir tarefas**, além de organizá-las automaticamente por **prioridade** e separá-las por **workspaces**. Atualmente o front-end funciona apenas **em memória no navegador**, sem persistência de dados, servindo como **protótipo funcional da interface**. Futuramente ele será **integrado ao back-end em Java**, consumindo as funcionalidades da aplicação por meio de uma API.

### Tecnologias do Front-end
- HTML5
- CSS3
- JavaScript (Vanilla JS)

---

## Tecnologias Utilizadas
- Java
- IntelliJ IDEA
- Programação Orientada a Objetos (POO)
- Estrutura MVC simplificada

---
## Como Executar o Projeto

1. Abra o projeto.2. Aguarde o carregamento do projeto.
2. Localize a classe: app/Main.java
3. Execute
4. O menu aparecerá no terminal da IDE.


