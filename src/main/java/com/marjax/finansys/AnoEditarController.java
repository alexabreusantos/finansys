/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.AnoDAO;
import com.marjax.finansys.model.Ano;
import com.marjax.finansys.util.AlertUtil;
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
public class AnoEditarController implements Initializable {

    @FXML
    private Button salvarButton;
    
    @FXML
    private TextField codigoTextField;

    @FXML
    private TextField anoTextField;

    private AnoDAO dao;
    private Ano ano;
  
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
        
        salvarButton.setOnAction(event -> salvarEdicao());        
    }

    public void setAno(Ano ano) {
        this.ano = ano;
        codigoTextField.setText(String.valueOf(ano.getCodigo()));
        anoTextField.setText(ano.getValor());
    }
    
    @FXML
    private void salvarEdicao() {
        if (ano != null) {
            String novoNome = anoTextField.getText().trim();
            String codigoTexto = codigoTextField.getText();

            boolean[] hasError = {false};
            boolean nomePreechido = ValidationUtil.validateNonEmpty(anoTextField, "Ano", hasError);

            // Verificar se o nome já existe no banco de dados
            if (dao.existe(novoNome) && !novoNome.equals(ano.getValor())) {
                AlertUtil.showWarningAlert("Ano já existente", "O ano informado já está em uso.",
                        "Por favor, informe um ano diferente.");
                return;
            }

            if (nomePreechido && !hasError[0]) {
                ano.setValor(novoNome);
                try {
                    int codigo = Integer.parseInt(codigoTexto);
                    ano.setCodigo(codigo);
                } catch (NumberFormatException e) {
                    AlertUtil.showErrorAlert("Erro", "Código inválido", "O código deve ser um número inteiro.");
                    return;
                }

                // Atualizar o responsável no banco de dados
                boolean success = dao.atualizar(ano);

                if (success) {
                    AlertUtil.showInformationAlert("Sucesso", null, "Categoria atualizada com sucesso.");                    
                    ((Stage) salvarButton.getScene().getWindow()).close();//fecha janela
                } else {
                    AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível atualizar o ano.");
                }
            }
        } else {
            AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível salvar os dados.");
        }
    }    
}
