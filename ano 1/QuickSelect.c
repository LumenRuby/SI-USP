//Programa que seleciona o i-ésimo menor elemento de uma sequência de inteiros utilizando um raciocínio parecido com o do algorimo QuickSort, porém ele só ordena as partes
//necessárias do conjunto para que se chegue no número desejado pelo usuário.

#include <stdio.h>
#include <stdlib.h>


void swap(int* a, int* b) {
    int tmp;
    tmp = *a;
    *a = *b;
    *b = tmp;
}

int particionar(int* array1, int size) {
    int m = array1[size-1];
    int i = -1;
    int j;
    int k;

    for(j = 0; j < size-1; j++){
        if(array1[j] <= m){
            swap(&array1[++i], &array1[j]);
        }
    }
    swap(&array1[i+1], &array1[j]);


    return i + 1;
}

int select(int* array1, int size, int i){
    int q = particionar(array1, size);

    if(size == 1){ return array1[0]; }

    if (i - 1 < q){ 
        return select(array1, q, i); 
    }
    else{
        if(i - 1 > q){ 
            return select(&array1[q + 1], size - (q + 1), i - (q + 1)); 
        } 
        else { return array1[q]; }
    }
}

int main(){
    #define MAX 10
    int *a = malloc (sizeof(int)*MAX); 
    int i, resultado;

    for(i = 0; i < MAX; i++){
        scanf("%d", &a[i]);
    }

    scanf("%d", &i);
    resultado = select(a, MAX, i);
    printf("\n%d", resultado);

    return 0;
}
