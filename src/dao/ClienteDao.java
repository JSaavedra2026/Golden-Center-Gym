package dao;

import config.ConexionBD;
import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    // CREAR
    public boolean insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombre_completo, documento, correo, telefono, estado) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEstado());
            return ps.executeUpdate() > 0;
        }
    }

    // LEER: todos los clientes
    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(convertirCliente(rs));
            }
        }
        return clientes;
    }

    // LEER: buscar por documento o nombre
    public List<Cliente> buscar(String texto) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE documento = ? OR nombre_completo LIKE ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, texto);
            ps.setString(2, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(convertirCliente(rs));
                }
            }
        }
        return clientes;
    }

    // ACTUALIZAR
    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre_completo = ?, documento = ?, correo = ?, "
                   + "telefono = ? WHERE id_cliente = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setInt(5, cliente.getIdCliente());
            return ps.executeUpdate() > 0;
        }
    }

    // "ELIMINAR": desactivar sin borrar el historial
    public boolean desactivar(int idCliente) throws SQLException {
        String sql = "UPDATE cliente SET estado = 'Inactivo' WHERE id_cliente = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        }
    }

    // Convierte una fila de la base de datos en un objeto Cliente
    private Cliente convertirCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("nombre_completo"),
                rs.getString("documento"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("estado"));
    }
}