package model;

public class Reservaciones {
    private String dia;
    private String mes;
    private String hora;
    private String cantidadPersonas;

    // Constructores
    public Reservaciones(String dia, String hora, String mes, String cantidadPersonas) {
        this.dia              = dia;
        this.hora             = hora;
        this.cantidadPersonas = cantidadPersonas;
        this.mes              = mes;
    }

    public Reservaciones() {
        this.dia              = "12";
        this.mes              = "12";
        this.hora             = "12";
        this.cantidadPersonas = "12";
    }

    // Getters y Setters

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(String cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }
}
