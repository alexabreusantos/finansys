/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.dao;

import com.marjax.finansys.connection.MySQLConnection;
import com.marjax.finansys.model.Responsavel;
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
public class ResponsavelDAO {
    
    //metodo para listar os responsaveis
    public List<Responsavel> getAllResponsaveis() throws SQLException {
        String sql = "SELECT * FROM responsavel order by nome asc";
        List<Responsavel> responsaveis = new ArrayList<>();       
        try {          	
            Connection connection = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql); 
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
               int codigo = resultSet.getInt("codigo");
               String nome = resultSet.getString("nome");               
               responsaveis.add(new Responsavel(codigo, nome));
           }          
        }
        catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e);
        }
        return responsaveis;
    }
}
