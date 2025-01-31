package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.SupplierDto;
import com.example.barmanagementsystempro.model.SupplierModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AddSupplierController {

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtItem;

    @FXML
    private TextField txtSupplierID;

    SceneManager sceneManager = new SceneManager();

    @FXML
    void back(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void handleSave(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (!txtSupplierID.getText().isEmpty() || !txtAddress.getText().isEmpty() || !txtContact.getText().isEmpty() || !txtEmail.getText().isEmpty() || !txtName.getText().isEmpty()){
            String id = txtSupplierID.getText();
            String address = txtAddress.getText();
            String contact = txtContact.getText();
            String email = txtEmail.getText();
            String name = txtName.getText();
            String itemsInput = txtItem.getText(); // Assume this is the text field for items

            // Split itemsInput into a list based on commas
            List<String> itemsList = Arrays.asList(itemsInput.split(","));

            // Validate items list
            if (itemsList.isEmpty() || itemsList.stream().anyMatch(String::isBlank)) {
                System.out.println("Invalid items input!");
                return;
            }

            if (id.length() == 6) {
                String modifiedId = id.substring(0, 3).toUpperCase() + id.substring(3);
                boolean isAdded = SupplierModel.addSupplier(new SupplierDto(modifiedId, name, contact, email, address,itemsList));

                if (isAdded){
                    sceneManager.showAlert("Success","Supplier added successfully");
                    clearFields();
                } else {
                    sceneManager.showAlert("Error","Supplier already exists");
                    clearFields();
                }
            } else {
                sceneManager.showAlert("Warning","Supplier ID must be 6 characters");
            }

        } else {
            sceneManager.showAlert("Error","Please enter all values");
        }
    }

    public void clearFields() {
        txtAddress.clear();
        txtContact.clear();
        txtEmail.clear();
        txtName.clear();
        txtSupplierID.clear();
    }
}