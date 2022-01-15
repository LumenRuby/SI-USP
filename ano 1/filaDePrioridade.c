#include <math.h>
#include "filaDePrioridade.h"
#define MAX 5

void troca(PONT *a , PONT *b) {
  PONT tmp;
  tmp = *a;
  *a = *b;
  *b = tmp;
}

void refazHeapMaximoBaixoCima(PFILA f, int k){
  int temp;
    
  if(k > 0 && f->heap[k]->prioridade > f->heap[(k - 1) / 2]->prioridade){
     troca(&f->heap[k], &f->heap[(k - 1) / 2]);
     temp =  f->heap[k]->posicao;
     f->heap[k]->posicao = f->heap[(k - 1) / 2]->posicao;
     f->heap[(k - 1) / 2]->posicao = temp;
     refazHeapMaximoBaixoCima(f, (k - 1) / 2);
  }
}

void refazHeapMaximoCimaBaixo(PFILA f, int k){
  int maior_filho;
  int temp;

  if (2*k + 1 < f->elementosNoHeap) {
    maior_filho = 2*k + 1;
    if (2*k + 2 < f->elementosNoHeap && f->heap[2*k + 1]->prioridade < f->heap[2*k + 2]->prioridade)
      maior_filho = 2*k + 2;
    if (f->heap[k]->prioridade < f->heap[maior_filho]->prioridade){
      troca(&f->heap[k], &f->heap[maior_filho]);
      temp =  f->heap[k]->posicao;
      f->heap[k]->posicao = f->heap[maior_filho]->posicao;
      f->heap[maior_filho]->posicao = temp;
      refazHeapMaximoCimaBaixo(f, maior_filho);
    }
  }
}
   


PFILA criarFila(){
  PFILA res = (PFILA) malloc(sizeof(FILADEPRIORIDADE));
  res->referencias = (PONT*) malloc(sizeof(PONT)*MAX);
  res->heap = (PONT*) malloc(sizeof(PONT)*MAX);
  int i;
  for (i=0;i<MAX;i++) {
    res->referencias[i] = NULL;
    res->heap[i] = NULL;
  }
  res->elementosNoHeap = 0;
  return res;
}


bool exibirLog(PFILA f){
  printf("Log [elementos: %i]\n", f->elementosNoHeap);
  PONT atual;
  int i;
  for (i=0;i<f->elementosNoHeap;i++){
    atual = f->heap[i];
    printf("[%i;%f;%i] ", atual->id, atual->prioridade, atual->posicao);
  }
  printf("\n\n");
}

int tamanho(PFILA f){
  int tam = 0;
  
  tam = f->elementosNoHeap;

  return tam;
}

bool inserirElemento(PFILA f, int id, float prioridade){
  bool res = false;
  PONT atual;
  int i;

  if (id < 0 || id >= MAX) { return res; }
  if(f->referencias[id] != NULL) { return res; }

  atual = (PONT) malloc(sizeof(ELEMENTO));
  atual->id = id;
  atual->prioridade = prioridade;
  f->referencias[id] = atual;
  atual->posicao = f->elementosNoHeap;
  f->heap[f->elementosNoHeap] = atual; 
  f->elementosNoHeap++;

  refazHeapMaximoBaixoCima(f, f->elementosNoHeap - 1);

  
  return true;
}

bool aumentarPrioridade(PFILA f, int id, float novaPrioridade){
  bool res = false;
  
  if(f->referencias[id] == NULL) { return res; }
  if (id < 0 || id >= MAX || f->referencias[id]->prioridade >= novaPrioridade) { return res; }

  f->referencias[id]->prioridade = novaPrioridade;
  refazHeapMaximoBaixoCima(f, f->referencias[id]->posicao);
  
  return true;
}

bool reduzirPrioridade(PFILA f, int id, float novaPrioridade){
  bool res = false;

  if(f->referencias[id] == NULL) { return res; }
  if (id < 0 || id >= MAX || f->referencias[id]->prioridade <= novaPrioridade) { return res; }
  
  f->referencias[id]->prioridade = novaPrioridade;
  refazHeapMaximoCimaBaixo(f, f->referencias[id]->posicao);
  
  return true;
}

PONT removerElemento(PFILA f){
  PONT res = NULL;
  PONT item;
  int temp;

  if(f->elementosNoHeap == 0) { return res; }
  item = f->heap[0];
  f->referencias[f->heap[0]->id] = NULL;
  troca(&f->heap[0], &f->heap[f->elementosNoHeap - 1]);
  temp =  f->heap[0]->posicao;
  f->heap[0]->posicao = f->heap[f->elementosNoHeap - 1]->posicao;
  f->heap[f->elementosNoHeap - 1]->posicao = temp;
  f->elementosNoHeap--;
  refazHeapMaximoCimaBaixo(f, 0);

  
  return item;
}

bool consultarPrioridade(PFILA f, int id, float* resposta){
  bool res = false;
  if (id < 0 || id >= MAX) { return res; }
  if(f->referencias[id] == NULL) { return res; }
  *resposta = f->referencias[id]->prioridade;
  return true;
}
