package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.dao.StockItemDao;
import com.kenshu.model.bean.StockItem;
import com.kenshu.model.bean.UserBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class InventoryEditServlet
 */
@WebServlet("/inventory/edit")
public class InventoryEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StockItemDao stockItemDao = new StockItemDao();

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        System.out.println("メソッドgetです！セッション変数の中身は" + session);
        
        String id = request.getParameter("id");
        Boolean edit_flag = true;
        
        if (session != null && session.getAttribute("user") != null) {
            try {
                if (id != null) {
                	int item_id;
                	item_id = Integer.parseInt(id);
                	 // セッションからUserBeanオブジェクトを取得
                    UserBean user = (UserBean) session.getAttribute("user");
                    String userId = user.getLoginid();
                    
//                    権限がちゃんとあるかuser_stocksテーブルを使って確認
                    edit_flag = stockItemDao.userStock(userId, item_id);
                    System.out.println("編集権限は"+edit_flag);
//                  権限がないなら
                    if(!(edit_flag)) {
                    	
                    	System.out.println("デバッグ: 権限を持っていないので在庫管理のホーム画面にリダイレクト");
                    	System.out.println("編集権限は"+edit_flag);
                        response.sendRedirect(request.getContextPath() + "/inventory_home");
                        return; // ここでメソッドを終了させる
                    }
                    
//                    以降は権限があるものとして処理
                    
                    
                    
                    
                    // idに基づいて在庫情報を取得して、リクエスト属性に設定
                	 StockItem stockItem = StockItemDao.getItemById(item_id);
                	 
                	 // SQLが完了次第セッションにitem_idを保存
                     session.setAttribute("editItemId", item_id);
                	
                     session.setAttribute("stockItem", stockItem);
                    // 編集ページにフォワード
                    request.getRequestDispatcher("/jsp/InventoryEdit.jsp").forward(request, response);
                } else {
                    System.out.println("デバッグ: パラメータにIDが不足しているので在庫管理のホーム画面にリダイレクト");
                    response.sendRedirect(request.getContextPath() + "/inventory_home");
                }
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
        Boolean edit_flag = true;
        if (session != null && session.getAttribute("user") != null) {
            try {
                Integer itemId = (Integer) session.getAttribute("editItemId");
                if (itemId == null) {
                    session.setAttribute("error", "編集するアイテムのIDが見つかりませんでした。もう一度やり直してください。");
                    response.sendRedirect(request.getContextPath() + "/inventory/edit");
                    return;
                }
                
                // セッションからUserBeanオブジェクトを取得
                UserBean user = (UserBean) session.getAttribute("user");
                String userId = user.getLoginid();
                
//                権限がちゃんとあるかuser_stocksテーブルを使って確認
                edit_flag = stockItemDao.userStock(userId, itemId);
                System.out.println("編集権限は"+edit_flag);
//              権限がないなら
                if(!(edit_flag)) {
                	
                	System.out.println("デバッグ: 権限を持っていないので在庫管理のホーム画面にリダイレクト");
                	System.out.println("編集権限は"+edit_flag);
                    response.sendRedirect(request.getContextPath() + "/inventory_home");
                    return; // ここでメソッドを終了させる
                }
                
//                以降は権限があるものとして処理

                String name = request.getParameter("name");
                String priceStr = request.getParameter("price");
                String stockStr = request.getParameter("stock");

                int price = 0;
                int stock = 0;
                boolean valid = true;

                try {
                    price = Integer.parseInt(priceStr);
                } catch (NumberFormatException e) {
                    valid = false;
                    session.setAttribute("error", "価格は数値で入力してください。");
                }

                try {
                    stock = Integer.parseInt(stockStr);
                } catch (NumberFormatException e) {
                    valid = false;
                    session.setAttribute("error", "在庫数は数値で入力してください。");
                }

                if (valid) {
                    StockItemDao.updateInventory(itemId, name, price, stock);
                    response.sendRedirect(request.getContextPath() + "/inventory_home");
                } else {
                    StockItem stockItem = StockItemDao.getItemById(itemId);
                    session.setAttribute("stockItem", stockItem);
                    session.setAttribute("inputName", name);
                    session.setAttribute("inputPrice", priceStr);
                    session.setAttribute("inputStock", stockStr);
                    response.sendRedirect(request.getContextPath() + "/inventory/edit?id=" + itemId);
                }

            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("error", "エラーが発生しました。再試行してください。");
                response.sendRedirect(request.getContextPath() + "/jsp/error.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }


}
