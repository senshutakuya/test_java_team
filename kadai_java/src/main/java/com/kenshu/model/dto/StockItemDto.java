package com.kenshu.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.kenshu.model.bean.StockItem;

public class StockItemDto {
    private List<StockItem> stockItemList;

    public StockItemDto() {
        this.stockItemList = new ArrayList<>();
    }

    public void add(StockItem stockItem) {
        stockItemList.add(stockItem);
    }

    public StockItem get(int i) {
        return stockItemList.get(i);
    }

    public int size() {
        return stockItemList.size();
    }

    public List<StockItem> getStockItemList() {
        return stockItemList;
    }
}
