package com.example.barmanagementsystempro.tm;

public class OrderTM {
    private String orderId;
    private int tableNumber;
    private String items;
    private double total;
    private String status;
    private String time;

    public OrderTM(String orderId, int tableNumber, String items, double total, String status, String time) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.items = items;
        this.total = total;
        this.status = status;
        this.time = time;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}

