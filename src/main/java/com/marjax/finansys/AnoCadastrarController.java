/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.AnoDAO;
import com.marjax.finansys.model.Ano;
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
public class AnoCadastrarController implements Initializable {

    @FXML
    private Button salvarButton;

    @FXML
    private TextField anoTextField;

    private AnoDAO dao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dao = new AnoDAO();
        anoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove todos os caracteres que não são dígitos
            newValue = newValue.replaceAll("[^\\d]", "");

            // Limita o tamanho do texto a 4 caracteres
            if (newValue.length() > 4) {
                newValue = newValue.substring(0, 4);
            }

            anoTextField.setText(newValue);
        });
        salvarButton.setOnAction(event -> salvar());
    }
   
    @FXML
    private void salvar() {
        String valor = anoTextField.getText().trim();
        boolean[] hasError = {false};
        boolean anoPreechido = ValidationUtil.validateNonEmpty(anoTextField, "Ano", hasError);

        if (anoPreechido && !hasError[0]) {

            // Criar um objeto Responsavel
            Ano ano = new Ano();
            ano.setValor(valor);
            // Tentar salvar o responsável
            dao.salvar(ano);
            
            ((Stage) salvarButton.getScene().getWindow()).close();
        }
    }
}
