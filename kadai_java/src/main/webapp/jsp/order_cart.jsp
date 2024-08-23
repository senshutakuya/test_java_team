<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kenshu.model.dto.OrderItemDto" %>
<%@ page import="com.kenshu.model.bean.OrderItem" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Map.Entry" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>注文管理</title>
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
                }
            };
            xhr.send('id=' + itemId + '&quantity=' + quantity);
        }
    </script>
    <!-- 外部のcalculateTotal.jsファイルを読み込む -->
    <script src="<%= request.getContextPath() %>/js/calculateTotal.js"></script>
</head>
<body>
		<% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
	    %>
    <h2>注文管理者</h2>    
    <%@ include file="logout_header.jsp" %>
    <h1>カート商品一覧</h1>
    
    <% 
    /* データベースの情報を取得 */
    OrderItemDto itemList = (OrderItemDto) request.getAttribute("itemList");
    /* セッションのカート情報を取得 */
    Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
    if (itemList != null && itemList.size() > 0) { 
    %>
    <table border="1">
        <tr>
            <th>商品名</th>
            <th>価格</th>
            <th>注文数</th>
            <th>削除</th>
        </tr>
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
                        <select id="quantity-<%= item.getId() %>" name="quantity" onchange="updateQuantity(<%= item.getId() %>)">
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
                        <button type="submit">削除</button>
                    </td>
                </form>
            </tr>
        <% } %>
    </table>
    <% } else { %>
        <p>注文商品が見つかりませんでした。</p>
    <% } %>
    
    <h3 id="totalAmount">合計金額: 0 円</h3>
    
    <form action="<%= request.getContextPath() %>/sales_management" method="get">
        <button type="submit">戻る</button>
    </form>
    
    <form action="<%= request.getContextPath() %>/order/confirm" method="post">
        <button type="submit">注文確定</button>
    </form>

    <!-- ページ読み込み時に合計金額を計算 -->
    <script>
        calculateTotal();
    </script>
</body>
</html>
