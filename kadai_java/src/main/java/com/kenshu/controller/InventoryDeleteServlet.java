package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.model.bean.UserBean;
import com.kenshu.service.StockService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class InventoryDeleteServlet
 */
@WebServlet("/inventory/delete")
public class InventoryDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        System.out.println("メソッドgetです！セッション変数の中身は" + session);
        
        
        if (session != null && session.getAttribute("user") != null) {
            try {
            	System.out.println("debug_get:delete_test");
            	response.sendRedirect(request.getContextPath() + "/inventory_home");
            } catch (Exception e) {
                System.out.println("デバッグ: 例外が発生しました");
                System.out.println(e);
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            System.out.println("失敗: セッション変数の中身は" + session);
            response.sendRedirect(request.getContextPath() + "/login");
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        System.out.println("メソッドgetです！セッション変数の中身は" + session);

        
        
        
        if (session != null && session.getAttribute("user") != null) {
            try {
//            	デバッグの確認
            	System.out.println("debug_post:delete_test");
//              itemsテーブルのidを取得
                String items_id_str = request.getParameter("id");
                int item_id =0;
                item_id = Integer.parseInt(items_id_str);
//                インスタンス化
                StockService stockService = new StockService();
             // セッションからUserBeanオブジェクトを取得
                UserBean user = (UserBean) session.getAttribute("user");
                String userId = user.getLoginid();
//                単一事にSQLをするのでサービスクラスで処理を行う
                stockService.delete_Inventory(item_id,userId);
//                最終的にはそのまま在庫管理のホーム画面にリダイレクトする
            	response.sendRedirect(request.getContextPath() + "/inventory_home");
            } catch (Exception e) {
                System.out.println("デバッグ: 例外が発生しました");
                System.out.println(e);
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            System.out.println("失敗: セッション変数の中身は" + session);
            response.sendRedirect(request.getContextPath() + "/login");
        }
	}

}
