package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.SalesDto;
import com.example.barmanagementsystempro.model.OrderModel;
import com.example.barmanagementsystempro.model.SalesModel;
import com.example.barmanagementsystempro.tm.InvoiceTM;
import com.example.barmanagementsystempro.tm.OrderTM;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class POSController {
    @FXML
    private TableView<OrderTM> ordersTable;

    @FXML
    private TableColumn<OrderTM, String> itemsCol;

    @FXML
    private TableColumn<OrderTM, String> orderIdCol;

    @FXML
    private TableColumn<OrderTM, String> TimeCol;

    @FXML
    private TableColumn<OrderTM, String> tableCol;

    @FXML
    private AnchorPane root;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private TableColumn<OrderTM, String> totalCol;

    @FXML
    private TextField txtSearch;

    ObservableList<OrderTM> orders;

    SceneManager sceneManager = new SceneManager();

    public void initialize() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        itemsCol.setCellValueFactory(new PropertyValueFactory<>("items"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        TimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        ordersTable.getColumns().forEach(column -> column.setResizable(false));
        ordersTable.getColumns().forEach(column -> column.setReorderable(false));

        loadOrdersIntoTable();

        // Add listener to handle row click
        ordersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                OrderTM selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    double totalAmount = selectedOrder.getTotal();
                    subtotalLabel.setText(String.format("%.2f", totalAmount));
                }
            }
        });
    }

    private void loadOrdersIntoTable() {
        try {
            orders = OrderModel.loadUnpaidOrders();
            ordersTable.setItems(orders);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void handleCompleteSale(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        OrderTM selectedOrder = ordersTable.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            Optional<ButtonType> result = sceneManager.confirmAlert("Confirmation", "Are you sure you want to complete the sale?");

            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<InvoiceTM> invoiceDetails = OrderModel.getInvoiceDetails(selectedOrder.getOrderId());

                boolean isCompleted = SalesModel.completeSale(new SalesDto(selectedOrder.getOrderId(),invoiceDetails));

                if(isCompleted){
                    sceneManager.showAlert("Success", "Order completed successfully");

                    // Generate the invoice for the selected order
                    String invoiceData = generateInvoiceData(selectedOrder);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/invoice-view.fxml"));
                    Parent root = loader.load();
                    InvoiceController controller = loader.getController();
                    controller.setInvoiceData(invoiceData);

                    Stage stage = (Stage) ordersTable.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.centerOnScreen();
                    stage.show();
                }



            }
        } else {
            sceneManager.showAlert("Error", "Please select an order to complete the sale.");
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchText = txtSearch.getText().trim();

        try {
            if (searchText.isEmpty()) {
                ordersTable.setItems(orders);
                return;
            }

            int searchTableNumber = Integer.parseInt(searchText);

            FilteredList<OrderTM> filteredOrders = new FilteredList<>(orders, order -> order.getTableNumber() == searchTableNumber);

            ordersTable.setItems(filteredOrders);

        } catch (NumberFormatException e) {
            sceneManager.showAlert("Error", "Please enter a valid table number");
            ordersTable.setItems(orders);
        }
    }

    private String generateInvoiceData(OrderTM selectedOrder) throws SQLException, ClassNotFoundException {
        StringBuilder invoiceBuilder = new StringBuilder();

        // Header
        invoiceBuilder.append("Paradise Bar Payment Receipt\n")
                .append("--------------------------------------")
                .append("\n")
                .append("Order ID: ").append(selectedOrder.getOrderId()).append("\n")
                .append("Table No: ").append(selectedOrder.getTableNumber()).append("\n")
                .append("Date: ").append(java.time.LocalDate.now()).append("\n\n");

        // Table Header
        invoiceBuilder.append(String.format("%-20s %-10s %-15s\n", "Item", "Quantity", "Price"))
                .append("-----------------------------------------------\n");

        // Calculate total amount
        double totalAmount = 0.0;

        ObservableList<InvoiceTM> invoiceDetails = OrderModel.getInvoiceDetails(selectedOrder.getOrderId());

        // Loop through the items associated with this order and append to the invoice
        for (InvoiceTM item : invoiceDetails) {
            String itemName = item.getItemName();
            int quantity = item.getQuantity();
            double price = item.getPrice();
            double total = price * quantity;

            // Append item data with proper formatting
            invoiceBuilder.append(String.format("%-20s %-10d %-15.2f\n", itemName, quantity, total));
            totalAmount += total;
        }

        // Tax and total calculation
        double tax = totalAmount * 0.10; // 10% tax
        double grandTotal = totalAmount + tax;

        // Footer
        invoiceBuilder.append("-----------------------------------------------\n")
                .append(String.format("%-30s %.2f\n", "Subtotal:", totalAmount))
                .append(String.format("%-30s %.2f\n", "Tax (10%):", tax))
                .append(String.format("%-30s %.2f\n", "Total:", grandTotal));

        return invoiceBuilder.toString();
    }
}
