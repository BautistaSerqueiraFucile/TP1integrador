package org.example.dao;

import org.example.dto.Producto_RecaudacionDTO;
import org.example.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Factura_ProductoDAO {
    private final Connection conn;

    public Factura_ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(int idFactura, int idProducto, int cantidad) {
        String sql = "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getProductosPorFactura(int idFactura) {
        List<Integer> productos = new ArrayList<>();
        String sql = "SELECT idProducto FROM Factura_Producto WHERE idFactura = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(rs.getInt("idProducto"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    public int getCantidad(int idFactura, int idProducto) {
        String sql = "SELECT cantidad FROM Factura_Producto WHERE idFactura = ? AND idProducto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ps.setInt(2, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean delete(int idFactura, int idProducto) {
        String sql = "DELETE FROM Factura_Producto WHERE idFactura = ? AND idProducto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ps.setInt(2, idProducto);
            int rows = ps.executeUpdate();
            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCantidad(int idFactura, int idProducto, int nuevaCantidad) {
        String sql = "UPDATE Factura_Producto SET cantidad = ? WHERE idFactura = ? AND idProducto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idFactura);
            ps.setInt(3, idProducto);
            int rows = ps.executeUpdate();
            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
