/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.temaProyecto;

import edu.unl.sigett.dao.TemaProyectoDao;
import edu.unl.sigett.proyecto.SessionProyecto;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "temaProyectoController")
@SessionScoped
public class TemaProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionProyecto sessionProyecto;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private TemaProyectoDao temaProyectoDao;
//</editor-fold>

    public TemaProyectoController() {
    }

    public void init() {
    }

}
