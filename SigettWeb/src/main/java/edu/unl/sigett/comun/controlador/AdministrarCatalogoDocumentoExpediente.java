/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.postulacion.managed.session.SessionCatalogoDocumentoExpediente;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.CatalogoDocumentoExpediente;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.IOException;
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
import edu.unl.sigett.dao.CatalogoDocumentoExpedienteFacadeLocal;
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
            id = "editarCategoriaRequisito",
            pattern = "/editarCategoriaRequisito/#{sessionCatalogoDocumentoExpediente.catalogoDocumentoExpediente.id}",
            viewId = "/faces/pages/sigett/editarCatalogoDocumentoExpediente.xhtml"
    ),
    @URLMapping(
            id = "crearCategoriaRequisito",
            pattern = "/crearCategoriaRequisito/",
            viewId = "/faces/pages/sigett/editarCatalogoDocumentoExpediente.xhtml"
    ),
    @URLMapping(
            id = "categoriasRequisitos",
            pattern = "/categoriasRequisitos/",
            viewId = "/faces/pages/sigett/buscarCatalogoDocumentoExpediente.xhtml"
    )})
public class AdministrarCatalogoDocumentoExpediente implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionCatalogoDocumentoExpediente sessionCatalogoDocumentoExpediente;
    private String criterio;
    @EJB
    private CatalogoDocumentoExpedienteFacadeLocal catalogoDocumentoExpedienteFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    private List<CatalogoDocumentoExpediente> catalogoDocumentoExpedientes;
    private boolean renderedNoEditar;
    private boolean renderedBuscar;

    public AdministrarCatalogoDocumentoExpediente() {
    }
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_expediente");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_catalogo_documento_expediente");
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
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_catalogo_documento_expediente");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public void remover(CatalogoDocumentoExpediente catalogoDocumentoExpediente) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_catalogo_documento_expediente");
            if (tienePermiso == 1) {
                catalogoDocumentoExpedienteFacadeLocal.edit(catalogoDocumentoExpediente);
                if (catalogoDocumentoExpediente.getEsActivo()) {
                    logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "ACTIVAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion() + "|EsActivo= " + catalogoDocumentoExpediente.getEsActivo() + "|EsObligatorio= " + catalogoDocumentoExpediente.getEsObligatorio(), sessionUsuario.getUsuario()));
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_desactivado"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "DESACTIVAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion() + "|EsActivo= " + catalogoDocumentoExpediente.getEsActivo() + "|EsObligatorio= " + catalogoDocumentoExpediente.getEsObligatorio(), sessionUsuario.getUsuario()));
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_desactivado"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                buscar("");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public String crear() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (param.equalsIgnoreCase("crear")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_expediente");
                if (tienePermiso == 1) {
                    sessionCatalogoDocumentoExpediente.setCatalogoDocumentoExpediente(new CatalogoDocumentoExpediente());
                    navegacion = "pretty:crearCategoriaRequisito";
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                if (param.equalsIgnoreCase("crear-dlg")) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_expediente");
                    if (tienePermiso == 1) {
                        sessionCatalogoDocumentoExpediente.setCatalogoDocumentoExpediente(new CatalogoDocumentoExpediente());
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarCatDocExpediente').show()");
                    } else {
                        if (tienePermiso == 2) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(CatalogoDocumentoExpediente catalogoDocumentoExpediente) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_catalogo_documento_expediente");
            if (tienePermiso == 1) {
                sessionCatalogoDocumentoExpediente.setCatalogoDocumentoExpediente(catalogoDocumentoExpediente);
                navegacion = "pretty:editarCategoriaRequisito";
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
        catalogoDocumentoExpedientes = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_catalogo_documento_expediente");
            if (tienePermiso == 1) {
                catalogoDocumentoExpedientes = catalogoDocumentoExpedienteFacadeLocal.buscar(criterio);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("pretty:login");
                }
            }
        } catch (IOException e) {
        }
    }

    public String grabar(CatalogoDocumentoExpediente catalogoDocumentoExpediente) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (catalogoDocumentoExpediente.getId() == null) {
                if (param.equalsIgnoreCase("grabar")) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_expediente");
                    if (tienePermiso == 1) {
                        catalogoDocumentoExpedienteFacadeLocal.create(catalogoDocumentoExpediente);
                        logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "CREAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion(), sessionUsuario.getUsuario()));
                        sessionCatalogoDocumentoExpediente.setCatalogoDocumentoExpediente(new CatalogoDocumentoExpediente());
                        navegacion = "pretty:categoriasRequisitos";
                    } else {
                        if (tienePermiso == 2) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.requisito") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            navegacion = "pretty:login";
                        }
                    }
                } else {
                    if (param.equalsIgnoreCase("grabar-editar")) {
                        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_expediente");
                        if (tienePermiso == 1) {
                            catalogoDocumentoExpedienteFacadeLocal.create(catalogoDocumentoExpediente);
                            logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "CREAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.requisito") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (tienePermiso == 2) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            } else {
                                navegacion = "pretty:login";
                            }
                        }
                    } else {
                        if (param.equalsIgnoreCase("grabar-crear")) {
                            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_expediente");
                            if (tienePermiso == 1) {
                                catalogoDocumentoExpedienteFacadeLocal.create(catalogoDocumentoExpediente);
                                logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "CREAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion(), sessionUsuario.getUsuario()));
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.requisito") + " " + bundle.getString("lbl.msm_grabar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionCatalogoDocumentoExpediente.setCatalogoDocumentoExpediente(new CatalogoDocumentoExpediente());
                            } else {
                                if (tienePermiso == 2) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    navegacion = "pretty:login";
                                }
                            }
                        } else {
                            if (param.equalsIgnoreCase("grabar-dlg")) {
                                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_expediente");
                                if (tienePermiso == 1) {
                                    catalogoDocumentoExpedienteFacadeLocal.create(catalogoDocumentoExpediente);
                                    logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "CREAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion(), sessionUsuario.getUsuario()));
                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarCatDocExpediente').hide()");
                                } else {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                }
                            }
                        }
                    }
                }
                buscar("");
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_expediente");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("grabar")) {
                        catalogoDocumentoExpedienteFacadeLocal.edit(catalogoDocumentoExpediente);
                        logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "EDITAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion(), sessionUsuario.getUsuario()));
                        sessionCatalogoDocumentoExpediente.setCatalogoDocumentoExpediente(new CatalogoDocumentoExpediente());
                        navegacion = "pretty:categoriasRequisitos";
                    } else {
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            catalogoDocumentoExpedienteFacadeLocal.edit(catalogoDocumentoExpediente);
                            logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "EDITAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion(), sessionUsuario.getUsuario()));
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.requisito") + " " + bundle.getString("lbl.msm_editar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (param.equalsIgnoreCase("grabar-crear")) {
                                catalogoDocumentoExpedienteFacadeLocal.edit(catalogoDocumentoExpediente);
                                logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoExpediente", catalogoDocumentoExpediente.getId() + "", "CREAR", "|Nombre= " + catalogoDocumentoExpediente.getNombre() + "|Descripcion= " + catalogoDocumentoExpediente.getDescripcion(), sessionUsuario.getUsuario()));
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.requisito") + " " + bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                sessionCatalogoDocumentoExpediente.setCatalogoDocumentoExpediente(new CatalogoDocumentoExpediente());
                            }
                        }
                    }
                    buscar("");
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
        }
        return navegacion;
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

    public SessionCatalogoDocumentoExpediente getSessionCatalogoDocumentoExpediente() {
        return sessionCatalogoDocumentoExpediente;
    }

    public void setSessionCatalogoDocumentoExpediente(SessionCatalogoDocumentoExpediente sessionCatalogoDocumentoExpediente) {
        this.sessionCatalogoDocumentoExpediente = sessionCatalogoDocumentoExpediente;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<CatalogoDocumentoExpediente> getCatalogoDocumentoExpedientes() {
        return catalogoDocumentoExpedientes;
    }

    public void setCatalogoDocumentoExpedientes(List<CatalogoDocumentoExpediente> catalogoDocumentoExpedientes) {
        this.catalogoDocumentoExpedientes = catalogoDocumentoExpedientes;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

//</editor-fold>
}
