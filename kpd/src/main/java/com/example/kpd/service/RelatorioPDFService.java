package com.example.kpd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.kpd.model.ResultadoExecucao;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class RelatorioPDFService {

    public void gerarPDF(List<ResultadoExecucao> resultados, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=resultado_algoritmos.pdf");

            Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Título
            Font tituloFonte = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Resultados dos Algoritmos de Grafos", tituloFonte);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));

            PdfPTable tabela = new PdfPTable(9);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[] { 2.5f, 1f, 1f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f });

            Font fCab = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor azul = new BaseColor(0, 70, 140);

            adicionarGrupo(tabela, "Grafo", 3, fCab, azul);
            adicionarGrupo(tabela, "CM - Dijkstra", 2, fCab, azul);
            adicionarGrupo(tabela, "AGM - Kruskal", 2, fCab, azul);
            adicionarGrupo(tabela, "FM - Prim", 2, fCab, azul);

            // ------ Linha 2: 9 sub-cabeçalhos ------
            adicionarSub(tabela, "Instância");
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
                adicionar(tabela, r.getNomeInstancia(), fDados);

                adicionar(tabela, String.valueOf(r.getN()), fDados);
                adicionar(tabela, String.valueOf(r.getM()), fDados);

                // Colunas Dijkstra
                adicionar(tabela, format(r.getCustoDijkstra()), fDados);
                adicionar(tabela, format(r.getTempoDijkstra()), fDados);

                // Colunas Kruskal (AGM)
                adicionar(tabela, format(r.getCustoKruskal()), fDados);
                adicionar(tabela, format(r.getTempoKruskal()), fDados);

                // Colunas Prim (FM)
                adicionar(tabela, format(r.getCustoPrim()), fDados);
                adicionar(tabela, format(r.getTempoPrim()), fDados);
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
        // Formata com vírgula para cultura PT-BR e 6 casas decimais
        return String.format("%,.6f", v);
    }
}