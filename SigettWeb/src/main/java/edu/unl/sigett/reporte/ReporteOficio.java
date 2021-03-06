/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reporte;

import java.io.Serializable;
import org.jlmallas.util.Oficio;

/**
 *
 * @author jorge-luis
 */
public class ReporteOficio extends Oficio implements Serializable {

    private byte[] logoCarrera;
    private String rutaLogoIntitucion;
    private String nombreCarrera;
    private String nombreArea;
    private String carreraSigla;
    private String etiquetaNO;
    private String cabecera1;
    private String cabecera2;
    private String ruta;
    private String tipoArchivo;
    private String footer;

    public ReporteOficio(byte[] logoCarrera, String rutaLogoIntitucion, String nombreCarrera, String nombreArea, String carreraSigla,
            String etiquetaNO, String cabecera1, String cabecera2, String lugar, String fecha, String numeracion, String cargoDestinatario, String destinatario,
            String cargoQuienFirma, String datosQuienFirma, String cuerpo, String referencia, String asunto, String despedida, String saludo, String tipoArchivo,
            String footer, String ruta) {
        super(lugar, fecha, numeracion, cargoDestinatario, destinatario, cargoQuienFirma, datosQuienFirma, cuerpo, referencia, asunto, despedida, saludo);
        this.logoCarrera = logoCarrera;
        this.rutaLogoIntitucion = rutaLogoIntitucion;
        this.nombreCarrera = nombreCarrera;
        this.nombreArea = nombreArea;
        this.carreraSigla = carreraSigla;
        this.etiquetaNO = etiquetaNO;
        this.cabecera1 = cabecera1;
        this.cabecera2 = cabecera2;
        this.ruta = ruta;
        this.tipoArchivo = tipoArchivo;
        this.footer = footer;
    }

    public ReporteOficio(byte[] logoCarrera, String rutaLogoIntitucion, String nombreCarrera, String nombreArea, String carreraSigla,
            String etiquetaNO, String cabecera1, String cabecera2) {
        this.logoCarrera = logoCarrera;
        this.rutaLogoIntitucion = rutaLogoIntitucion;
        this.nombreCarrera = nombreCarrera;
        this.nombreArea = nombreArea;
        this.carreraSigla = carreraSigla;
        this.etiquetaNO = etiquetaNO;
        this.cabecera1 = cabecera1;
        this.cabecera2 = cabecera2;
    }

    public byte[] getLogoCarrera() {
        return logoCarrera;
    }

    public void setLogoCarrera(byte[] logoCarrera) {
        this.logoCarrera = logoCarrera;
    }

    public String getRutaLogoIntitucion() {
        return rutaLogoIntitucion;
    }

    public void setRutaLogoIntitucion(String rutaLogoIntitucion) {
        this.rutaLogoIntitucion = rutaLogoIntitucion;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }

    public String getCarreraSigla() {
        return carreraSigla;
    }

    public void setCarreraSigla(String carreraSigla) {
        this.carreraSigla = carreraSigla;
    }

    public String getEtiquetaNO() {
        return etiquetaNO;
    }

    public void setEtiquetaNO(String etiquetaNO) {
        this.etiquetaNO = etiquetaNO;
    }

    public String getCabecera1() {
        return cabecera1;
    }

    public void setCabecera1(String cabecera1) {
        this.cabecera1 = cabecera1;
    }

    public String getCabecera2() {
        return cabecera2;
    }

    public void setCabecera2(String cabecera2) {
        this.cabecera2 = cabecera2;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

}
