package com.example.kpd.algoritimo;

import java.util.Arrays;

public class Prim {
    private int V;
    private int[][] matriz;
    public int custoTotalMST = 0;

    public Prim(int vertices) {
        this.V = vertices;
        matriz = new int[V][V];
    }

    public void carregarMatriz(int[][] mat) {
        this.matriz = mat;
    }

    public void primMST() {
        boolean[] mstSet = new boolean[V];
        int[] key = new int[V];
        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;

        for (int count = 0; count < V; count++) {
            int u = -1;
            int minKey = Integer.MAX_VALUE;
            for (int v = 0; v < V; v++) {
                if (!mstSet[v] && key[v] < minKey) {
                    minKey = key[v];
                    u = v;
                }
            }

            mstSet[u] = true;
            custoTotalMST += key[u];

            for (int v = 0; v < V; v++) {
                if (matriz[u][v] != 0 && !mstSet[v] && matriz[u][v] < key[v])
                    key[v] = matriz[u][v];
            }
        }
    }
}
