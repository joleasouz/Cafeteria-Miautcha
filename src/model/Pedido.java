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
        this.data = LocalDateTime.now();
        this.status = status;
        this.itens = new ArrayList<>();
        this.valorTotal = 0;
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
}
