/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import edu.unl.sigett.director.DirectorDTO;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "directorProyectoDM")
@SessionScoped
public class DirectorProyectoDM implements Serializable {

    private List<DirectorDTO> directoresDTO;
    private List<DirectorDTO> filterDirectoresDTO;

    private DirectorProyectoDTO directorProyectoDTO;

    private Boolean renderedCrear;
    private Boolean renderedEliminar;
    private Boolean renderedBuscar;
    private Boolean renderedBuscarDirectorDisponible;
    private Boolean renderedPnlDirectoresDisponibles;

    public DirectorProyectoDM() {
        this.directoresDTO=new ArrayList<>();
        this.filterDirectoresDTO=new ArrayList<>();
        this.directorProyectoDTO = new DirectorProyectoDTO();
    }

    public DirectorProyectoDTO getDirectorProyectoDTO() {
        return directorProyectoDTO;
    }

    public void setDirectorProyectoDTO(DirectorProyectoDTO directorProyectoDTO) {
        this.directorProyectoDTO = directorProyectoDTO;
    }

    public Boolean getRenderedBuscarDirectorDisponible() {
        return renderedBuscarDirectorDisponible;
    }

    public void setRenderedBuscarDirectorDisponible(Boolean renderedBuscarDirectorDisponible) {
        this.renderedBuscarDirectorDisponible = renderedBuscarDirectorDisponible;
    }

    public Boolean getRenderedPnlDirectoresDisponibles() {
        return renderedPnlDirectoresDisponibles;
    }

    public void setRenderedPnlDirectoresDisponibles(Boolean renderedPnlDirectoresDisponibles) {
        this.renderedPnlDirectoresDisponibles = renderedPnlDirectoresDisponibles;
    }


    public Boolean getRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(Boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public Boolean getRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(Boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public Boolean getRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(Boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public List<DirectorDTO> getDirectoresDTO() {
        return directoresDTO;
    }

    public void setDirectoresDTO(List<DirectorDTO> directoresDTO) {
        this.directoresDTO = directoresDTO;
    }

    public List<DirectorDTO> getFilterDirectoresDTO() {
        return filterDirectoresDTO;
    }

    public void setFilterDirectoresDTO(List<DirectorDTO> filterDirectoresDTO) {
        this.filterDirectoresDTO = filterDirectoresDTO;
    }

}
