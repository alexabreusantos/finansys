package com.marjax.finansys;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
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

    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        mesesMenuItem.setOnAction(event -> abrirMeses());
        anoMenuItem.setOnAction(event -> abrirAno());
        cartaoButton.setOnAction(event -> abrirCartaoAction());
        categoriaButton.setOnAction(event -> abrirCategoriaAction());
        faturaButton.setOnAction(event -> abrirFaturaAction());
        responsavelButton.setOnAction(event -> abrirResponsavelAction());
    }
    
    @FXML
    public void abrirMeses() {

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
    public void abrirAno() {

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
    public void abrirCartaoAction() {

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
    public void abrirCategoriaAction() {

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
    public void abrirFaturaAction() {

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
    public void abrirResponsavelAction() {

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
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
