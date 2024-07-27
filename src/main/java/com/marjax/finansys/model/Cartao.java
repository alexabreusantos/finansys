/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class Cartao {
    private final IntegerProperty codigo;
    private final StringProperty nome;
    
    public Cartao (int _codigo, String _nome) {
        this.codigo = new SimpleIntegerProperty(_codigo);
        this.nome = new SimpleStringProperty(_nome);               
    }
}
