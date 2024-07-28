/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.dao;

import com.marjax.finansys.connection.MySQLConnection;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.util.AlertUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class CartaoDAO {
    //metodo para listar os cartões
    public ObservableList<Cartao> getAllCartoes() {
        String sql = "SELECT * FROM cartao order by nome asc";
        ObservableList<Cartao> cartoes = FXCollections.observableArrayList();       
        try {          	
            Connection connection = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql); 
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
               Cartao cartao = new Cartao();
               cartao.setCodigo(rs.getInt("codigo"));
               cartao.setNome(rs.getString("nome"));
               cartao.setLimite(rs.getDouble("limite"));
               cartao.setLimiteDisponivel(rs.getDouble("limiteDisponivel"));
               cartao.setLimiteUsado(rs.getDouble("limiteUsado"));
               cartao.setFechamento(rs.getInt("fechamento"));
               cartao.setVencimento(rs.getInt("vencimento"));
               cartoes.add(cartao);
           }          
        }
        catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e);
        }
        return cartoes;
    }
}
