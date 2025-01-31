package com.example.barmanagementsystempro.tm;

import javafx.beans.property.*;

public class CartItemTM {
    private final IntegerProperty id;
    private final StringProperty itemName;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final DoubleProperty total;

    public CartItemTM(int id, String itemName, int quantity, double price, double total) {
        this.id = new SimpleIntegerProperty(id);
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.total = new SimpleDoubleProperty(total);
    }

    public IntegerProperty getId() {
        return id;
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public IntegerProperty getQuantity() {
        return quantity;
    }

    public DoubleProperty getPrice() {
        return price;
    }

    public DoubleProperty getTotal() {
        return total;
    }

//    Setter Methods
    public void setId(int id) {
        this.id.set(id);
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

}
