package com.kenshu.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class InventoryAdd
 */
//在庫管理者のホーム画面
@WebServlet("/inventory/add")
public class InventoryAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		System.out.println("移動できたかのtest");
		// セッションによるユーザの認可処理
        HttpSession session = request.getSession(false);
        System.out.println("セッション変数の中身は"+ session);
        
//        もしセッションに値が入っていて且つuserという値が空ではなく入っていたなら
        if (session != null && session.getAttribute("user") != null) {
        	try {
//        		商品登録画面に遷移
        		request.getRequestDispatcher("/jsp/add_stock.jsp").forward(request, response);
        	}catch(Exception e){
        		System.out.println("デバッグです");
        		System.out.println(e);
        		request.getRequestDispatcher("/jsp/eroor.jsp").forward(request, response);
        	}
//            セッション関係が違うならログインサーブレットに遷移
        } else {
        	System.out.println("失敗、セッション変数の中身は"+ session);
            response.sendRedirect(request.getContextPath() + "/login");
            
   
        }
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
