/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ConverterDate {

    public static Date formatarDataParaDate(ComboBox<String> mesComboBox, ComboBox<Integer> anoComboBox, int dia) {
        
        String mesSelecionado = mesComboBox.getValue().toString();
        Integer anoSelecionado = anoComboBox.getSelectionModel().getSelectedItem();

        Integer mesNumero = obterNumeroMes(mesSelecionado);
        
        if (mesSelecionado == null) {
            AlertUtil.showErrorAlert("Erro", "Mês Inválido", "Por favor, selecione um mês válido.");
            return null;
        }
        
        if (mesNumero == null) {
            AlertUtil.showErrorAlert("Erro", "Mês Inválido", "O número do mês não foi encontrado.");            
            return null;
        }

        String dataStr = anoSelecionado + "-" + mesNumero + "-" + dia;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date parsedDate = dateFormat.parse(dataStr);
            return new Date(parsedDate.getTime());
        } catch (ParseException e) {
            AlertUtil.showErrorAlert("Erro", "Data Inválida", "Erro ao formatar a data: " + dataStr);              
            return null;
        }
    }
    
    private static Integer obterNumeroMes(String nomeMes) {
        // Usando switch para mapear o nome do mês para o número do mês
        switch (nomeMes) {
            case "Janeiro": return 1;
            case "Fevereiro": return 2;
            case "Março": return 3;
            case "Abril": return 4;
            case "Maio": return 5;
            case "Junho": return 6;
            case "Julho": return 7;
            case "Agosto": return 8;
            case "Setembro": return 9;
            case "Outubro": return 10;
            case "Novembro": return 11;
            case "Dezembro": return 12;
            default: return null; // Ou você pode lançar uma exceção se preferir
        }
    }
    
    public static String nomeMes(Date periodo){
        // Converter Timestamp para LocalDateTime         
        LocalDate data = periodo.toLocalDate();
        Locale locale = Locale.forLanguageTag("pt-BR");
        // Extrair o mês e o ano
        String mesNome = data.getMonth().getDisplayName(TextStyle.FULL, locale);
        String nomeMesFormatado = mesNome.substring(0, 1).toUpperCase() + mesNome.substring(1);
        
        return nomeMesFormatado;
    }   
}
