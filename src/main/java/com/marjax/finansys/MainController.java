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
    private MenuItem menuItemCategoria;
    
    @FXML
    private MenuItem menuItemCadastrarCategoria;

    @FXML
    private MenuItem menuItemResponsavel;
    
    @FXML
    private MenuItem menuItemCartao;

    @FXML
    private Button btn;

    private String css = "/com/marjax/finansys/style/main.css";
    
    @FXML
    public void AbrirJanelaCategoriaAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Categoria.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Categorias");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(menuItemCategoria.getParentPopup().getOwnerWindow());
            stage.showAndWait();            
        } catch (Exception e) {
            e.printStackTrace();
        }		
    }
        
    @FXML
    public void AbrirJanelaResponsavelAction() {

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
            stage.initOwner(menuItemResponsavel.getParentPopup().getOwnerWindow());
            stage.showAndWait();            
        } catch (Exception e) {
                e.printStackTrace();
            }		
        }  
    
    @FXML
    public void AbrirJanelaCartaoAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Cartao.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cartões de Crédito");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(menuItemCartao.getParentPopup().getOwnerWindow());
            stage.showAndWait();            
        } catch (Exception e) {
                e.printStackTrace();
            }		
        }      

    @FXML
    public void AbrirJanelaCadastrarCategoriaAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CategoriaCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Categoria");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);
            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(menuItemCadastrarCategoria.getParentPopup().getOwnerWindow());
            stage.showAndWait();            
        } catch (Exception e) {
                e.printStackTrace();
            }		
        }  
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        menuItemCategoria.setOnAction(event -> AbrirJanelaCategoriaAction());
        menuItemResponsavel.setOnAction(event -> AbrirJanelaResponsavelAction());
        menuItemCartao.setOnAction(event -> AbrirJanelaCartaoAction());
        menuItemCadastrarCategoria.setOnAction(event -> AbrirJanelaCadastrarCategoriaAction());
    }
}
