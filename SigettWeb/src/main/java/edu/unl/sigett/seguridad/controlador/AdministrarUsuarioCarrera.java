/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.dao.CarreraDao;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuarioCarrera;
import org.jlmallas.seguridad.entity.Usuario;
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
import edu.unl.sigett.dao.UsuarioCarreraDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.dto.UsuarioCarreraAux;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarUsuarioCarrera",
            pattern = "/editarUsuarioCarrera/#{sessionUsuarioCarrera.usuarioCarreraAux.carrera.id}",
            viewId = "/faces/pages/sigett/usuarioCarrera/editarUsuarioCarrera.xhtml"
    ),
    @URLMapping(
            id = "carrerasUsuario",
            pattern = "/carrerasUsuario/",
            viewId = "/faces/faces/pages/sigett/buscarCarrerasUsuario.xhtml"
    )})
public class AdministrarUsuarioCarrera implements Serializable {
    
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private SessionUsuario sessionUsuario;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private UsuarioCarreraDao usuarioCarreraDao;
    @EJB
    private CarreraDao carreraDao;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraDao;
    
    public AdministrarUsuarioCarrera() {
    }
    public void init(){
        buscar();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void renderedEditar() {
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_usuario_carrera");
        if (tienePermiso == 1) {
            sessionUsuarioCarrera.setRenderedEditar(true);
            sessionUsuarioCarrera.setRenderedEditar(false);
            return;
        }
        sessionUsuarioCarrera.setRenderedEditar(false);
        sessionUsuarioCarrera.setRenderedEditar(true);
    }
    
    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioDao.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            sessionUsuarioCarrera.setRenderedBuscar(true);
            return;
        }
        sessionUsuarioCarrera.setRenderedBuscar(false);
    }
//</editor-fold>

    public String editar(UsuarioCarreraAux usuarioCarreraAux) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_usuario_carrera");
            if (tienePermiso == 1) {
                usuarioCarreraAux.setCarrera(carreraDao.find(usuarioCarreraAux.getCarrera().getId()));
                sessionUsuarioCarrera.setUsuarioCarreraAux(usuarioCarreraAux);
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
    
    public void buscar() {
        try {
            sessionUsuarioCarrera.setUsuarioCarrerasAuxs(new ArrayList<UsuarioCarreraAux>());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "buscar_usuario_carrera");
            if (tienePermiso == 1) {
                UsuarioCarrera usuarioCarrera = new UsuarioCarrera();
                usuarioCarrera.setUsuarioId(sessionUsuario.getUsuario().getId());
                List<UsuarioCarrera> usuarioCarreras = usuarioCarreraDao.buscar(usuarioCarrera);
                if (usuarioCarreras == null) {
                    return;
                }
                for (UsuarioCarrera uc : usuarioCarreras) {
                    UsuarioCarreraAux usuarioCarreraAux = new UsuarioCarreraAux(uc, usuarioDao.find(uc.getUsuarioId()),
                            carreraDao.find(uc.getCarreraId()));
                    sessionUsuarioCarrera.getUsuarioCarrerasAuxs().add(usuarioCarreraAux);
                }
                return;
            }
            if (tienePermiso == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public String grabarCarrera() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getIdSga() == null) {
                sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().setIdSga("");
            }
            if (configuracionCarreraDao.buscarPorCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera()
                    .getId(), "MA") == null) {
                ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera();
                configuracionCarrera1.setNombre("Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación");
                configuracionCarrera1.setCodigo("MA");
                configuracionCarrera1.setValor("?");
                configuracionCarrera1.setObservacion("S/N");
                configuracionCarrera1.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId());
                configuracionCarrera1.setTipo("numerico");
                configuracionCarreraDao.create(configuracionCarrera1);
            }
            if (configuracionCarreraDao.buscarPorCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId(), "ME") == null) {
                ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera();
                configuracionCarrera2.setNombre("Número de Modulo aprobado para ser egresado");
                configuracionCarrera2.setCodigo("ME");
                configuracionCarrera2.setValor("?");
                configuracionCarrera2.setObservacion("S/N");
                configuracionCarrera2.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId());
                configuracionCarrera2.setTipo("numerico");
                configuracionCarreraDao.create(configuracionCarrera2);
            }
            if (configuracionCarreraDao.buscarPorCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId(), "OA") == null) {
                ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera();
                configuracionCarrera.setNombre("ID de Oferta Academica Actual de la Carrera");
                configuracionCarrera.setCodigo("OA");
                configuracionCarrera.setValor("?");
                configuracionCarrera.setObservacion("S/N");
                configuracionCarrera.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId());
                configuracionCarrera.setTipo("boton");
                configuracionCarreraDao.create(configuracionCarrera);
            }
            if (configuracionCarreraDao.buscarPorCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId(), "NO") == null) {
                ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera();
                configuracionCarrera3.setNombre("Número de Oficio");
                configuracionCarrera3.setCodigo("NO");
                configuracionCarrera3.setValor("1");
                configuracionCarrera3.setObservacion("Número de Oficio");
                configuracionCarrera3.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId());
                configuracionCarrera3.setTipo("numerico");
                configuracionCarreraDao.create(configuracionCarrera3);
            }
            if (configuracionCarreraDao.buscarPorCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId(), "NA") == null) {
                ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera();
                configuracionCarrera4.setNombre("Número de Acta de tesis");
                configuracionCarrera4.setCodigo("NA");
                configuracionCarrera4.setValor("?");
                configuracionCarrera4.setObservacion("S/N");
                configuracionCarrera4.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().getId());
                configuracionCarrera4.setTipo("numerico");
                configuracionCarreraDao.create(configuracionCarrera4);
            }
            carreraDao.edit(sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera());
            if (param.equalsIgnoreCase("grabar")) {
                sessionUsuarioCarrera.setUsuarioCarreraAux(new UsuarioCarreraAux(new UsuarioCarrera(), new Usuario(), new Carrera()));
                return "pretty:editarArea";
            }
            
            if (param.equalsIgnoreCase("grabar-editar")) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.carrera") + " " + bundle.getString("lbl.msm_editar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            this.buscar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navegacion;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        try {
            sessionUsuarioCarrera.getUsuarioCarreraAux().getCarrera().setLogo(event.getFile().getContents());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
