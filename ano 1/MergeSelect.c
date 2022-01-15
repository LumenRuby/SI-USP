//Programa para selecionar o i-ésimo menor elemento dentro de uma sequência de inteiros após ela ser ordenada pelo algoritmo MergeSort.

#include <stdio.h>
#include <stdlib.h>


int* merge(int* array1, int n, int m){
    int i, j, k;
    int* array2 = malloc(n * sizeof (int));

    for(i = 0, j = m, k = 0; k < n; k++){
        array2[k] = j == n      ? array1[i++]
             : i == m           ?  array1[j++]
             : array1[j] < array1[i] ? array1[j++]
             :               array1[i++]; 
    }

    return array2;
}

void mergeSort(int* array1, int size){
    int m = size / 2;
    int * temp;
    int i;
    
    if (size <= 1){ return; }
    mergeSort(array1, m);
    mergeSort(array1 + m, size - m);
    temp = merge(array1, size, m);

    for(int i = 0; i < size; i++){ array1[i] = temp[i]; }
}

int select(int* array1, int i, int size){
    mergeSort(array1, size);
    return array1[i-1]; 
}

int main () {
    #define MAX 10
    int *a = malloc (sizeof(int)*MAX);
    int i, resultado;

    for(i = 0; i < MAX; i++){
        scanf("%d", &a[i]);
    }

    scanf("%d", &i);
    resultado = select(a, i, MAX);
    printf("\n%d", resultado);



    return 0;
}
