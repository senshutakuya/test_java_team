package com.kenshu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    // JDBC ドライバとデータベース URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kadai_java";

    // データベースのユーザー名とパスワード
    static final String USER = "root";
    static final String PASS = "password";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // JDBC ドライバをロード
            Class.forName(JDBC_DRIVER);

            // データベースに接続
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 接続成功時のメッセージ
            if (conn != null) {
                System.out.println("Connected to the database successfully.");
            } else {
                System.out.println("Failed to make connection to the database.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 接続をクローズ
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
