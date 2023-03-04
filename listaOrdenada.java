package escalonador;

import java.util.ArrayList;
import java.util.Collections;

public class listaOrdenada extends ArrayList<BCP> {

    // Declaração da variável serialVersionUID para não incorrer em
    // InvalidClassException.
    private static final long serialVersionUID = 1L;

    public void insercaoOrdenada(BCP processo) {
        add(processo);
        Comparable<BCP> cmp = (Comparable<BCP>) processo;
        for (int i = size() - 1; i > 0 && cmp.compareTo(get(i - 1)) < 0; i--)
            Collections.swap(this, i, i - 1);
    }
}
