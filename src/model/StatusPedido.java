package model;

public enum StatusPedido {
    PENDENTE,
    PREPARANDO,
    PRONTO,
    ENVIADO,
    CANCELADO;

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

    public static StatusPedido cancelado() {
        return CANCELADO;
    }

    public String exibicao() {
        switch (this) {
            case PENDENTE: return "Pendente";
            case PREPARANDO: return "Preparando";
            case PRONTO: return "Pronto";
            case ENVIADO: return "Enviado";
            case CANCELADO: return "Cancelado";
            default: return name();
        }
    }
}

