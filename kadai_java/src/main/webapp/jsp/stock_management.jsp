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
	<!-- logout_header.jspのインクルード -->
	<%@ include file="logout_header.jsp" %>
    <h1>在庫一覧</h1>
    
    <% 
    if (itemList != null && itemList.getStockItemList() != null) {
        List<StockItem> items = itemList.getStockItemList();
    %>
    <!-- デバッグ情報 -->
    <table border="1">
        <tr>
            <th>商品名</th>
            <th>価格</th>
            <th>在庫数</th>
            <th>編集</th>
            <th>削除</th>
        </tr>
        <% for (StockItem item : items) { %>
            <tr>
                <form action="<%= request.getContextPath() %>/inventory/delete?id=<%= item.getId() %>" method="post">
                    <td><%= item.getName() %></td>
                    <td><%= item.getPrice() %></td>
                    <td><%= item.getStock() %></td>
                    <td>
                        <button type="button" onclick="location.href='<%= request.getContextPath() %>/inventory/edit?id=<%= item.getId() %>'">編集</button>
                    </td>
                    <td>
                        <input type="hidden" name="id" value="<%= item.getId() %>" />
                        <button type="submit">削除</button>
                    </td>
                </form>
            </tr>
        <% } %>
    </table>
    <% } else { %>
        <p>在庫商品が見つかりませんでした。</p>
    <% } %>
    
    <form action="<%= request.getContextPath() %>/inventory/add" method="get">
        <button type="submit">商品追加</button>
    </form>

</body>
</html>
