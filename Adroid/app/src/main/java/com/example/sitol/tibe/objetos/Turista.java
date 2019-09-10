package com.example.sitol.tibe.objetos;

public class Turista {

    private String idTurista;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String contraseña;
    private String fecha_naciemiento;
    private String telefono;

    public Turista(String idTurista, String nombre, String apellidoPaterno, String apellidoMaterno, String correo, String contraseña, String fecha_naciemiento, String telefono, String sesion) {
        this.idTurista = idTurista;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.contraseña = contraseña;
        this.fecha_naciemiento = fecha_naciemiento;
        this.telefono = telefono;
        this.sesion = sesion;
    }

    String sesion;

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }


    public Turista() {
    }

    public String getIdTurista() {
        return idTurista;
    }

    public void setIdTurista(String idTurista) {
        this.idTurista = idTurista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getFecha_naciemiento() {
        return fecha_naciemiento;
    }

    public void setFecha_naciemiento(String fecha_naciemiento) {
        this.fecha_naciemiento = fecha_naciemiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
