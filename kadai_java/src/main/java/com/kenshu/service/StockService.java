package com.kenshu.service;

import com.kenshu.dao.StockItemDao;
import com.kenshu.model.dto.StockItemDto;

public class StockService {
	private StockItemDao stockItemDao = new StockItemDao();

	public StockItemDto list() {
		// 在庫商品のリストを取得し、DTOとして返す
		return stockItemDao.getAll();
	}
	
	public StockItemDto add(String name, int price, int stock) {
		return stockItemDao.addStock(name, price, stock);
	}

}
