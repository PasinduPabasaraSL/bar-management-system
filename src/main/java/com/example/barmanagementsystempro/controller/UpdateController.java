package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.UpdateDto;
import com.example.barmanagementsystempro.model.ItemModel;
import com.example.barmanagementsystempro.tm.InventoryItemTM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class UpdateController {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtItemName;
    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtPrice;

    private int itemId;

    SceneManager sceneManager = new SceneManager();

    public void initializeWithItem(InventoryItemTM item) {
        itemId = item.getId();
        txtItemName.setText(item.getItemName());
        txtCategory.setText(item.getCategory());
        txtQty.setText(String.valueOf(item.getQuantity()));
        txtPrice.setText(String.valueOf(item.getCostPrice()));
    }


    @FXML
    private void handleUpdate() throws SQLException, ClassNotFoundException {
        if (itemId != 0) {
            int id = itemId;
            String itemName = txtItemName.getText();
            String itemCategory = txtCategory.getText();
            int itemQty = Integer.parseInt(txtQty.getText());
            double itemPrice = Double.parseDouble(txtPrice.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to update this item?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isUpdated = ItemModel.updateItem(new UpdateDto(id, itemName, itemCategory, itemQty, itemPrice));

                if (isUpdated) {
                    sceneManager.showAlert("success","Item Update Successfully");
                    clearFields();
                } else {
                    sceneManager.showAlert("Error","Item Update Failed");
                }

            }

        }
    }

    @FXML
    public void back(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void clearFields() {
        txtItemName.clear();
        txtCategory.clear();
        txtQty.clear();
        txtPrice.clear();
    }
}