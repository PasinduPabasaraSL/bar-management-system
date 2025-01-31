package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.dto.ItemDto;
import com.example.barmanagementsystempro.model.ItemModel;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class DashboardController {

    @FXML
    private PieChart categoryChart;

    @FXML
    private VBox contentArea;

    @FXML
    private BarChart<?, ?> salesChart;

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblLowStockItems;

    @FXML
    private Label lblAttention;

    public void initialize() {
        setLowStockItems();
    }

    public void setWelcomeMessage(String role) {
        if (lblWelcome == null) {
            System.out.println("lblWelcome is null!");
        } else {
            String userRole = HomeController.capitalizeFirstLetter(role);
            lblWelcome.setText("Welcome back,  " + userRole);
        }
    }

    public void setLowStockItems(){
        List<ItemDto> lowStockItems = null;
        try {
            lowStockItems = ItemModel.getLowStockItems();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        int lowStockItemCount = lowStockItems.size();
        lblLowStockItems.setText(String.valueOf(lowStockItemCount));

        lblAttention.setVisible(lowStockItemCount != 0);

    }

}
