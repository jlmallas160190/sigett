/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.renuncia;

import edu.unl.sigett.entity.Renuncia;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionRenuncia")
@SessionScoped
public class SessionRenuncia implements Serializable {

    private Renuncia renuncia;

    public SessionRenuncia() {
        this.renuncia = new Renuncia();
    }

    public Renuncia getRenuncia() {
        return renuncia;
    }

    public void setRenuncia(Renuncia renuncia) {
        this.renuncia = renuncia;
    }

}
