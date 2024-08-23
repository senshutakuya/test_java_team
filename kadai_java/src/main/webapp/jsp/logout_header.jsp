<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- logout_header.jsp -->
<!DOCTYPE html>
<html>
<head>
    <title>My Application</title>
<!--     <link rel="stylesheet" type="text/css" href="styles.css"> -->
</head>
<body>
		<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
	    %>
    <header>
        <form action="<%= request.getContextPath() %>/logout" method="post">
            <button type="submit">Logout</button>
        </form>
    </header>
