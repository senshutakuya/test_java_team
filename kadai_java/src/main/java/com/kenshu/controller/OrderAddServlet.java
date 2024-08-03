package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.model.bean.UserBean; // 正しいUserBeanクラスをインポート
import com.kenshu.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class OrderAddServlet
 */
@WebServlet("/order/add")
public class OrderAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションからユーザの認証を確認
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            try {
                // セッションからUserBeanオブジェクトを取得
                UserBean user = (UserBean) session.getAttribute("user");

                // リクエストパラメータからアイテムIDと数量を取得
                String itemsIdStr = request.getParameter("id");
                String quantityStr = request.getParameter("quantity");

                // OrderServiceのインスタンス化
                OrderService orderService = new OrderService();
                orderService.cartAdd(itemsIdStr, user, quantityStr);

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
