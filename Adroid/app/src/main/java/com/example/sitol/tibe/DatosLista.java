package com.example.sitol.tibe;

import com.example.sitol.tibe.objetos.Guia;
import com.example.sitol.tibe.objetos.ObjCita;

/**
 * Created by sitol on 20/11/2017.
 */

public class DatosLista {
    private String titulo;
    private  String subtitulo;
    private int imagen;
    private Guia lista;
    private ObjCita lista2;

    public DatosLista(String titulo, String subtitulo, int imagen, Guia lista) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagen = imagen;
        this.lista=lista;
    }
    public DatosLista(String titulo, String subtitulo, int imagen, ObjCita lista) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagen = imagen;
        this.lista2=lista;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public Guia getLista() {
        return lista;
    }

    public void setLista(Guia lista) {
        this.lista = lista;
    }

    public ObjCita getLista2() {
        return lista2;
    }

    public void setLista2(ObjCita lista2) {
        this.lista2 = lista2;
    }
}
