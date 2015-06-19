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
    private List<DocumentoActividadDTO> filterDocumentosActividadDTO;
    private List<Actividad> actividadesPadre;

    private Boolean renderedCrud;

    private String fechaInicioLimite;
    private String fechaFinLimite;

    private String titulo;
    private String tarea;
    private String objetivo;

    public SessionActividad() {
        this.tarea = TipoActividadEnum.TAREA.getTipo();
        this.objetivo = TipoActividadEnum.OBJETIVO.getTipo();
        this.actividadesPadre = new ArrayList<>();
        this.actividad = new Actividad();
        this.documentosActividadDTO = new ArrayList<>();
        this.filterDocumentosActividadDTO=new ArrayList<>();
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

    public TreeNode getRootActividades() {
        return rootActividades;
    }

    public void setRootActividades(TreeNode rootActividades) {
        this.rootActividades = rootActividades;
    }

    public List<Actividad> getActividadesPadre() {
        return actividadesPadre;
    }

    public void setActividadesPadre(List<Actividad> actividadesPadre) {
        this.actividadesPadre = actividadesPadre;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFechaInicioLimite() {
        return fechaInicioLimite;
    }

    public void setFechaInicioLimite(String fechaInicioLimite) {
        this.fechaInicioLimite = fechaInicioLimite;
    }

    public String getFechaFinLimite() {
        return fechaFinLimite;
    }

    public void setFechaFinLimite(String fechaFinLimite) {
        this.fechaFinLimite = fechaFinLimite;
    }

    public List<DocumentoActividadDTO> getFilterDocumentosActividadDTO() {
        return filterDocumentosActividadDTO;
    }

    public void setFilterDocumentosActividadDTO(List<DocumentoActividadDTO> filterDocumentosActividadDTO) {
        this.filterDocumentosActividadDTO = filterDocumentosActividadDTO;
    }

}
