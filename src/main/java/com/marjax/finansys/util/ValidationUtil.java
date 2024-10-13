/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Categoria;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.model.Responsavel;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ValidationUtil {

    // Valida se o campo de texto não está vazio
    public static boolean validateNonEmpty(TextField textField, String fieldName, boolean[] hasError) {
        if (textField.getText() == null || textField.getText().trim().isEmpty()) {
            if (!hasError[0]) {
                textField.requestFocus(); // Adiciona foco ao primeiro campo vazio encontrado
            }
            showValidationError(fieldName + " não pode estar vazio.");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    // Valida a quantidade de caracteres do campo
    public static boolean validateSizeTextMes(TextField textField, String fieldName, boolean[] hasError) {
        if (textField.getText().length() < 2) {
            if (!hasError[0]) {
                textField.requestFocus(); // Adiciona foco ao primeiro campo vazio encontrado
            }
            showValidationError(fieldName + " não pode ser menor 2 dígitos.");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    // Valida a quantidade de caracteres do campo
    public static boolean validateSizeTextAno(TextField textField, String fieldName, boolean[] hasError) {
        if (textField.getText().length() < 4) {
            if (!hasError[0]) {
                textField.requestFocus(); // Adiciona foco ao primeiro campo vazio encontrado
            }
            showValidationError(fieldName + " não pode ser menor 4 dígitos.");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    // Valida se o ComboBox não está com a seleção padrão
    public static boolean validateComboBoxSelection(ComboBox<String> comboBox, String fieldName, boolean[] hasError) {
        if (comboBox.getValue() == null || "Selecione".equals(comboBox.getValue())) {
            if (!hasError[0]) {
                comboBox.requestFocus(); // Adiciona foco ao primeiro ComboBox inválido encontrado
            }
            showValidationError("Por favor, selecione um valor para " + fieldName + ".");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    // Valida se o fechamento é menor que o vencimento
    public static boolean validateFechamentoMenorQueVencimento(ComboBox<Integer> fechamentoComboBox, ComboBox<Integer> vencimentoComboBox, boolean[] hasError) {
        Integer fechamento = fechamentoComboBox.getValue();
        Integer vencimento = vencimentoComboBox.getValue();

        if (fechamento == null || vencimento == null) {
            showValidationError("Os valores de Fechamento e Vencimento devem ser selecionados.");
            hasError[0] = true;
            return false;
        }

        if (fechamento >= vencimento) {
            if (!hasError[0]) {
                fechamentoComboBox.requestFocus(); // Adiciona foco ao ComboBox de fechamento
            }
            showValidationError("Fechamento deve ser menor que Vencimento.");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    // Valida se o ComboBox Mes da Fatura não está com a seleção padrão
    public static boolean validateComboBoxMes(ComboBox<String> comboBox, String fieldName, boolean[] hasError) {
        // Verifica se o ComboBox está vazio ou se o valor selecionado é nulo
        if (comboBox.getValue() == null) {
            if (!hasError[0]) {
                comboBox.requestFocus(); // Adiciona foco ao primeiro ComboBox inválido encontrado
            }
            showValidationError("Por favor, selecione um valor para " + fieldName + ".");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    // Valida se o ComboBox Ano da Fatura não está com a seleção padrão
    public static boolean validateComboBoxAno(ComboBox<Integer> comboBox, String fieldName, boolean[] hasError) {
        // Verifica se o ComboBox está vazio ou se o valor selecionado é nulo
        if (comboBox.getValue() == null) {
            if (!hasError[0]) {
                comboBox.requestFocus(); // Adiciona foco ao primeiro ComboBox inválido encontrado
            }
            showValidationError("Por favor, selecione um valor para " + fieldName + ".");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    // Valida se o ComboBox Ano da Fatura não está com a seleção padrão
    public static boolean validateComboBoxCartao(ComboBox<Cartao> comboBox, String fieldName, boolean[] hasError) {
        // Verifica se o ComboBox está vazio ou se o valor selecionado é nulo
        if (comboBox.getValue() == null) {
            if (!hasError[0]) {
                comboBox.requestFocus(); // Adiciona foco ao primeiro ComboBox inválido encontrado
            }
            showValidationError("Por favor, selecione um valor para " + fieldName + ".");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    // Exibe um alerta de erro de validação
    private static void showValidationError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro de Validação");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Valida se o ComboBox não está com a seleção padrão

    public static boolean validateComboBoxCategoria(ComboBox<Categoria> comboBox, String fieldName, boolean[] hasError) {
        if (comboBox.getValue() == null || "Selecione".equals(comboBox.getValue())) {
            if (!hasError[0]) {
                comboBox.requestFocus(); // Adiciona foco ao primeiro ComboBox inválido encontrado
            }
            showValidationError("Por favor, selecione um valor para " + fieldName + ".");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    public static boolean validateComboBoxResponsavel(ComboBox<Responsavel> comboBox, String fieldName, boolean[] hasError) {
        if (comboBox.getValue() == null || "Selecione".equals(comboBox.getValue())) {
            if (!hasError[0]) {
                comboBox.requestFocus(); // Adiciona foco ao primeiro ComboBox inválido encontrado
            }
            showValidationError("Por favor, selecione um valor para " + fieldName + ".");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    public static boolean validateComboBoxFatura(ComboBox<Fatura> comboBox, String fieldName, boolean[] hasError) {
        if (comboBox.getValue() == null || "Selecione".equals(comboBox.getValue())) {
            if (!hasError[0]) {
                comboBox.requestFocus(); // Adiciona foco ao primeiro ComboBox inválido encontrado
            }
            showValidationError("Por favor, selecione um valor para " + fieldName + ".");
            hasError[0] = true;
            return false;
        }
        return true;
    }

    public static boolean validateData(DatePicker date, String fieldName, boolean[] hasError) {
        if (date.getValue() == null || "Selecione".equals(date.getValue())) {
            if (!hasError[0]) {
                date.requestFocus(); // Adiciona foco ao primeiro ComboBox inválido encontrado
            }
            showValidationError("Por favor, selecione um valor para " + fieldName + ".");
            hasError[0] = true;
            return false;
        }
        return true;
    }
}
