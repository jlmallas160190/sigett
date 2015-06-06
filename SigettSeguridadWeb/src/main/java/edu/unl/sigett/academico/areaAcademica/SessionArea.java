/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.areaAcademica;

import edu.jlmallas.academico.entity.Area;
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
public class SessionArea implements Serializable {

    private Area area;
    private List<Area> areasWS;
    private Area areaWs;
    private List<Area> areas;
    private String key;
    private String areaConverter;
    private int keyEntero;
    private boolean esNuevaArea = false;
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedSincronizar;

    public SessionArea() {
        this.areaWs = new Area();
        this.areas = new ArrayList<>();
        this.areasWS = new ArrayList<>();
        this.area = new Area();
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Area> getAreasWS() {
        return areasWS;
    }

    public void setAreasWS(List<Area> areasWS) {
        this.areasWS = areasWS;
    }

    public Area getAreaWs() {
        return areaWs;
    }

    public void setAreaWs(Area areaWs) {
        this.areaWs = areaWs;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
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

    public boolean isEsNuevaArea() {
        return esNuevaArea;
    }

    public void setEsNuevaArea(boolean esNuevaArea) {
        this.esNuevaArea = esNuevaArea;
    }

    public String getAreaConverter() {
        return areaConverter;
    }

    public void setAreaConverter(String areaConverter) {
        this.areaConverter = areaConverter;
    }

}
