/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import edu.jlmallas.academico.entity.OfertaAcademica;
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
public class SessionOfertaAcademica implements Serializable {

    private OfertaAcademica ofertaAcademica;
    private List<OfertaAcademica> ofertaAcademicas;
    private List<OfertaAcademica> ofertaAcademicasFilter;
    private OfertaAcademica ofertaAcademicaWS;
    private boolean renderedTodo;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedSincronizar;
    private boolean renderedNoEditar;
    private boolean esEditado = false;
    private String key;
    private int keyEntero;

    public SessionOfertaAcademica() {
        this.ofertaAcademicaWS = new OfertaAcademica();
        this.ofertaAcademicas = new ArrayList<>();
        this.ofertaAcademicasFilter = new ArrayList<>();
        this.ofertaAcademica = new OfertaAcademica();
    }

    public OfertaAcademica getOfertaAcademica() {
        return ofertaAcademica;
    }

    public void setOfertaAcademica(OfertaAcademica ofertaAcademica) {
        this.ofertaAcademica = ofertaAcademica;
    }

    public boolean isRenderedTodo() {
        return renderedTodo;
    }

    public void setRenderedTodo(boolean renderedTodo) {
        this.renderedTodo = renderedTodo;
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

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public OfertaAcademica getOfertaAcademicaWS() {
        return ofertaAcademicaWS;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<OfertaAcademica> getOfertaAcademicas() {
        return ofertaAcademicas;
    }

    public void setOfertaAcademicas(List<OfertaAcademica> ofertaAcademicas) {
        this.ofertaAcademicas = ofertaAcademicas;
    }

    public List<OfertaAcademica> getOfertaAcademicasFilter() {
        return ofertaAcademicasFilter;
    }

    public void setOfertaAcademicasFilter(List<OfertaAcademica> ofertaAcademicasFilter) {
        this.ofertaAcademicasFilter = ofertaAcademicasFilter;
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
    }

    public void setOfertaAcademicaWS(OfertaAcademica ofertaAcademicaWS) {
        this.ofertaAcademicaWS = ofertaAcademicaWS;
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

}
