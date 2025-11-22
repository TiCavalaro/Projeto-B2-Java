package util;

import java.sql.*;

public class ConnectionFactory {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/loja_aquario"
                   + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
        String user = "root"; 
        String senha = "Npk151515!";   
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, senha);
    }
}

