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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class FaturaDAO {

    // Método para verificar se um responsável já existe
    public boolean existe(Fatura fatura) {
        String sql = "SELECT COUNT(*) FROM fatura WHERE periodo = ? AND cartao = ? AND situacao = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, fatura.getPeriodo());
            preparedStatement.setInt(2, fatura.getCartao().getCodigo());
            preparedStatement.setString(3, fatura.getSituacao());

            try (ResultSet rs = preparedStatement.executeQuery()) {
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

    // metodo para listar todas as faturas
    public ObservableList<Fatura> getAllFaturas() {
        String sql = "SELECT f.codigo, f.periodo, f.valor, c.nome, c.fechamento, c.vencimento, f.situacao FROM fatura f JOIN cartao c ON f.cartao = c.codigo ORDER BY f.periodo ASC";
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
                cartao.setFechamento(rs.getInt("fechamento"));
                cartao.setVencimento(rs.getInt("vencimento"));
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
        }
        return total;
    }

    // Método para salvar no banco
    public void salvar(Fatura fatura) {

        if (existe(fatura)) {
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

    // Método para salvar lista de faturas
    public void salvarLista(List<Fatura> faturas) {
        String sql = "INSERT INTO fatura (periodo, valor, situacao, cartao) VALUES (?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Fatura fatura : faturas) {
                if (existe(fatura)) {
                    continue;
                }
                preparedStatement.setDate(1, fatura.getPeriodo()); // Período da fatura
                preparedStatement.setDouble(2, fatura.getValor()); // Valor da fatura
                preparedStatement.setString(3, fatura.getSituacao()); // Situação da fatura
                preparedStatement.setInt(4, fatura.getCartao().getCodigo()); // Código do cartão

                preparedStatement.executeUpdate();
            }
            AlertUtil.showInformationAlert("Sucesso", "Faturas cadastradas!", "As faturas para essa compra foram cadastradas com sucesso.");
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

        if (existe(fatura)) {
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

    public List<Cartao> buscarCartoesDistintos() {
        List<Cartao> cartoes = new ArrayList<>();
        String sql = "SELECT DISTINCT c.codigo, c.nome, c.fechamento, c.vencimento, c.limite, c.limiteDisponivel, c.limiteUsado FROM fatura f INNER JOIN cartao c ON f.cartao = c.codigo ORDER BY c.nome";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cartao cartao = new Cartao();
                cartao.setCodigo(rs.getInt("codigo"));
                cartao.setNome(rs.getString("nome"));
                cartao.setFechamento(rs.getInt("fechamento"));
                cartao.setVencimento(rs.getInt("vencimento"));
                cartao.setLimite(rs.getDouble("limite"));
                cartao.setLimiteDisponivel(rs.getDouble("limiteDisponivel"));
                cartao.setLimiteUsado(rs.getDouble("limiteUsado"));
                cartoes.add(cartao);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartoes;
    }

    public List<Fatura> buscarFaturasPorCartao(int cartao) {
        List<Fatura> faturas = new ArrayList<>();
        String sql = "SELECT codigo, periodo FROM fatura WHERE cartao = ? AND situacao = 'Pendente' ORDER BY periodo";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o valor do parâmetro para o código do cartão
            stmt.setInt(1, cartao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fatura fatura = new Fatura();
                    fatura.setCodigo(rs.getInt("codigo"));
                    fatura.setPeriodo(rs.getDate("periodo"));
                    // Preenche os campos da fatura conforme sua classe
                    faturas.add(fatura);
                }
            }

        } catch (SQLException e) {

        }

        return faturas;
    }

    public List<Fatura> carregarComboBoxFaturas() {
        List<Fatura> faturas = new ArrayList<>();
        String sql = "SELECT codigo, periodo, valor, situacao FROM fatura";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fatura fatura = new Fatura();
                fatura.setPeriodo(rs.getDate("periodo"));
                fatura.setSituacao(rs.getString("situacao"));
                fatura.setValor(rs.getDouble("valor"));
                fatura.setCodigo(rs.getInt("codigo"));
                faturas.add(fatura);
            }

        } catch (Exception e) {
            // Aqui você pode usar uma abordagem de logging ou lançar uma exceção customizada            
        }
        return faturas;
    }

    // Método para atualizar a situação de acordo com o checkbox marcado
    public void atualizarSituacao(Fatura fatura) {
        String sql = "UPDATE fatura SET situacao = ? WHERE codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fatura.getSituacao());
            preparedStatement.setInt(2, fatura.getCodigo()); // Utiliza o código da compra
            preparedStatement.executeUpdate();
        } catch (Exception e) {
        }
    }

    public void atualizarValor(Double valor, Integer codigo) {
        String sql = "UPDATE fatura SET valor = ? WHERE codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Define o valor a ser atualizado
            preparedStatement.setDouble(1, valor);  // Passa o valor da fatura

            // Define o código da fatura para identificar qual registro atualizar
            preparedStatement.setInt(2, codigo);

            // Executa a atualização
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        }

    }

    public Fatura buscarFaturaPorCartao(int codigo, Date periodo) {
        String sql = "SELECT codigo, periodo, valor, situacao FROM fatura WHERE cartao = ? AND DATE_FORMAT(fatura.periodo, '%Y-%m') = ? LIMIT 1";  // LIMIT 1 para garantir que apenas um resultado seja retornado
        Fatura fatura = null;
        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o valor do parâmetro para o código do cartão
            stmt.setInt(1, codigo);
            stmt.setDate(2, periodo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fatura = new Fatura();
                    fatura.setCodigo(rs.getInt("codigo"));
                    fatura.setPeriodo(rs.getDate("periodo"));
                    fatura.setValor(rs.getDouble("valor"));
                    fatura.setSituacao(rs.getString("situacao"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Adicione o tratamento de exceção
        }

        return fatura;
    }

    public Fatura buscarPorCartaoEPeriodo(Cartao cartao, LocalDate periodo) {
        Fatura fatura = new Fatura();
        String sql = "SELECT * FROM fatura WHERE cartao = ? AND EXTRACT(MONTH FROM periodo) = ? AND EXTRACT(YEAR FROM periodo) = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da consulta
            stmt.setInt(1, cartao.getCodigo()); // Assumindo que `cartao.getCodigo()` retorna o ID do cartão
            stmt.setInt(2, periodo.getMonthValue()); // Extrai o mês da data
            stmt.setInt(3, periodo.getYear()); // Extrai o ano da data

            // Executa a consulta
            try (ResultSet rs = stmt.executeQuery()) {
                // Se existir uma fatura para o período e cartão especificados
                if (rs.next()) {
                    fatura.setCodigo(rs.getInt("codigo"));
                    fatura.setPeriodo(rs.getDate("periodo"));
                    fatura.setValor(rs.getDouble("valor"));
                    fatura.setCartao(cartao);                    
                    fatura.setSituacao(rs.getString("situacao"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return fatura;
    }

    public List<Fatura> listarFaturasCartaoPeriodo(Cartao cartao, Date dataReferencia, int intervalo) {
        List<Fatura> faturas = new ArrayList<>();
        String sql = "SELECT * FROM fatura WHERE cartao = ? AND periodo BETWEEN ? AND DATE_ADD(?, INTERVAL ? MONTH)";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartao.getCodigo());
            stmt.setDate(2, dataReferencia);
            stmt.setDate(3, dataReferencia);
            stmt.setInt(4, intervalo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int codigo = rs.getInt("codigo");
                    Date periodo = rs.getDate("periodo");
                    double valor = rs.getDouble("valor");
                    String situacao = rs.getString("situacao");

                    Fatura fatura = new Fatura();
                    fatura.setCodigo(codigo);
                    fatura.setPeriodo(periodo);
                    fatura.setValor(valor);
                    fatura.setSituacao(situacao);
                    fatura.setCartao(cartao);
                    
                    faturas.add(fatura);
                }
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", e.getMessage());  // Você pode usar um logger aqui
        }
        return faturas;
    }

    public boolean verificarFaturaExistente(int codigoCartao, Date proximoMes) {
        // SQL para verificar se a fatura já existe
        String sql = "SELECT COUNT(*) FROM fatura WHERE cartao = ? AND DATE_FORMAT(periodo, '%Y-%m') = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigoCartao);

            // Usando SimpleDateFormat para formatar a data como 'YYYY-MM'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String dataFormatada = sdf.format(proximoMes);

            stmt.setString(2, dataFormatada); // Defina a data formatada como string no prepared statement

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true se existir uma fatura
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
