<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規登録</title>
<!-- Bootstrap CSS -->
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<!-- Optional theme -->
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap-theme.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <h2 class="text-center mb-4">新規登録</h2>

                <% 
                    // キャッシュを無効化するレスポンスヘッダーの設定
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    response.setHeader("Pragma", "no-cache");
                    response.addHeader("Cache-Control", "no-store");
                    response.setDateHeader("Expires", 0);

                    // AddResult を取得
                    String addResult = (String) request.getAttribute("AddResult");
                %>

                <%-- メッセージがあれば表示 --%>
                <%
                    if (addResult != null && !addResult.isEmpty()) {
                %>
                    <div class="alert alert-info">
                        <%= addResult %>
                    </div>
                <%
                    }
                %>

                <form action="<%= request.getContextPath() %>/user_add" method="post" class="mb-4" onsubmit="return validateForm()">
                    <div class="form-group">
                        <label for="name">Name:</label>
                        <input type="text" id="name" name="name" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="loginid">Login ID:</label>
                        <input type="text" id="loginid" name="loginid" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="authcode">Role:</label>
                        <select id="authcode" name="authcode" class="form-control" required>
                            <option value="1">在庫管理者</option>
                            <option value="2">注文管理者</option>
                        </select>
                    </div>
                    
                    <button type="submit" class="btn btn-secondary btn-block">送信</button>
                </form>

                <form action="<%= request.getContextPath() %>/login" method="get">
                    <button type="submit" class="btn btn-primary btn-block">ログインはこちら</button>
                </form>
            </div>
        </div>
    </div>

    <!-- バリデーション用のJavaScript -->
    <script>
        function validateForm() {
            var isValid = true;
            var errorMessage = [];

            // 名前のバリデーションチェック（20文字以内）
            var name = document.getElementById("name").value;
            if (name == null || name.length > 20) {
                errorMessage.push("・名前は20文字以内にしてください");
                isValid = false;
            }

            // ログインIDのバリデーションチェック（半角英数字、記号$と_のみ、8〜24文字）
            var loginid = document.getElementById("loginid").value;
            var loginidPattern = /^[a-zA-Z0-9$_]{8,24}$/;
            if (!loginidPattern.test(loginid)) {
                errorMessage.push("・ユーザIDは半角英数字(記号は$と_のみ)で、8文字以上24文字以内にしてください");
                isValid = false;
            }

            // パスワードのバリデーションチェック（半角英数字、記号$と_のみ、8〜24文字）
            var password = document.getElementById("password").value;
            var passwordPattern = /^[a-zA-Z0-9$_]{8,24}$/;
            if (!passwordPattern.test(password)) {
                errorMessage.push("・パスワードは半角英数字(記号は$と_のみ)で、8文字以上24文字以内にしてください");
                isValid = false;
            }

            // ロール（authcode）のバリデーションチェック（1か2であること）
            var authcode = parseInt(document.getElementById("authcode").value);
            if (!(authcode === 1 || authcode === 2)) {
                errorMessage.push("・在庫管理者か注文管理者を選択してください");
                isValid = false;
            }

            // エラーメッセージがあれば表示
            if (!isValid) {
                alert(errorMessage.join("\n"));
            }

            return isValid;  // バリデーションに合格した場合のみフォーム送信
        }
    </script>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
