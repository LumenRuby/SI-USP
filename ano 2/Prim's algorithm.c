#define INFINITO 999999
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>


typedef struct {
   int id;
   float peso;
} TipoItem;

typedef struct {
   TipoItem *elementos;
   int n;
} TipoFila;

void troca(TipoItem *a, TipoItem *b) {
    TipoItem t = *a;
    *a = *b;
    *b = t;
}

typedef struct aresta{
    int vdest;
    float peso_t;
    struct aresta* prox;    
} TipoAresta;

typedef TipoAresta* TipoApontador;

typedef struct{
    TipoApontador* listaadj;
    int NumVertices;
    int NumArestas;
}TipoGrafo;

TipoGrafo* inicializaGrafo(int nv){
    int i;
    TipoGrafo* grafo = (TipoGrafo*)malloc(sizeof(TipoGrafo));
    grafo->listaadj = (TipoApontador*)malloc(nv * sizeof(TipoApontador));
    for(i = 0; i < nv; i++){
        grafo->listaadj[i] = NULL;
    }
    grafo->NumVertices = nv;
    return grafo;
}

void preencheGrafo(int na, TipoGrafo* grafo,int* arcos, float* pesos){
    for(int i = 0; i <= na * 2 - 2; i = i + 2){
        for (TipoApontador j = grafo->listaadj[arcos[i]]; j != NULL; j = j->prox){ 
            if (j->vdest == (arcos[i + 1])) return;
        }
        TipoApontador novo1 = (TipoApontador)malloc(sizeof(TipoAresta));
        novo1->vdest = arcos[i + 1];
        novo1->peso_t = pesos[i/2];
        novo1->prox = grafo->listaadj[arcos[i]];
        grafo->listaadj[arcos[i]] = novo1;
        grafo->NumArestas++;
    }

    for(int i = 0; i <= na * 2 - 2; i = i + 2){
        TipoApontador novo2 = (TipoApontador)malloc(sizeof(TipoAresta));
        novo2->vdest = arcos[i];
        novo2->peso_t = pesos[i/2]; 
        novo2->prox = grafo->listaadj[arcos[i + 1]];
        grafo->listaadj[arcos[i + 1]] = novo2;
    }
}

TipoFila* criarFila(int tam){
  TipoFila* res = (TipoFila*)malloc(sizeof(TipoFila));
  res->elementos = (TipoItem*)malloc(sizeof(TipoItem)*tam);
  res->n = 0;
  return res;
}


void inserirElemento(TipoFila* f, TipoItem a){
    f->elementos[f->n] = a;
    f->n++; 
}

TipoItem extrairMinimo(TipoFila* f){
    int min = 0;
    for (int i = 1; i < f->n; i++){
        if (f->elementos[min].peso > f->elementos[i].peso){
            min = i;
        }
    }
    troca(&(f->elementos[min]), &(f->elementos[f->n-1]));
    f->n--;
    return f->elementos[f->n];

}

void BuscaSimples(TipoFila* f, int id, float peso){
    for (int i = 0; i < f->n; i++){
        if (f->elementos[i].id == id){
            f->elementos[i].peso = peso;
        }
    }
}


void Prim(TipoGrafo* grafo, FILE* out){
    float custo[grafo->NumVertices];
    float TOTAL = 0;
    bool arvore[grafo->NumVertices];
    int anteriores[grafo->NumVertices];
    TipoFila* fila = criarFila(grafo->NumVertices);
    
    for (int i = 1; i < grafo->NumVertices; i++){
        anteriores[i] = -1, arvore[i] = false, custo[i] = INFINITO;
    } 

    anteriores[0] = 0; 
    arvore[0] = true;
    custo[0] = 0;

   for (TipoApontador j = grafo->listaadj[0]; j != NULL; j = j->prox){ 
        anteriores[j->vdest] = 0, custo[j->vdest] = j->peso_t;
    } 

    for(int i = 1; i < grafo->NumVertices; i++){
        TipoItem* b = (TipoItem*)malloc(sizeof(TipoItem));
        b->id = i;
        b->peso = custo[i];
        inserirElemento(fila, *b);
    }

    
    while(fila->n != 0){
        int i = extrairMinimo(fila).id;
        TOTAL = custo[i] + TOTAL;
        if (custo[i] == INFINITO) break;
        arvore[i] = true;       

        for(TipoApontador j = grafo->listaadj[i]; j != NULL; j = j->prox){
            if(arvore[j->vdest] == false && j->peso_t < custo[j->vdest]){
                custo[j->vdest] = j->peso_t;
                BuscaSimples(fila, j->vdest, j->peso_t); 
                anteriores[j->vdest] = i; 
            }
            
        }

    }    
    free(fila);

    fprintf(out, "%g\n", TOTAL);
    
    for(int i = 1; i < grafo->NumVertices; i ++){
        fprintf(out,"%d %d\n", anteriores[i], i);
    }
    
}
 

int main(int argc, char* argv[]){
    char linha[50];
    int vertices, arestas, *arcos, peso;
    float* pesos; 
    vertices = arestas = 0;
    FILE* in = fopen(argv[1], "r");
    fgets(linha, 50, in);
    sscanf(linha,"%d %d \n", &vertices, &arestas);
    arcos = (int*)malloc(sizeof(int)*arestas*2);
  
    pesos = (float*)malloc(sizeof(float)*arestas);
    
    for(int i = 0; i <= arestas*2 - 2; i = i + 2){
        fgets(linha, 50, in);
        sscanf(linha, "%d %d %d", &arcos[i], &arcos[i+1], &peso);
        pesos[i/2] = (float)peso;
    }
     
    TipoGrafo* grafo = inicializaGrafo(vertices);
    preencheGrafo(arestas, grafo, arcos, pesos);

    FILE* out = fopen(argv[2], "w");
    
    Prim(grafo, out);
        
    return 0;
}