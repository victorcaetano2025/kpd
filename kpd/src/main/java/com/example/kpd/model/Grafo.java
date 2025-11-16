package com.example.kpd.model;

import java.util.*;

public class Grafo {
    private int numVertices;
    private boolean direcionado;
    private List<Aresta> arestas;
    private List<List<Aresta>> adj;

    public Grafo(int n, boolean direcionado) {
        this.numVertices = n;
        this.direcionado = direcionado;
        this.arestas = new ArrayList<>();
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void adicionarAresta(int u, int v, double peso) {
        Vertice origem = new Vertice(u);
        Vertice destino = new Vertice(v);
        Aresta a = new Aresta(origem, destino, peso);
        arestas.add(a);
        adj.get(u).add(a);
        if (!direcionado) {
            adj.get(v).add(new Aresta(destino, origem, peso));
        }
    }

    public int getNumVertices() { return numVertices; }
    public List<Aresta> getArestas() { return arestas; }
    public List<Aresta> getAdjacentes(int u) { return adj.get(u); }
    public List<Aresta> getAdjacentes(Vertice v) { return adj.get(v.getId()); }
}
