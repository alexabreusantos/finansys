/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.dao;

import com.marjax.finansys.connection.MySQLConnection;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.util.AlertUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class FaturaDAO {

    // Método para verificar se um responsável já existe
    public boolean existe(Date periodo, int cartao, int codigo) {
        String sql = "SELECT COUNT(*) FROM fatura WHERE periodo = ? AND cartao = ? AND codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, periodo);
            preparedStatement.setInt(2, cartao);
            preparedStatement.setInt(3, codigo);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            //AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return false;
    }

    // metodo para listar todas as faturas
    public ObservableList<Fatura> getAllFaturas() {
        String sql = "SELECT f.codigo, f.periodo, f.valor, c.nome, f.situacao FROM fatura f JOIN cartao c ON f.cartao = c.codigo ORDER BY f.periodo ASC";
        ObservableList<Fatura> faturas = FXCollections.observableArrayList();
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Fatura fatura = new Fatura();
                fatura.setCodigo(rs.getInt("codigo"));
                fatura.setPeriodo(rs.getDate("periodo"));
                fatura.setValor(rs.getDouble("valor"));
                fatura.setSituacao(rs.getString("situacao"));

                Cartao cartao = new Cartao();
                cartao.setCodigo(rs.getInt("codigo"));
                cartao.setNome(rs.getString("nome"));
                fatura.setCartao(cartao);

                faturas.add(fatura);
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e);
        }
        return faturas;
    }

    // Método para saber a quantidade de cartoes cadastrados 
    public int getTotalFaturas() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM fatura";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Método para salvar 
    public void salvar(Fatura fatura) {

        if (existe(fatura.getPeriodo(), fatura.getCartao().getCodigo(), fatura.getCodigo())) {
            return;
        }
        
        String sql = "INSERT INTO fatura (periodo, valor, situacao, cartao) VALUES (?, ?, ?, ?)";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, fatura.getPeriodo()); // Período da fatura
            preparedStatement.setDouble(2, fatura.getValor()); // Valor da fatura
            preparedStatement.setString(3, fatura.getSituacao()); // Situação da fatura
            preparedStatement.setInt(4, fatura.getCartao().getCodigo()); // Código do cartão

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                AlertUtil.showInformationAlert("Sucesso", "Fatura cadastrada!", "A fatura foi cadastrada com sucesso.");
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro ao salvar", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

    // Método para excluir uma fatura
    public boolean excluir(int codigo) {
        String sql = "DELETE FROM fatura WHERE codigo = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para atualizar a fatura
    public boolean atualizar(Fatura fatura) {

        if (existe(fatura.getPeriodo(), fatura.getCartao().getCodigo(), fatura.getCodigo())) {
            return false; // Nome já existe no banco de dados
        }
        String sql = "UPDATE fatura SET periodo = ?, valor = ?, situacao = ?, cartao = ? WHERE codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, fatura.getPeriodo());
            preparedStatement.setDouble(2, fatura.getValor());
            preparedStatement.setString(3, fatura.getSituacao());
            preparedStatement.setInt(4, fatura.getCartao().getCodigo());
            preparedStatement.setInt(5, fatura.getCodigo());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
}
