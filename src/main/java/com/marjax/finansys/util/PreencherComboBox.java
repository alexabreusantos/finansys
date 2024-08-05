/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import javafx.scene.control.ComboBox;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class PreencherComboBox {
    public static void ComboBoxDias(ComboBox dias) {        
        // Preenche o ComboBox com valores de 1 a 31
        for (int i = 1; i <= 31; i++) {
            dias.getItems().add(i);
        }
    }
}
