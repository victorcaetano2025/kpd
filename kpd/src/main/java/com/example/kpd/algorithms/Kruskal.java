package com.example.kpd.algorithms;

import com.example.kpd.model.Grafo;
import com.example.kpd.model.Aresta;
import java.util.*;

public class Kruskal {

    private static class Subconjunto { int pai, rank; }

    private static int encontrar(Subconjunto[] subconjuntos, int i) {
        if (subconjuntos[i].pai != i)
            subconjuntos[i].pai = encontrar(subconjuntos, subconjuntos[i].pai);
        return subconjuntos[i].pai;
    }

    private static void unir(Subconjunto[] subconjuntos, int x, int y) {
        int raizX = encontrar(subconjuntos, x);
        int raizY = encontrar(subconjuntos, y);
        if (subconjuntos[raizX].rank < subconjuntos[raizY].rank)
            subconjuntos[raizX].pai = raizY;
        else if (subconjuntos[raizX].rank > subconjuntos[raizY].rank)
            subconjuntos[raizY].pai = raizX;
        else {
            subconjuntos[raizY].pai = raizX;
            subconjuntos[raizX].rank++;
        }
    }

    public static double executar(Grafo grafo) {
        List<Aresta> arestas = new ArrayList<>(grafo.getArestas());
        arestas.sort(Comparator.comparingDouble(Aresta::getPeso));
        int n = grafo.getNumVertices();
        Subconjunto[] subconjuntos = new Subconjunto[n];
        for (int i = 0; i < n; i++) {
            subconjuntos[i] = new Subconjunto();
            subconjuntos[i].pai = i;
            subconjuntos[i].rank = 0;
        }

        double custoTotal = 0.0;
        int arestasIncluidas = 0;
        for (Aresta a : arestas) {
            int u = a.getOrigem().getId();
            int v = a.getDestino().getId();
            int raizU = encontrar(subconjuntos, u);
            int raizV = encontrar(subconjuntos, v);
            if (raizU != raizV) {
                custoTotal += a.getPeso();
                unir(subconjuntos, raizU, raizV);
                arestasIncluidas++;
                if (arestasIncluidas == n - 1) break;
            }
        }
        return custoTotal;
    }
}
