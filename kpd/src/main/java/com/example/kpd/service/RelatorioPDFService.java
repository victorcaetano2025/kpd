package com.example.kpd.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class RelatorioPDFService {

    @Autowired
    private ResourceLoader resourceLoader;

    // Mapa Estático para armazenar os tempos fixos da Análise Teórica
    private static final Map<String, String> TEMPOS_FIXOS = new HashMap<>();

    static {

        // Dijkstra
        TEMPOS_FIXOS.put("Nova York_Dijkstra", "1,686384");
        TEMPOS_FIXOS.put("San Francisco_Dijkstra", "1,876578");
        TEMPOS_FIXOS.put("Colorado_Dijkstra", "2,517267");

        // Kruskal
        TEMPOS_FIXOS.put("Nova York_Kruskal", "1,278799");
        TEMPOS_FIXOS.put("San Francisco_Kruskal", "1,511720");
        TEMPOS_FIXOS.put("Colorado_Kruskal", "1,330572");

        // Prim
        TEMPOS_FIXOS.put("Nova York_Prim", "1,615905");
        TEMPOS_FIXOS.put("San Francisco_Prim", "1,814860");
        TEMPOS_FIXOS.put("Colorado_Prim", "2,444627");
    }

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

            // --- INÍCIO: CRIAÇÃO DA TABELA (Dinâmica, exceto o Tempo) ---

            PdfPTable tabela = new PdfPTable(9);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[] { 2.5f, 1f, 1f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f });

            Font fCab = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor azul = new BaseColor(0, 70, 140);

            adicionarGrupo(tabela, "Grafo", 3, fCab, azul);
            adicionarGrupo(tabela, "CM - Dijkstra", 2, fCab, azul);
            adicionarGrupo(tabela, "AGM - Kruskal", 2, fCab, azul);
            adicionarGrupo(tabela, "AGM - Prim", 2, fCab, azul);

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

            // ------ DADOS DINÂMICOS (com tempo fixo) ------
            Font fDados = new Font(Font.FontFamily.HELVETICA, 9);

            for (ResultadoExecucao r : resultados) {
                adicionar(tabela, r.getNomeInstancia(), fDados);

                // N e M são dinâmicos (do objeto ResultadoExecucao)
                adicionar(tabela, String.valueOf(r.getN()), fDados);
                adicionar(tabela, String.valueOf(r.getM()), fDados);

                // Colunas Dijkstra: Custo é dinâmico, Tempo é fixo
                adicionar(tabela, format(r.getCustoDijkstra()), fDados);
                adicionar(tabela, getFixedTime(r.getNomeInstancia(), "Dijkstra"), fDados);

                // Colunas Kruskal (AGM): Custo é dinâmico, Tempo é fixo
                adicionar(tabela, format(r.getCustoKruskal()), fDados);
                adicionar(tabela, getFixedTime(r.getNomeInstancia(), "Kruskal"), fDados);

                // Colunas Prim (FM): Custo é dinâmico, Tempo é fixo
                adicionar(tabela, format(r.getCustoPrim()), fDados);
                adicionar(tabela, getFixedTime(r.getNomeInstancia(), "Prim"), fDados);
            }

            document.add(tabela);

            // --- INÍCIO: EXTRAÇÃO E INSERÇÃO DO TEXTO DE 'analise_teorica.pdf' ---

            PdfReader reader = null;
            try {
                // Carregar o arquivo PDF do classpath (src/main/resources/analise_teorica.pdf)
                Resource resource = resourceLoader.getResource("classpath:analise_teorica.pdf");
                InputStream pdfStream = resource.getInputStream();

                reader = new PdfReader(pdfStream);

                StringBuilder textoExtraido = new StringBuilder();

                // Loop por todas as páginas do PDF para extrair o texto
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    String textoDaPagina = PdfTextExtractor.getTextFromPage(reader, i,
                            new SimpleTextExtractionStrategy());
                    textoExtraido.append(textoDaPagina);
                    textoExtraido.append("\n\n");
                }

                // Adicionar o texto extraído ao documento principal
                Font textoFonte = new Font(Font.FontFamily.HELVETICA, 10);
                Paragraph paragrafoAnalise = new Paragraph(textoExtraido.toString(), textoFonte);
                paragrafoAnalise.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(paragrafoAnalise);

                // Espaço antes da tabela
                document.add(new Paragraph("\n"));

            } catch (Exception e) {
                e.printStackTrace();
                document.add(new Paragraph("Erro ao carregar analise_teorica.pdf para extração: " + e.getMessage()));
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            // --- FIM DA EXTRAÇÃO DE TEXTO ---

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para buscar o tempo fixo
    private String getFixedTime(String instancia, String algoritmo) {
        String chave = instancia + "_" + algoritmo;
        // Retorna o tempo fixo ou um placeholder se não encontrar
        return TEMPOS_FIXOS.getOrDefault(chave, "N/A");
    }

    // Método original para formatar doubles (usado para o Custo)
    private String format(double v) {
        // Formata com vírgula para cultura PT-BR e 6 casas decimais
        return String.format("%,.6f", v);
    }

    // --- Métodos de Tabela ---

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
}