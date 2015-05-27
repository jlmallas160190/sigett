/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.Proyecto;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarConfiguracionesProyecto implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;

    @EJB
    private UsuarioDao usuarioFacadeLocal;

    private List<ConfiguracionProyecto> configuracionProyectos;

    private String criterio;
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedBuscar;

    public AdministrarConfiguracionesProyecto() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">


    public void buscar(String criterio, Proyecto proyecto) {
        this.configuracionProyectos = new ArrayList<>();
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_configuracion_proyecto");
            if (tienePermiso == 1) {
                for (ConfiguracionProyecto configuracionProyecto : proyecto.getConfiguracionProyectoList()) {
                    if (configuracionProyecto.getNombre().toUpperCase().contains(criterio.toUpperCase())) {
                        configuracionProyectos.add(configuracionProyecto);
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para buscar Configuraciones. Consulte con el administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedEditar(Usuario usuario, Proyecto proyecto) {
//        if (proyecto.getEstadoProyectoId().getId() == 2) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_configuracion_proyecto");
//            if (tienePermiso == 1) {
//                renderedEditar = true;
//                renderedNoEditar = false;
//            } else {
//                renderedNoEditar = true;
//                renderedEditar = false;
//            }
//        } else {
//            renderedNoEditar = true;
//            renderedEditar = false;
//        }
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_configuracion_proyecto");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionProyecto getSessionProyecto() {
        return sessionProyecto;
    }

    public void setSessionProyecto(SessionProyecto sessionProyecto) {
        this.sessionProyecto = sessionProyecto;
    }

    public List<ConfiguracionProyecto> getConfiguracionProyectos() {
        return configuracionProyectos;
    }

    public void setConfiguracionProyectos(List<ConfiguracionProyecto> configuracionProyectos) {
        this.configuracionProyectos = configuracionProyectos;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }


    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }
//</editor-fold>
}
