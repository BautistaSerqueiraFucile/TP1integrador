package org.example;

import org.example.utils.HelperMySQL;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        HelperMySQL dbMySQL = new HelperMySQL();
        dbMySQL.dropTables();
        dbMySQL.createTables();
        dbMySQL.cargarDB();
        dbMySQL.closeConnection();
    }
}