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

            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Título
            Font tituloFonte = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Resultados dos Algoritmos de Grafos", tituloFonte);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));

            // ==== TABELA (4 + 8 COLUNAS) ====
            PdfPTable tabela = new PdfPTable(8);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1, 1, 2, 2, 2, 2, 2, 2});

            Font fCab = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor azul = new BaseColor(0, 70, 140);

            // ------ Linha 1: 4 blocos ------
            adicionarGrupo(tabela, "Grafo", 2, fCab, azul);
            adicionarGrupo(tabela, "CM - Dijkstra", 2, fCab, azul);
            adicionarGrupo(tabela, "AGM - Prim", 2, fCab, azul);
            adicionarGrupo(tabela, "AGM - Kruskal", 2, fCab, azul);

            // ------ Linha 2: 8 colunas ------
            adicionarSub(tabela, "N");
            adicionarSub(tabela, "M");
            adicionarSub(tabela, "Custo");
            adicionarSub(tabela, "Tempo(s)");
            adicionarSub(tabela, "Custo");
            adicionarSub(tabela, "Tempo(s)");
            adicionarSub(tabela, "Custo");
            adicionarSub(tabela, "Tempo(s)");

            // ------ DADOS ------
            Font fDados = new Font(Font.FontFamily.HELVETICA, 9);

            for (ResultadoExecucao r : resultados) {
                adicionar(tabela, String.valueOf(r.getN()), fDados);
                adicionar(tabela, String.valueOf(r.getM()), fDados);

                adicionar(tabela, format(r.getCustoDijkstra()), fDados);
                adicionar(tabela, format(r.getTempoDijkstra()), fDados);

                adicionar(tabela, format(r.getCustoPrim()), fDados);
                adicionar(tabela, format(r.getTempoPrim()), fDados);

                adicionar(tabela, format(r.getCustoKruskal()), fDados);
                adicionar(tabela, format(r.getTempoKruskal()), fDados);
            }

            document.add(tabela);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionarGrupo(PdfPTable t, String txt, int colspan, Font f, BaseColor cor) {
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setColspan(colspan);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setBackgroundColor(cor);
        c.setPadding(5f);
        t.addCell(c);
    }

    private void adicionarSub(PdfPTable t, String txt) {
        Font f = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setBackgroundColor(new BaseColor(0, 100, 200));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setPadding(4f);
        t.addCell(c);
    }

    private void adicionar(PdfPTable t, String txt, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(txt, f));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setPadding(4f);
        t.addCell(c);
    }

    private String format(double v) {
        return String.format("%.6f", v);
    }
}