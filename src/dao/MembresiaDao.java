package dao;

import config.ConexionBD;
import model.Membresia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembresiaDao {

    // CREAR: calcula la fecha de vencimiento según la duración (en días)
    public boolean insertar(Membresia membresia) throws SQLException {
        String sql = "INSERT INTO membresia (fecha_inicio, fecha_vencimiento, duracion, costo, "
                   + "tipo_membresia, estado, id_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)";
        membresia.setFechaVencimiento(
                membresia.getFechaInicio().plusDays(membresia.getDuracion()));
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(2, Date.valueOf(membresia.getFechaVencimiento()));
            ps.setInt(3, membresia.getDuracion());
            ps.setDouble(4, membresia.getCosto());
            ps.setString(5, membresia.getTipoMembresia());
            ps.setString(6, membresia.getEstado());
            ps.setInt(7, membresia.getIdCliente());
            return ps.executeUpdate() > 0;
        }
    }

    // LEER: todas las membresías
    public List<Membresia> listar() throws SQLException {
        List<Membresia> membresias = new ArrayList<>();
        String sql = "SELECT * FROM membresia";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                membresias.add(convertirMembresia(rs));
            }
        }
        return membresias;
    }

    // LEER: membresías de un cliente
    public List<Membresia> buscarPorCliente(int idCliente) throws SQLException {
        List<Membresia> membresias = new ArrayList<>();
        String sql = "SELECT * FROM membresia WHERE id_cliente = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    membresias.add(convertirMembresia(rs));
                }
            }
        }
        return membresias;
    }

    // ACTUALIZAR: cambia tipo, duración y costo, y recalcula el vencimiento
    public boolean actualizar(Membresia membresia) throws SQLException {
        String sql = "UPDATE membresia SET tipo_membresia = ?, duracion = ?, costo = ?, "
                   + "fecha_vencimiento = DATE_ADD(fecha_inicio, INTERVAL ? DAY) "
                   + "WHERE id_membresia = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, membresia.getTipoMembresia());
            ps.setInt(2, membresia.getDuracion());
            ps.setDouble(3, membresia.getCosto());
            ps.setInt(4, membresia.getDuracion());
            ps.setInt(5, membresia.getIdMembresia());
            return ps.executeUpdate() > 0;
        }
    }

    // "ELIMINAR": desactivar sin borrar el historial
    public boolean desactivar(int idMembresia) throws SQLException {
        String sql = "UPDATE membresia SET estado = 'Inactiva' WHERE id_membresia = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idMembresia);
            return ps.executeUpdate() > 0;
        }
    }

    // VALIDAR: marca como vencidas las membresías activas cuya fecha ya pasó
    public int actualizarVencidas() throws SQLException {
        String sql = "UPDATE membresia SET estado = 'Vencida' "
                   + "WHERE estado = 'Activa' AND fecha_vencimiento < CURDATE()";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }

    // Convierte una fila de la base de datos en un objeto Membresia
    private Membresia convertirMembresia(ResultSet rs) throws SQLException {
        return new Membresia(
                rs.getInt("id_membresia"),
                rs.getString("tipo_membresia"),
                rs.getInt("duracion"),
                rs.getDouble("costo"),
                rs.getDate("fecha_inicio").toLocalDate(),
                rs.getDate("fecha_vencimiento").toLocalDate(),
                rs.getString("estado"),
                rs.getInt("id_cliente"));
    }
}