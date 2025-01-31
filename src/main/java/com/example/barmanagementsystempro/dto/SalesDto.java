package com.example.barmanagementsystempro.dto;

import com.example.barmanagementsystempro.tm.InvoiceTM;
import javafx.collections.ObservableList;

public class SalesDto {
    private String orderId;
    private ObservableList<InvoiceTM> invoiceDetails;

    public SalesDto(String orderId, ObservableList<InvoiceTM> invoiceDetails) {
        this.orderId = orderId;
        this.invoiceDetails = invoiceDetails;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ObservableList<InvoiceTM> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(ObservableList<InvoiceTM> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }
}
