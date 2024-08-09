<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.kenshu.model.dto.OrderItemDto" %>
<%@ page import="com.kenshu.model.bean.OrderItem" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>注文管理</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> <!-- jQueryの読み込み -->
    <script src="<%= request.getContextPath() %>/js/calculateTotal.js"></script> <!-- JavaScriptファイルの読み込み -->
</head>
<body>
    <h2>注文管理者</h2>    
    <%@ include file="logout_header.jsp" %>
    <h1>カート商品一覧</h1>
    
    <% 
    OrderItemDto itemList = (OrderItemDto) request.getAttribute("itemList");
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
            int currentQuantity = item.getQuantity(); // 現在の注文数を取得
        %>
            <tr>
                <form action="<%= request.getContextPath() %>/order/delete?id=<%= item.getId() %>" method="post">
                    <td><%= item.getName() %></td>
                    <td><%= item.getPrice() %></td>
                    <td>
                        <select id="quantity" name="quantity">
                            <% 
                                // 在庫数に基づいて<select>ボックスのオプションを生成
                                for (int j = 1; j <= item.getStock()+currentQuantity; j++) {
                                    // 現在の注文数がオプションの値と一致する場合、選択状態にする
                                    boolean selected = (j == currentQuantity);
                                    out.println("<option value='" + j + "'" + (selected ? " selected" : "") + ">" + j + "</option>");
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
    
    <form action="<%= request.getContextPath() %>/order/confirm" method="post">
        <button type="submit">注文確定</button>
    </form>

</body>
</html>
