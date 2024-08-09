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
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class FaturaDAO {
    
    // metodo para listar todas as faturas
    public ObservableList<Fatura> getAllFaturas(){
        String sql = "SELECT f.codigo, f.periodo, f.valor, c.nome, f.situacao FROM fatura f JOIN cartao c ON f.cartao = c.codigo ORDER BY f.periodo ASC";
        ObservableList<Fatura> faturas = FXCollections.observableArrayList();
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()){
            while (rs.next()) {
                Fatura fatura = new Fatura();
                fatura.setCodigo(rs.getInt("codigo"));
                fatura.setPeriodo(rs.getObject("periodo", LocalDate.class));
                fatura.setValor(rs.getDouble("valor"));
                fatura.setSituacao(rs.getString("situacao"));
                
                Cartao cartao = new Cartao();
                cartao.setCodigo(rs.getInt("codigo"));
                cartao.setNome(rs.getString("nome"));
                fatura.setCartao(cartao);
                
                faturas.add(fatura);
            }
        }catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e);
        }
        return faturas;
    }

    // Método para saber a quantidade de cartoes cadastrados 
    public int getTotalFaturas() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM fatura";
        
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
