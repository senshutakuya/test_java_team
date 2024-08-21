package com.kenshu.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.kenshu.model.bean.StockItem;
import com.kenshu.model.dto.StockItemDto;

public class StockItemDao {
	// JDBCドライバとデータベースURL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kadai_java";

    // データベースのユーザー名とパスワード
    static final String USER = "root";
    static final String PASS = "password";
    
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

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

            // itemsテーブルからitemIdを基にstock_idを取得するSQL
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



//    item_idからstock_idの取得
	public int findStockIdByItemId(int items_id) {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int stock_id = 0 ;
        
        try {
            // JDBCドライバをロードし、データベースに接続
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // SQLクエリを準備
            String sql = "SELECT stock_id FROM items WHERE id = ?";
            stmt = conn.prepareStatement(sql);
//            引数からidをセット
            stmt.setInt(1, items_id);

            // SQLクエリを実行し、結果を取得
            rs = stmt.executeQuery();
         // 結果をStockItemにマッピング
            if (rs.next()) {
//            	stock_idをマッピング
                stock_id = rs.getInt("stock_id");
                

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
        }
		return stock_id;
		
	}


//	item_idからitemsテーブルのデータを削除
	public void deleteItemByItemId(int itemId, Connection conn) throws SQLException {
	    String deleteSql = "DELETE FROM items WHERE id = ?";
	    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
	        deleteStmt.setInt(1, itemId);
	        deleteStmt.executeUpdate();
	    }
	}




	public void deleteStockByStockId(int stockId, Connection conn) throws SQLException {
	    String deleteSql = "DELETE FROM stocks WHERE id = ?";
	    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
	        deleteStmt.setInt(1, stockId);
	        deleteStmt.executeUpdate();
	    }
	}

	


	
//	在庫から注文数を引く	
	public void decrementStock(Integer stockId, int quantity, Map<Integer, Integer> cart) {
	    String checkStockSql = "SELECT stock FROM stocks WHERE id = ?";
	    String getOrderIdSql = "SELECT id FROM orders WHERE stock_id = ?"; // orderId取得用SQL
	    String updateStockSql = "UPDATE stocks SET stock = ? WHERE id = ?";
	    
	    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
	         PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
	         PreparedStatement getOrderIdStmt = conn.prepareStatement(getOrderIdSql);
	         PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {
	        
	        // 在庫がどの位あるのかチェック
	        checkStockStmt.setInt(1, stockId);
	        try (ResultSet rs = checkStockStmt.executeQuery()) {
	            if (rs.next()) {
	                int currentStock = rs.getInt("stock");

	                // stockIdを基にorderIdを取得
	                getOrderIdStmt.setInt(1, stockId);
	                Integer orderId = null;
	                try (ResultSet orderRs = getOrderIdStmt.executeQuery()) {
	                    if (orderRs.next()) {
	                        orderId = orderRs.getInt("id");
	                    } else {
	                        throw new SQLException("指定されたstockIdに対応する注文が見つかりません。");
	                    }
	                }

	                // cartから既存の注文数を取得
	                int existingQuantity = cart.getOrDefault(orderId, 0);

	                // 合計注文数が在庫を超えないかチェック
	                if (currentStock >= existingQuantity + quantity) {
	                    // 在庫が十分にある場合にのみ更新
	                    updateStockStmt.setInt(1, currentStock - quantity);
	                    updateStockStmt.setInt(2, stockId);
	                    updateStockStmt.executeUpdate();
	                } else {
	                    throw new SQLException("在庫が不足しています。");
	                }
	            } else {
	                throw new SQLException("指定された在庫IDが見つかりません。");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	
	
	// 在庫数を増やすメソッド
    public void incrementStock(int stockId, int quantity) throws SQLException, ClassNotFoundException {
        String updateSql = "UPDATE stocks SET stock = stock + ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // パラメータを設定
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, stockId);

            // SQLクエリを実行
            updateStmt.executeUpdate();
        }
    }

    public Integer findStockIdByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT stock_id FROM orders WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock_id");
                }
            }
        }
        return null;  // 在庫IDが見つからなかった場合
    }

    public Integer getStockByItemId(int itemId, Connection conn) throws SQLException {
        String query = "SELECT s.stock FROM items i JOIN stocks s ON i.stock_id = s.id WHERE i.id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock");
                } else {
                    // アイテムIDに基づく在庫が存在しない場合
                    return null;
                }
            }
        }
    }

 // 在庫から注文数を引く
    public void finally_decrementStock(Integer stockId, int quantity, Map<Integer, Integer> cart) {
        String checkStockSql = "SELECT stock FROM stocks WHERE id = ?";
        String getOrderIdSql = "SELECT id FROM orders WHERE stock_id = ?"; // orderId取得用SQL
        String getOrderQuantitySql = "SELECT quantity FROM orders WHERE id = ?"; // orderIdに基づく注文数取得用SQL
        String updateStockSql = "UPDATE stocks SET stock = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
             PreparedStatement getOrderIdStmt = conn.prepareStatement(getOrderIdSql);
             PreparedStatement getOrderQuantityStmt = conn.prepareStatement(getOrderQuantitySql);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {
            
            // 在庫がどのくらいあるのかチェック
            checkStockStmt.setInt(1, stockId);
            try (ResultSet rs = checkStockStmt.executeQuery()) {
                if (rs.next()) {
                    int currentStock = rs.getInt("stock");

                    // stockIdからorderIdを取得
                    getOrderIdStmt.setInt(1, stockId);
                    Integer orderId = null;
                    try (ResultSet orderRs = getOrderIdStmt.executeQuery()) {
                        if (orderRs.next()) {
                            orderId = orderRs.getInt("id");
                        } else {
                            throw new SQLException("指定された在庫IDに対応する注文が見つかりません。");
                        }
                    }

                    // cartからorderIdに基づく既存の注文数を取得
                    int existingQuantity = cart.getOrDefault(orderId, 0);

                    // 注文テーブルから該当注文の数量を取得
                    getOrderQuantityStmt.setInt(1, orderId);
                    int ordersQuantity = 0;
                    try (ResultSet orderQuantityRs = getOrderQuantityStmt.executeQuery()) {
                        if (orderQuantityRs.next()) {
                            ordersQuantity = orderQuantityRs.getInt("quantity");
                        } else {
                            throw new SQLException("指定された注文IDに対応する数量が見つかりません。");
                        }
                    }

                    // 最終的な注文数を計算
                    int finallyQuantity = existingQuantity - ordersQuantity;

                    System.out.println("StockItemDaoのdecrementStock: 既存の注文数 existingQuantity の中身は " + existingQuantity);
                    System.out.println("StockItemDaoのdecrementStock: 注文数 ordersQuantity の中身は " + ordersQuantity);
                    System.out.println("StockItemDaoのdecrementStock: 在庫数 currentStock の中身は " + currentStock);
                    System.out.println("StockItemDaoのdecrementStock: 最終的な引く数 finallyQuantity の中身は " + finallyQuantity);

                    // 合計注文数が在庫を超えないかチェック
                    if (currentStock >= finallyQuantity) {
                        // 在庫が十分にある場合にのみ更新
                        updateStockStmt.setInt(1, currentStock - finallyQuantity);
                        updateStockStmt.setInt(2, stockId);
                        updateStockStmt.executeUpdate();
                    } else {
                        throw new SQLException("在庫が不足しています。");
                    }
                } else {
                    throw new SQLException("指定された在庫IDが見つかりません。");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



    

    
    





