package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.model.bean.UserBean;
import com.kenshu.service.LogoutService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// キャッシュを無効化
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control","no-store");
        response.setDateHeader("Expires", 0); // Proxies.

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            try {
                UserBean user = (UserBean) session.getAttribute("user"); // 型キャスト
                int authcode = user.getAuthcode();
                LogoutService.redirectToOperationScreen(request, response, authcode);
                return; // リダイレクト後の処理を防ぐために return を使用します
            } catch(Exception e) {
                e.printStackTrace(); // 詳細なエラーメッセージ
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションの無効化
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // キャッシュ制御ヘッダーの設定
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.

        // ログアウト成功ページへリダイレクト
        response.sendRedirect(request.getContextPath() + "/logout-success");
    }
}
