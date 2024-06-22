package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.model.bean.UserBean;
import com.kenshu.model.dto.UserDto;
import com.kenshu.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginidStr = request.getParameter("loginid");
        String password = request.getParameter("password");

        // フォームの値をコンソールに表示
        System.out.println("Login ID: " + loginidStr);
        System.out.println("Password: " + password);

        // フォームの値をリクエスト属性に設定
        request.setAttribute("loginid", loginidStr);
        request.setAttribute("password", password);

        try {
            boolean authenticated = loginService.login(loginidStr, password);

            if (authenticated) {
                UserDto userDto = loginService.getUserDto(loginidStr);
                int accessPermission = determineAccessPermission(userDto);
                redirectToOperationScreen(response, accessPermission);
            } else {
                request.setAttribute("error", "authentication_failed");
                request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
        }
    }

    private int determineAccessPermission(UserDto userDto) {
        UserBean userBean = userDto.get(0);
        return userBean.getAuthcode();
    }

    private void redirectToOperationScreen(HttpServletResponse response, int accessPermission) throws IOException {
        if (accessPermission == 1) {
            response.sendRedirect("jsp/stock_management.jsp");
        } else if (accessPermission == 2) {
            response.sendRedirect("jsp/sales_management.jsp");
        } else {
            response.sendRedirect("jsp/access_denied.jsp");
        }
    }
}
