/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import com.marjax.finansys.model.Cartao;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class LocaleUtil {
    public static void moedaBrasilColuna(TableColumn coluna){
        // Formatando o valor para moeda brasileira
        coluna.setCellFactory(column -> new TableCell<Cartao, Double>() {
            
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });
    }
    
    public static void moedaBrasilTextField(double limite, TextField limiteLabel) {
        // Configura o formato para moeda brasileira
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

        // Formata o valor do limite
        String limiteFormatado = formatoMoeda.format(limite);

        // Define o valor formatado no Label
        limiteLabel.setText(limiteFormatado);
    }
    
    public static void moedaBrasilLabel(double limite, Label limiteLabel) {
        // Configura o formato para moeda brasileira
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

        // Formata o valor do limite
        String limiteFormatado = formatoMoeda.format(limite);

        // Define o valor formatado no Label
        limiteLabel.setText(limiteFormatado);
    }
    
    public static void applyBrazilianCurrencyFormat(TextField textField) {
        Locale locale = Locale.forLanguageTag("pt-BR");
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Se o novo valor estiver vazio, apenas limpe o campo
            if (newValue.isEmpty()) {
                textField.setText("");
                return;
            }

            // Remove qualquer caractere não numérico
            String numericValue = newValue.replaceAll("[^\\d]", "");

            // Verifica se a conversão para double é possível
            double value = 0;
            if (!numericValue.isEmpty()) {
                try {
                    value = Double.parseDouble(numericValue) / 100.0;
                } catch (NumberFormatException e) {
                    // Se ocorrer uma exceção, apenas limpe o campo
                    textField.setText("");
                    return;
                }
            }

            // Atualiza o campo de texto com o valor formatado
            String formattedValue = currencyFormat.format(value);

            // Define o novo texto e corrige a posição do cursor
            textField.setText(formattedValue);
            // Move o cursor para o final do texto
            textField.positionCaret(formattedValue.length());
        });
    }
}
