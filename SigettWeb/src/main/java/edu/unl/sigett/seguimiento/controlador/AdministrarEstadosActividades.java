/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguimiento.session.SessionEstadoActividad;
import edu.unl.sigett.entity.EstadoActividad;
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
import edu.unl.sigett.session.EstadoActividadFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarEstadoActividad",
            pattern = "/editarEstadoActividad/#{sessionEstadoActividad.estadoActividad.id}",
            viewId = "/faces/pages/sigett/editarEstadoActividad.xhtml"
    ),
    @URLMapping(
            id = "crearEstadoActividad",
            pattern = "/crearEstadoActividad/",
            viewId = "/faces/pages/sigett/editarEstadoActividad.xhtml"
    ),
    @URLMapping(
            id = "estadosActividades",
            pattern = "/estadosActividades/",
            viewId = "/faces/pages/sigett/buscarEstadosActividades.xhtml"
    )
})
public class AdministrarEstadosActividades implements Serializable {

    @Inject
    private SessionEstadoActividad sessionEstadoActividad;
    private String criterio;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    @EJB
    private EstadoActividadFacadeLocal estadoActividadFacadeLocal;
    private List<EstadoActividad> estadoActividades;
    private boolean noEditar;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private boolean renderedBuscar;

    public AdministrarEstadosActividades() {
    }

