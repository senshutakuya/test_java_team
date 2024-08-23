<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.kenshu.model.dto.StockItemDto" %>
<%@ page import="com.kenshu.model.bean.StockItem" %>

<jsp:useBean id="itemList" scope="request" class="com.kenshu.model.dto.StockItemDto" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>注文管理</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/order_management.css" rel="stylesheet">
</head>
<body>
    <% 
        // キャッシュを無効化するレスポンスヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setDateHeader("Expires", 0); // Proxies.
    %>

    <div class="container">
        <!-- ヘッダー -->
        <div class="header d-flex justify-content-between align-items-center mb-4">
            <h2>注文管理者</h2>
            <%@ include file="logout_header.jsp" %>
        </div>

        <!-- エラーメッセージ表示 -->
        <% 
            String errorMessage = (String) session.getAttribute("error");
            if (errorMessage != null) {
        %>
        <div class="alert alert-danger" role="alert">
            <%= errorMessage %>
        </div>
        <% 
            session.removeAttribute("error"); // エラーメッセージをセッションから削除
        } 
        %>

        <!-- 商品一覧 -->
        <h3>商品一覧</h3>
        <% if (itemList != null && itemList.getStockItemList() != null) {
            List<StockItem> items = itemList.getStockItemList();
        %>
        <table class="table table-bordered">
            <thead class="thead-light">
                <tr>
                    <th>商品名</th>
                    <th>価格</th>
                    <th>在庫数</th>
                    <th>注文数</th>
                    <th>カートにいれる</th>
                </tr>
            </thead>
            <tbody>
                <% for (StockItem item : items) { %>
                <tr>
                    <form action="<%= request.getContextPath() %>/order/add?id=<%= item.getId() %>" method="post">
                        <td><%= item.getName() %></td>
                        <td>¥<%= item.getPrice() %></td>
                        <td><%= item.getStock() %></td>
                        <td>
                            <select id="quantity" name="quantity" class="form-control">
                                <% 
                                    for (int i = 1; i <= item.getStock(); i++) {
                                        out.println("<option value='" + i + "'>" + i + "</option>");
                                    }
                                %>
                            </select>
                        </td>
                        <td>
                            <input type="hidden" name="id" value="<%= item.getId() %>" />
                            <button type="submit" class="btn btn-primary">カートにいれる</button>
                        </td>
                    </form>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else { %>
        <p class="text-danger">在庫商品が見つかりませんでした。</p>
        <% } %>

        <!-- 注文確認ボタン -->
        <div class="text-center mt-4">
            <form action="<%= request.getContextPath() %>/order/check" method="get">
                <button type="submit" class="btn btn-success btn-lg">注文確認</button>
            </form>
        </div>
    </div>

</body>
</html>
