<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.kenshu.model.dto.StockItemDto" %>
<%@ page import="com.kenshu.model.bean.StockItem" %>

<jsp:useBean id="itemList" scope="request" class="com.kenshu.model.dto.StockItemDto" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>注文管理</title>
</head>
<body>
	<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
    %>
    <h2>注文管理者</h2>    
    <!-- logout_header.jspのインクルード -->
    <%@ include file="logout_header.jsp" %>
    <h1>商品一覧</h1>
    <% 
    String errorMessage = (String) session.getAttribute("error");
    if (errorMessage != null) {
        out.println("<p class='error'>" + errorMessage + "</p>");
        session.removeAttribute("error"); // エラーメッセージをセッションから削除
    }
%>
    
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
