package com.example.sitol.tibe.objetos;

public class ObjCita {
 private String fecha_reser;
 private Double latitud;
    private Double longitud;
    private String nombreGuia;
    private String apePatGuia;
    private String apeMateGuia;

    private String nombreTurista;
    private String apePatTurista;
    private String apeMateTurista;

   private String id_guia;
   private String id_turista;
   private String estado;
   private String hora;

   private String id_cita;

    public ObjCita( String id_cita,String fecha_reser, Double latitud, Double longitud, String nombreGuia, String apePatGuia, String apeMAteGuia, String nombreTurista, String apePatTurista, String apeMAteTurista, String id_guia, String id_turista, String estado,String hora) {
        this.fecha_reser = fecha_reser;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreGuia = nombreGuia;
        this.apePatGuia = apePatGuia;
        this.apeMateGuia = apeMAteGuia;
        this.nombreTurista = nombreTurista;
        this.apePatTurista = apePatTurista;
        this.apeMateTurista = apeMAteTurista;
        this.id_guia = id_guia;
        this.id_turista = id_turista;
        this.estado = estado;
        this.id_cita = id_cita;
        this.hora=hora;
    }

    public ObjCita() {
    }

    public String getFecha_reser() {
        return fecha_reser;
    }

    public void setFecha_reser(String fecha_reser) {
        this.fecha_reser = fecha_reser;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getId_turista() {
        return id_turista;
    }


    public String getNombreGuia() {
        return nombreGuia;
    }

    public void setNombreGuia(String nombreGuia) {
        this.nombreGuia = nombreGuia;
    }

    public String getApePatGuia() {
        return apePatGuia;
    }

    public void setApePatGuia(String apePatGuia) {
        this.apePatGuia = apePatGuia;
    }

    public String getApeMateGuia() {
        return apeMateGuia;
    }

    public void setApeMateGuia(String apeMateGuia) {
        this.apeMateGuia = apeMateGuia;
    }


    public String getId_guia() {
        return id_guia;
    }

    public void setId_guia(String id_guia) {
        this.id_guia = id_guia;
    }

    public void setId_turista(String id_turista) {
        this.id_turista = id_turista;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId_cita() {
        return id_cita;
    }

    public void setId_cita(String id_cita) {
        this.id_cita = id_cita;
    }

    public String getNombreTurista() {
        return nombreTurista;
    }

    public void setNombreTurista(String nombreTurista) {
        this.nombreTurista = nombreTurista;
    }

    public String getApePatTurista() {
        return apePatTurista;
    }

    public void setApePatTurista(String apePatTurista) {
        this.apePatTurista = apePatTurista;
    }

    public String getApeMateTurista() {
        return apeMateTurista;
    }

    public void setApeMateTurista(String apeMateTurista) {
        this.apeMateTurista = apeMateTurista;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
