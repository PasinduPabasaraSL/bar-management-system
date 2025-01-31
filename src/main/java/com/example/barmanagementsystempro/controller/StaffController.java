package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.model.StaffModel;
import com.example.barmanagementsystempro.tm.StaffTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;
import java.util.ArrayList;

public class StaffController {
    @FXML
    private Label bartendersLabel;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableColumn<StaffTM, String> colContact;

    @FXML
    private TableColumn<StaffTM, String> colEmail;

    @FXML
    private TableColumn<StaffTM, String> colJoinDate;

    @FXML
    private TableColumn<StaffTM, String> colName;

    @FXML
    private TableColumn<StaffTM, String> colRole;

    @FXML
    private TableColumn<StaffTM, String> colSalary;

    @FXML
    private TableColumn<StaffTM, String> colStatus;

    @FXML
    private Label managersLabel;

    @FXML
    private Label monthlyPayrollLabel;

    @FXML
    private BorderPane root;

    @FXML
    private TableView<StaffTM> staffTable;

    @FXML
    private Label todayShiftsLabel;

    @FXML
    private Label totalStaffLabel;

    @FXML
    private TextField txtSearchStaff;

    @FXML
    private Label waitersLabel;

    ObservableList<StaffTM> staffList;


    public void initialize() throws SQLException, ClassNotFoundException {
        staffList = javafx.collections.FXCollections.observableArrayList();

        colName.setCellValueFactory(new PropertyValueFactory<>("staffName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colJoinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        staffTable.getColumns().forEach(column -> column.setResizable(false));
        staffTable.getColumns().forEach(column -> column.setReorderable(false));

        ArrayList<StaffTM> staffTMS = StaffModel.loadAllStaff();
        staffList.addAll(staffTMS);
        staffTable.setItems(staffList);

        ObservableList<String> roles = FXCollections.observableArrayList(
                "Bartender",
                "Waiter",
                "Cleaner",
                "Cashier",
                "Chef",
                "Security"
        );

        cmbRole.setItems(roles);

    }

    @FXML
    void addStaff(ActionEvent event) {

    }

    @FXML
    void deleteStaff(ActionEvent event) {

    }

    @FXML
    void handleSearch(ActionEvent event) {

    }

    @FXML
    void updateStaff(ActionEvent event) {

    }
}
