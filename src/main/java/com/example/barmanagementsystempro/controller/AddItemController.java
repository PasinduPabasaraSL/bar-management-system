package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.ItemDto;
import com.example.barmanagementsystempro.model.ItemModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AddItemController {

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtItemName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtProfitPercentage;


    @FXML
    private AnchorPane root;

    SceneManager sceneManager = new SceneManager();

    @FXML
    private void handleAddItem() {
        if (!txtItemName.getText().isEmpty() && !txtPrice.getText().isEmpty() && !txtQty.getText().isEmpty() && !txtCategory.getText().isEmpty() && !txtProfitPercentage.getText().isEmpty()) {
            try {
                String itemName = txtItemName.getText();
                String category = txtCategory.getText();
                int qty = Integer.parseInt(txtQty.getText());
                double costPrice = Double.parseDouble(txtPrice.getText());
                double profitPercentage = Double.parseDouble(txtProfitPercentage.getText());
                double sellingPrice = costPrice + (costPrice * profitPercentage);

                boolean isAdded = ItemModel.addItem(new ItemDto(itemName, category, qty, costPrice, sellingPrice));

                if (isAdded) {
                    sceneManager.showAlert("Success", "Item added successfully");
                    clearForm();
                    HomeController.cachedTotalOrders += 1;
                } else {
                    sceneManager.showAlert("Error", "Item addition failed");
                }
            } catch (NumberFormatException e) {
                sceneManager.showAlert("Error", "Quantity and Price must be valid numbers");
                txtQty.clear();
                txtPrice.clear();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            sceneManager.showAlert("Error", "All fields are required");
        }
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void clearForm() {
        txtItemName.clear();
        txtCategory.clear();
        txtQty.clear();
        txtPrice.clear();
        txtProfitPercentage.clear();
    }

}
