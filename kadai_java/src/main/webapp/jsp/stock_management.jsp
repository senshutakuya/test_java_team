<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.kenshu.model.dto.StockItemDto" %>
<%@ page import="com.kenshu.model.bean.StockItem" %>

<jsp:useBean id="itemList" scope="request" class="com.kenshu.model.dto.StockItemDto" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>在庫管理</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/inventory.css" rel="stylesheet">
</head>
<body>
    <% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
    %>

    <div class="container">
        <!-- ヘッダー -->
        <div class="header d-flex justify-content-between align-items-center mb-4">
            <h2>在庫管理者様</h2>
            <%@ include file="logout_header.jsp" %>
        </div>

        <!-- 在庫一覧 -->
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">在庫一覧</h3>
            </div>
            <div class="card-body">
                <% if (itemList != null && itemList.getStockItemList() != null) {
                    List<StockItem> items = itemList.getStockItemList();
                %>
                <table class="table table-bordered inventory-table">
                    <thead>
                        <tr>
                            <th>商品名</th>
                            <th>価格</th>
                            <th>在庫数</th>
                            <th>編集</th>
                            <th>削除</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (StockItem item : items) { %>
                            <tr>
                                <form action="<%= request.getContextPath() %>/inventory/delete?id=<%= item.getId() %>" method="post">
                                    <td><%= item.getName() %></td>
                                    <td><%= item.getPrice() %></td>
                                    <td><%= item.getStock() %></td>
                                    <td>
                                        <button type="button" class="btn btn-primary" onclick="location.href='<%= request.getContextPath() %>/inventory/edit?id=<%= item.getId() %>'">編集</button>
                                    </td>
                                    <td>
                                        <input type="hidden" name="id" value="<%= item.getId() %>" />
                                        <button type="submit" class="btn btn-danger">削除</button>
                                    </td>
                                </form>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
                <% } else { %>
                    <p>在庫商品が見つかりませんでした。</p>
                <% } %>
            </div>
        </div>

        <!-- 商品追加ボタン -->
        <div class="text-center mt-4">
            <form action="<%= request.getContextPath() %>/inventory/add" method="get">
                <button type="submit" class="btn btn-success btn-lg">商品追加</button>
            </form>
        </div>
    </div>

</body>
</html>
