package model;

public class Produto {
    private int id;
    private String nome;
    private double valor;
    private int qntdEstoque;

    public Produto(int id, String nome, double valor, int qntdEstoque) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.qntdEstoque = qntdEstoque;
    }

    // método simples para baixa de estoque
    public void baixarEstoque(int id, int quantidade) {
        if (this.id == id) {
            this.qntdEstoque -= quantidade;
            if (this.qntdEstoque < 0) {
                this.qntdEstoque = 0; // evita estoque negativo
            }
            System.out.println("Baixa realizada! Novo estoque de " + this.nome + ": " + this.qntdEstoque);
        }
    }

    // método simples para repor de estoque
    public void reporEstoque(int id, int quantidade) {
        if (this.id == id) {
            this.qntdEstoque += quantidade;
            System.out.println("Reposição realizada! Novo estoque de " + this.nome + ": " + this.qntdEstoque);
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public int getQntdEstoque() { return qntdEstoque; }
    public void setQntdEstoque(int qntdEstoque) { this.qntdEstoque = qntdEstoque; }
}