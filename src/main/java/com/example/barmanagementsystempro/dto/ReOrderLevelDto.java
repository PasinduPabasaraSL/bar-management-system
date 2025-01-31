package com.example.barmanagementsystempro.dto;

public class ReOrderLevelDto {
    private int itemId;
    private int reOrderLevel;
    private String itemName;

    public ReOrderLevelDto(int itemId, int reOrderLevel) {
        this.itemId = itemId;
        this.reOrderLevel = reOrderLevel;
    }

    public ReOrderLevelDto(int itemId, int reOrderLevel, String itemName) {
        this.itemId = itemId;
        this.reOrderLevel = reOrderLevel;
        this.setItemName(itemName);

    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getReOrderLevel() {
        return reOrderLevel;
    }

    public void setReOrderLevel(int reOrderLevel) {
        this.reOrderLevel = reOrderLevel;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
