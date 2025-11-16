package com.example.kpd.controller;

import com.example.kpd.model.Grafo;
import com.example.kpd.model.ResultadoExecucao;
import com.example.kpd.service.AlgoritmoService;
import com.example.kpd.service.RelatorioPDFService;
import com.example.kpd.utils.GeradorGrafoAleatorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class RelatorioPDFController {

    @Autowired
    private AlgoritmoService algoritmoService;

    @Autowired
    private RelatorioPDFService relatorioPDFService;

    @GetMapping("/pdf/gerar")
    public void gerarPDF(HttpServletResponse response) {
        // Gerar grafo aleatório de teste
        Grafo grafo = GeradorGrafoAleatorio.gerarGrafo(5, 7, false);

        ResultadoExecucao resultado = algoritmoService.executar(grafo);
        List<ResultadoExecucao> resultados = List.of(resultado);

        relatorioPDFService.gerarPDF(resultados, response);
    }
}
