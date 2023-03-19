#include<stdlib.h>
#include<stdio.h>
#include <stdbool.h>
#include <string.h>
#define t 2

typedef struct NO{
    int chaves[2*t - 1];
    struct NO* filhos[2*t];
    int tam;
    bool folha;
}TipoNO;

TipoNO* CriarArvore(){
    TipoNO* raiz = (TipoNO*)malloc(sizeof(TipoNO));
    raiz->tam = 0;
    raiz->folha = true;
    return raiz;
}

void dividirNO(TipoNO* x, TipoNO* y, int i){
    TipoNO* z = (TipoNO*)malloc(sizeof(TipoNO));
    z->folha = y->folha;
    z->tam = t - 1;

    for(int j = 0; j < t - 1; j++){
        z->chaves[j] = y->chaves[j + t];
    }

    if(y->folha == false){
        for(int  j = 0; j <= t - 1; j++){
            z->filhos[j] = y->filhos[j + t];
        }
    }

    y->tam = t - 1;
    for(int j = x->tam; j >= i; j--){
        x->filhos[j + 1] = x->filhos[j];
    }
    x->filhos[i + 1] = z;

    for(int j = x->tam - 1; j >= i - 1; j--){
        x->chaves[j + 1] = x->chaves[j];
    }
    
    x->chaves[i] = y->chaves[t - 1];
    x->tam = x->tam + 1;
}

void switchToRight(TipoNO* ant, int position){
	int i;
	for (i = ant->tam-1; i > position-1; i--){
		ant->chaves[i+1] = ant->chaves[i];
		ant->filhos[i+2] = ant->filhos[i+1];
	}
	ant->tam++;
}

void switchToLeft (TipoNO* ant, int position){
	int i;
	for(i = position; i < ant->tam - 1; i++)
	{
		ant->chaves[i] = ant->chaves[i+1];
		ant->filhos[i+1] = ant->filhos[i+2];
	}

	ant->tam--;
}

TipoNO* pai (TipoNO* ant, int * path){
	if (!(ant->folha)) return pai(ant->filhos[ant->tam], path);
	*path = ant->chaves[ant->tam-1];
	return ant;
}

TipoNO* filho (TipoNO* ant, int * path){
	if (!(ant->folha)) return pai(ant->filhos[0], path);
	*path = ant->chaves[0];
	return ant;
}


void unir(TipoNO** raiz, TipoNO* ant, int position, int side, int* flag){
	int i;
	*flag = 0;

	TipoNO* filho = ant->filhos[position];
	TipoNO* irmao = ant->filhos[side];

	filho->tam = 2*t - 1;
	filho->chaves[t-1] = ant->chaves[position];

	for(i = 0; i < irmao->tam; i++)
	{
		filho->chaves[i+t] = irmao->chaves[i];
		filho->filhos[i+t] = irmao->filhos[i];
	}

	filho->filhos[filho->tam] = irmao->filhos[i];
	switchToLeft(ant, position);

	if(ant == *raiz && (*raiz)->tam == 0)
	{
		*flag = 1;
		*raiz = filho;
	}

	free(irmao);
}

