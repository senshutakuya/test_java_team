package com.kenshu.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kenshu.model.bean.StockItem;
import com.kenshu.model.dto.StockItemDto;

public class StockItemDao {
	// JDBCドライバとデータベースURL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kadai_java";

    // データベースのユーザー名とパスワード
    static final String USER = "root";
    static final String PASS = "password";

    // データベースに確認する処理(LoginServiceで使う)
    public StockItemDto getAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StockItemDto stockItemDto = new StockItemDto();

        try {
            // JDBCドライバをロードし、データベースに接続
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // SQLクエリを準備
            String sql = "SELECT i.id, i.name, i.price, s.stock FROM items i JOIN stocks s ON i.stock_id = s.id";
            stmt = conn.prepareStatement(sql);

            // SQLクエリを実行し、結果を取得
            rs = stmt.executeQuery();

            // 結果をStockItemにマッピング
//            whileでデータがある限り続ける
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int stock = rs.getInt("stock");
                
                StockItem item = new StockItem(id, name, price, stock);
                stockItemDto.add(item);
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

        return stockItemDto;
    }
    
    
    
 // item_idを基に情報を取得し確認する処理(InventoryEditで使う)
    public static StockItem getItemById(int stock_id) {
    	
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StockItem stockItem = null;

        try {
            // JDBCドライバをロードし、データベースに接続
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // SQLクエリを準備
            String sql = "SELECT i.id, i.name, i.price, s.stock FROM items i JOIN stocks s ON i.stock_id = s.id WHERE i.id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, stock_id);

            // SQLクエリを実行し、結果を取得
            rs = stmt.executeQuery();
            
         // 結果をStockItemにマッピング
            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int stock = rs.getInt("stock");
                
                stockItem = new StockItem(id, name, price, stock);
            }

        }catch (SQLException | ClassNotFoundException e) {
//        	eを標準出力
            e.printStackTrace();
        } finally {
            // 接続をクローズ
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
//            	eを標準出力
                e.printStackTrace();
            }
        }return stockItem;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
//    データベースに在庫を追加する処理
    public StockItemDto addStock(String name, int price, int stock) {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stockStmt = null;
        PreparedStatement itemStmt = null;
        ResultSet rs = null;
        StockItemDto stockItemDto = new StockItemDto();

        try {
            // JDBCドライバをロードし、データベースに接続
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
         // トランザクションを開始
            conn.setAutoCommit(false);

            // SQLクエリを準備
//          // 在庫テーブルに在庫数を追加
            String stockSql = "INSERT INTO stocks (stock) VALUES (?)";
//            RETURN_GENERATED_KEYSでstock_idを取得
            stockStmt = conn.prepareStatement(stockSql, PreparedStatement.RETURN_GENERATED_KEYS);
            stockStmt.setInt(1, stock);
            stockStmt.executeUpdate();

           
            int stockId = 0;
            // 自動生成された在庫IDを取得
            try (ResultSet generatedKeys = stockStmt.getGeneratedKeys()) {
//            	もしあったらマッピング
                if (generatedKeys.next()) {
                    stockId = generatedKeys.getInt(1);
//                結果が無かったら
                } else {
                    throw new SQLException("在庫IDの取得に失敗しました");
                }
            }

            // 商品テーブルに商品を追加
            String itemSql = "INSERT INTO items (name, price, stock_id) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(itemSql);
            stmt.setString(1, name);
            stmt.setInt(2, price);
            stmt.setInt(3, stockId);
            stmt.executeUpdate();

         // トランザクションをコミット
            conn.commit();
         // 更新成功のメッセージを出力
            System.out.println("在庫が正常に追加されました");

        } catch (SQLException | ClassNotFoundException e) {
            // エラーが発生した場合、トランザクションをロールバック
//        	接続が確立されているなら
            if (conn != null) {
                try {
//                	ロールバック
                    conn.rollback();
//                 データベースとロールバックのエラーの時
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // 接続をクローズ
            try {
                if (rs != null) rs.close();
                if (stockStmt != null) stockStmt.close();
                if (itemStmt != null) itemStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return stockItemDto;
    }





    public static void updateInventory(int id, String name, int price, int stock) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // JDBCドライバをロードし、データベースに接続
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // トランザクションを開始
            conn.setAutoCommit(false);

            // itemsテーブルからstock_idを取得するSQL
            String getStockIdSql = "SELECT stock_id FROM items WHERE id = ?";
            stmt = conn.prepareStatement(getStockIdSql);
//            引数のidを基に対象のstock_idを取得
            stmt.setInt(1, id);
//            変数rsにstock_idを取得
            ResultSet rs = stmt.executeQuery();
            System.out.printf("デバッグrsの中身は",rs);

            int stockId = -1;
         // ResultSet rsの中身をデバッグ出力
            if (rs.next()) {
                stockId = rs.getInt("stock_id");
                System.out.printf("デバッグ: stock_id = %d%n", stockId);
            } else {
                System.out.println("デバッグ: stock_idが見つかりませんでした。");
            }
            rs.close();
            stmt.close();

            if (stockId != -1) {
                // stocksテーブルの在庫を更新するSQL
                String updateStockSql = "UPDATE stocks SET stock = ? WHERE id = ?";
                stmt = conn.prepareStatement(updateStockSql);
                stmt.setInt(1, stock);
                stmt.setInt(2, stockId);
                stmt.executeUpdate();
                stmt.close();

                // itemsテーブルのnameとpriceを更新するSQL
                String updateItemSql = "UPDATE items SET name = ?, price = ? WHERE id = ?";
                stmt = conn.prepareStatement(updateItemSql);
                stmt.setString(1, name);
                stmt.setInt(2, price);
                stmt.setInt(3, id);
                stmt.executeUpdate();
                stmt.close();

                // トランザクションをコミット
                conn.commit();
            } else {
                throw new SQLException("stock_idが見つかりませんでした");
            }
        } catch (SQLException | ClassNotFoundException e) {
            // エラーが発生した場合、トランザクションをロールバック
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
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




