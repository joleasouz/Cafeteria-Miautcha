import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import view.Formulario;
import view.PainelClientes;
import view.PainelEstoque;

public class Principal extends JFrame {
    public Principal() {
       setTitle("Miaucha");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();

        Formulario aba1 = new Formulario();
        PainelEstoque aba2 = new PainelEstoque();
        PainelClientes aba3 = new PainelClientes();
        abas.addTab("Formulario", aba1);
        abas.addTab("Estoque", aba2);
         abas.addTab("Clientes", aba3);

        add(abas);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Principal();
    }
}