int removerArvore(TipoNO** raiz, TipoNO* pontoPai, int chave){
	int i = 0;
	while(pontoPai->chaves[i] < chave && i < pontoPai->tam) i++;

	if(!(pontoPai->folha)){
		if(pontoPai->chaves[i] == chave){
			int * pathAnt = (int*)malloc(sizeof(int));
			int * pathChild = (int*)malloc(sizeof(int));
			TipoNO* ant = pai(pontoPai->filhos[i], pathAnt);
			TipoNO* suc = filho(pontoPai->filhos[i+1],pathChild);
			if(pontoPai->filhos[i]->tam > t-1){
				pontoPai->chaves[i] = *pathAnt;
				return removerArvore(raiz, pontoPai->filhos[i], *pathAnt);
			}
			else if(i < pontoPai->tam && pontoPai->filhos[i+1]->tam > t-1){
				pontoPai->chaves[i] = *pathChild;
				return removerArvore(raiz, pontoPai->filhos[i+1], *pathChild);
			}
			else {
				int* flag = (int*)malloc(sizeof(int));
				if(i < pontoPai->tam) unir(raiz, pontoPai,i,i+1, flag);
				else unir(raiz, pontoPai,i,i-1, flag);
				return removerArvore(raiz, pontoPai->filhos[i], chave);
			}
		}else{
			int j = 0;
			TipoNO* pontoFilho = pontoPai->filhos[i];

			while (pontoFilho->chaves[j] < chave && j < pontoFilho->tam) j++;

			if(pontoFilho->tam == t-1){
				if(i < pontoPai->tam && pontoPai->filhos[i+1]->tam > t-1){
					TipoNO* rightNode = pontoPai->filhos[i+1];
					pontoFilho->tam++;
					pontoFilho->chaves[t-1] = pontoPai->chaves[i];
					pontoFilho->filhos[t] = rightNode->filhos[0];
					pontoPai->chaves[i] = rightNode->chaves[i];
					switchToLeft(rightNode, 0);
					return removerArvore(raiz, pontoFilho, chave);
				}else if(i > 0 && pontoPai->filhos[i-1]->tam > t-1){
					TipoNO* leftNode = pontoPai->filhos[i-1];
					pontoFilho->tam++;
					pontoFilho->chaves[t-1] = pontoPai->chaves[i];
					pontoFilho->filhos[t] = leftNode->filhos[0];
					pontoPai->chaves[i] = leftNode->chaves[i];
					switchToLeft(leftNode, 0);
					return removerArvore(raiz, pontoFilho, chave);
				}else{
					int* flag = (int*)malloc(sizeof(int));

					if(i < pontoPai->tam){
						unir(raiz,pontoPai,i,i+1,flag);
						if(*flag) free(pontoPai);
						return removerArvore(raiz,pontoFilho,chave);
					}else{
						unir(raiz,pontoPai,i,i-1,flag);
						if(*flag) free(pontoPai);
						return removerArvore(raiz,pontoFilho,chave);
					} 
				}
			}else return removerArvore(raiz,pontoFilho,chave);
		}
	}else{
		if (pontoPai->chaves[i] == chave){
			switchToLeft(pontoPai,i);
			return true;
		}
		return false;
	}
}



void InserirArvoreNonFull(TipoNO* x, int chave){
    int i = x->tam - 1;
    if(x->folha){
        if(x->tam == 0){
            x->chaves[0] = chave;
            x->tam = x->tam + 1;
            return;
        }
        while(i >= 0 && chave < x->chaves[i]){
            x->chaves[i + 1] = x->chaves[i];
            i--;
        }
        x->chaves[i + 1] = chave;
        x->tam = x->tam + 1;
    }
    else{
        while(i >= 0 && chave < x->chaves[i]){
            i--;
        }
        i++;
       

        if(x->filhos[i]->tam == 2*t - 1){
            dividirNO(x, x->filhos[i], i);
            if(chave > x->chaves[i]){
                i++;
            }
        }
        InserirArvoreNonFull(x->filhos[i], chave);
    }
}

TipoNO* InserirArvore(TipoNO* raiz, int chave){
    if (raiz->tam == 2*t - 1){
        TipoNO* novoNO = (TipoNO*)malloc(sizeof(TipoNO));
        novoNO->folha = false;
        novoNO->tam = 0;
        novoNO->filhos[0] = raiz;
        dividirNO(novoNO, raiz, 0);
        InserirArvoreNonFull(novoNO, chave);
        return novoNO;
    }
    else{
        InserirArvoreNonFull(raiz, chave);
        return raiz;
    }
}

void ImprimirArvorePreOrdem(TipoNO* raiz, int nivel, FILE* out){
    if (raiz == NULL) return;

    fprintf(out, "Nivel %d : ", nivel);
    fflush(out);
    for (int i = 0; raiz && i < raiz->tam; i++){
        fprintf(out, "%d ", raiz->chaves[i]);
    }
    fprintf(out, "\n");
    fflush(out);
    nivel++;
    for (int i = 0; i <= raiz->tam; i++){
        ImprimirArvorePreOrdem(raiz->filhos[i], nivel, out);
    }
} 



int main(int argc, char** argv){
    char linha[256], comando[256];
    FILE* in = fopen(argv[1], "r");
    FILE* out = fopen("saida.txt", "w");
    int parametro;

    TipoNO* raiz = CriarArvore();


   while (fgets(linha, sizeof(linha), in)){
       sscanf(linha, " %s %d", comando, &parametro);
        if (strcmp(comando, "insere") == 0) {
            raiz = InserirArvore(raiz, parametro);
        }
        else if(strcmp(comando, "imprime") == 0){
            ImprimirArvorePreOrdem(raiz, 0, out);
        }
        else if(strcmp(comando, "remove") == 0){
            removerArvore(&raiz, raiz, parametro);
        }
        else{
            fclose(in);
            break;
        }
    
    }
    ImprimirArvorePreOrdem(raiz, 0, out);
    fclose(out);
   
    return 0;
}