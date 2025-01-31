package com.example.barmanagementsystempro.model;

import com.example.barmanagementsystempro.db.DBConnection;
import com.example.barmanagementsystempro.dto.ReOrderLevelDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReOrderLevelModel {
    public static boolean saveReOrderLevel(ReOrderLevelDto reOrderLevelDto) throws SQLException, ClassNotFoundException {
        boolean status;
        Connection connection = DBConnection.getDBConnection().getConnection();
        String query = "update item set reorder_level=? where id=? ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setObject(1, reOrderLevelDto.getReOrderLevel());
        preparedStatement.setObject(2, reOrderLevelDto.getItemId());

        int rows = preparedStatement.executeUpdate();
        status = rows > 0;

        return status;
    }

//    public void checkCurrentStock() throws SQLException, ClassNotFoundException {
//        ArrayList<ItemDto> items
//        Connection connection = DBConnection.getDBConnection().getConnection();
//        String query = "select * from item";
//
//
//    }
}

