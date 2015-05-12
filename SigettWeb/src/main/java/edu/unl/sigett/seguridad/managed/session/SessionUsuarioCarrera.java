/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import edu.unl.sigett.dto.UsuarioCarreraAux;
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
public class SessionUsuarioCarrera implements Serializable {

    private UsuarioCarreraAux usuarioCarreraAux;
    private List<UsuarioCarreraAux> usuarioCarrerasAuxs;
    private boolean renderedNoEditar;
    private boolean renderedBuscar;
    private boolean renderedEditar;
    private boolean renderedCrear;

    public SessionUsuarioCarrera() {
        this.usuarioCarrerasAuxs = new ArrayList<>();
        this.usuarioCarreraAux = new UsuarioCarreraAux();
    }

    public UsuarioCarreraAux getUsuarioCarreraAux() {
        return usuarioCarreraAux;
    }

    public void setUsuarioCarreraAux(UsuarioCarreraAux usuarioCarreraAux) {
        this.usuarioCarreraAux = usuarioCarreraAux;
    }

    public List<UsuarioCarreraAux> getUsuarioCarrerasAuxs() {
        return usuarioCarrerasAuxs;
    }

    public void setUsuarioCarrerasAuxs(List<UsuarioCarreraAux> usuarioCarrerasAuxs) {
        this.usuarioCarrerasAuxs = usuarioCarrerasAuxs;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
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
