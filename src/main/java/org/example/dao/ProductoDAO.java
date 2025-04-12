package org.example.dao;

import org.example.dto.Producto_RecaudacionDTO;
import org.example.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private final Connection conn;

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Producto producto) {
        String sql = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setFloat(3, producto.getValor());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Producto find(int idProducto) {
        String sql = "SELECT * FROM Producto WHERE idProducto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("idProducto"),
                            rs.getString("nombre"),
                            rs.getFloat("valor")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getFloat("valor")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    public boolean delete(int idProducto) {
        String sql = "DELETE FROM Producto WHERE idProducto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            int rows = ps.executeUpdate();
            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Producto producto) {
        String sql = "UPDATE Producto SET nombre = ?, valor = ? WHERE idProducto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setFloat(2, producto.getValor());
            ps.setInt(3, producto.getIdProducto());
            int rows = ps.executeUpdate();
            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Producto_RecaudacionDTO getMostProfitable(){
        //Producto que más recaudó
        String sql = """
                SELECT p.nombre, SUM(fp.cantidad * p.valor) AS recaudacion
                FROM Factura_Producto fp
                JOIN Producto p ON fp.idProducto = p.idProducto
                GROUP BY p.idProducto
                ORDER BY recaudacion DESC
                LIMIT 1
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Producto_RecaudacionDTO(
                        rs.getString("nombre"),
                        rs.getFloat("recaudacion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
