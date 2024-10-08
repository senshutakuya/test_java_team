// StockService.java
package com.kenshu.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.kenshu.dao.OrderDao;
import com.kenshu.dao.StockItemDao;
import com.kenshu.model.dto.StockItemDto;

public class StockService {
    private StockItemDao stockItemDao = new StockItemDao();
    private OrderDao orderDao = new OrderDao();

    public StockItemDto list() {
        // 在庫商品のリストを取得し、DTOとして返す
        return stockItemDao.getAll();
    }

    public StockItemDto add(String name, int price, int stock) {
        return stockItemDao.addStock(name, price, stock);
    }

    public void delete_Inventory(int id) {
        Connection conn = null;
        try {
            // データベース接続を取得
            conn = stockItemDao.getConnection();
            conn.setAutoCommit(false);  // トランザクション開始

            // itemidからstock_idを取得
            int stock_id = stockItemDao.findStockIdByItemId(id);

            // item_idを基にordersテーブルの情報を削除
            orderDao.deleteOrderByItemId(id, conn);

            // stock_idからstocksテーブルの情報を削除
            stockItemDao.deleteStockByStockId(stock_id, conn);

            // itemsテーブルの情報もidを基に削除
            stockItemDao.deleteItemByItemId(id, conn);

            conn.commit();  // トランザクションコミット
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();  // エラー発生時にロールバック
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();  // 接続をクローズ
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
