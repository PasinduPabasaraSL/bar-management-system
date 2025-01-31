package com.example.barmanagementsystempro.model;

import com.example.barmanagementsystempro.db.DBConnection;
import com.example.barmanagementsystempro.dto.SalesDto;
import com.example.barmanagementsystempro.tm.InvoiceTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesModel {
    public static boolean completeSale(SalesDto salesDto) throws SQLException, ClassNotFoundException {
        Connection connection = null;

        try {
            connection = DBConnection.getDBConnection().getConnection();

            // Disable auto-commit
            connection.setAutoCommit(false);

            // Insert into sales table
            try (PreparedStatement stm1 = connection.prepareStatement("INSERT INTO sales (order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)")) {

                for (InvoiceTM item : salesDto.getInvoiceDetails()) {
                    stm1.setObject(1, salesDto.getOrderId());
                    stm1.setObject(2, item.getItemId());
                    stm1.setObject(3, item.getQuantity());
                    stm1.setObject(4, item.getPrice());
                    stm1.addBatch();
                }

                int[] batchResults = stm1.executeBatch();

                // Validate batch results
                for (int result : batchResults) {
                    if (result == PreparedStatement.EXECUTE_FAILED) {
                        throw new SQLException("Batch insert into sales failed.");
                    }
                }
            }

            // Update payment status
            try (PreparedStatement stm2 = connection.prepareStatement("UPDATE orders SET payment = ? WHERE order_id = ?")) {
                stm2.setObject(1, "paid");
                stm2.setObject(2, salesDto.getOrderId());

                int orderRows = stm2.executeUpdate();

                if (orderRows <= 0) {
                    throw new SQLException("Failed to update payment status.");
                }
            }

            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rollback on failure
            }
            throw e; // Re-throw exception to caller
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true); // Ensure auto-commit is reset
            }
        }

    }

    public static double getTotalRevenue() throws SQLException, ClassNotFoundException {
        double totalRevenue = 0.0;
        Connection connection = DBConnection.getDBConnection().getConnection();
        String query = "SELECT SUM(price) AS total_revenue FROM sales WHERE DATE(time) = CURDATE()";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            totalRevenue = resultSet.getDouble("total_revenue");
        }

        return totalRevenue;
    }

    public static double getRevenueIncreasePercentage() throws SQLException, ClassNotFoundException {
        double revenueIncreasePercentage = 0.0;
        double yesterdayRevenue = 0.0;
        double todayRevenue = getTotalRevenue();

        Connection connection = DBConnection.getDBConnection().getConnection();

        String query = "SELECT SUM(price) AS yesterday_revenue " +
                "FROM sales " +
                "WHERE DATE(time) = CURDATE() - INTERVAL 1 DAY;";


        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Process the result
        if (resultSet.next()) {
            yesterdayRevenue = resultSet.getDouble("yesterday_revenue");
        }

        if (todayRevenue > 0) {
            revenueIncreasePercentage = ((todayRevenue - yesterdayRevenue) / yesterdayRevenue) * 100;
        }

        return revenueIncreasePercentage;
    }

    public static double getTotalCost() throws SQLException, ClassNotFoundException {
        double totalCost = 0.0;
        Connection connection = DBConnection.getDBConnection().getConnection();
        String query = "select SUM(item.cost_price) as total_cost from item inner join sales on item.id = sales.item_id where DATE(time) = CURDATE()";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            totalCost = resultSet.getDouble("total_cost");
        }

        return totalCost;
    }
}
