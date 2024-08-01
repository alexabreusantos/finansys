/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CategoriaDAO;
import com.marjax.finansys.dao.ResponsavelDAO;
import com.marjax.finansys.model.Categoria;
import com.marjax.finansys.util.AlertUtil;
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
public class CategoriaController implements Initializable {

    TextField txtPesquisar;

    @FXML
    private TableView<Categoria> tabelaCategorias;

    @FXML
    private TableColumn<Categoria, Integer> codigoColuna;

    @FXML
    private TableColumn<Categoria, String> nomeColuna;

    @FXML
    private Button btnAdicionar;

    @FXML
    private Button btnAlterar;

    @FXML
    private Button btnExcluir;

    private CategoriaDAO dao;

    private String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        dao = new CategoriaDAO();
        atualizarTableView();
        btnAdicionar.setOnAction(event -> AbrirCadastrarCategoriaAction());        
        AtivarBotaoExcluir();
        btnExcluir.setOnAction(event -> excluirCategoriaSelecionada());
    }

    public void atualizarTableView() {
        ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList(dao.getAllCategorias());
        tabelaCategorias.setItems(listaCategorias);
    }

    private void excluirCategoriaSelecionada() {
        Categoria categoria = tabelaCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            // Mostrar popup de confirmação
            Optional<ButtonType> result = AlertUtil.showConfirmationAlert(
                    "Confirmação de Exclusão",
                    "Excluir Categoria",
                    "Tem certeza que deseja excluir a categoria " + categoria.getNome() + "?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Se o usuário confirmar, excluir o responsável
                boolean success = dao.excluirCategoria(categoria.getCodigo());
                if (success) {
                    tabelaCategorias.getItems().remove(categoria);
                    AlertUtil.showInformationAlert("Sucesso", null, "Responsável excluído com sucesso.");
                } else {
                    AlertUtil.showErrorAlert("Erro", null, "Erro ao excluir o responsável.");
                }
            }
        }
    }

    public void AtivarBotaoExcluir() {
        // Adicionar listener para ativar/desativar botão Excluir
        tabelaCategorias.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Categoria>() {
            @Override
            public void changed(ObservableValue<? extends Categoria> observable, Categoria oldValue, Categoria newValue) {
                btnExcluir.setDisable(newValue == null);
                btnAlterar.setDisable(newValue == null);
            }
        }
        );
    }
    
    @FXML
    public void AbrirCadastrarCategoriaAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/categoriaCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Responsável");
            stage.setScene(new Scene(root)); 
            stage.setMaximized(false);
            stage.setResizable(false);
            
            CategoriaCadastrarController controller = fxmlLoader.getController();
            controller.setCategoriaDAO(dao);
            controller.setCategoriaController(this);
            
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAdicionar.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
