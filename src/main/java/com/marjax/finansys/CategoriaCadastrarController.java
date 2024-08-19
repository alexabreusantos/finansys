/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CategoriaDAO;
import com.marjax.finansys.model.Categoria;
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
public class CategoriaCadastrarController implements Initializable {

    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private TextField nomeTextField;

    private CategoriaDAO dao;

    private CategoriaController controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cancelarButton.setOnAction(event -> cancelarCadastro());
        salvarButton.setOnAction(event -> Salvar());
    }

    public void setCategoriaDAO(CategoriaDAO dao) {
        this.dao = dao;
    }

    public void setCategoriaController(CategoriaController controller) {
        this.controller = controller;
    }

    @FXML
    private void cancelarCadastro() {
        // get a handle to the stage
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    private void Salvar() {
        String nome = nomeTextField.getText().trim();
        boolean[] hasError = { false };        
        boolean nomePreechido = ValidationUtil.validateNonEmpty(nomeTextField, "Nome", hasError);         
        
        if (nomePreechido && !hasError[0]) {

            // Criar um objeto Responsavel
            Categoria categoria = new Categoria();
            categoria.setNome(nome);
            // Tentar salvar o responsável
            dao.salvar(categoria);

            // Atualize o TableView na janela principal
            if (controller != null) {
                controller.atualizarTableView();
                controller.atualizarTotalCategorias();
            }

            ((Stage) salvarButton.getScene().getWindow()).close();
        }
    }

}
