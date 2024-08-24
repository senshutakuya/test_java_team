<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文完了画面</title>
<!-- BootstrapのCSSをCDNから読み込む -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/order_confirm.css">
</head>
<body>
	<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
	%>

<div class="container mt-4">
    <div class="header d-flex justify-content-between align-items-center mb-4">
		<h2>注文管理者様</h2>
		    <!-- logout_header.jspのインクルード -->
		 <div id="logout_button" >
		   	<%@ include file="logout_header.jsp" %>
	    </div>
	</div>

    <div class="alert alert-success mt-4">
        <%
        /* messageを取得 */
        String message = (String) request.getAttribute("message");
        %>
        <p class="mb-0"><%= message %></p>
    </div>

    <div class="text-center mt-4">
        <form action="<%= request.getContextPath() %>/order/check" method="get">
            <button type="submit" class="btn btn-primary">戻る</button>
        </form>
    </div>
</div>

<!-- BootstrapのJSと依存関係をCDNから読み込む -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.min.js"></script>
</body>
</html>
