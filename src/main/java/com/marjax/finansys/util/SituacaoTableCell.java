/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import com.marjax.finansys.dao.CompraDAO;
import com.marjax.finansys.model.Compra;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class SituacaoTableCell extends TableCell<Compra, String> {

    private final CheckBox checkBox;
    private CompraDAO compraDAO; // Adicione sua DAO aqui

    public SituacaoTableCell(CompraDAO compraDAO) {
        this.compraDAO = compraDAO;
        this.checkBox = new CheckBox();

        // Adiciona um listener para o CheckBox
        this.checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (getTableRow() != null && getTableRow().getItem() != null) {
                Compra compra = getTableRow().getItem();
                // Atualiza a situação com base no estado do CheckBox
                compra.setSituacao(newValue ? "Paga" : "Pendente");

                try {
                    // Atualiza no banco de dados usando o código da compra
                    compraDAO.atualizarSituacao(compra);
                } catch (Exception e) {
                    // Aqui você pode adicionar lógica para exibir uma mensagem de erro para o usuário
                }
            }
        });

        setGraphic(checkBox);
    }

    @Override
    protected void updateItem(String situacao, boolean empty) {
        super.updateItem(situacao, empty);

        if (empty || situacao == null) {
            setGraphic(null);
        } else {
            // Define o estado do CheckBox com base na situação
            checkBox.setSelected(situacao.equalsIgnoreCase("paga"));
            setGraphic(checkBox);
        }
    }
}