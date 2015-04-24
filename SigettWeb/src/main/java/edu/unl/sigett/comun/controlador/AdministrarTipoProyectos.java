/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.postulacion.managed.session.SessionTipoProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.TipoProyecto;
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
import com.jlmallas.seguridad.session.LogFacadeLocal;
import edu.unl.sigett.session.TipoProyectoFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarTipoProyecto",
            pattern = "/editarTipoProyecto/#{sessionTipoProyecto.tipoProyecto.id}",
            viewId = "/faces/pages/sigett/editarTipoProyecto.xhtml"
    ),
    @URLMapping(
            id = "crearTipoProyecto",
            pattern = "/crearTipoProyecto/",
            viewId = "/faces/pages/sigett/editarTipoProyecto.xhtml"
    ),
    @URLMapping(
            id = "tiposProyecto",
            pattern = "/tiposProyecto/",
            viewId = "/faces/pages/sigett/buscarTipoProyectos.xhtml"
    )
})
public class AdministrarTipoProyectos implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionTipoProyecto sessionTipoProyecto;
    @EJB
    private TipoProyectoFacadeLocal tipoProyectoFacadeLocal;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    private String criterio;
    private boolean renderedNoEditar = false;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private List<TipoProyecto> tipoProyectos;
    private boolean renderedCrearAux;
    private boolean renderedCrear;
    private boolean renderedBuscar;

    public AdministrarTipoProyectos() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_tipo_proyecto");
        if (tienePermiso == 1) {
            var = true;
            renderedCrear = true;
            renderedCrearAux = false;
        } else {
            renderedCrear = false;
            renderedCrearAux = true;
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

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_tipo_proyecto");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }

    public boolean renderedEliminar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_tipo_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODO CRUD">

    public String crear() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_tipo_proyecto");
            if (tienePermiso == 1) {
                sessionTipoProyecto.setTipoProyecto(new TipoProyecto());
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "pretty:crearTipoProyecto";
                } else {
                    if (param.equalsIgnoreCase("crear-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarTipoProyecto').show()");
                    }
                }
            } else {
                if (tienePermiso == 2) {
                    navegacion = "";
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + " " + bundle.getString("lbl.tipo") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(TipoProyecto tipoProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_tipo_proyecto");
            if (tienePermiso == 1) {
                sessionTipoProyecto.setTipoProyecto(tipoProyecto);
                navegacion = "pretty:editarTipoProyecto";
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

    public String grabar(TipoProyecto tipoProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (tipoProyecto.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_tipo_proyecto");
                if (tienePermiso == 1) {
                    tipoProyectoFacadeLocal.create(tipoProyecto);
                    logFacadeLocal.create(logFacadeLocal.crearLog("TipoProyecto", tipoProyecto.getId() + "", "CREAR", "|Nombre= " + tipoProyecto.getNombre() + "|Descripcion= " + tipoProyecto.getDescripcion(), sessionUsuario.getUsuario()));
                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:tiposProyecto";
                        sessionTipoProyecto.setTipoProyecto(new TipoProyecto());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipoProyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-dlg")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipoProyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionTipoProyecto.setTipoProyecto(new TipoProyecto());
                                RequestContext.getCurrentInstance().execute("PF('dlgEditarTipoProyecto').hide()");
                            } else {
                                sessionTipoProyecto.setTipoProyecto(new TipoProyecto());
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipoProyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + " " + bundle.getString("lbl.tipo") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_tipo_proyecto");
                if (tienePermiso == 1) {
                    tipoProyectoFacadeLocal.edit(tipoProyecto);
                    logFacadeLocal.create(logFacadeLocal.crearLog("TipoProyecto", tipoProyecto.getId() + "", "CREAR", "|Nombre= " + tipoProyecto.getNombre() + "|Descripcion= " + tipoProyecto.getDescripcion(), sessionUsuario.getUsuario()));
                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:tiposProyecto";
                        sessionTipoProyecto.setTipoProyecto(new TipoProyecto());
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipoProyecto") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            sessionTipoProyecto.setTipoProyecto(new TipoProyecto());
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.tipoProyecto") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        navegacion = "";
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + " " + bundle.getString("lbl.tipo") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void desactivar(TipoProyecto tipoProyecto) {
        try {
            if (tipoProyecto.getId() != null) {
                tipoProyectoFacadeLocal.edit(tipoProyecto);
                if (tipoProyecto.getEsActivo()) {
                    logFacadeLocal.create(logFacadeLocal.crearLog("TipoProyecto", tipoProyecto.getId() + "", "ACTIVAR", "|Nombre= " + tipoProyecto.getNombre() + "|Descripcion= " + tipoProyecto.getDescripcion() + "|esActivo= " + tipoProyecto.getEsActivo(), sessionUsuario.getUsuario()));
                } else {
                    logFacadeLocal.create(logFacadeLocal.crearLog("TipoProyecto", tipoProyecto.getId() + "", "DESACTIVAR", "|Nombre= " + tipoProyecto.getNombre() + "|Descripcion= " + tipoProyecto.getDescripcion() + "|esActivo= " + tipoProyecto.getEsActivo(), sessionUsuario.getUsuario()));

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listadoTipoProyectos(String criterio) {
        this.tipoProyectos = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_tipo_proyecto");
            if (tienePermiso == 1) {
                tipoProyectos = tipoProyectoFacadeLocal.buscarPorCriterio(criterio);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<TipoProyecto> buscarActivos() {
        try {
            try {
                return tipoProyectoFacadeLocal.buscarActivos();
            } catch (Exception e) {
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionTipoProyecto getSessionTipoProyecto() {
        return sessionTipoProyecto;
    }

    public void setSessionTipoProyecto(SessionTipoProyecto sessionTipoProyecto) {
        this.sessionTipoProyecto = sessionTipoProyecto;
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

    public List<TipoProyecto> getTipoProyectos() {
        return tipoProyectos;
    }

    public void setTipoProyectos(List<TipoProyecto> tipoProyectos) {
        this.tipoProyectos = tipoProyectos;
    }

    public boolean isRenderedCrearAux() {
        return renderedCrearAux;
    }

    public void setRenderedCrearAux(boolean renderedCrearAux) {
        this.renderedCrearAux = renderedCrearAux;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }
//</editor-fold>

}
