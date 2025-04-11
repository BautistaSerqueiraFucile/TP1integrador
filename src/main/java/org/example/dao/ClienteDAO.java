package org.example.dao;

import java.sql.Connection;

public class ClienteDAO {
    private Connection conn;

    public ClienteDAO(Connection conn) {this.conn = conn;}
}
