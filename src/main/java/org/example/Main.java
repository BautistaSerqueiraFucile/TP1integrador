package org.example;

import org.example.dao.ClienteDAO;
import org.example.dao.ProductoDAO;
import org.example.factory.AbstractFactory;
import org.example.dto.Producto_RecaudacionDTO;
import org.example.dto.Cliente_FacturacionDTO;
import org.example.utils.HelperMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {

        HelperMySQL dbMySQL = new HelperMySQL();
        dbMySQL.dropTables();
        dbMySQL.createTables();
        dbMySQL.cargarDB();

        System.out.println("///////////////////////////////////////////////");

        AbstractFactory SQLFactory = AbstractFactory.getDAOFactory(1);

        ProductoDAO productoDAO = SQLFactory.getProductoDAO();

        Producto_RecaudacionDTO recaudacionDTO = productoDAO.getMostProfitable();

        System.out.println(recaudacionDTO);

        System.out.println("///////////////////////////////////////////////");

        System.out.println("Clientes ordenados por total facturado:");

        ClienteDAO clienteDAO = SQLFactory.getClienteDAO();

        List<Cliente_FacturacionDTO> facturacionDTO = clienteDAO.getClienteFacturacion();

        for (Cliente_FacturacionDTO clienteDTO : facturacionDTO) {
            System.out.println(clienteDTO);
        }

        dbMySQL.closeConnection();

    }


}