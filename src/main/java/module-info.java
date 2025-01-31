module com.example.barmanagementsystempro {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.barmanagementsystempro to javafx.fxml;
    opens com.example.barmanagementsystempro.controller to javafx.fxml;
    opens com.example.barmanagementsystempro.tm to javafx.base;

    exports com.example.barmanagementsystempro;
}
