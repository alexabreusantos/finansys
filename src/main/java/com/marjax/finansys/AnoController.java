/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.AnoDAO;
import com.marjax.finansys.model.Ano;
import com.marjax.finansys.util.AlertUtil;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
public class AnoController implements Initializable {

    @FXML
    TextField pesquisarTextField;

    @FXML
    Label totalCadastroLabel;

    @FXML
    private TableView<Ano> anoTableView;

    @FXML
    private TableColumn<Ano, Integer> codigoColuna;

    @FXML
    private TableColumn<Ano, String> anoColuna;

    @FXML
    private Button adicionarButton;

    @FXML
    private Button excluirButton;

    private AnoDAO dao;

    private ObservableList<Ano> listaAnos;

    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dao = new AnoDAO();
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        anoColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));
        atualizarTableView();
        ativarBotoes();
        atualizarTotal();
        adicionarButton.setOnAction(event -> abrirCadastrar());
        excluirButton.setOnAction(event -> excluir());
        
        anoTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Duplo clique
                Ano selected = anoTableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    abrirEdicao(selected);
                }
            }
        });

    }

    public void atualizarTableView() {
        listaAnos = FXCollections.observableArrayList(dao.getAll());

        // Usar FilteredList para permitir a pesquisa
        FilteredList<Ano> filteredData = new FilteredList<>(listaAnos, p -> true);

        // Adicionar um listener ao campo de pesquisa
        pesquisarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ano -> {
                // Se o campo de pesquisa estiver vazio, exibir todas as categorias
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Comparar o nome da categoria com o texto da pesquisa para uma correspondência exata, ignorando maiúsculas/minúsculas
                String filter = newValue.toLowerCase();
                return ano.getValor().toLowerCase().equals(filter);
            });
        });

        anoTableView.setItems(filteredData);
    }

    private void ativarBotoes() {
        // Adicionar listener para ativar/desativar botão Excluir
        anoTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ano>() {
            @Override
            public void changed(ObservableValue<? extends Ano> observable, Ano oldValue, Ano newValue) {
                excluirButton.setDisable(newValue == null);
            }
        }
        );
    }

    public void atualizarTotal() {
        int total = dao.getTotal();
        totalCadastroLabel.setText(total + " anos cadastrados!");
    }

    @FXML
    public void abrirCadastrar() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/anoCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Ano");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarButton.getScene().getWindow());
            stage.setOnHidden(event -> atualizarTableView());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excluir() {
        Ano ano = anoTableView.getSelectionModel().getSelectedItem();
        if (ano != null) {
            // Mostrar popup de confirmação
            Optional<ButtonType> result = AlertUtil.showConfirmationAlert(
                    "Confirmação de Exclusão",
                    "Excluir Ano",
                    "Tem certeza que deseja excluir o ano " + ano.getValor() + "?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Se o usuário confirmar, excluir o ano
                boolean success = dao.excluir(ano.getCodigo());
                if (success) {
                    listaAnos.remove(ano); // Remover da lista original
                    AlertUtil.showInformationAlert("Sucesso", null, "Ano excluído com sucesso.");
                    atualizarTableView(); // Atualizar a TableView
                    atualizarTotal();
                } else {
                    AlertUtil.showErrorAlert("Erro", null, "Erro ao excluir o ano.");
                }
            }
        } else {
            AlertUtil.showWarningAlert("Aviso", null, "Nenhuma ano selecionado.");
        }
    }

    private void abrirEdicao(Ano ano) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/anoEditar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Editar " + ano.getValor());
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            AnoEditarController controller = fxmlLoader.getController();            
            controller.setAno(ano);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(anoTableView.getScene().getWindow());
            stage.setOnHidden(event -> atualizarTableView());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
