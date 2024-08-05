/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.model;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */

public class Cartao {

    private Integer codigo;        
    private String nome; 
    private Double limite;
    private Double limiteDisponivel;
    private Double limiteUsado;
    private Integer fechamento;
    private Integer vencimento;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Double getLimiteDisponivel() {
        return limiteDisponivel;
    }

    public void setLimiteDisponivel(Double limiteDisponivel) {
        this.limiteDisponivel = limiteDisponivel;
    }

    public Double getLimiteUsado() {
        return limiteUsado;
    }

    public void setLimiteUsado(Double limiteUsado) {
        this.limiteUsado = limiteUsado;
    }

    public Integer getFechamento() {
        return fechamento;
    }

    public void setFechamento(Integer fechamento) {
        this.fechamento = fechamento;
    }

    public Integer getVencimento() {
        return vencimento;
    }

    public void setVencimento(Integer vencimento) {
        this.vencimento = vencimento;
    }
}
