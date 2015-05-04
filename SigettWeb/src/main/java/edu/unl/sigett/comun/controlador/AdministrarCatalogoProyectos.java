/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.postulacion.managed.session.SessionCatalogoProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.CatalogoProyecto;
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
import org.primefaces.context.RequestContext;
import edu.unl.sigett.session.CatalogoProyectoFacadeLocal;
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
            id = "editarCategoria",
            pattern = "/editarCategoria/#{sessionCatalogoProyecto.catalogoProyecto.id}",
            viewId = "/faces/pages/sigett/editarCatalogoProyecto.xhtml"
    ),
    @URLMapping(
            id = "crearCategoria",
            pattern = "/crearCategoria/",
            viewId = "/faces/pages/sigett/editarCatalogoProyecto.xhtml"
    ),
    @URLMapping(
            id = "categorias",
            pattern = "/categorias/",
            viewId = "/faces/pages/sigett/buscarCatalogoProyectos.xhtml"
    )
})
public class AdministrarCatalogoProyectos implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionCatalogoProyecto sessionCatalogoProyecto;
    private String criterio;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private CatalogoProyectoFacadeLocal catalogoProyectoFacadeLocal;
    private boolean renderedNoEditar;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    private List<CatalogoProyecto> catalogoProyectos;
    private boolean renderedCrearAux;
    private boolean renderedCrear;
    private boolean renderedBuscar;
    private String catalogo;

    public AdministrarCatalogoProyectos() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_proyecto");
            if (tienePermiso == 1) {
                sessionCatalogoProyecto.setCatalogoProyecto(new CatalogoProyecto());
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "pretty:crearCategoria";
                } else {
                    if (param.equalsIgnoreCase("crear-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarCatProyecto').show()");
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

    public void desactivar(CatalogoProyecto catalogoProyecto) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_catalogo_proyecto");
            if (tienePermiso == 1) {
                catalogoProyectoFacadeLocal.edit(catalogoProyecto);
                if (catalogoProyecto.getEsActivo()) {
                    logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoProyecto", catalogoProyecto.getId() + "", "ACTIVAR", "|Nombre= " + catalogoProyecto.getNombre() + "|Descripcion= " + catalogoProyecto.getDescripcion() + "|esActivo= " + catalogoProyecto.getEsActivo(), sessionUsuario.getUsuario()));
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoProyecto", catalogoProyecto.getId() + "", "DESACTIVAR", "|Nombre= " + catalogoProyecto.getNombre() + "|Descripcion= " + catalogoProyecto.getDescripcion() + "|esActivo= " + catalogoProyecto.getEsActivo(), sessionUsuario.getUsuario()));
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listadoCatalogoProyectos(String criterio) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_catalogo_proyecto");
            if (tienePermiso == 1) {
                catalogoProyectos = catalogoProyectoFacadeLocal.buscarPorCriterio(criterio);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("pretty:login");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<CatalogoProyecto> buscarActivos() {
        try {
            return catalogoProyectoFacadeLocal.buscarActivos();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String editar(CatalogoProyecto catalogoProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_catalogo_proyecto");
            if (tienePermiso == 1) {
                sessionCatalogoProyecto.setCatalogoProyecto(catalogoProyecto);
                navegacion = "pretty:editarCategoria";
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

    public String grabar(CatalogoProyecto catalogoProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (catalogoProyecto.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_proyecto");
                if (tienePermiso == 1) {
                    catalogoProyectoFacadeLocal.create(catalogoProyecto);
                    logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoProyecto", catalogoProyecto.getId() + "", "CREAR", "Nombre= " + catalogoProyecto.getNombre() + "|Descripcion= " + catalogoProyecto.getDescripcion() + "|EsActivo= " + catalogoProyecto.getEsActivo(), sessionUsuario.getUsuario()));
                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:categorias";
                        sessionCatalogoProyecto.setCatalogoProyecto(new CatalogoProyecto());
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_grabar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            navegacion = "";
                        } else {
                            if (param.equalsIgnoreCase("grabar-dlg")) {
                                RequestContext.getCurrentInstance().execute("PF('dlgEditarCatProyecto').hide()");
                                sessionCatalogoProyecto.setCatalogoProyecto(new CatalogoProyecto());
                            } else {
                                sessionCatalogoProyecto.setCatalogoProyecto(new CatalogoProyecto());
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_catalogo_proyecto");
                if (tienePermiso == 1) {
                    catalogoProyectoFacadeLocal.edit(catalogoProyecto);
                    logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoProyecto", catalogoProyecto.getId() + "", "CREAR", "|Nombre= " + catalogoProyecto.getNombre() + "|Descripcion= " + catalogoProyecto.getDescripcion() + "|EsActivo= " + catalogoProyecto.getEsActivo(), sessionUsuario.getUsuario()));

                    if (param.equalsIgnoreCase("grabar")) {
                        navegacion = "pretty:categorias";
                        sessionCatalogoProyecto.setCatalogoProyecto(new CatalogoProyecto());
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            navegacion = "";
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-dlg")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                RequestContext.getCurrentInstance().execute("PF('dlgEditarCatProyecto').hide()");
                            } else {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.categoria") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                navegacion = crear();
                            }
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
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
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_proyecto");
        if (tienePermiso == 1) {
            var = true;
            renderedCrearAux = false;
            renderedCrear = true;
        } else {
            renderedCrear = false;
            renderedCrearAux = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_catalogo_proyecto");
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
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_catalogo_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_catalogo_proyecto");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionCatalogoProyecto getSessionCatalogoProyecto() {
        return sessionCatalogoProyecto;
    }

    public void setSessionCatalogoProyecto(SessionCatalogoProyecto sessionCatalogoProyecto) {
        this.sessionCatalogoProyecto = sessionCatalogoProyecto;
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

    public List<CatalogoProyecto> getCatalogoProyectos() {
        return catalogoProyectos;
    }

    public void setCatalogoProyectos(List<CatalogoProyecto> catalogoProyectos) {
        this.catalogoProyectos = catalogoProyectos;
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

    public String getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }
//</editor-fold>
}
