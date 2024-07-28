/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.model.Responsavel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
public class CadastrarResponsavelController implements Initializable {

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;
    
    @FXML
    private TextField txtNome;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCancelar.setOnAction(event -> cancelAction());
    }
    
    /*@FXML
    private void salvarResponsavel() {
        String nome = txtNome.getText();        
        Responsavel responsavel = new Responsavel();
        user.setName(nameField.getText());
        user.setEmail(emailField.getText());

        Set<ConstraintViolation<User>> violations = userController.validateUser(user);

        if (violations.isEmpty()) {
            validationMessage.setText("Usuário válido!");
        } else {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<User> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            validationMessage.setText(sb.toString());
        }
    }*/

    @FXML
    private void cancelAction() {
        // get a handle to the stage
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
