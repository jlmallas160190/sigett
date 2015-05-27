/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import edu.unl.sigett.postulacion.managed.session.SessionDocumentoProyecto;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionDocenteUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionEstudianteUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.Proyecto;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import edu.unl.sigett.dao.DocumentoProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarDocumentosProyecto implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionDocumentoProyecto sessionDocumentoProyecto;
    @Inject
    private SessionDocenteUsuario sessionDocenteUsuario;
    @Inject
    private SessionEstudianteUsuario sessionEstudianteUsuario;
    private UploadedFile uploadedFile;
    @EJB
    private LogDao logFacadeLocal;
    private String catalogoDocumentoProyecto;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private DocumentoProyectoFacadeLocal documentoProyectoFacadeLocal;

    private List<DocumentoProyecto> documentoProyectos;
    private List<DocumentoProyecto> anteproyectos;
    private List<DocumentoProyecto> documentoProyectosPorAutor;

    private DocumentoProyecto documentoProyecto;

    private boolean renderedNoEditar;
    private boolean renderedNoEditarAutor;
    private boolean esEditado = false;
    private boolean renderedEditar;
    private boolean renderedEliminar;
    private boolean renderedCrear;
    private boolean renderedEditarAutor;
    private boolean renderedEliminarAutor;
    private boolean renderedCrearAutor;
    private boolean renderedBuscar;
    private boolean renderedDlgEditar;

    @PostConstruct
    public void AdministrarDocumentosProyecto() {
        this.renderedDlgEditar = false;
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_documento_proyecto");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_documento_proyecto");
        if (tienePermiso == 1) {
            renderedCrear = true;
            renderedCrearAutor = true;
        } else {
            renderedCrear = false;
            renderedCrearAutor = false;
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_documento_proyecto");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedEditarAutor = true;
            renderedNoEditar = false;
            renderedNoEditarAutor = false;
        } else {
            renderedNoEditar = true;
            renderedNoEditarAutor = true;
            renderedEditar = false;
            renderedEditarAutor = false;
        }
    }

    public void renderedEliminar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_documento_proyecto");
        if (tienePermiso == 1) {
            renderedEliminar = true;
            renderedEliminarAutor = true;
        } else {
            renderedEliminarAutor = false;
            renderedEliminar = false;
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void buscar(Proyecto proyecto) {
        this.documentoProyectos = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_documento_proyecto");
            if (tienePermiso == 1) {
                for (DocumentoProyecto documentoProyecto : proyecto.getDocumentoProyectoList()) {
                    if (documentoProyecto.getEsActivo()) {
                        documentoProyectos.add(documentoProyecto);
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void downloadDocumento(DocumentoProyecto documentoProyecto) {
        try {
//            FacesContext context = FacesContext.getCurrentInstance();
//            HttpServletResponse response = (HttpServletResponse) context.getCurrentInstance().getExternalContext().getResponse();
//            response.setHeader("Content-Disposition", "attachment; filename=\"" + documentoProyecto.getCatalogoDocumentoProyectoId().getNombre() + ".pdf");
//            response.setContentType("application/pdf");
//            response.setContentLength(documentoProyecto.getDocumento().length);
//            response.getOutputStream().write(documentoProyecto.getDocumento());
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
//            context.responseComplete();
        } catch (Exception e) {
        }
    }

    public void buscarPorAutor(Proyecto proyecto) {
        this.documentoProyectosPorAutor = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionEstudianteUsuario.getUsuario(), "buscar_autor_proyecto");
            if (tienePermiso == 1) {
                for (DocumentoProyecto documentoProyecto : documentoProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                    if (documentoProyecto.getEsActivo()) {
                        documentoProyectosPorAutor.add(documentoProyecto);
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }

    }

    public void buscarAnteproyectos(Proyecto proyecto, Usuario usuario) {
        this.anteproyectos = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_documento_proyecto");
            if (tienePermiso == 1) {
                for (DocumentoProyecto documentoProyecto : documentoProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                    anteproyectos.add(documentoProyecto);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    public String obtenerSizeKB(DocumentoProyecto documentoProyecto) {
////        double valorKB = documentoProyecto.getTamanio() / 1024;
////        valorKB = Math.round(valorKB * 100);
////        valorKB = valorKB / 100;
////        return valorKB + " kB";
//    }

    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_documento_proyecto");
            if (tienePermiso == 1) {
                sessionDocumentoProyecto.setDocumentoProyecto(new DocumentoProyecto());
                esEditado = false;
                if (param.equalsIgnoreCase("crear-dlg")) {
                    this.renderedDlgEditar = true;
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').show()");
                } else {
                    navegacion = "pretty:editarDocumentoProyecto";
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(DocumentoProyecto documentoProyecto, Usuario usuario) {
        String navegacion = "";
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_documento_proyecto");
//            if (tienePermiso == 1) {
//                sessionDocumentoProyecto.setDocumentoProyecto(documentoProyecto);
//                catalogoDocumentoProyecto = documentoProyecto.getCatalogoDocumentoProyectoId().toString();
//                esEditado = true;
//                if (param.equalsIgnoreCase("editar-dlg")) {
//                    this.renderedDlgEditar = true;
//                    RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').show()");
//                } else {
//                    navegacion = "pretty:editarDocumentoProyecto";
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            String tipoArchivo = event.getFile().getContentType();
//            sessionDocumentoProyecto.getDocumentoProyecto().setTipoArchivo(tipoArchivo);
//            sessionDocumentoProyecto.getDocumentoProyecto().setTamanio(event.getFile().getSize());
//            sessionDocumentoProyecto.getDocumentoProyecto().setDocumento(event.getFile().getContents());
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.uploaded"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public String grabar(DocumentoProyecto documentoProyecto, Proyecto proyecto, Usuario usuario) {
        String navegacion = "";
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//        documentoProyecto.setProyectoId(proyecto);
//        Calendar fecha = Calendar.getInstance();
//        documentoProyecto.setFecha(fecha.getTime());
//        documentoProyecto.setEsActivo(true);
//        try {
//            int posCat = catalogoDocumentoProyecto.indexOf(":");
//            CatalogoDocumentoProyecto cdp = catalogoDocumentoProyectoFacadeLocal.find(Integer.parseInt(catalogoDocumentoProyecto.substring(0, posCat)));
//            if (cdp != null) {
//                documentoProyecto.setCatalogoDocumentoProyectoId(cdp);
//            }
//            if (documentoProyecto.getId() == null) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_documento_proyecto");
//                if (tienePermiso == 1) {
//                    documentoProyectoFacadeLocal.create(documentoProyecto);
//                    logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoProyecto", documentoProyecto.getId() + "", "CREAR", "|TipoArchivo= " + documentoProyecto.getTipoArchivo() + "|Tamaño=" + documentoProyecto.getTamanio() + "|CatalogoDocumentoProyecto= " + documentoProyecto.getCatalogoDocumentoProyectoId() + "|Proyecto= " + documentoProyecto.getProyectoId().getId(), usuario));
//                    if (param.equalsIgnoreCase("grabar-dlg")) {
//                        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').hide()");
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        sessionDocumentoProyecto.setDocumentoProyecto(new DocumentoProyecto());
//                    } else {
//                        if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    }
//                    buscarPorAutor(proyecto);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_documento_proyecto");
//                if (tienePermiso == 1) {
//                    documentoProyectoFacadeLocal.edit(documentoProyecto);
//                    logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoProyecto", documentoProyecto.getId() + "", "EDITAR", "|TipoArchivo= " + documentoProyecto.getTipoArchivo() + "|Tamaño=" + documentoProyecto.getTamanio() + "|CatalogoDocumentoProyecto= " + documentoProyecto.getCatalogoDocumentoProyectoId() + "|Proyecto= " + documentoProyecto.getProyectoId().getId(), usuario));
//                    if (param.equalsIgnoreCase("grabar-dlg")) {
//                        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').hide()");
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        sessionDocumentoProyecto.setDocumentoProyecto(new DocumentoProyecto());
//                    } else {
//                        if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    }
//                    buscarPorAutor(proyecto);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
//        } catch (Exception e) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }

        return navegacion;
    }

    public String agregarDocumentoProyecto(DocumentoProyecto documentoProyecto, Proyecto proyecto) {
        String navegacion = "";
//        Calendar fecha = Calendar.getInstance();
//        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//            documentoProyecto.setEsEditado(true);
//            if (esEditado == false) {
//                if (documentoProyecto.getDocumento() != null) {
//                    int posCat = catalogoDocumentoProyecto.indexOf(":");
//                    CatalogoDocumentoProyecto cdp = catalogoDocumentoProyectoFacadeLocal.find(Integer.parseInt(catalogoDocumentoProyecto.substring(0, posCat)));
//                    if (cdp != null) {
//                        documentoProyecto.setCatalogoDocumentoProyectoId(cdp);
//                    }
//                    documentoProyecto.setEsActivo(true);
//                    documentoProyecto.setFecha(fecha.getTime());
//                    proyecto.getDocumentoProyectoList().add(documentoProyecto);
//                    if (param.equalsIgnoreCase("agregar-dlg")) {
//                        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').hide()");
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                    buscar(proyecto);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " " + bundle.getString("lbl.documento"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//
//            } else {
//                RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').hide()");
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//        } catch (Exception e) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
        return navegacion;
    }

    public void eliminar(DocumentoProyecto documentoProyecto, Proyecto proyecto, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_documento_proyecto");
            if (tienePermiso == 1) {
                if (documentoProyecto.getId() != null) {
                    documentoProyectoFacadeLocal.remove(documentoProyecto);
                    buscarPorAutor(proyecto);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void viewDocumento(DocumentoProyecto documento) {
        try {
            this.documentoProyecto = documento;
            this.renderedDlgEditar = true;
            RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').show()");
        } catch (Exception e) {
        }
    }

    public void remover(DocumentoProyecto documentoProyecto, Proyecto proyecto, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_documento_proyecto");
            if (tienePermiso == 1) {
                if (documentoProyecto.getId() != null) {
                    documentoProyecto.setEsActivo(false);
                    buscar(proyecto);
                } else {
                    proyecto.getDocumentoProyectoList().remove(documentoProyecto);
                    buscar(proyecto);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public DocumentoProyecto getDocumentoProyecto() {
        return documentoProyecto;
    }

    public void setDocumentoProyecto(DocumentoProyecto documentoProyecto) {
        this.documentoProyecto = documentoProyecto;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionProyecto getSessionProyecto() {
        return sessionProyecto;
    }

    public void setSessionProyecto(SessionProyecto sessionProyecto) {
        this.sessionProyecto = sessionProyecto;
    }

    public SessionDocumentoProyecto getSessionDocumentoProyecto() {
        return sessionDocumentoProyecto;
    }

    public void setSessionDocumentoProyecto(SessionDocumentoProyecto sessionDocumentoProyecto) {
        this.sessionDocumentoProyecto = sessionDocumentoProyecto;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getCatalogoDocumentoProyecto() {
        return catalogoDocumentoProyecto;
    }

    public void setCatalogoDocumentoProyecto(String catalogoDocumentoProyecto) {
        this.catalogoDocumentoProyecto = catalogoDocumentoProyecto;
    }

    public List<DocumentoProyecto> getDocumentoProyectos() {
        return documentoProyectos;
    }

    public void setDocumentoProyectos(List<DocumentoProyecto> documentoProyectos) {
        this.documentoProyectos = documentoProyectos;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
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

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public SessionDocenteUsuario getSessionDocenteUsuario() {
        return sessionDocenteUsuario;
    }

    public void setSessionDocenteUsuario(SessionDocenteUsuario sessionDocenteUsuario) {
        this.sessionDocenteUsuario = sessionDocenteUsuario;
    }

    public List<DocumentoProyecto> getAnteproyectos() {
        return anteproyectos;
    }

    public void setAnteproyectos(List<DocumentoProyecto> anteproyectos) {
        this.anteproyectos = anteproyectos;
    }

    public SessionEstudianteUsuario getSessionEstudianteUsuario() {
        return sessionEstudianteUsuario;
    }

    public void setSessionEstudianteUsuario(SessionEstudianteUsuario sessionEstudianteUsuario) {
        this.sessionEstudianteUsuario = sessionEstudianteUsuario;
    }

    public List<DocumentoProyecto> getDocumentoProyectosPorAutor() {
        return documentoProyectosPorAutor;
    }

    public void setDocumentoProyectosPorAutor(List<DocumentoProyecto> documentoProyectosPorAutor) {
        this.documentoProyectosPorAutor = documentoProyectosPorAutor;
    }

    public boolean isRenderedNoEditarAutor() {
        return renderedNoEditarAutor;
    }

    public void setRenderedNoEditarAutor(boolean renderedNoEditarAutor) {
        this.renderedNoEditarAutor = renderedNoEditarAutor;
    }

    public boolean isRenderedEditarAutor() {
        return renderedEditarAutor;
    }

    public void setRenderedEditarAutor(boolean renderedEditarAutor) {
        this.renderedEditarAutor = renderedEditarAutor;
    }

    public boolean isRenderedEliminarAutor() {
        return renderedEliminarAutor;
    }

    public void setRenderedEliminarAutor(boolean renderedEliminarAutor) {
        this.renderedEliminarAutor = renderedEliminarAutor;
    }

    public boolean isRenderedCrearAutor() {
        return renderedCrearAutor;
    }

    public void setRenderedCrearAutor(boolean renderedCrearAutor) {
        this.renderedCrearAutor = renderedCrearAutor;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }
//</editor-fold>

}
