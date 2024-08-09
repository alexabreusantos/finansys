/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.util.LocaleUtil;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private final FaturaDAO dao = new FaturaDAO();

    private ObservableList<Fatura> listaFaturas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        periodoColuna.setCellValueFactory(cellData -> {
            // Formata o LocalDate usando o formatter e retorna como SimpleStringProperty
            return new SimpleStringProperty(cellData.getValue().getPeriodo().format(formatter));
        });
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));
        LocaleUtil.moedaBrasilColuna(valorColuna);
        situacaoColuna.setCellValueFactory(new PropertyValueFactory<>("situacao"));
        cartaoColuna.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getCartao().getNome());
        });

        // Chama o método para carregar os dados na TableView
        atualizarTableView();
        atualizarTotalCartoes();
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
                } else if (fatura.getPeriodo().format(DateTimeFormatter.ofPattern("MM/yyyy")).contains(lowerCaseFilter)) {
                    return true; // Filtra pelo período formatado
                }

                return false; // Não corresponde a nenhum filtro
            });
        });

        SortedList<Fatura> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(faturaTableView.comparatorProperty());

        faturaTableView.setItems(sortedData);
    }

}
