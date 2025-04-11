package org.example.dao;

import java.sql.Connection;

public class ProductoDAO {
    private Connection conn;

    public ProductoDAO(Connection conn) {this.conn = conn;}
}
