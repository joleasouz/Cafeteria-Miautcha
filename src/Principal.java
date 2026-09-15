import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import view.Formulario;
import view.Interface;
import view.PainelClientes;
import view.PainelEstoque;
import view.PainelComandas;

public class Principal extends JFrame {
    public Principal() {
       setTitle("Miautcha");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));

        JTabbedPane abas = new JTabbedPane();

        abas.setBackground(Interface.COR_FUNDO_PAINEL);
        abas.setForeground(new Color(50, 50, 50));
        abas.setBorder(null);

        Formulario aba1 = new Formulario();
        PainelEstoque aba2 = new PainelEstoque();
        PainelClientes aba3 = new PainelClientes();
        PainelComandas aba4 = new PainelComandas();
        
        abas.addTab("Formulario", aba1);
        abas.addTab("Estoque", aba2);
        abas.addTab("Clientes", aba3);
        abas.addTab("Comandas", aba4);


        add(abas);
        setVisible(true);

    }

    public static void main(String[] args) {
        new Principal();
    }
}
