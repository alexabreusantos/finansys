package com.marjax.finansys;

import com.marjax.finansys.util.UtilClass;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    private MenuItem compraMenuItem;
    @FXML
    private MenuItem cartaoMenuItem;
    @FXML
    private MenuItem categoriaMenuItem;
    @FXML
    private MenuItem faturaMenuItem;
    @FXML
    private MenuItem responsavelMenuItem;

    @FXML
    private Label horaLabel;
    @FXML
    private Label dataLabel;

    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {        

        UtilClass.horaAtual(horaLabel);
        UtilClass.dataAtual(dataLabel);
        actions();        
    }

    private void actions() {
        cartaoMenuItem.setOnAction(event -> abrirCartaoAction());
        categoriaMenuItem.setOnAction(event -> abrirCategoriaAction());
        compraMenuItem.setOnAction(event -> abrirCompraAction());
        faturaMenuItem.setOnAction(event -> abrirFaturaAction());
        responsavelMenuItem.setOnAction(event -> abrirResponsavelAction());
    }

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
            //stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }
    
    private void abrirCompraAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/compra.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Compras");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) compraMenuItem.getParentPopup().getOwnerWindow());
            //stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }
    

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
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

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
            //stage.setOnHidden(event -> atualizarTabela());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

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
            stage.showAndWait();
        } catch (IOException e) {
        }
    }
}
