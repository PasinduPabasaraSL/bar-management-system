package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.ReOrderLevelDto;
import com.example.barmanagementsystempro.model.ReOrderLevelModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CalculateReorderlevelController {

    @FXML
    private VBox root;
    @FXML
    private Label lblLeadTimeDemand;

    @FXML
    private Label lblROL;

    @FXML
    private Label lblSafetyStock;

    @FXML
    private TextField txtAvgDemand;

    @FXML
    private TextField txtLeadTime;

    @FXML
    private TextField txtStandardDeviationOfDemand;

    @FXML
    private TextField txtZScore;

    @FXML
    private Label lblItemName;

    @FXML
    private Label lblPreviousROL;

    private int itemId;

    SceneManager sceneManager = new SceneManager();

    @FXML
    public void initialize(ReOrderLevelDto reorderLevelDto) {
        this.itemId = reorderLevelDto.getItemId();
        String itemName = reorderLevelDto.getItemName();
        int rol = reorderLevelDto.getReOrderLevel();
        setPreviousItemData(itemName, rol);
    }

    private void setPreviousItemData(String itemName, int rol) {
        lblItemName.setText(itemName);
        lblPreviousROL.setText(String.valueOf(rol));
    }

    @FXML
    private void calLeadTimeDemand(ActionEvent event) {
        String avgDemandText = txtAvgDemand.getText();
        String leadTimeText = txtLeadTime.getText();

        if (avgDemandText.isEmpty() || leadTimeText.isEmpty()) {
            sceneManager.showAlert("Error", "Please enter all values.");
            return;
        }

        try {
            double averageDemandPerWeek = Double.parseDouble(avgDemandText);
            double leadTimePerWeek = Double.parseDouble(leadTimeText);

            double leadTimeDemand = averageDemandPerWeek * leadTimePerWeek;
            lblLeadTimeDemand.setText(String.format("%.2f", leadTimeDemand));

        } catch (NumberFormatException e) {
            sceneManager.showAlert("Error", "Both inputs must be valid numbers.");
            txtAvgDemand.clear();
            txtLeadTime.clear();
        }
    }


    @FXML
    private void calSafetyStock(ActionEvent event) {
        String zScoreText = txtZScore.getText();
        String demandStdDevText = txtStandardDeviationOfDemand.getText();
        String leadTimeText = txtLeadTime.getText();

        if (zScoreText.isEmpty() || demandStdDevText.isEmpty() || leadTimeText.isEmpty()) {
            sceneManager.showAlert("Error", "Please enter all values.");
            return;
        }

        try {
            double zScore = Double.parseDouble(zScoreText);
            double demandStdDev = Double.parseDouble(demandStdDevText);
            double leadTime = Double.parseDouble(leadTimeText);

            if (zScore <= 0 || demandStdDev <= 0 || leadTime <= 0) {
                sceneManager.showAlert("Error", "Inputs must be greater than zero.");
                txtZScore.clear();
                txtStandardDeviationOfDemand.clear();
            } else {
                double safetyStock = zScore * demandStdDev * Math.sqrt(leadTime);
                lblSafetyStock.setText(String.format("%.2f", safetyStock));
            }
        } catch (NumberFormatException e) {
            sceneManager.showAlert("Error", "All inputs must be valid numbers.");
            txtZScore.clear();
            txtStandardDeviationOfDemand.clear();
        }
    }

    @FXML
    private void calROL(ActionEvent event) {
        double leadTimeDemand = Double.parseDouble(lblLeadTimeDemand.getText());
        double safetyStock = Double.parseDouble(lblSafetyStock.getText());

        if (lblLeadTimeDemand.getText().isEmpty() && lblSafetyStock.getText().isEmpty()) {
            sceneManager.showAlert("Error", "Please enter all values.");
            return;
        }

        int rol = (int) (leadTimeDemand + safetyStock);

        lblROL.setText(String.valueOf(rol));

    }

    @FXML
    private void handleSaveROL(ActionEvent event) throws SQLException, ClassNotFoundException {
        int rlo = Integer.parseInt(lblROL.getText());

        if (lblROL.getText().isEmpty()){
            sceneManager.showAlert("Error", "Please Calculate Re Order Level");
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to change Re Order Level?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean isSaved = ReOrderLevelModel.saveReOrderLevel(new ReOrderLevelDto(itemId, rlo));

            if (isSaved) {
                sceneManager.showAlert("Success", "Saved reorder level successfully.");
                txtAvgDemand.clear();
                txtLeadTime.clear();
                txtStandardDeviationOfDemand.clear();
                txtZScore.clear();
                lblLeadTimeDemand.setText("");
                lblSafetyStock.setText("");
                lblROL.setText("");

            } else {
                sceneManager.showAlert("Error", "Failed to save reorder level.");
            }

        }

    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

}
