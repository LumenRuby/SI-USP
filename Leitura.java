package escalonador;

import java.io.*;
import java.util.*;

public class Leitura {
    private ArrayList<BCP> Programas;
    private int quantum;

    public Leitura(int numDeArquivos) {
        try {
            Programas = leProgramas(numDeArquivos);
            quantum = leQuantum();

        } catch (FileNotFoundException e) {
            System.out.println("File name not found");
            // System.out.println(numArquivo + ".txt");
        } catch (IOException e) {
            System.out.println("Invalid File format");
        }
    }

    private ArrayList<BCP> leProgramas(int numDeArquivos) throws IOException {
        ArrayList<BCP> Programas = new ArrayList<BCP>();

        for (int i = 1; i <= numDeArquivos; i++) {
            String[] buffer = new String[21]; // inicializando o array que guarda temporariamente as instruções do
                                              // programa
            String nomeDoArquivo = i < 10 ? "0" + Integer.toString(i) + ".txt" : Integer.toString(i) + ".txt";

            BufferedReader in = new BufferedReader(new FileReader(nomeDoArquivo));
            ; // in que irá conter as instruções de cada programa
            // Encontrando o nome do arquivo
            String Line = in.readLine();
            String nome = Line;

            // Lê as instruções
            Line = in.readLine();
            int j;
            for (j = 0; Line != null; j++) {
                buffer[j] = Line;
                Line = in.readLine();
            }

            String[] instrucoes = new String[j];
            for (int m = 0; m < j; m++)
                instrucoes[m] = buffer[m]; // para evitar que as instruções sejam um array com vários null no fim, isso
                                           // é necessário
            int prioridade = lePrioridade(i); // lê prioridade

            BCP bcp = new BCP(nome, instrucoes, prioridade); // criando o bcp
            Programas.add(bcp); // adicionando o bcp à lista
            in.close();
        }
        return Programas;

    }

    private int leQuantum() throws NumberFormatException, IOException {
        BufferedReader in = new BufferedReader(new FileReader("quantum.txt"));
        int quantum = Integer.parseInt(in.readLine());
        in.close();
        return quantum;
    }

    private int lePrioridade(int numArquivo) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("prioridades.txt"));
        int prioridade = 0;
        String Line = new String();
        for (int i = 0; i < numArquivo; i++) {
            Line = in.readLine();
            prioridade = Integer.parseInt(Line);
        }
        in.close();
        return prioridade;
    }

    public int getQuantum() {
        return quantum;
    }

    public BCP getBCP(int numArquivo) {
        return Programas.get(numArquivo);
    }
}
