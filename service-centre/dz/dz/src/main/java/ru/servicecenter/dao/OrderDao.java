package ru.servicecenter.dao;

import ru.servicecenter.model.Order;
import java.util.List;

public interface OrderDao {

    void create(Order order, java.sql.Connection conn) throws java.sql.SQLException;

    Order getById(int id);

    List<Order> getAll();

    void updateStatus(int id, String status);

    void delete(int id);

    List<Order> filterByStatus(String status);
}
