import data.RepositorioMock;
import model.Cliente;
import model.Produto;
import view.Interface;

public class Main {
    public static void main(String[] args) {
        RepoCdD.clientes.add(new Cliente(1, "Ana Silva", "111.111.111-11", "ana@email.com", "11999990000"));
        RepoCdD.clientes.add(new Cliente(2, "Bruno Costa", "222.222.222-22", "bruno@email.com", "11988880000"));

        RepoCdD.produtos.add(new Produto(1, "Café Expresso", 6.50, 50));
        RepoCdD.produtos.add(new Produto(2, "Pão de Queijo", 4.00, 30));

        new Interface().setVisible(true);
    }
}
