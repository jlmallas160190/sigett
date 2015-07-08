/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionPeriodoCoordinacion;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import edu.jlmallas.academico.service.PeriodoCoordinacionService;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarPeriodoCoordinacion",
            pattern = "/editarPeriodoCoordinacion/",
            viewId = "/faces/pages/academico/periodosCoordinacion/editarPeriodoCoordinacion.xhtml"
    ),
    @URLMapping(
            id = "crearPeriodoCoordinacion",
            pattern = "/crearPeriodoCoordinacion/",
            viewId = "/faces/pages/academico/periodosCoordinacion/editarPeriodoCoordinacion.xhtml"
    ),
    @URLMapping(
            id = "periodosCoordinacion",
            pattern = "/periodosCoordinacion/",
            viewId = "/faces/pages/academico/periodosCoordinacion/index.xhtml"
    )})
public class AdministrarPeriodoCoordinacion implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGE BEANS"> 
    @Inject
    private SessionPeriodoCoordinacion sessionPeriodoCoordinacion;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private SessionUsuario sessionUsuario;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS"> 
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/AcademicoService/PeriodoCoordinacionServiceImplement!edu.jlmallas.academico.service.PeriodoCoordinacionService")
    private PeriodoCoordinacionService periodoCoordinacionService;
    //</editor-fold>

    public AdministrarPeriodoCoordinacion() {
    }

    public void init() {
        this.buscar();
        this.renderedCrear(sessionUsuario.getUsuario());
        this.renderedEditar(sessionUsuario.getUsuario());
        this.renderedEliminar(sessionUsuario.getUsuario());
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD"> 
    public String crear(Usuario usuario, Carrera carrera) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "crear_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionPeriodoCoordinacion.setPeriodoCoordinacion(new PeriodoCoordinacion(carrera, Boolean.TRUE));
                sessionPeriodoCoordinacion.getPeriodoCoordinacion().setCarreraId(carrera);
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "pretty:crearPeriodoCoordinacion";
                } else {
                    if (param.equalsIgnoreCase("crear-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarPeriodoCoordinacion').show()");
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(PeriodoCoordinacion periodoCoordinacion, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "editar_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionPeriodoCoordinacion.setPeriodoCoordinacion(periodoCoordinacion);
                if (param.equalsIgnoreCase("editar")) {
                    navegacion = "pretty:editarPeriodoCoordinacion";
                } else {
                    if (param.equalsIgnoreCase("editar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarPeriodoCoordinacion').show()");
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void buscar() {
        this.sessionPeriodoCoordinacion.getPeriodosCoordinacion().clear();
        this.sessionPeriodoCoordinacion.getFilterPeriodosCoordinacion().clear();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "buscar_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionPeriodoCoordinacion.setPeriodosCoordinacion(periodoCoordinacionService.buscar(new PeriodoCoordinacion(
                        sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), Boolean.TRUE)));
                sessionPeriodoCoordinacion.setFilterPeriodosCoordinacion(sessionPeriodoCoordinacion.getPeriodosCoordinacion());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    /**
     * GRABAR PERIODO DE COORDINACIÓN
     *
     * @param periodoCoordinacion
     * @param usuario
     * @return
     */
    public String grabar(PeriodoCoordinacion periodoCoordinacion, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (periodoCoordinacion.getId() == null) {
                int tienePermiso = usuarioService.tienePermiso(usuario, "crear_periodo_coordinacion");
                if (tienePermiso == 1) {
                    periodoCoordinacionService.guardar(periodoCoordinacion);
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionPeriodoCoordinacion.setPeriodoCoordinacion(new PeriodoCoordinacion());
                        navegacion = "pretty:periodosCoordinacion";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.periodoAcademico") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioService.tienePermiso(usuario, "editar_periodo_coordinacion");
                if (tienePermiso == 1) {
                    periodoCoordinacionService.actualizar(periodoCoordinacion);
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionPeriodoCoordinacion.setPeriodoCoordinacion(new PeriodoCoordinacion());
                        navegacion = "pretty:periodosCoordinacion";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.periodoAcademico") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED"> 

    public void renderedCrear(Usuario usuario) {
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "crear_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionPeriodoCoordinacion.setRenderedCrear(Boolean.TRUE);
            } else {
                sessionPeriodoCoordinacion.setRenderedCrear(Boolean.FALSE);
            }
        } catch (Exception e) {
        }
    }

    public void renderedEditar(Usuario usuario) {
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "editar_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionPeriodoCoordinacion.setRenderedEditar(Boolean.TRUE);
            } else {
                sessionPeriodoCoordinacion.setRenderedEditar(Boolean.FALSE);
            }
        } catch (Exception e) {
        }
    }

    public void renderedEliminar(Usuario usuario) {
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "eliminar_periodo_coordinacion");
            sessionPeriodoCoordinacion.setRenderedEliminar(Boolean.FALSE);
            if (tienePermiso == 1) {
                sessionPeriodoCoordinacion.setRenderedEliminar(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET"> 

    public SessionPeriodoCoordinacion getSessionPeriodoCoordinacion() {
        return sessionPeriodoCoordinacion;
    }

    public void setSessionPeriodoCoordinacion(SessionPeriodoCoordinacion sessionPeriodoCoordinacion) {
        this.sessionPeriodoCoordinacion = sessionPeriodoCoordinacion;
    }
//</editor-fold>
}
