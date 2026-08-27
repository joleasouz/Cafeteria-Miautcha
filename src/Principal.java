import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import view.Formulario;
import view.PainelEstoque;

public class Principal extends JFrame {
    public Principal() {
       setTitle("Miaucha");
        setSize(3000, 2000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();

        Formulario aba1 = new Formulario();
        PainelEstoque aba2 = new PainelEstoque();
        abas.addTab("Formulario", aba1);
        abas.addTab("Estoque", aba2);

        add(abas);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Principal();
    }
}
