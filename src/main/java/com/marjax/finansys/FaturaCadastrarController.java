/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.util.AlertUtil;
import com.marjax.finansys.util.ConverterDate;
import com.marjax.finansys.util.PreencherComboBox;
import com.marjax.finansys.util.ValidationUtil;
import java.net.URL;
import java.sql.Date;
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
    private ComboBox<String> mesComboBox;

    @FXML
    private ComboBox<Integer> anoComboBox;

    @FXML
    private Button salvarButton;

    @FXML
    private ToggleGroup situacao;

    @FXML
    private ComboBox<Cartao> cartaoComboBox;

    private FaturaDAO dao = new FaturaDAO();

    private Cartao cartaoSelecionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dao = new FaturaDAO();

        PreencherComboBox.preencherMeses(mesComboBox);
        PreencherComboBox.preencherAnos(anoComboBox);
        PreencherComboBox.agendarAtualizacaoAnos(anoComboBox);
        PreencherComboBox.ComboBoxCartoes(cartaoComboBox);

        salvarButton.setOnAction((var event) -> Salvar());
    }

    // Método para receber o cartão selecionado
    public void setCartaoSelecionado(Cartao cartao) {
        // Preenche a lista de cartões no ComboBox, se necessário
       // PreencherComboBox.comboBoxFaturaCartao(cartaoComboBox);

        // Verifica se o cartão não é nulo e o seleciona no ComboBox
        if (cartao != null) {
            cartaoComboBox.getSelectionModel().select(cartao);
        }
    }

    
  
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

            Date periodo = ConverterDate.formatarDataParaDate(mesComboBox, anoComboBox, cartao.getFechamento());
            fatura.setPeriodo(periodo);
            fatura.setValor(0.0);
            fatura.setSituacao(selectedRadioButton.getText());
            fatura.setCartao(cartao);

            // Verificar se a fatura já existe
            if (dao.existe(fatura)) {
                AlertUtil.showErrorAlert("Atenção", "Fatura já existente", "Já existe uma fatura para este cartão no período especificado.");
            } else {
                dao.salvar(fatura);
                ((Stage) salvarButton.getScene().getWindow()).close();
            }
        }
    }
}
