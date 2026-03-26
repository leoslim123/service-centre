package ru.servicecenter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.servicecenter.dao.OrderDaoImpl;
import ru.servicecenter.dao.PartDaoImpl;
import ru.servicecenter.model.Order;
import ru.servicecenter.model.Part;
import ru.servicecenter.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ServiceCenter {

    private static final Logger log = LoggerFactory.getLogger(ServiceCenter.class);

    private final PartDaoImpl partDao = new PartDaoImpl();
    private final OrderDaoImpl orderDao = new OrderDaoImpl();

    public void addPart(Part part) {
        partDao.create(part);
    }

    public Part getPart(int id) {
        return partDao.getById(id);
    }

    public List<Part> getAllParts() {
        return partDao.getAll();
    }

    public void updatePart(Part part) {
        partDao.update(part);
    }

    public void deletePart(int id) {
        partDao.delete(id);
    }

    public List<Part> searchParts(String name) {
        return partDao.searchByName(name);
    }

    public boolean createOrder(Order order) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Part part = partDao.getById(order.getPartId(), conn);
            if (part == null) {
                log.warn("Запчасть не найдена, id={}", order.getPartId());
                conn.rollback();
                return false;
            }

            if (part.getQuantity() < order.getQuantity()) {
                log.warn("Недостаточно запчастей на складе. Доступно: {}, запрошено: {}", part.getQuantity(), order.getQuantity());
                conn.rollback();
                return false;
            }

            partDao.updateQuantity(part.getId(), part.getQuantity() - order.getQuantity(), conn);
            orderDao.create(order, conn);

            conn.commit();
            return true;
        } catch (SQLException e) {
            log.error("Ошибка при оформлении заказа", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    log.error("Ошибка при откате транзакции", ex);
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    log.error("Ошибка при закрытии соединения", e);
                }
            }
        }
    }

    public List<Order> getAllOrders() {
        return orderDao.getAll();
    }

    public void updateOrderStatus(int id, String status) {
        orderDao.updateStatus(id, status);
    }

    public void deleteOrder(int id) {
        orderDao.delete(id);
    }

    public List<Order> filterOrders(String status) {
        return orderDao.filterByStatus(status);
    }
}
