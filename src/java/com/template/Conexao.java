package com.template;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:postgresql://localhost:5432/FigurasHistoricas";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    
    public static Connection conectar() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do PostgreSQL não encontrado! Verifique se incluiu o JAR no projeto.", e);
        }
    }
}
