/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.AnoDAO;
import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.dao.MesDAO;
import com.marjax.finansys.model.Ano;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.model.Mes;
import com.marjax.finansys.util.AlertUtil;
import com.marjax.finansys.util.ConverterTimeStamp;
import com.marjax.finansys.util.PreencherComboBox;
import com.marjax.finansys.util.ValidationUtil;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

    private FaturaController faturaController;

    private FaturaDAO faturaDAO;

    @FXML
    private Button salvarButton;

    @FXML
    private TextField codigoTextField;

    @FXML
    private ComboBox<Mes> mesComboBox;

    @FXML
    private ComboBox<Ano> anoComboBox;

    @FXML
    private ToggleGroup situacao;

    @FXML
    private RadioButton pagaRadioButton;

    @FXML
    private RadioButton pendenteRadioButton;

    @FXML
    private ComboBox<Cartao> cartaoComboBox;

    private FaturaDAO dao = new FaturaDAO();

    private FaturaController controller;

    public void setFaturaDAO(FaturaDAO faturaDAO) {
        this.faturaDAO = faturaDAO;
    }

    public void setFaturaController(FaturaController faturaController) {
        this.faturaController = faturaController;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
        codigoTextField.setText(String.valueOf(fatura.getCodigo()));

        if ("Paga".equals(fatura.getSituacao())) {
            pagaRadioButton.setSelected(true);
        } else {
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
        PreencherComboBox.ComboBoxMeses(mesComboBox);
        // Converter Timestamp para LocalDateTime         
        LocalDateTime data = fatura.getPeriodo().toLocalDateTime();
        Locale locale = Locale.forLanguageTag("pt-BR");
        // Extrair o mês e o ano
        String mesNome = data.getMonth().getDisplayName(TextStyle.FULL, locale);
        MesDAO mesDAO = new MesDAO();
        Mes mesSelecionado = mesDAO.buscarMes(mesNome);
        // Percorre os itens do ComboBox para encontrar o mes correspondente
        for (Mes mes : mesComboBox.getItems()) {
            if (mes.getNome().equals(mesSelecionado.getNome())) {
                mesComboBox.setValue(mes);
                break;
            }
        }

        PreencherComboBox.ComboBoxAnos(anoComboBox);
        String anoValor = String.valueOf(data.getYear());
        AnoDAO anoDAO = new AnoDAO();
        Ano anoSelecionado = anoDAO.buscarAno(anoValor);
        // Percorre os itens do ComboBox para encontrar o ano correspondente
        for (Ano ano : anoComboBox.getItems()) {
            if (ano.getValor().equals(anoSelecionado.getValor())) {
                anoComboBox.setValue(ano);
                break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        salvarButton.setOnAction(event -> salvarEdicao());
    }

    @FXML
    private void salvarEdicao() {
        if (fatura != null) {
            int codigo = Integer.parseInt(codigoTextField.getText());
            Timestamp periodo = ConverterTimeStamp.formatarDataParaTimestamp(mesComboBox, anoComboBox);
            Double valor = 0.0;
            Cartao cartao = cartaoComboBox.getValue();
            RadioButton selectedRadioButton = (RadioButton) situacao.getSelectedToggle();

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
            if (dao.existe(fatura.getPeriodo(), fatura.getCartao().getCodigo(), fatura.getCodigo())) {
                AlertUtil.showWarningAlert("Fatura já existente", "A fatura já está em uso.", "Por favor, informe dados diferente.");
                return;
            }

            if (validaMes && validaAno && validaCartao && !hasError[0]) {
                fatura.setPeriodo(periodo);
                fatura.setValor(valor);
                fatura.setSituacao(selectedRadioButton.getText());
                fatura.setCartao(cartao);
                try {
                    fatura.setCodigo(codigo);
                } catch (NumberFormatException e) {
                    AlertUtil.showErrorAlert("Erro", "Código inválido", "O código deve ser um número inteiro.");
                    return;
                }

                // Atualizar o responsável no banco de dados
                boolean success = faturaDAO.atualizar(fatura);

                if (success) {
                    AlertUtil.showInformationAlert("Sucesso", null, "Fatura atualizada com sucesso.");

                    // Atualize o TableView na janela principal
                    if (faturaController != null) {
                        faturaController.atualizarTableView();
                    }
                    fecharJanela();
                } else {
                    AlertUtil.showErrorAlert("Erro", "Erro ao salvar", "Não foi possível atualizar a fatura.");
                }
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
