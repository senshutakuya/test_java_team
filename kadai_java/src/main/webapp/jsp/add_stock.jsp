<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>商品登録</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/inventory_add.css">
</head>
<body>
	<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
    %>
    
    <div class="container mt-5">
    	<div class="header d-flex justify-content-between align-items-center mb-4">
			<h2>在庫管理者様</h2>
		    <!-- logout_header.jspのインクルード -->
		    <div id="logout_button" >
		    	<%@ include file="logout_header.jsp" %>
		    </div>
		</div>
        <h2 class="mb-4">商品登録</h2>
        
        <% 
        String error = (String) request.getAttribute("error");
        if (error != null) { 
        %>
            <div class="alert alert-danger" role="alert">
                <%= error %>
            </div>
        <% } %>
        
        <form action="<%= request.getContextPath() %>/inventory/add/confirm" method="post">
            <div class="form-group">
                <input type="text" class="form-control" placeholder="商品名" name="stock_name">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="単価" name="stock_price">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="在庫数" name="stock_number">
            </div>
            <div class="d-flex justify-content-between">
                <button type="button" class="btn btn-secondary" onclick="location.href='<%= request.getContextPath() %>/inventory_home'">戻る</button>
                <button type="submit" class="btn btn-primary">商品追加</button>
            </div>
        </form>
    </div>
    
    <!-- BootstrapのJavaScript -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
