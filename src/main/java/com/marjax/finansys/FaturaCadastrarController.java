/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.model.Ano;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.model.Mes;
import com.marjax.finansys.util.AlertUtil;
import com.marjax.finansys.util.ConverterDate;
import com.marjax.finansys.util.PreencherComboBox;
import com.marjax.finansys.util.ValidationUtil;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class FaturaCadastrarController implements Initializable {

    @FXML
    private ComboBox<Mes> mesComboBox;

    @FXML
    private ComboBox<Ano> anoComboBox;

    @FXML
    private Button salvarButton;

    @FXML
    private ToggleGroup situacao;

    @FXML
    private ComboBox<Cartao> cartaoComboBox;

    private FaturaDAO dao = new FaturaDAO();    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dao = new FaturaDAO();
        
        PreencherComboBox.ComboBoxMeses(mesComboBox);
        PreencherComboBox.ComboBoxAnos(anoComboBox);
        PreencherComboBox.ComboBoxCartoes(cartaoComboBox);

        salvarButton.setOnAction((var event) -> Salvar());
    }    

    @FXML
    private void Salvar() {

        boolean[] hasError = {false};
        // Valida os ComboBoxes na ordem desejada

        boolean validaMes = ValidationUtil.validateComboBoxMes(mesComboBox, "Mês", hasError);
        if (!validaMes) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        boolean validaAno = ValidationUtil.validateComboBoxAno(anoComboBox, "Ano", hasError);
        if (!validaAno) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        boolean validaCartao = ValidationUtil.validateComboBoxCartao(cartaoComboBox, "Cartão", hasError);
        if (!validaCartao) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        if (!hasError[0]) {
            
            Fatura fatura = new Fatura();            
            Cartao cartao = cartaoComboBox.getValue();    

            // 2024-07-01
            RadioButton selectedRadioButton = (RadioButton) situacao.getSelectedToggle();

            Date periodo = ConverterDate.formatarDataParaDate(mesComboBox, anoComboBox, cartao.getFechamento()) ;
            fatura.setPeriodo(periodo);
            fatura.setValor(0.0);
            fatura.setSituacao(selectedRadioButton.getText());
            fatura.setCartao(cartao);
                        
            // Verificar se a fatura já existe
            if (dao.existe(fatura.getPeriodo(), fatura.getCartao().getCodigo(), fatura.getCodigo())) {
                AlertUtil.showErrorAlert("Atenção", "Fatura já existente", "Já existe uma fatura para este cartão no período especificado.");
            } else {
                dao.salvar(fatura);                 
                ((Stage) salvarButton.getScene().getWindow()).close();
            }
        }
    }
}
