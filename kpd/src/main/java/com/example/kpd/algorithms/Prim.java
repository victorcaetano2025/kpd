package com.example.kpd.algorithms;

import com.example.kpd.model.Grafo;
import java.util.*;

public class Prim {
    public static double executar(Grafo grafo) {
        int n = grafo.getNumVertices();
        boolean[] visitado = new boolean[n];
        double[] chave = new double[n];
        Arrays.fill(chave, Double.POSITIVE_INFINITY);
        chave[0] = 0.0;

        PriorityQueue<int[]> fila = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        fila.add(new int[]{0, 0});
        double custoTotal = 0.0;

        while (!fila.isEmpty()) {
            int u = fila.poll()[0];
            if (visitado[u]) continue;
            visitado[u] = true;
            custoTotal += chave[u];

            for (var aresta : grafo.getAdjacentes(u)) {
                int v = aresta.getDestino().getId();
                double peso = aresta.getPeso();
                if (!visitado[v] && peso < chave[v]) {
                    chave[v] = peso;
                    fila.add(new int[]{v, (int) peso});
                }
            }
        }
        return custoTotal;
    }
}
