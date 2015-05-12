/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import edu.unl.sigett.entity.ConfiguracionGeneral;
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
public class SessionConfiguracion implements Serializable {

    private ConfiguracionGeneral configuracionGeneral;
    private long tamanioPermitidoImagenes;
    private long intervalo;
    private long tamanioPermitidoArchivos;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private List<ConfiguracionGeneral> configuracionesGenerales;

    public SessionConfiguracion() {
        this.renderedCrear=false;
        this.renderedEditar=false;
        this.configuracionesGenerales = new ArrayList<>();
        this.configuracionGeneral = new ConfiguracionGeneral();
    }

    public ConfiguracionGeneral getConfiguracionGeneral() {
        return configuracionGeneral;
    }

    public void setConfiguracionGeneral(ConfiguracionGeneral configuracionGeneral) {
        this.configuracionGeneral = configuracionGeneral;
    }

    public long getTamanioPermitidoImagenes() {
        return tamanioPermitidoImagenes;
    }

    public void setTamanioPermitidoImagenes(long tamanioPermitidoImagenes) {
        this.tamanioPermitidoImagenes = tamanioPermitidoImagenes;
    }

    public long getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(long intervalo) {
        this.intervalo = intervalo;
    }

    public long getTamanioPermitidoArchivos() {
        return tamanioPermitidoArchivos;
    }

    public void setTamanioPermitidoArchivos(long tamanioPermitidoArchivos) {
        this.tamanioPermitidoArchivos = tamanioPermitidoArchivos;
    }

    public List<ConfiguracionGeneral> getConfiguracionesGenerales() {
        return configuracionesGenerales;
    }

    public void setConfiguracionesGenerales(List<ConfiguracionGeneral> configuracionesGenerales) {
        this.configuracionesGenerales = configuracionesGenerales;
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

}
