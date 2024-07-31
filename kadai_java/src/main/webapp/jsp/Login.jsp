<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <p>
        10000001<br>
        password1<br><br>
        
        10000002<br>
        password2<br><br>
        
        エラー用<br>
        10000003<br>
        password3<br>
    </p>
    <form action="login" method="post">
        <label for="loginid">Login ID:</label>
        <input type="text" id="loginid" name="loginid" required><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br>
        <input type="submit" value="Login">
    </form>
    
    <% 
    String error = (String) request.getAttribute("error");
    if (error != null) { 
    %>
        <p style="color:red;"><%= error %></p>
    <% } %>
</body>
</html>
