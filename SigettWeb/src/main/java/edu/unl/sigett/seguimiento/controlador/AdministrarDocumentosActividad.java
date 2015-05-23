/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import edu.unl.sigett.proyecto.managed.session.SessionProyecto;
import edu.unl.sigett.seguimiento.session.SessionActividad;
import edu.unl.sigett.seguimiento.session.SessionDocumentoActividad;
import edu.unl.sigett.seguimiento.session.SessionProyectosAutor;
import edu.unl.sigett.seguimiento.session.SessionProyectosDirector;
import edu.unl.sigett.seguridad.managed.session.SessionDocenteUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionEstudianteUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.DocumentoActividad;
import edu.unl.sigett.entity.Proyecto;
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
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.DocumentoActividadFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarDocumentosActividad implements Serializable {

    @Inject
    private SessionDocumentoActividad sessionDocumentoActividad;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionEstudianteUsuario sessionEstudianteUsuario;
    @Inject
    private SessionActividad sessionActividad;
    @Inject
    private SessionDocenteUsuario sessionDocenteUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionProyectosAutor sessionProyectosAutor;

    @EJB
    private DocumentoActividadFacadeLocal documentoActividadFacadeLocal;
    @EJB
    ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;

    private List<DocumentoActividad> documentosActividad;

    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedNoEditar;
    private boolean renderedSeleccionar;
    private boolean renderedEliminar;
    private boolean renderedBuscar;

    public AdministrarDocumentosActividad() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear(Usuario usuario, Actividad actividad, Proyecto proyecto) {
        String navegacion = "";
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_documento_actividad");
//                if (tienePermiso == 1) {
//                    sessionDocumentoActividad.setDocumentoActividad(new DocumentoActividad());
//                    sessionDocumentoActividad.getDocumentoActividad().setActividadId(actividad);
//                    RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoActividad').show()");
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void viewDocumento(Actividad actividad) {
        try {
            for (DocumentoActividad doc : documentoActividadFacadeLocal.buscarPorActividad(actividad.getId())) {
                if (doc.getEsActual()) {
                    sessionDocumentoActividad.setDocumentoActividad(doc);
                    sessionDocumentoActividad.getDocumentoActividad().setActividadId(actividad);
                    break;
                }
            }
            if (sessionDocumentoActividad.getDocumentoActividad() != null) {
                RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoActividad').show()");
            }
        } catch (Exception e) {
        }
    }

    public DocumentoActividad getDocumento(Actividad actividad) {
        DocumentoActividad documentoActividad = null;
        try {
            for (DocumentoActividad doc : documentoActividadFacadeLocal.buscarPorActividad(actividad.getId())) {
                if (doc.getEsActual()) {
                    documentoActividad = doc;
                    break;
                }
            }
        } catch (Exception e) {

        }
        return documentoActividad;
    }

    public String obtenerSizeKB(DocumentoActividad documentoActividad) {
        double valorKB = documentoActividad.getTamanio() / 1024;
        valorKB = Math.round(valorKB * 100);
        valorKB = valorKB / 100;
        return valorKB + " kB";
    }

    public void buscar(Actividad actividad, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_documento_actividad");
            if (tienePermiso == 1) {
                documentosActividad = documentoActividadFacadeLocal.buscarPorActividad(actividad.getId());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public String editar(DocumentoActividad documentoActividad, Usuario usuario, Actividad actividad, Proyecto proyecto) {
        String navegacion = "";
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_documento_actividad");
//                if (tienePermiso == 1) {
//                    sessionDocumentoActividad.setDocumentoActividad(documentoActividad);
//                    sessionDocumentoActividad.getDocumentoActividad().setActividadId(actividad);
//                    RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoActividad').show()");
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            String tipoArchivo = event.getFile().getContentType();
            String param = (String) event.getComponent().getAttributes().get("1");
            Usuario usuario = new Usuario();
            Proyecto p = null;
            if (param.equalsIgnoreCase("usuario")) {
                usuario = sessionUsuario.getUsuario();
                p = sessionProyecto.getProyecto();
            } else {
                if (param.equalsIgnoreCase("autor")) {
                    usuario = sessionEstudianteUsuario.getUsuario();
                    p = sessionProyectosAutor.getAutorProyecto().getProyectoId();
                } else {
                    if (param.equalsIgnoreCase("director")) {
                        p = sessionActividad.getActividad().getCronogramaId().getProyecto();
                        usuario = sessionDocenteUsuario.getUsuario();
                    } else {
                        if (param.equalsIgnoreCase("autor1")) {
                            usuario = sessionEstudianteUsuario.getUsuario();
                            p = sessionActividad.getActividad().getCronogramaId().getProyecto();
                        }
                    }
                }
            }
            this.sessionDocumentoActividad.getDocumentoActividad().setTamanio(event.getFile().getSize());
            sessionDocumentoActividad.getDocumentoActividad().setTipoArchivo(tipoArchivo);
            sessionDocumentoActividad.getDocumentoActividad().setDocumento(event.getFile().getContents());
            sessionDocumentoActividad.getDocumentoActividad().setFecha(fechaActual.getTime());
            grabar(sessionDocumentoActividad.getDocumentoActividad(), usuario, p);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void actualizaEstadoDocumentos(Actividad actividad, DocumentoActividad documentoActividad) {
        for (DocumentoActividad doc : actividad.getDocumentoActividadList()) {
            if (doc != documentoActividad) {
                doc.setEsActual(false);
                documentoActividadFacadeLocal.edit(doc);
            }
        }
    }

    public void remover(DocumentoActividad documentoActividad, Usuario usuario, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_documento_actividad");
//                if (tienePermiso == 1) {
//                    if (documentoActividad.getId() != null) {
//                        logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoActividad", documentoActividad.getId() + "", "ELIMINAR", "|Tipo Archivo= " + documentoActividad.getTipoArchivo() + "|Tamanio= " + documentoActividad.getTamanio() + "|Actividad= " + documentoActividad.getActividadId(), usuario));
//                        documentoActividadFacadeLocal.remove(documentoActividad);
//                        buscar(documentoActividad.getActividadId(), usuario);
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.documento") + " " + bundle.getString("lbl.msm_eliminar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.documento") + " " + bundle.getString("lbl.msm_eliminar"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public void grabar(DocumentoActividad documentoActividad, Usuario usuario, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (documentoActividad.getId() == null) {
//                if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_documento_actividad");
//                    if (tienePermiso == 1) {
//                        documentoActividadFacadeLocal.create(documentoActividad);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoActividad", documentoActividad.getId() + "", "CREAR", "|Tipo Archivo= " + documentoActividad.getTipoArchivo() + "|Tamanio= " + documentoActividad.getTamanio() + "|Actividad= " + documentoActividad.getActividadId(), usuario));
//                        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoActividad').hide()");
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.documento") + " " + bundle.getString("lbl.msm_grabar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        buscar(documentoActividad.getActividadId(), usuario);
//                        sessionDocumentoActividad.setDocumentoActividad(new DocumentoActividad());
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_documento_actividad");
//                    if (tienePermiso == 1) {
//                        documentoActividadFacadeLocal.edit(documentoActividad);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoActividad", documentoActividad.getId() + "", "EDITAR", "|Tipo Archivo= " + documentoActividad.getTipoArchivo() + "|Tamanio= " + documentoActividad.getTamanio() + "|Actividad= " + documentoActividad.getActividadId(), usuario));
//                        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoActividad').hide()");
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.documento") + " " + bundle.getString("lbl.msm_editar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        buscar(documentoActividad.getActividadId(), usuario);
//                        sessionDocumentoActividad.setDocumentoActividad(new DocumentoActividad());
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void seleccionarDocumento(DocumentoActividad documentoActividad, Usuario usuario, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "select_documento_actividad");
//                if (tienePermiso == 1) {
//                    actualizaEstadoDocumentos(sessionActividad.getActividad(), documentoActividad);
//                    documentoActividad.setEsActual(true);
//                    documentoActividadFacadeLocal.edit(documentoActividad);
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.documento") + " " + bundle.getString("lbl.msm_editar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_select"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_select"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedBuscar(Usuario usuario, Actividad actividad) {
        try {
            if (actividad.getId() != null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_documento_actividad");
                if (tienePermiso == 1) {
                    renderedBuscar = true;
                } else {
                    renderedBuscar = false;
                }
            } else {
                renderedBuscar = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedCrear(Usuario usuario, Proyecto proyecto) {
//        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_documento_actividad");
//            if (tienePermiso == 1) {
//                renderedCrear = true;
//            } else {
//                renderedCrear = false;
//            }
//        } else {
//            renderedCrear = false;
//        }
    }

    public void renderedEditar(Usuario usuario, Proyecto proyecto) {
//        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_documento_actividad");
//            if (tienePermiso == 1) {
//                renderedEditar = true;
//                renderedNoEditar = false;
//            } else {
//                renderedEditar = false;
//                renderedNoEditar = true;
//            }
//        } else {
//            renderedEditar = false;
//            renderedNoEditar = true;
//        }
    }

    public void renderedEliminar(Usuario usuario, Proyecto proyecto) {
//        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_documento_actividad");
//            if (tienePermiso == 1) {
//                renderedEliminar = true;
//            } else {
//                renderedEliminar = false;
//            }
//        } else {
//            renderedEliminar = false;
//        }
    }

    public void renderedSeleccionar(Usuario usuario, Proyecto proyecto) {
//        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "select_documento_actividad");
//            if (tienePermiso == 1) {
//                renderedSeleccionar = true;
//            } else {
//                renderedSeleccionar = false;
//            }
//        } else {
//            renderedSeleccionar = false;
//        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public SessionDocumentoActividad getSessionDocumentoActividad() {
        return sessionDocumentoActividad;
    }

    public void setSessionDocumentoActividad(SessionDocumentoActividad sessionDocumentoActividad) {
        this.sessionDocumentoActividad = sessionDocumentoActividad;
    }

    public SessionActividad getSessionActividad() {
        return sessionActividad;
    }

    public void setSessionActividad(SessionActividad sessionActividad) {
        this.sessionActividad = sessionActividad;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionEstudianteUsuario getSessionEstudianteUsuario() {
        return sessionEstudianteUsuario;
    }

    public void setSessionEstudianteUsuario(SessionEstudianteUsuario sessionEstudianteUsuario) {
        this.sessionEstudianteUsuario = sessionEstudianteUsuario;
    }

    public SessionDocenteUsuario getSessionDocenteUsuario() {
        return sessionDocenteUsuario;
    }

    public void setSessionDocenteUsuario(SessionDocenteUsuario sessionDocenteUsuario) {
        this.sessionDocenteUsuario = sessionDocenteUsuario;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedSeleccionar() {
        return renderedSeleccionar;
    }

    public void setRenderedSeleccionar(boolean renderedSeleccionar) {
        this.renderedSeleccionar = renderedSeleccionar;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public List<DocumentoActividad> getDocumentosActividad() {
        return documentosActividad;
    }

    public void setDocumentosActividad(List<DocumentoActividad> documentosActividad) {
        this.documentosActividad = documentosActividad;
    }
//</editor-fold>

}
