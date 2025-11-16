package com.example.kpd.service;

import com.example.kpd.model.ResultadoExecucao;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RelatorioPDFService {

    public void gerarPDF(List<ResultadoExecucao> resultados, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=resultado_algoritmos.pdf");

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Título
            Font tituloFonte = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Resultados dos Algoritmos de Grafos (Tabela Estendida)", tituloFonte);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));

            PdfPTable tabela = new PdfPTable(8);
            tabela.setWidthPercentage(100);
            // Largura das colunas: 2 Grafo + 2 CM + 2 AGM + 2 FM
            tabela.setWidths(new float[] { 1, 1, 2, 2, 2, 2, 2, 2 });

            // --- CABEÇALHO PRINCIPAL (AGRUPADO - 8 COLUNAS) ---
            Font principalFonte = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor principalCor = new BaseColor(0, 51, 102);

            adicionarCabecalhoPrincipal(tabela, "Grafo", 2, principalFonte, principalCor);
            adicionarCabecalhoPrincipal(tabela, "CM (Dijkstra)", 2, principalFonte, principalCor);
            adicionarCabecalhoPrincipal(tabela, "AGM (Kruskal/Prim)", 2, principalFonte, principalCor);
            adicionarCabecalhoPrincipal(tabela, "FM (Dijkstra/Kruskal/Prim)", 2, principalFonte, principalCor);

            // --- CABEÇALHO SECUNDÁRIO (8 COLUNAS) ---
            adicionarSubCabecalho(tabela, "N");
            adicionarSubCabecalho(tabela, "M");
            adicionarSubCabecalho(tabela, "Custo");
            adicionarSubCabecalho(tabela, "Tempo (s)");
            adicionarSubCabecalho(tabela, "Custo");
            adicionarSubCabecalho(tabela, "Tempo (s)");
            adicionarSubCabecalho(tabela, "Custo");
            adicionarSubCabecalho(tabela, "Tempo (s)");

            // --- 9 LINHAS DE DADOS ESTRUTURADAS (3 LINHAS POR RESULTADO) ---
            final int ROWSPAN = 3;
            Font dadosFonte = new Font(Font.FontFamily.HELVETICA, 9);

            for (ResultadoExecucao r : resultados) {

                // === LINHA 1/3 ===

                // Colunas 1, 2, 3, 4: N, M, CM Custo, CM Tempo (Rowspan 3)
                adicionarCelula(tabela, String.valueOf(r.getN()), ROWSPAN, dadosFonte);
                adicionarCelula(tabela, String.valueOf(r.getM()), ROWSPAN, dadosFonte);
                adicionarCelula(tabela, String.format("%.2f", r.getCustoDijkstra()), ROWSPAN, dadosFonte);
                adicionarCelula(tabela, String.format("%.6f", r.getTempoDijkstra()), ROWSPAN, dadosFonte);

                // Colunas 5 e 6: AGM (Kruskal)
                adicionarCelula(tabela, String.format("%.2f", r.getCustoKruskal()), 1, dadosFonte);
                adicionarCelula(tabela, String.format("%.6f", r.getTempoKruskal()), 1, dadosFonte);

                // Colunas 7 e 8: FM (Dijkstra)
                adicionarCelula(tabela, String.format("%.2f", r.getCustoDijkstra()), 1, dadosFonte);
                adicionarCelula(tabela, String.format("%.6f", r.getTempoDijkstra()), 1, dadosFonte);

                // === LINHA 2/3 ===

                // Colunas 5 e 6: AGM (Prim)
                adicionarCelula(tabela, String.format("%.2f", r.getCustoPrim()), 1, dadosFonte);
                adicionarCelula(tabela, String.format("%.6f", r.getTempoPrim()), 1, dadosFonte);

                // Colunas 7 e 8: FM (Kruskal)
                adicionarCelula(tabela, String.format("%.2f", r.getCustoKruskal()), 1, dadosFonte);
                adicionarCelula(tabela, String.format("%.6f", r.getTempoKruskal()), 1, dadosFonte);

                // === LINHA 3/3 ===

                // Colunas 5 e 6: AGM (N/A)
                adicionarCelula(tabela, "N/A", 1, dadosFonte);
                adicionarCelula(tabela, "N/A", 1, dadosFonte);

                // Colunas 7 e 8: FM (Prim)
                adicionarCelula(tabela, String.format("%.2f", r.getCustoPrim()), 1, dadosFonte);
                adicionarCelula(tabela, String.format("%.6f", r.getTempoPrim()), 1, dadosFonte);
            }

            // --- ADICIONAR 8 LINHAS DE PLACEHOLDER SOLICITADAS ---
            Font placeholderFonte = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.RED);
            for (int i = 0; i < 8; i++) {
                // 8 células para preencher as 8 colunas em uma linha simples
                adicionarCelula(tabela, "Linha Extra " + (i + 1), 1, placeholderFonte);
                adicionarCelula(tabela, "N/A", 1, placeholderFonte);
                adicionarCelula(tabela, "N/A", 1, placeholderFonte);
                adicionarCelula(tabela, "N/A", 1, placeholderFonte);
                adicionarCelula(tabela, "N/A", 1, placeholderFonte);
                adicionarCelula(tabela, "N/A", 1, placeholderFonte);
                adicionarCelula(tabela, "N/A", 1, placeholderFonte);
                adicionarCelula(tabela, "N/A", 1, placeholderFonte);
            }

            document.add(tabela);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Métodos Auxiliares
    private void adicionarCabecalhoPrincipal(PdfPTable tabela, String texto, int colspan, Font font, BaseColor cor) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(cor);
        cell.setPadding(6f);
        tabela.addCell(cell);
    }

    private void adicionarSubCabecalho(PdfPTable tabela, String texto) {
        Font font = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(0, 102, 204));
        cell.setPadding(5f);
        tabela.addCell(cell);
    }

    private void adicionarCelula(PdfPTable tabela, String texto, int rowspan, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(4f);
        if (rowspan > 1) {
            cell.setRowspan(rowspan);
        }
        tabela.addCell(cell);
    }
}