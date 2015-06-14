/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import edu.unl.sigett.seguimiento.session.SessionProyectosDirector;
import edu.unl.sigett.seguimiento.session.SessionRevision;
import edu.unl.sigett.entity.Actividad;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.Revision;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
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
import edu.unl.sigett.dao.ActividadDao;
import org.jlmallas.seguridad.dao.LogDao;
import edu.unl.sigett.dao.RevisionFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarRevisiones implements Serializable {

    @Inject
    private SessionProyectosDirector sessionProyectosDirector;
    @Inject
    private SessionRevision sessionRevision;
    @EJB
    private RevisionFacadeLocal revisionFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private ActividadDao actividadFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;

    private boolean renderedCrear;
    private boolean renderedEditar;
    private boolean renderedNoEditar;
    private boolean renderedEliminar;
    private boolean renderedDlgEditar;

    private List<Revision> revisionesPorAutor;
    private List<Revision> revisions;

    private String criterio;

    public AdministrarRevisiones() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_revision");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_revision");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
            renderedEditar = false;
        }
    }

    public boolean renderedSubirDocActividad(Actividad actividad) {
        boolean var = false;
        if (!actividad.getDocumentoActividadList().isEmpty()) {
            var = true;
        }
        return var;
    }

    public void renderedEliminar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_revision");
        if (tienePermiso == 1) {
            renderedEliminar = true;
        } else {
            renderedEliminar = false;
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String crear(Actividad actividad, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_revision");
            if (tienePermiso == 1) {
//                sessionActividad.setActividad(actividad);
                sessionRevision.setRevision(new Revision());
                Calendar fechaActual = Calendar.getInstance();
                sessionRevision.getRevision().setFecha(fechaActual.getTime());
                sessionRevision.getRevision().setHoraInicio(fechaActual.getTime());
                sessionRevision.getRevision().setActividadId(actividad);
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "editarRevision?faces-redirect=true";
                } else {
                    if (param.equalsIgnoreCase("crear-dlg")) {
                        renderedDlgEditar = true;
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarRevision').show()");
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

    public String editar(Revision revision, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_revision");
            if (tienePermiso == 1) {
                sessionRevision.setRevision(revision);
//                sessionActividad.setActividad(revision.getActividadId());
                Calendar fechaActual = Calendar.getInstance();
                sessionRevision.getRevision().setFecha(fechaActual.getTime());
                sessionRevision.getRevision().setHoraInicio(fechaActual.getTime());
                if (param.equalsIgnoreCase("editar")) {
                    navegacion = "editarRevision?faces-redirect=true";
                } else {
                    if (param.equalsIgnoreCase("editar-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarRevision').show()");
                    }
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

    public void buscarPorAutor(String criterio, Actividad actividad, Usuario usuario) {
        this.revisionesPorAutor = new ArrayList<>();
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_revision");
            if (tienePermiso == 1) {
                for (Revision revision : revisionFacadeLocal.buscarPorActividad(actividad.getId())) {
                    if (revision.getObservacion().toLowerCase().contains(criterio.toLowerCase()) || revision.getSugerencia().toLowerCase().contains(criterio.toLowerCase())) {
                        revisionesPorAutor.add(revision);
                    }
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para buscar Revisión. Consulte con el Administrador del Sistema.", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public void buscar(Usuario usuario, Actividad actividad) {
        this.revisions = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_revision");
            if (tienePermiso == 1) {
                for (Revision revision : revisionFacadeLocal.buscarPorActividad(actividad.getId())) {
                    revisions.add(revision);
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public void eliminar(Revision revision, Actividad actividad, Proyecto proyecto, Usuario usuario) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            EstadoActividad estadoActividad = estadoActividadFacadeLocal.find(2);
//            if (estadoActividad != null) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_revision");
//                if (tienePermiso == 1) {
//                    logFacadeLocal.create(logFacadeLocal.crearLog("Revision", revision.getId() + "", "ELIMINAR", "|Observación=" + revision.getObservacion() + "|Sugerencia= " + revision.getSugerencia() + "|Actividad= " + revision.getActividadId().getId(), usuario));
//                    actividad.setEstadoActividadId(estadoActividad);
//                    actividadFacadeLocal.edit(actividad);
//                    logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "EDITAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|Estado" + actividad.getEstadoActividadId().getId() + "|ActividadId= " + actividad.getActividadId(), usuario));
//                    revisionFacadeLocal.remove(revision);
//                    administrarActividades.calculosActividadObjetivo(actividad);
//                    administrarActividades.actualizarPorcentajesDuracionActividades(actividad, actividadFacadeLocal.buscarPorProyecto(proyecto.getCronograma().getId()));
////                    administrarCronograma.calculaAvanceFaltanteCronograma(sessionProyectosDirector.getDirectorProyecto().getProyectoId().getCronograma(), actividadFacadeLocal.buscarPorProyecto(sessionProyectosDirector.getDirectorProyecto().getProyectoId().getCronograma().getId()), usuario);
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public String grabar(Revision revision, Actividad actividad, Proyecto proyecto, Docente docente, Usuario usuario) {
        String navegacion = "";
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//            Calendar fechaActual = Calendar.getInstance();
//            EstadoActividad estadoActividad = estadoActividadFacadeLocal.find(2);
//            if (estadoActividad != null) {
//                if (revision.getId() == null) {
//                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_revision");
//                    if (tienePermiso == 1) {
//                        revision.setHoraFin(fechaActual.getTime());
//                        revisionFacadeLocal.create(revision);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("Revision", revision.getId() + "", "CREAR", "|Observación=" + revision.getObservacion() + "|Sugerencia= " + revision.getSugerencia() + "|Actividad= " + revision.getActividadId().getId(), usuario));
//                        actividad.setEstadoActividadId(estadoActividad);
//                        actividad.setAvance(100.00);
//                        actividad.setFaltante(0.0);
//                        actividadFacadeLocal.edit(actividad);
//                        administrarActividades.notificacionActividadesPorRevisar(docente);
//                        administrarActividades.calculosActividadObjetivo(actividad);
//                        administrarActividades.actualizarPorcentajesDuracionActividades(actividad, actividadFacadeLocal.buscarPorProyecto(proyecto.getCronograma().getId()));
////                        administrarCronograma.calculaAvanceFaltanteCronograma(proyecto.getCronograma(), actividadFacadeLocal.buscarPorProyecto(proyecto.getCronograma().getId()), usuario);
////                        administrarNotificaciones.notificarRevisionActividad(proyecto.getCronograma());
//                        logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "EDITAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|Estado" + actividad.getEstadoActividadId().getId() + "|ActividadId= " + actividad.getActividadId(), usuario));
//                        if (param.equalsIgnoreCase("grabar-dlg")) {
//                            sessionRevision.setRevision(new Revision());
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarRevision').hide()");
//                        } else {
//                            if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        }
//                    } else {
//                        if (tienePermiso == 2) {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    }
//                } else {
//                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_revision");
//                    if (tienePermiso == 1) {
//                        revision.setHoraFin(fechaActual.getTime());
//                        revisionFacadeLocal.edit(revision);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("Revision", revision.getId() + "", "EDITAR", "|Observación=" + revision.getObservacion() + "|Sugerencia= " + revision.getSugerencia() + "|Actividad= " + revision.getActividadId().getId(), usuario));
//                        actividad.setEstadoActividadId(estadoActividad);
//                        actividad.setAvance(100.00);
//                        actividad.setFaltante(0.0);
//                        actividadFacadeLocal.edit(actividad);
//                        administrarActividades.notificacionActividadesPorRevisar(docente);
//                        administrarActividades.calculosActividadObjetivo(actividad);
//                        administrarActividades.actualizarPorcentajesDuracionActividades(actividad, actividadFacadeLocal.buscarPorProyecto(proyecto.getCronograma().getId()));
////                        administrarCronograma.calculaAvanceFaltanteCronograma(proyecto.getCronograma(), actividadFacadeLocal.buscarPorProyecto(proyecto.getCronograma().getId()), usuario);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "EDITAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|Estado" + actividad.getEstadoActividadId().getId() + "|ActividadId= " + actividad.getActividadId(), usuario));
////                        administrarNotificaciones.notificarRevisionActividad(proyecto.getCronograma());
//                        if (param.equalsIgnoreCase("grabar-dlg")) {
//                            sessionRevision.setRevision(new Revision());
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarRevision').hide()");
//                        } else {
//                            if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        }
//                    } else {
//                        if (tienePermiso == 2) {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
        }

        return navegacion;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public List<Revision> getRevisions() {
        return revisions;
    }

    public void setRevisions(List<Revision> revisions) {
        this.revisions = revisions;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }

    public void setSessionRevision(SessionRevision sessionRevision) {
        this.sessionRevision = sessionRevision;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

  

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

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

  
    public SessionProyectosDirector getSessionProyectosDirector() {
        return sessionProyectosDirector;
    }

    public void setSessionProyectosDirector(SessionProyectosDirector sessionProyectosDirector) {
        this.sessionProyectosDirector = sessionProyectosDirector;
    }

    public List<Revision> getRevisionesPorAutor() {
        return revisionesPorAutor;
    }

    public void setRevisionesPorAutor(List<Revision> revisionesPorAutor) {
        this.revisionesPorAutor = revisionesPorAutor;
    }
//</editor-fold>
}
