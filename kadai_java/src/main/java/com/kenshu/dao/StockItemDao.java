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



}



