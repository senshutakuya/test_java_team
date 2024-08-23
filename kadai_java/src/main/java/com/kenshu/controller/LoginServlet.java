package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.model.bean.UserBean;
import com.kenshu.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// キャッシュを無効化
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.addHeader("Cache-Control","no-store");
        response.setDateHeader("Expires", 0); // Proxies.
        HttpSession session = request.getSession(false);
        System.out.println("メソッドgetです！セッション変数の中身は" + session);

        if (session != null && session.getAttribute("user") != null) {
            try {
                UserBean user = (UserBean) session.getAttribute("user");
                System.out.println("デバッグリダイレクト");
                int authcode = user.getAuthcode();
                redirectToOperationScreen(request, response, authcode);
                return;
            } catch (Exception e) {
                System.out.println("デバッグです");
                e.printStackTrace();
                session.invalidate();
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            System.out.println("失敗、セッション変数の中身は" + session);
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginidStr = request.getParameter("loginid");
        String password = request.getParameter("password");

        System.out.println("Received loginid: " + loginidStr);
        System.out.println("Received password: " + password);

        if (loginidStr == null || password == null || password.isEmpty()) {
            request.setAttribute("error", "ログインIDとパスワードを入力してください");
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
            return;
        }

        try {
            int loginid = Integer.parseInt(loginidStr);

            UserBean user = loginService.login(loginid, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                int authcode = user.getAuthcode();
                redirectToOperationScreen(request, response, authcode);
            } else {
                request.setAttribute("error", "認証失敗");
                request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ログインIDは数値で入力してください");
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "恐れ入りますがIDとパスワードをご確認の上もう一度やり直してください");
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
        }
    }

    private void redirectToOperationScreen(HttpServletRequest request, HttpServletResponse response, int authcode) throws ServletException, IOException {
        String redirectURL = null;
        if (authcode == 1) {
            System.out.println("authcodeは" + authcode);
            redirectURL = request.getContextPath() + "/inventory_home";
        } else if (authcode == 2) {
            System.out.println("authcodeは" + authcode);
            redirectURL = request.getContextPath() + "/sales_management";
        } else {
            System.out.println("test");
            request.setAttribute("error", "認証失敗");
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
            return;
        }
        response.sendRedirect(redirectURL);
    }
}
