package com.example.kpd.utils;

import com.example.kpd.model.Grafo;
import java.util.*;

public class GeradorGrafoAleatorio {

    public static Grafo gerarGrafo(int n, int m, boolean direcionado) {
        Grafo grafo = new Grafo(n, direcionado);
        Random random = new Random();
        Set<String> arestasCriadas = new HashSet<>();

        while (grafo.getArestas().size() < m) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u == v) continue;
            String id = u + "-" + v;
            if (arestasCriadas.contains(id)) continue;
            double peso = 1 + random.nextInt(100);
            grafo.adicionarAresta(u, v, peso);
            arestasCriadas.add(id);
        }

        return grafo;
    }
}
