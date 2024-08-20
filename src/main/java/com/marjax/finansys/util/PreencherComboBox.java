/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import com.marjax.finansys.dao.AnoDAO;
import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.dao.MesDAO;
import com.marjax.finansys.model.Ano;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Mes;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

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

    public static void ComboBoxMeses(ComboBox<Mes> mesComboBox) {
        MesDAO dao = new MesDAO();
        mesComboBox.getItems().clear();
        mesComboBox.getItems().addAll(dao.buscarNomesMeses());
        
        // Define um CellFactory para exibir o nome do mês no ComboBox
        mesComboBox.setCellFactory(lv -> new ListCell<Mes>() {
            @Override
            protected void updateItem(Mes item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        // Define como o nome do mês deve ser exibido quando o item é selecionado
        mesComboBox.setButtonCell(new ListCell<Mes>() {
            @Override
            protected void updateItem(Mes item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
    }

    public static void ComboBoxAnos(ComboBox<Ano> anoComboBox) {
        AnoDAO dao = new AnoDAO();
        anoComboBox.getItems().clear();
        anoComboBox.getItems().addAll(dao.buscarAnos());
        
        // Define um CellFactory para exibir o nome do mês no ComboBox
        anoComboBox.setCellFactory(lv -> new ListCell<Ano>() {
            @Override
            protected void updateItem(Ano item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getValor());
            }
        });

        // Define como o nome do mês deve ser exibido quando o item é selecionado
        anoComboBox.setButtonCell(new ListCell<Ano>() {
            @Override
            protected void updateItem(Ano item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getValor());
            }
        });

    }

    public static void ComboBoxCartoes(ComboBox<Cartao> cartaoComboBox) {
        CartaoDAO dao = new CartaoDAO();
        cartaoComboBox.getItems().clear();
        cartaoComboBox.getItems().addAll(dao.buscarCartoes());
        
        // Define um CellFactory para exibir o nome do mês no ComboBox
        cartaoComboBox.setCellFactory(lv -> new ListCell<Cartao>() {
            @Override
            protected void updateItem(Cartao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        // Define como o nome do mês deve ser exibido quando o item é selecionado
        cartaoComboBox.setButtonCell(new ListCell<Cartao>() {
            @Override
            protected void updateItem(Cartao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
    }
}
