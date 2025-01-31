package com.example.barmanagementsystempro.dto;

public class OrderDetailsDto {
    private int orderId;
    private int itemId;
    private int qty;
    private double price;

    public OrderDetailsDto(int orderId, int itemId, int qty, double price) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.qty = qty;
        this.price = price;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
