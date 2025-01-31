package com.example.barmanagementsystempro.tm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SupplierTM {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty contactNumber;
    private final StringProperty email;
    private final StringProperty address;
    private final StringProperty registrationDate;
    private final StringProperty status;

    public SupplierTM(String id, String name, String contactNumber, String email, String address, String registrationDate, String status) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.contactNumber = new SimpleStringProperty(contactNumber);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.registrationDate = new SimpleStringProperty(registrationDate);
        this.status = new SimpleStringProperty(status);
    }

    // Getter methods for values
    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getContactNumber() {
        return contactNumber.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getRegistrationDate() {
        return registrationDate.get();
    }

    public String getStatus() {
        return status.get();
    }

    // Property accessor methods
    public StringProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty registrationDateProperty() {
        return registrationDate;
    }

    public StringProperty statusProperty() {
        return status;
    }

    // Setter methods for properties
    public void setId(String id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber.set(contactNumber);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate.set(registrationDate);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
