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
import com.marjax.finansys.util.AlertUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class CompraDAO {

    // Método para verificar se uma compra já existe
    public boolean existe(Compra compra) {
        String sql = "SELECT COUNT(*) FROM compra WHERE descricao = ? AND responsavel = ? AND fatura = ? AND valor = ? AND categoria = ? AND  dataCompra = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, compra.getDescricao());
            stmt.setInt(2, compra.getResponsavel().getCodigo());
            stmt.setInt(3, compra.getFatura().getCodigo());
            stmt.setDouble(4, compra.getValor());
            stmt.setInt(5, compra.getCategoria().getCodigo());
            stmt.setDate(6, compra.getDataCompra());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // Aqui você pode usar um método utilitário para mostrar um alerta de erro
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return false;
    }

    // Método para salvar compra única
    public void salvarCompraUnica(Compra compra) {
        if (existe(compra)) {
            return;
        }
        String sql = "INSERT INTO compra (descricao, responsavel, fatura, valor, categoria, dataCompra, valorTotal, situacao, tipoCompra ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, compra.getDescricao());
            psmt.setInt(2, compra.getResponsavel().getCodigo());
            psmt.setInt(3, compra.getFatura().getCodigo());
            psmt.setDouble(4, compra.getValor());
            psmt.setInt(5, compra.getCategoria().getCodigo());
            psmt.setDate(6, compra.getDataCompra());
            psmt.setDouble(7, compra.getValorTotal());
            psmt.setString(8, compra.getSituacao());
            psmt.setString(9, compra.getTipoCompra());

            int rowsAffected = psmt.executeUpdate();

            if (rowsAffected > 0) {
                AlertUtil.showInformationAlert("Sucesso", "Compra cadastrada!", "A compra foi cadastrada com sucesso.");
            }

        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro ao salvar", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

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

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
        }
        return total;
    }

    // Método para atualizar a situação de acordo com o checkbox marcado
    public void atualizarSituacao(Compra compra) {
        String sql = "UPDATE compra SET situacao = ? WHERE fatura = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, compra.getSituacao());
            preparedStatement.setInt(2, compra.getFatura().getCodigo()); // Utiliza o código da compra
            preparedStatement.executeUpdate();
        } catch (Exception e) {
        }
    }

    // Método para atualizar a situação das compras de acordo com a situação da fatura
    public void atualizarSituacaoCompras(Fatura fatura) {
        String sql = "UPDATE compra SET situacao = ? WHERE fatura = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fatura.getSituacao());
            preparedStatement.setInt(2, fatura.getCodigo()); // Utiliza o código da compra
            preparedStatement.executeUpdate();
        } catch (Exception e) {
        }
    }

    // Método para listar as compras na tabela
    public ObservableList<Compra> listarComprasPorPeriodo(String periodo) {
        String sql = "SELECT compra.codigo, compra.descricao, compra.valor, compra.parcelaAtual, compra.parcelaTotal, compra.dataCompra, compra.valorTotal, compra.situacao, compra.tipoCompra, \n"
                + "responsavel.codigo AS codigoResponsavel, responsavel.nome AS nomeResponsavel, fatura.codigo AS codigoFatura, fatura.periodo AS periodoFatura, categoria.codigo AS codigoCategoria, \n"
                + "categoria.nome AS nomeCategoria, cartao.codigo AS codigoCartao, cartao.nome AS nomeCartao FROM compra JOIN fatura ON compra.fatura = fatura.codigo \n"
                + "JOIN cartao ON fatura.cartao = cartao.codigo JOIN responsavel ON compra.responsavel = responsavel.codigo JOIN categoria ON compra.categoria = categoria.codigo \n"
                + "WHERE DATE_FORMAT(fatura.periodo, '%Y-%m') = ?";

        ObservableList<Compra> compras = FXCollections.observableArrayList();

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Define o valor do parâmetro "periodo"
            stmt.setString(1, periodo);

            try (ResultSet rs = stmt.executeQuery()) {
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
                    compra.setTipoCompra(rs.getString("tipoCompra"));

                    // Preenche o Responsavel
                    Responsavel responsavel = new Responsavel();
                    responsavel.setCodigo(rs.getInt("codigoResponsavel"));
                    responsavel.setNome(rs.getString("nomeResponsavel"));
                    compra.setResponsavel(responsavel);

                    // Preenche a Fatura e o Cartão
                    Fatura fatura = new Fatura();
                    fatura.setCodigo(rs.getInt("codigoFatura"));
                    fatura.setPeriodo(rs.getDate("periodoFatura"));

                    Cartao cartao = new Cartao();
                    cartao.setNome(rs.getString("nomeCartao"));
                    fatura.setCartao(cartao);
                    compra.setFatura(fatura);

                    // Preenche a Categoria
                    Categoria categoria = new Categoria();
                    categoria.setCodigo(rs.getInt("codigoCategoria"));
                    categoria.setNome(rs.getString("nomeCategoria"));
                    compra.setCategoria(categoria);

                    // Adiciona à lista de compras
                    compras.add(compra);
                }
            }
        } catch (Exception e) {
        }

        return compras;
    }
    
    // Método para recuperar as compras parceladas
    public List<Compra> listarComprasParceladas(String periodo) {
        List<Compra> comprasParceladas = new ArrayList<>();
        String sql = "SELECT compra.codigo, compra.descricao, compra.valor, compra.parcelaAtual, compra.parcelaTotal, compra.dataCompra, compra.valorTotal, compra.situacao, compra.tipoCompra, \n"
                + "responsavel.codigo AS codigoResponsavel, responsavel.nome AS nomeResponsavel, fatura.codigo AS codigoFatura, fatura.periodo AS periodoFatura, categoria.codigo AS codigoCategoria, \n"
                + "categoria.nome AS nomeCategoria, cartao.codigo AS codigoCartao, cartao.nome AS nomeCartao FROM compra JOIN fatura ON compra.fatura = fatura.codigo \n"
                + "JOIN cartao ON fatura.cartao = cartao.codigo JOIN responsavel ON compra.responsavel = responsavel.codigo JOIN categoria ON compra.categoria = categoria.codigo \n"
                + "WHERE compra.tipoCompra = 'Parcelada' AND DATE_FORMAT(fatura.periodo, '%Y-%m') = ?";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, periodo);

            try (ResultSet rs = stmt.executeQuery()) {
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
                    compra.setTipoCompra(rs.getString("tipoCompra"));

                    // Preenche o Responsavel
                    Responsavel responsavel = new Responsavel();
                    responsavel.setCodigo(rs.getInt("codigo"));
                    responsavel.setNome(rs.getString("nome"));
                    compra.setResponsavel(responsavel);

                    // Preenche a Fatura e o Cartão
                    Fatura fatura = new Fatura();
                    fatura.setCodigo(rs.getInt("codigo"));
                    fatura.setPeriodo(rs.getDate("periodo"));

                    Cartao cartao = new Cartao();
                    cartao.setCodigo(rs.getInt("codigo"));
                    cartao.setNome(rs.getString("nomeCartao"));
                    fatura.setCartao(cartao);
                    compra.setFatura(fatura);

                    // Preenche a Categoria
                    Categoria categoria = new Categoria();
                    categoria.setCodigo(rs.getInt("codigo"));
                    categoria.setNome(rs.getString("nome"));
                    compra.setCategoria(categoria);

                    comprasParceladas.add(compra);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return comprasParceladas;
    }
    
    // Método para salvar as compras parceladas
    public void salvarCompraParcelada(List<Compra> compras) {
        String sql = "INSERT INTO compra (descricao, responsavel, fatura, valor, categoria, parcelaAtual, parcelaTotal, dataCompra, valorTotal, situacao, tipoCompra) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement psmt = connection.prepareStatement(sql)) {

            for (Compra compra : compras) {
                if (existe(compra)) {
                    continue;
                }
                psmt.setString(1, compra.getDescricao());
                psmt.setInt(2, compra.getResponsavel().getCodigo());
                psmt.setInt(3, compra.getFatura().getCodigo());
                psmt.setDouble(4, compra.getValor());
                psmt.setInt(5, compra.getCategoria().getCodigo());
                psmt.setInt(6, compra.getParcelaAtual());
                psmt.setInt(7, compra.getParcelaTotal());
                psmt.setDate(8, compra.getDataCompra());
                psmt.setDouble(9, compra.getValorTotal());
                psmt.setString(10, compra.getSituacao());
                psmt.setString(11, compra.getTipoCompra());

                psmt.executeUpdate();
            }

            // Exibir alerta de sucesso apenas uma vez após a conclusão da inserção
            //AlertUtil.showInformationAlert("Sucesso", "Compra cadastrada!", "A compra foi cadastrada com sucesso.");
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro ao salvar", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

    // Método para salvar as compras mensais
    public void salvarCompraMensal(List<Compra> compras) {
        String sql = "INSERT INTO compra (descricao, responsavel, fatura, valor, categoria, dataCompra, valorTotal, situacao, tipoCompra) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement psmt = connection.prepareStatement(sql)) {

            for (Compra compra : compras) {
                if (existe(compra)) {
                    continue;
                }
                psmt.setString(1, compra.getDescricao());
                psmt.setInt(2, compra.getResponsavel().getCodigo());
                psmt.setInt(3, compra.getFatura().getCodigo());
                psmt.setDouble(4, compra.getValor());
                psmt.setInt(5, compra.getCategoria().getCodigo());
                psmt.setDate(6, compra.getDataCompra());
                psmt.setDouble(7, compra.getValorTotal());
                psmt.setString(8, compra.getSituacao());
                psmt.setString(9, compra.getTipoCompra());

                psmt.executeUpdate();
            }

            // Exibir alerta de sucesso apenas uma vez após a conclusão da inserção
            //AlertUtil.showInformationAlert("Sucesso", "Compra cadastrada!", "A compra foi cadastrada com sucesso.");
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro ao salvar", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

    // Método para recuperar as compras mensais no mes atual
    public List<Compra> buscarComprasMensaisNoMesAtual() {
        // Obter a data do primeiro e último dia do mês atual
        LocalDate primeiroDia = LocalDate.now().withDayOfMonth(1);
        LocalDate ultimoDia = LocalDate.now().withDayOfMonth(primeiroDia.lengthOfMonth());

        // Criar a consulta SQL para verificar compras mensais no mês atual
        String sql = "SELECT compra.codigo, compra.descricao, compra.valor, compra.parcelaAtual, compra.parcelaTotal, compra.dataCompra, compra.valorTotal, compra.situacao, compra.tipoCompra, \n"
                + "                responsavel.codigo AS codigoResponsavel, responsavel.nome AS nomeResponsavel, fatura.codigo AS codigoFatura, fatura.periodo AS periodoFatura, categoria.codigo AS codigoCategoria,\n"
                + "                categoria.nome AS nomeCategoria, cartao.codigo AS codigoCartao, cartao.nome AS nomeCartao FROM compra JOIN fatura ON compra.fatura = fatura.codigo\n"
                + "                JOIN cartao ON fatura.cartao = cartao.codigo JOIN responsavel ON compra.responsavel = responsavel.codigo JOIN categoria ON compra.categoria = categoria.codigo\n"
                + "                WHERE compra.tipoCompra = 'Mensal' AND dataCompra BETWEEN ? AND ? ";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setDate(1, Date.valueOf(primeiroDia));
            psmt.setDate(2, Date.valueOf(ultimoDia));

            ResultSet rs = psmt.executeQuery();
            List<Compra> compras = new ArrayList<>();

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
                compra.setTipoCompra(rs.getString("tipoCompra"));

                // Preenche o Responsavel
                Responsavel responsavel = new Responsavel();
                responsavel.setCodigo(rs.getInt("codigoResponsavel"));
                responsavel.setNome(rs.getString("nomeResponsavel"));
                compra.setResponsavel(responsavel);

                // Preenche a Fatura e o Cartão
                Fatura fatura = new Fatura();
                fatura.setCodigo(rs.getInt("codigoFatura"));
                fatura.setPeriodo(rs.getDate("periodoFatura"));

                Cartao cartao = new Cartao();
                cartao.setCodigo(rs.getInt("codigoCartao"));
                cartao.setNome(rs.getString("nomeCartao"));
                fatura.setCartao(cartao);
                compra.setFatura(fatura);

                // Preenche a Categoria
                Categoria categoria = new Categoria();
                categoria.setCodigo(rs.getInt("codigoCategoria"));
                categoria.setNome(rs.getString("nomeCategoria"));
                compra.setCategoria(categoria);

                compras.add(compra);
            }
            return compras;

        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro ao salvar", "Erro de SQL", "Erro: " + e.getMessage());
            return null;
        }
    }
}
