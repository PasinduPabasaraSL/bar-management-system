package com.example.barmanagementsystempro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class InventoryReportController {
    @FXML
    private PieChart categoryChart;

    @FXML
    private TableColumn<?, ?> categoryCol;

    @FXML
    private TableView<?> inventoryTable;

    @FXML
    private TableColumn<?, ?> itemNameCol;

    @FXML
    private TableColumn<?, ?> lastUpdatedCol;

    @FXML
    private Label lowStockLabel;

    @FXML
    private TableColumn<?, ?> quantityCol;

    @FXML
    private LineChart<?, ?> stockTrendChart;

    @FXML
    private ComboBox<?> timeRangeCombo;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private TableColumn<?, ?> totalValueCol;

    @FXML
    private Label totalValueLabel;

    @FXML
    private Label turnoverLabel;

    @FXML
    private TableColumn<?, ?> unitPriceCol;

    @FXML
    void handleExport(ActionEvent event) {


    }
}
