package com.example.barmanagementsystempro.common;

import com.example.barmanagementsystempro.controller.HomeController;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class SceneManager {
    private static final String BACKGROUND_COLOR = "#F8FAFC";
    private static final String ACCENT_COLOR = "#243444";
    private static final String HOVER_COLOR = "#36454F";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String TEXT_PRIMARY = "#1E293B";
    private static final String TEXT_SECONDARY = "#64748B";

    public HomeController handleBackButton(ActionEvent event, String fxmlFile) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());

        HomeController homeController = fxmlLoader.getController();

        stage.setScene(scene);
        stage.centerOnScreen();

        return homeController;
    }

    public void showAlert(String title, String message) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(title);

        VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("""
                -fx-background-color: %s;
                -fx-padding: 24px;
                -fx-min-width: 360;
                """.formatted(BACKGROUND_COLOR));

        // Subtle shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        shadow.setRadius(15);
        shadow.setSpread(0.1);
        vbox.setEffect(shadow);

        // Smaller title
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Inter", 20));
        titleText.setFill(Color.web(TEXT_PRIMARY));
        titleText.setStyle("-fx-font-weight: bold;");

        // Compact message
        Text messageText = new Text(message);
        messageText.setFont(Font.font("Inter", 13));
        messageText.setFill(Color.web(TEXT_SECONDARY));
        messageText.setTextAlignment(TextAlignment.CENTER);
        messageText.setWrappingWidth(320);

        vbox.getChildren().addAll(titleText, messageText);

        dialog.getDialogPane().setStyle("""
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-border-width: 1px;
                """.formatted(BACKGROUND_COLOR, BORDER_COLOR));

        // Teal button with compact design
        ButtonType okButtonType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setStyle("""
                -fx-background-color: %s;
                -fx-text-fill: white;
                -fx-font-family: 'Inter';
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-padding: 8px 24px;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(20, 184, 166, 0.3), 6, 0, 0, 2);
                """.formatted(ACCENT_COLOR));

        // Subtle hover effect
        okButton.setOnMouseEntered(e -> okButton.setStyle(okButton.getStyle() + """
                -fx-background-color: %s;
                -fx-effect: dropshadow(gaussian, rgba(20, 184, 166, 0.4), 8, 0, 0, 3);
                """.formatted(HOVER_COLOR)));

        okButton.setOnMouseExited(e -> okButton.setStyle(okButton.getStyle() + """
                -fx-background-color: %s;
                -fx-effect: dropshadow(gaussian, rgba(20, 184, 166, 0.3), 6, 0, 0, 2);
                """.formatted(ACCENT_COLOR)));

        dialog.getDialogPane().setContent(vbox);

        // Quick fade animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), dialog.getDialogPane());
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        dialog.getDialogPane().setStyle("""
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-border-width: 1px;
                -fx-padding: 10px;
                """.formatted(BACKGROUND_COLOR, BORDER_COLOR));

        dialog.showAndWait();
    }

    public Optional<ButtonType> confirmAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().setStyle(
                "-fx-background-color: #ffffff; " +  // White background for the alert box
                        "-fx-border-radius: 10; " +          // Rounded corners for the alert box
                        "-fx-padding: 20; " +                // Padding inside the alert box
                        "-fx-font-family: 'Segoe UI', Tahoma, Arial; " +  // Font family (modern look)
                        "-fx-font-size: 14px; " +            // Font size for content text
                        "-fx-alignment: center; " +          // Center the content
                        "-fx-text-fill: #333333;"            // Text color (dark gray)
        );

        alert.getButtonTypes().forEach(buttonType -> alert.getDialogPane().lookupButton(buttonType).setStyle(
                "-fx-background-color: #4A90E2; " +   // Blue background for confirmation button
                        "-fx-text-fill: white; " +            // White text
                        "-fx-font-weight: bold; " +           // Bold text
                        "-fx-font-size: 14px; " +             // Button font size
                        "-fx-padding: 8 20; " +               // Button padding
                        "-fx-background-radius: 5; " +        // Rounded corners for buttons
                        "-fx-cursor: hand; "                  // Hand cursor on hover
        ));

        alert.getButtonTypes().stream()
                .filter(buttonType -> buttonType.getText().equals("Cancel"))
                .findFirst().ifPresent(cancelButton -> alert.getDialogPane().lookupButton(cancelButton).setStyle(
                        "-fx-background-color: #B0B0B0; " +   // Gray background for cancel button
                                "-fx-text-fill: white; " +             // White text
                                "-fx-font-weight: bold; " +            // Bold text
                                "-fx-font-size: 14px; " +              // Button font size
                                "-fx-padding: 8 20; " +                // Button padding
                                "-fx-background-radius: 5; "           // Rounded corners
                ));

        return alert.showAndWait();
    }

}
