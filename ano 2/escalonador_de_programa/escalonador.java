package escalonador;

import static escalonador.TabelaProcessos.bloqueados;
import static escalonador.TabelaProcessos.prontos;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class escalonador {

    private static int quantum;
    private static int interrupcoes = 0;
    private static int instrucoes = 0;
    private static int contQuantuns = 0;
    private final static int processos = 10;

    public static void main(String[] args) throws Exception {
        Leitura leitura = new Leitura();
        int quantum = leitura.getQuantum();

        criarLog(quantum);

        for (BCP bcp : prontos) {
            System.out.println("Carregando processo " + bcp.processo.nome);
        }

        while (prontos.size() > 0 || bloqueados.size() > 0) {
            administraProcesso(TabelaProcessos.removePrimeiroPronto(), quantum);
            decrementaEsperaBloqueados();
            verificaEspera();
            verificaCreditosProntos();
        }

        System.out.println("media de trocas: " + interrupcoes / processos);

        System.out.println("media de instruções: " + instrucoes / contQuantuns);

        System.out.println("quantum: " + quantum);

    }

    private static void criarLog(int quantum) {
        if (quantum < 10) {
            System.setOut(new PrintStream(new FileOutputStream("log0" + quantum + ".txt")));
        } else {
            System.setOut(new PrintStream(new FileOutputStream("log" + quantum + ".txt")));
        }
    }

    private static void administraProcesso(BCP atual, int quantum) {
        int cont = 0;
        boolean termino = false, suspenso = false;
        atual.consomeCredito();
        atual.processo.setEstado(Estado.EXECUTANDO);
        System.out.println("Executando processo " + atual.processo.nome);
        while (atual != null && cont < quantum) {
            administraInstrucao(atual, termino, suspenso);
            atual.PC++;
            cont++;
        }
        if (!termino) {
            if (cont < 2) {
                System.out.println("Interrompendo " + atual.processo.nome + " após " + cont + " instrução");
            } else {
                System.out.println("Interrompendo " + atual.processo.nome + " após " + cont + " instruções");
            }
        }

        interrupcoes++;
        contQuantuns++;
        instrucoes += cont;

        if (!suspenso) {
            atual.processo.setEstado(Estado.PRONTO);
            TabelaProcessos.adicionaBlocoProntos(atual);
        }
    }

    private static void administraInstrucao(BCP atual) {
        String instrucao = atual.processo.instrucao[atual.PC];
        boolean suspenso, termino;

        if (instrucao.equals("E/S")) {
            if (!atual.executado) {
                System.out.println("E/S iniciada em " + atual.processo.nome);
                entradaSaida(atual);
                suspenso = true;
                break;
            } else {
                atual.executado = false;
            }
        }

        else if (instrucao.equals("FIM")) {
            System.out.println(atual.processo.nome + " terminou. Sendo, X=" + atual.registradorX + " e Y="
                    + atual.registradorY);
            prontos.remove(atual);
            suspenso = true;
            termino = true;
            break;
        }

        else if (instrucao.contains("X=")) {
            atual.registradorX = Integer.parseInt(instrucao.substring(2));
        }

        else if (instrucao.contains("Y=")) {
            atual.registradorY = Integer.parseInt(instrucao.substring(2));
        }
    }

    private static void entradaSaida(BCP atual) {
        atual.processo.setEstado(Estado.BLOQUEADO);
        TabelaProcessos.adicionaBlocoBloqueados(atual);
        int i = bloqueados.indexOf(atual);
        bloqueados.get(i).espera = 2;
        bloqueados.get(i).executado = true;
    }

    private static boolean verificaCreditosProntos() {
        for (BCP p : prontos) {
            if (p.getCreditos() != 0)
                return false;
        }
        for (BCP p : prontos) {
            p.setCreditos(p.getPrioridade());
        }
        return true;
    }

    private static void decrementaEsperaBloqueados() {
        for (BCP b : bloqueados) {
            if (b.getEspera() > 0)
                b.setEspera(b.getEspera() - 1);
        }
    }

    private static void verificaEspera() {
        int cont = 0;

        for (BCP b : bloqueados) {
            if (b.getEspera() != 0) {
                break;
            }
            b.processo.setEstado(Estado.PRONTO);
            TabelaProcessos.adicionaBlocoProntos(b);
            cont++;
        }

        for (int i = 0; i < cont; i++) {
            TabelaProcessos.removePrimeiroBloqueado();
        }
    }
}
