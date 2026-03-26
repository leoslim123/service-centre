package ru.servicecenter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream in = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            props.load(in);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (Exception e) {
            log.error("Не удалось загрузить db.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void initDb() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {

            st.execute("""
                    CREATE TABLE IF NOT EXISTS parts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        description TEXT,
                        quantity INTEGER NOT NULL,
                        price REAL NOT NULL
                    )
                    """);

            st.execute("""
                    CREATE TABLE IF NOT EXISTS orders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        part_id INTEGER NOT NULL,
                        client_name TEXT NOT NULL,
                        description TEXT,
                        quantity INTEGER NOT NULL,
                        status TEXT NOT NULL,
                        created_at TEXT NOT NULL,
                        FOREIGN KEY (part_id) REFERENCES parts(id)
                    )
                    """);

            log.info("Таблицы созданы/проверены");
        } catch (SQLException e) {
            log.error("Ошибка при инициализации БД", e);
        }
    }
}
