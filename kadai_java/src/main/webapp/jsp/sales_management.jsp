<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.kenshu.model.dto.StockItemDto" %>
<%@ page import="com.kenshu.model.bean.StockItem" %>

<jsp:useBean id="itemList" scope="request" class="com.kenshu.model.dto.StockItemDto" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>注文管理</title>
</head>
<body>
    <h2>注文管理者</h2>    
    <!-- logout_header.jspのインクルード -->
    <%@ include file="logout_header.jsp" %>
    <h1>商品一覧</h1>
    
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
            <th>注文数</th>
            <th>カートにいれる</th>
        </tr>
        <% for (StockItem item : items) { %>
            <tr>
                <form action="<%= request.getContextPath() %>/order/add?id=<%= item.getId() %>" method="post">
                    <td><%= item.getName() %></td>
                    <td><%= item.getPrice() %></td>
                    <td><%= item.getStock() %></td>
                    <td>
                        <select id="quantity" name="quantity">
                            <% 
                                // 在庫数に基づいて<select>ボックスのオプションを生成
                                for (int i = 1; i <= item.getStock(); i++) {
                                    out.println("<option value='" + i + "'>" + i + "</option>");
                                }
                            %>
                        </select>
                    </td>
                    <td>
                        <input type="hidden" name="id" value="<%= item.getId() %>" />
                        <button type="submit">カートにいれる</button>
                    </td>
                </form>
            </tr>
        <% } %>
    </table>
    <% } else { %>
        <p>在庫商品が見つかりませんでした。</p>
    <% } %>
    
    <form action="<%= request.getContextPath() %>/order/check" method="get">
        <button type="submit">注文確認</button>
    </form>

</body>
</html>
