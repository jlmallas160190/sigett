/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.director;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "directorDM")
@SessionScoped
public class DirectorDM implements Serializable {

    private List<DirectorDTO> directoresDTO;
    private List<DirectorDTO> filterDirectoresDTO;

    public DirectorDM() {
        this.directoresDTO = new ArrayList<>();
        this.filterDirectoresDTO = new ArrayList<>();
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
