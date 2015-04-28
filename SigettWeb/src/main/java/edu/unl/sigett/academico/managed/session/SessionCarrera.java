/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import edu.jlmallas.academico.entity.Carrera;
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
public class SessionCarrera implements Serializable {

    private Carrera carrera;
    private Carrera carreraWs;
    private String key;
    private int keyEntero;
    private String keyWsParalelosCarrera;
    private int keyWsParalelosCarreraEntero;
    private byte[] contents;
    private String nivel;

    private boolean esEditado = false;
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedSincronizar;

    private List<Carrera> carrerasGrabar;
    private List<Carrera> carreras;

    public SessionCarrera() {
        this.carreras = new ArrayList<>();
        this.carrerasGrabar = new ArrayList<>();
        this.carrera = new Carrera();
        this.carreraWs = new Carrera();
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
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

    public String getKeyWsParalelosCarrera() {
        return keyWsParalelosCarrera;
    }

    public void setKeyWsParalelosCarrera(String keyWsParalelosCarrera) {
        this.keyWsParalelosCarrera = keyWsParalelosCarrera;
    }

    public int getKeyWsParalelosCarreraEntero() {
        return keyWsParalelosCarreraEntero;
    }

    public void setKeyWsParalelosCarreraEntero(int keyWsParalelosCarreraEntero) {
        this.keyWsParalelosCarreraEntero = keyWsParalelosCarreraEntero;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
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

    public List<Carrera> getCarrerasGrabar() {
        return carrerasGrabar;
    }

    public void setCarrerasGrabar(List<Carrera> carrerasGrabar) {
        this.carrerasGrabar = carrerasGrabar;
    }

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
    }

    public Carrera getCarreraWs() {
        return carreraWs;
    }

    public void setCarreraWs(Carrera carreraWs) {
        this.carreraWs = carreraWs;
    }

}
