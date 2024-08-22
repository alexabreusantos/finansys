/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.ResponsavelDAO;
import com.marjax.finansys.model.Responsavel;
import com.marjax.finansys.util.ValidationUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ResponsavelCadastrarController implements Initializable {

    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private TextField nomeTextField;

    private ResponsavelDAO responsavelDAO;
    
    public ResponsavelCadastrarController() {
        responsavelDAO = new ResponsavelDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cancelarButton.setOnAction(event -> cancelarCadastro());
        salvarButton.setOnAction(event -> salvarResponsavel());
    }
    
    @FXML
    private void salvarResponsavel() {
        String nome = nomeTextField.getText().trim();
        boolean[] hasError = { false };
        boolean nomePreechido = ValidationUtil.validateNonEmpty(nomeTextField, "Nome", hasError);        
        
        if (nomePreechido && !hasError[0]) {
            // Criar um objeto Responsavel
            Responsavel responsavel = new Responsavel();
            responsavel.setNome(nome);

            // Tentar salvar o responsável
            responsavelDAO.salvar(responsavel);

            ((Stage) salvarButton.getScene().getWindow()).close();
        }
    }

    @FXML
    private void cancelarCadastro() {
        // get a handle to the stage
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
