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

            Document document = new Document(PageSize.A4, 50,50,50,50);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Título
            Font tituloFonte = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Resultados dos Algoritmos de Grafos", tituloFonte);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));

            PdfPTable tabela = new PdfPTable(8);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1,1,2,2,2,2,2,2});

            // Cabeçalho
            adicionarCabecalho(tabela, "N");
            adicionarCabecalho(tabela, "M");
            adicionarCabecalho(tabela, "Custo Dijkstra");
            adicionarCabecalho(tabela, "Tempo Dijkstra");
            adicionarCabecalho(tabela, "Custo Prim");
            adicionarCabecalho(tabela, "Tempo Prim");
            adicionarCabecalho(tabela, "Custo Kruskal");
            adicionarCabecalho(tabela, "Tempo Kruskal");

            // Linhas
            for (ResultadoExecucao r : resultados) {
                adicionarCelula(tabela, String.valueOf(r.getN()));
                adicionarCelula(tabela, String.valueOf(r.getM()));
                adicionarCelula(tabela, String.format("%.2f", r.getCustoDijkstra()));
                adicionarCelula(tabela, String.format("%.6f", r.getTempoDijkstra()));
                adicionarCelula(tabela, String.format("%.2f", r.getCustoPrim()));
                adicionarCelula(tabela, String.format("%.6f", r.getTempoPrim()));
                adicionarCelula(tabela, String.format("%.2f", r.getCustoKruskal()));
                adicionarCelula(tabela, String.format("%.6f", r.getTempoKruskal()));
            }

            document.add(tabela);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionarCabecalho(PdfPTable tabela, String texto) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(0, 102, 204));
        cell.setPadding(6f);
        tabela.addCell(cell);
    }

    private void adicionarCelula(PdfPTable tabela, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.FontFamily.HELVETICA, 11)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(4f);
        tabela.addCell(cell);
    }
}
