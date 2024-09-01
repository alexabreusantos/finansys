/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;



/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class UtilClass {
    public static void horaAtual(Label label){
        // Formatar a hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Atualiza o Label com a hora atual do computador
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            label.setText(currentTime.format(formatter));
        }),
        new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }
    public static void dataAtual(Label label){
        Locale locale = Locale.forLanguageTag("pt-BR");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", locale);
        LocalDate currentDate = LocalDate.now();
        label.setText(currentDate.format(dateFormatter));
    }
}
