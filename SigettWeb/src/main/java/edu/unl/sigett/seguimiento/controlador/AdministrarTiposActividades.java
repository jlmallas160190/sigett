/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguimiento.session.SessionTipoActividad;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.TipoActividad;
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
import org.jlmallas.seguridad.dao.LogDao;
import edu.unl.sigett.dao.TipoActividadFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarTipoActividad",
            pattern = "/editarTipoActividad/#{sessionTipoActividad.tipoActividad.id}",
            viewId = "/faces/pages/sigett/editarTipoActividad.xhtml"
    ),
    @URLMapping(
            id = "crearTipoActividad",
            pattern = "/crearTipoActividad/",
            viewId = "/faces/pages/sigett/editarTipoActividad.xhtml"
    ),
    @URLMapping(
            id = "tiposActividades",
            pattern = "/tiposActividades/",
            viewId = "/faces/pages/sigett/buscarTiposActividades.xhtml"
    )
})
public class AdministrarTiposActividades implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionTipoActividad sessionTipoActividad;
    private String criterio;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private TipoActividadFacadeLocal tipoActividadFacadeLocal;
    private List<TipoActividad> tipoActividades;
    private boolean noEditar;
    @EJB
    private UsuarioDao usuarioFacadeLocal;

    public AdministrarTiposActividades() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_tipo_actividad");
            if (tienePermiso == 1) {
                sessionTipoActividad.setTipoActividad(new TipoActividad());
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "pretty:crearTipoActividad";
                } else {
                    if (param.equalsIgnoreCase("crear-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarTipoActividad').show()");
                    }
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    if (param.equalsIgnoreCase("crear")) {
                        navegacion = "login?faces-redirect=true";
                    }
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void desactivar(TipoActividad tipoActividad) {
        try {
            if (tipoActividad.getId() != null) {
                tipoActividadFacadeLocal.edit(tipoActividad);
                logFacadeLocal.create(logFacadeLocal.crearLog("TipoActividad", tipoActividad.getId() + "", "EDITAR", "|Nombre= " + tipoActividad.getNombre() + "|Observación= " + tipoActividad.getDescripcion() + "|EsActivo= " + tipoActividad.getEsActivo(), sessionUsuario.getUsuario()));
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipo de Actividad desactivado correctamente...", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public String editar(TipoActividad tipoActividad) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_tipo_actividad");
            if (tienePermiso == 1) {
                sessionTipoActividad.setTipoActividad(tipoActividad);
                if (param.equalsIgnoreCase("editar")) {
                    navegacion = "pretty:editarTipoActividad";
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

    public String grabar(TipoActividad tipoActividad) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");

        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (tipoActividad.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_proyecto");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        tipoActividadFacadeLocal.create(tipoActividad);
                        logFacadeLocal.create(logFacadeLocal.crearLog("TipoActividad", tipoActividad.getId() + "", "CREAR", "|Nombre= " + tipoActividad.getNombre() + "|Observación= " + tipoActividad.getDescripcion() + "|EsActivo= " + tipoActividad.getEsActivo(), sessionUsuario.getUsuario()));
                        navegacion = "pretty:tiposActividades";
                        sessionTipoActividad.setTipoActividad(new TipoActividad());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            tipoActividadFacadeLocal.create(tipoActividad);
                            logFacadeLocal.create(logFacadeLocal.crearLog("TipoActividad", tipoActividad.getId() + "", "CREAR", "|Nombre= " + tipoActividad.getNombre() + "|Observación= " + tipoActividad.getDescripcion() + "|EsActivo= " + tipoActividad.getEsActivo(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipo") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                tipoActividadFacadeLocal.create(tipoActividad);
                                logFacadeLocal.create(logFacadeLocal.crearLog("TipoActividad", tipoActividad.getId() + "", "CREAR", "|Nombre= " + tipoActividad.getNombre() + "|Observación= " + tipoActividad.getDescripcion() + "|EsActivo= " + tipoActividad.getEsActivo(), sessionUsuario.getUsuario()));
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipo") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionTipoActividad.setTipoActividad(new TipoActividad());
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
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_catalogo_proyecto");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        tipoActividadFacadeLocal.edit(tipoActividad);
                        logFacadeLocal.create(logFacadeLocal.crearLog("TipoActividad", tipoActividad.getId() + "", "EDITAR", "|Nombre= " + tipoActividad.getNombre() + "|Observación= " + tipoActividad.getDescripcion() + "|EsActivo= " + tipoActividad.getEsActivo(), sessionUsuario.getUsuario()));
                        navegacion = "pretty:tiposActividades";
                        sessionTipoActividad.setTipoActividad(new TipoActividad());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            tipoActividadFacadeLocal.edit(tipoActividad);
                            logFacadeLocal.create(logFacadeLocal.crearLog("TipoActividad", tipoActividad.getId() + "", "EDITAR", "|Nombre= " + tipoActividad.getNombre() + "|Observación= " + tipoActividad.getDescripcion() + "|EsActivo= " + tipoActividad.getEsActivo(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipo") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                tipoActividadFacadeLocal.edit(tipoActividad);
                                logFacadeLocal.create(logFacadeLocal.crearLog("TipoActividad", tipoActividad.getId() + "", "EDITAR", "|Nombre= " + tipoActividad.getNombre() + "|Observación= " + tipoActividad.getDescripcion() + "|EsActivo= " + tipoActividad.getEsActivo(), sessionUsuario.getUsuario()));
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipo") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionTipoActividad.setTipoActividad(new TipoActividad());
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

    public void listadoTipoActividadesPorNombre(String criterio) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_tipo_actividad");
            if (tienePermiso == 1) {
                this.tipoActividades = new ArrayList<>();
                tipoActividades = tipoActividadFacadeLocal.buscarPorNombre(criterio);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para listar Tipos de actividades.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public List<TipoActividad> listadoTipoActividadesActivos() {
        try {
            return tipoActividadFacadeLocal.buscarActivos();
        } catch (Exception e) {
        }
        return null;
    }

    public void listadoTipoActividadesTodo() {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_tipo_actividad");
            if (tienePermiso == 1) {
                this.tipoActividades = new ArrayList<>();
                tipoActividades = tipoActividadFacadeLocal.findAll();
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para listar Tipos de actividades.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_tipo_actividad");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_tipo_actividad");
        if (tienePermiso == 1) {
            var = true;
            noEditar = false;
        } else {
            noEditar = true;
        }
        return var;
    }

    public boolean renderedEliminar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_tipo_actividad");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionTipoActividad getSessionTipoActividad() {
        return sessionTipoActividad;
    }

    public void setSessionTipoActividad(SessionTipoActividad sessionTipoActividad) {
        this.sessionTipoActividad = sessionTipoActividad;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<TipoActividad> getTipoActividades() {
        return tipoActividades;
    }

    public void setTipoActividades(List<TipoActividad> tipoActividades) {
        this.tipoActividades = tipoActividades;
    }

    public boolean isNoEditar() {
        return noEditar;
    }

    public void setNoEditar(boolean noEditar) {
        this.noEditar = noEditar;
    }
//</editor-fold>
}
