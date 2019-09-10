package com.example.sitol.tibe.objetos;

import java.util.concurrent.TransferQueue;

public class Guia {

    private String nombre;
    private String apPat;
    private String apMat;
    private String correo;
    private String estado;
    private String tipo;
    private String desc;
    private String acerca;
    private String zona;

    private String telefono;
    private String tarifa;
    private String idG;

    private String actividades;
            private String idioma;
    private int img;
    private String sesion;

    public Guia(String nombre, String apPat, String apMat, String correo, String estado, String tipo, String desc, String telefono, String tarifa, String acerca, String zona, String idG, int img, String idioma,String actividades,String sesion) {
        this.nombre = nombre;
        this.apPat = apPat;
        this.apMat = apMat;
        this.correo = correo;
        this.estado = estado;
        this.tipo = tipo;
        this.desc = desc;
        this.telefono = telefono;
        this.tarifa = tarifa;
        this.acerca=acerca;
        this.zona=zona;
        this.idG=idG;
        this.img=img;
        this.actividades=actividades;
        this.idioma=idioma;
        this.sesion=sesion;
    }
    public Guia(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPat() {
        return apPat;
    }

    public void setApPat(String apPat) {
        this.apPat = apPat;
    }

    public String getApMat() {
        return apMat;
    }

    public void setApMat(String apMat) {
        this.apMat = apMat;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String  getTelefono() {
        return telefono;
    }

    public void setTelefono(String  telefono) {
        this.telefono = telefono;
    }

    public String  getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getAcerca() {
        return acerca;
    }

    public void setAcerca(String acerca) {
        this.acerca = acerca;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getIdG() {
        return idG;
    }

    public void setIdG(String idG) {
        this.idG = idG;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }
}
