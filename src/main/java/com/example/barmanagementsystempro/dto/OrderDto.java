package com.example.barmanagementsystempro.dto;

import java.util.ArrayList;

public class OrderDto {
    private String orderId;
    private int tableNo;
    private double subTotal;
    private ArrayList<OrderDetailsDto> orderDetails;

    public OrderDto(String orderId, int tableNo, double subTotal, ArrayList<OrderDetailsDto> orderDetails) {
        this.orderId = orderId;
        this.tableNo = tableNo;
        this.subTotal = subTotal;
        this.orderDetails = orderDetails;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public ArrayList<OrderDetailsDto> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetailsDto> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
