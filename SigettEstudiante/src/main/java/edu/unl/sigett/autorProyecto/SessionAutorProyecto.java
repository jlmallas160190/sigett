/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.autorProyecto;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionAutorProyecto")
@SessionScoped
public class SessionAutorProyecto implements Serializable {

    private AutorProyectoDTO autorProyectoDTO;

    private List<AutorProyectoDTO> autorProyectos;
    private List<AutorProyectoDTO> filterAutorProyectos;

    public SessionAutorProyecto() {
        this.filterAutorProyectos = new ArrayList<>();
        this.autorProyectos = new ArrayList<>();
        this.autorProyectoDTO = new AutorProyectoDTO();
    }

    public AutorProyectoDTO getAutorProyectoDTO() {
        return autorProyectoDTO;
    }

    public void setAutorProyectoDTO(AutorProyectoDTO autorProyectoDTO) {
        this.autorProyectoDTO = autorProyectoDTO;
    }

    public List<AutorProyectoDTO> getAutorProyectos() {
        return autorProyectos;
    }

    public void setAutorProyectos(List<AutorProyectoDTO> autorProyectos) {
        this.autorProyectos = autorProyectos;
    }

    public List<AutorProyectoDTO> getFilterAutorProyectos() {
        return filterAutorProyectos;
    }

    public void setFilterAutorProyectos(List<AutorProyectoDTO> filterAutorProyectos) {
        this.filterAutorProyectos = filterAutorProyectos;
    }

}
