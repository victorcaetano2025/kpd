package com.example.kpd.algorithms;

import com.example.kpd.model.Grafo;
import com.example.kpd.model.Vertice;
import java.util.*;

public class Dijkstra {
    public static double executar(Grafo grafo, int origem) {
        int n = grafo.getNumVertices();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[origem] = 0.0;

        PriorityQueue<Vertice> fila = new PriorityQueue<>(Comparator.comparingDouble(v -> dist[v.getId()]));
        fila.add(new Vertice(origem));

        while (!fila.isEmpty()) {
            Vertice u = fila.poll();
            for (var aresta : grafo.getAdjacentes(u)) {
                Vertice v = aresta.getDestino();
                double peso = aresta.getPeso();
                if (dist[u.getId()] + peso < dist[v.getId()]) {
                    dist[v.getId()] = dist[u.getId()] + peso;
                    fila.add(v);
                }
            }
        }

        return Arrays.stream(dist)
                .filter(d -> d < Double.POSITIVE_INFINITY)
                .sum();
    }
}
