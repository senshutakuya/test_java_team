package com.kenshu.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kenshu.model.bean.OrderItem;
import com.kenshu.model.bean.UserBean;
import com.kenshu.model.dto.OrderItemDto;
import com.kenshu.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order/add")
public class OrderAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションからユーザの認証を確認
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            try {
                // 成功した場合、注文管理者のホームページにリダイレクト
                response.sendRedirect(request.getContextPath() + "/sales_management");

            } catch (Exception e) {
                e.printStackTrace();
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            // セッションが無効またはユーザが認証されていない場合、ログインページにリダイレクト
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            try {
                UserBean user = (UserBean) session.getAttribute("user");

				int itemsId = 0;

				int quantity = 0;
                OrderItemDto itemList = OrderService.list(user);
                List<OrderItem> items = itemList.getOrderItemList();

                @SuppressWarnings("unchecked")
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

                if (cart == null || cart.isEmpty()) {
                    cart = new HashMap<>();

                    for (OrderItem item : items) {
                        int cart_orderId = item.getId();
                        int cart_quantity = item.getQuantity();

                        cart.put(cart_orderId, cart_quantity);
                    }
                }

                // リクエストパラメータからアイテムIDと数量を取得
                String itemsIdStr = request.getParameter("id");
                String quantityStr = request.getParameter("quantity");
                
                try {
                    if (itemsIdStr != null && !itemsIdStr.isEmpty()) {
                        itemsId = Integer.parseInt(itemsIdStr);
                    } else {
                        session.setAttribute("error", "対象商品が見つかりません。");
                        response.sendRedirect(request.getContextPath() + "/sales_management");
                        return;
                    }

                    if (quantityStr != null && !quantityStr.isEmpty()) {
                        quantity = Integer.parseInt(quantityStr);
                        if (quantity <= 0) {
                            session.setAttribute("error", "数量は1以上でなければなりません。");
                            response.sendRedirect(request.getContextPath() + "/sales_management");
                            return;
                        }
                    } else {
                        session.setAttribute("error", "数量が無効です。");
                        response.sendRedirect(request.getContextPath() + "/sales_management");
                        return;
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("error", "無効な数値が入力されました。");
                    response.sendRedirect(request.getContextPath() + "/sales_management");
                    return;
                }
                


                
                
                // OrderServiceを使用して、itemIdに対応するorderIdをデータベースから取得
                OrderService orderService = new OrderService();
                int availableStock = orderService.getAvailableStock(itemsId); // 在庫数を取得
                System.out.println("データベースを更新する前のセッションのカート情報は"+cart);
                
                
//              まずはデータベースに追加する処理
                orderService.cartAdd(itemsIdStr, user, quantityStr, cart);
                
                
                
//              orderIdをItemIdから取得
                Integer orderId = orderService.getOrderIdByItemId(itemsId);
                
                
                System.out.println("Servlet:在庫数は"+availableStock);


                // orderIdがnullでないことを確認
                if (orderId != null) {
                    int existingQuantity = cart.getOrDefault(orderId, 0);
                    int newQuantity = existingQuantity + quantity;
                    
                    System.out.println("Servlet:セッションの注文数は"+existingQuantity);
                    System.out.println("Servlet:新しく追加する注文数は"+quantity);
 

                    if (newQuantity > availableStock) {
                        session.setAttribute("error", "在庫が不足しています。");
                        response.sendRedirect(request.getContextPath() + "/sales_management");
                        return;
                    }

                    cart.put(orderId, newQuantity);
                    session.setAttribute("cart", cart);

                    response.sendRedirect(request.getContextPath() + "/sales_management");
                } else {
                    session.setAttribute("error", "注文IDの取得に失敗しました。");
                    response.sendRedirect(request.getContextPath() + "/sales_management");
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }


}
