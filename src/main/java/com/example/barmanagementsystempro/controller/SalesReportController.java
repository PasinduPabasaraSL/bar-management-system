package com.example.barmanagementsystempro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;

public class SalesReportController {
    @FXML
    private Label avgOrderLabel;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private DatePicker endDate;

    @FXML
    private TableColumn<?, ?> itemsCol;

    @FXML
    private Label netProfitLabel;

    @FXML
    private Label orderGrowthLabel;

    @FXML
    private TableColumn<?, ?> orderIdCol;

    @FXML
    private TableColumn<?, ?> paymentMethodCol;

    @FXML
    private TableColumn<?, ?> profitCol;

    @FXML
    private Label profitMarginLabel;

    @FXML
    private TableColumn<?, ?> quantityCol;

    @FXML
    private TableColumn<?, ?> revenueCol;

    @FXML
    private Label revenueGrowthLabel;

    @FXML
    private TableView<?> salesTable;

    @FXML
    private LineChart<?, ?> salesTrendChart;

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker startDate;

    @FXML
    private BarChart<?, ?> topProductsChart;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    void handleExport(ActionEvent event) {

    }

    @FXML
    void handleGenerateReport(ActionEvent event) {

    }
}
