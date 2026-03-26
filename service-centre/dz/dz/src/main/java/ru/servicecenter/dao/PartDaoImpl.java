package ru.servicecenter.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.servicecenter.model.Part;
import ru.servicecenter.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartDaoImpl implements PartDao {

    private static final Logger log = LoggerFactory.getLogger(PartDaoImpl.class);

    @Override
    public void create(Part part) {
        String sql = "INSERT INTO parts (name, description, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, part.getName());
            ps.setString(2, part.getDescription());
            ps.setInt(3, part.getQuantity());
            ps.setDouble(4, part.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при добавлении запчасти", e);
        }
    }

    @Override
    public Part getById(int id) {
        String sql = "SELECT * FROM parts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении запчасти по id=" + id, e);
        }
        return null;
    }

    @Override
    public List<Part> getAll() {
        List<Part> list = new ArrayList<>();
        String sql = "SELECT * FROM parts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении списка запчастей", e);
        }
        return list;
    }

    @Override
    public void update(Part part) {
        String sql = "UPDATE parts SET name=?, description=?, quantity=?, price=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, part.getName());
            ps.setString(2, part.getDescription());
            ps.setInt(3, part.getQuantity());
            ps.setDouble(4, part.getPrice());
            ps.setInt(5, part.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при обновлении запчасти id=" + part.getId(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM parts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при удалении запчасти id=" + id, e);
        }
    }

    @Override
    public List<Part> searchByName(String name) {
        List<Part> list = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE name LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске запчастей по имени", e);
        }
        return list;
    }

    public void updateQuantity(int id, int newQuantity, Connection conn) throws SQLException {
        String sql = "UPDATE parts SET quantity = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public Part getById(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM parts WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    private Part mapRow(ResultSet rs) throws SQLException {
        Part p = new Part();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setQuantity(rs.getInt("quantity"));
        p.setPrice(rs.getDouble("price"));
        return p;
    }
}
