/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.dao;

import com.marjax.finansys.connection.MySQLConnection;
import com.marjax.finansys.model.Ano;
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
public class AnoDAO {
    // Método para verificar se um ano já existe
    public boolean existe(String nome) {
        String sql = "SELECT COUNT(*) FROM ano WHERE valor = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nome);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return false;
    }
    //metodo para salvar responsavel
    public void salvar(Ano ano) {        

        if (existe(ano.getValor())) {
            AlertUtil.showErrorAlert("Erro", "Ano já cadastrado", "O ano já está cadastrado no sistema.");
            return;
        }

        String sql = "INSERT INTO ano (valor) VALUES (?)";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, ano.getValor());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                AlertUtil.showInformationAlert("Sucesso", "Ano Cadastrado", "Ano cadastrado com sucesso.");
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

    //metodo para listar responsaveis
    public ObservableList<Ano> getAll() {
        String sql = "SELECT * FROM ano ORDER BY valor ASC";
        ObservableList<Ano> anos = FXCollections.observableArrayList();
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Ano ano = new Ano();
                ano.setCodigo(rs.getInt("codigo"));
                ano.setValor(rs.getString("valor"));
                anos.add(ano);
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return anos;
    }

    // Método para excluir 
    public boolean excluir(int codigo) {
        String sql = "DELETE FROM ano WHERE codigo = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para atualizar o ano
    public boolean atualizar(Ano ano) {
        
        if (existe(ano.getValor())) {
            AlertUtil.showErrorAlert("Erro", "Ano já cadastrado", "O ano já está cadastrado no sistema.");
            return false; // 
        }

        String sql = "UPDATE ano SET valor = ? WHERE codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ano.getValor());
            preparedStatement.setInt(2, ano.getCodigo());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Método para saber a quantidade de anos cadastrados
    public int getTotal() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM ano";
        
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }        
        return total;
    }
}
