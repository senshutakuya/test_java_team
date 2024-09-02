package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.model.bean.UserBean;
import com.kenshu.service.LoginService;
import com.kenshu.service.LogoutService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class UserAdd
 */
@WebServlet("/user_add")
public class UserAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */


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
        System.out.println("メソッドgetです！セッション変数の中身は" + session);
        
        if (session != null && session.getAttribute("user") != null) {
            try {
                UserBean user = (UserBean) session.getAttribute("user");
                System.out.println("デバッグリダイレクト");
                int authcode = user.getAuthcode();
                LogoutService.redirectToOperationScreen(request, response, authcode);
                return;
            } catch (Exception e) {
                System.out.println("デバッグです");
                e.printStackTrace();
                session.invalidate();
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        }else {
            System.out.println("失敗、セッション変数の中身は" + session);
//        	user_add.jspに遷移
            request.getRequestDispatcher("/jsp/user_add.jsp").forward(request, response);
            }
                
    } 
    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String AddResult =""; 
		HttpSession session = request.getSession(false);
        System.out.println("メソッドpostです！セッション変数の中身は" + session);
		if (session != null && session.getAttribute("user") != null) {
            try {
                UserBean user = (UserBean) session.getAttribute("user");
                System.out.println("デバッグリダイレクト");
                int authcode = user.getAuthcode();
                LogoutService.redirectToOperationScreen(request, response, authcode);
                return;
            } catch (Exception e) {
                System.out.println("デバッグです");
                e.printStackTrace();
                session.invalidate();
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        }else {
            System.out.println("失敗、セッション変数の中身は" + session);
//            リクエストパラメータの取得
         // フォームから送信されたパラメータを取得
            String name = request.getParameter("name");
            String loginid = request.getParameter("loginid");
            String password = request.getParameter("password");
            String authcode = request.getParameter("authcode");

            // パラメータを使ってユーザーを追加
            AddResult = LoginService.userAdd(name, loginid, password, authcode);

            // 改行を<br>タグに変換
            AddResult = AddResult.replace("\n", "<br>").replace("\r", "");
            // 結果をリクエストにセット
            request.setAttribute("AddResult", AddResult);
            System.out.println("AddResultは:"+AddResult);
//        	user_add.jspに遷移
            request.getRequestDispatcher("/jsp/user_add.jsp").forward(request, response);
            }
	}

}
