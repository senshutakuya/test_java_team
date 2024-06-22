<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <form action="login" method="post">
        <label for="loginid">Login ID:</label>
        <input type="text" id="loginid" name="loginid" required><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br>
        <input type="submit" value="Login">
    </form>
    <% if (request.getParameter("error") != null) { %>
        <p style="color:red;">Invalid login ID or password</p>
    <% } %>
</body>
</html>