    public String crear(Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_estado_actividad");
            if (tienePermiso == 1) {
                sessionEstadoActividad.setEstadoActividad(new EstadoActividad());
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "pretty:crearEstadoActividad";
                } else {
                    if (param.equalsIgnoreCase("crear-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarEstadoActividad').show()");
                    }
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

    public String editar(EstadoActividad estadoActividad, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_estado_actividad");
            if (tienePermiso == 1) {
                sessionEstadoActividad.setEstadoActividad(estadoActividad);
                if (param.equalsIgnoreCase("editar")) {
                    navegacion = "pretty:editarEstadoActividad";
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

    public String grabar(EstadoActividad estadoActividad, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (estadoActividad.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_estado_actividad");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        estadoActividadFacadeLocal.create(estadoActividad);
                        logFacadeLocal.create(logFacadeLocal.crearLog("EstadoActividad", estadoActividad.getId() + "", "CREAR", "|Nombre= " + estadoActividad.getNombre() + "|Observación= " + estadoActividad.getObservacion() + "|EsActivo= " + estadoActividad.getEsActivo(), usuario));
                        navegacion = "pretty:estadosActividades";
                        sessionEstadoActividad.setEstadoActividad(new EstadoActividad());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            estadoActividadFacadeLocal.create(estadoActividad);
                            logFacadeLocal.create(logFacadeLocal.crearLog("EstadoActividad", estadoActividad.getId() + "", "CREAR", "|Nombre= " + estadoActividad.getNombre() + "|Observación= " + estadoActividad.getObservacion() + "|EsActivo= " + estadoActividad.getEsActivo(), usuario));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estado") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                estadoActividadFacadeLocal.create(estadoActividad);
                                logFacadeLocal.create(logFacadeLocal.crearLog("EstadoActividad", estadoActividad.getId() + "", "CREAR", "|Nombre= " + estadoActividad.getNombre() + "|Observación= " + estadoActividad.getObservacion() + "|EsActivo= " + estadoActividad.getEsActivo(), usuario));
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estado") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionEstadoActividad.setEstadoActividad(new EstadoActividad());
                            } else {
                                if (param.equalsIgnoreCase("grabar-dlg")) {
                                    estadoActividadFacadeLocal.create(estadoActividad);
                                    logFacadeLocal.create(logFacadeLocal.crearLog("EstadoActividad", estadoActividad.getId() + "", "CREAR", "|Nombre= " + estadoActividad.getNombre() + "|Observación= " + estadoActividad.getObservacion() + "|EsActivo= " + estadoActividad.getEsActivo(), usuario));
                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEstadoActividad').hide()");
                                    sessionEstadoActividad.setEstadoActividad(new EstadoActividad());
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estado") + " " + bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);

                                }
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_estado_actividad");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        estadoActividadFacadeLocal.edit(estadoActividad);
                        logFacadeLocal.create(logFacadeLocal.crearLog("EstadoActividad", estadoActividad.getId() + "", "EDITAR", "|Nombre= " + estadoActividad.getNombre() + "|Observación= " + estadoActividad.getObservacion() + "|EsActivo= " + estadoActividad.getEsActivo(), usuario));
                        navegacion = "buscarEstadosActividades?faces-redirect=true";
                        sessionEstadoActividad.setEstadoActividad(new EstadoActividad());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            estadoActividadFacadeLocal.edit(estadoActividad);
                            logFacadeLocal.create(logFacadeLocal.crearLog("EstadoActividad", estadoActividad.getId() + "", "EDITAR", "|Nombre= " + estadoActividad.getNombre() + "|Observación= " + estadoActividad.getObservacion() + "|EsActivo= " + estadoActividad.getEsActivo(), usuario));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estado") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                estadoActividadFacadeLocal.edit(estadoActividad);
                                logFacadeLocal.create(logFacadeLocal.crearLog("EstadoActividad", estadoActividad.getId() + "", "EDITAR", "|Nombre= " + estadoActividad.getNombre() + "|Observación= " + estadoActividad.getObservacion() + "|EsActivo= " + estadoActividad.getEsActivo(), usuario));
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estado") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionEstadoActividad.setEstadoActividad(new EstadoActividad());
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public boolean renderedCrear(Usuario usuario) {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_estado_actividad");
        if (tienePermiso == 1) {
            var = true;
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

    public void desactivar(EstadoActividad estadoActividad, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (estadoActividad.getId() != null) {
                estadoActividadFacadeLocal.edit(estadoActividad);
                logFacadeLocal.create(logFacadeLocal.crearLog("TipoActividad", estadoActividad.getId() + "", "EDITAR", "|Nombre= " + estadoActividad.getNombre() + "|Observación= " + estadoActividad.getObservacion() + "|EsActivo= " + estadoActividad.getEsActivo(), usuario));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estado") + " " + bundle.getString("lbl.msm_eliminar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public boolean renderedEditar(Usuario usuario) {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_estado_actividad");
        if (tienePermiso == 1) {
            var = true;
            noEditar = false;
        } else {
            noEditar = true;
        }
        return var;
    }

    public boolean renderedEliminar(Usuario usuario) {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_estado_actividad");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public void buscar(Usuario usuario, String criterio) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_estado_actividad");
            if (tienePermiso == 1) {
                this.estadoActividades = new ArrayList<>();
                estadoActividades = estadoActividadFacadeLocal.buscarPorNombre(criterio);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + " " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public List<EstadoActividad> listadoEstadoActividadesActivos() {
        try {
            return estadoActividadFacadeLocal.buscarActivos();
        } catch (Exception e) {
        }
        return null;
    }

    public void buscarTodo(Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_estado_actividad");
            if (tienePermiso == 1) {
                this.estadoActividades = new ArrayList<>();
                estadoActividades = estadoActividadFacadeLocal.findAll();
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + " " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public SessionEstadoActividad getSessionEstadoActividad() {
        return sessionEstadoActividad;
    }

    public void setSessionEstadoActividad(SessionEstadoActividad sessionEstadoActividad) {
        this.sessionEstadoActividad = sessionEstadoActividad;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<EstadoActividad> getEstadoActividades() {
        return estadoActividades;
    }

    public void setEstadoActividades(List<EstadoActividad> estadoActividades) {
        this.estadoActividades = estadoActividades;
    }

    public boolean isNoEditar() {
        return noEditar;
    }

    public void setNoEditar(boolean noEditar) {
        this.noEditar = noEditar;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

}
