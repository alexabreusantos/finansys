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
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class ResponsavelDAO {

    private Validator validator;

    public ResponsavelDAO() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Método para verificar se um responsável já existe
    public boolean existsResponsavel(String nome) {
        String sql = "SELECT COUNT(*) FROM responsavel WHERE nome = ?";
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
    public void salvar(Responsavel responsavel) {
        Set<ConstraintViolation<Responsavel>> violations = validator.validate(responsavel);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Responsavel> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            AlertUtil.showErrorAlert("Atenção", "Campo não preenchido", sb.toString());
            return;
        }

        if (existsResponsavel(responsavel.getNome())) {
            AlertUtil.showErrorAlert("Erro", "Responsável já cadastrado", "O responsável já está cadastrado no sistema.");
            return;
        }

        String sql = "INSERT INTO responsavel (nome) VALUES (?)";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, responsavel.getNome());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                AlertUtil.showInformationAlert("Sucesso", "Responsável já cadastrado", "O responsável já está cadastrado no sistema.");
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

    //metodo para listar responsaveis
    public ObservableList<Responsavel> getAllResponsaveis() {
        String sql = "SELECT * FROM responsavel ORDER BY nome ASC";
        ObservableList<Responsavel> responsaveis = FXCollections.observableArrayList();
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Responsavel responsavel = new Responsavel();
                responsavel.setCodigo(rs.getInt("codigo"));
                responsavel.setNome(rs.getString("nome"));
                responsaveis.add(responsavel);
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return responsaveis;
    }

    // Método para excluir um responsável
    public boolean excluirResponsavel(int codigo) {
        String sql = "DELETE FROM responsavel WHERE codigo = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para atualizar o responsável
    public boolean atualizarResponsavel(Responsavel responsavel) {

        Set<ConstraintViolation<Responsavel>> violations = validator.validate(responsavel);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Responsavel> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            AlertUtil.showErrorAlert("Atenção", "Campo não preenchido", sb.toString());
            return false;
        }

        if (existsResponsavel(responsavel.getNome())) {
            return false; // Nome já existe no banco de dados
        }

        String sql = "UPDATE responsavel SET nome = ? WHERE codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, responsavel.getNome());
            preparedStatement.setInt(2, responsavel.getCodigo());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
