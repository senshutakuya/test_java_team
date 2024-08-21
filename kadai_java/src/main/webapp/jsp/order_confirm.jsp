<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文完了画面</title>
</head>
<body>
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
