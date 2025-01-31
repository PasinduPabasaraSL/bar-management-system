package com.example.barmanagementsystempro.model;

import com.example.barmanagementsystempro.db.DBConnection;
import com.example.barmanagementsystempro.dto.ItemDto;
import com.example.barmanagementsystempro.dto.UpdateDto;
import com.example.barmanagementsystempro.tm.InventoryItemTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {

    public static boolean addItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        boolean status;

        String query = "insert into item (item_name, category, qty, cost_price,selling_price) values (?, ?, ?, ?,?)";

        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setObject(1, itemDto.getItemName());
        preparedStatement.setObject(2, itemDto.getCategory());
        preparedStatement.setObject(3, itemDto.getQty());
        preparedStatement.setObject(4, itemDto.getCostPrice());
        preparedStatement.setObject(5, itemDto.getSellingPrice());

        int rows = preparedStatement.executeUpdate();
        status = rows > 0;

        return status;
    }

    public static boolean deleteItem(int itemId) throws SQLException, ClassNotFoundException {
        boolean status;

        String query = "delete from item where id=? ";

        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setObject(1, itemId);

        int rows = preparedStatement.executeUpdate();
        status = rows > 0;

        return status;
    }

    public static boolean updateItem(UpdateDto updateDto) throws SQLException, ClassNotFoundException {
        boolean status;

        String query = "update item set item_name=?,category=?,qty=?,price=? where id=? ";

        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setObject(1, updateDto.getItemName());
        preparedStatement.setObject(2, updateDto.getCategory());
        preparedStatement.setObject(3, updateDto.getQty());
        preparedStatement.setObject(4, updateDto.getPrice());
        preparedStatement.setObject(5, updateDto.getItemId());

        int rows = preparedStatement.executeUpdate();
        status = rows > 0;

        return status;
    }

    public static ArrayList<InventoryItemTM> loadAllInventoryItems() {
        Connection connection;
        ArrayList<InventoryItemTM> items = new ArrayList<>();
        try {
            connection = DBConnection.getDBConnection().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("select * from item");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                items.add(new InventoryItemTM(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getInt(4),
                                resultSet.getDouble(5),
                                resultSet.getDouble(6),
                                resultSet.getInt(7)
                        )
                );

            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return items;
    }

    public static int getTotalItemCount() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from item");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public static List<ItemDto> getLowStockItems() throws SQLException, ClassNotFoundException {
        List<ItemDto> lowStockItems = new ArrayList<>();
        String query = "SELECT item_name, qty FROM item WHERE qty < reorder_level";

        Connection connection = DBConnection.getDBConnection().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String itemName = rs.getString("item_name");
            int qty = rs.getInt("qty");
            lowStockItems.add(new ItemDto(itemName, "", qty, 0.0, 0.0)); // Adjust second parameter if needed
        }


        return lowStockItems;

    }

}
