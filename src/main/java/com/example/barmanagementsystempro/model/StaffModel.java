package com.example.barmanagementsystempro.model;

import com.example.barmanagementsystempro.db.DBConnection;
import com.example.barmanagementsystempro.tm.InventoryItemTM;
import com.example.barmanagementsystempro.tm.StaffTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class StaffModel {
    public static ArrayList<StaffTM> loadAllStaff() throws SQLException, ClassNotFoundException {
        ArrayList<StaffTM> staffList = new ArrayList<>();

        Connection connection = DBConnection.getDBConnection().getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select * from staff");

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String fullName = firstName + " " + lastName; // Combine first and last names
            String role = resultSet.getString("role");
            String contact = resultSet.getString("phone_number");
            String email = resultSet.getString("email");
            Date joinDate = resultSet.getDate("hire_date");
            double salary = resultSet.getDouble("salary");
            String status = resultSet.getString("status");

            staffList.add(new StaffTM(id, fullName, role, contact, email, joinDate, salary, status));
        }

        return staffList;

    }
}
