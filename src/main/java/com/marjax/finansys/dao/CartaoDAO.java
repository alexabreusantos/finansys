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

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class CartaoDAO {
    //metodo para listar os cartões
    public List<Cartao> getAllResponsaveis() throws SQLException {
        String sql = "SELECT * FROM cartao order by nome asc";
        List<Cartao> cartoes = new ArrayList<>();       
        try {          	
            Connection connection = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql); 
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
               int codigo = resultSet.getInt("codigo");
               String nome = resultSet.getString("nome"); 
               double limite = resultSet.getDouble("limite");
               double limiteDisponivel = resultSet.getDouble("limiteDisponivel");
               double limiteUsado = resultSet.getDouble("limiteUsado");
               int fechamento = resultSet.getInt("fechamento");
               int vencimento = resultSet.getInt("vencimento");
               cartoes.add(new Cartao(codigo, nome, limite, limiteDisponivel, limiteUsado, fechamento, vencimento));
           }          
        }
        catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e);
        }
        return cartoes;
    }
}
