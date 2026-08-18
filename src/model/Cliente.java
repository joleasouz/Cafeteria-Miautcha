package model;

/* --- Jolea

    Vitoria, tive que arrumar o erro daqui pra poder rodar o teste dos Pedidos,
    mas basicamente você tinha declarado telefone como int na linha 8 e como String no
    método setTelefone(). O mesmo aconteceu com o cpf. Deixei tudo como String porque é o padrão
    do diagrama de classes */ 

public class Cliente  {
    private int id;
    private String nome;
    private String cpf;
    public String email;
    private String telefone;


public Cliente(int id, String nome, String cpf, String email, String telefone) {
    this.id = id;
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;
    this.telefone = telefone;
}
public Cliente() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}