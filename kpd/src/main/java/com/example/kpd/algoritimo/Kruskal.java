package main.java.com.example.kpd.algoritimo;
//evoluir e adapatar

public class Kruskal {
    import java.util.Arrays;

// Classe auxiliar para o Union-Find
class Subconjunto {
    int pai, rank;
}

public class Grafo {
    
    int V, E; // Número de vértices (V) e número de arestas (E)
    Aresta[] arestas; // Array de todas as arestas do grafo

    // Construtor
    public Grafo(int v, int e) {
        V = v;
        E = e;
        arestas = new Aresta[e];
    }

    // --- Estrutura Union-Find ---

    // Função find: Encontra o pai (raiz) do elemento i
    // Usa Path Compression para otimização
    int find(Subconjunto[] subconjuntos, int i) {
        // Se 'i' não for a raiz, faz a compressão do caminho
        if (subconjuntos[i].pai != i)
            subconjuntos[i].pai = find(subconjuntos, subconjuntos[i].pai);

        return subconjuntos[i].pai;
    }

    // Função union: Une dois conjuntos (as raízes x e y)
    // Usa Union by Rank para otimização
    void union(Subconjunto[] subconjuntos, int x, int y) {
        int raizX = find(subconjuntos, x);
        int raizY = find(subconjuntos, y);

        // Anexa a árvore de menor rank à árvore de rank mais alto
        if (subconjuntos[raizX].rank < subconjuntos[raizY].rank)
            subconjuntos[raizX].pai = raizY;
        else if (subconjuntos[raizX].rank > subconjuntos[raizY].rank)
            subconjuntos[raizY].pai = raizX;
        else {
            // Se os ranks forem iguais, escolhe um como pai e incrementa seu rank
            subconjuntos[raizY].pai = raizX;
            subconjuntos[raizX].rank++;
        }
    }

    // --- Algoritmo de Kruskal ---

    void kruskalMST() {
        Aresta[] resultado = new Aresta[V]; // Array para armazenar a MST final
        int e = 0; // Contador/Índice de arestas na MST (deve chegar a V-1)
        int i = 0; // Índice para percorrer todas as arestas ordenadas
        
        // 1. Classificar todas as arestas em ordem crescente de peso
        Arrays.sort(arestas);
        
        // 2. Inicializar V subconjuntos (um para cada vértice)
        Subconjunto[] subconjuntos = new Subconjunto[V];
        for (int v = 0; v < V; ++v) {
            subconjuntos[v] = new Subconjunto();
            subconjuntos[v].pai = v;  // Cada vértice é seu próprio pai inicialmente
            subconjuntos[v].rank = 0;
        }

        // 3. Processo principal:
        // Continua enquanto a MST não estiver completa (e < V - 1) 
        // E houver arestas para inspecionar (i < E)
        while (e < V - 1 && i < E) {
            Aresta proxAresta = arestas[i++]; // Pega a próxima aresta de menor peso
            
            // Encontra as raízes (representantes) dos conjuntos de origem e destino
            int x = find(subconjuntos, proxAresta.origem);
            int y = find(subconjuntos, proxAresta.destino);

            // Verifica Ciclo: Se as raízes são diferentes, ADICIONA a aresta
            if (x != y) {
                resultado[e++] = proxAresta; // Adiciona à MST
                union(subconjuntos, x, y);   // Une os conjuntos
            }
            // Se x == y, a aresta forma um ciclo e é ignorada
        }

        // 4. Imprimir o resultado e calcular o custo total
        System.out.println("--- Árvore Geradora Mínima (MST) ---");
        System.out.println("Arestas da árvore geradora mínima:");
        int custoTotal = 0;
        for (i = 0; i < e; ++i) {
            System.out.println(resultado[i].origem + " -- " +
                               resultado[i].destino + " == " + resultado[i].peso);
            custoTotal += resultado[i].peso;
        }
        System.out.println("\nCusto Total da MST: " + custoTotal);
    }
}
}
