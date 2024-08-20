/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import com.marjax.finansys.dao.MesDAO;
import com.marjax.finansys.model.Ano;
import com.marjax.finansys.model.Mes;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ConverterTimeStamp {

    public static Timestamp formatarDataParaTimestamp(ComboBox<Mes> mesComboBox, ComboBox<Ano> anoComboBox) {
        MesDAO dao = new MesDAO();
        Mes mesSelecionado = mesComboBox.getSelectionModel().getSelectedItem();
        Ano anoSelecionado = anoComboBox.getSelectionModel().getSelectedItem();

        if (mesSelecionado == null) {
            AlertUtil.showErrorAlert("Erro", "Mês Inválido", "Por favor, selecione um mês válido.");
            return null;
        }

        String mesNumero = mesSelecionado.getNumero();
        String anoValor = anoSelecionado.getValor();

        if (mesNumero == null || mesNumero.isEmpty()) {
            AlertUtil.showErrorAlert("Erro", "Mês Inválido", "O número do mês não foi encontrado.");            
            return null;
        }

        String dataStr = anoValor + "-" + mesNumero + "-01 00:00:00";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date parsedDate = dateFormat.parse(dataStr);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Erro", "Data Inválida", "Erro ao formatar a data: " + dataStr);              
            return null;
        }
    }
}
