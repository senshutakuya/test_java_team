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
    <header>
        <form action="<%= request.getContextPath() %>/logout" method="post">
            <button type="submit">Logout</button>
        </form>
    </header>
