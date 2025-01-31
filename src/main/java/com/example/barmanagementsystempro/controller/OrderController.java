package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.OrderDetailsDto;
import com.example.barmanagementsystempro.dto.OrderDto;
import com.example.barmanagementsystempro.model.ItemModel;
import com.example.barmanagementsystempro.model.OrderModel;
import com.example.barmanagementsystempro.tm.CartItemTM;
import com.example.barmanagementsystempro.tm.InventoryItemTM;
import com.example.barmanagementsystempro.tm.OrderTM;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;


public class OrderController {
    @FXML
    private TableColumn<InventoryItemTM, Void> actionCol;

    @FXML
    private TableColumn<InventoryItemTM, String> categoryCol;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TableColumn<InventoryItemTM, String> itemNameCol;

    @FXML
    private TableView<InventoryItemTM> itemsTable;

    @FXML
    private TableColumn<CartItemTM, Void> orderActionCol;

    @FXML
    private VBox orderEntrySection;

    @FXML
    private TableColumn<CartItemTM, String> orderItemCol;

    @FXML
    private TableView<CartItemTM> orderItemsTable;

    @FXML
    private TableColumn<CartItemTM, String> orderPriceCol;

    @FXML
    private TableColumn<CartItemTM, String> orderQtyCol;

    @FXML
    private TableColumn<CartItemTM, String> orderTotalCol;

    @FXML
    private TableColumn<InventoryItemTM, String> sellingPriceCol;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<InventoryItemTM, String> stockCol;

    @FXML
    private ComboBox<String> tableComboBox;

    @FXML
    private Label totalAmount;

    @FXML
    private Button btnNewOrder;

    @FXML
    private TableView<OrderTM> ordersTable;

    @FXML
    private TableColumn<OrderTM, String> orderIdCol;

    @FXML
    private TableColumn<OrderTM, String> tableCol;

    @FXML
    private TableColumn<OrderTM, String> itemsCol;

    @FXML
    private TableColumn<OrderTM, Double> totalCol;

    @FXML
    private TableColumn<OrderTM, String> statusCol;

    @FXML
    private TableColumn<OrderTM, String> timeCol;

    @FXML
    private TableColumn<OrderTM, Void> changeOrderCol;

    @FXML
    private TableColumn<OrderTM, Void> completedActionCol;


    ObservableList<InventoryItemTM> inventoryList;
    ObservableList<InventoryItemTM> filteredCategoryList;
    private ArrayList<OrderDetailsDto> orderDetailsDtos;

    SceneManager sceneManager = new SceneManager();

    public void initialize() {
        inventoryList = FXCollections.observableArrayList();

        itemNameCol.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        stockCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asString());
        sellingPriceCol.setCellValueFactory(cellData -> cellData.getValue().sellingPriceProperty().asString());

        itemsTable.getColumns().forEach(column -> column.setResizable(false));
        itemsTable.getColumns().forEach(column -> column.setReorderable(false));

        ArrayList<InventoryItemTM> items = ItemModel.loadAllInventoryItems();
        inventoryList.addAll(items);
        itemsTable.setItems(inventoryList);

        orderItemCol.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        orderPriceCol.setCellValueFactory(cellData -> cellData.getValue().getPrice().asString());
        orderQtyCol.setCellValueFactory(cellData -> cellData.getValue().getQuantity().asString());
        orderTotalCol.setCellValueFactory(cellData -> cellData.getValue().getTotal().asString());


        orderItemsTable.getColumns().forEach(column -> column.setResizable(false));
        orderItemsTable.getColumns().forEach(column -> column.setReorderable(false));

        actionCol.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Add");

