package com.kenshu.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogoutSuccessServlet
 */
@WebServlet("/logout-success")
public class LogoutSuccessServlet extends HttpServlet {
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
        // ここではログアウト成功メッセージを表示
        request.getRequestDispatcher("/jsp/logout-success.jsp").forward(request, response);
    }
}

