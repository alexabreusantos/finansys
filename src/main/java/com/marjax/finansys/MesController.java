/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.MesDAO;
import com.marjax.finansys.model.Mes;
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
public class MesController implements Initializable {

    @FXML
    private Button adicionarButton;

    @FXML
    private TextField pesquisarTextField;

    @FXML
    private Label totalCadastroLabel;

    @FXML
    private TableView<Mes> mesTableView;

    @FXML
    private TableColumn<Mes, Integer> codigoColuna;

    @FXML
    private TableColumn<Mes, String> nomeColuna;

    @FXML
    private TableColumn<Mes, Integer> numeroColuna;

    @FXML
    private Button excluirButton;

    private MesDAO dao;
    private ObservableList<Mes> listaMeses;
    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        numeroColuna.setCellValueFactory(new PropertyValueFactory<>("numero"));

        adicionarButton.setOnAction(event -> abrirCadastro());
        excluirButton.setOnAction(event -> excluirSelecionado());
        
        mesTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Duplo clique
                Mes selectedMes = mesTableView.getSelectionModel().getSelectedItem();
                if (selectedMes != null) {
                    abrirEdicao(selectedMes);
                }
            }
        });
        
        dao = new MesDAO();
        atualizarTotal();
        atualizarTableView();
        ativarBotoes();
    }

    @FXML
    public void abrirCadastro() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/mesCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Mês");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            MesCadastrarController controller = fxmlLoader.getController();
            controller.setMesDAO(dao);
            controller.setMesController(this);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarButton.getScene().getWindow());
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizarTotal() {
        int total = dao.getTotal();
        totalCadastroLabel.setText(total + " meses cadastrados!");
    }

    public void atualizarTableView() {
        listaMeses = FXCollections.observableArrayList(dao.getAllMeses());

        // Usar FilteredList para permitir a pesquisa
        FilteredList<Mes> filteredData = new FilteredList<>(listaMeses, p -> true);

        // Adicionar um listener ao campo de pesquisa
        pesquisarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(mes -> {
                // Se o campo de pesquisa estiver vazio, exibir todas os meses
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Comparar o nome do mes com o texto da pesquisa para uma correspondência exata, ignorando maiúsculas/minúsculas
                String filter = newValue.toLowerCase();               
                return mes.getNome().toLowerCase().contains(filter);
            });
        });

        mesTableView.setItems(filteredData);
    }

    private void ativarBotoes() {
        // Adicionar listener para ativar/desativar botão Excluir
        mesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Mes>() {
            @Override
            public void changed(ObservableValue<? extends Mes> observable, Mes oldValue, Mes newValue) {
                excluirButton.setDisable(newValue == null);
            }
        }
        );
    }
    
    private void excluirSelecionado() {
        Mes mes = mesTableView.getSelectionModel().getSelectedItem();
        if (mes != null) {
            // Mostrar popup de confirmação
            Optional<ButtonType> result = AlertUtil.showConfirmationAlert(
                    "Confirmação de Exclusão",
                    "Excluir Mês",
                    "Tem certeza que deseja excluir o mês " + mes.getNome() + "?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Se o usuário confirmar, excluir o mes
                boolean success = dao.excluir(mes.getCodigo());
                if (success) {
                    listaMeses.remove(mes); // Remover da lista original
                    AlertUtil.showInformationAlert("Sucesso", null, "Mês excluído com sucesso.");
                    atualizarTableView(); // Atualizar a TableView
                    atualizarTotal();
                } else {
                    AlertUtil.showErrorAlert("Erro", null, "Erro ao excluir o mês.");
                }
            }
        } else {
            AlertUtil.showWarningAlert("Aviso", null, "Nenhuma mês selecionado.");
        }
    }
    
    private void abrirEdicao(Mes mes) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/mesEditar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Editar " + mes.getNome());
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            MesEditarController controller = fxmlLoader.getController();
            controller.setMesDAO(dao);
            controller.setMesController(this);
            controller.setMes(mes);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mesTableView.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
