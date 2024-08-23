<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>商品登録</title>
</head>
<body>
	<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
    %>
    <h2>商品登録</h2>
    <% 
    String error = (String) request.getAttribute("error");
    if (error != null) { 
    %>
        <p style="color:red;"><%= error %></p>
    <% } %>
    <form action="<%= request.getContextPath() %>/inventory/add/confirm" method="post">
        <input type="text" placeholder="商品名" name="stock_name">
        <input type="text" placeholder="単価" name="stock_price">
        <input type="text" placeholder="在庫数" name="stock_number">
        <button type="button" onclick="location.href='<%= request.getContextPath() %>/inventory_home'">戻る</button>
        <button type="submit">商品追加</button>
    </form>
</body>
</html>
