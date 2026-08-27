package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Pedido{
    private int id;
    private LocalDateTime data;
    private StatusPedido status;
    private double valorTotal;

    private Cliente cliente;
    private List<ItemPedido> itens;

    public Pedido(int id, Cliente cliente, LocalDateTime data, StatusPedido status, double valorTotal){
        this.id = id;
        this.cliente = cliente;
        this.data = data;
        this.status = status;
        this.itens = new ArrayList<>();
        this.valorTotal = valorTotal;
    } 

    public void adicionarItem(ItemPedido item){
        this.itens.add(item);
        calcularTotal();
    }

    public void removerItem(ItemPedido item){
        this.itens.remove(item);
        calcularTotal();
    }

    public double calcularTotal(){
        double total = 0;
        for(ItemPedido item : itens){
            total += item.calcularSubtotal();
        }
        this.valorTotal = total;
        return this.valorTotal;
    }

    public void finalizarPedido(){};

    public int getId(){ return id; }

    public LocalDateTime getData(){ return data; }

    public StatusPedido getStatus(){ return status; }
    public void setStatus(StatusPedido status){ 
        this.status = status; 
    }

    public double getValorTotal(){ return valorTotal; }

    public Cliente getCliente(){ return cliente; }
    public void setCliente(Cliente cliente){ 
        this.cliente = cliente; 
    }

    public List<ItemPedido> getItens(){ return itens; }

}
