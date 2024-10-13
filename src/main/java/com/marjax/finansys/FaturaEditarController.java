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
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class FaturaEditarController implements Initializable {

    private Fatura fatura;

    @FXML
    private Button salvarButton;

    @FXML
    private TextField codigoTextField;

    @FXML
    private ComboBox<String> mesComboBox;

    @FXML
    private ComboBox<Integer> anoComboBox;

    @FXML
    private ToggleGroup situacao;

    @FXML
    private RadioButton pagaRadioButton;

    @FXML
    private RadioButton pendenteRadioButton;

    @FXML
    private ComboBox<Cartao> cartaoComboBox;

    private FaturaDAO dao = new FaturaDAO();

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
        codigoTextField.setText(String.valueOf(fatura.getCodigo()));

        if ("Paga".equals(fatura.getSituacao())) {
            pagaRadioButton.setSelected(true);
        }
        if ("Pendente".equals(fatura.getSituacao())) {
            pendenteRadioButton.setSelected(true);
        }

        PreencherComboBox.ComboBoxCartoes(cartaoComboBox);
        Cartao cartaoSelecionado = fatura.getCartao();
        // Percorre os itens do ComboBox para encontrar o cartão correspondente
        for (Cartao cartao : cartaoComboBox.getItems()) {
            if (cartao.getNome().equals(cartaoSelecionado.getNome())) {
                cartaoComboBox.setValue(cartao);
                break;
            }
        }
        PreencherComboBox.preencherMeses(mesComboBox);
        String mesNome = ConverterDate.nomeMes(fatura.getPeriodo());
        mesComboBox.setValue(mesNome);

        PreencherComboBox.preencherAnos(anoComboBox);
        SimpleDateFormat anoPeriodo = new SimpleDateFormat("yyyy");
        String anoValor = anoPeriodo.format(fatura.getPeriodo());
        Integer anoSelecionado = Integer.valueOf(anoValor);
        anoComboBox.setValue(anoSelecionado);

        // Percorre os itens do ComboBox para encontrar o ano correspondente
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dao = new FaturaDAO();
        salvarButton.setOnAction(event -> salvarEdicao());

        pagaRadioButton.setToggleGroup(situacao);
        pendenteRadioButton.setToggleGroup(situacao);
    }

    @FXML
    private void salvarEdicao() {
        if (fatura != null) {
            int codigo = Integer.parseInt(codigoTextField.getText());
            Double valor = 0.0;
            Cartao cartao = cartaoComboBox.getValue();
            Date periodo = ConverterDate.formatarDataParaDate(mesComboBox, anoComboBox, cartao.getFechamento());

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

            // Verificar se o nome já existe no banco de dados
            if (dao.existe(fatura)) {
                AlertUtil.showWarningAlert("Fatura já existente", "A fatura já está em uso.", "Por favor, informe dados diferente.");
                return;
            }

            if (validaMes && validaAno && validaCartao && !hasError[0]) {
                fatura.setPeriodo(periodo);
                fatura.setValor(valor);
                RadioButton selectedRadioButton = (RadioButton) situacao.getSelectedToggle();
                if (selectedRadioButton != null) {
                    String selectedText = selectedRadioButton.getText();
                    fatura.setSituacao(selectedText);
                }

                fatura.setCartao(cartao);
                try {
                    fatura.setCodigo(codigo);
                } catch (NumberFormatException e) {
                    AlertUtil.showErrorAlert("Erro", "Código inválido", "O código deve ser um número inteiro.");
                    return;
                }

                // Atualizar o responsável no banco de dados
                boolean success = dao.atualizar(fatura);

                if (success) {
                    AlertUtil.showInformationAlert("Sucesso", null, "Fatura atualizada com sucesso.");
                    ((Stage) salvarButton.getScene().getWindow()).close();//fecha janela
                } else {
                    AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível atualizar a fatura.");
                }
            }
        } else {
            AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível salvar os dados.");
        }
    }
}
