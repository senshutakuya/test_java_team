package com.kenshu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kenshu.model.bean.UserBean;

public class UserDao {

    // JDBCドライバとデータベースURL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kadai_java";

    // データベースのユーザー名とパスワード
    static final String USER = "root";
    static final String PASS = "password";

    // データベースにユーザーが登録されているか確認する処理(LoginServiceで使う)
    public UserBean getUserByLoginIdAndPassword(String loginid, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserBean user = null;

        try {
            // JDBCドライバをロードし、データベースに接続
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // SQLクエリを準備
            String sql = "SELECT * FROM users WHERE loginid = ? AND pass = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, loginid);
            stmt.setString(2, password);

            // SQLクエリを実行し、結果を取得
            rs = stmt.executeQuery();

            // 結果をUserBeanにマッピング
            if (rs.next()) {
                int authcode = rs.getInt("authcode");
                user = new UserBean(loginid, password, authcode);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 接続をクローズ
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

 // 新しいユーザーを追加する処理
    public void newUserAdd(String name, String loginid, String password, int authcode) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // JDBCドライバをロードし、データベースに接続
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // SQLクエリを準備
            String insertSql = "INSERT INTO users (loginID, pass, name, authcode) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertSql);
            stmt.setString(1, loginid);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setInt(4, authcode);

            // SQLクエリを実行
            stmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 接続をクローズ
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
