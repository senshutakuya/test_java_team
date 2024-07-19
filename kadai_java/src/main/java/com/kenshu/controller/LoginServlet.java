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
        request.getRequestDispatcher("jsp/Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginidStr = request.getParameter("loginid");
        String password = request.getParameter("password");

        // デバッグメッセージをコンソールに出力
        System.out.println("Received loginid: " + loginidStr);
        System.out.println("Received password: " + password);

        try {
            // ログインIDを整数に変換
            int loginid = Integer.parseInt(loginidStr);

            UserBean user = loginService.login(loginid, password);

            if (user != null) {
                // ログイン成功の場合の処理

                // セッションにユーザー情報を保存
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // authcodeを取得してリダイレクト先を決定
                int authcode = user.getAuthcode();
                redirectToOperationScreen(request,response, authcode);
            } else {
                // 認証失敗の場合の処理
                request.setAttribute("error", "authentication_failed");
                request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            // ログインIDが整数に変換できない場合の処理
            request.setAttribute("error", "invalid_loginid_format");
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
        } catch (Exception e) {
        	e.printStackTrace();
            request.setAttribute("error", "database_error");
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
        }
    }

    private void redirectToOperationScreen(HttpServletRequest request, HttpServletResponse response, int authcode) throws ServletException, IOException {
        // アクセス権限に応じて適切な画面にリダイレクトする処理
        String redirectURL = null;
        if (authcode == 1) {
            System.out.println("authcodeは"+authcode);
            redirectURL = request.getContextPath() + "/inventory_home";
        } else if (authcode == 2) {
            System.out.println("authcodeは"+authcode);
            redirectURL = request.getContextPath() + "/sales_management";
        } else {
            // 認証失敗の場合の処理
            System.out.println("test");
            request.setAttribute("error", "authentication_failed");
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
            return; // リダイレクトする前に処理を終了するために return を使用します
        }
//        ここでreidrectURLに対してリダイレクト
        response.sendRedirect(redirectURL);
    }
}
