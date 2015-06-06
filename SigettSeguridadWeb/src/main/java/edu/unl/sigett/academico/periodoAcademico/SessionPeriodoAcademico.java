/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.periodoAcademico;

import edu.jlmallas.academico.entity.PeriodoAcademico;
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
public class SessionPeriodoAcademico implements Serializable {

    private PeriodoAcademico periodoAcademico;
    private List<PeriodoAcademico> periodoAcademicosGrabar;
    private List<PeriodoAcademico> periodoAcademicos;
    private List<PeriodoAcademico> periodoAcademicosFilter;
    private PeriodoAcademico periodoAcademicoWs;
    private String key;
    private int keyEntero;
    private boolean esNuevoPeriodo = false;
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedSincronizar;

    public SessionPeriodoAcademico() {
        this.periodoAcademicoWs = new PeriodoAcademico();
        this.periodoAcademicos = new ArrayList<>();
        this.periodoAcademicosFilter = new ArrayList<>();
        this.periodoAcademicosGrabar = new ArrayList<>();
        this.periodoAcademico = new PeriodoAcademico();
    }

    public PeriodoAcademico getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void setPeriodoAcademico(PeriodoAcademico periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public List<PeriodoAcademico> getPeriodoAcademicosGrabar() {
        return periodoAcademicosGrabar;
    }

    public void setPeriodoAcademicosGrabar(List<PeriodoAcademico> periodoAcademicosGrabar) {
        this.periodoAcademicosGrabar = periodoAcademicosGrabar;
    }

    public List<PeriodoAcademico> getPeriodoAcademicos() {
        return periodoAcademicos;
    }

    public void setPeriodoAcademicos(List<PeriodoAcademico> periodoAcademicos) {
        this.periodoAcademicos = periodoAcademicos;
    }

    public List<PeriodoAcademico> getPeriodoAcademicosFilter() {
        return periodoAcademicosFilter;
    }

    public void setPeriodoAcademicosFilter(List<PeriodoAcademico> periodoAcademicosFilter) {
        this.periodoAcademicosFilter = periodoAcademicosFilter;
    }

    public PeriodoAcademico getPeriodoAcademicoWs() {
        return periodoAcademicoWs;
    }

    public void setPeriodoAcademicoWs(PeriodoAcademico periodoAcademicoWs) {
        this.periodoAcademicoWs = periodoAcademicoWs;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getKeyEntero() {
        return keyEntero;
    }

    public void setKeyEntero(int keyEntero) {
        this.keyEntero = keyEntero;
    }

    public boolean isEsNuevoPeriodo() {
        return esNuevoPeriodo;
    }

    public void setEsNuevoPeriodo(boolean esNuevoPeriodo) {
        this.esNuevoPeriodo = esNuevoPeriodo;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedSincronizar() {
        return renderedSincronizar;
    }

    public void setRenderedSincronizar(boolean renderedSincronizar) {
        this.renderedSincronizar = renderedSincronizar;
    }

}
