package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.model.SupplierModel;
import com.example.barmanagementsystempro.tm.SupplierTM;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class SupplierController {
    @FXML
    private Button btnAddSupplier;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnExport;

    @FXML
    private Button btnImport;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<SupplierTM,String> colAddress;

    @FXML
    private TableColumn<SupplierTM,String> colContact;

    @FXML
    private TableColumn<SupplierTM,String> colEmail;

    @FXML
    private TableColumn<SupplierTM,String> colName;

    @FXML
    private TableColumn<SupplierTM,String> colStatus;

    @FXML
    private Label inactiveSuppliersLabel;

    @FXML
    private Label lastUpdatedLabel;

    @FXML
    private Label newSuppliersLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<SupplierTM> supplierTable;

    @FXML
    private Label totalSuppliersLabel;

    ObservableList<SupplierTM> supplierList;

    public void initialize() throws SQLException, ClassNotFoundException {
        supplierList = javafx.collections.FXCollections.observableArrayList();

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        supplierTable.getColumns().forEach(column -> column.setResizable(false));
        supplierTable.getColumns().forEach(column -> column.setReorderable(false));

        ArrayList<SupplierTM> supplierTMS = SupplierModel.loadAllSuppliers();
        supplierList.addAll(supplierTMS);
        supplierTable.setItems(supplierList);

    }

    @FXML
    void handleAddSupplier(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/com/example/barmanagementsystempro/view/add-supplier-view.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());
        newStage.setScene(newScene);
        newStage.setTitle("Add Supplier");
        newStage.centerOnScreen();
        newStage.show();
    }

    @FXML
    void handleDeleteSupplier(ActionEvent event) {

    }

    @FXML
    void handleSearch(ActionEvent event) {
        javafx.collections.transformation.FilteredList<SupplierTM> filteredList =
                new javafx.collections.transformation.FilteredList<>(supplierList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(supplier -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();
            return supplier.getName().toLowerCase().contains(lowerCaseFilter);
        }));

        supplierTable.setItems(filteredList);
    }

    @FXML
    void handleUpdateSupplier(ActionEvent event) {

    }
}
