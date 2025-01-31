package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.LoginDto;
import com.example.barmanagementsystempro.model.LoginModel;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword;

    SceneManager sceneManager = new SceneManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bg.jpg")));
            backgroundImage.setImage(image);
            backgroundImage.setFitWidth(800);
            backgroundImage.setFitHeight(600);
            backgroundImage.setPreserveRatio(false);
            roleComboBox.getItems().addAll("Admin", "Staff");
            roleComboBox.setValue("Admin");
        } catch (Exception e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }

    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUserName.getText();
        String password = txtPassword.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            sceneManager.showAlert("Warning", "Please enter username, password, and select a role.");
            return;
        }

        try {
            boolean isLogged = LoginModel.login(new LoginDto(username, password, role));

            if (isLogged) {
                loadDashboardScene(role, username, event);
                closeCurrentWindow(event);
            } else {
                sceneManager.showAlert("Error", "Incorrect username or password for this role.");
                txtUserName.clear();
                txtPassword.clear();
                txtUserName.requestFocus();
            }
        } catch (IOException e) {
            sceneManager.showAlert("Error", "An error occurred while logging in. Please try again.");
            System.out.println(e.getMessage());
        }
    }

    private void loadDashboardScene(String role, String username, ActionEvent event) throws IOException {
        // Load the FXML file for the dashboard
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/home-view.fxml"));
        Parent root = fxmlLoader.load();

        // Get the controller and initialize it with data
        HomeController homeController = fxmlLoader.getController();
        homeController.setupUserInfo(role, username);
        homeController.handleDashboard(event);


        // Create a new stage for the dashboard
        Stage newStage = new Stage();
        newStage.setTitle("Dashboard");

        // Set the new scene to the stage
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);

        // Optional: Set stage properties
        newStage.centerOnScreen();

        // Show the new stage
        newStage.show();
    }

    private void closeCurrentWindow(ActionEvent event) {
        // Get the current stage from the event
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Close the current stage
        currentStage.close();
    }

    @FXML
    private void handleForgotPassword() {
        System.out.println("Forgot password clicked");
    }
}