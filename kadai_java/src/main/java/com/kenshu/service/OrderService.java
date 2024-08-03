package com.kenshu.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.kenshu.dao.OrderDao;
import com.kenshu.dao.StockItemDao;
import com.kenshu.model.bean.UserBean;

public class OrderService {

    public void cartAdd(String itemsIdStr, UserBean user, String quantityStr) {
        StockItemDao stockItemDao = new StockItemDao();
        boolean isFirstOrder = false; // 初回かどうかの変数

        try (Connection conn = stockItemDao.getConnection()) {
            conn.setAutoCommit(false);  // トランザクション開始

            try {
                // アイテムID文字列を整数に変換
                int itemId = Integer.parseInt(itemsIdStr);

                // user_idを取得
                int userId = user.getLoginid();

                // アイテムIDに基づいて在庫IDを取得
                Integer stockId = stockItemDao.findStockIdByItemId(itemId);

                // 注文数量を整数に変換
                int quantity = Integer.parseInt(quantityStr);

                // この関数で初回かどうかを判断
                isFirstOrder = OrderDao.isFirstOrder(userId, itemId);

                if (isFirstOrder) {
                    // 初回の追加処理を行う
                    OrderDao.addOrder(userId, itemId, stockId, quantity);
                    // 在庫から注文数を引く
                    stockItemDao.decrementStock(stockId, quantity);
                } else {
                    // 既存の注文を更新する
                    OrderDao.addQuantityToOrder(userId, itemId, stockId, quantity);
                    // 在庫から注文数を引く
                    stockItemDao.decrementStock(stockId, quantity);
                }

                conn.commit();  // トランザクションコミット

            } catch (SQLException e) {
                // SQLエラー
                System.err.println("SQLエラーが発生しました: " + e.getMessage());
                try {
                    conn.rollback();  // トランザクションロールバック
                } catch (SQLException rollbackEx) {
                    System.err.println("ロールバック中にエラーが発生しました: " + rollbackEx.getMessage());
                }
            } catch (NumberFormatException e) {
                // 数値変換の失敗
                System.err.println("数値の変換に失敗しました: " + e.getMessage());
                try {
                    conn.rollback();  // トランザクションロールバック
                } catch (SQLException rollbackEx) {
                    System.err.println("ロールバック中にエラーが発生しました: " + rollbackEx.getMessage());
                }
            }

        } catch (Exception e) {
            // その他の例外
            System.err.println("注文処理中にエラーが発生しました: " + e.getMessage());
        }
    }
}
