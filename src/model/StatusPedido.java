package model;

public enum StatusPedido {
    PENDENTE,
    PREPARANDO,
    PRONTO,
    ENVIADO;

    public static StatusPedido pendente() {
        return PENDENTE;
    }

    public static StatusPedido preparando() {
        return PREPARANDO;
    }

    public static StatusPedido pronto() {
        return PRONTO;
    }

    public static StatusPedido enviado() {
        return ENVIADO;
    }
}


//forma de usar o enum. Obs:get e set no arquivo q for usar o enum
/* StatusPedido StatusPedido = StatusPedido.preparando();

if (StatusPedido == StatusPedido.PREPARANDO) {
    bglgenericodemonstracao();
} */
