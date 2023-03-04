package escalonador;

public class BCP implements Comparable<BCP> {
    int PC, estado, prioridade, creditos; // Estado: 2 = executando; 1 = pronto; 0 = espera
    int espera;
    int X;
    int Y;
    boolean executado;
    boolean termino, suspenso;
    String[] instrucoes;
    String nome;

    public BCP(String nome, String[] instrucoes, int prioridade) {
        PC = 0;
        X = 0;
        Y = 0;
        estado = 1;
        this.nome = nome;
        this.instrucoes = instrucoes;
        this.prioridade = prioridade;
        this.creditos = prioridade;
    }

    public void consomeCredito() {
        creditos--;
    }

    public int compareTo(BCP bcp) {
        if (this.creditos > bcp.creditos)
            return 1;
        if (this.creditos == bcp.creditos)
            return 0;
        return -1;
    }

    public String getNome() {
        return nome;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getEspera() {
        return espera;
    }

    public void setEspera(int espera) {
        this.espera = espera;
    }

    public void print() {
        System.out.println(prioridade);
        for (int i = 0; i < instrucoes.length; i++)
            System.out.println(instrucoes[i]);
    }
}
