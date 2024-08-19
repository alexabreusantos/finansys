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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class FaturaDAO {

    // Método para verificar se já existe uma fatura cadastrada
    public boolean existeFatura(Timestamp periodo, int codigoCartao) {
        String sql = "SELECT COUNT(*) FROM fatura WHERE periodo = ? and cartao = ?";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, periodo);
            preparedStatement.setInt(2, codigoCartao);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro de SQL", "Erro no método existeFatura", "Erro: " + e.getMessage());
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
                fatura.setPeriodo(rs.getObject("periodo", Timestamp.class));
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
        try {

            if (existeFatura(fatura.getPeriodo(), fatura.getCartao().getCodigo())) {
                throw new SQLException("Já existe uma fatura para este cartão no período especificado.");
            }

            String sql = "INSERT INTO fatura (periodo, valor, situacao, cartao) VALUES (?, ?, ?, ?)";
            try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setTimestamp(1, fatura.getPeriodo()); // Período da fatura
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
        } catch (SQLException ex) {
            Logger.getLogger(FaturaDAO.class.getName()).log(Level.SEVERE, null, ex);
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
}
