package com.example.barmanagementsystempro.model;

import com.example.barmanagementsystempro.db.DBConnection;
import com.example.barmanagementsystempro.dto.OrderDetailsDto;
import com.example.barmanagementsystempro.dto.OrderDto;
import com.example.barmanagementsystempro.tm.InvoiceTM;
import com.example.barmanagementsystempro.tm.OrderTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderModel {
    public static boolean placeOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException {
        boolean status;
        Connection connection;

        connection = DBConnection.getDBConnection().getConnection();

        // Disable auto-commit
        connection.setAutoCommit(false);

        try {
            // Insert into orders
            PreparedStatement stm1 = connection.prepareStatement("INSERT INTO orders (order_id, table_no, status, total) VALUES (?, ?, ?, ?)");
            stm1.setObject(1, orderDto.getOrderId());
            stm1.setObject(2, orderDto.getTableNo());
            stm1.setObject(3, "pending");
            stm1.setObject(4, orderDto.getSubTotal());

            int orderSave = stm1.executeUpdate();

            if (orderSave > 0) {
                // Insert into order_details
                for (OrderDetailsDto dto : orderDto.getOrderDetails()) {
                    PreparedStatement stm2 = connection.prepareStatement("INSERT INTO order_details (order_id, item_id, qty, price) VALUES (?, ?, ?, ?)");
                    stm2.setObject(1, orderDto.getOrderId()); // Use the same orderId
                    stm2.setObject(2, dto.getItemId());
                    stm2.setObject(3, dto.getQty());
                    stm2.setObject(4, dto.getPrice());

                    int orderDetailSave = stm2.executeUpdate();

                    if (orderDetailSave > 0) {
                        // Update item quantity
                        PreparedStatement stm3 = connection.prepareStatement("UPDATE item SET qty = qty - ? WHERE id = ?");
                        stm3.setObject(1, dto.getQty());
                        stm3.setObject(2, dto.getItemId());

                        int itemQtyUpdate = stm3.executeUpdate();

                        if (itemQtyUpdate <= 0) {
                            connection.rollback();
                            connection.setAutoCommit(true);
                            return false; // Exit on failure
                        }
                    } else {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        return false; // Exit on failure
                    }
                }
            } else {
                connection.rollback();
                connection.setAutoCommit(true);
                return false; // Exit on failure
            }

            // Commit the transaction
            connection.commit();
            status = true;
        } catch (SQLException e) {
            connection.rollback(); // Rollback in case of exception
            throw e; // Rethrow exception for logging or further handling
        } finally {
            connection.setAutoCommit(true); // Restore auto-commit
        }

        return status;

    }

    public static ObservableList<OrderTM> loadAllOrders() throws SQLException, ClassNotFoundException {
        ObservableList<OrderTM> orders = FXCollections.observableArrayList();

        Connection connection = DBConnection.getDBConnection().getConnection();

        String query = "SELECT o.order_id, o.table_no, o.status, o.time, o.total, " +
                "GROUP_CONCAT(i.item_name SEPARATOR ', ') AS items " +
                "FROM orders o " +
                "LEFT JOIN order_details od ON o.order_id = od.order_id " +
                "LEFT JOIN item i ON od.item_id = i.id " +
                "GROUP BY o.order_id";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String orderId = resultSet.getString("order_id");
            int tableNo = resultSet.getInt("table_no");
            String status = resultSet.getString("status");
            String time = resultSet.getString("time");
            double total = resultSet.getDouble("total");
            String items = resultSet.getString("items");

            OrderTM order = new OrderTM(orderId, tableNo, items, total, status, time);

            orders.add(order);
        }

        return orders;
    }

    public static ObservableList<OrderTM> loadUnpaidOrders() throws SQLException, ClassNotFoundException {
        ObservableList<OrderTM> orders = FXCollections.observableArrayList();

        Connection connection = DBConnection.getDBConnection().getConnection();

        String query = """
                SELECT o.order_id,\s
                       o.table_no,\s
                       o.status,\s
                       o.time,\s
                       o.total,\s
                       GROUP_CONCAT(CONCAT(ic.item_name, ' - ', ic.item_qty) SEPARATOR ', ') AS items
                FROM orders o
                LEFT JOIN (
                    SELECT od.order_id,\s
                           i.item_name,\s
                           SUM(od.qty) AS item_qty
                    FROM order_details od
                    JOIN item i ON od.item_id = i.id
                    GROUP BY od.order_id, i.item_name
                ) AS ic ON o.order_id = ic.order_id
                WHERE o.status = 'completed'
                  AND o.payment = 'unpaid'
                  AND DATE(o.time) = CURDATE()  -- Filter for today's date
                GROUP BY o.order_id;
                """;


        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String orderId = resultSet.getString("order_id");
            int tableNo = resultSet.getInt("table_no");
            String status = resultSet.getString("status");
            String time = resultSet.getString("time");
            double total = resultSet.getDouble("total");
            String items = resultSet.getString("items");

            OrderTM order = new OrderTM(orderId, tableNo, items, total, status, time);

            orders.add(order);
        }

        return orders;
    }

    public static ObservableList<InvoiceTM> getInvoiceDetails(String orderId) throws SQLException, ClassNotFoundException {
        ObservableList<InvoiceTM> orderDetails = FXCollections.observableArrayList();

        Connection connection = DBConnection.getDBConnection().getConnection();

        String query = "SELECT i.id AS item_id, i.item_name, od.qty, od.price " +
                "FROM order_details od " +
                "JOIN item i ON od.item_id = i.id " +
                "WHERE od.order_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setObject(1, orderId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int itemId = resultSet.getInt("item_id");
            String itemName = resultSet.getString("item_name");
            int quantity = resultSet.getInt("qty");
            double price = resultSet.getDouble("price");

            // Create an InvoiceTM object and add it to the list
            InvoiceTM invoiceTM = new InvoiceTM(itemId, itemName, quantity, price);
            orderDetails.add(invoiceTM);
        }

        return orderDetails;

    }

    public static void updateOrderStatus(String orderId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDBConnection().getConnection();

        String query = "UPDATE orders SET status = ? WHERE order_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setObject(1, "completed");
        preparedStatement.setObject(2, orderId);

        int rows = preparedStatement.executeUpdate();

        if (rows > 0) {
            System.out.println("Order status updated");
        }
    }

    public static int getTotalOrderCount() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String query = "SELECT COUNT(*) FROM orders WHERE DATE(time) = CURRENT_DATE";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public static int getCompletedOrders() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String query = "SELECT COUNT(*) AS completed_orders FROM orders WHERE status = 'completed'";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        int completedOrders = 0;

        if (resultSet.next()) {
            completedOrders = resultSet.getInt("completed_orders");
        }

        return completedOrders;

    }

    public static int getPendingOrders() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDBConnection().getConnection();
        String query = "SELECT COUNT(*) AS completed_orders FROM orders WHERE status = 'pending'";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        int pendingOrders = 0;

        if (resultSet.next()) {
            pendingOrders = resultSet.getInt("completed_orders");
        }

        return pendingOrders;

    }

}