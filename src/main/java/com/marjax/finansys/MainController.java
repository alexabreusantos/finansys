package com.marjax.finansys;

import com.marjax.finansys.dao.CompraDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Compra;
import com.marjax.finansys.util.LocaleUtil;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    private MenuItem mesesMenuItem;

    @FXML
    private MenuItem anoMenuItem;

    @FXML
    private Button cartaoButton;

    @FXML
    private Button categoriaButton;

    @FXML
    private Button faturaButton;

    @FXML
    private Button responsavelButton;

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

    private CompraDAO compraDAO;
    Cartao cartao;
    private ObservableList<Compra> compras;

    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        compraDAO = new CompraDAO();
        cartao = new Cartao();
        mesesMenuItem.setOnAction(event -> abrirMeses());
        anoMenuItem.setOnAction(event -> abrirAno());
        cartaoButton.setOnAction(event -> abrirCartaoAction());
        categoriaButton.setOnAction(event -> abrirCategoriaAction());
        faturaButton.setOnAction(event -> abrirFaturaAction());
        responsavelButton.setOnAction(event -> abrirResponsavelAction());

        carregarTabela();
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
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
            stage.initOwner((Stage) anoMenuItem.getParentPopup().getOwnerWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
            stage.initOwner(cartaoButton.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
            stage.initOwner(categoriaButton.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
            stage.initOwner(faturaButton.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
            stage.initOwner(responsavelButton.getScene().getWindow());
            stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarTabela() {
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
            situacaoColuna.prefWidthProperty().setValue(tableWidth * 0.07);  
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
    
    private void atualizarTabela(){
        compras = compraDAO.getAllCompras();
        compraTableView.setItems(compras);
    }
}
