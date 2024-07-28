<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.kenshu.model.bean.StockItem" %>

<%
    String error = (String) session.getAttribute("error");
    StockItem stockItem = (StockItem) session.getAttribute("stockItem");
    String inputName = (String) session.getAttribute("inputName");
    String inputPrice = (String) session.getAttribute("inputPrice");
    String inputStock = (String) session.getAttribute("inputStock");
    session.removeAttribute("error");
    session.removeAttribute("stockItem");
    session.removeAttribute("inputName");
    session.removeAttribute("inputPrice");
    session.removeAttribute("inputStock");

    if (inputName == null && stockItem != null) {
        inputName = stockItem.getName();
    }
    if (inputPrice == null && stockItem != null) {
        inputPrice = String.valueOf(stockItem.getPrice());
    }
    if (inputStock == null && stockItem != null) {
        inputStock = String.valueOf(stockItem.getStock());
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品編集ページ</title>
</head>
<body>
<% if (error != null) { %>
    <p style="color:red;"><%= error %></p>
<% } %>

<% if (stockItem != null) { %>
    <form action="<%= request.getContextPath() %>/inventory/edit" method="post">
        <h2>商品編集</h2>
        <p>商品名</p>
        <input type="text" id="name" name="name" value="<%= inputName != null ? inputName : "" %>" /> 
        <p>価格</p>
        <input type="text" id="price" name="price" value="<%= inputPrice != null ? inputPrice : "" %>" /> 
        <p>在庫数</p>
        <input type="text" id="stock" name="stock" value="<%= inputStock != null ? inputStock : "" %>" />
        <button type="submit">送信</button>
    </form>
<% } else { %>
    <p>在庫情報が見つかりませんでした。</p>
<% } %>
</body>
</html>
