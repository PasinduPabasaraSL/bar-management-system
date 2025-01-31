package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class InvoiceController {

    @FXML
    private TextArea invoiceTextArea;

    SceneManager sceneManager = new SceneManager();

    public void setInvoiceData(String invoiceData) {
        if (invoiceTextArea != null) {
            //System.out.println(invoiceData);
            invoiceTextArea.setText(invoiceData);
        } else {
            System.out.println("invoiceTextArea is null!");
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        HomeController homeController = sceneManager.handleBackButton(event,"/com/example/barmanagementsystempro/view/home-view.fxml");
        homeController.handlePOS(event);
    }

    @FXML
    void downloadPdf(ActionEvent event) {

    }

    @FXML
    void printBill(ActionEvent event) {

    }

}