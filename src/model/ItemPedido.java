package model;

public class ItemPedido {
    private int id;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemPedido(int id, Produto produto, int quantidade, double precoUnitario){
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    /*---Doda
            atualiza o estoque automatiamente*/
    public void processarBaixa() {
        if (this.produto != null) {
            this.produto.baixarEstoque(this.produto.getId(), this.quantidade);
        }
    }

    public double calcularSubtotal() {
        return this.quantidade * this.precoUnitario;
    }

    public int getId(){ return id; }

    public Produto getProduto(){ return produto; }
    public void setProduto(Produto produto){
        this.produto = produto; 
    }

    public int getQuantidade(){ return quantidade; }
    public void setQuantidade(int quantidade){ 
        this.quantidade = quantidade; 
    }

    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario){ 
        this.precoUnitario = precoUnitario; 
    }
}

