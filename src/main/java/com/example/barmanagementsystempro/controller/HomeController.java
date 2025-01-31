package com.example.barmanagementsystempro.controller;

import com.example.barmanagementsystempro.common.SceneManager;
import com.example.barmanagementsystempro.dto.ItemDto;
import com.example.barmanagementsystempro.model.ItemModel;
import com.example.barmanagementsystempro.model.OrderModel;
import com.example.barmanagementsystempro.model.SalesModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HomeController {

    @FXML
    public Button posBtn;

    @FXML
    private PieChart categoryChart;

    @FXML
    private VBox contentArea;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button inventoryBtn;

    @FXML
    private Button inventoryReportBtn;

    @FXML
    private Button ordersBtn;

    @FXML
    private BarChart<?, ?> salesChart;

    @FXML
    private Button salesReportBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Button staffBtn;

    @FXML
    private Button suppliersBtn;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblRole;

    @FXML
    private Label clockLabel;

    @FXML
    private Label lblTotalItems;

    @FXML
    private Label lblTotalOrders;

    @FXML
    private Label lblTotalRevenue;

    @FXML
    private Label lblRevenue;

    @FXML
    private Label lblRevenueIncreasePercentage;

    @FXML
    private Label lblCompleteOrders;

    @FXML
    private Label lblPendingOrders;

    @FXML
    private VBox lowStockAlert;

    @FXML
    private Label lblTotalCost;

    @FXML
    private Label lblProfit;

    private String userName = "";

    static int cachedTotalOrders;
    static int cachedTotalItems;
    static double cachedTotalRevenue;
    static double cachedTotalRevenueIncreasePercentage;
    static int cachedCompleteOrders;
    static int cachedPendingOrders;
    static double cachedTotalCost;

    private static HomeController instance;

    SceneManager sceneManager = new SceneManager();

    public HomeController() {
        instance = this;
    }

    public static HomeController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        initializeClock();
        scheduleResetAtMidnight();
        startLabelUpdates();
        setHoverEffects();
        checkLowStockItems();
    }

    public void checkLowStockItems() throws SQLException, ClassNotFoundException {
        List<ItemDto> lowStockItems = ItemModel.getLowStockItems();

        if (lowStockItems.isEmpty()) {
            lowStockAlert.setVisible(true);
            lowStockAlert.getChildren().clear();

            Label noLowStockLabel = new Label("No low stock items");
            noLowStockLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: green;  -fx-padding: 12;");


            lowStockAlert.getChildren().add(noLowStockLabel);
        } else {
            lowStockAlert.setVisible(true);
            lowStockAlert.getChildren().clear();

            for (ItemDto item : lowStockItems) {
                HBox hbox = new HBox();
                hbox.setStyle("-fx-background-color: #FFF3E0; -fx-padding: 8; -fx-background-radius: 5; -fx-spacing: 10;");

                hbox.setAlignment(Pos.CENTER_LEFT);

                Label label = new Label(item.getItemName() + " (" + item.getQty() + ")                               ");
                label.setTextFill(Color.BLACK);
                label.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");

                Button reorderButton = new Button("Reorder");
                reorderButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 11;");
                reorderButton.setCursor(Cursor.HAND);

                hbox.getChildren().addAll(label, new Region(), reorderButton);

                lowStockAlert.getChildren().add(hbox);
            }

        }
    }

    private void setHoverEffects() {
        // List of all buttons
        List<Button> buttons = Arrays.asList(dashboardBtn, ordersBtn, posBtn, inventoryBtn, suppliersBtn, staffBtn, salesReportBtn, inventoryReportBtn);

        // Apply hover and key effects to each button
        buttons.forEach(this::applyEffects);
    }

    // Method to apply hover and key effects to a button
    private void applyEffects(Button button) {
        // Hover effects
        button.setOnMouseEntered(e -> button.setStyle(getHoverStyle()));
        button.setOnMouseExited(e -> {
            button.setStyle(getDefaultStyle());
        });
    }

    private String getDefaultStyle() {
        return "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 15; -fx-font-size: 14; -fx-alignment: center-left; -fx-min-width: 230; -fx-cursor: hand;";
    }

    private String getHoverStyle() {
        return "-fx-background-color: #34495E; -fx-text-fill: white; -fx-padding: 15; -fx-font-size: 14; -fx-alignment: center-left; -fx-min-width: 230; -fx-cursor: hand;";
    }

    private void scheduleResetAtMidnight() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), event -> {
            LocalTime currentTime = LocalTime.now();
            if (currentTime.getHour() == 0 && currentTime.getMinute() == 0) { // At 12:00 AM
                try {
                    startLabelUpdates();
                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private boolean isDataCachedForToday = false;

    private void startLabelUpdates() throws SQLException, ClassNotFoundException {
        if (isDataCachedForToday) {
            return;
        }

        refreshCacheImmediately();
        isDataCachedForToday = true;
    }

    private void refreshCacheImmediately() throws SQLException, ClassNotFoundException {
        cachedTotalOrders = OrderModel.getTotalOrderCount();
        cachedTotalItems = ItemModel.getTotalItemCount();
        cachedTotalRevenue = SalesModel.getTotalRevenue();
        cachedTotalRevenueIncreasePercentage = SalesModel.getRevenueIncreasePercentage();
        cachedCompleteOrders = OrderModel.getCompletedOrders();
        cachedPendingOrders = OrderModel.getPendingOrders();
        cachedTotalCost = SalesModel.getTotalCost();

        labelUpdates();
    }

    public void labelUpdates() {
        lblTotalOrders.setText(String.valueOf(cachedTotalOrders));
        lblTotalItems.setText(String.valueOf(cachedTotalItems));
        lblRevenue.setText("Rs: " + cachedTotalRevenue);
        String formattedPercentage = String.format("%.2f", cachedTotalRevenueIncreasePercentage);
        if (cachedTotalRevenueIncreasePercentage > 0) {
            lblRevenueIncreasePercentage.setText("+" + formattedPercentage + "%");
        } else {
            lblRevenueIncreasePercentage.setText(formattedPercentage + "%");
            lblRevenueIncreasePercentage.setTextFill(Color.RED);
        }

        lblCompleteOrders.setText(String.valueOf(cachedCompleteOrders));
        lblPendingOrders.setText(cachedPendingOrders + " pending");
        lblTotalRevenue.setText(String.valueOf(cachedTotalRevenue));
        lblTotalCost.setText(String.valueOf(cachedTotalCost));
        lblProfit.setText(String.valueOf(cachedTotalRevenue - cachedTotalCost));

    }

    private void initializeClock() {
        // Create a timeline that updates every second
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
            clockLabel.setText(formatter.format(now));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void setupUserInfo(String role, String username) {
        userName = username;
        lblUserName.setText(capitalizeFirstLetter(username));
        lblRole.setText(role);
        if ("Staff".equals(role)) {
            staffBtn.setDisable(true);
        }
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @FXML
    void handleDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/dashboard-view.fxml"));
        Parent dashboardView = loader.load();

        DashboardController dashboardController = loader.getController();
        dashboardController.setWelcomeMessage(userName);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardView);
    }

    @FXML
    void handleInventory(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/inventory-view.fxml"));
        Parent inventoryView = loader.load();

        String role = lblRole.getText();

        InventoryController inventoryController = loader.getController();
        inventoryController.setSecurity(role);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(inventoryView);
    }

    @FXML
    void handleInventoryReport(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/inventory-report-view.fxml"));
        Parent inventoryReportView = loader.load();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(inventoryReportView);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        Optional<ButtonType> result = sceneManager.confirmAlert("Logout", "Are you sure you want to logout?");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            closeCurrentWindow();
            openLoginScreen();
        }
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.close();
    }

    private void openLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/login-view.fxml"));
            Parent loginView = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(loginView));

            loginStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            sceneManager.showAlert("Error", "An error occurred while loading the login screen.");
        }
    }

    @FXML
    void handleOrders(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/order-view.fxml"));
        Parent inventoryView = loader.load();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(inventoryView);
    }

    @FXML
    void handleSalesReport(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/sales-report-view.fxml"));
        Parent inventoryReportView = loader.load();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(inventoryReportView);
    }

    @FXML
    void handleStaff(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/staff-view.fxml"));
        Parent staffView = loader.load();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(staffView);
    }

    @FXML
    void handleSuppliers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/supplier-view.fxml"));
        Parent supplierView = loader.load();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(supplierView);
    }

    public void handlePOS(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/barmanagementsystempro/view/pos-system-view.fxml"));
        Parent posView = loader.load();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(posView);
    }
}
