package org.example.dao;

import org.example.entities.Factura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    private final Connection conn;

    public FacturaDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Factura factura) {
        String sql = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, factura.getIdFactura());
            ps.setInt(2, factura.getIdCliente());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Factura find(int idFactura) {
        String sql = "SELECT * FROM Factura WHERE idFactura = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Factura(
                            rs.getInt("idFactura"),
                            rs.getInt("idCliente")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Factura> findAll() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM Factura";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                facturas.add(new Factura(
                        rs.getInt("idFactura"),
                        rs.getInt("idCliente")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturas;
    }

    public boolean delete(int idFactura) {
        String sql = "DELETE FROM Factura WHERE idFactura = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            int rows = ps.executeUpdate();
            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Factura factura) {
        String sql = "UPDATE Factura SET idCliente = ? WHERE idFactura = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, factura.getIdCliente());
            ps.setInt(2, factura.getIdFactura());
            int rows = ps.executeUpdate();
            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
