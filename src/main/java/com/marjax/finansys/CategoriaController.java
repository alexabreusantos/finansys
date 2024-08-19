/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CategoriaDAO;
import com.marjax.finansys.model.Categoria;
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
public class CategoriaController implements Initializable {

    @FXML
    TextField pesquisarTextField;

    @FXML
    Label totalCadastroLabel;

    @FXML
    private TableView<Categoria> categoriaTableView;

    @FXML
    private TableColumn<Categoria, Integer> codigoColuna;

    @FXML
    private TableColumn<Categoria, String> nomeColuna;

    @FXML
    private Button adicionarButton;

    @FXML
    private Button excluirButton;

    private CategoriaDAO dao;

    private ObservableList<Categoria> listaCategorias;

    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        dao = new CategoriaDAO();
        atualizarTableView();
        adicionarButton.setOnAction(event -> abrirCadastrarCategoriaAction());
        ativarBotoes();
        excluirButton.setOnAction(event -> excluirCategoriaSelecionada());

        categoriaTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Duplo clique
                Categoria selectedCategoria = categoriaTableView.getSelectionModel().getSelectedItem();
                if (selectedCategoria != null) {
                    abrirTelaEdicao(selectedCategoria);
                }
            }
        });

        atualizarTotalCategorias();
    }

    public void atualizarTotalCategorias() {
        int total = dao.getTotalCategorias();
        totalCadastroLabel.setText(total + " categorias cadastradas!");
    }

    public void atualizarTableView() {
        listaCategorias = FXCollections.observableArrayList(dao.getAllCategorias());

        // Usar FilteredList para permitir a pesquisa
        FilteredList<Categoria> filteredData = new FilteredList<>(listaCategorias, p -> true);

        // Adicionar um listener ao campo de pesquisa
        pesquisarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(categoria -> {
                // Se o campo de pesquisa estiver vazio, exibir todas as categorias
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Comparar o nome da categoria com o texto da pesquisa para uma correspondência exata, ignorando maiúsculas/minúsculas
                String filter = newValue.toLowerCase();
                return categoria.getNome().toLowerCase().equals(filter);
            });
        });

        categoriaTableView.setItems(filteredData);
    }

    private void excluirCategoriaSelecionada() {
        Categoria categoria = categoriaTableView.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            // Mostrar popup de confirmação
            Optional<ButtonType> result = AlertUtil.showConfirmationAlert(
                    "Confirmação de Exclusão",
                    "Excluir Categoria",
                    "Tem certeza que deseja excluir a categoria " + categoria.getNome() + "?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Se o usuário confirmar, excluir a categoria
                boolean success = dao.excluirCategoria(categoria.getCodigo());
                if (success) {
                    listaCategorias.remove(categoria); // Remover da lista original
                    AlertUtil.showInformationAlert("Sucesso", null, "Categoria excluída com sucesso.");
                    atualizarTableView(); // Atualizar a TableView
                    atualizarTotalCategorias();
                } else {
                    AlertUtil.showErrorAlert("Erro", null, "Erro ao excluir a categoria.");
                }
            }
        } else {
            AlertUtil.showWarningAlert("Aviso", null, "Nenhuma categoria selecionada.");
        }
    }

    private void ativarBotoes() {
        // Adicionar listener para ativar/desativar botão Excluir
        categoriaTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Categoria>() {
            @Override
            public void changed(ObservableValue<? extends Categoria> observable, Categoria oldValue, Categoria newValue) {
                excluirButton.setDisable(newValue == null);
            }
        }
        );
    }

    @FXML
    public void abrirCadastrarCategoriaAction() {

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
            stage.initOwner(adicionarButton.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirTelaEdicao(Categoria categoria) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/categoriaEditar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Editar " + categoria.getNome());
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            CategoriaEditarController controller = fxmlLoader.getController();
            controller.setCategoriaDAO(dao);
            controller.setCategoriaController(this);
            controller.setCategoria(categoria);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(categoriaTableView.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
