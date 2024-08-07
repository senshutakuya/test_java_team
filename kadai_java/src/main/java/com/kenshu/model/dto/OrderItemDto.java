package com.kenshu.model.dto;
import java.util.ArrayList;
import java.util.List;

import com.kenshu.model.bean.OrderItem;

public class OrderItemDto {
	private List<OrderItem> orderItemList;

    public OrderItemDto() {
        this.orderItemList = new ArrayList<>();
    }

    public void add(OrderItem orderItem) {
        orderItemList.add(orderItem);
    }

    public OrderItem get(int i) {
        return orderItemList.get(i);
    }

    public int size() {
        return orderItemList.size();
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

}



