/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.miembroTribunal;

import com.jlmallas.comun.entity.Item;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.EvaluacionTribunal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionMiembroTribunal")
@SessionScoped
public class SessionMiembroTribunal implements Serializable {

    private MiembroTribunalDTO miembroTribunalDTOSeleccionado;
    private Item cargoSeleccionado;

    private List<MiembroTribunalDTO> miembrosTribunalDTO;
    private List<MiembroTribunalDTO> filterMiembrosTribunalDTO;
    private List<EvaluacionTribunal> evaluacionTribunales;
    private List<CalificacionMiembro> calificacionMiembros;

    public SessionMiembroTribunal() {
        this.calificacionMiembros = new ArrayList<>();
        this.evaluacionTribunales = new ArrayList<>();
        this.miembrosTribunalDTO = new ArrayList<>();
        this.filterMiembrosTribunalDTO = new ArrayList<>();
        this.miembroTribunalDTOSeleccionado = new MiembroTribunalDTO();
    }

    public MiembroTribunalDTO getMiembroTribunalDTOSeleccionado() {
        return miembroTribunalDTOSeleccionado;
    }

    public void setMiembroTribunalDTOSeleccionado(MiembroTribunalDTO miembroTribunalDTOSeleccionado) {
        this.miembroTribunalDTOSeleccionado = miembroTribunalDTOSeleccionado;
    }

    public List<MiembroTribunalDTO> getMiembrosTribunalDTO() {
        return miembrosTribunalDTO;
    }

    public void setMiembrosTribunalDTO(List<MiembroTribunalDTO> miembrosTribunalDTO) {
        this.miembrosTribunalDTO = miembrosTribunalDTO;
    }

    public List<MiembroTribunalDTO> getFilterMiembrosTribunalDTO() {
        return filterMiembrosTribunalDTO;
    }

    public void setFilterMiembrosTribunalDTO(List<MiembroTribunalDTO> filterMiembrosTribunalDTO) {
        this.filterMiembrosTribunalDTO = filterMiembrosTribunalDTO;
    }

    public Item getCargoSeleccionado() {
        return cargoSeleccionado;
    }

    public void setCargoSeleccionado(Item cargoSeleccionado) {
        this.cargoSeleccionado = cargoSeleccionado;
    }

    public List<EvaluacionTribunal> getEvaluacionTribunales() {
        return evaluacionTribunales;
    }

    public void setEvaluacionTribunales(List<EvaluacionTribunal> evaluacionTribunales) {
        this.evaluacionTribunales = evaluacionTribunales;
    }

    public List<CalificacionMiembro> getCalificacionMiembros() {
        return calificacionMiembros;
    }

    public void setCalificacionMiembros(List<CalificacionMiembro> calificacionMiembros) {
        this.calificacionMiembros = calificacionMiembros;
    }

}
