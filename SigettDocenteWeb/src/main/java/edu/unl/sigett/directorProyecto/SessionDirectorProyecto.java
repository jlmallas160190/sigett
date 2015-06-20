/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionDirectorProyecto")
@SessionScoped
public class SessionDirectorProyecto implements Serializable {

    private DirectorProyectoDTO directorProyectoDTO;

    private List<DirectorProyectoDTO> directoresProyectoDTO;
    private List<DirectorProyectoDTO> filterDirectoresProyectoDTO;

    public SessionDirectorProyecto() {
        this.filterDirectoresProyectoDTO = new ArrayList<>();
        this.directoresProyectoDTO = new ArrayList<>();
        this.directorProyectoDTO = new DirectorProyectoDTO();
    }

    public DirectorProyectoDTO getDirectorProyectoDTO() {
        return directorProyectoDTO;
    }

    public void setDirectorProyectoDTO(DirectorProyectoDTO directorProyectoDTO) {
        this.directorProyectoDTO = directorProyectoDTO;
    }

    public List<DirectorProyectoDTO> getDirectoresProyectoDTO() {
        return directoresProyectoDTO;
    }

    public void setDirectoresProyectoDTO(List<DirectorProyectoDTO> directoresProyectoDTO) {
        this.directoresProyectoDTO = directoresProyectoDTO;
    }

    public List<DirectorProyectoDTO> getFilterDirectoresProyectoDTO() {
        return filterDirectoresProyectoDTO;
    }

    public void setFilterDirectoresProyectoDTO(List<DirectorProyectoDTO> filterDirectoresProyectoDTO) {
        this.filterDirectoresProyectoDTO = filterDirectoresProyectoDTO;
    }

}
