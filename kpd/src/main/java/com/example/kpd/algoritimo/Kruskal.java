package com.example.kpd.algoritimo;

import java.util.Arrays;

public class Kruskal {
    public int V, E;
    public Aresta[] arestas;
    public int custoTotalMST = 0;

    public Kruskal(int v, int e) {
        V = v;
        E = e;
        arestas = new Aresta[e];
    }

    class Subconjunto {
        int pai, rank;
    }

    private int find(Subconjunto[] subconjuntos, int i) {
        if (subconjuntos[i].pai != i)
            subconjuntos[i].pai = find(subconjuntos, subconjuntos[i].pai);
        return subconjuntos[i].pai;
    }

    private void union(Subconjunto[] subconjuntos, int x, int y) {
        int raizX = find(subconjuntos, x);
        int raizY = find(subconjuntos, y);

        if (subconjuntos[raizX].rank < subconjuntos[raizY].rank)
            subconjuntos[raizX].pai = raizY;
        else if (subconjuntos[raizX].rank > subconjuntos[raizY].rank)
            subconjuntos[raizY].pai = raizX;
        else {
            subconjuntos[raizY].pai = raizX;
            subconjuntos[raizX].rank++;
        }
    }

    public void kruskalMST() {
        Aresta[] resultado = new Aresta[V];
        int e = 0, i = 0;

        Arrays.sort(arestas);

        Subconjunto[] subconjuntos = new Subconjunto[V];
        for (int v = 0; v < V; ++v) {
            subconjuntos[v] = new Subconjunto();
            subconjuntos[v].pai = v;
            subconjuntos[v].rank = 0;
        }

        while (e < V - 1 && i < E) {
            Aresta prox = arestas[i++];
            int x = find(subconjuntos, prox.origem);
            int y = find(subconjuntos, prox.destino);

            if (x != y) {
                resultado[e++] = prox;
                custoTotalMST += prox.peso;
                union(subconjuntos, x, y);
            }
        }
    }
}
