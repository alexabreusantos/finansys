/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.util.PreencherComboBox;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private Spinner<String> mesSpinner;

    @FXML
    private Spinner<Integer> anoSpinner;

    @FXML
    private ToggleGroup situacao;

    @FXML
    private RadioButton pagaRadioButton;

    @FXML
    private RadioButton pendenteRadioButton;

    @FXML
    private ComboBox<String> cartaoComboBox;

    public void setFaturaDAO(FaturaDAO faturaDAO) {
        this.faturaDAO = faturaDAO;
    }

    public void setFaturaController(FaturaController faturaController) {
        this.faturaController = faturaController;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
        codigoTextField.setText(String.valueOf(fatura.getCodigo()));

        preencherSpinners();
        LocalDate localDate = fatura.getPeriodo().toLocalDateTime().toLocalDate();
        DateTimeFormatter anoFormatado = DateTimeFormatter.ofPattern("yyyy");
        String somenteAno = localDate.format(anoFormatado);
        int ano = Integer.parseInt(somenteAno);        
        anoSpinner.getValueFactory().setValue(ano);

        if ("Paga".equals(fatura.getSituacao())) {
            pagaRadioButton.setSelected(true);
        } else {
            pendenteRadioButton.setSelected(true);
        }
        PreencherComboBox.ComboBoxNomesCartoes(cartaoComboBox);
        cartaoComboBox.setValue(fatura.getCartao().getNome());
    }

    private void preencherSpinners() {
        LocalDate localDate = fatura.getPeriodo().toLocalDateTime().toLocalDate();
        DateTimeFormatter mesFormatado = DateTimeFormatter.ofPattern("MM");
        String somenteMes = localDate.format(mesFormatado);        
        //mesSpinner.setValueFactory(somenteMes);

        // Configuração do Spinner de anos
        int startYear = 2024;
        int endYear = startYear + 100; // Define o intervalo de 2024 a 2124

        SpinnerValueFactory<Integer> yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(startYear, endYear, startYear);
        anoSpinner.setValueFactory(yearValueFactory);
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
