package model;

public class ItemPedido {
    private int id;
    private String nome;
    private int quantidade;
    private double precoUnitario;

    public ItemPedido(int id, String nome, int quantidade, double precoUnitario){
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public double calcularSubtotal() {
        return this.quantidade * this.precoUnitario;
    }

    public int getId(){
        return id; 
    }
    public void setId(int id){
        this.id = id; 
    }

    public String getNome(){ 
        return nome; 
    }
    public void setNome(String nome){
        this.nome = nome; 
    }

    public int getQuantidade(){ 
        return quantidade; 
    }
    public void setQuantidade(int quantidade){ 
        this.quantidade = quantidade; 
    }

    public double getPrecoUnitario() { 
        return precoUnitario; 
    }
    public void setPrecoUnitario(double precoUnitario){ 
        this.precoUnitario = precoUnitario; 
    }
}

