/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.configuracionCarrera;

import edu.unl.sigett.entity.ConfiguracionCarrera;
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
public class SessionConfiguracionCarrera implements Serializable {

    private ConfiguracionCarrera configuracionCarrera;
    private boolean renderedEditar;
    private boolean tieneModulos;
    private List<ConfiguracionCarrera> configuracionCarreras;

    public SessionConfiguracionCarrera() {
        this.tieneModulos = false;
        this.configuracionCarreras = new ArrayList<>();
        this.configuracionCarrera = new ConfiguracionCarrera();
    }

    public ConfiguracionCarrera getConfiguracionCarrera() {
        return configuracionCarrera;
    }

    public void setConfiguracionCarrera(ConfiguracionCarrera configuracionCarrera) {
        this.configuracionCarrera = configuracionCarrera;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public List<ConfiguracionCarrera> getConfiguracionCarreras() {
        return configuracionCarreras;
    }

    public void setConfiguracionCarreras(List<ConfiguracionCarrera> configuracionCarreras) {
        this.configuracionCarreras = configuracionCarreras;
    }

    public boolean isTieneModulos() {
        return tieneModulos;
    }

    public void setTieneModulos(boolean tieneModulos) {
        this.tieneModulos = tieneModulos;
    }

}
