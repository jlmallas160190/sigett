/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.actividad;

import edu.unl.sigett.documentoActividad.DocumentoActividadDTO;
import edu.unl.sigett.entity.Actividad;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionActividad implements Serializable {

    private Actividad actividad;

    private List<DocumentoActividadDTO> documentosActividadDTO;
    private List<DocumentoActividadDTO> documentosActividadEliminadosDTO;
    private List<Actividad> actividades;
    private List<Actividad> filterActividades;

    public SessionActividad() {
        this.filterActividades = new ArrayList<>();
        this.actividad = new Actividad();
        this.actividades = new ArrayList<>();
        this.documentosActividadEliminadosDTO = new ArrayList<>();
        this.documentosActividadDTO = new ArrayList<>();
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public List<DocumentoActividadDTO> getDocumentosActividadDTO() {
        return documentosActividadDTO;
    }

    public void setDocumentosActividadDTO(List<DocumentoActividadDTO> documentosActividadDTO) {
        this.documentosActividadDTO = documentosActividadDTO;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public List<DocumentoActividadDTO> getDocumentosActividadEliminadosDTO() {
        return documentosActividadEliminadosDTO;
    }

    public void setDocumentosActividadEliminadosDTO(List<DocumentoActividadDTO> documentosActividadEliminadosDTO) {
        this.documentosActividadEliminadosDTO = documentosActividadEliminadosDTO;
    }

    public List<Actividad> getFilterActividades() {
        return filterActividades;
    }

    public void setFilterActividades(List<Actividad> filterActividades) {
        this.filterActividades = filterActividades;
    }

}
