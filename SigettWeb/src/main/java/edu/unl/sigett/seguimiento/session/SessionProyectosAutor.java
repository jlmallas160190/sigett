/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.session;

import edu.unl.sigett.entity.AutorProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionProyectosAutor implements Serializable {

    private AutorProyecto autorProyecto;

    public SessionProyectosAutor() {
        this.autorProyecto = new AutorProyecto();
    }

    public AutorProyecto getAutorProyecto() {
        return autorProyecto;
    }

    public void setAutorProyecto(AutorProyecto autorProyecto) {
        this.autorProyecto = autorProyecto;
    }

}
