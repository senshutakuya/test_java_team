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

/**
 * Servlet implementation class OrderDeleteServlet
 */
@WebServlet("/order/delete")
public class OrderDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッションからユーザの認証を確認
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            try {
                // 成功した場合、注文管理者のホームページにリダイレクト
                response.sendRedirect(request.getContextPath() + "/sales_management");

            } catch (Exception e) {
                e.printStackTrace(); // スタックトレースを出力
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            // セッションが無効またはユーザが認証されていない場合、ログインページにリダイレクト
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションからユーザの認証を確認
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            try {
                // セッションからUserBeanオブジェクトを取得
                UserBean user = (UserBean) session.getAttribute("user");

                // リクエストパラメータから注文IDを取得
                String ordersIdStr = request.getParameter("id");
             // リクエストパラメータから注文数を取得
                String quantityStr = request.getParameter("quantity");
                
                System.out.println(quantityStr);

                // OrderServiceのインスタンス化
                OrderService orderService = new OrderService();
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
                System.out.println("new cart contents: " + cart);
                if (cart == null || cart.isEmpty()) {
                	// 対象ユーザーの注文を取ってくる処理
                	OrderItemDto itemList = OrderService.list(user);
                	List<OrderItem> items = itemList.getOrderItemList();
                    cart = new HashMap<>();
                    System.out.println("null cart contents: " + cart);
                 // itemListの情報をもとに注文情報をcartに格納
                    for (OrderItem item : items) {
                        int OrderId = item.getId();
                        int quantity = item.getQuantity();
                        
                     // カートに数量をセット（既存の数量を上書き）
                        cart.put(OrderId, quantity);
                    }

                }

                

                // データベースの更新
                orderService.cartDelete(user, ordersIdStr);
                
                
             // セッションのカートからアイテムを更新
                cart = orderService.cartUpdate(cart, ordersIdStr, quantityStr);
                session.setAttribute("cart", cart);
                
             // 更新後のカート内容をログに出力
                System.out.println("Updated cart contents: " + cart);

                // 成功した場合、カート情報にリダイレクト
                response.sendRedirect(request.getContextPath() + "/order/check");

            } catch (Exception e) {
                e.printStackTrace(); // スタックトレースを出力
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            // セッションが無効またはユーザが認証されていない場合、ログインページにリダイレクト
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

}
