/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.ResponsavelDAO;
import com.marjax.finansys.model.Responsavel;
import com.marjax.finansys.util.AlertUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ResponsavelEditarController implements Initializable {

    @FXML
    private Button salvarButton;  

    @FXML
    private TextField codigoTextField;
    
    @FXML
    private TextField nomeTextField;

    private Responsavel responsavel;

    private ResponsavelController responsavelController;

    private ResponsavelDAO responsavelDAO;    

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        salvarButton.setOnAction(event -> salvarEdicao());        
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
        codigoTextField.setText(String.valueOf(responsavel.getCodigo()));
        nomeTextField.setText(responsavel.getNome());
    }

    public void setResponsavelDAO(ResponsavelDAO responsavelDAO) {
        this.responsavelDAO = responsavelDAO;
    }

    public void setResponsavelController(ResponsavelController responsavelController) {
        this.responsavelController = responsavelController;
    }
 
    @FXML
    private void salvarEdicao() {
        if (responsavel != null) {
            String novoNome = nomeTextField.getText();
            String codigoTexto = codigoTextField.getText();

            // Verificar se o nome já existe no banco de dados
            if (responsavelDAO.existsResponsavel(novoNome) && !novoNome.equals(responsavel.getNome())) {
                AlertUtil.showWarningAlert("Nome já existente", "O nome informado já está em uso.",
                        "Por favor, informe um nome diferente.");
                return;
            }

            responsavel.setNome(novoNome);
            try {
                int codigo = Integer.parseInt(codigoTexto);
                responsavel.setCodigo(codigo);
            } catch (NumberFormatException e) {
                AlertUtil.showErrorAlert("Erro", "Código inválido", "O código deve ser um número inteiro.");
                return;
            }

            // Atualizar o responsável no banco de dados
            boolean success = responsavelDAO.atualizarResponsavel(responsavel);

            if (success) {
                AlertUtil.showInformationAlert("Sucesso", null, "Responsável atualizado com sucesso.");
                // Atualize o TableView na janela principal
                if (responsavelController != null) {
                    responsavelController.atualizarTableView();
                }
                fecharJanela();
            } else {
                AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível atualizar o responsável.");
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
