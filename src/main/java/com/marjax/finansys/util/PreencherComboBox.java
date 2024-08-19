/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.dao.MesDAO;
import java.time.Month;
import java.time.Year;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class PreencherComboBox {

    public static void ComboBoxDias(ComboBox dias) {
        // Preenche o ComboBox com valores de 1 a 31
        for (int i = 1; i <= 31; i++) {
            dias.getItems().add(i);
        }
    }

    public static void ComboBoxMeses(ComboBox mesComboBox) {
        MesDAO dao = new MesDAO();
        mesComboBox.getItems().clear();
        mesComboBox.getItems().addAll(dao.buscarNomesMeses());
    }
    
    public static void ComboBoxAnos(ComboBox anoComboBox) {
        ObservableList<Integer> anos = FXCollections.observableArrayList();

        // Ano atual
        int anoAtual = Year.now().getValue();

        // Preenche o ComboBox com os próximos 100 anos
        for (int i = 0; i <= 100; i++) {
            anos.add(anoAtual + i);
        }

        anoComboBox.setItems(anos);

        // Seleciona o ano atual
        anoComboBox.setValue(anoAtual);
    }

    public static void ComboBoxNomesCartoes(ComboBox nomesCartoes) {
        CartaoDAO cartaoDAO = new CartaoDAO();
        nomesCartoes.getItems().clear();
        nomesCartoes.getItems().addAll(cartaoDAO.buscarNomesCartoes());
    }
}
