package com.kenshu.controller;

import java.io.IOException;
import java.util.List;

import com.kenshu.model.bean.StockItem;
import com.kenshu.model.dto.StockItemDto;
import com.kenshu.service.StockService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class InventoryServlet
 */
//在庫管理者のホーム画面
@WebServlet("/inventory_home")
public class InventoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StockService stockService = new StockService();
       
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // セッションによるユーザの認可処理
        HttpSession session = request.getSession(false);
        System.out.println("セッション変数の中身は"+ session);
        
//        もしセッションに値が入っていて且つuserという値が空ではなく入っていたなら
        if (session != null && session.getAttribute("user") != null) {
        	try {
        		StockItemDto itemList = stockService.list();
        		request.setAttribute("itemList", itemList);
        		List<StockItem> items = itemList.getStockItemList();
                for (StockItem item : items) {
                    System.out.println(item.getId());
                    System.out.println(item.getName());
                    System.out.println(item.getPrice());
                    System.out.println(item.getStock());
                }
        		request.getRequestDispatcher("/jsp/stock_management.jsp").forward(request, response);
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
    	// セッションによるユーザの認可処理
        HttpSession session = request.getSession(false);
        System.out.println("postセッション変数の中身は"+ session);
        System.out.println("postセッションアトリビュートuserは"+ session.getAttribute("user"));
        if (session != null && session.getAttribute("user") != null) {
            request.getRequestDispatcher("/jsp/stock_management.jsp").forward(request, response);
        } else {
        	response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
