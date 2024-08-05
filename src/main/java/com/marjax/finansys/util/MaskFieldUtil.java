/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import javafx.scene.input.KeyEvent;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class MaskFieldUtil {

    private static List<KeyCode> ignoreKeyCodes;

    static {
        ignoreKeyCodes = new ArrayList<>();
        Collections.addAll(ignoreKeyCodes, new KeyCode[]{KeyCode.F1, KeyCode.F2, KeyCode.F3, KeyCode.F4, KeyCode.F5, KeyCode.F6, KeyCode.F7, KeyCode.F8, KeyCode.F9, KeyCode.F10, KeyCode.F11, KeyCode.F12});
    }

    public static void ignoreKeys(final TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (ignoreKeyCodes.contains(keyEvent.getCode())) {
                keyEvent.consume();
            }
        });
    }

    public static void monetaryField(final TextField textField) {
        textField.setAlignment(Pos.CENTER_LEFT);
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            formatTextField(textField);
            textField.textProperty().addListener((observableValue, oldText, newText) -> {
                if (newText.length() > 17) {
                    textField.setText(oldText);
                }
            });
        });

        textField.focusedProperty().addListener((observableValue, aBoolean, fieldChange) -> {
            if (!fieldChange) {
                final int length = textField.getText().length();
                if (length > 0 && length < 3) {
                    textField.setText(textField.getText() + "00");
                }
            }
        });
    }

    private static void formatTextField(final TextField textField) {
        String value = textField.getText();
        value = formatValue(Double.parseDouble(value.replace(".", "").replace(",", ".")));
        textField.setText(value);
        positionCaret(textField);
    }

    private static String formatValue(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(value);
    }

    private static void positionCaret(final TextField textField) {
        Platform.runLater(() -> textField.positionCaret(textField.getText().length()));
    }

    public static void setInitialText(final TextField textField, double value) {
        textField.setText(formatValue(value));
        formatTextField(textField);
    }

    public static String getFormattedValue(double value) {
        return formatValue(value);
    }
}
