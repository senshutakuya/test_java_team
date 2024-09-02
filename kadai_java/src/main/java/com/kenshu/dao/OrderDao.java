package com.kenshu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.kenshu.model.bean.OrderItem;
import com.kenshu.model.dto.OrderItemDto;

import jakarta.servlet.http.HttpSession;

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

    public static boolean isFirstOrder(String userId, int itemId) {
        String query = "SELECT COUNT(*) FROM orders WHERE user_id = ? AND item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
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

    public static void addOrder(String userId, int itemId, Integer stockId, int quantity) {
        String query = "INSERT INTO orders (user_id, item_id, stock_id, quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
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
    public static void addQuantityToOrder(String userId, int itemId, Integer stockId, int quantityToAdd) {
        String checkSql = "SELECT quantity FROM orders WHERE user_id = ? AND item_id = ? AND stock_id = ?";
        String updateSql = "UPDATE orders SET quantity = ? WHERE user_id = ? AND item_id = ? AND stock_id = ?";
        String insertSql = "INSERT INTO orders (user_id, item_id, stock_id, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // 注文が既に存在するかチェック
            checkStmt.setString(1, userId);
            checkStmt.setInt(2, itemId);
            checkStmt.setInt(3, stockId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // 注文が存在する場合は、数量を加算する
                    int currentQuantity = rs.getInt("quantity");
                    updateStmt.setInt(1, currentQuantity + quantityToAdd);
                    updateStmt.setString(2, userId);
                    updateStmt.setInt(3, itemId);
                    updateStmt.setInt(4, stockId);
                    updateStmt.executeUpdate();
                } else {
                    // 注文が存在しない場合は、新しい注文を追加する
                    insertStmt.setString(1, userId);
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

    
    
	public static OrderItemDto list(String userId) {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
//      ArryListorderItemDtoを初期化
        OrderItemDto orderItemDto = new OrderItemDto();

        try {
            // JDBCドライバをロードし、データベースに接続
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // SQLクエリを準備
            String sql = "SELECT o.id, i.name, i.price, o.quantity, s.stock FROM orders o " +
                    "JOIN items i ON o.item_id = i.id " +
                    "JOIN stocks s ON o.stock_id = s.id WHERE o.user_id = ?";
            stmt = conn.prepareStatement(sql);
//          対象のuserIdをセット
            stmt.setString(1, userId);

            // SQLクエリを実行し、結果を取得
            rs = stmt.executeQuery();

            // 結果をStockItemにマッピング
//            whileでデータがある限り続ける
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                int stock = rs.getInt("stock");
                
//              一件ずつitem変数に格納
                OrderItem item = new OrderItem(id, name, price, quantity, stock);
//              ArrayListに対象のitemオブジェクトを追加していく
                orderItemDto.add(item);
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

        return orderItemDto;
    }

	
	
	
	
	
	
//	itemIdを基にorderの情報を消す
	public void deleteOrderByItemId(int itemId, Connection conn) throws SQLException {
        String deleteSql = "DELETE FROM orders WHERE item_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, itemId);
            deleteStmt.executeUpdate();
        }
    }
	

	// orderIdを基にユーザーのカート情報を削除
    public Map<String, Integer> deleteUserCartByItemid(String userId, int orderId, Connection conn) throws SQLException {
        String selectSql = "SELECT quantity, stock_id FROM orders WHERE user_id = ? AND id = ?";
        String deleteSql = "DELETE FROM orders WHERE user_id = ? AND id = ?";

        int quantity = 0;
        int stockId = 0;

        // まず削除対象のquantityとstockIdを取得する
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setString(1, userId);
            selectStmt.setInt(2, orderId);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                    stockId = rs.getInt("stock_id");
                }
            }
        }

        // 次に削除処理を行う
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setString(1, userId);
            deleteStmt.setInt(2, orderId);
            deleteStmt.executeUpdate();
        }

        // quantityとstockIdをMapに格納して返す
        Map<String, Integer> result = new HashMap<>();
        result.put("quantity", quantity);
        result.put("stockId", stockId);
        return result;
    }


    
    
    public void deleteUserOrderById(String string, int orderId, HttpSession session, Connection conn) {
        String sql = "DELETE FROM orders WHERE user_id = ? AND id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // パラメータを設定
            pstmt.setString(1, string);
            pstmt.setInt(2, orderId);

            // SQLクエリを実行
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // 注文が削除された場合、セッションからカート情報を削除
                session.removeAttribute("cart");
                System.out.println("Order and cart session deleted successfully.");
            } else {
                System.out.println("No order found with the given user_id and orderId.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
//	itemIdを基にorderIdを渡す
    public Integer getOrderIdByItemId(int itemsId, Connection conn) throws SQLException {
        Integer orderId = null;
        String query = "SELECT id FROM orders WHERE item_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemsId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                orderId = rs.getInt("id");
            }
        }
        
        return orderId;
    }

    public Integer getStockByItemId(int itemsId, Connection conn) {
        Integer availableStock = null;
        String sql = "SELECT s.stock FROM stocks s "
                   + "JOIN items i ON s.id = i.stock_id "
                   + "WHERE i.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemsId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    availableStock = rs.getInt("stock");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLエラーが発生しました: " + e.getMessage());
        }

        return availableStock;
    }




}
