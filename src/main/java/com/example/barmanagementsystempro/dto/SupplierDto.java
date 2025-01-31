package com.example.barmanagementsystempro.dto;

import java.util.List;

public class SupplierDto {
    private String id;
    private String name;
    private String contactNumber;
    private String email;
    private String address;
    private List<String> itemsList;

    public SupplierDto(String id, String name, String contactNumber, String email, String address, List<String> itemsList) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.itemsList = itemsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<String> itemsList) {
        this.itemsList = itemsList;
    }
}
