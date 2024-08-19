/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.MesDAO;
import com.marjax.finansys.model.Mes;
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
public class MesEditarController implements Initializable {

    @FXML
    private Button salvarButton;
      
    @FXML
    private TextField codigoTextField;  
    
    @FXML
    private TextField nomeTextField;
    
    @FXML
    private TextField numeroTextField;
    
    private Mes mes;

    private MesController controller;

    private MesDAO dao;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       // Adiciona um ouvinte para limitar a entrada a 4 dígitos
        numeroTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove todos os caracteres que não são dígitos
            newValue = newValue.replaceAll("[^\\d]", "");

            // Limita o tamanho do texto a 4 caracteres
            if (newValue.length() > 2) {
                newValue = newValue.substring(0, 2);
            }

            numeroTextField.setText(newValue);
        });
        
        salvarButton.setOnAction(event -> salvarEdicao());   
    } 
    
    public void setMes(Mes mes) {
        this.mes = mes;
        codigoTextField.setText(String.valueOf(mes.getCodigo()));
        nomeTextField.setText(mes.getNome());
        numeroTextField.setText(mes.getNumero());
    }

    public void setMesDAO(MesDAO dao) {
        this.dao = dao;
    }

    public void setMesController(MesController controller) {
        this.controller = controller;
    }
    
    @FXML
    private void salvarEdicao() {
        if (mes != null) {
            String novoNome = nomeTextField.getText().trim();
            String novoNumero = numeroTextField.getText().trim();
            String codigoTexto = codigoTextField.getText();

            boolean[] hasError = {false};
            boolean nomePreechido = ValidationUtil.validateNonEmpty(nomeTextField, "Nome", hasError);
            boolean numeroPreechido = ValidationUtil.validateNonEmpty(numeroTextField, "Número", hasError);

            // Verificar se o nome já existe no banco de dados
            if (dao.existe(novoNome) && !novoNome.equals(mes.getNome()) && !novoNumero.equals(mes.getNumero())) {
                AlertUtil.showWarningAlert("Mês já existente", "O mês informado já está em uso.",
                        "Por favor, informe um nome ou um número diferente.");
                return;
            }

            if (nomePreechido && numeroPreechido && !hasError[0]) {
                mes.setNome(novoNome);
                mes.setNumero(novoNumero);
                try {
                    int codigo = Integer.parseInt(codigoTexto);
                    mes.setCodigo(codigo);
                } catch (NumberFormatException e) {
                    AlertUtil.showErrorAlert("Erro", "Código inválido", "O código deve ser um número inteiro.");
                    return;
                }

                // Atualizar o responsável no banco de dados
                boolean success = dao.atualizar(mes);

                if (success) {
                    AlertUtil.showInformationAlert("Sucesso", null, "Categoria atualizada com sucesso.");
                    // Atualize o TableView na janela principal
                    if (controller != null) {
                        controller.atualizarTableView();
                    }
                    fecharJanela();
                } else {
                    AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível atualizar o mês.");
                }
            }
        } else {
            AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível salvar os dados.");
        }
    }
    
    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow();
        stage.close();
    }
}
