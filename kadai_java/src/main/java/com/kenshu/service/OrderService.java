package com.kenshu.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.kenshu.dao.OrderDao;
import com.kenshu.dao.StockItemDao;
import com.kenshu.model.bean.UserBean;
import com.kenshu.model.dto.OrderItemDto;

import jakarta.servlet.http.HttpSession;

public class OrderService {
    private OrderDao orderDao = new OrderDao();
    private StockItemDao stockItemDao = new StockItemDao();

    static {
        try {
            // JDBC ドライバーのロード
            Class.forName("com.mysql.cj.jdbc.Driver"); // 使用するドライバーに応じて変更
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC ドライバーが見つかりません: " + e.getMessage());
            // 必要に応じて適切な処理を追加
        }
    }

    public void cartAdd(String itemsIdStr, UserBean user, String quantityStr, Map<Integer, Integer> cart) {
        try (Connection conn = stockItemDao.getConnection()) {
            conn.setAutoCommit(false);  // トランザクション開始

            try {
                int itemId = Integer.parseInt(itemsIdStr);
                int userId = user.getLoginid();
                Integer stockId = stockItemDao.findStockIdByItemId(itemId);
                int quantity = Integer.parseInt(quantityStr);
                System.out.println("Service:itemIdは"+itemId);

                Integer availableStock = stockItemDao.getStockByItemId(itemId, conn);
                if (availableStock == null) {
                    throw new RuntimeException("商品の在庫が見つかりません。");
                }
                
                System.out.println("デバッグ:セッションcartの情報は" + cart);
                System.out.println("デバッグ:現在の注文数は" + quantity);
                System.out.println("デバッグ:現在の在庫数は" + availableStock);

                // この関数で初回かどうかを判断
                boolean isFirstOrder = OrderDao.isFirstOrder(userId, itemId);

                if (isFirstOrder) {
                    // 初回の追加処理を行う
                    OrderDao.addOrder(userId, itemId, stockId, quantity);
                } else {
                    // 既存の注文を更新する
                    OrderDao.addQuantityToOrder(userId, itemId, stockId, quantity);
                }

                // 在庫から注文数を引く
                stockItemDao.decrementStock(stockId, quantity,cart);

                conn.commit();  // トランザクションコミット

            } catch (SQLException e) {
                conn.rollback();  // トランザクションロールバック
                System.err.println("SQLエラーが発生しました: " + e.getMessage());
            } catch (NumberFormatException e) {
                conn.rollback();  // トランザクションロールバック
                System.err.println("数値の変換に失敗しました: " + e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("接続エラーが発生しました: " + e.getMessage());
        }
    }


    // ユーザーごとの注文情報を取得して返す
    public static OrderItemDto list(UserBean user) {
        int userId = user.getLoginid();
        return OrderDao.list(userId);
    }

    // ユーザーごとの注文情報をorderIdを基に削除し在庫を基に戻す
    public void cartDelete(UserBean user, String ordersIdStr) {
        try (Connection conn = OrderDao.getConnection()) {
            conn.setAutoCommit(false);  // トランザクション開始
            
            try {
                // userIdをセッションから取得
                int userId = user.getLoginid();
                // アイテムID文字列を整数に変換
                int orderId = Integer.parseInt(ordersIdStr);

                // item_idを基にordersテーブルの情報を削除
                Map<String, Integer> result = orderDao.deleteUserCartByItemid(userId, orderId, conn);

                // MapからquantityとstockIdを取得
                int quantity = result.get("quantity");
                int stockId = result.get("stockId");
                
                // 例: 在庫戻し処理など（必要であれば）
                 stockItemDao.incrementStock(stockId, quantity);

                conn.commit();  // トランザクションコミット
            } catch (SQLException e) {
                conn.rollback();  // トランザクションロールバック
                System.err.println("SQLエラーが発生しました: " + e.getMessage());
            } catch (NumberFormatException e) {
                conn.rollback();  // トランザクションロールバック
                System.err.println("数値の変換に失敗しました: " + e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("接続エラーが発生しました: " + e.getMessage());
        }
    }

    
 // セッションのカートからアイテムを削除
	public Map<Integer, Integer> cartUpdate(Map<Integer, Integer> cart, String ordersIdStr, String quantityStr) {
		// アイテムID文字列を整数に変換
        int orderId = Integer.parseInt(ordersIdStr);
     // 注文数の文字列を整数に変換
        int quantity = Integer.parseInt(ordersIdStr);
        
        System.out.println("更新前のcart情報"+cart);
        
        cart.put(orderId, quantity);
//      カートからorderIdを基に対象のデータをセッションから削除
        cart.remove(orderId);
        
        System.out.println("更新後のcart情報"+cart);
        
        return cart;
		
	}



	public static boolean cartConfirm(Map<Integer, Integer> cart, UserBean user, HttpSession session) {
	    boolean success_flag = false;
	    StockItemDao stockItemDao = new StockItemDao();
	    OrderDao orderDao = new OrderDao();
	    try (Connection conn = OrderDao.getConnection()) {
	        
	        conn.setAutoCommit(false);  // トランザクション開始
	        
	        try {
	            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
	                int orderId = entry.getKey();
	                int quantity = entry.getValue();
	                
	                // OrderIdを基にstocksテーブルを取得
	                Integer stockId = stockItemDao.findStockIdByOrderId(orderId);
	                
	                if (stockId != null) {
	                    // 対象の在庫商品を減らす
	                	stockItemDao.finally_decrementStock(stockId, quantity, cart);
	                    
	                    // OrderIdを基に対象userのorderテーブルを削除
	                    orderDao.deleteUserOrderById(user.getLoginid(), orderId, session, conn);
	                } else {
	                    throw new SQLException("在庫IDが見つかりません: Order ID = " + orderId);
	                }
	            }
	            
	            conn.commit();  // トランザクションコミット
	            success_flag = true;
	            
	        } catch (SQLException e) {
	            conn.rollback();  // トランザクションロールバック
	            System.err.println("SQLエラーが発生しました: " + e.getMessage());
	        } catch (NumberFormatException e) {
	            conn.rollback();  // トランザクションロールバック
	            System.err.println("数値の変換に失敗しました: " + e.getMessage());
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        System.err.println("接続エラーが発生しました: " + e.getMessage());
	    }
	    
	    return success_flag;
	}

	public Integer getOrderIdByItemId(int itemsId) {
	    Integer orderId = null;
	    try (Connection conn = OrderDao.getConnection()) {
	        orderId = orderDao.getOrderIdByItemId(itemsId, conn);
	    } catch (SQLException | ClassNotFoundException e) {
	        System.err.println("接続エラーが発生しました: " + e.getMessage());
	    }
	    System.out.println("引数itemsIdは " + itemsId);
	    System.out.println("戻り値のorderIdは " + orderId);
	    return orderId;
	}

	public int getAvailableStock(int itemsId) {
		Integer availableStock = null;
		try (Connection conn = OrderDao.getConnection()) {
			availableStock  = orderDao.getStockByItemId(itemsId, conn);
	    } catch (SQLException | ClassNotFoundException e) {
	        System.err.println("接続エラーが発生しました: " + e.getMessage());
	    }
	    System.out.println("引数itemsIdは " + itemsId);
	    System.out.println("戻り値のavailableStockは " + availableStock);
		return availableStock;
	}



	



}
	
	

