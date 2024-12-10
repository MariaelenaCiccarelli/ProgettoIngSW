package com.example.lootmarketbackend.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    // ATTRIBUTI
    private static ConnessioneDatabase instance;
    public Connection connection;
    private String nome = "postgres";
    private String password = "postgres";
    private String url = "jdbc:postgresql://db:5432/postgres";
    private String driver = "org.postgresql.Driver";

    // COSTRUTTORE
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);

        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        } else if (instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

}