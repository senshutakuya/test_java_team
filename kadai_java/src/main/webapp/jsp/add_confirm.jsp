<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品登録完了画面</title>
</head>
<body>
	<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
    %>
<p>登録完了しました</p>
<button type="button" onclick="location.href='<%= request.getContextPath() %>/inventory_home'">戻る</button>
</body>
</html>