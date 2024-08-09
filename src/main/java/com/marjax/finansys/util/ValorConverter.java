/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import javafx.scene.control.TextField;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ValorConverter {

    public Double valor(TextField valor) {
        String valorBrasileiro = valor.getText();

        // Remove o prefixo "R$" e quaisquer espaços extras
        String valorLimpo = valorBrasileiro.replace("R$", "").trim();

        // Remove qualquer outro caractere não numérico, exceto a vírgula
        valorLimpo = valorLimpo.replaceAll("[^\\d,]", "");

        // Substitui a vírgula por ponto
        valorLimpo = valorLimpo.replace(",", ".");

        // Converte para Double
        Double valorParaMySQL = null;
        try {
            valorParaMySQL = Double.parseDouble(valorLimpo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return valorParaMySQL;
    }
}
