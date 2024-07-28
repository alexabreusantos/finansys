/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.ResponsavelDAO;
import com.marjax.finansys.model.Responsavel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ResponsavelController implements Initializable {

    /**
     * Initializes the controller class.
     */
    TextField txtPesquisar;

    @FXML
    private TableView<Responsavel> tabelaResponsaveis;
    @FXML
    private TableColumn<Responsavel, Integer> codigoColuna;
    @FXML
    private TableColumn<Responsavel, String> nomeColuna;

    private ResponsavelDAO dao = new ResponsavelDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tabelaResponsaveis.setItems(dao.getAllResponsaveis());
    } 

}
