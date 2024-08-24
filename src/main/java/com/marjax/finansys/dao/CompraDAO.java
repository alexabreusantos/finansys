/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.dao;

import com.marjax.finansys.connection.MySQLConnection;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Categoria;
import com.marjax.finansys.model.Compra;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.model.Responsavel;
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
public class CompraDAO {

    // método para listar as compras na tabela
    public ObservableList<Compra> getAllCompras() {
        String sql = "SELECT "
                + "compra.codigo, compra.descricao, compra.valor, compra.parcelaAtual, compra.parcelaTotal, compra.dataCompra, compra.valorTotal, compra.situacao, "
                + "responsavel.codigo AS codigoResponsavel, responsavel.nome AS nomeResponsavel, "
                + "fatura.codigo AS codigoFatura, fatura.periodo AS periodoFatura, "
                + "categoria.codigo AS codigoCategoria, categoria.nome AS nomeCategoria, "
                + "cartao.codigo AS codigoCartao, cartao.nome AS nomeCartao "
                + "FROM compra "
                + "JOIN fatura ON compra.fatura = fatura.codigo "
                + "JOIN cartao ON fatura.cartao = cartao.codigo "
                + "JOIN responsavel ON compra.responsavel = responsavel.codigo "
                + "JOIN categoria ON compra.categoria = categoria.codigo";

        ObservableList<Compra> compras = FXCollections.observableArrayList();
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setCodigo(rs.getInt("codigo"));
                compra.setDescricao(rs.getString("descricao"));
                compra.setValor(rs.getDouble("valor"));
                compra.setParcelaAtual(rs.getInt("parcelaAtual"));
                compra.setParcelaTotal(rs.getInt("parcelaTotal"));
                compra.setDataCompra(rs.getDate("dataCompra"));
                compra.setValorTotal(rs.getDouble("valorTotal"));
                compra.setSituacao(rs.getString("situacao"));

                Responsavel responsavel = new Responsavel();
                responsavel.setCodigo(rs.getInt("codigoResponsavel"));
                responsavel.setNome(rs.getString("nomeResponsavel"));
                compra.setResponsavel(responsavel);

                Fatura fatura = new Fatura();
                fatura.setCodigo(rs.getInt("codigoFatura"));
                fatura.setPeriodo(rs.getDate("periodoFatura"));

                // Instancia e define o Cartão
                Cartao cartao = new Cartao();
                cartao.setNome(rs.getString("nomeCartao"));
                fatura.setCartao(cartao);
                compra.setFatura(fatura);

                Categoria categoria = new Categoria();
                categoria.setCodigo(rs.getInt("codigoCategoria"));
                categoria.setNome(rs.getString("nomeCategoria"));
                compra.setCategoria(categoria);

                compras.add(compra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compras;
    }
    
    // Método para saber a quantidade de compras cadastradas 
    public int getTotal() {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM compra";
        
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
        }        
        return total;
    }
}
