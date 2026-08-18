# Cafeteria-Miautcha — Sistema de Gestão para Cafeteria

> **Projeto Integrador em Java & Swing**  
> Sistema desktop focado em otimizar o atendimento no balcão, controle de estoque, comanda digital e histórico de clientes da **Cafeteria Miautcha**.

---

## 📌 Sumário
- [Sobre o Projeto](#-sobre-o-projeto)
- [Problemas Identificados e Soluções](#-problemas-identificados-e-soluções)
- [Arquitetura e Módulos do Sistema](#-arquitetura-e-módulos-do-sistema)
- [Divisão de Responsabilidades (Grupo de 4)](#-divisão-de-responsabilidades-grupo-de-4)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Como Executar o Projeto](#-como-executar-o-projeto)
- [Estrutura de Pastas](#-estrutura-de-pastas)

---

## Sobre o Projeto

O **Miautcha System** é uma aplicação desktop desenvolvida em Java com interface gráfica em Swing. O projeto foi projetado para resolver falhas operacionais recorrentes do balcão de uma cafeteria, trazendo automação no cálculo dos pedidos, alertas visuais para falta de estoque e consultas rápidas ao histórico de compras dos clientes.

---

## Problemas Identificados e Soluções

| Problema Mapeado | Solução Implementada |
| :--- | :--- |
| **Pedidos duplicados ou perdidos no balcão** | Implementação de **Comanda Digital** e registro persistente de dados do pedido (ID, atendente, valor, itens, data/hora). |
| **Dificuldade de cadastro e histórico de clientes** | **Barra de pesquisa por Nome e CPF** com visualização detalhada do histórico de compras ao clicar no cliente. |
| **Erros frequentes no cálculo do valor total** | **Formulário de Pedido com soma automática** e atualização do total em tempo real conforme novos itens são adicionados. |
| **Falta de controle de estoque e insumos** | **Painel de alertas dinâmico** com sinalização visual de estoque crítico e esgotado (*warnings* pop-up). |
| **Perda de dados de funcionários** | Módulo de **armazenamento dos dados dos funcionários** para vínculo de cada atendimento. |

---

## Arquitetura e Módulos do Sistema

O sistema foi estruturado com base no modelo orientado a objetos (*DiagramaGatocafe*), priorizando o desacoplamento de responsabilidades:

Miautcha/
│
├── src/
│       └── cafebao/
│           │
│           ├── model/                     # Módulos de Dados (Diagrama)
│           │   ├── Cliente.java           # [Vitoria Brenda] id, nome, cpf, email, telefone
│           │   ├── Produto.java           # [Maria Eduarda] id, nomeProduto, categoria, preco, quantidadeEmEstoque
│           │   ├── Pedido.java            # [Julia Silva] id, data, status, valorTotal
│           │   ├── ItemPedido.java        # [Julia Silva] id, quantidade, precoUnitario
│           │   ├── Comanda.java           # [Larissa Peters] id, mesa, status
│           │   └── StatusPedido.java      # [Larissa Peters] Enum (ex: PENDENTE, FINALIZADO, CANCELADO)
│           │
│           ├── data/                      # Banco em Memória
│           │   ├── BancoDados.java        # **Listas estáticas para compartilhar dados**
│           │   ├── ClienteDAO.java        #
│           │
│           ├── view/                      # Interfaces Gráficas (Swing)
│           │   ├── Interface.java         # **JFrame principal com JTabbedPane (Abas)**
│           │   ├── Formulario.java        # [Julia Silva] Tela/Painel de Novo Pedido / Balcão
│           │   ├── PainelClientes.java    # [Vitoria Brenda] Tela/Painel de busca e histórico de Clientes
│           │   ├── PainelEstoque.java     # [Maria Eduarda] Tela/Painel do Cardápio e Alertas de Estoque
│           │   └── PainelComandas.java    # [Larissa Peters] Painel com os cards/botões de Comanda Digital
│           │
│           └── Main.java                  # **Ponto de entrada, carrega Mocks e abre a Interface**
│
├── bin/                                   # Arquivos compilados (.class)
└── README.md                              # Documentação do projeto

---

## Divisão de Responsabilidades (Grupo de 4)

Para garantir um desenvolvimento paralelo, fluido e sem conflitos de código no Git, as funções foram divididas por responsabilidade funcional:

### Vitoria Brenda — Módulo de Clientes e Histórico
* **Classe:** `Cliente.java`
* **Métodos Principais:** `consultarHistorico(): List<Pedido>`
* **Interface Swing:** Painel de busca por Nome/CPF e janela pop-up de histórico de pedidos.

### Maria Eduarda — Módulo de Produtos e Gestão de Estoque
* **Classe:** `Produto.java`
* **Métodos Principais:** `estocar()`, `verificarEstoque()`, `atualizarPreco()`
* **Interface Swing:** Painel do cardápio com **alertas visuais dinâmicos** para produtos zerados ou em nível crítico.

### Julia Silva — Módulo de Pedidos e Lógica do Caixa (`Formulario.java`)
* **Classes:** `Pedido.java`, `ItemPedido.java`
* **Métodos Principais:** `adicionarItem()`, `calcularSubtotal()`, `calcularTotal()`, `finalizarPedido()`
* **Interface Swing:** Screen de lançamento de novos pedidos com atualização instantânea do valor total.

### Larissa Peters — Arquitetura Central, Comanda e Navegação (`Interface.java`)
* **Classes:** `Comanda.java`, `BancoDados.java` (Repositório em memória), `Main.java`
* **Métodos Principais:** `Comanda.expandir()`, `Comanda.minimizar()`
* **Interface Swing:** Janela principal (`JFrame`) utilizando `JTabbedPane` para unificar as telas de todos os integrantes em abas, além do cadastro inicial de dados de teste (*mock data*).

---

## Tecnologias Utilizadas

* **Linguagem:** Java (JDK 17 ou superior)
* **GUI / Interface Gráfica:** Java Swing / AWT
* **Paradigma:** Orientação a Objetos (POO)
* **Armazenamento:** Repositório em Memória (`List<T>`)
* **Banco de Dados:** MySQL 8.0+
* **Conectividade:** JDBC (Java Database Connectivity)

---

