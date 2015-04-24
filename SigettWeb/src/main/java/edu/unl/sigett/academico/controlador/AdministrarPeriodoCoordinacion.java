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
import com.jlmallas.seguridad.entity.Usuario;
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
import org.primefaces.context.RequestContext;
import edu.jlmallas.academico.service.PeriodoCoordinacionFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarPeriodoCoordinacion",
            pattern = "/editarPeriodoCoordinacion/#{sessionPeriodoCoordinacion.periodoCoordinacion.id}",
            viewId = "/faces/pages/academico/editarPeriodoCoordinacion.xhtml"
    ),
    @URLMapping(
            id = "crearPeriodoCoordinacion",
            pattern = "/crearPeriodoCoordinacion/",
            viewId = "/faces/pages/academico/editarPeriodoCoordinacion.xhtml"
    ),
    @URLMapping(
            id = "periodosCoordinacion",
            pattern = "/periodosCoordinacion/",
            viewId = "/faces/pages/academico/buscarPeriodosCoordinacion.xhtml"
    )})
public class AdministrarPeriodoCoordinacion implements Serializable {

    @Inject
    private SessionPeriodoCoordinacion sessionPeriodoCoordinacion;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private List<PeriodoCoordinacion> periodosCoordinacion;
    @EJB
    private PeriodoCoordinacionFacadeLocal periodoCoordinacionFacadeLocal;
    private boolean renderedCrear;
    private boolean renderedEditar;
    private boolean renderedNoEditar;
    private boolean renderedEliminar;

    public AdministrarPeriodoCoordinacion() {
    }
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD"> 

    public String navegarListado(Usuario usuario, Carrera carrera) {
        buscarPorCarrera(usuario, carrera);
        renderedCrear(usuario);
        renderedEditar(usuario);
        renderedEliminar(usuario);
        return "pretty:periodosCoordinacion";
    }

    public String crear(Usuario usuario, Carrera carrera) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso =  usuarioFacadeLocal.tienePermiso(usuario, "crear_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionPeriodoCoordinacion.setPeriodoCoordinacion(new PeriodoCoordinacion());
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

    public String editar(PeriodoCoordinacion periodoCoordinacion, Usuario usuario, Carrera carrera) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionPeriodoCoordinacion.setPeriodoCoordinacion(periodoCoordinacion);
                sessionPeriodoCoordinacion.getPeriodoCoordinacion().setCarreraId(carrera);
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

    public void buscarPorCarrera(Usuario usuario, Carrera carrera) {
        this.periodosCoordinacion = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_periodo_coordinacion");
            if (tienePermiso == 1) {
                periodosCoordinacion = periodoCoordinacionFacadeLocal.buscarPorCarrera(carrera.getId());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public List<PeriodoCoordinacion> buscarActivos() {
        try {
            return periodoCoordinacionFacadeLocal.buscarActivos();
        } catch (Exception e) {
        }
        return null;
    }

    public String grabar(PeriodoCoordinacion periodoCoordinacion, Usuario usuario, Carrera carrera) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (periodoCoordinacion.getId() == null) {
                int tienePermiso  = usuarioFacadeLocal.tienePermiso(usuario, "crear_periodo_coordinacion");
                if (tienePermiso == 1) {
                    periodoCoordinacionFacadeLocal.create(periodoCoordinacion);
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionPeriodoCoordinacion.setPeriodoCoordinacion(new PeriodoCoordinacion());
                        navegacion = "pretty:periodosCoordinacion";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.periodo") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                    buscarPorCarrera(usuario, carrera);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_periodo_coordinacion");
                if (tienePermiso == 1) {
                    periodoCoordinacionFacadeLocal.edit(periodoCoordinacion);
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionPeriodoCoordinacion.setPeriodoCoordinacion(new PeriodoCoordinacion());
                        navegacion = "buscarPeriodosCoordinacion";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.periodo") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                    buscarPorCarrera(usuario, carrera);
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
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_periodo_coordinacion");
            if (tienePermiso == 1) {
                renderedCrear = true;
            } else {
                renderedCrear = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedEditar(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_periodo_coordinacion");
            if (tienePermiso == 1) {
                renderedEditar = true;
                renderedNoEditar = false;
            } else {
                renderedNoEditar = true;
                renderedEditar = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedEliminar(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_periodo_coordinacion");
            if (tienePermiso == 1) {
                renderedEliminar = true;
            } else {
                renderedEliminar = false;
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

    public List<PeriodoCoordinacion> getPeriodosCoordinacion() {
        return periodosCoordinacion;
    }

    public void setPeriodosCoordinacion(List<PeriodoCoordinacion> periodosCoordinacion) {
        this.periodosCoordinacion = periodosCoordinacion;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }
//</editor-fold>
}
