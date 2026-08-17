package model;

public class StatusPedido {
public enum Status {
    PENDENTE,
    PREPARANDO,
    PRONTO,
    ENVIADO;

    public static Status pendente() {
        return PENDENTE;
    }

    public static Status preparando() {
        return PREPARANDO;
    }

    public static Status pronto() {
        return PRONTO;
    }

    public static Status enviado() {
        return ENVIADO;
    }
}
}

//forma de usar o enum
/* Status status = Status.preparando();

if (status == Status.PREPARANDO) {
    bglgenericodemonstracao();
} */

