/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CategoriaDAO;
import com.marjax.finansys.model.Categoria;
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
public class CategoriaEditarController implements Initializable {

    @FXML
    private Button salvarButton;
      
    @FXML
    private TextField codigoTextField;  
    
    @FXML
    private TextField nomeTextField;    
    
    private Categoria categoria;

    private CategoriaController categoriaController;

    private CategoriaDAO categoriaDAO;

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        codigoTextField.setText(String.valueOf(categoria.getCodigo()));
        nomeTextField.setText(categoria.getNome());
    }

    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public void setCategoriaController(CategoriaController categoriaController) {
        this.categoriaController = categoriaController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        salvarButton.setOnAction(event -> salvarEdicao());        
    }    

    @FXML
    private void salvarEdicao() {
        if (categoria != null) {
            String novoNome = nomeTextField.getText().trim();
            String codigoTexto = codigoTextField.getText();

            boolean[] hasError = {false};
            boolean nomePreechido = ValidationUtil.validateNonEmpty(nomeTextField, "Nome", hasError);

            // Verificar se o nome já existe no banco de dados
            if (categoriaDAO.existsCategoria(novoNome) && !novoNome.equals(categoria.getNome())) {
                AlertUtil.showWarningAlert("Nome já existente", "O nome informado já está em uso.",
                        "Por favor, informe um nome diferente.");
                return;
            }

            if (nomePreechido && !hasError[0]) {
                categoria.setNome(novoNome);
                try {
                    int codigo = Integer.parseInt(codigoTexto);
                    categoria.setCodigo(codigo);
                } catch (NumberFormatException e) {
                    AlertUtil.showErrorAlert("Erro", "Código inválido", "O código deve ser um número inteiro.");
                    return;
                }

                // Atualizar o responsável no banco de dados
                boolean success = categoriaDAO.atualizar(categoria);

                if (success) {
                    AlertUtil.showInformationAlert("Sucesso", null, "Categoria atualizada com sucesso.");
                    // Atualize o TableView na janela principal
                    if (categoriaController != null) {
                        categoriaController.atualizarTableView();
                    }
                    fecharJanela();
                } else {
                    AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível atualizar a categoria.");
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
