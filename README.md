# Sistema de Gestão de Biblioteca (Projeto)

Projeto desenvolvido no âmbito de uma UC do Instituto Superior Técnico, seguindo uma abordagem incremental com três fases: **modelação, implementação parcial e implementação final**.

---

## Descrição

Este projeto consiste no desenvolvimento de um sistema de gestão de biblioteca, permitindo gerir utentes, obras e operações associadas (consultas, registos e interações).

A aplicação está organizada em duas camadas principais:
- **Core** -> lógica de negócio  
- **App** -> interação com o utilizador  

---

## Fases do Projeto

### 1ª Entrega — Modelação (Diagrama UML)

Nesta fase foi desenvolvido o **diagrama de classes UML do domínio da aplicação**, incluindo:

- Identificação das entidades principais (Library, Utente, Obra, etc.)  
- Definição das relações entre classes  
- Estrutura da lógica de negócio (core)  
- Exclusão da camada de interface (menus/comandos)  

- Objetivo: definir corretamente a arquitetura antes da implementação  

---

### 2ª Entrega — Implementação Intermédia

Nesta fase foi implementada a funcionalidade base do sistema, incluindo:

#### Funcionalidades implementadas
- Leitura e interpretação de ficheiros (`import`)  
- Persistência de dados:
  - Abrir estado  
  - Guardar estado  
- Gestão da data:
  - Mostrar data atual  
  - Avançar data  

#### Gestão de Utentes
- Registar utente  
- Mostrar utente  
- Listar utentes  

#### Gestão de Obras
- Mostrar obra  
- Listar obras  
- Listar obras por criador  

#### Outros
- Estrutura completa de menus (restantes comandos definidos, mesmo que não implementados)  
- Implementação parcial do domínio (core) suficiente para suportar funcionalidades  

- Objetivo: garantir funcionamento mínimo do sistema e validar arquitetura  

---

### 3ª Entrega — Projeto Final

Entrega final com implementação completa do sistema:

#### Funcionalidades finais
- Implementação total dos comandos do sistema  
- Gestão completa de utentes, obras e operações  
- Tratamento de notificações  
- Sistema de classificações de utentes  
- Gestão de erros e validações  

#### Qualidade do software
- Aplicação de princípios de desenho:
  - Open/Closed Principle  
  - Programação para o supertipo  
- Separação clara entre camadas (core vs app)  
- Uso adequado de estruturas de dados  
- Tratamento de exceções  
- Serialização  

- Objetivo: sistema completo, robusto e bem estruturado  

---

## Funcionalidades

- Gestão de utentes  
- Gestão de obras  
- Sistema de notificações  
- Persistência de dados  
- Interface baseada em menus  
- Separação arquitetura (core/app)  

---

## Estrutura do Projeto

- `uml/` -> diagramas UML  
- `src/` -> código fonte  
- `core/` -> lógica de negócio  
- `app/` -> interface com o utilizador  
- `README.md` -> documentação  

---

## Como executar

Abrir na pasta onde estiver o (*bci*) e executar o seguinte comando:

- ``java -cp po-uilib.jar:. bci.app.App``
