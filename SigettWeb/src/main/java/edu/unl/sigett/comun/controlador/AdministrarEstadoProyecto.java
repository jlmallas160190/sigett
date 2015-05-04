/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.postulacion.managed.session.SessionEstadoProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.EstadoProyecto;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.session.EstadoProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarEstadoProyecto",
            pattern = "/editarEstadoProyecto/#{sessionEstadoProyecto.estadoProyecto.id}",
            viewId = "/faces/pages/sigett/editarEstadoProyecto.xhtml"
    ),
    @URLMapping(
            id = "crearEstadoProyecto",
            pattern = "/crearEstadoProyecto/",
            viewId = "/faces/pages/sigett/editarEstadoProyecto.xhtml"
    ),
    @URLMapping(
            id = "estadosProyecto",
            pattern = "/estadosProyecto/",
            viewId = "/faces/pages/sigett/buscarEstadosProyecto.xhtml"
    )
})
public class AdministrarEstadoProyecto implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionEstadoProyecto sessionEstadoProyecto;
    @EJB
    private EstadoProyectoFacadeLocal estadoProyectoFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    private String criterio;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    private List<EstadoProyecto> estadosProyectos;
    private boolean renderedNoEditar;
    private boolean renderedBuscar;
    private String estado;

    public AdministrarEstadoProyecto() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_estado_proyecto");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_estado_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEliminar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_estado_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_estado_proyecto");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_estado_proyecto");
            if (tienePermiso == 1) {
                sessionEstadoProyecto.setEstadoProyecto(new EstadoProyecto());
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "pretty:crearEstadoProyecto";
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(EstadoProyecto estadoProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_estado_proyecto");
            if (tienePermiso == 1) {
                sessionEstadoProyecto.setEstadoProyecto(estadoProyecto);
                if (param.equalsIgnoreCase("editar")) {
                    navegacion = "pretty:editarEstadoProyecto";
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String grabar(EstadoProyecto estadoProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (estadoProyecto.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_estado_proyecto");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        estadoProyectoFacadeLocal.create(estadoProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("EstadoProyecto", estadoProyecto.getId() + "", "CREAR", "|Nombre= " + estadoProyecto.getNombre(), sessionUsuario.getUsuario()));
                        sessionEstadoProyecto.setEstadoProyecto(new EstadoProyecto());
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estadoProyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        navegacion = "pretty:estadosProyecto";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            estadoProyectoFacadeLocal.create(estadoProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("EstadoProyecto", estadoProyecto.getId() + "", "CREAR", "|Nombre= " + estadoProyecto.getNombre(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estadoProyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                estadoProyectoFacadeLocal.create(estadoProyecto);
                                logFacadeLocal.create(logFacadeLocal.crearLog("EstadoProyecto", estadoProyecto.getId() + "", "CREAR", "|Nombre= " + estadoProyecto.getNombre(), sessionUsuario.getUsuario()));
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estadoProyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionEstadoProyecto.setEstadoProyecto(new EstadoProyecto());
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        if (param.equalsIgnoreCase("grabar") || param.equalsIgnoreCase("grabar-editar") || param.equalsIgnoreCase("grabar-crear")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_estado_proyecto");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        estadoProyectoFacadeLocal.edit(estadoProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("EstadoProyecto", estadoProyecto.getId() + "", "EDITAR", " |Nombre= " + estadoProyecto.getNombre(), sessionUsuario.getUsuario()));
                        sessionEstadoProyecto.setEstadoProyecto(new EstadoProyecto());
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        navegacion = "pretty:estadosProyecto";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            estadoProyectoFacadeLocal.edit(estadoProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("EstadoProyecto", estadoProyecto.getId() + "", "EDITAR", "|Nombre= " + estadoProyecto.getNombre(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                estadoProyectoFacadeLocal.edit(estadoProyecto);
                                logFacadeLocal.create(logFacadeLocal.crearLog("EstadoProyecto", estadoProyecto.getId() + "", "EDITAR", "|Nombre= " + estadoProyecto.getNombre(), sessionUsuario.getUsuario()));
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionEstadoProyecto.setEstadoProyecto(new EstadoProyecto());
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        if (param.equalsIgnoreCase("grabar") || param.equalsIgnoreCase("grabar-editar") || param.equalsIgnoreCase("grabar-crear")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void listadoEstadosProyecto(String criterio) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_estado_proyecto");
            if (tienePermiso == 1) {
                estadosProyectos = estadoProyectoFacadeLocal.buscarPorNombre(criterio);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para buscar EstadosProyectos. Consulte con el Administrador del Sistema.", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public List<EstadoProyecto> buscarTodo() {
        try {
            return estadoProyectoFacadeLocal.findAll();
        } catch (Exception e) {
        }
        return null;
    }

    public void desactivar(EstadoProyecto estadoProyecto) {
        try {
            if (estadoProyecto.getId() != null) {
                estadoProyectoFacadeLocal.edit(estadoProyecto);
                if (estadoProyecto.getEsActivo()) {
                    logFacadeLocal.create(logFacadeLocal.crearLog("EstadoProyecto", estadoProyecto.getId() + "", "ACTIVAR", "|Nombre= " + estadoProyecto.getNombre() + "|Descripcion= " + estadoProyecto.getDescripcion() + "|esActivo= " + estadoProyecto.getEsActivo(), sessionUsuario.getUsuario()));
                } else {
                    logFacadeLocal.create(logFacadeLocal.crearLog("EstadoProyecto", estadoProyecto.getId() + "", "DESACTIVAR", "|Nombre= " + estadoProyecto.getNombre() + "|Descripcion= " + estadoProyecto.getDescripcion() + "|esActivo= " + estadoProyecto.getEsActivo(), sessionUsuario.getUsuario()));

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionEstadoProyecto getSessionEstadoProyecto() {
        return sessionEstadoProyecto;
    }

    public void setSessionEstadoProyecto(SessionEstadoProyecto sessionEstadoProyecto) {
        this.sessionEstadoProyecto = sessionEstadoProyecto;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<EstadoProyecto> getEstadosProyectos() {
        return estadosProyectos;
    }

    public void setEstadosProyectos(List<EstadoProyecto> estadosProyectos) {
        this.estadosProyectos = estadosProyectos;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }
//</editor-fold>
}
