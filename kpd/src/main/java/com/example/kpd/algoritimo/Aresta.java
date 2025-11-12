package com.example.kpd.algoritimo;

public class Aresta implements Comparable<Aresta> {
    
    // Atributos
    int origem, destino, peso;

    // Construtor
    public Aresta(int origem, int destino, int peso) {
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    // Método de comparação para ordenar as arestas pelo peso (ordem crescente)
    @Override
    public int compareTo(Aresta outra) {
        // Retorna a diferença de pesos. Se positivo, 'this' é maior.
        // Isso garante que o Arrays.sort() ordene da menor para a maior.
        return this.peso - outra.peso;
    }
}