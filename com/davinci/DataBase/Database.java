package com.davinci.DataBase;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by macintoshhd on 11.01.18.
 */
public class Database {

    public static java.sql.Connection dbconnect() {
        String username = "root";
        String password = "HTCdream131";
        String dbname = "sirbot";
        String host = "localhost";
        java.sql.Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://" + host + "/" + dbname + "?user=" + username + "&password=" + password + "&connectTimeout=0&socketTimeout=0&autoReconnect=true";
            conn = java.sql.DriverManager.getConnection(dbURL);
            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        /*try {
            String url1 = "jdbc:mysql://85.217.201.52:3306/sirbot";
            conn = DriverManager.getConnection(url1, username, password);
            return conn;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }*/


    }


}
