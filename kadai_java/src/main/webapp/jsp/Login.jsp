<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login Page</h1>
    <form action="login" method="post">
        Login ID: <input type="text" name="loginid"><br>
        Password: <input type="password" name="password"><br>
        <input type="submit" value="Login">
    </form>
    <%
        String error = (String) request.getAttribute("error");
        String loginid = (String) request.getAttribute("loginid");
        String password = (String) request.getAttribute("password");

        if (loginid != null && password != null) {
            out.println("<p>Login ID: " + loginid + "</p>");
            out.println("<p>Password: " + password + "</p>");
        }

        if (error != null) {
            if ("authentication_failed".equals(error)) {
                out.println("<p style='color:red;'>Authentication failed. Please try again.</p>");
            } else {
                out.println("<p style='color:red;'>Error: " + error + "</p>");
            }
        }
    %>
</body>
</html>
