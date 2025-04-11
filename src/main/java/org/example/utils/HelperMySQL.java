package org.example.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.dao.Factura_ProductoDAO;
import org.example.entities.Cliente;
import org.example.entities.Factura;
import org.example.entities.Factura_Producto;
import org.example.entities.Producto;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelperMySQL {
    private Connection conn = null;

    public HelperMySQL() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String uri = "jdbc:mysql://localhost:3306/DBintegrador1";

        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            conn = DriverManager.getConnection(uri, "root", "");
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (conn != null){
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dropTables() throws SQLException {
        String dropFactura_Producto = "DROP TABLE IF EXISTS Factura_Producto";
        this.conn.prepareStatement(dropFactura_Producto).execute();
        this.conn.commit();

        String dropFactura = "DROP TABLE IF EXISTS Factura";
        this.conn.prepareStatement(dropFactura).execute();
        this.conn.commit();

        String dropCliente = "DROP TABLE IF EXISTS Cliente";
        this.conn.prepareStatement(dropCliente).execute();
        this.conn.commit();

        String dropProducto = "DROP TABLE IF EXISTS Producto";
        this.conn.prepareStatement(dropProducto).execute();
        this.conn.commit();
    }

    public void createTables() throws SQLException {
        String tableCliente = "CREATE TABLE IF NOT EXISTS Cliente(" +
                "idCliente INT NOT NULL, " +
                "nombre VARCHAR(500), " +
                "mail VARCHAR(150), " +
                "CONSTRAINT Cliente_pk PRIMARY KEY (idCliente));" ;
        this.conn.prepareStatement(tableCliente).execute();
        this.conn.commit();

        String tableProducto = "CREATE TABLE IF NOT EXISTS Producto(" +
                "idProducto INT NOT NULL, " +
                "nombre VARCHAR(45), " +
                "valor FLOAT," +
                "CONSTRAINT Producto_pk PRIMARY KEY (idProducto))";
        this.conn.prepareStatement(tableProducto).execute();
        this.conn.commit();

        String tableFactura = "CREATE TABLE IF NOT EXISTS Factura(" +
                "idFactura INT NOT NULL, " +
                "idCliente INT NOT NULL, " +
                "CONSTRAINT Factura_pk PRIMARY KEY (idFactura), " +
                "CONSTRAINT FK_idCliente FOREIGN KEY (idCliente) REFERENCES Cliente (idCliente))";
        this.conn.prepareStatement(tableFactura).execute();
        this.conn.commit();


        String tableFactura_Producto = "CREATE TABLE IF NOT EXISTS Factura_Producto(" +
                "idFactura INT NOT NULL," +
                "idProducto INT NOT NULL, " +
                "cantidad INT NOT NULL, " +
                "CONSTRAINT Factura_Producto_pk PRIMARY KEY (idFactura, idProducto), " +
                "CONSTRAINT FK_Factura FOREIGN KEY (idFactura) REFERENCES Factura (idFactura), " +
                "CONSTRAINT FK_Producto FOREIGN KEY (idProducto) REFERENCES Producto (idProducto))";
        this.conn.prepareStatement(tableFactura_Producto).execute();
        this.conn.commit();

    }

    private void insertCliente(Cliente cliente, Connection conn) throws SQLException {
        String insert = "INSERT INTO Cliente (idCliente ,nombre, mail) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(insert);
            ps.setInt(1, cliente.getIdCliente());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getEmail());
            if(ps.executeUpdate() == 0) {
                throw new SQLException("No se pudo insertar cliente");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closePsAndCommit(conn, ps);
        }
    }

    private void insertProducto(Producto producto, Connection conn) throws SQLException {
        String insert = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(insert);
            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setFloat(3, producto.getValor());
            if(ps.executeUpdate() == 0) {
                throw new SQLException("No se pudo insertar producto");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closePsAndCommit(conn, ps);
        }
    }

    private void insertFactura(Factura factura, Connection conn) throws SQLException {
        String insert = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(insert);
            ps.setInt(1, factura.getIdFactura());
            ps.setInt(2, factura.getIdCliente());
            if(ps.executeUpdate() == 0) {
                throw new SQLException("No se pudo insertar factura");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closePsAndCommit(conn, ps);
        }
    }

    private void insertFactura_Producto(Factura_Producto factura_producto, Connection conn) throws SQLException {
        String insert = "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ? ,?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(insert);
            ps.setInt(1, factura_producto.getIdFactura());
            ps.setInt(2, factura_producto.getIdProducto());
            ps.setInt(3, factura_producto.getCantidad());
            if(ps.executeUpdate() == 0) {
                throw new SQLException("No se pudo insertar la relacion de factura y producto");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closePsAndCommit(conn, ps);
        }
    }

    private void closePsAndCommit(Connection conn, PreparedStatement ps) {
        if (conn != null){
            try {
                ps.close();
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "src\\main\\resources\\" + archivo;
        Reader in = new FileReader(path);
        CSVParser csvParser = CSVFormat.EXCEL.withHeader().parse(in);

        Iterable<CSVRecord> data = csvParser.getRecords();
        return data;
    }

    public void cargarDB() throws SQLException {
        try{
            System.out.println("Cargando DB...");
            for (CSVRecord linea :getData("clientes.csv")){ //cargado del csv de clientes
                if (linea.size() >= 3){
                    String idClienteString = linea.get(0);
                    if (!idClienteString.isEmpty()){
                        try {
                            int idcliente = Integer.parseInt(idClienteString);
                            Cliente cliente = new Cliente(idcliente, linea.get(1), linea.get(2));
                            insertCliente(cliente, conn);
                        } catch (NumberFormatException e) {
                            System.err.println("Error de formato en datos de cliente: " + e.getMessage());
                        }
                    }
                }
            }

            System.out.println("Clientes insertados");

            for (CSVRecord linea :getData("productos.csv")){ //cargado del csv de productos
                if (linea.size() >= 3){
                    String idProductoString = linea.get(0);
                    String valorString = linea.get(2);
                    if (!idProductoString.isEmpty() && !valorString.isEmpty()){
                        try {
                            int idProducto = Integer.parseInt(idProductoString);
                            int valor = Integer.parseInt(valorString);
                            Producto producto = new Producto(idProducto, linea.get(1), valor );
                            insertProducto(producto, conn);
                        } catch (NumberFormatException e) {
                            System.err.println("Error de formato en datos de producto: " + e.getMessage());
                        }
                    }
                }
            }

            System.out.println("Productos insertados");

            for (CSVRecord linea :getData("facturas.csv")){ //cargado del csv de facturas
                if (linea.size() >= 2){
                    String idFacturaString = linea.get(0);
                    String idClienteString = linea.get(1);
                    if (!idFacturaString.isEmpty() && !idClienteString.isEmpty()){
                        try {
                            int idFactura = Integer.parseInt(idFacturaString);
                            int idProducto = Integer.parseInt(idClienteString);
                            Factura factura = new Factura(idFactura, idProducto);
                            insertFactura(factura, conn);
                        } catch (NumberFormatException e) {
                            System.err.println("Error de formato en datos de factura: " + e.getMessage());
                        }
                    }
                }
            }

            System.out.println("Facturas insertadas");

            for (CSVRecord linea :getData("facturas-productos.csv")){ //cargado de la relacion entre productos y facturas
                if (linea.size() >= 3){
                    String idFacturaString = linea.get(0);
                    String idProductoString = linea.get(1);
                    String CantidadString = linea.get(2);
                    if (!idFacturaString.isEmpty() && !idProductoString.isEmpty() && !CantidadString.isEmpty()){
                        try {
                            int idFactura = Integer.parseInt(idFacturaString);
                            int idProducto = Integer.parseInt(idProductoString);
                            int Cantidad = Integer.parseInt(CantidadString);
                            Factura_Producto producto = new Factura_Producto(idFactura, idProducto, Cantidad);
                            insertFactura_Producto(producto, conn);
                        } catch (NumberFormatException e) {
                            System.err.println("Error de formato en datos de factura/producto: " + e.getMessage());
                        }
                    }
                }
            }

            System.out.println("Relacion entre factura y producto realizada");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
