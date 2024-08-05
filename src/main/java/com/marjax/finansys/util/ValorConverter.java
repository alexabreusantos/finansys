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

        // Remove o prefixo "R$" e os espaços
        String valorLimpo = valorBrasileiro.replace(".", "").replace(",", ".");

        // Converte para Double
        double valorParaMySQL = Double.parseDouble(valorLimpo);  
        
        return valorParaMySQL;
    }
}
