package com.kenshu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDao {

    // JDBCドライバとデータベースURL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kadai_java";

    // データベースのユーザー名とパスワード
    static final String USER = "root";
    static final String PASS = "password";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static boolean isFirstOrder(int userId, int itemId) {
        String query = "SELECT COUNT(*) FROM orders WHERE user_id = ? AND item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, itemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // 件数が0なら初回
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public static void addOrder(int userId, int itemId, Integer stockId, int quantity) {
        String query = "INSERT INTO orders (user_id, item_id, stock_id, quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, stockId);
            stmt.setInt(4, quantity);

            stmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void updateOrder(int userId, int itemId, Integer stockId, int quantity) {
        String query = "UPDATE orders SET quantity = ? WHERE user_id = ? AND item_id = ? AND stock_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, userId);
            stmt.setInt(3, itemId);
            stmt.setInt(4, stockId);

            stmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    
    

 // 既存の注文に数量を加算するメソッド
    public static void addQuantityToOrder(int userId, int itemId, Integer stockId, int quantityToAdd) {
        String checkSql = "SELECT quantity FROM orders WHERE user_id = ? AND item_id = ? AND stock_id = ?";
        String updateSql = "UPDATE orders SET quantity = ? WHERE user_id = ? AND item_id = ? AND stock_id = ?";
        String insertSql = "INSERT INTO orders (user_id, item_id, stock_id, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // 注文が既に存在するかチェック
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, itemId);
            checkStmt.setInt(3, stockId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // 注文が存在する場合は、数量を加算する
                    int currentQuantity = rs.getInt("quantity");
                    updateStmt.setInt(1, currentQuantity + quantityToAdd);
                    updateStmt.setInt(2, userId);
                    updateStmt.setInt(3, itemId);
                    updateStmt.setInt(4, stockId);
                    updateStmt.executeUpdate();
                } else {
                    // 注文が存在しない場合は、新しい注文を追加する
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, itemId);
                    insertStmt.setInt(3, stockId);
                    insertStmt.setInt(4, quantityToAdd);
                    insertStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
