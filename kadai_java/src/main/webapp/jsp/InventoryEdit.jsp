<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>商品編集ページ</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/inventory_edit.css">
</head>
<body>
    <div class="container">
    	<div class="header d-flex justify-content-between align-items-center mb-4">
			<h2>在庫管理者様</h2>
		    <!-- logout_header.jspのインクルード -->
		    <div id="logout_button" >
		    	<%@ include file="logout_header.jsp" %>
		    </div>
		</div>

        <% if (error != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= error %>
            </div>
        <% } %>

        <% if (stockItem != null) { %>
        <div class="card">
            <form action="<%= request.getContextPath() %>/inventory/edit" method="post" class="mt-4">
                <div class="card-header">
                	<h3 class="card-title">商品編集</h3>
            	</div>
                <div class="form-group">
                    <label for="name">商品名</label>
                    <input type="text" class="form-control" id="name" name="name" value="<%= inputName != null ? inputName : "" %>">
                </div>
                <div class="form-group">
                    <label for="price">価格</label>
                    <input type="text" class="form-control" id="price" name="price" value="<%= inputPrice != null ? inputPrice : "" %>">
                </div>
                <div class="form-group">
                    <label for="stock">在庫数</label>
                    <input type="text" class="form-control" id="stock" name="stock" value="<%= inputStock != null ? inputStock : "" %>">
                </div>
                <button type="button" class="btn btn-primary" onclick="location.href='<%= request.getContextPath() %>/inventory_home'">戻る</button>
                <button type="submit" class="btn btn-success">送信</button>
            </form>
        </div>
        <% } else { %>
            <div class="alert alert-warning mt-4" role="alert">
                在庫情報が見つかりませんでした。
            </div>
        <% } %>
    </div>

    <!-- BootstrapのJavaScript -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
