package ru.servicecenter.model;

public class Order {

    private int id;
    private int partId;
    private String clientName;
    private String description;
    private int quantity;
    private String status;
    private String createdAt;

    public Order() {
    }

    public Order(int partId, String clientName, String description, int quantity) {
        this.partId = partId;
        this.clientName = clientName;
        this.description = description;
        this.quantity = quantity;
        this.status = "НОВЫЙ";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ID=" + id +
                ", Запчасть ID=" + partId +
                ", Клиент='" + clientName + '\'' +
                ", Описание='" + description + '\'' +
                ", Кол-во=" + quantity +
                ", Статус='" + status + '\'' +
                ", Дата='" + createdAt + '\'';
    }
}
