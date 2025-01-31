package com.example.barmanagementsystempro.tm;

import javafx.beans.property.*;

import java.util.Date;

public class StaffTM {
    private final StringProperty staffId;
    private final StringProperty staffName;
    private final StringProperty role;
    private final StringProperty contact;
    private final StringProperty email;
    private final ObjectProperty<Date> joinDate;
    private final DoubleProperty salary;
    private final StringProperty status;

    public StaffTM(String staffId, String staffName, String role, String contact, String email,Date joinDate, double salary, String status) {
        this.staffId = new SimpleStringProperty(staffId);;
        this.staffName = new SimpleStringProperty(staffName);
        this.role = new SimpleStringProperty(role);;
        this.contact = new SimpleStringProperty(contact);;
        this.email = new SimpleStringProperty(email);;
        this.joinDate = new SimpleObjectProperty<>(joinDate);
        this.salary = new SimpleDoubleProperty(salary);
        this.status = new SimpleStringProperty(status);
    }

    // Getters for Properties
    public StringProperty staffIdProperty() {
        return staffId;
    }

    public StringProperty staffNameProperty() {
        return staffName;
    }

    public StringProperty roleProperty() {
        return role;
    }

    public StringProperty contactProperty() {
        return contact;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public ObjectProperty<Date> joinDateProperty() {
        return joinDate;
    }

    public DoubleProperty salaryProperty() {
        return salary;
    }

    public StringProperty statusProperty() {
        return status;
    }

    // Regular Getters and Setters for non-property fields (if needed)
    public Date getJoinDate() {
        return joinDate.get();
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate.set(joinDate);
    }

    public double getSalary() {
        return salary.get();
    }

    public void setSalary(double salary) {
        this.salary.set(salary);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

}
