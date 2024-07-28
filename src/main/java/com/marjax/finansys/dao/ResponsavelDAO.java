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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ResponsavelDAO {

    //metodo para listar os responsaveis
    public ObservableList<Responsavel> getAllResponsaveis() {
        String sql = "SELECT * FROM responsavel order by nome asc";
        ObservableList<Responsavel> responsaveis = FXCollections.observableArrayList();
        try {
            Connection connection = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Responsavel responsavel = new Responsavel();
                responsavel.setCodigo(rs.getInt("codigo"));
                responsavel.setNome(rs.getString("nome"));
                responsaveis.add(responsavel);
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e);
        }
        return responsaveis;
    }
}
