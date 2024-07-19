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
</head>
<body>
    <h1>在庫一覧</h1>
    
    <% 
    if (itemList != null && itemList.getStockItemList() != null) {
        List<StockItem> items = itemList.getStockItemList();
    %>
    <!-- デバッグ情報 -->
    <h2>デバッグ情報</h2>
    <pre>
    itemsの中身は: <%= items.toString() %>
    </pre>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>商品名</th>
            <th>価格</th>
            <th>在庫数</th>
        </tr>
        <% for (StockItem item : items) { %>
            <tr>
                <td><%= item.getId() %></td>
                <td><%= item.getName() %></td>
                <td><%= item.getPrice() %></td>
                <td><%= item.getStock() %></td>
            </tr>
        <% } %>
    </table>
    <% } else { %>
        <p>在庫商品が見つかりませんでした。</p>
    <% } %>
    
    <form action="inventory/add" method="get">
        <button type="submit">商品追加</button>
    </form>

</body>
</html>
