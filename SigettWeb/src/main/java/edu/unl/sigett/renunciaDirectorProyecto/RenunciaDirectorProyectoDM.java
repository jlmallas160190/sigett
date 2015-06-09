/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.renunciaDirectorProyecto;

import edu.unl.sigett.entity.RenunciaDirector;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "renunciaDirectorProyectoDM")
@SessionScoped
public class RenunciaDirectorProyectoDM implements Serializable {

    private RenunciaDirector renunciaDirector;

    public RenunciaDirectorProyectoDM() {
        this.renunciaDirector = new RenunciaDirector();
    }

    public RenunciaDirector getRenunciaDirector() {
        return renunciaDirector;
    }

    public void setRenunciaDirector(RenunciaDirector renunciaDirector) {
        this.renunciaDirector = renunciaDirector;
    }

}
