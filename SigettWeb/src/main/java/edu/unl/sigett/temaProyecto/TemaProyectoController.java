/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.temaProyecto;

import edu.unl.sigett.dao.TemaProyectoDao;
import edu.unl.sigett.entity.Tema;
import edu.unl.sigett.entity.TemaProyecto;
import edu.unl.sigett.proyecto.SessionProyecto;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
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
        this.crearEditar();
    }

    private void crearEditar() {
        List<TemaProyecto> temaProyectos = temaProyectoDao.buscar(new TemaProyecto(sessionProyecto.getProyectoSeleccionado().getId() != null
                ? sessionProyecto.getProyectoSeleccionado() : null, null, Boolean.TRUE));
        if (temaProyectos == null) {
            sessionProyecto.setTemaProyecto(new TemaProyecto(sessionProyecto.getProyectoSeleccionado(), new Tema(), Boolean.TRUE));
            return;
        }
        TemaProyecto temaProyecto = !temaProyectos.isEmpty() ? temaProyectos.get(0) : null;
        if (temaProyecto == null) {
            sessionProyecto.setTemaProyecto(new TemaProyecto(sessionProyecto.getProyectoSeleccionado(), new Tema(), Boolean.TRUE));
            return;
        }
        sessionProyecto.setTemaProyecto(temaProyecto);
    }
}
