package com.example.kpd.Service;

import com.example.kpd.algorithms.Dijkstra;
import com.example.kpd.algorithms.Prim;
import com.example.kpd.algorithms.Kruskal;
import com.example.kpd.model.Grafo;
import com.example.kpd.model.ResultadoExecucao;
import org.springframework.stereotype.Service;

@Service
public class AlgoritmoService {

    public ResultadoExecucao executar(Grafo grafo) {
        long inicio, fim;

        inicio = System.nanoTime();
        double custoDijkstra = Dijkstra.executar(grafo, 0);
        fim = System.nanoTime();
        double tempoDijkstra = (fim - inicio) / 1_000_000_000.0;

        inicio = System.nanoTime();
        double custoPrim = Prim.executar(grafo);
        fim = System.nanoTime();
        double tempoPrim = (fim - inicio) / 1_000_000_000.0;

        inicio = System.nanoTime();
        double custoKruskal = Kruskal.executar(grafo);
        fim = System.nanoTime();
        double tempoKruskal = (fim - inicio) / 1_000_000_000.0;

        return new ResultadoExecucao(
                grafo.getNumVertices(),
                grafo.getArestas().size(),
                custoDijkstra, tempoDijkstra,
                custoPrim, tempoPrim,
                custoKruskal, tempoKruskal
        );
    }
}
