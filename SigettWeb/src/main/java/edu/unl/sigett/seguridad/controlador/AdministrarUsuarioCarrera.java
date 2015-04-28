/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import edu.unl.sigett.academico.controlador.AdministrarCarreras;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuarioCarrera;
import com.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
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
import edu.unl.sigett.session.UsuarioCarreraFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.service.CarreraService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarUsuarioCarrera",
            pattern = "/editarUsuarioCarrera/#{sessionUsuarioCarrera.usuarioCarrera.id}",
            viewId = "/faces/pages/sigett/editarUsuarioCarrera.xhtml"
    ),
    @URLMapping(
            id = "carrerasUsuario",
            pattern = "/carrerasUsuario/",
            viewId = "/faces/faces/pages/sigett//buscarCarrerasUsuario.xhtml"
    )})
public class AdministrarUsuarioCarrera implements Serializable {
    
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private UsuarioCarreraFacadeLocal usuarioCarreraFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    
    @Inject
    private SessionUsuario sessionUsuario;
    private List<UsuarioCarrera> usuarioCarreras;
    private boolean renderedNoEditar;
    private String criterio;
    private boolean renderedBuscar;
    private List<UsuarioCarrera> carreras;
    
    public AdministrarUsuarioCarrera() {
    }
    
    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_usuario_carrera");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }
    
    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }
    
    public Carrera getCarrera(UsuarioCarrera usuarioCarrera) {
        try {
            return carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String editar(UsuarioCarrera usuarioCarrera, AdministrarCarreras carreras) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_usuario_carrera");
            if (tienePermiso == 1) {
                sessionUsuarioCarrera.setUsuarioCarrera(usuarioCarrera);
                sessionUsuarioCarrera.setCarrera(carreraFacadeLocal.find(usuarioCarrera.getCarreraId()));
                sessionUsuarioCarrera.setUsuario(usuarioFacadeLocal.find(usuarioCarrera.getUsuarioId()));
//                carreras.setNivel(sessionUsuarioCarrera.getCarrera().getNivelId().toString());
                navegacion = "pretty:editarUsuarioCarrera";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
        
    }
    
    public void buscar(String criterio) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_usuario_carrera");
            if (tienePermiso == 1) {
                usuarioCarreras = usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId());
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void usuarioCarreras(Usuario usuario) {
        carreras = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_usuario_carrera");
            if (tienePermiso == 1) {
                for (UsuarioCarrera uc : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    carreras.add(uc);
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public List<UsuarioCarrera> getCarreras() {
        return carreras;
    }
    
    public void setCarreras(List<UsuarioCarrera> carreras) {
        this.carreras = carreras;
    }
    
    public SessionUsuarioCarrera getSessionUsuarioCarrera() {
        return sessionUsuarioCarrera;
    }
    
    public void setSessionUsuarioCarrera(SessionUsuarioCarrera sessionUsuarioCarrera) {
        this.sessionUsuarioCarrera = sessionUsuarioCarrera;
    }
    
    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }
    
    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }
    
    public List<UsuarioCarrera> getUsuarioCarreras() {
        return usuarioCarreras;
    }
    
    public void setUsuarioCarreras(List<UsuarioCarrera> usuarioCarreras) {
        this.usuarioCarreras = usuarioCarreras;
    }
    
    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }
    
    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }
    
    public String getCriterio() {
        return criterio;
    }
    
    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }
    
    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }
    
    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }
    
}
