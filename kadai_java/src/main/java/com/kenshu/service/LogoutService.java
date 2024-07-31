package com.kenshu.service;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoutService {

	public static void redirectToOperationScreen(HttpServletRequest request, HttpServletResponse response, int authcode) throws ServletException, IOException {
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
	        request.setAttribute("error", "認証失敗");
	        request.getRequestDispatcher("/jsp/Login.jsp").forward(request, response);
	        return; // リダイレクトする前に処理を終了するために return を使用します
	    }
//	    ここでreidrectURLに対してリダイレクト
	    response.sendRedirect(redirectURL);
	}
}


