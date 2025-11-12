package com.example.kpd.algoritimo;

import java.util.Random;

public class GeradorGrafo {

    // Gera um array de arestas aleatórias
    public static Aresta[] gerarArestas(int numVertices, int numArestas) {
        Aresta[] arestas = new Aresta[numArestas];
        Random rand = new Random();

        for (int i = 0; i < numArestas; i++) {
            int origem = rand.nextInt(numVertices);
            int destino = rand.nextInt(numVertices);

            // Garante que não haja laços (origem != destino)
            while (destino == origem) {
                destino = rand.nextInt(numVertices);
            }

            int peso = rand.nextInt(20) + 1; // peso aleatório de 1 a 20
            arestas[i] = new Aresta(origem, destino, peso);
        }

        return arestas;
    }
}
