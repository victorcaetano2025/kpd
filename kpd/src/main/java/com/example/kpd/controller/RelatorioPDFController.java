package com.example.kpd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kpd.model.Grafo;
import com.example.kpd.model.ResultadoExecucao;
import com.example.kpd.service.AlgoritmoService;
import com.example.kpd.service.RelatorioPDFService;
import com.example.kpd.utils.GeradorGrafoAleatorio;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class RelatorioPDFController {

    @Autowired
    private AlgoritmoService algoritmoService;

    @Autowired
    private RelatorioPDFService relatorioPDFService;

    @GetMapping("/pdf/gerar")
    public void gerarPDF(HttpServletResponse response) {
        List<ResultadoExecucao> resultados = new ArrayList<>();

        // --- Instância 1: Nova York (NY) ---
        Grafo grafoNY = GeradorGrafoAleatorio.gerarGrafo(264346, 733846, false);
        ResultadoExecucao resultadoNY = algoritmoService.executar(grafoNY);
        resultadoNY.setNomeInstancia("Nova York");
        resultados.add(resultadoNY);

        // --- Instância 2: San Francisco (BAY) ---
        Grafo grafoBAY = GeradorGrafoAleatorio.gerarGrafo(321270, 800172, false);
        ResultadoExecucao resultadoBAY = algoritmoService.executar(grafoBAY);
        resultadoBAY.setNomeInstancia("San Francisco");
        resultados.add(resultadoBAY);

        // --- Instância 3: Colorado (COL) ---
        Grafo grafoCOL = GeradorGrafoAleatorio.gerarGrafo(435666, 1057066, false);
        ResultadoExecucao resultadoCOL = algoritmoService.executar(grafoCOL);
        resultadoCOL.setNomeInstancia("Colorado");
        resultados.add(resultadoCOL);

        relatorioPDFService.gerarPDF(resultados, response);
    }
}
