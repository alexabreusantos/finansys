/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    private final DoubleProperty limite;
    private final DoubleProperty limiteDisponivel;
    private final DoubleProperty limiteUsado;
    private final IntegerProperty fechamento;
    private final IntegerProperty vencimento;

    public Cartao(int _codigo, String _nome, double _limite, double _limiteDisponivel, double _limiteUsado, int _fechamento, int _vencimento) {
        this.codigo = new SimpleIntegerProperty(_codigo);
        this.nome = new SimpleStringProperty(_nome);
        this.limite = new SimpleDoubleProperty(_limite);
        this.limiteDisponivel = new SimpleDoubleProperty(_limiteDisponivel);
        this.limiteUsado = new SimpleDoubleProperty(_limiteUsado);
        this.fechamento = new SimpleIntegerProperty(_fechamento);
        this.vencimento = new SimpleIntegerProperty(_vencimento);
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
    public double getLimite(){
        return limite.get();
    }
    public void setLimite(double limite){
        this.limite.set(limite);
    }
    public DoubleProperty limiteProperty(){
        return limite;
    }
    public double getLimiteDisponivel(){
        return limiteDisponivel.get();
    }
    public void setLimiteDisponivel(double limiteDisponivel){
        this.limiteDisponivel.set(limiteDisponivel);
    }
    public DoubleProperty limiteDisponivelProperty(){
        return limiteDisponivel;
    }
    public double getLimiteusado(){
        return limiteUsado.get();
    }
    public void setLimiteUsado(double limiteUsado){
        this.limiteUsado.set(limiteUsado);
    }
    public DoubleProperty limiteUsadoProperty(){
        return limiteUsado;
    }
    public int getFechamento() {
        return fechamento.get();
    }
    public void setFechamento(int fechamento) {
        this.fechamento.set(fechamento);
    }
    public IntegerProperty fechamentoProperty() {
        return fechamento;
    }
    public int getVencimento() {
        return vencimento.get();
    }
    public void setVencimento(int vencimento) {
        this.vencimento.set(vencimento);
    }
    public IntegerProperty vencimentoProperty() {
        return vencimento;
    }
}
