/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.session;

import edu.unl.sigett.entity.DirectorProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionProyectosDirector implements Serializable {

    private DirectorProyecto directorProyecto;

    public SessionProyectosDirector() {
        this.directorProyecto = new DirectorProyecto();
    }

    public DirectorProyecto getDirectorProyecto() {
        return directorProyecto;
    }

    public void setDirectorProyecto(DirectorProyecto directorProyecto) {
        this.directorProyecto = directorProyecto;
    }

}
