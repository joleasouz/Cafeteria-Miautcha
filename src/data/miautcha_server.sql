CREATE DATABASE IF NOT EXISTS miautcha_server;
USE miautcha_server;

CREATE TABLE IF NOT EXISTS clientes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100), cpf VARCHAR(14), email VARCHAR(100), telefone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS cargo (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome_cargo VARCHAR(50), descricao VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS funcionarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_cargo INT, nome VARCHAR(100), cpf VARCHAR(14),
  FOREIGN KEY (id_cargo) REFERENCES cargo(id)
);

CREATE TABLE IF NOT EXISTS produtos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome_produto VARCHAR(100), categoria VARCHAR(50),
  quantidade_estoque INT, preco DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS pedidos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_cliente INT, id_funcionario INT,
  data_pedido DATETIME, status VARCHAR(30), valor_total DECIMAL(10,2),
  FOREIGN KEY (id_cliente) REFERENCES clientes(id),
  FOREIGN KEY (id_funcionario) REFERENCES funcionarios(id)
);

CREATE TABLE IF NOT EXISTS item_pedidos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_produtos INT, id_pedidos INT,
  quantidade INT, valor_subtotal DECIMAL(10,2),
  FOREIGN KEY (id_produtos) REFERENCES produtos(id),
  FOREIGN KEY (id_pedidos) REFERENCES pedidos(id)
);