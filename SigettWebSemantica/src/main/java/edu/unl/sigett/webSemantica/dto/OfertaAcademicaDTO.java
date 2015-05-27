/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jorge-luis
 */
public class OfertaAcademicaDTO implements Serializable {

    private Long id;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;
    private PeriodoAcademicoDTO periodoAcademicoDTO;

    public OfertaAcademicaDTO(Long id, String nombre, Date fechaInicio, Date fechaFin, PeriodoAcademicoDTO periodoAcademicoDTO) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.periodoAcademicoDTO = periodoAcademicoDTO;
    }

    public OfertaAcademicaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public PeriodoAcademicoDTO getPeriodoAcademicoDTO() {
        return periodoAcademicoDTO;
    }

    public void setPeriodoAcademicoDTO(PeriodoAcademicoDTO periodoAcademicoDTO) {
        this.periodoAcademicoDTO = periodoAcademicoDTO;
    }

}
