<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>Logout Success</title>
    <script type="text/javascript">
        window.onload = function() {
            // キャッシュを削除
            if ('caches' in window) {
                caches.keys().then(function(names) {
                    for (let name of names) {
                        caches.delete(name);
                    }
                });
            }

            // ログインページへのリダイレクト
            setTimeout(function() {
                window.location.href = "<%= request.getContextPath() %>/login";
            }, 2000); // 2秒後にリダイレクト
        };
    </script>
</head>
<body>
		<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
	    %>
    <h1>ログアウトが成功しました。</h1>
    <p>ログインページにリダイレクトしています...</p>
</body>
</html>
