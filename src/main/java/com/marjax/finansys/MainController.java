package com.marjax.finansys;

import com.marjax.finansys.dao.CompraDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Compra;
import com.marjax.finansys.util.LocaleUtil;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    TextField pesquisarTextField;

    @FXML
    private MenuItem mesesMenuItem;
    @FXML
    private MenuItem anosMenuItem;
    @FXML
    private MenuItem cartaoMenuItem;
    @FXML
    private MenuItem categoriaMenuItem;
    @FXML
    private MenuItem faturaMenuItem;
    @FXML
    private MenuItem responsavelMenuItem;

    @FXML
    private TableView<Compra> compraTableView;
    @FXML
    private TableColumn<Compra, Integer> codigoColuna;
    @FXML
    private TableColumn<Compra, String> descricaoColuna;
    @FXML
    private TableColumn<Compra, String> responsavelColuna;
    @FXML
    private TableColumn<Compra, String> cartaoColuna;
    @FXML
    private TableColumn<Compra, String> categoriaColuna;
    @FXML
    private TableColumn<Compra, Double> valorColuna;
    @FXML
    private TableColumn<Compra, String> parcelaColuna;
    @FXML
    private TableColumn<Compra, String> dataColuna;
    @FXML
    private TableColumn<Compra, Double> totalColuna;
    @FXML
    private TableColumn<Compra, String> situacaoColuna;

    private CompraDAO dao;
    Cartao cartao;

    private ObservableList<Compra> compras;

    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        dao = new CompraDAO();
        cartao = new Cartao();

        hoverMenu();

        mesesMenuItem.setOnAction(event -> abrirMeses());
        anosMenuItem.setOnAction(event -> abrirAno());
        cartaoMenuItem.setOnAction(event -> abrirCartaoAction());
        categoriaMenuItem.setOnAction(event -> abrirCategoriaAction());
        faturaMenuItem.setOnAction(event -> abrirFaturaAction());
        responsavelMenuItem.setOnAction(event -> abrirResponsavelAction());

        configurarTabela();
    }

    private void hoverMenu() {

    }

    @FXML
    private void abrirMeses() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/mes.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Meses");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) mesesMenuItem.getParentPopup().getOwnerWindow());
            stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    @FXML
    private void abrirAno() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/ano.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Anos");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) anosMenuItem.getParentPopup().getOwnerWindow());
            stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    @FXML
    private void abrirCartaoAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/cartao.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cartões de Crédito");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) cartaoMenuItem.getParentPopup().getOwnerWindow());
            stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    @FXML
    private void abrirCategoriaAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/categoria.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Categorias");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) categoriaMenuItem.getParentPopup().getOwnerWindow());
            stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    @FXML
    private void abrirFaturaAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/fatura.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Faturas");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) faturaMenuItem.getParentPopup().getOwnerWindow());
            stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    @FXML
    private void abrirResponsavelAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/responsavel.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Responsáveis");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) responsavelMenuItem.getParentPopup().getOwnerWindow());
            stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    private void configurarTabela() {
        compraTableView.widthProperty().addListener((observable, oldValue, newValue) -> {
            double tableWidth = newValue.doubleValue();

            codigoColuna.prefWidthProperty().setValue(tableWidth * 0.05);
            descricaoColuna.prefWidthProperty().setValue(tableWidth * 0.2);
            responsavelColuna.prefWidthProperty().setValue(tableWidth * 0.15);
            cartaoColuna.prefWidthProperty().setValue(tableWidth * 0.1);
            valorColuna.prefWidthProperty().setValue(tableWidth * 0.094);
            categoriaColuna.prefWidthProperty().setValue(tableWidth * 0.1);
            dataColuna.prefWidthProperty().setValue(tableWidth * 0.09);
            parcelaColuna.prefWidthProperty().setValue(tableWidth * 0.05);
            totalColuna.prefWidthProperty().setValue(tableWidth * 0.09);
            situacaoColuna.prefWidthProperty().setValue(tableWidth * 0.066);
        });

        // Configura as colunas do TableView
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        responsavelColuna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResponsavel().getNome()));

        // Configura a coluna para exibir o nome do cartão
        cartaoColuna.setCellValueFactory(cellData -> {
            cartao = cellData.getValue().getFatura().getCartao();
            return new SimpleStringProperty(cartao.getNome());
        });

        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));
        LocaleUtil.moedaBrasilColuna(valorColuna);
        categoriaColuna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria().getNome()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataColuna.setCellValueFactory(cellData -> {
            // Converte o Date para LocalDateTime, formata e retorna como SimpleStringProperty
            LocalDate localDate = cellData.getValue().getDataCompra().toLocalDate();
            String formattedDate = localDate.format(formatter);
            return new SimpleStringProperty(formattedDate);
        });
        // Configura a coluna para exibir as parcelas no formato 1/12
        parcelaColuna.setCellValueFactory(cellData -> {
            Compra compra = cellData.getValue();
            int parcelaAtual = compra.getParcelaAtual();
            int parcelaTotal = compra.getParcelaTotal();

            String parcelaText;
            if (parcelaAtual == 0 && parcelaTotal == 0) {
                parcelaText = "";
            } else {
                parcelaText = parcelaAtual + "/" + parcelaTotal;
            }

            return new SimpleStringProperty(parcelaText);
        });
        totalColuna.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        LocaleUtil.moedaBrasilColuna(totalColuna);
        situacaoColuna.setCellValueFactory(new PropertyValueFactory<>("situacao"));

        // Carrega as compras do banco de dados e preenche o TableView
        atualizarTabela();
    }

    private void atualizarTabela() {
        compras = FXCollections.observableArrayList(dao.getAllCompras());

        // Usar FilteredList para permitir a pesquisa
        FilteredList<Compra> filteredData = new FilteredList<>(compras, p -> true);

        // Adicionar um listener ao campo de pesquisa
        pesquisarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(compra -> {
                // Se o campo de pesquisa estiver vazio, mostra todas as faturas
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (compra.getDescricao().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pela descrição
                } else if (String.valueOf(compra.getResponsavel().getNome()).contains(lowerCaseFilter)) {
                    return true; // Filtra pelo responsavel
                } else if (compra.getFatura().getCartao().getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo nome do cartao
                } else if (compra.getCategoria().getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo nome da categoria
                } else if (compra.getSituacao().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pela situação
                } else if (compra.getDataCompra().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).contains(lowerCaseFilter)) {
                    return true; // Filtra pelo período formatado
                }

                return false; // Não corresponde a nenhum filtro
            });
        });

        SortedList<Compra> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(compraTableView.comparatorProperty());

        compraTableView.setItems(sortedData);
    }
}
