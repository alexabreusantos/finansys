/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.util.AlertUtil;
import com.marjax.finansys.util.LocaleUtil;
import com.marjax.finansys.util.MaskFieldUtil;
import com.marjax.finansys.util.PreencherComboBox;
import com.marjax.finansys.util.ValidationUtil;
import com.marjax.finansys.util.ValorConverter;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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

    private CartaoController controller;

    private CartaoDAO dao;

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
        codigoTextField.setText(String.valueOf(cartao.getCodigo()));
        nomeTextField.setText(cartao.getNome());

        double limite = cartao.getLimite();
        LocaleUtil.moedaBrasilTextField(limite, limiteTextField);

        PreencherComboBox.ComboBoxDias(fechamentoComboBox);
        fechamentoComboBox.setValue(cartao.getFechamento());
        PreencherComboBox.ComboBoxDias(vencimentoComboBox);
        vencimentoComboBox.setValue(cartao.getVencimento());
    }

    public void setCartaoDAO(CartaoDAO cartaoDAO) {
        this.dao = cartaoDAO;
    }

    public void setCartaoController(CartaoController cartaoController) {
        this.controller = cartaoController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocaleUtil.applyBrazilianCurrencyFormat(limiteTextField);
        salvarButton.setOnAction(event -> salvar());
    }

    private void salvar() {
        if (cartao != null) {
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

                cartao.setCodigo(Integer.valueOf(codigoTextField.getText()));
                cartao.setNome(nome);
                cartao.setLimite(valorConvertido);
                cartao.setLimiteDisponivel(valorConvertido);
                cartao.setLimiteUsado(0.0);
                cartao.setFechamento(fechamento);
                cartao.setVencimento(vencimento);

                //sout + tab
                //System.out.println(valorConvertido);
                
                // Tentar salvar o responsável
                dao.atualizar(cartao);
                // Atualize o TableView na janela principal
                if (controller != null) {
                    controller.atualizarTableView();
                    controller.atualizarTotalCartoes();
                }
                ((Stage) salvarButton.getScene().getWindow()).close();
            }
        } else {
            AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível salvar os dados.");
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow();
        stage.close();
    }
}
