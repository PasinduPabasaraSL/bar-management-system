package com.example.barmanagementsystempro.dto;

public class UpdateDto {
    private int itemId;
    private String itemName;
    private String category;
    private int qty;
    private double price;

    public UpdateDto(int itemId, String itemName, String category, int qty, double price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.qty = qty;
        this.price = price;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
