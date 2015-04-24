/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import edu.unl.sigett.postulacion.managed.session.SessionCatalogoDocumentoProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.CatalogoDocumentoProyecto;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.session.CatalogoDocumentoProyectoFacadeLocal;
import com.jlmallas.seguridad.session.LogFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarCatalogoDocumentoProyecto implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionCatalogoDocumentoProyecto sessionCatalogoDocumentoProyecto;
    @EJB
    private LogFacadeLocal logFacadeLocal;
    @EJB
    private CatalogoDocumentoProyectoFacadeLocal catalogoDocumentoProyectoFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    public AdministrarCatalogoDocumentoProyecto() {
    }
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedBuscar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_catalogo_documento_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (param.equalsIgnoreCase("crear-dlg")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_proyecto");
                if (tienePermiso == 1) {
                    sessionCatalogoDocumentoProyecto.setCatalogoDocumentoProyecto(new CatalogoDocumentoProyecto());
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarCatDocProyecto').show()");
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para crear Catalogo Documento Proyecto...", "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            } else {
                if (param.equalsIgnoreCase("crear")) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_proyecto");
                    if (tienePermiso == 1) {
                        sessionCatalogoDocumentoProyecto.setCatalogoDocumentoProyecto(new CatalogoDocumentoProyecto());
                    } else {
                        if (tienePermiso == 2) {
                            navegacion = "pretty:principal";
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para crear Catalogo Documento Proyecto...", "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            navegacion = "pretty:login";
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public String editar(CatalogoDocumentoProyecto catalogoDocumentoProyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_catalogo_documento_proyecto");
            if (tienePermiso == 1) {
                sessionCatalogoDocumentoProyecto.setCatalogoDocumentoProyecto(catalogoDocumentoProyecto);
                if (param.equalsIgnoreCase("editar")) {
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarCatalogoDocumentoProyecto').show()");
                }
            } else {
                if (tienePermiso == 2) {
                    navegacion = "pretty:principal";
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para editar Catalogo Documento Proyecto...", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String grabar(CatalogoDocumentoProyecto catalogoDocumentoProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (catalogoDocumentoProyecto.getId() == null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_catalogo_documento_proyecto");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("guardar")) {
                        catalogoDocumentoProyectoFacadeLocal.create(catalogoDocumentoProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoProyecto", catalogoDocumentoProyecto.getId() + "", "CREAR", "NUEVO: |Nombre= " + catalogoDocumentoProyecto.getNombre() + "|Descripción= " + catalogoDocumentoProyecto.getDescripcion(), sessionUsuario.getUsuario()));
                        sessionCatalogoDocumentoProyecto.setCatalogoDocumentoProyecto(new CatalogoDocumentoProyecto());
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarCatalogoDocumentoProyecto').hide()");
                    } else {
                        if (param.equalsIgnoreCase("guardar-editar")) {
                            catalogoDocumentoProyecto.setNombre(new String(catalogoDocumentoProyecto.getNombre().getBytes()));
                            catalogoDocumentoProyecto.setDescripcion(new String(catalogoDocumentoProyecto.getDescripcion().getBytes()));
                            catalogoDocumentoProyectoFacadeLocal.create(catalogoDocumentoProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoProyecto", catalogoDocumentoProyecto.getId() + "", "CREAR", "NUEVO: |Nombre= " + catalogoDocumentoProyecto.getNombre() + "|Descripción= " + catalogoDocumentoProyecto.getDescripcion(), sessionUsuario.getUsuario()));
                        } else {
                            catalogoDocumentoProyectoFacadeLocal.create(catalogoDocumentoProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoProyecto", catalogoDocumentoProyecto.getId() + "", "CREAR", "NUEVO: |Nombre= " + catalogoDocumentoProyecto.getNombre() + "|Descripción= " + catalogoDocumentoProyecto.getDescripcion(), sessionUsuario.getUsuario()));
                            sessionCatalogoDocumentoProyecto.setCatalogoDocumentoProyecto(new CatalogoDocumentoProyecto());

                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        navegacion = "";
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para crear CatalogoDocumentoProyecto...", "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_catalogo_documento_proyecto");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("guardar")) {
                        catalogoDocumentoProyectoFacadeLocal.edit(catalogoDocumentoProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoProyecto", catalogoDocumentoProyecto.getId() + "", "EDITAR", "NUEVO: |Nombre= " + catalogoDocumentoProyecto.getNombre() + "|Descripción= " + catalogoDocumentoProyecto.getDescripcion(), sessionUsuario.getUsuario()));
                        sessionCatalogoDocumentoProyecto.setCatalogoDocumentoProyecto(new CatalogoDocumentoProyecto());
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarCatalogoDocumentoProyecto').hide()");
                    } else {
                        if (param.equalsIgnoreCase("guardar-editar")) {
                            catalogoDocumentoProyecto.setNombre(new String(catalogoDocumentoProyecto.getNombre().getBytes()));
                            catalogoDocumentoProyecto.setDescripcion(new String(catalogoDocumentoProyecto.getDescripcion().getBytes()));
                            catalogoDocumentoProyectoFacadeLocal.edit(catalogoDocumentoProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoProyecto", catalogoDocumentoProyecto.getId() + "", "EDITAR", "NUEVO: |Nombre= " + catalogoDocumentoProyecto.getNombre() + "|Descripción= " + catalogoDocumentoProyecto.getDescripcion(), sessionUsuario.getUsuario()));
                        } else {
                            catalogoDocumentoProyectoFacadeLocal.edit(catalogoDocumentoProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("CatalogoDocumentoProyecto", catalogoDocumentoProyecto.getId() + "", "EDITAR", "NUEVO: |Nombre= " + catalogoDocumentoProyecto.getNombre() + "|Descripción= " + catalogoDocumentoProyecto.getDescripcion(), sessionUsuario.getUsuario()));
                            sessionCatalogoDocumentoProyecto.setCatalogoDocumentoProyecto(new CatalogoDocumentoProyecto());
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        navegacion = "";
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para crear CatalogoDocumentoProyecto...", "");
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

    public List<CatalogoDocumentoProyecto> buscarActivos() {
        try {
            return catalogoDocumentoProyectoFacadeLocal.buscarActivos();
        } catch (Exception e) {
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

    public SessionCatalogoDocumentoProyecto getSessionCatalogoDocumentoProyecto() {
        return sessionCatalogoDocumentoProyecto;
    }

    public void setSessionCatalogoDocumentoProyecto(SessionCatalogoDocumentoProyecto sessionCatalogoDocumentoProyecto) {
        this.sessionCatalogoDocumentoProyecto = sessionCatalogoDocumentoProyecto;
    }
//</editor-fold>
}
