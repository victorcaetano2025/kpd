package com.example.kpd.model;

public class ResultadoExecucao {
    private int n;
    private int m;

    private double custoDijkstra;
    private double tempoDijkstra;

    private double custoPrim;
    private double tempoPrim;

    private double custoKruskal;
    private double tempoKruskal;

    private String nomeInstancia;

    public ResultadoExecucao(int n, int m,
            double custoDijkstra, double tempoDijkstra,
            double custoPrim, double tempoPrim,
            double custoKruskal, double tempoKruskal) {
        this.n = n;
        this.m = m;
        this.custoDijkstra = custoDijkstra;
        this.tempoDijkstra = tempoDijkstra;
        this.custoPrim = custoPrim;
        this.tempoPrim = tempoPrim;
        this.custoKruskal = custoKruskal;
        this.tempoKruskal = tempoKruskal;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public double getCustoDijkstra() {
        return custoDijkstra;
    }

    public double getTempoDijkstra() {
        return tempoDijkstra;
    }

    public double getCustoPrim() {
        return custoPrim;
    }

    public double getTempoPrim() {
        return tempoPrim;
    }

    public double getCustoKruskal() {
        return custoKruskal;
    }

    public double getTempoKruskal() {
        return tempoKruskal;
    }

    public String getNomeInstancia() {
        return nomeInstancia;
    }

    public void setNomeInstancia(String nomeInstancia) {
        this.nomeInstancia = nomeInstancia;
    }
}
