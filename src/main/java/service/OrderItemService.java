package service;

import db.DBConnectionProvider;
import model.Dish;
import model.Order;
import model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderItemService {

    private final Connection connection = DBConnectionProvider.getInstance().getConnection();

    public void addOrderItem(OrderItem orderItem) {
        String query = "INSERT INTO order_items (order_id, dish_id,quantity,price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, orderItem.getOrder().getId());
            preparedStatement.setInt(2, orderItem.getDish().getId());
            preparedStatement.setInt(3, orderItem.getQuantity());
            preparedStatement.setDouble(4, orderItem.getPrice());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                orderItem.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Error while inserting OrderItem" + e.getMessage());
        }
    }




}
