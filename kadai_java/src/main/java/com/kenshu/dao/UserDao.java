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
    public UserBean getUserByLoginIdAndPassword(int loginid, String password) {
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
            stmt.setInt(1, loginid);
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
}
