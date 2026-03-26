package ru.servicecenter.view;

import ru.servicecenter.model.Order;
import ru.servicecenter.model.Part;
import ru.servicecenter.service.ServiceCenter;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final ServiceCenter service = new ServiceCenter();
    private final Scanner sc = new Scanner(System.in);

    public void run() {
        while (true) {
            System.out.println();
            System.out.println("===== Сервисный Центр =====");
            System.out.println("1. Управление запчастями");
            System.out.println("2. Управление заказами");
            System.out.println("0. Выход");
            System.out.print("Выбор: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> partsMenu();
                case "2" -> ordersMenu();
                case "0" -> {
                    System.out.println("До свидания!");
                    return;
                }
                default -> System.out.println("Неверный ввод");
            }
        }
    }

    private void partsMenu() {
        while (true) {
            System.out.println();
            System.out.println("----- Запчасти -----");
            System.out.println("1. Показать все");
            System.out.println("2. Добавить");
            System.out.println("3. Редактировать");
            System.out.println("4. Удалить");
            System.out.println("5. Поиск по названию");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> showAllParts();
                case "2" -> addPart();
                case "3" -> editPart();
                case "4" -> deletePart();
                case "5" -> searchParts();
                case "0" -> { return; }
                default -> System.out.println("Неверный ввод");
            }
        }
    }

    private void showAllParts() {
        List<Part> parts = service.getAllParts();
        if (parts.isEmpty()) {
            System.out.println("Склад пуст");
            return;
        }
        for (Part p : parts) {
            System.out.println(p);
        }
    }

    private void addPart() {
        System.out.print("Название: ");
        String name = sc.nextLine().trim();
        System.out.print("Описание: ");
        String desc = sc.nextLine().trim();
        System.out.print("Количество: ");
        int qty = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Цена: ");
        double price = Double.parseDouble(sc.nextLine().trim());

        Part part = new Part(name, desc, qty, price);
        service.addPart(part);
        System.out.println("Запчасть добавлена");
    }

    private void editPart() {
        System.out.print("ID запчасти: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Part part = service.getPart(id);
        if (part == null) {
            System.out.println("Запчасть не найдена");
            return;
        }
        System.out.println("Текущие данные: " + part);
        System.out.print("Новое название (Enter - оставить): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) part.setName(name);
        System.out.print("Новое описание (Enter - оставить): ");
        String desc = sc.nextLine().trim();
        if (!desc.isEmpty()) part.setDescription(desc);
        System.out.print("Новое количество (Enter - оставить): ");
        String qtyStr = sc.nextLine().trim();
        if (!qtyStr.isEmpty()) part.setQuantity(Integer.parseInt(qtyStr));
        System.out.print("Новая цена (Enter - оставить): ");
        String priceStr = sc.nextLine().trim();
        if (!priceStr.isEmpty()) part.setPrice(Double.parseDouble(priceStr));

        service.updatePart(part);
        System.out.println("Запчасть обновлена");
    }

    private void deletePart() {
        System.out.print("ID запчасти для удаления: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        service.deletePart(id);
        System.out.println("Удалено");
    }

    private void searchParts() {
        System.out.print("Введите название для поиска: ");
        String name = sc.nextLine().trim();
        List<Part> parts = service.searchParts(name);
        if (parts.isEmpty()) {
            System.out.println("Ничего не найдено");
            return;
        }
        for (Part p : parts) {
            System.out.println(p);
        }
    }

    private void ordersMenu() {
        while (true) {
            System.out.println();
            System.out.println("----- Заказы -----");
            System.out.println("1. Показать все");
            System.out.println("2. Оформить заказ");
            System.out.println("3. Изменить статус");
            System.out.println("4. Удалить заказ");
            System.out.println("5. Фильтр по статусу");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> showAllOrders();
                case "2" -> createOrder();
                case "3" -> changeOrderStatus();
                case "4" -> deleteOrder();
                case "5" -> filterOrders();
                case "0" -> { return; }
                default -> System.out.println("Неверный ввод");
            }
        }
    }

    private void showAllOrders() {
        List<Order> orders = service.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Заказов нет");
            return;
        }
        for (Order o : orders) {
            System.out.println(o);
        }
    }

    private void createOrder() {
        System.out.print("ID запчасти: ");
        int partId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Имя клиента: ");
        String client = sc.nextLine().trim();
        System.out.print("Описание ремонта: ");
        String desc = sc.nextLine().trim();
        System.out.print("Количество запчастей: ");
        int qty = Integer.parseInt(sc.nextLine().trim());

        Order order = new Order(partId, client, desc, qty);
        if (service.createOrder(order)) {
            System.out.println("Заказ оформлен (запчасти списаны со склада)");
        } else {
            System.out.println("Не удалось оформить заказ");
        }
    }

    private void changeOrderStatus() {
        System.out.print("ID заказа: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.println("Статусы: НОВЫЙ, В_РАБОТЕ, ГОТОВ, ОТМЕНЕН");
        System.out.print("Новый статус: ");
        String status = sc.nextLine().trim();
        service.updateOrderStatus(id, status);
        System.out.println("Статус обновлен");
    }

    private void deleteOrder() {
        System.out.print("ID заказа для удаления: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        service.deleteOrder(id);
        System.out.println("Заказ удален");
    }

    private void filterOrders() {
        System.out.println("Статусы: НОВЫЙ, В_РАБОТЕ, ГОТОВ, ОТМЕНЕН");
        System.out.print("Фильтр по статусу: ");
        String status = sc.nextLine().trim();
        List<Order> orders = service.filterOrders(status);
        if (orders.isEmpty()) {
            System.out.println("Заказов с таким статусом нет");
            return;
        }
        for (Order o : orders) {
            System.out.println(o);
        }
    }
}
