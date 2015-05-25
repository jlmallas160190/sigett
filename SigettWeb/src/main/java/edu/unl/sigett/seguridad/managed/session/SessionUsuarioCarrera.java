/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import edu.unl.sigett.academico.dto.EstudianteCarreraDTO;
import edu.unl.sigett.dto.UsuarioCarreraDTO;
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

    private UsuarioCarreraDTO usuarioCarreraDTO;

    private List<UsuarioCarreraDTO> usuarioCarrerasDTOS;
    private List<UsuarioCarreraDTO> filterUsuarioCarrerasDTO;

    private boolean renderedNoEditar;
    private boolean renderedBuscar;
    private boolean renderedEditar;
    private boolean renderedCrear;

    public SessionUsuarioCarrera() {
        this.filterUsuarioCarrerasDTO = new ArrayList<>();
        this.usuarioCarrerasDTOS = new ArrayList<>();
        this.usuarioCarreraDTO = new UsuarioCarreraDTO();
    }

    public UsuarioCarreraDTO getUsuarioCarreraDTO() {
        return usuarioCarreraDTO;
    }

    public void setUsuarioCarreraDTO(UsuarioCarreraDTO usuarioCarreraAux) {
        this.usuarioCarreraDTO = usuarioCarreraAux;
    }

    public List<UsuarioCarreraDTO> getUsuarioCarrerasDTOS() {
        return usuarioCarrerasDTOS;
    }

    public void setUsuarioCarrerasDTOS(List<UsuarioCarreraDTO> usuarioCarrerasDTOS) {
        this.usuarioCarrerasDTOS = usuarioCarrerasDTOS;
    }

    public boolean getRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean getRenderedBuscar() {
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

    public boolean getRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public List<UsuarioCarreraDTO> getFilterUsuarioCarrerasDTO() {
        return filterUsuarioCarrerasDTO;
    }

    public void setFilterUsuarioCarrerasDTO(List<UsuarioCarreraDTO> filterUsuarioCarrerasDTO) {
        this.filterUsuarioCarrerasDTO = filterUsuarioCarrerasDTO;
    }

}
