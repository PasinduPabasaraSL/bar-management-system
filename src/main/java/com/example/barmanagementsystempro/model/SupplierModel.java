package com.example.barmanagementsystempro.model;

import com.example.barmanagementsystempro.db.DBConnection;
import com.example.barmanagementsystempro.dto.SupplierDto;
import com.example.barmanagementsystempro.tm.InventoryItemTM;
import com.example.barmanagementsystempro.tm.SupplierTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {
    public static ArrayList<SupplierTM> loadAllSuppliers() throws SQLException, ClassNotFoundException {
        ArrayList<SupplierTM> suppliers = new ArrayList<>();

        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM supplier");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String contactNumber = resultSet.getString("contact_number");
            String email = resultSet.getString("email");
            String address = resultSet.getString("address");
            String registrationDate = resultSet.getString("RegistrationDate");
            String status = resultSet.getString("status");

            suppliers.add(new SupplierTM(id, name, contactNumber, email, address, registrationDate, status));
        }

        return suppliers;
    }

    public static boolean addSupplier(SupplierDto supplierDto) throws SQLException, ClassNotFoundException {
        boolean status = false;

        String checkQuery = "SELECT id FROM supplier WHERE id = ?";
        String query = "insert into supplier (id, name, contact_number, email,address) values (?, ?, ?, ?,?)";
        String addItemQry = "insert into item (item_name) values (?)";

        Connection connection = DBConnection.getDBConnection().getConnection();

        connection.setAutoCommit(false);

        PreparedStatement stm = connection.prepareStatement(checkQuery);
        stm.setObject(1, supplierDto.getId());
        ResultSet resultSet = stm.executeQuery();

        if (resultSet.next()) {
            System.out.println("Supplier with ID " + supplierDto.getId() + " already exists.");
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, supplierDto.getId());
            preparedStatement.setObject(2, supplierDto.getName());
            preparedStatement.setObject(3, supplierDto.getContactNumber());
            preparedStatement.setObject(4, supplierDto.getEmail());
            preparedStatement.setObject(5, supplierDto.getAddress());

            int rows = preparedStatement.executeUpdate();
            status = rows > 0;
        }

        return status;
    }

}
