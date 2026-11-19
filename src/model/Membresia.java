package model;

import java.time.LocalDate;

public class Membresia {

    private int idMembresia;
    private String tipoMembresia;
    private int duracion;
    private double costo;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private String estado;
    private int idCliente;

    public Membresia() {
    }

    public Membresia(int idMembresia, String tipoMembresia, int duracion, double costo,
                     LocalDate fechaInicio, LocalDate fechaVencimiento,
                     String estado, int idCliente) {
        this.idMembresia = idMembresia;
        this.tipoMembresia = tipoMembresia;
        this.duracion = duracion;
        this.costo = costo;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    public int getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(int idMembresia) {
        this.idMembresia = idMembresia;
    }

    public String getTipoMembresia() {
        return tipoMembresia;
    }

    public void setTipoMembresia(String tipoMembresia) {
        this.tipoMembresia = tipoMembresia;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}