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

    // Método para verificar se um cartao já existe
    public boolean existeCartao(String nome) {
        String sql = "SELECT COUNT(*) FROM cartao WHERE nome = ?";
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
    public void salvar(Cartao cartao) {

        if (existeCartao(cartao.getNome())) {
            AlertUtil.showErrorAlert("Erro", "Categoria já cadastrada", "A categoria já está cadastrada no sistema.");
            return;
        }

        String sql = "INSERT INTO cartao (nome, limite, limiteDisponivel, limiteUsado, fechamento, vencimento) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, cartao.getNome());
            preparedStatement.setDouble(2, cartao.getLimite());
            preparedStatement.setDouble(3, cartao.getLimiteDisponivel());
            preparedStatement.setDouble(4, cartao.getLimiteUsado());
            preparedStatement.setInt(5, cartao.getFechamento());
            preparedStatement.setInt(6, cartao.getVencimento());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                AlertUtil.showInformationAlert("Sucesso", "Cartão cadastrado!", "O cartão foi cadastrado com sucesso.");
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

    // Método para atualizar o cartao
    public boolean atualizar(Cartao cartao) {

        String sql = "UPDATE cartao SET nome = ?, limite = ?, limiteDisponivel = ?, limiteUsado = ?, fechamento = ?, vencimento = ? WHERE codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cartao.getNome());
            preparedStatement.setDouble(2, cartao.getLimite());
            preparedStatement.setDouble(3, cartao.getLimiteDisponivel());
            preparedStatement.setDouble(4, cartao.getLimiteUsado());
            preparedStatement.setInt(5, cartao.getFechamento());
            preparedStatement.setInt(6, cartao.getVencimento());
            preparedStatement.setInt(7, cartao.getCodigo());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                AlertUtil.showInformationAlert("Sucesso", "Cartão alterado!", "O cartão foi alterado com sucesso.");
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return false;
    }

    //metodo para listar os cartões
    public ObservableList<Cartao> getAllCartoes() {
        String sql = "SELECT * FROM cartao order by nome asc";
        ObservableList<Cartao> cartoes = FXCollections.observableArrayList();
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {
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
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e);
        }
        return cartoes;
    }

    // Método para verificar se um cartao já existe
    public boolean podeExcluirCartao(int codigo) {
        String sql = "SELECT COUNT(*) FROM fatura WHERE cartao = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, codigo);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Número de faturas associadas: " + count); // Adicionado para depuração
                return count == 0;  // Retorna true se não há faturas associadas
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return false;
    }

    // Método para excluir um cartao
    public boolean excluirCartao(int codigo) {

        if (!podeExcluirCartao(codigo)) {
            return false;
        }

        String sql = "DELETE FROM cartao WHERE codigo = ?";
        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    // Método para saber a quantidade de cartoes cadastrados 
    public int getTotalCartoes() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM cartao";

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
    public List<Cartao> buscarCartoes() {
        List<Cartao> cartoes = new ArrayList<>();
        String sql = "SELECT * FROM cartao ORDER BY nome";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

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

        } catch (Exception e) {
            e.printStackTrace(); // Aqui você pode usar uma abordagem de logging ou lançar uma exceção customizada
        }

        return cartoes;
    }
}
