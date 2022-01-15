//Programa que calcula a raiz quadrada aproximada de algum número maior que 1 com certa precisão escolhida pelo usuário.

#include<stdio.h>

int main() {

    double n, ini, fim, x, eps, dif;
    int i;
    ini = 0;
    i = 0;
    
   

    printf("Digite o numero e a precisao com quais voce deseja calcular a raiz quadrada:\n");

    if (scanf("%lf %lf", &n, &eps)==2){
        if (n >= 1 && eps > 0){
        
        fim = n;
    
        while(1){
            x = (ini + fim)/ 2;

            if (x*x - n >= 0){
         
             dif = x*x - n;
            } 
            else{

                dif = -(x*x - n);
            }

            printf("%d: intervalo = [ %.8lf, %.8lf] x = %.8lf \n", i, ini, fim, x);
    
            if (dif < eps){
                break;
             }
    
            if (x*x - n > 0){      

                fim = x;  
            }

            else{
           
                ini = x;        
            }

            i = i + 1;

            }
            
    

            printf("Numero de iteracoes: %d \nDiferenca absoluta: %.10lf \nRaiz quadrada: %.10lf\n", i + 1, dif, x);
        
            return 0;
        }
        else{
            printf("Erro na leitura do valor.");
            return -2;
        }
    }

    else{
        printf("Erro na leitura do valor.");
    
        return -1;
    }
}
