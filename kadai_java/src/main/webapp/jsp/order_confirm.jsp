<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文完了画面</title>
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
<%@ include file="logout_header.jsp" %>

<%
/* messageを取得 */
String message = (String) request.getAttribute("message");
%>

<p><%= message %></p>

<form action="<%= request.getContextPath() %>/order/check" method="get">
	<button type="submit">戻る</button>
</form>
</body>
</html>
