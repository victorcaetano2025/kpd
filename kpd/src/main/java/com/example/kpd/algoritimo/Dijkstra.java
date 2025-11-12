package com.example.kpd.algoritimo;

import java.util.Arrays;

public class Dijkstra {
    private int V;
    private int[][] matriz;
    public int custoTotal = 0;

    public Dijkstra(int vertices) {
        this.V = vertices;
        matriz = new int[V][V];
    }

    public void carregarMatriz(int[][] mat) {
        this.matriz = mat;
    }

    public void dijkstra(int origem) {
        int[] dist = new int[V];
        boolean[] visitado = new boolean[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[origem] = 0;

        for (int count = 0; count < V; count++) {
            int u = -1, minDist = Integer.MAX_VALUE;
            for (int v = 0; v < V; v++) {
                if (!visitado[v] && dist[v] < minDist) {
                    minDist = dist[v];
                    u = v;
                }
            }

            visitado[u] = true;

            for (int v = 0; v < V; v++) {
                if (matriz[u][v] != 0 && !visitado[v] && dist[u] + matriz[u][v] < dist[v]) {
                    dist[v] = dist[u] + matriz[u][v];
                }
            }
        }

        // soma todas as distâncias a partir da origem
        for (int d : dist)
            if (d < Integer.MAX_VALUE)
                custoTotal += d;
    }
}
