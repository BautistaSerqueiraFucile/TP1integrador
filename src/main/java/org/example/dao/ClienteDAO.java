package org.example.dao;

import org.example.dto.Cliente_FacturacionDTO;
import org.example.entities.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private final Connection conn;

    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Cliente cliente) {
        String sql = "INSERT INTO Cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliente.getIdCliente());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getEmail());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente find(int id) {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("idCliente"),
                            rs.getString("nombre"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("email")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Cliente cliente) {
        String sql = "UPDATE Cliente SET nombre = ?, email = ? WHERE idCliente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setInt(3, cliente.getIdCliente());
            int rows = ps.executeUpdate();
            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Cliente_FacturacionDTO> getClienteFacturacion() {

        List<Cliente_FacturacionDTO> clientes = new ArrayList<>();
        //Clientes ordenados por facturación total
        String sql = """
                    SELECT c.nombre, SUM(fp.cantidad * p.valor) AS total_facturado
                    FROM Factura f
                    JOIN Cliente c ON f.idCliente = c.idCliente
                    JOIN Factura_Producto fp ON f.idFactura = fp.idFactura
                    JOIN Producto p ON fp.idProducto = p.idProducto
                    GROUP BY c.idCliente
                    ORDER BY total_facturado DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(new Cliente_FacturacionDTO(
                            rs.getString("nombre"),
                            rs.getFloat("total_facturado")
                    ));
                }
            }
         catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
}
