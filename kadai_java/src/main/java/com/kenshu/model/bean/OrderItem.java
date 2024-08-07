package com.kenshu.model.bean;

public class OrderItem {
	private int id;
    private String name;
    private int price;
    private int quantity;
	private int stock;

    
    
    public OrderItem(int id, String name, int price, int quantity, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.stock = stock;
    }
    
    
	    public int getId() {
	        return id;
	    }
	    
	    public void setId(int id) {
	        this.id = id;
	    }
	
	    public String getName() {
	        return name;
	    }
	
	    public void setName(String name) {
	        this.name = name;
	    }
	
	    public int getPrice() {
	        return price;
	    }
	
	    public void setPrice(int price) {
	        this.price = price;
	    }
	
	    public int getQuantity() {
	        return quantity;
	    }
	
	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }
	    
	    public int getStock() {
	        return stock;
	    }
	
	    public void setStock(int stock) {
	        this.stock = stock;
	    }

    
}










