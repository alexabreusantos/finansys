/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class AlertUtil {
    public static void showInformationAlert(String title, String header, String content) {
        showAlert(AlertType.INFORMATION, title, header, content);
    }

    public static void showWarningAlert(String title, String header, String content) {
        showAlert(AlertType.WARNING, title, header, content);
    }

    public static void showErrorAlert(String title, String header, String content) {
        showAlert(AlertType.ERROR, title, header, content);
    }

    public static void showConfirmationAlert(String title, String header, String content) {
        showAlert(AlertType.CONFIRMATION, title, header, content);
    }

    private static void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
