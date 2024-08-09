/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.util.LocaleUtil;
import com.marjax.finansys.util.MaskFieldUtil;
import com.marjax.finansys.util.PreencherComboBox;
import com.marjax.finansys.util.ValidationUtil;
import com.marjax.finansys.util.ValorConverter;
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
public class CartaoCadastrarController implements Initializable {

    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField limiteTextField;

    @FXML
    private ComboBox fechamentoComboBox;

    @FXML
    private ComboBox vencimentoComboBox;

    private CartaoDAO dao;

    private CartaoController controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cancelarButton.setOnAction(event -> cancelarCadastro());
        PreencherComboBox.ComboBoxDias(fechamentoComboBox);
        PreencherComboBox.ComboBoxDias(vencimentoComboBox);

        LocaleUtil.applyBrazilianCurrencyFormat(limiteTextField);
        
        salvarButton.setOnAction(event -> salvar());
    }

    public void setCartaoDAO(CartaoDAO dao) {
        this.dao = dao;
    }

    public void setCartaoController(CartaoController controller) {
        this.controller = controller;
    }

    @FXML
    private void cancelarCadastro() {
        // get a handle to the stage
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    private void salvar() {

        ValorConverter converter = new ValorConverter();
        String nome = nomeTextField.getText().trim();        
        boolean[] hasError = {false};

        // Valida o nome primeiro
        boolean validNome = ValidationUtil.validateNonEmpty(nomeTextField, "Nome", hasError);
        if (!validNome) {
            return; // Interrompe a validação se o nome for inválido
        }
        // Valida o limite após o nome
        boolean validLimite = ValidationUtil.validateNonEmpty(limiteTextField, "Limite", hasError);
        if (!validLimite) {
            return; // Interrompe a validação se o limite for inválido
        }
        // Valida os ComboBoxes na ordem desejada
        boolean validFechamento = ValidationUtil.validateComboBoxSelection(fechamentoComboBox, "Fechamento", hasError);
        if (!validFechamento) {
            return; // Interrompe a validação se o fechamento for inválido
        }
        boolean validVencimento = ValidationUtil.validateComboBoxSelection(vencimentoComboBox, "Vencimento", hasError);
        if (!validVencimento) {
            return; // Interrompe a validação se o vencimento for inválido
        }
        // Valida se o fechamento é menor que o vencimento
        boolean validFechamentoMenorQueVencimento = ValidationUtil.validateFechamentoMenorQueVencimento(fechamentoComboBox, vencimentoComboBox, hasError);
        if (!validFechamentoMenorQueVencimento) {
            return; // Interrompe a validação se a relação entre fechamento e vencimento for inválida
        }

        if (!hasError[0]) {
            // Criar um objeto Responsavel
            Cartao cartao = new Cartao();
            Double valorConvertido = converter.valor(limiteTextField);
            int fechamento = Integer.parseInt(fechamentoComboBox.getValue().toString());
            int vencimento = Integer.parseInt(vencimentoComboBox.getValue().toString());

            cartao.setNome(nome);
            cartao.setLimite(valorConvertido);
            cartao.setLimiteDisponivel(valorConvertido);
            cartao.setLimiteUsado(0.0);
            cartao.setFechamento(fechamento);
            cartao.setVencimento(vencimento);

            //sout + tab
            //System.out.println(fechamento);
            // Tentar salvar o responsável
            dao.salvar(cartao);
            // Atualize o TableView na janela principal
            if (controller != null) {
                controller.atualizarTableView();
                controller.atualizarTotalCartoes();
            }
            ((Stage) salvarButton.getScene().getWindow()).close();
        }
    }

}
