package com.example.barmanagementsystempro.dto;

public class ItemDto {
    private String itemName;
    private String category;
    private int qty;
    private double costPrice;
    private double sellingPrice;

    public ItemDto(String itemName, String category, int qty, double costPrice , double sellingPrice) {
        this.itemName = itemName;
        this.category = category;
        this.qty = qty;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double price) {
        this.costPrice = price;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
