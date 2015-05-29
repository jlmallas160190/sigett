/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class PermisoAdministrarProyecto implements Serializable {

    private Boolean renderedBuscarDocenteProyecto;
    private Boolean renderedBuscarEspecialista;
    private Boolean renderedEliminarDocenteProyecto;
    private Boolean renderedImprimirOficioDocenteProyecto;
    private Boolean renderedSeleccionarEspecialista;

    public PermisoAdministrarProyecto() {
    }

    public Boolean getRenderedBuscarDocenteProyecto() {
        return renderedBuscarDocenteProyecto;
    }

    public void setRenderedBuscarDocenteProyecto(Boolean renderedBuscarDocenteProyecto) {
        this.renderedBuscarDocenteProyecto = renderedBuscarDocenteProyecto;
    }

    public Boolean getRenderedBuscarEspecialista() {
        return renderedBuscarEspecialista;
    }

    public void setRenderedBuscarEspecialista(Boolean renderedBuscarEspecialista) {
        this.renderedBuscarEspecialista = renderedBuscarEspecialista;
    }

    public Boolean getRenderedEliminarDocenteProyecto() {
        return renderedEliminarDocenteProyecto;
    }

    public void setRenderedEliminarDocenteProyecto(Boolean renderedEliminarDocenteProyecto) {
        this.renderedEliminarDocenteProyecto = renderedEliminarDocenteProyecto;
    }

    public Boolean getRenderedImprimirOficioDocenteProyecto() {
        return renderedImprimirOficioDocenteProyecto;
    }

    public void setRenderedImprimirOficioDocenteProyecto(Boolean renderedImprimirOficioDocenteProyecto) {
        this.renderedImprimirOficioDocenteProyecto = renderedImprimirOficioDocenteProyecto;
    }

    public Boolean getRenderedSeleccionarEspecialista() {
        return renderedSeleccionarEspecialista;
    }

    public void setRenderedSeleccionarEspecialista(Boolean renderedSeleccionarEspecialista) {
        this.renderedSeleccionarEspecialista = renderedSeleccionarEspecialista;
    }

}
