/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.autorProyecto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionAutorProyecto")
@SessionScoped
public class SessionAutorProyecto implements Serializable{

  private List<AutorProyectoDTO> autorProyectos;
    private List<AutorProyectoDTO> filterAutorProyectos;
    public SessionAutorProyecto() {
         this.autorProyectos = new ArrayList<>();
        this.filterAutorProyectos = new ArrayList<>();
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
