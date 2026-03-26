package ru.servicecenter;

import ru.servicecenter.util.DatabaseConnection;
import ru.servicecenter.view.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initDb();
        new ConsoleMenu().run();
    }
}
