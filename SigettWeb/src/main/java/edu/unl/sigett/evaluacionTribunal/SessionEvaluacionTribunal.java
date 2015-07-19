/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.evaluacionTribunal;

import com.jlmallas.comun.entity.Item;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.RangoNota;
import edu.unl.sigett.miembroTribunal.MiembroTribunalDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionEvaluacionTribunal implements Serializable {

    private EvaluacionTribunal evaluacionTribunal;
    private Item equivalencia;
    private List<EvaluacionTribunal> evaluacionesTribunal;
    private List<MiembroTribunalDTO> miembrosTribunalDTO;
    private List<RangoNota> rangoNotas;

    private ScheduleModel eventModel;

    private Date fechaInicioSeleccionada;
    private Date fechaFinSeleccionada;

    private Boolean renderedCrear;
    private Boolean renderedEditar;
    private Boolean renderedEliminar;
    private Boolean renderedDlgCrud;
    private Boolean renderedCalificarPublica;
    private Boolean renderedCalificarPrivada;

    public SessionEvaluacionTribunal() {
        eventModel = new DefaultScheduleModel();
        this.miembrosTribunalDTO = new ArrayList<>();
        this.evaluacionesTribunal = new ArrayList<>();
        this.rangoNotas = new ArrayList<>();
        this.evaluacionTribunal = new EvaluacionTribunal();
    }

    public EvaluacionTribunal getEvaluacionTribunal() {
        return evaluacionTribunal;
    }

    public void setEvaluacionTribunal(EvaluacionTribunal evaluacionTribunal) {
        this.evaluacionTribunal = evaluacionTribunal;
    }

    public Boolean getRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(Boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public Boolean getRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(Boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public Boolean getRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(Boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public Date getFechaInicioSeleccionada() {
        return fechaInicioSeleccionada;
    }

    public void setFechaInicioSeleccionada(Date fechaInicioSeleccionada) {
        this.fechaInicioSeleccionada = fechaInicioSeleccionada;
    }

    public Date getFechaFinSeleccionada() {
        return fechaFinSeleccionada;
    }

    public void setFechaFinSeleccionada(Date fechaFinSeleccionada) {
        this.fechaFinSeleccionada = fechaFinSeleccionada;
    }

    public Boolean getRenderedDlgCrud() {
        return renderedDlgCrud;
    }

    public void setRenderedDlgCrud(Boolean renderedDlgCrud) {
        this.renderedDlgCrud = renderedDlgCrud;
    }

    public Boolean getRenderedCalificarPublica() {
        return renderedCalificarPublica;
    }

    public void setRenderedCalificarPublica(Boolean renderedCalificarPublica) {
        this.renderedCalificarPublica = renderedCalificarPublica;
    }

    public Boolean getRenderedCalificarPrivada() {
        return renderedCalificarPrivada;
    }

    public void setRenderedCalificarPrivada(Boolean renderedCalificarPrivada) {
        this.renderedCalificarPrivada = renderedCalificarPrivada;
    }

    public List<EvaluacionTribunal> getEvaluacionesTribunal() {
        return evaluacionesTribunal;
    }

    public void setEvaluacionesTribunal(List<EvaluacionTribunal> evaluacionesTribunal) {
        this.evaluacionesTribunal = evaluacionesTribunal;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public List<RangoNota> getRangoNotas() {
        return rangoNotas;
    }

    public void setRangoNotas(List<RangoNota> rangoNotas) {
        this.rangoNotas = rangoNotas;
    }

    public Item getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(Item equivalencia) {
        this.equivalencia = equivalencia;
    }

    public List<MiembroTribunalDTO> getMiembrosTribunalDTO() {
        return miembrosTribunalDTO;
    }

    public void setMiembrosTribunalDTO(List<MiembroTribunalDTO> miembrosTribunalDTO) {
        this.miembrosTribunalDTO = miembrosTribunalDTO;
    }

}
