package service;

import db.DBConnectionProvider;
import model.Customer;
import model.Order;
import model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final Connection connection = DBConnectionProvider.getInstance().getConnection();
    private final CustomerService customerService = new CustomerService();

    public Order addOrder(Order order) {
        String query = "INSERT INTO orders (customer_id, total_price, status) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setInt(1, order.getCustomer().getId());
            preparedStatement.setDouble(2, order.getTotalPrice());
            preparedStatement.setString(3, order.getStatus().name());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setId(resultSet.getInt(1));
            }
            return order;
        } catch (SQLException e) {
            System.err.println("Error adding order" + e.getMessage());
        }
        return null;
    }

    public Order getOrderById(int id) {
        String query = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt(1));
                order.setStatus(Status.valueOf(resultSet.getString(2)));
                order.setCustomer(customerService.getCustomerById(resultSet.getInt(3)));
                order.setTotalPrice(resultSet.getDouble(4));
                return order;
            }
        }catch (SQLException e){
            System.out.println("Error getting order by id" + e.getMessage());
        }
        return null;
    }

    public void updateOrder(Order order) {
        String query = "UPDATE orders SET total_price = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, order.getTotalPrice());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Error changing order to total price" + e.getMessage());
        }
    }

    public void changeOrderStatus(Order order) {
        String query = "UPDATE orders SET status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, order.getStatus().name());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.err.println("Error changing order status" + e.getMessage());
        }
    }

    public List<Order> getOrdersByCustomer(Customer customer) {
        List<Order> orderListByCostumer = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customer.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setCustomer(customer);
                order.setOrderDate(LocalDateTime.parse(resultSet.getString("order_date")));
                order.setTotalPrice(resultSet.getDouble("total_price"));
                order.setStatus(Status.valueOf(resultSet.getString("status")));
                orderListByCostumer.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error printing orders by customer" + e.getMessage());
        }
        return orderListByCostumer;
    }

    public List<Order> getOrders() {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM orders";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setCustomer(customerService.getCustomerById(resultSet.getInt("customer_id")));
                order.setOrderDate(LocalDateTime.parse(resultSet.getString("order_date")));
                order.setTotalPrice(resultSet.getDouble("total_price"));
                order.setStatus(Status.valueOf(resultSet.getString("status")));
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            System.err.println("Error getting orders" + e.getMessage());
        }
        return orderList;
    }
}
