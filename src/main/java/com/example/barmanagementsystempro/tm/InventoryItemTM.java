package com.example.barmanagementsystempro.tm;

import javafx.beans.property.*;

public class InventoryItemTM {
    private final IntegerProperty id;
    private final StringProperty itemName;
    private final StringProperty category;
    private final IntegerProperty quantity;
    private final DoubleProperty costPrice;
    private final DoubleProperty sellingPrice;
    private final IntegerProperty reOrderLevel;

    // Constructor
    public InventoryItemTM(int id, String itemName, String category, int quantity, double costPrice, double sellingPrice, int reOrderLevel) {
        this.id = new SimpleIntegerProperty(id);
        this.itemName = new SimpleStringProperty(itemName);
        this.category = new SimpleStringProperty(category);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.costPrice = new SimpleDoubleProperty(costPrice);
        this.sellingPrice = new SimpleDoubleProperty(sellingPrice);
        this.reOrderLevel = new SimpleIntegerProperty(reOrderLevel);
    }

    // Getter methods for JavaFX properties
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public DoubleProperty costPriceProperty() {
        return costPrice;
    }

    public DoubleProperty sellingPriceProperty() {
        return sellingPrice;
    }

    public IntegerProperty reOrderLevelProperty() {
        return reOrderLevel;
    }

    // Getter methods for values
    public int getId() {
        return id.get();
    }

    public String getItemName() {
        return itemName.get();
    }

    public String getCategory() {
        return category.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public double getCostPrice() {
        return costPrice.get();
    }

    public double getSellingPrice() {
        return sellingPrice.get();
    }

    public int getReOrderLevel() {
        return reOrderLevel.get();
    }

    // Setter methods
    public void setId(int id) {
        this.id.set(id);
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setCostPrice(double costPrice) {
        this.costPrice.set(costPrice);
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice.set(sellingPrice);
    }

    public void setReOrderLevel(int reOrderLevel) {
        this.reOrderLevel.set(reOrderLevel);
    }
}
