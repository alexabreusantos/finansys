/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.util.MaskFieldUtil;
import com.marjax.finansys.util.PreencherComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class CartaoEditarController implements Initializable {

    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private TextField codigoTextField;

    @FXML
    private TextField nomeTextField;
    
    @FXML
    private TextField limiteTextField;

    @FXML
    private ComboBox fechamentoComboBox;

    @FXML
    private ComboBox vencimentoComboBox; 

    private Cartao cartao;

    private CartaoController cartaoController;

    private CartaoDAO dao;
    
    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
        codigoTextField.setText(String.valueOf(cartao.getCodigo()));
        nomeTextField.setText(cartao.getNome()); 
        
        double limite = cartao.getLimite();
        String limiteFormatado = MaskFieldUtil.getFormattedValue(limite);
        MaskFieldUtil.monetaryField(limiteTextField);
        limiteTextField.setText(limiteFormatado);
        fechamentoComboBox.setValue(cartao.getFechamento());
        PreencherComboBox.ComboBoxDias(fechamentoComboBox);
        vencimentoComboBox.setValue(cartao.getVencimento());
        PreencherComboBox.ComboBoxDias(vencimentoComboBox);        
    }

    public void setCartaoDAO(CartaoDAO cartaoDAO) {
        this.dao = cartaoDAO;
    }

    public void setCartaoController(CartaoController cartaoController) {
        this.cartaoController = cartaoController;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cancelarButton.setOnAction(event -> cancelarCadastro());        
    } 
    
    @FXML
    private void cancelarCadastro() {
        // get a handle to the stage
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
}
