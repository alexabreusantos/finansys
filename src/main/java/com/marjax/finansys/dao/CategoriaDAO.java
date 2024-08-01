/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.dao;

import com.marjax.finansys.connection.MySQLConnection;
import com.marjax.finansys.model.Categoria;
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
public class CategoriaDAO {

    private Validator validator;

    public CategoriaDAO() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Método para verificar se um responsável já existe
    public boolean existsCategoria(String nome) {
        String sql = "SELECT COUNT(*) FROM categoria WHERE nome = ?";
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
    public void salvar(Categoria categoria) {
        Set<ConstraintViolation<Categoria>> violations = validator.validate(categoria);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Categoria> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            AlertUtil.showErrorAlert("Atenção", "Campo não preenchido", sb.toString());
            return;
        }

        if (existsCategoria(categoria.getNome())) {
            AlertUtil.showErrorAlert("Erro", "Categoria já cadastrada", "A categoria já está cadastrada no sistema.");
            return;
        }

        String sql = "INSERT INTO categoria (nome) VALUES (?)";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, categoria.getNome());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                AlertUtil.showInformationAlert("Sucesso", "Categoria já cadastrada", "A categoria já está cadastrada no sistema.");
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
    }

    //metodo para listar responsaveis
    public ObservableList<Categoria> getAllCategorias() {
        String sql = "SELECT * FROM categoria ORDER BY nome ASC";
        ObservableList<Categoria> categorias = FXCollections.observableArrayList();
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setCodigo(rs.getInt("codigo"));
                categoria.setNome(rs.getString("nome"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            AlertUtil.showErrorAlert("Erro", "Erro de SQL", "Erro: " + e.getMessage());
        }
        return categorias;
    }

    // Método para excluir um responsável
    public boolean excluirCategoria(int codigo) {
        String sql = "DELETE FROM categoria WHERE codigo = ?";

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
    public boolean atualizarCategoria(Categoria categoria) {

        Set<ConstraintViolation<Categoria>> violations = validator.validate(categoria);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Categoria> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            AlertUtil.showErrorAlert("Atenção", "Campo não preenchido", sb.toString());
            return false;
        }

        if (existsCategoria(categoria.getNome())) {
            return false; // Nome já existe no banco de dados
        }

        String sql = "UPDATE categoria SET nome = ? WHERE codigo = ?";
        try (Connection connection = MySQLConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoria.getNome());
            preparedStatement.setInt(2, categoria.getCodigo());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
