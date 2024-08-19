/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.util.AlertUtil;
import com.marjax.finansys.util.LocaleUtil;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
public class FaturaController implements Initializable {

    @FXML
    Label totalCadastroLabel;

    @FXML
    TextField pesquisarTextField;

    @FXML
    private TableView<Fatura> faturaTableView;

    @FXML
    private TableColumn<Fatura, Integer> codigoColuna;

    @FXML
    private TableColumn<Fatura, String> periodoColuna;

    @FXML
    private TableColumn<Fatura, Double> valorColuna;

    @FXML
    private TableColumn<Fatura, String> situacaoColuna;

    @FXML
    private TableColumn<Fatura, String> cartaoColuna;

    @FXML
    private Button adicionarButton;

    @FXML
    private Button excluirButton;

    private final FaturaDAO dao = new FaturaDAO();

    private ObservableList<Fatura> listaFaturas;

    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        periodoColuna.setCellValueFactory(cellData -> {
            // Converte o Timestamp para LocalDateTime, formata e retorna como SimpleStringProperty
            LocalDateTime localDateTime = cellData.getValue().getPeriodo().toLocalDateTime();
            String formattedDate = localDateTime.format(formatter);
            return new SimpleStringProperty(formattedDate);
        });
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));
        LocaleUtil.moedaBrasilColuna(valorColuna);
        situacaoColuna.setCellValueFactory(new PropertyValueFactory<>("situacao"));
        cartaoColuna.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getCartao().getNome());
        });
        
        faturaTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Duplo clique
                Fatura selectedFatura = faturaTableView.getSelectionModel().getSelectedItem();
                if (selectedFatura != null) {
                    abrirTelaEdicao(selectedFatura);
                }
            }
        });

        // Chama o método para carregar os dados na TableView
        atualizarTableView();
        atualizarTotalCartoes();
        ativarBotoes();

        adicionarButton.setOnAction(event -> abrirCadastrarAction());
        excluirButton.setOnAction(event -> excluir());
    }

    public void atualizarTotalCartoes() {
        int total = dao.getTotalFaturas();
        totalCadastroLabel.setText(total + " faturas cadastradas!");
    }

    public void atualizarTableView() {
        listaFaturas = FXCollections.observableArrayList(dao.getAllFaturas());

        // Usar FilteredList para permitir a pesquisa
        FilteredList<Fatura> filteredData = new FilteredList<>(listaFaturas, p -> true);

        // Adicionar um listener ao campo de pesquisa
        pesquisarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(fatura -> {
                // Se o campo de pesquisa estiver vazio, mostra todas as faturas
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (fatura.getCartao().getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo nome do cartão
                } else if (String.valueOf(fatura.getCodigo()).contains(lowerCaseFilter)) {
                    return true; // Filtra pelo código da fatura
                } else if (fatura.getSituacao().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pela situação
                } else if (fatura.getPeriodo().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/yyyy")).contains(lowerCaseFilter)) {
                    return true; // Filtra pelo período formatado
                }

                return false; // Não corresponde a nenhum filtro
            });
        });

        SortedList<Fatura> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(faturaTableView.comparatorProperty());

        faturaTableView.setItems(sortedData);
    }

    private void ativarBotoes() {
        // Adicionar listener para ativar/desativar botão Excluir
        faturaTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fatura>() {
            @Override
            public void changed(ObservableValue<? extends Fatura> observable, Fatura oldValue, Fatura newValue) {
                excluirButton.setDisable(newValue == null);
            }
        }
        );
    }

    @FXML
    private void abrirCadastrarAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/faturaCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Fatura");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            FaturaCadastrarController controller = fxmlLoader.getController();
            controller.setFaturaDAO(dao);
            controller.setFaturaController(this);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarButton.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excluir() {
        Fatura fatura = faturaTableView.getSelectionModel().getSelectedItem();
        if (fatura != null) {
            LocalDate localDate = fatura.getPeriodo().toLocalDateTime().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            String dataFormatada = localDate.format(formatter);
            // Mostrar popup de confirmação
            Optional<ButtonType> result = AlertUtil.showConfirmationAlert(
                    "Confirmação de Exclusão",
                    "Excluir Fatura",
                    "Tem certeza que deseja excluir a fatura do cartão " + fatura.getCartao().getNome() + " e do período " + dataFormatada + "?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Se o usuário confirmar, excluir a categoria
                boolean success = dao.excluir(fatura.getCodigo());
                if (success) {
                    listaFaturas.remove(fatura); // Remover da lista original
                    AlertUtil.showInformationAlert("Sucesso", null, "Fatura excluída com sucesso.");
                    atualizarTableView(); // Atualizar a TableView
                    atualizarTotalCartoes();
                } else {
                    AlertUtil.showErrorAlert("Erro", null, "Erro ao excluir a fatura.");
                }
            }
        } else {
            AlertUtil.showWarningAlert("Aviso", null, "Nenhuma fatura selecionada.");
        }
    }
    
    private void abrirTelaEdicao(Fatura fatura) {

        try {
            LocalDate localDate = fatura.getPeriodo().toLocalDateTime().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            String dataFormatada = localDate.format(formatter);
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/faturaEditar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Editar fatura do cartão " + fatura.getCartao().getNome() +" do Período " + dataFormatada);
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            FaturaEditarController controller = fxmlLoader.getController();
            controller.setFaturaDAO(dao);
            controller.setFaturaController(this);
            controller.setFatura(fatura);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(faturaTableView.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
