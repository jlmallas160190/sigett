/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import edu.unl.sigett.comun.managed.session.SessionDocumentoExpediente;
import edu.unl.sigett.comun.managed.session.SessionExpediente;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.DocumentoExpediente;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import edu.unl.sigett.dao.DocumentoExpedienteFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarDocumentosExpediente implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionExpediente sessionExpediente;
    @Inject
    private SessionDocumentoExpediente sessionDocumentoExpediente;
    private boolean esEditado;
    private String catalogoDocumentoExpediente;    
    @EJB
    private DocumentoExpedienteFacadeLocal documentoExpedienteFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    private boolean renderedDlgEditar;
    private boolean renderedCrear;
    private boolean renderedEditar;
    private boolean renderedEliminar;
    private boolean renderedNoEditar;

    public AdministrarDocumentosExpediente() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_documento_expediente");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_documento_expediente");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
            renderedCrear = false;
        }
    }

    public boolean renderedBuscar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_documento_expediente");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public void renderedEliminar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_documento_expediente");
        if (tienePermiso == 1) {
            renderedEliminar = true;
        } else {
            renderedEliminar = false;
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear() {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (param.equalsIgnoreCase("crear")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_documento_expediente");
                if (tienePermiso == 1) {
                    sessionDocumentoExpediente.setDocumentoExpediente(new DocumentoExpediente());
                    esEditado = false;
                    navegacion = "pretty:editarDocumentoExpediente";
                } else {
                    if (tienePermiso == 2) {
                        renderedDlgEditar = true;
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                if (param.equalsIgnoreCase("crear-dlg")) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_documento_expediente");
                    if (tienePermiso == 1) {
                        renderedDlgEditar = true;
                        sessionDocumentoExpediente.setDocumentoExpediente(new DocumentoExpediente());
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoExpediente').show()");
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public String editar(DocumentoExpediente documentoExpediente) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (param.equalsIgnoreCase("editar")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_documento_expediente");
                if (tienePermiso == 1) {
                    sessionDocumentoExpediente.setDocumentoExpediente(documentoExpediente);
                    esEditado = true;
                    navegacion = "pretty:editarDocumentoExpediente";
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        navegacion = "pretty:login";
                    }
                }
            } else {
                if (param.equalsIgnoreCase("editar-dlg")) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_documento_expediente");
                    if (tienePermiso == 1) {
                        esEditado = true;
                        renderedDlgEditar = true;
                        sessionDocumentoExpediente.setDocumentoExpediente(documentoExpediente);
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoExpediente').show()");
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

//    public List<CatalogoDocumentoExpediente> listadoCatalogos() {
//        try {
//            return catalogoDocumentoExpedienteFacadeLocal.buscarActivos();
//        } catch (Exception e) {
//        }
//        return null;
//    }

    public String agregar(DocumentoExpediente documentoExpediente) {
        String navegacion = "";
        Calendar fecha = Calendar.getInstance();
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//            if (esEditado == false) {
//                if (documentoExpediente.getDocumento() != null) {
//                    int posCat = catalogoDocumentoExpediente.indexOf(":");
//                    CatalogoDocumentoExpediente cde = catalogoDocumentoExpedienteFacadeLocal.find(Integer.parseInt(catalogoDocumentoExpediente.substring(0, posCat)));
//                    if (cde != null) {
//                        documentoExpediente.setCatalogoDocumentoExpedienteId(cde);
//                    }
//                    documentoExpediente.setFecha(fecha.getTime());
//                    documentoExpediente.setExpedienteId(sessionExpediente.getExpediente());
//                    sessionExpediente.getExpediente().getDocumentoExpedienteList().add(documentoExpediente);
//                    if (param.equalsIgnoreCase("agregar-dlg")) {
//                        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoExpediente').hide()");
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " " + bundle.getString("lbl.msm_documento"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//
//            } else {
//                RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoExpediente').hide()");
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            String tipoArchivo = event.getFile().getContentType();
//            sessionDocumentoExpediente.getDocumentoExpediente().setTipoArchivo(tipoArchivo);
//            sessionDocumentoExpediente.getDocumentoExpediente().setTamanio(event.getFile().getSize());
//            sessionDocumentoExpediente.getDocumentoExpediente().setDocumento(event.getFile().getContents());
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl_documento") + " " + bundle.getString("lbl.uploaded"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            agregar(sessionDocumentoExpediente.getDocumentoExpediente());
        } catch (Exception e) {
        }

    }

    public void remover(DocumentoExpediente documentoExpediente) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (documentoExpediente.getId() == null) {
//                sessionExpediente.getExpediente().getDocumentoExpedienteList().remove(documentoExpediente);
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl_msm_eliminar"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            } else {
//                sessionExpediente.getExpediente().getDocumentoExpedienteList().remove(documentoExpediente);
//                logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoExpediente", documentoExpediente.getId() + "", "ELIMINAR", "|TipoArchivo= " + documentoExpediente.getTipoArchivo() + "|Catalogo= " + documentoExpediente.getCatalogoDocumentoExpedienteId(), sessionUsuario.getUsuario()));
//                documentoExpedienteFacadeLocal.remove(documentoExpediente);
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl_msm_eliminar"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }

        } catch (Exception e) {
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
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

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionExpediente getSessionExpediente() {
        return sessionExpediente;
    }

    public void setSessionExpediente(SessionExpediente sessionExpediente) {
        this.sessionExpediente = sessionExpediente;
    }

    public SessionDocumentoExpediente getSessionDocumentoExpediente() {
        return sessionDocumentoExpediente;
    }

    public void setSessionDocumentoExpediente(SessionDocumentoExpediente sessionDocumentoExpediente) {
        this.sessionDocumentoExpediente = sessionDocumentoExpediente;
    }

    public boolean isEsEditado() {
        return esEditado;
    }

    public void setEsEditado(boolean esEditado) {
        this.esEditado = esEditado;
    }

    public String getCatalogoDocumentoExpediente() {
        return catalogoDocumentoExpediente;
    }

    public void setCatalogoDocumentoExpediente(String catalogoDocumentoExpediente) {
        this.catalogoDocumentoExpediente = catalogoDocumentoExpediente;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }
//</editor-fold>

}
