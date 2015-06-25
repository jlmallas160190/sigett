/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import edu.unl.sigett.entity.DirectorProyecto;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class DirectorProyectoDTO implements Serializable {

    private DirectorProyecto directorProyecto;
    private DirectorDTO directorDTO;

    public DirectorProyectoDTO() {
    }

    public DirectorProyectoDTO(DirectorProyecto directorProyecto, DirectorDTO directorDTO) {
        this.directorProyecto = directorProyecto;
        this.directorDTO = directorDTO;
    }

    public DirectorProyecto getDirectorProyecto() {
        return directorProyecto;
    }

    public void setDirectorProyecto(DirectorProyecto directorProyecto) {
        this.directorProyecto = directorProyecto;
    }

    public DirectorDTO getDirectorDTO() {
        return directorDTO;
    }

    public void setDirectorDTO(DirectorDTO directorDTO) {
        this.directorDTO = directorDTO;
    }

}
