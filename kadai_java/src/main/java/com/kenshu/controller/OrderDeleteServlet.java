package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.model.bean.UserBean;
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

                // OrderServiceのインスタンス化
                OrderService orderService = new OrderService();
                orderService.cartDelete(user, ordersIdStr);

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

}
