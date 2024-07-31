<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Logout Success</title>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
</head>
<body>
    <h1>ログアウトが成功しました。</h1>
    <p>ログインページにリダイレクトしています...</p>
    <script>
        // ログインページへのリダイレクト
        setTimeout(function() {
            window.location.href = "<%= request.getContextPath() %>/login";
        }, 2000); // 2秒後にリダイレクト
    </script>
</body>
</html>
