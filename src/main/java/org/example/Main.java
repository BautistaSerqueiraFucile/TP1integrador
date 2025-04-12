package org.example;

import org.example.dao.ClienteDAO;
import org.example.dao.ProductoDAO;
import org.example.factory.AbstractFactory;
import org.example.dto.Producto_RecaudacionDTO;
import org.example.dto.Cliente_FacturacionDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        /*HelperMySQL dbMySQL = new HelperMySQL();
        dbMySQL.dropTables();
        dbMySQL.createTables();
        dbMySQL.cargarDB();
        dbMySQL.closeConnection();
*/
        AbstractFactory SQLFactory = AbstractFactory.getDAOFactory(1);

        ProductoDAO productoDAO = SQLFactory.getProductoDAO();

        Producto_RecaudacionDTO recaudacionDTO = productoDAO.getMostProfitable();
        System.out.println(recaudacionDTO);

        try (Connection conn = org.example.factory.MySQLDAOFactory.createConnection()) {

            //Clientes ordenados por facturación total
            String cliQuery = """
                SELECT c.nombre, SUM(fp.cantidad * p.valor) AS total_facturado
                FROM Factura f
                JOIN Cliente c ON f.idCliente = c.idCliente
                JOIN Factura_Producto fp ON f.idFactura = fp.idFactura
                JOIN Producto p ON fp.idProducto = p.idProducto
                GROUP BY c.idCliente
                ORDER BY total_facturado DESC
            """;
            List<Cliente_FacturacionDTO> clientes = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(cliQuery);
                 ResultSet rs = ps.executeQuery()) {
                System.out.println("\nClientes ordenados por facturación:");
                while (rs.next()) {
                    clientes.add(new Cliente_FacturacionDTO(
                            rs.getString("nombre"),
                            rs.getFloat("total_facturado")
                    ));
                }
            }
            clientes.forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}