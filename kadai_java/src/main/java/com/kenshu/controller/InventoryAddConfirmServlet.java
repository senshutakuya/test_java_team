package com.kenshu.controller;

import java.io.IOException;

import com.kenshu.service.StockService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class InventoryAddConfirmServlet
 */
@WebServlet("/inventory/add/confirm")
public class InventoryAddConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StockService stockService = new StockService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        System.out.println("メソッドgetです！セッション変数の中身は"+ session);

        if (session != null && session.getAttribute("user") != null) {
            try {
                System.out.println("デバッグリダイレクト");
                response.sendRedirect(request.getContextPath() + "/inventory/add");
            } catch(Exception e){
                System.out.println("デバッグです");
                System.out.println(e);
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        } else {
            System.out.println("失敗、セッション変数の中身は"+ session);
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        System.out.println("メソッドpostです！セッション変数の中身は"+ session);

        if (session != null && session.getAttribute("user") != null) {
            try {
//            	入力値を取得
            	// 入力値を取得
                String stockName = request.getParameter("stock_name");
                String stockPriceStr = request.getParameter("stock_price");
                String stockNumberStr = request.getParameter("stock_number");
//            	一つでもnullなら
            	// 入力値のチェック
                if (stockName == null || stockPriceStr == null || stockNumberStr == null ||
                    stockName.isEmpty() || stockPriceStr.isEmpty() || stockNumberStr.isEmpty()) {
                    request.setAttribute("error", "全ての項目を満たしてください");
                    request.getRequestDispatcher("/jsp/add_stock.jsp").forward(request, response);
                    return;
                }
//            	全部入力されていたなら
//            	商品名は文字列に変換
//            	在庫数と単価を数値に変換
                int stockPrice;
                int stockNumber;
                try {
                    stockPrice = Integer.parseInt(stockPriceStr);
                    stockNumber = Integer.parseInt(stockNumberStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "単価と在庫数は数値で入力してください");
                    request.getRequestDispatcher("/jsp/add_stock.jsp").forward(request, response);
                    return;
                }
//            	stockItemDtoのaddstockメソッドを呼び出す(引数に入力値を入れる)これによってデータベースに更新がかかる
                stockService.add(stockName, stockPrice, stockNumber);
//            	無事成功したなら
            	System.out.println("デバック、add_confirm.jspに移動");
                request.getRequestDispatcher("/jsp/add_confirm.jsp").forward(request, response);
            }catch(Exception e){
                System.out.println("デバッグです");
                System.out.println(e);
                request.setAttribute("error", "大変申し訳ございませんしばらくお待ちいただいた後やり直してください");
                request.getRequestDispatcher("/jsp/add_stock.jsp").forward(request, response);
            }
        } else {
            System.out.println("失敗、セッション変数の中身は"+ session);
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
