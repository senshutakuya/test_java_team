<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kenshu.model.dto.OrderItemDto" %>
<%@ page import="com.kenshu.model.bean.OrderItem" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Map.Entry" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>注文管理</title>
    <!-- BootstrapのCSSをCDNから読み込む -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/order_cart.css">
    <!-- jQueryライブラリをCDNから読み込む -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function updateQuantity(itemId) {
            var quantity = document.getElementById('quantity-' + itemId).value;
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '<%= request.getContextPath() %>/order/updateQuantity', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    console.log('Quantity updated successfully');
                    calculateTotal(); // 合計金額の再計算
                }
            };
            xhr.send('id=' + itemId + '&quantity=' + quantity);
        }
    </script>
    <!-- 外部のcalculateTotal.jsファイルを読み込む -->
    <script src="<%= request.getContextPath() %>/js/calculateTotal.js"></script>
</head>
<body>
    <div class="container mt-5">
        <% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
        %>


		<div class="header d-flex justify-content-between align-items-center mb-4">
			<h2>注文管理者様</h2>
		    <!-- logout_header.jspのインクルード -->
		    <div id="logout_button" >
		    	<%@ include file="logout_header.jsp" %>
		    </div>
		</div>
		

        <h1 class="text-center mb-4">カート商品一覧</h1>
        
        <% 
        /* データベースの情報を取得 */
        OrderItemDto itemList = (OrderItemDto) request.getAttribute("itemList");
        /* セッションのカート情報を取得 */
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (itemList != null && itemList.size() > 0) { 
        %>
        <table class="table table-bordered table-striped">
            <thead class="thead-dark">
                <tr>
                    <th>商品名</th>
                    <th>価格</th>
                    <th>注文数</th>
                    <th>削除</th>
                </tr>
            </thead>
            <tbody>
            <% for (int i = 0; i < itemList.size(); i++) {
                OrderItem item = itemList.get(i);
                int cartQuantity = (cart != null && cart.containsKey(item.getId())) ? cart.get(item.getId()) : 1;
                // デバッグ用に cartQuantity の値を出力
                out.println("<!-- Item ID: " + item.getId() + ", cartQuantity: " + cartQuantity + " -->");
            %>
                <tr>
                    <form action="<%= request.getContextPath() %>/order/delete?id=<%= item.getId() %>" method="post">
                        <td><%= item.getName() %></td>
                        <td><%= item.getPrice() %></td>
                        <td>
                            <select id="quantity-<%= item.getId() %>" name="quantity" class="form-control" onchange="updateQuantity(<%= item.getId() %>)">
                                <% 
                                    int minQuantity = 1;
                                    int maxQuantity = item.getStock() + cartQuantity;
                                    for (int j = minQuantity; j <= maxQuantity; j++) {
                                        boolean selected = (j == cartQuantity);
                                        // デバッグ用に selected 属性を出力
                                        out.println("<!-- Option Value: " + j + ", Selected: " + selected + " -->");
                                        out.println("<option value='" + j + "'" + (selected ? " selected='selected'" : "") + ">" + j + "</option>");
                                    }
                                %>
                            </select>
                        </td>
                        <td>
                            <input type="hidden" name="id" value="<%= item.getId() %>" />
                            <button type="submit" class="btn btn-danger">削除</button>
                        </td>
                    </form>
                </tr>
            <% } %>
            </tbody>
        </table>
        <% } else { %>
            <div class="alert alert-warning" role="alert">
                注文商品が見つかりませんでした。
            </div>
        <% } %>

        <h3 id="totalAmount" class="text-right">合計金額: 0 円</h3>

        <div class="text-center mt-4">
            <form action="<%= request.getContextPath() %>/sales_management" method="get" class="d-inline">
                <button type="submit" class="btn btn-secondary">戻る</button>
            </form>

            <form action="<%= request.getContextPath() %>/order/confirm" method="post" class="d-inline">
                <button type="submit" class="btn btn-primary">注文確定</button>
            </form>
        </div>

    </div>

    <!-- ページ読み込み時に合計金額を計算 -->
    <script>
        calculateTotal();
    </script>

    <!-- BootstrapのJavaScript -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
