/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.MesDAO;
import com.marjax.finansys.model.Mes;
import com.marjax.finansys.util.ValidationUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class MesCadastrarController implements Initializable {

    @FXML
    private Button salvarButton;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField numeroTextField;

    private MesDAO dao;

    private MesController controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        salvarButton.setOnAction(event -> salvar());
        // Adiciona um ouvinte para limitar a entrada a 4 dígitos
        numeroTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove todos os caracteres que não são dígitos
            newValue = newValue.replaceAll("[^\\d]", "");

            // Limita o tamanho do texto a 2 caracteres
            if (newValue.length() > 2) {
                newValue = newValue.substring(0, 2);
            }

            numeroTextField.setText(newValue);
        });
    }

    public void setMesDAO(MesDAO dao) {
        this.dao = dao;
    }

    public void setMesController(MesController controller) {
        this.controller = controller;
    }

    @FXML
    private void salvar() {
        boolean[] hasError = {false};

        // Valida o nome primeiro
        boolean validaNome = ValidationUtil.validateNonEmpty(nomeTextField, "Nome", hasError);
        if (!validaNome) {
            return; // Interrompe a validação se o nome for inválido
        }

        // Valida o numero
        boolean validaNumero = ValidationUtil.validateNonEmpty(numeroTextField, "Número", hasError);
        if (!validaNumero) {
            return; // Interrompe a validação se o nome for inválido
        }
        
        // Valida o numero
        boolean validaQuantidadeNumero = ValidationUtil.validateSizeTextMes(numeroTextField, "Número", hasError);
        if (!validaQuantidadeNumero) {
            return; // Interrompe a validação se o nome for inválido
        }

        if (!hasError[0]) {

            // Criar um objeto Mes
            Mes mes = new Mes();
            mes.setNome(nomeTextField.getText().trim());
            mes.setNumero(numeroTextField.getText().trim());
            // Tentar salvar o mes
            dao.salvar(mes);

            // Atualize o TableView na janela principal
            if (controller != null) {
                controller.atualizarTableView();
                controller.atualizarTotal();
            }
            ((Stage) salvarButton.getScene().getWindow()).close();
        }
    }
}
