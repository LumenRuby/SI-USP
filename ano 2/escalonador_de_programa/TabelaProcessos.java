package escalonador;

public class TabelaProcessos {
    static listaOrdenada prontos = new listaOrdenada();
    static listaOrdenada bloqueados = new listaOrdenada();

    public static void adicionaBlocoProntos(BCP bloco) {
        prontos.insercaoOrdenada(bloco);
    }

    public static void adicionaBlocoBloqueados(BCP bloco) {
        bloqueados.insercaoOrdenada(bloco);
    }

    public static BCP removePrimeiroPronto() {
        return prontos.remove(0);
    }

    public static BCP removePrimeiroBloqueado() {
        return bloqueados.remove(0);
    }
}
