package ru.servicecenter.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.servicecenter.model.Order;
import ru.servicecenter.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    private static final Logger log = LoggerFactory.getLogger(OrderDaoImpl.class);

    @Override
    public void create(Order order, Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (part_id, client_name, description, quantity, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, datetime('now', 'localtime'))";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getPartId());
            ps.setString(2, order.getClientName());
            ps.setString(3, order.getDescription());
            ps.setInt(4, order.getQuantity());
            ps.setString(5, order.getStatus());
            ps.executeUpdate();
        }
    }

    @Override
    public Order getById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении заказа id=" + id, e);
        }
        return null;
    }

    @Override
    public List<Order> getAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении списка заказов", e);
        }
        return list;
    }

    @Override
    public void updateStatus(int id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при обновлении статуса заказа id=" + id, e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при удалении заказа id=" + id, e);
        }
    }

    @Override
    public List<Order> filterByStatus(String status) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            log.error("Ошибка при фильтрации заказов по статусу", e);
        }
        return list;
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setPartId(rs.getInt("part_id"));
        o.setClientName(rs.getString("client_name"));
        o.setDescription(rs.getString("description"));
        o.setQuantity(rs.getInt("quantity"));
        o.setStatus(rs.getString("status"));
        o.setCreatedAt(rs.getString("created_at"));
        return o;
    }
}
