package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.ReOrderLevelDto;
import com.example.barmanagementsystempro.model.ItemModel;
import com.example.barmanagementsystempro.tm.InventoryItemTM;
import com.mysql.cj.conf.IntegerProperty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class InventoryController {
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnChangeROL;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private Label categoriesLabel;

    @FXML
    private TableColumn<InventoryItemTM, String> categoryColumn;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private TableView<InventoryItemTM> inventoryTable;

    @FXML
    private TableColumn<InventoryItemTM, String> itemNameColumn;

    @FXML
    private Label lowStockLabel;

    @FXML
    private TableColumn<InventoryItemTM, Double> costPriceColumn;

    @FXML
    private TableColumn<InventoryItemTM, IntegerProperty> quantityColumn;

    @FXML
    private TableColumn<InventoryItemTM, Double> sellingPriceColumn;

    @FXML
    private TableColumn<InventoryItemTM, Double> reorderLevelColumn;

    @FXML
    private BorderPane root;

    @FXML
    private Label stockValueLabel;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private TextField txtSearchName;

    @FXML
    private Label lblNeedsAttention;

    @FXML
    private Label alcoholCountLabel;

    @FXML
    private Label barSuppliesCountLabel;

    @FXML
    private Label mixersCountLabel;

    @FXML
    public Label nonAlcoholCountLabel;

    ObservableList<InventoryItemTM> inventoryList;
    ObservableList<InventoryItemTM> filteredCategoryList;

    SceneManager sceneManager = new SceneManager();

    public void initialize() {
        inventoryList = javafx.collections.FXCollections.observableArrayList();

        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        costPriceColumn.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        reorderLevelColumn.setCellValueFactory(new PropertyValueFactory<>("reOrderLevel"));

        inventoryTable.getColumns().forEach(column -> column.setResizable(false));
        inventoryTable.getColumns().forEach(column -> column.setReorderable(false));

        ArrayList<InventoryItemTM> items = ItemModel.loadAllInventoryItems();
        inventoryList.addAll(items);
        inventoryTable.setItems(inventoryList);

        filteredCategoryList = FXCollections.observableArrayList();

        cmbCategory.getItems().addAll("All", "Alcohol", "Mixers", "Bar Supplies", "Non-Alcohol");
        cmbCategory.getSelectionModel().selectFirst();

        cmbCategory.valueProperty().addListener((observable, oldValue, newValue) -> filterTableByCategory(newValue));

        updateInventoryStats();
    }

    private void updateInventoryStats() {
        updateTotalItemsLabel();
        checkLowStock();
        startStockCheck();
        updateStockValueLabel();
        trackCategoryCounts();
    }

    private void trackCategoryCounts() {
        int alcoholCount = 0;
        int mixersCount = 0;
        int barSuppliesCount = 0;
        int nonAlcoholCount = 0;

        // Iterate over the inventory and count the items by category
        for (InventoryItemTM item : inventoryList) {
            switch (item.getCategory().toLowerCase()) {
                case "alcohol":
                    alcoholCount++;
                    break;
                case "mixers":
                    mixersCount++;
                    break;
                case "bar supplies":
                    barSuppliesCount++;
                    break;
                case "non-alcohol":
                    nonAlcoholCount++;
                    break;
            }
        }

        // Update the labels with the counts
        alcoholCountLabel.setText("Alcohol: " + alcoholCount);
        mixersCountLabel.setText("Mixers: " + mixersCount);
        barSuppliesCountLabel.setText("Bar Supplies: " + barSuppliesCount);
        nonAlcoholCountLabel.setText("Non-Alcohol: " + nonAlcoholCount);
    }

    private void startStockCheck() {
        Timeline stockCheckTimeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> {
            checkLowStock();
            updateStockValueLabel();
        }));
        stockCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        stockCheckTimeline.play();
    }

    private void checkLowStock() {
        StringBuilder lowStockItems = new StringBuilder();
        for (InventoryItemTM item : inventoryList) {
            if (item.getQuantity() <= item.getReOrderLevel()) {
                lowStockItems.append(item.getItemName()).append(", ");
            }
        }

        if (!lowStockItems.isEmpty()) {
            lowStockLabel.setText(lowStockItems.substring(0, lowStockItems.length() - 2));
            lowStockLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            lowStockLabel.setText("Stock levels are adequate.");
            lblNeedsAttention.setVisible(false);
            lowStockLabel.setStyle("-fx-text-fill: green; -fx-font-weight: normal;");
        }
    }

    private double calculateTotalStockValue() {
        double totalValue = 0.0;
        for (InventoryItemTM item : inventoryList) {
            // Use get() to retrieve the primitive double value
            totalValue += item.getCostPrice() * item.getQuantity();
        }
        return totalValue;
    }

    private void updateStockValueLabel() {
        double totalStockValue = calculateTotalStockValue();
        stockValueLabel.setText("Rs. " + totalStockValue);
    }

    private void updateTotalItemsLabel() {
        int totalCount = calculateTotalItemsCount();
        totalItemsLabel.setText("" + totalCount);
    }

    private int calculateTotalItemsCount() {
        return inventoryList.stream().mapToInt(InventoryItemTM::getQuantity).sum();
    }

    public void setSecurity(String role){
        if ("Staff".equals(role)) {
            btnChangeROL.setDisable(true);
        }
    }

    @FXML
    void addItem(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/com/example/barmanagementsystempro/view/add-item-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());
        newStage.setScene(newScene);
        newStage.setTitle("Add Item");
        newStage.centerOnScreen();
        newStage.show();
    }

    @FXML
    void changeReorderLevel(ActionEvent event) {
        InventoryItemTM selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            int itemId = selectedItem.getId();
            String itemName = selectedItem.getItemName();
            int reOrderLevel = selectedItem.getReOrderLevel();

            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/com/example/barmanagementsystempro/view/calculate-reorderlevel-view.fxml"));

            try {
                Parent root = fxmlLoader.load();
                CalculateReorderlevelController controller = fxmlLoader.getController();
                controller.initialize(new ReOrderLevelDto(itemId, reOrderLevel, itemName));

                Stage newStage = new Stage();
                newStage.setTitle("Change Reorder Level");
                Scene scene = new Scene(root);
                newStage.setScene(scene);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(this.root.getScene().getWindow());
                newStage.centerOnScreen();
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            sceneManager.showAlert("Error", "Please select an item");
        }
    }


    @FXML
    void deleteItem(ActionEvent event) throws SQLException, ClassNotFoundException {
        InventoryItemTM selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Optional<ButtonType> result = sceneManager.confirmAlert("Confirmation", "Are you sure you want to delete this item?");

            if (result.isPresent() && result.get() == ButtonType.OK) {

                int itemId = selectedItem.getId();
                boolean isDeleted = ItemModel.deleteItem(itemId);

                if (isDeleted) {
                    inventoryTable.getItems().remove(selectedItem);
                    HomeController.cachedTotalItems -= 1;
                    sceneManager.showAlert("Success", "Item deleted successfully");
                } else {
                    sceneManager.showAlert("Error", "Item could not be deleted");
                }
            }
        } else {
            sceneManager.showAlert("Error", "Please select a item");
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchItem = txtSearchName.getText().toLowerCase();

        if (searchItem.isEmpty()) {
            if (filteredCategoryList.isEmpty()) {
                inventoryTable.setItems(inventoryList);
            } else {
                inventoryTable.setItems(filteredCategoryList);
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
                txtSearchName.clear();
            } else {
                inventoryTable.setItems(filteredList);
            }
        } else {
            inventoryTable.setItems(filteredList);
        }

    }

    private void filterTableByCategory(String category) {
        filteredCategoryList.clear();
        if (category.equals("All")) {
            inventoryTable.setItems(inventoryList);
            txtSearchName.clear();
            return;
        }

        for (InventoryItemTM items : inventoryList) {
            if (items.getCategory().toLowerCase().contains(category.toLowerCase())) {
                filteredCategoryList.add(items);
            }
        }

        inventoryTable.setItems(filteredCategoryList);
        txtSearchName.clear();

        if (filteredCategoryList.isEmpty()) {
            sceneManager.showAlert("No Results", "No items match your selected category.");
        }
    }

    @FXML
    void updateItem(ActionEvent event) {
        InventoryItemTM selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            try {
                // Create a new stage (window) for the new scene
                Stage newStage = new Stage();

                // Load the FXML for the update scene
                FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/com/example/barmanagementsystempro/view/update-view.fxml"));
                Parent root = fxmlLoader.load();

                // Get the controller for the new scene and initialize with selected item
                UpdateController updateController = fxmlLoader.getController();
                updateController.initializeWithItem(selectedItem);

                // Create a new scene and set it in the new stage
                Scene scene = new Scene(root);
                newStage.setScene(scene);

                // Set properties for the new stage (optional)
                newStage.setTitle("Update Item");
                newStage.centerOnScreen();

                // Show the new stage (window)
                newStage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                sceneManager.showAlert("Error", "Failed to load update view");
            }
        } else {
            sceneManager.showAlert("Error", "Please select an item");
        }
    }


}