            {
                btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #4CAF50, #81C784);"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 10;"
                        + "-fx-padding: 5 15;"
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 0, 0);");
                btn.setOnAction(event -> {
                    InventoryItemTM inventoryItem = getTableRow().getItem();
                    if (inventoryItem != null) {
                        addToCart(inventoryItem);
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        orderActionCol.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Remove");

            {
                btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #FF6347, #FF7F50);"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 10;"
                        + "-fx-padding: 5 15;"
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 0, 0);");
                btn.setOnAction(event -> {
                    CartItemTM cartItem = getTableRow().getItem();
                    if (cartItem != null) {
                        removeOrDecreaseQuantityFromCart(cartItem);
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }

        });

        filteredCategoryList = FXCollections.observableArrayList();

        categoryComboBox.getSelectionModel().select("All");
        tableComboBox.getSelectionModel().select("Table No 1");

        categoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filterTableByCategory(newValue));

        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        itemsCol.setCellValueFactory(new PropertyValueFactory<>("items"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        changeOrderCol.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Change");

            {
                btn.setStyle(
                        "-fx-background-color: #4CAF50; " +           // Green background
                                "-fx-text-fill: white; " +                      // White text
                                "-fx-font-size: 14px; " +                        // Font size
                                "-fx-padding: 5px 15px; " +                     // Padding
                                "-fx-border-radius: 5px; " +                     // Rounded corners
                                "-fx-background-radius: 5px; " +                 // Rounded background
                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 2); " +  // Shadow effect
                                "-fx-transition: all 0.3s ease;"                 // Transition effect
                );

                btn.setOnAction(event -> {
                    OrderTM order = getTableRow().getItem();
                    if (order != null) {
                        // Add your logic here
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        completedActionCol.setCellFactory(column -> new TableCell<>() {
            private final Button btn = new Button("Complete");

            {
                btn.setStyle(
                        "-fx-background-color: #4CAF50; " +           // Green background
                                "-fx-text-fill: white; " +                      // White text
                                "-fx-font-size: 14px; " +                        // Font size
                                "-fx-padding: 5px 15px; " +                     // Padding
                                "-fx-border-radius: 5px; " +                     // Rounded corners
                                "-fx-background-radius: 5px; " +                 // Rounded background
                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 2); " +  // Shadow effect
                                "-fx-transition: all 0.3s ease;"                 // Transition effect
                );

                btn.setOnAction(event -> {
                    OrderTM order = getTableRow().getItem();
                    if (order != null) {
                        // Change order status to "completed"
                        order.setStatus("completed");

                        // Update the order in the database
                        try {
                            OrderModel.updateOrderStatus(order.getOrderId());
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        // Refresh the table or update the row in the table if needed
                        getTableView().refresh();  // Optional: Refresh the table to reflect the change
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        loadOrdersIntoTable();

        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setStyle("");  // Reset styles for empty or null items
                    setText("");   // Clear the text if empty
                } else {
                    setText(status);  // Set the status text
                    switch (status) {
                        case "completed":
                            setStyle("-fx-text-fill: #28a745;"); // Green text
                            break;
                        case "pending":
                            setStyle("-fx-text-fill: #17a2b8;"); // Blue text
                            break;
                        case "cancelled":
                            setStyle("-fx-text-fill: #dc3545;"); // Red text
                            break;
                        default:
                            setStyle("-fx-text-fill: #6c757d;"); // Gray text
                            break;
                    }
                }
            }
        });


    }

    private void loadOrdersIntoTable() {
        try {
            ObservableList<OrderTM> orders = OrderModel.loadAllOrders();
            ordersTable.setItems(orders);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void filterTableByCategory(String category) {
        filteredCategoryList.clear();
        if (category.equals("All")) {
            itemsTable.setItems(inventoryList);
            searchField.clear();
            return;
        }

        for (InventoryItemTM items : inventoryList) {
            if (items.getCategory().toLowerCase().contains(category.toLowerCase())) {
                filteredCategoryList.add(items);
            }
        }

        itemsTable.setItems(filteredCategoryList);
        searchField.clear();

        if (filteredCategoryList.isEmpty()) {
            sceneManager.showAlert("No Results", "No items match your selected category.");
        }
    }

    private void removeOrDecreaseQuantityFromCart(CartItemTM cartItem) {
        int currentQuantity = cartItem.getQuantity().get();
        if (currentQuantity > 1) {
            cartItem.setQuantity(currentQuantity - 1);
            cartItem.setTotal(cartItem.getQuantity().get() * cartItem.getPrice().get());
        } else {
            orderItemsTable.getItems().remove(cartItem);
        }

        updateTotalAmount();
    }

    private void updateTotalAmount() {
        double totAmount = 0.00;
        for (CartItemTM cartItem : orderItemsTable.getItems()) {
            totAmount += cartItem.getTotal().get();
        }
        totalAmount.setText("" + totAmount);
    }

    private void addToCart(InventoryItemTM inventoryItem) {
        boolean itemExistsInCart = false;

        // Initialize the orderDetailsDtos list if not already done
        if (orderDetailsDtos == null) {
            orderDetailsDtos = new ArrayList<>();
        }

        // Check if the item already exists in the cart
        for (CartItemTM cartItem : orderItemsTable.getItems()) {
            if (cartItem.getId().get() == inventoryItem.getId()) {
                // Update quantity and total in the cart
                cartItem.setQuantity(cartItem.getQuantity().get() + 1);
                cartItem.setTotal(cartItem.getQuantity().get() * inventoryItem.getSellingPrice());

                // Update the corresponding OrderDetailsDto
                for (OrderDetailsDto orderDetail : orderDetailsDtos) {
                    if (orderDetail.getItemId() == inventoryItem.getId()) {
                        orderDetail.setQty(orderDetail.getQty() + 1);
                        orderDetail.setPrice(cartItem.getTotal().get());
                        break;
                    }
                }

                itemExistsInCart = true;
                break;
            }
        }

        // If the item is not already in the cart, add a new entry
        if (!itemExistsInCart) {
            // Add item to the cart
            CartItemTM cartItem = new CartItemTM(
                    inventoryItem.getId(),
                    inventoryItem.getItemName(),
                    1,
                    inventoryItem.getSellingPrice(),
                    inventoryItem.getSellingPrice()

            );
            orderItemsTable.getItems().add(cartItem);

            // Create a new OrderDetailsDto and add it to the list
            OrderDetailsDto orderDetail = new OrderDetailsDto(
                    0, // Placeholder for orderId, can be set later
                    inventoryItem.getId(),
                    1,
                    inventoryItem.getSellingPrice()
            );
            orderDetailsDtos.add(orderDetail);
        }

        // Update total amount displayed
        updateTotalAmount();
    }

    @FXML
    void handleCancelOrder(ActionEvent event) {
        clearOrderForm();
    }

    @FXML
    void handleNewOrder(ActionEvent event) {
        orderEntrySection.setVisible(true);
        btnNewOrder.setVisible(false);
    }

    @FXML
    void handlePlaceOrder(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (orderItemsTable.getItems().isEmpty()) {
            sceneManager.showAlert("Empty Order", "No items in the cart to place an order.");
            return;
        }

        Optional<ButtonType> result = sceneManager.confirmAlert("Confirmation", "Are you sure you want to place the order?");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String orderId = generateOrderId();
            int tableNo = tableComboBox.getSelectionModel().getSelectedIndex() + 1;
            double subTotal = Double.parseDouble(totalAmount.getText());

            boolean status = OrderModel.placeOrder(new OrderDto(orderId, tableNo, subTotal, orderDetailsDtos));

            if (status) {
                orderDetailsDtos.clear();
                orderItemsTable.getItems().clear();
                totalAmount.setText("0.00");
                categoryComboBox.getSelectionModel().select("All");
                tableComboBox.getSelectionModel().select("Table No 1");
                loadOrdersIntoTable();
                HomeController.cachedTotalOrders += 1;
                Platform.runLater(() -> {
                    HomeController.getInstance().labelUpdates();
                });

                sceneManager.showAlert("success", "Order placed successfully.");
            } else {
                sceneManager.showAlert("error", "Failed to place the order. Please try again.");
            }

        }

    }

    private String generateOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 31);
    }


    private void clearOrderForm() {
        orderEntrySection.setVisible(false);
        btnNewOrder.setVisible(true);
        orderItemsTable.getItems().clear();
        itemsTable.setItems(inventoryList);
        totalAmount.setText("0.00");
        categoryComboBox.getSelectionModel().select("All");
        tableComboBox.getSelectionModel().select("Table No 1");
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchItem = searchField.getText().toLowerCase();

        if (searchItem.isEmpty()) {
            if (filteredCategoryList.isEmpty()) {
                itemsTable.setItems(inventoryList);
            } else {
                itemsTable.setItems(filteredCategoryList);
            }

            return;
        }

        ObservableList<InventoryItemTM> filteredList = FXCollections.observableArrayList();

        for (InventoryItemTM item : filteredCategoryList) {
            if (item.getItemName().toLowerCase().equals(searchItem)) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            for (InventoryItemTM item : inventoryList) {
                if (item.getItemName().toLowerCase().equals(searchItem)) {
                    filteredList.add(item);
                }
            }
            if (filteredList.isEmpty()) {
                sceneManager.showAlert("No Results", "No items match your search category");
                searchField.clear();
            } else {
                itemsTable.setItems(filteredList);
            }
        } else {
            itemsTable.setItems(filteredList);
        }
    }

}