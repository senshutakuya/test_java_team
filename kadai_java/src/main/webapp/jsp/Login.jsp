<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="0">
        <title>Login</title>
        <!-- Bootstrap CSSの読み込み -->
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript">
        window.onload = function() {
            // キャッシュクリア用のリクエスト
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "<%= request.getContextPath() %>/clearCache", true);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send();
        };
    </script>
    </head>
    <body class="bg-light">
    	<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
	    %>
	    <p>
                10000001<br>
                password1<br><br>
                
                10000002<br>
                password2<br><br>
                
                エラー用<br>
                10000003<br>
                password3<br>
            </p>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card mt-5 shadow-sm">
                        <div class="card-header bg-primary text-white text-center">
                            <h3>Login</h3>
                        </div>
                        <div class="card-body">
                            <form action="login" method="post">
                                <div class="form-group">
                                    <label for="loginid">Login ID:</label>
                                    <input type="text" id="loginid" name="loginid" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="password">Password:</label>
                                    <input type="password" id="password" name="password" class="form-control" required>
                                </div>
                                <button type="submit" class="btn btn-primary btn-block">Login</button>
                            </form>
                            <br>
                            <form action="<%= request.getContextPath() %>/user_add" method="get" class="d-inline">
				                <button type="submit" class="btn btn-secondary btn-block">新規登録</button>
				            </form>
                        </div>
                        <% 
                        String error = (String) request.getAttribute("error");
                        if (error != null) { 
                        %>
                            <div class="card-footer text-danger text-center">
                                <%= error %>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
