/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.connection;

import com.marjax.finansys.util.AlertUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class MySQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/finansys";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD); 
        } catch (ClassNotFoundException e) {            
            AlertUtil.showErrorAlert("Erro de Driver JDBC", "Driver JDBC não encontrado.", e.getMessage());
        } catch (SQLException e) {            
            AlertUtil.showErrorAlert("Erro de conexão", "Erro ao estabelecer a conexão com o banco de dados.", e.getMessage());
        }
        return connection;
    }    
}
