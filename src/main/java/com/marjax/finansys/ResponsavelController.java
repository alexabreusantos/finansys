/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.ResponsavelDAO;
import com.marjax.finansys.model.Responsavel;
import com.marjax.finansys.util.AlertUtil;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ResponsavelController implements Initializable {
    
    TextField txtPesquisar;

    @FXML
    private TableView<Responsavel> tabelaResponsaveis;

    @FXML
    private TableColumn<Responsavel, Integer> codigoColuna;

    @FXML
    private TableColumn<Responsavel, String> nomeColuna;

    @FXML
    private Button btnAdicionar;
    
    @FXML
    private Button btnAlterar;
    
    @FXML
    private Button btnExcluir;

    private ResponsavelDAO dao;

    private String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        dao = new ResponsavelDAO();         
        atualizarTableView();
        btnAdicionar.setOnAction(event -> AbrirJanelaCadastrarResponsavelAction());        
        AtivarBotaoExcluir();        
        btnExcluir.setOnAction(event -> excluirResponsavelSelecionado());        
        btnAlterar.setOnAction(event -> editarResponsavel());
    }  

    public void AtivarBotaoExcluir(){
        // Adicionar listener para ativar/desativar botão Excluir
        tabelaResponsaveis.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Responsavel>() {
                @Override
                public void changed(ObservableValue<? extends Responsavel> observable, Responsavel oldValue, Responsavel newValue) {
                    btnExcluir.setDisable(newValue == null);
                    btnAlterar.setDisable(newValue == null);
                }
            }
        );
    }
    
    @FXML
    public void AbrirJanelaCadastrarResponsavelAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/responsavelCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Responsável");
            stage.setScene(new Scene(root)); 
            stage.setMaximized(false);
            stage.setResizable(false);
            
            ResponsavelCadastrarController controller = fxmlLoader.getController();
            controller.setResponsavelDAO(dao);
            controller.setResponsavelController(this);
            
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAdicionar.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void atualizarTableView() {
        ObservableList<Responsavel> listaResponsaveis = FXCollections.observableArrayList(dao.getAllResponsaveis());
        tabelaResponsaveis.setItems(listaResponsaveis);
    }    
    
    
    @FXML
    private void editarResponsavel() {
        Responsavel responsavelSelecionado = tabelaResponsaveis.getSelectionModel().getSelectedItem();
        if (responsavelSelecionado != null) {
            abrirTelaEdicao(responsavelSelecionado);
        } else {
            AlertUtil.showWarningAlert("Seleção Inválida", "Nenhum responsável selecionado", "Por favor, selecione um responsável para editar.");
        }
    }
    
    public void abrirTelaEdicao(Responsavel responsavel) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/responsavelEditar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Responsável");
            stage.setScene(new Scene(root)); 
            stage.setMaximized(false);
            stage.setResizable(false);
            
            ResponsavelEditarController controller = fxmlLoader.getController();
            controller.setResponsavelDAO(dao);
            controller.setResponsavelController(this);
            controller.setResponsavel(responsavel);
            
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAlterar.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void excluirResponsavelSelecionado() {
        Responsavel responsavel = tabelaResponsaveis.getSelectionModel().getSelectedItem();
        if (responsavel != null) {
            // Mostrar popup de confirmação
            Optional<ButtonType> result = AlertUtil.showConfirmationAlert(
                "Confirmação de Exclusão", 
                "Excluir Responsável", 
                "Tem certeza que deseja excluir o responsável " + responsavel.getNome() + "?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Se o usuário confirmar, excluir o responsável
                boolean success = dao.excluirResponsavel(responsavel.getCodigo());
                if (success) {
                    tabelaResponsaveis.getItems().remove(responsavel);
                    AlertUtil.showInformationAlert("Sucesso", null, "Responsável excluído com sucesso.");
                } else {
                    AlertUtil.showErrorAlert("Erro", null, "Erro ao excluir o responsável.");
                }
            }
        }
    }
}
