/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class MySQLConnection {
    //URL do banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/finansys";
    // Usuário do banco de dados
    private static final String USER = "root";
    // Senha do banco de dados
    private static final String PASSWORD = "root";

 // Conexão
    private static Connection connection = null;

    // Método para obter a conexão
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Registrar o driver JDBC
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Estabelecer a conexão
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexão estabelecida com sucesso!");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver JDBC não encontrado: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erro ao estabelecer a conexão: " + e.getMessage());
            }
        }
        return connection;
    }

    // Método para fechar a conexão
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    // Método principal para teste
    public static void main(String[] args) {
        Connection conn = MySQLConnection.getConnection();
        if (conn != null) {
            System.out.println("Teste de conexão foi bem-sucedido!");
        } else {
            System.out.println("Falha no teste de conexão.");
        }
        MySQLConnection.closeConnection();
    }    
}
