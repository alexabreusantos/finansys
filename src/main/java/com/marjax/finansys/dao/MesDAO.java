/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.dao;

import com.marjax.finansys.connection.MySQLConnection;
import com.marjax.finansys.model.Mes;
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
public class MesDAO {

    // Método para verificar se um mes já existe
    public boolean existe(String nome) {
        String sql = "SELECT COUNT(*) FROM mes WHERE nome = ?";
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
    public void salvar(Mes mes) {

        if (existe(mes.getNome())) {
            AlertUtil.showErrorAlert("Erro", "Mês já cadastrado", "O mês já está cadastrado no sistema.");
            return;
        }

        String sql = "INSERT INTO mes (nome, numero) VALUES (?, ?)";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, mes.getNome());
            preparedStatement.setString(2, mes.getNumero());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                AlertUtil.showInformationAlert("Sucesso", "Mês Cadastrado", "Mês cadastrado com sucesso.");
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

    //metodo para listar os meses
    public ObservableList<Mes> getAllMeses() {
        String sql = "SELECT * FROM mes ORDER BY numero ASC";
        ObservableList<Mes> meses = FXCollections.observableArrayList();
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Mes mes = new Mes();
                mes.setCodigo(rs.getInt("codigo"));
                mes.setNome(rs.getString("nome"));
                mes.setNumero(rs.getString("numero"));
                meses.add(mes);
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return meses;
    }

    // Método para excluir um mes
    public boolean excluir(int codigo) {
        String sql = "DELETE FROM mes WHERE codigo = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para atualizar o mes
    public boolean atualizar(Mes mes) {

        if (existe(mes.getNome())) {
            AlertUtil.showErrorAlert("Erro", "Mês já cadastrado", "O mês já está cadastrado no sistema.");
            return false; // Nome já existe no banco de dados
        }

        String sql = "UPDATE mes SET nome = ?, numero = ? WHERE codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, mes.getNome());
            preparedStatement.setString(2, mes.getNumero());
            preparedStatement.setInt(3, mes.getCodigo());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para saber a quantidade de categorias cadastradas 
    public int getTotal() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM mes";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Método para listar os nomes dos cartões no combobox
    public List<Mes> buscarNomesMeses() {
        List<Mes> meses = new ArrayList<>();
        String sql = "SELECT * FROM mes ORDER BY numero";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mes mes = new Mes();
                mes.setCodigo(rs.getInt("codigo"));
                mes.setNome(rs.getString("nome"));
                mes.setNumero(rs.getString("numero"));
                meses.add(mes);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Aqui você pode usar uma abordagem de logging ou lançar uma exceção customizada
        }

        return meses;
    }

    // Método para buscar um mês
    public Mes buscarMes(String nome) {
        Mes mes = null;
        String sql = "SELECT * FROM mes WHERE nome = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mes = new Mes();
                    mes.setCodigo(rs.getInt("codigo"));
                    mes.setNome(rs.getString("nome"));
                    mes.setNumero(rs.getString("numero"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Aqui você pode usar uma abordagem de logging ou lançar uma exceção customizada
        }
        return mes;
    }
}
