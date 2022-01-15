#include <stdlib.h>
#include <stdio.h>
#define true 1
#define false 0

typedef int bool;

typedef struct aux {
  int id;
  bool ehPreferencial;
  struct aux* ant;
  struct aux* prox;
} ELEMENTO, * PONT;

typedef struct {
  PONT cabeca;
  PONT inicioNaoPref;
} FILAPREFERENCIAL, * PFILA;

PFILA criarFila(){
    PFILA res = (PFILA) malloc(sizeof(FILAPREFERENCIAL));
    res->cabeca = (PONT) malloc(sizeof(ELEMENTO));
    res->inicioNaoPref = res->cabeca;
    res->cabeca->id = -1;
    res->cabeca->ehPreferencial = false;
    res->cabeca->ant = res->cabeca;
    res->cabeca->prox = res->cabeca;
    return res;
}

int tamanho(PFILA f){
	PONT atual = f->cabeca->prox;
	int tam = 0;
	while (atual != f->cabeca) {
		atual = atual->prox;
		tam++;
	}
	return tam;
}

PONT buscarID(PFILA f, int id){
	PONT atual = f->cabeca->prox;
	while (atual != f->cabeca) {
		if (atual->id == id) return atual;
		atual = atual->prox;
	}
	return NULL;
}

void exibirLog(PFILA f){
	int numElementos = tamanho(f);
	printf("\nLog fila [elementos: %i]\t- Inicio:", numElementos);
	PONT atual = f->cabeca->prox;
	while (atual != f->cabeca) {
		printf(" [%i;%i]", atual->id, atual->ehPreferencial);
		atual = atual->prox;
	}
	printf("\n                       \t-    Fim:");
	atual = f->cabeca->ant;
	while (atual != f->cabeca) {
		printf(" [%i;%i]", atual->id, atual->ehPreferencial);
		atual = atual->ant;
	}
	printf("\n\n");
}


bool consultarPreferencial(PFILA f, int id){
	PONT atual = f->cabeca->prox;
	while (atual != f->cabeca) {
		if (atual->id == id) return atual->ehPreferencial;
		atual = atual->prox;
	}
	return -1;
}



bool inserirPessoaNaFila(PFILA f, int id, bool ehPreferencial){

	PONT i = buscarID(f, id);
    if(id < 0 || i != NULL) { return false; }

    i = (PONT) malloc(sizeof(ELEMENTO));
    i->id = id;
    i->ehPreferencial = ehPreferencial;
    
    if(ehPreferencial == true){
    i->ant = f->inicioNaoPref->ant;    
    f->inicioNaoPref->ant->prox = i; 
    f->inicioNaoPref->ant = i; 
    i->prox = f->inicioNaoPref;
    } else{
        if(f->inicioNaoPref == f->cabeca) { f->inicioNaoPref = i; }
        f->cabeca->ant->prox = i;
        i->ant = f->cabeca->ant;
        i->prox = f->cabeca;
        f->cabeca->ant = i;
    }

	return true;
}

bool atenderPrimeiraDaFila(PFILA f, int* id){
    int tam = tamanho(f); 
	PONT i = f->cabeca->prox;
	if (tam == 0) { return false; }

    *id = f->cabeca->prox->id; 
	
	f->cabeca->prox->prox->ant = f->cabeca;
	f->cabeca->prox = f->cabeca->prox->prox;

	if (i->ehPreferencial == false) { f->inicioNaoPref = f->cabeca->prox;}
	free(i);
   
	return true;
}


bool desistirDaFila(PFILA f, int id){
    PONT i = buscarID(f, id);
    if(id < 0 || i == NULL) { return false; } 

    if(i == f->inicioNaoPref) { f->inicioNaoPref = i->prox; }
    i->ant->prox = i->prox;
	i->prox->ant = i->ant;
    free(i);
	return true;
}
