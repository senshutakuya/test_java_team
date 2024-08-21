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
 * Servlet implementation class OrderConfirmServlet
 */
@WebServlet("/order/confirm")
public class OrderConfirmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// セッションからユーザの認証を確認
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            try {
                System.out.println("getが非対応だから/order/checkにリダイレクト");
                // 成功した場合、注文管理者のホームページにリダイレクト
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッションからユーザの認証を確認
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            try {
            	System.out.println("/order/confirmのpost");
            	// セッションからUserBeanオブジェクトを取得
                UserBean user = (UserBean) session.getAttribute("user");
                
                boolean success_flag = false;
                
                
//            	cart情報を取得
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
                
//                もしカート情報がnullなら
                if (cart == null || cart.isEmpty()) {
                	// 対象ユーザーの注文を取ってくる処理
                	OrderItemDto itemList = OrderService.list(user);
//                	配列で注文情報をitemsに格納
                	List<OrderItem> items = itemList.getOrderItemList();
                    // セッションcartの初期化
                    cart = new HashMap<>();
                 // itemListの情報をもとに注文情報をcartに格納
                    for (OrderItem item : items) {
                        int OrderId = item.getId();
                        int quantity = item.getQuantity();
                        
                     // カートに数量をセット（既存の数量を上書き）
                        cart.put(OrderId, quantity);
                    }
                   
                }
                
                
//             もし上記の初期化をしてもcart情報がnullならそもそも注文がない
               if (cart == null || cart.isEmpty()) {
            	   request.setAttribute("message", "注文商品が確認できませんでしたご確認ください。");
            	   request.getRequestDispatcher("/jsp/order_confirm.jsp").forward(request, response);
               }
               
               System.out.println("confirmサーブレット:cart情報は"+cart);

                
//             以下はcart情報があるものとする
               success_flag = OrderService.cartConfirm(cart,user,session);
                
                if(success_flag) {
                	request.setAttribute("message", "注文完了しました");
                	// 成功した場合、注文完了ページに遷移
                    request.getRequestDispatcher("/jsp/order_confirm.jsp").forward(request, response);
                }else {
                	// 失敗した場合、注文完了(エラーページ)に遷移
                	request.setAttribute("message", "注文途中にエラーが発生しました。"
                			+ "対象商品が品切れの可能性がありますご確認ください。");
                	// 成功した場合、注文完了ページに遷移
                    request.getRequestDispatcher("/jsp/order_confirm.jsp").forward(request, response);
                }
                
                
                

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
