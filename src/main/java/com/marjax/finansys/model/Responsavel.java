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
public class Responsavel {
    private final IntegerProperty codigo;
    private final StringProperty nome;
    
    public Responsavel(int _codigo, String _nome) {
        this.codigo = new SimpleIntegerProperty(_codigo);
        this.nome = new SimpleStringProperty(_nome);               
    }
    
    public int getCodigo() {
        return codigo.get();
    }

    public void setCodigo(int codigo) {
        this.codigo.set(codigo);
    }
    public IntegerProperty codigoProperty() {
        return codigo;
    }
    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }       
}
