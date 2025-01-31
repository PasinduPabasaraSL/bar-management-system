package com.example.barmanagementsystempro.model;

import com.example.barmanagementsystempro.db.DBConnection;
import com.example.barmanagementsystempro.dto.LoginDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    public static boolean login(LoginDto loginDto){
        boolean isLogged = false;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            connection = DBConnection.getDBConnection().getConnection();

            String query;
            if (loginDto.getRole().equals("Admin")) {
                query = "SELECT * FROM admin WHERE username=? AND password=?";
            } else {
                query = "SELECT * FROM staff WHERE username=? AND password=?";
            }

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, loginDto.getUsername());
            preparedStatement.setString(2, loginDto.getPassword());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isLogged = true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }

        return isLogged;
    }
}
