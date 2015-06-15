/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.actividad;

import edu.unl.sigett.documentoActividad.DocumentoActividadDTO;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.enumeration.TipoActividadEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.TreeNode;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionActividad implements Serializable {

    private Actividad actividad;

    private TreeNode rootActividades;

    private List<DocumentoActividadDTO> documentosActividadDTO;
    private List<DocumentoActividadDTO> documentosActividadEliminadosDTO;
    private List<Actividad> actividades;

    private Boolean renderedCrud;

    private String tarea;
    private String objetivo;

    public SessionActividad() {
        this.tarea = TipoActividadEnum.TAREA.getTipo();
        this.objetivo = TipoActividadEnum.OBJETIVO.getTipo();
        this.actividades = new ArrayList<>();
        this.actividad = new Actividad();
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

    public List<DocumentoActividadDTO> getDocumentosActividadEliminadosDTO() {
        return documentosActividadEliminadosDTO;
    }

    public void setDocumentosActividadEliminadosDTO(List<DocumentoActividadDTO> documentosActividadEliminadosDTO) {
        this.documentosActividadEliminadosDTO = documentosActividadEliminadosDTO;
    }

    public TreeNode getRootActividades() {
        return rootActividades;
    }

    public void setRootActividades(TreeNode rootActividades) {
        this.rootActividades = rootActividades;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public Boolean getRenderedCrud() {
        return renderedCrud;
    }

    public void setRenderedCrud(Boolean renderedCrud) {
        this.renderedCrud = renderedCrud;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

}
