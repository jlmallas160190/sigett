/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import org.jlmallas.api.date.DateResource;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.postulacion.controlador.AdministrarCronograma;
import edu.unl.sigett.postulacion.managed.session.SessionProyecto;
import edu.unl.sigett.seguimiento.session.SessionActividad;
import edu.unl.sigett.seguimiento.session.SessionProyectosAutor;
import edu.unl.sigett.seguimiento.session.SessionProyectosDirector;
import edu.unl.sigett.seguridad.managed.session.SessionDocenteUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionEstudianteUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.CatalogoEvento;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.Cronograma;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.EstadoActividad;
import edu.unl.sigett.entity.EstadoProyecto;
import edu.jlmallas.academico.entity.Estudiante;
import edu.unl.sigett.entity.Evento;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.TipoActividad;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;
import edu.unl.sigett.dao.ActividadFacadeLocal;
import edu.unl.sigett.dao.CatalogoEventoFacadeLocal;
import edu.unl.sigett.dao.EstadoActividadFacadeLocal;
import edu.unl.sigett.dao.EventoFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import edu.unl.sigett.dao.ProyectoFacadeLocal;
import edu.unl.sigett.dao.TipoActividadFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarActividades implements Serializable {

    @Inject
    private SessionActividad sessionActividad;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private AdministrarDocumentosActividad administrarDocumentosActividad;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionDocenteUsuario sessionDocenteUsuario;
    @Inject
    private SessionEstudianteUsuario sessionEstudianteUsuario;
    @Inject
    private SessionProyectosAutor sessionProyectosAutor;
    @Inject
    private SessionProyectosDirector sessionProyectosDirector;
    @Inject
    private AdministrarCronograma administrarCronograma;
    @Inject
    private AdministrarRevisiones administrarRevisiones;

    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private ActividadFacadeLocal actividadFacadeLocal;
    @EJB
    private TipoActividadFacadeLocal tipoActividadFacadeLocal;
    @EJB
    private EstadoActividadFacadeLocal estadoActividadFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ProyectoFacadeLocal proyectoFacadeLocal;
    @EJB
    private EventoFacadeLocal eventoFacadeLocal;
    @EJB
    private CatalogoEventoFacadeLocal catalogoEventoFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;

    private List<Actividad> actividades;
    private List<Actividad> actividadesPorDirectorProyecto;
    private List<Actividad> actividadesPorAutor;
    private List<Actividad> actividadesConsulta;

    private boolean noEditado;
    private boolean estadoEsSeleccionado = false;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedNoEditar;
    private boolean renderedEditarPorAutorProyecto;
    private boolean renderedNoEditarPorAutorProyecto;
    private boolean renderedCrearPorAutorProyecto;
    private boolean renderedEliminarPorAutorProyecto;
    private boolean renderedBuscar;
    private boolean renderedDlgEditar;
    private boolean renderedDlgRevisar;

    private String criterio;
    private String criterioConsulta;
    private String tipoActividad;
    private TreeNode root;
    private String actividadPadre;
    private String estadoActividad;
    private Date fechaInicioActividadPadre;
    private Date fechaFinActividadPadre;
    private String criterioPorDirectorProyecto;
    private String criterioPorAutorProyecto;
    private TreeNode rootPorAutorProyecto;
    private TreeNode rootPorDirectorProyecto;
    private Date fechaInicioLimit;
    private Date fechaFinLimit;
    private ScheduleModel eventModelDirector;
    private ScheduleModel eventModelAutorProyecto;

    public AdministrarActividades() {
        renderedEditar = false;
        eventModelDirector = new DefaultScheduleModel();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void onTabChange(TabChangeEvent event) {
        switch (event.getTab().getId()) {
            case "tabDocumentos-usuario":
                administrarDocumentosActividad.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarDocumentosActividad.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarDocumentosActividad.renderedSeleccionar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarDocumentosActividad.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarDocumentosActividad.buscar(sessionActividad.getActividad(), sessionUsuario.getUsuario());
                break;
            case "tabDocumentos-autor":
                administrarDocumentosActividad.renderedCrear(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarDocumentosActividad.renderedEditar(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarDocumentosActividad.renderedSeleccionar(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarDocumentosActividad.renderedEliminar(sessionEstudianteUsuario.getUsuario(), sessionProyectosAutor.getAutorProyecto().getProyectoId());
                administrarDocumentosActividad.buscar(sessionActividad.getActividad(), sessionEstudianteUsuario.getUsuario());
                break;
            case "tabDocumentos-director":
                administrarDocumentosActividad.renderedCrear(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
                administrarDocumentosActividad.renderedEditar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
                administrarDocumentosActividad.renderedSeleccionar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
                administrarDocumentosActividad.renderedEliminar(sessionDocenteUsuario.getUsuario(), sessionProyectosDirector.getDirectorProyecto().getProyectoId());
                administrarDocumentosActividad.buscar(sessionActividad.getActividad(), sessionDocenteUsuario.getUsuario());
                break;
            case "tabRevisiones-autor":
                administrarRevisiones.buscar(sessionEstudianteUsuario.getUsuario(), sessionActividad.getActividad());
                break;
            case "tabRevisiones-director":
                administrarRevisiones.buscar(sessionDocenteUsuario.getUsuario(), sessionActividad.getActividad());
                break;
            case "tabDocumentos-autor1":
                administrarDocumentosActividad.renderedCrear(sessionEstudianteUsuario.getUsuario(), sessionActividad.getActividad().getCronogramaId().getProyecto());
                administrarDocumentosActividad.renderedEditar(sessionEstudianteUsuario.getUsuario(), sessionActividad.getActividad().getCronogramaId().getProyecto());
                administrarDocumentosActividad.renderedSeleccionar(sessionEstudianteUsuario.getUsuario(), sessionActividad.getActividad().getCronogramaId().getProyecto());
                administrarDocumentosActividad.renderedEliminar(sessionEstudianteUsuario.getUsuario(), sessionActividad.getActividad().getCronogramaId().getProyecto());
                administrarDocumentosActividad.buscar(sessionActividad.getActividad(), sessionEstudianteUsuario.getUsuario());

                break;

        }

    }

    public void download(String tipo, Actividad actividad) {
        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            byte[] documento = administrarDocumentosActividad.getDocumento(actividad).getDocumento();
            if (tipo.equalsIgnoreCase("pdf")) {
                response.addHeader("Content-disposition", "attachment; filename=" + actividad.getId() + ".pdf");
                response.setContentType("application/pdf");
                response.setContentLength(documento.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(documento, 0, documento.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    ByteArrayInputStream bis = new ByteArrayInputStream(documento);
                    response.addHeader("Content-disposition", "attachment; filename=" + actividad.getId() + ".docx");
                    ServletOutputStream outStream = response.getOutputStream();
                    while ((read = bis.read(bytes)) != -1) {
                        outStream.write(bytes, 0, read);
                    }
                    outStream.flush();
                    outStream.close();
                }
            }
        } catch (Exception e) {
        }
    }

    public void notificacionActividadesPorRevisar(Docente docente) {
        try {
            Persona persona = personaFacadeLocal.find(docente.getId());
            eventModelDirector = new DefaultScheduleModel();
            for (Actividad actividad : actividadFacadeLocal.buscarPorRevisarDirectorProyecto(persona.getNumeroIdentificacion())) {
                if (actividad.getTipoActividadId().getId() == 2) {
                    eventModelDirector.addEvent(new DefaultScheduleEvent(actividad.getNombre(), actividad.getFechaInicioRevision(), actividad.getFechaFinRevision(), actividad));
                }
            }
        } catch (Exception e) {
        }
    }

    public void notificacionActividadesAutorProyecto(Estudiante estudiante) {
        try {
            eventModelAutorProyecto = new DefaultScheduleModel();
            Persona persona = personaFacadeLocal.find(estudiante.getId());
            for (Actividad actividad : actividadFacadeLocal.buscarPorAutorProyecto(persona.getNumeroIdentificacion())) {
                if (actividad.getTipoActividadId().getId() == 2) {
                    DefaultScheduleEvent evento = new DefaultScheduleEvent();
                    evento.setTitle(actividad.getNombre());
                    evento.setStartDate(actividad.getFechaInicioRevision());
                    evento.setEndDate(actividad.getFechaFinRevision());
                    evento.setData(actividad);
                    if (actividad.getEstadoActividadId().getId() == 1) {
                        evento.setStyleClass("porRevisar");
                    } else {
                        evento.setStyleClass("revisado");
                    }
                    eventModelAutorProyecto.addEvent(evento);
                }
            }
        } catch (Exception e) {
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        try {
            ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
            sessionActividad.setActividad((Actividad) event.getData());
            renderedDlgRevisar = true;
            administrarRevisiones.renderedCrear(sessionDocenteUsuario.getUsuario());
            RequestContext.getCurrentInstance().execute("PF('dlgRevisarActividad').show()");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onEventSelectAutorProyecto(SelectEvent selectEvent) {
        try {
            ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
            sessionActividad.setActividad((Actividad) event.getData());
            renderedDlgEditar = true;
            administrarDocumentosActividad.buscar(sessionActividad.getActividad(), sessionEstudianteUsuario.getUsuario());
            administrarDocumentosActividad.renderedCrear(sessionEstudianteUsuario.getUsuario(), sessionActividad.getActividad().getCronogramaId().getProyecto());
            administrarDocumentosActividad.renderedEditar(sessionEstudianteUsuario.getUsuario(), sessionActividad.getActividad().getCronogramaId().getProyecto());
            administrarDocumentosActividad.renderedEliminar(sessionEstudianteUsuario.getUsuario(), sessionActividad.getActividad().getCronogramaId().getProyecto());
            RequestContext.getCurrentInstance().execute("PF(dlgEditarActividad').show()");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onEventMove(ScheduleEntryMoveEvent moveEvent) {
        try {
            ScheduleEvent event = (ScheduleEvent) moveEvent.getScheduleEvent();
            Actividad a = ((Actividad) event.getData());
            actividadFacadeLocal.edit(a);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onEventResize(ScheduleEntryResizeEvent resizeEvent) {
        ScheduleEvent event = (ScheduleEvent) resizeEvent.getScheduleEvent();
        Actividad a = ((Actividad) event.getData());
        actividadFacadeLocal.edit(a);
    }

    public void seleccionarEstadoActividad(Actividad actividad) {
        int posTipo = estadoActividad.indexOf(":");
        EstadoActividad ea = estadoActividadFacadeLocal.find(Integer.parseInt(estadoActividad.substring(0, posTipo)));
        if (ea != null) {
            actividad.setEstadoActividadId(ea);
            estadoEsSeleccionado = true;
        }
    }

    public String styleEstadoActividad(Actividad actividad) {
        String style = "";
        try {
            if (actividad.getEstadoActividadId().getId() == 1) {
                style = "actividadDesarrollo";
            } else {
                if (actividad.getEstadoActividadId().getId() == 2) {
                    style = "actividadRevisada";
                }
            }
        } catch (Exception e) {
        }
        return style;
    }

    public String crearSubActividad(Actividad actividad, Usuario usuario, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())
                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_actividad");
                if (tienePermiso == 1) {
                    sessionActividad.setActividad(new Actividad());
                    sessionActividad.getActividad().setLugarRevision("S/N");
                    sessionActividad.getActividad().setActividadId(actividad.getId());
                    sessionActividad.getActividad().setTienePadre(true);
                    fechaFinActividadPadre = actividad.getFechaCulminacion();
                    fechaInicioActividadPadre = actividad.getFechaInicio();
                    fechaInicioLimit = actividad.getFechaInicio();
                    fechaFinLimit = actividad.getFechaCulminacion();
                    TipoActividad tipoActividad = tipoActividadFacadeLocal.find(2);
                    if (tipoActividad != null) {
                        sessionActividad.getActividad().setTipoActividadId(tipoActividad);
                    }
                    administrarDocumentosActividad.renderedCrear(usuario, proyecto);
                    administrarDocumentosActividad.renderedEditar(usuario, proyecto);
                    administrarDocumentosActividad.renderedSeleccionar(usuario, proyecto);
                    administrarDocumentosActividad.renderedEliminar(usuario, proyecto);
                    administrarDocumentosActividad.buscar(sessionActividad.getActividad(), usuario);
                    administrarDocumentosActividad.renderedBuscar(usuario, sessionActividad.getActividad());
                    administrarRevisiones.buscarPorAutor("", sessionActividad.getActividad(), usuario);

                    if (param.equalsIgnoreCase("crear")) {
                        navegacion = "editarActividad?faces-redirect=true";
                    } else {
                        if (param.equalsIgnoreCase("crear-dlg")) {
                            renderedDlgEditar = true;
                            RequestContext.getCurrentInstance().execute("PF('dlgEditarActividad').show()");
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
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String crear(Usuario usuario, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            proyecto = proyectoFacadeLocal.find(proyecto.getId());
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())
                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_actividad");
                if (tienePermiso == 1) {
                    sessionActividad.setActividad(new Actividad());
                    sessionActividad.getActividad().setLugarRevision("S/N");
                    sessionActividad.getActividad().setCronogramaId(proyecto.getCronograma());
                    sessionActividad.getActividad().setFaltante(100);
                    sessionActividad.getActividad().setAvance(0.0);
                    estadoEsSeleccionado = false;
                    fechaFinActividadPadre = null;
                    fechaInicioActividadPadre = null;
                    fechaInicioLimit = proyecto.getCronograma().getFechaInicio();
                    fechaFinLimit = proyecto.getCronograma().getFechaFin();
                    TipoActividad tipoActividad = tipoActividadFacadeLocal.find(1);
                    if (tipoActividad != null) {
                        sessionActividad.getActividad().setTipoActividadId(tipoActividad);
                    }
                    administrarDocumentosActividad.renderedCrear(usuario, proyecto);
                    administrarDocumentosActividad.renderedEditar(usuario, proyecto);
                    administrarDocumentosActividad.renderedSeleccionar(usuario, proyecto);
                    administrarDocumentosActividad.renderedEliminar(usuario, proyecto);
                    administrarDocumentosActividad.buscar(sessionActividad.getActividad(), usuario);
                    administrarDocumentosActividad.renderedBuscar(usuario, sessionActividad.getActividad());

                    administrarRevisiones.buscarPorAutor("", sessionActividad.getActividad(), usuario);

                    if (param.equalsIgnoreCase("crear")) {
                        navegacion = "pretty:editarActividad";
                    } else {
                        if (param.equalsIgnoreCase("crear-dlg")) {
                            renderedDlgEditar = true;
                            RequestContext.getCurrentInstance().execute("PF('dlgEditarActividad').show()");
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        if (param.equalsIgnoreCase("crear")) {
                            navegacion = "pretty:login";
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(Actividad actividad, Usuario usuario, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            proyecto = proyectoFacadeLocal.find(proyecto.getId());
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())
                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_actividad");
                if (tienePermiso == 1) {
                    sessionActividad.setActividad(actividad);
                    tipoActividad = actividad.getTipoActividadId().toString();
                    estadoActividad = actividad.getEstadoActividadId().toString();
                    estadoEsSeleccionado = false;
                    if (actividad.getId() != actividad.getActividadId()) {
                        actividad.setTienePadre(true);
                        Actividad actividadPadre = actividadFacadeLocal.find((actividad.getActividadId()));
                        fechaFinActividadPadre = actividadPadre.getFechaCulminacion();
                        fechaInicioActividadPadre = actividadPadre.getFechaInicio();
                        fechaInicioLimit = actividadPadre.getFechaInicio();
                        fechaFinLimit = actividadPadre.getFechaCulminacion();
                    } else {
                        fechaInicioLimit = proyecto.getCronograma().getFechaInicio();
                        fechaFinLimit = proyecto.getCronograma().getFechaFin();
                        fechaFinActividadPadre = null;
                        fechaInicioActividadPadre = null;
                    }
                    administrarDocumentosActividad.renderedCrear(usuario, proyecto);
                    administrarDocumentosActividad.renderedBuscar(usuario, actividad);
                    administrarDocumentosActividad.renderedEditar(usuario, proyecto);
                    administrarDocumentosActividad.renderedSeleccionar(usuario, proyecto);
                    administrarDocumentosActividad.renderedEliminar(usuario, proyecto);
                    administrarDocumentosActividad.buscar(sessionActividad.getActividad(), usuario);

                    administrarRevisiones.buscarPorAutor("", sessionActividad.getActividad(), usuario);
                    if (param.equalsIgnoreCase("editar")) {
                        navegacion = "editarActividad?faces-redirect=true";
                    } else {
                        if (param.equalsIgnoreCase("editar-dlg")) {
                            renderedDlgEditar = true;
                            RequestContext.getCurrentInstance().execute("PF('dlgEditarActividad').show()");
                        }
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        if (param.equalsIgnoreCase("crear")) {
                            navegacion = "pretty:login";
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public int validaFechas(Actividad actividad, Cronograma cronograma) {
        int var = -1;
        try {
            if (actividad.getFechaInicio().before(actividad.getFechaCulminacion()) || actividad.getFechaInicio().equals(actividad.getFechaCulminacion())) {
                if (actividad.isTienePadre()) {
                    if ((actividad.getFechaInicio().equals(fechaInicioActividadPadre) || actividad.getFechaInicio().after(fechaInicioActividadPadre)) && (actividad.getFechaCulminacion().equals(fechaFinActividadPadre) || actividad.getFechaCulminacion().before(fechaFinActividadPadre))) {
                        var = 1;
                    } else {
                        var = 0;
                    }
                } else {
                    if ((actividad.getFechaInicio().equals(cronograma.getFechaInicio()) || actividad.getFechaInicio().after(cronograma.getFechaInicio())) && (actividad.getFechaCulminacion().equals(cronograma.getFechaProrroga()) || actividad.getFechaCulminacion().before(cronograma.getFechaProrroga()))) {
                        var = 1;
                    } else {
                        var = 2;
                    }
                }
            }
        } catch (Exception e) {
            var = -1;
        }

        return var;
    }

    public int calcularDiasSemanaTrabajo(Proyecto proyecto) {
        int dias = 0;
        for (ConfiguracionProyecto cf : proyecto.getConfiguracionProyectoList()) {
            if (cf.getCodigo().equalsIgnoreCase("DS")) {
                dias = Integer.parseInt(cf.getValor());
                break;
            }
        }
        return dias;
    }

    public void calculaDuracionActividad(Actividad actividad, Cronograma cronograma) {
        double duracionDias = 0;
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            DateResource calculo = new DateResource();
            if (actividad.getFechaCulminacion() != null && actividad.getFechaInicio() != null) {
                int var = validaFechas(actividad, cronograma);
                if (var == 1) {
                    actividad.setCronogramaId(cronograma);
                    duracionDias = calculo.calculaDuracionEnDias(actividad.getFechaInicio(), actividad.getFechaCulminacion(), 7 - calcularDiasSemanaTrabajo(cronograma.getProyecto()));
                } else {
                    actividad.setFechaCulminacion(null);
                    if (var == 0) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_invalidas"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        if (var == -1) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_invalidas"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            if (var == 2) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_invalidas"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    }
                }
            }
            actividad.setDuracion(duracionDias);
            calculaPorcentajeDuracion(actividad);

        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void obtenerEstadoActividad(Actividad actividad, String param) {
        if (estadoEsSeleccionado == false) {
            EstadoActividad estadoActividad = estadoActividadFacadeLocal.find((int) 1);
            if (estadoActividad != null) {
                actividad.setEstadoActividadId(estadoActividad);
            }
        } else {
            int pos = estadoActividad.indexOf(":");
            EstadoActividad ea = estadoActividadFacadeLocal.find(Integer.parseInt(estadoActividad.substring(0, pos)));
            if (ea != null) {
                actividad.setEstadoActividadId(ea);
            }
        }
    }

    public void calculosActividadObjetivo(Actividad actividad) {
        try {
            double avance = 0;
            EstadoActividad estadoActividad = null;
            if (actividad.getActividadId() != null) {
                Actividad actividadPadre = actividadFacadeLocal.find(actividad.getActividadId());
                if (actividadPadre != null) {
                    for (Actividad a : actividadFacadeLocal.buscarSubActividades(actividadPadre.getId())) {
                        if (a.getEsActivo()) {
                            if (a.getEstadoActividadId().getId() == 2 && a.getId() != a.getActividadId()) {
                                avance += a.getPorcentajeDuracion();
                            }
                        }
                    }
                    double valor = avance;
                    valor = Math.round(valor * 100);
                    valor = valor / 100;
                    actividadPadre.setAvance(valor);
                    actividadPadre.setFaltante(100 - valor);
                    if (actividadPadre.getAvance() == 100.00) {
                        estadoActividad = estadoActividadFacadeLocal.find(2);
                        if (estadoActividad != null) {
                            actividadPadre.setEstadoActividadId(estadoActividad);
                        }
                    } else {
                        estadoActividad = estadoActividadFacadeLocal.find(1);
                        if (estadoActividad != null) {
                            actividadPadre.setEstadoActividadId(estadoActividad);
                        }
                    }
                    actividadFacadeLocal.edit(actividadPadre);
                }
            }
        } catch (Exception e) {
        }
    }

    public String grabar(Actividad actividad, Cronograma cronograma, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        String param2 = (String) facesContext.getExternalContext().getRequestParameterMap().get("2");
        try {
            CatalogoEvento catalogoEvento = catalogoEventoFacadeLocal.find(1);
            Evento evento = null;
            actividad.setEsActivo(true);
            obtenerEstadoActividad(actividad, param);
            if (actividad.getDuracion() > 0) {
                if (actividad.getId() == null) {
                    if (cronograma.getProyecto().getEstadoProyectoId().getId() == 2 || cronograma.getProyecto().getEstadoProyectoId().getId() == 3) {
                        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_actividad");
                        if (tienePermiso == 1) {
                            actividad.setFechaInicioRevision(actividad.getFechaCulminacion());
                            Calendar ffr = Calendar.getInstance();
                            ffr.setTime(actividad.getFechaInicioRevision());
                            ffr.add(Calendar.HOUR_OF_DAY, 1);
                            actividad.setFechaFinRevision(ffr.getTime());
                            actividadFacadeLocal.create(actividad);
                            evento = new Evento(null, actividad.getNombre(), actividad.getFechaInicioRevision(), actividad.getFechaFinRevision(), actividad.getId() + "");
                            evento.setCatalogoEventoId(catalogoEvento);
                            eventoFacadeLocal.create(evento);
                            administrarDocumentosActividad.renderedBuscar(usuario, actividad);
                            if (actividad.getActividadId() == null) {
                                actividad.setActividadId(actividad.getId());
                                actividadFacadeLocal.edit(actividad);
                            }
                            actualizarPorcentajesDuracionActividades(actividad, actividadFacadeLocal.buscarPorProyecto(cronograma.getId()));
                            calculosActividadObjetivo(actividad);
                            administrarCronograma.calculaAvanceFaltanteCronograma(cronograma, actividadFacadeLocal.buscarPorProyecto(cronograma.getId()), usuario);
                            logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "CREAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|ActividadId= " + actividad.getActividadId() + "|Estado=" + actividad.getEstadoActividadId().getId(), usuario));
                            if (param2.equalsIgnoreCase("estudiante")) {
                                buscarPorAutorProyecto("", usuario, cronograma);
                            } else {
                                buscarPorCronograma("", cronograma, usuario);
                            }
                            if (param.equalsIgnoreCase("grabar")) {
                                sessionActividad.setActividad(new Actividad());
                            } else {
                                if (param.equalsIgnoreCase("grabar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    if (param.equalsIgnoreCase("grabar-editar-dlg")) {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                                        FacesContext.getCurrentInstance().addMessage(null, message);
                                    } else {
                                        if (param.equalsIgnoreCase("grabar-dlg")) {
                                            RequestContext.getCurrentInstance().execute("PF('dlgEditarActividad').hide()");
                                            sessionActividad.setActividad(new Actividad());
                                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                                            FacesContext.getCurrentInstance().addMessage(null, message);
                                        }
                                    }
                                }
                            }
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_actividad");
                    if (tienePermiso == 1) {
                        actividadFacadeLocal.edit(actividad);
                        evento = eventoFacadeLocal.buscarPorCategoriaEvento(catalogoEvento.getId(), actividad.getId() + "");
                        if (evento == null) {
                            evento = new Evento(null, actividad.getNombre(), actividad.getFechaInicioRevision(), actividad.getFechaFinRevision(), actividad.getId() + "");
                            evento.setCatalogoEventoId(catalogoEvento);
                            eventoFacadeLocal.create(evento);
                        } else {
                            evento.setTitulo(actividad.getNombre());
                            evento.setFechaInicio(actividad.getFechaInicioRevision());
                            evento.setFechaFin(actividad.getFechaFinRevision());
                            eventoFacadeLocal.edit(evento);
                        }
                        actualizarPorcentajesDuracionActividades(actividad, actividadFacadeLocal.buscarPorProyecto(cronograma.getId()));
                        calculosActividadObjetivo(actividad);
                        administrarCronograma.calculaAvanceFaltanteCronograma(cronograma, actividadFacadeLocal.buscarPorProyecto(cronograma.getId()), usuario);
                        administrarDocumentosActividad.renderedBuscar(usuario, actividad);
                        logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "EDITAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|ActividadId= " + actividad.getActividadId() + "|Estado=" + actividad.getEstadoActividadId().getId(), usuario));
                        if (param2.equalsIgnoreCase("estudiante")) {
                            buscarPorAutorProyecto(criterioPorAutorProyecto, usuario, cronograma);
                        } else {
                            buscarPorCronograma(criterio, cronograma, usuario);
                        }
                        if (param.equalsIgnoreCase("grabar")) {
                            sessionActividad.setActividad(new Actividad());
                        } else {
                            if (param.equalsIgnoreCase("grabar-editar")) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            } else {
                                if (param.equalsIgnoreCase("grabar-editar-dlg")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    if (param.equalsIgnoreCase("grabar-dlg")) {
                                        RequestContext.getCurrentInstance().execute("PF('dlgEditarActividad').hide()");
                                        sessionActividad.setActividad(new Actividad());
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                                        FacesContext.getCurrentInstance().addMessage(null, message);

                                    }
                                }
                            }
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public boolean disabledEditar(Actividad actividad) {
        boolean var = false;
        if (actividad.getEsActivo() == false) {
            var = true;
        }
        return var;
    }

    public void buscarPorAutorProyecto(String criterio, Usuario usuario, Cronograma cronograma) {
        actividadesPorAutor = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (cronograma.getProyecto().getEstadoProyectoId().getId() == 2 || cronograma.getProyecto().getEstadoProyectoId().getId() == 3) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_actividad");
                if (tienePermiso == 1) {
                    for (Actividad actividad : actividadFacadeLocal.buscarPorProyecto(cronograma.getId())) {
                        if (actividad.getNombre().toUpperCase().contains(criterio.toUpperCase())) {
                            actividadesPorAutor.add(actividad);
                        }
                    }
                    rootPorAutorProyecto = new DefaultTreeNode("Root", null);
                    for (Actividad actividad : actividadesPorAutor) {
                        TreeNode node = null;
                        if (actividad.getActividadId() == actividad.getId()) {
                            node = new DefaultTreeNode(actividad, rootPorAutorProyecto);
                        }
                    }
                    for (Actividad actividad : actividadesPorAutor) {
                        for (TreeNode nodoPadre : rootPorAutorProyecto.getChildren()) {
                            if (actividad.getActividadId() != actividad.getId()) {
                                Actividad actividadPadre = (Actividad) nodoPadre.getData();
                                if (actividadPadre.getId() == actividad.getActividadId()) {
                                    nodoPadre.getChildren().add(new DefaultTreeNode(actividad));
                                }
                            }
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void actualizarPorcentajesDuracionActividades(Actividad actividad, List<Actividad> actividades) {
        try {
            double sum = 0;
            double sumSubActividades = 0;
            sum = actividadFacadeLocal.sumatoriaActividades((long) 0, actividad.getCronogramaId().getId());
            for (Actividad a : actividades) {
                if (a.getId() == a.getActividadId()) {
                    if (a.getId() != null) {
                        double valor = ((a.getDuracion() / sum) * 100);
                        valor = Math.round(valor * 100);
                        valor = valor / 100;
                        a.setPorcentajeDuracion(valor);
                        actividadFacadeLocal.edit(a);
                    }
                } else {
                    if (a.getId() != null) {
                        sumSubActividades = actividadFacadeLocal.sumatoriaSubActividades((long) 0, a.getActividadId());
                        double valor = ((a.getDuracion() / sumSubActividades) * 100);
                        valor = Math.round(valor * 100);
                        valor = valor / 100;
                        a.setPorcentajeDuracion(valor);
                        actividadFacadeLocal.edit(a);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void calculaPorcentajeDuracion(Actividad actividad) {
        try {
            double sum = 0;
            if (actividad.getId() == actividad.getActividadId()) {
                if (actividad.getId() != null) {
                    sum = actividadFacadeLocal.sumatoriaActividades(actividad.getId(), actividad.getCronogramaId().getId());
                    sum += actividad.getDuracion();
                } else {
                    sum = actividadFacadeLocal.sumatoriaActividades((long) 0, actividad.getCronogramaId().getId());
                    sum += actividad.getDuracion();
                }
            } else {
                if (actividad.getId() != null) {
                    sum = actividadFacadeLocal.sumatoriaSubActividades(actividad.getId(), actividad.getActividadId());
                    sum += actividad.getDuracion();
                } else {
                    sum = actividadFacadeLocal.sumatoriaSubActividades((long) 0, actividad.getActividadId());
                    sum += actividad.getDuracion();
                }
            }
            double valor = ((actividad.getDuracion() / sum) * 100);
            valor = Math.round(valor * 100);
            valor = valor / 100;
            actividad.setPorcentajeDuracion(valor);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void desactivar(Actividad actividad, Cronograma cronograma, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_actividad");
            if (tienePermiso == 1) {
                if (actividad.getEsActivo()) {
                    actividad.setEsActivo(false);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    actividad.setEsActivo(true);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                actividadFacadeLocal.edit(actividad);
                logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "EDITAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|ActividadId= " + actividad.getActividadId(), usuario));
                actualizarPorcentajesDuracionActividades(actividad, actividadFacadeLocal.buscarPorProyecto(cronograma.getId()));
                calculosActividadObjetivo(actividad);
                administrarCronograma.calculaAvanceFaltanteCronograma(cronograma, actividadFacadeLocal.buscarPorProyecto(cronograma.getId()), usuario);
                if (param.equalsIgnoreCase("estudiante")) {
                    buscarPorAutorProyecto(criterioPorAutorProyecto, usuario, cronograma);
                } else {
                    buscarPorCronograma(criterio, cronograma, usuario);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void subirNivel(Actividad actividad, Usuario usuario, Cronograma cronograma) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        if (cronograma.getProyecto().getEstadoProyectoId().getId() == 2 || cronograma.getProyecto().getEstadoProyectoId().getId() == 3) {
            int padreEncontrado = 0;
            Actividad actividadAux = actividad;
            while (padreEncontrado == 0) {
                Actividad actividadPadre = actividadFacadeLocal.find(actividadAux.getActividadId());
                if (actividadPadre != null) {
                    if (actividadPadre.getId() == actividadPadre.getActividadId()) {
                        actividad.setActividadId(actividadAux.getId());
                        padreEncontrado = 1;
                    } else {
                        actividadAux = actividadPadre;
                    }
                }
            }
            calculaPorcentajeDuracion(actividad);
            actividadFacadeLocal.edit(actividad);
            calculosActividadObjetivo(actividad);
            if (param.equalsIgnoreCase("autor")) {
                buscarPorAutorProyecto(criterioPorAutorProyecto, usuario, cronograma);
                logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "EDITAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|ActividadId= " + actividad.getActividadId(), usuario));
            } else {
                buscarPorCronograma(criterio, cronograma, usuario);
                logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "EDITAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|ActividadId= " + actividad.getActividadId(), usuario));
            }
            actualizarPorcentajesDuracionActividades(actividad, actividades);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void bajarNivel(Actividad actividad) {
        try {
            sessionActividad.setActividad(actividad);
            RequestContext.getCurrentInstance().execute("PF('dlgBajarNivelActividad').show()");
        } catch (Exception e) {
        }
    }

    public List<String> listadoActividades() {
        List<String> nodos = new ArrayList<>();
        try {
            if (root != null) {
                for (TreeNode node : root.getChildren()) {
                    nodos.add(node.toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return nodos;
    }

    public List<String> listadoActividadesPorAutorProyecto() {
        List<String> nodos = new ArrayList<>();
        try {
            if (rootPorAutorProyecto != null) {
                for (TreeNode node : rootPorAutorProyecto.getChildren()) {
                    nodos.add(node.toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return nodos;
    }

    public void consultaActividades(String criterio, Cronograma cronograma) {
        try {
            this.actividadesConsulta = new ArrayList<>();
            for (Actividad actividad : actividadFacadeLocal.buscarPorProyecto(cronograma.getId())) {
                if (actividad.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                    actividadesConsulta.add(actividad);
                }
            }
        } catch (Exception e) {
        }
    }

    public void confirmarBajarNivel(Actividad actividad, Cronograma cronograma, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int pos = actividadPadre.indexOf(":");
            sessionActividad.getActividad().setActividadId(Long.parseLong(actividadPadre.substring(0, pos)));
            calculaPorcentajeDuracion(actividad);
            actividadFacadeLocal.edit(actividad);
            logFacadeLocal.create(logFacadeLocal.crearLog("Actividad", actividad.getId() + "", "EDITAR", "|Nombre= " + actividad.getNombre() + "|EsActivo= " + actividad.getCronogramaId().getId() + "|Tipo= " + actividad.getTipoActividadId().getId() + "|ActividadId= " + actividad.getActividadId(), usuario));
            if (param.equalsIgnoreCase("autor")) {
                buscarPorAutorProyecto(criterio, usuario, cronograma);
            } else {
                buscarPorCronograma(criterio, cronograma, usuario);
            }
            actualizarPorcentajesDuracionActividades(actividad, actividades);
            calculosActividadObjetivo(actividad);
            RequestContext.getCurrentInstance().execute("PF('dlgBajarNivelActividad').hide()");
        } catch (Exception e) {
        }
    }

    public void buscarPorCronograma(String criterio, Cronograma cronograma, Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_actividad");
            if (tienePermiso == 1) {
                this.actividades = new ArrayList<>();
                for (Actividad actividad : actividadFacadeLocal.buscarPorProyecto(cronograma.getId())) {
                    if (actividad.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                        actividades.add(actividad);
                    }
                }

                root = new DefaultTreeNode("Root", null);
                for (Actividad actividad : actividades) {
                    TreeNode node = null;
                    if (actividad.getActividadId() == actividad.getId()) {
                        node = new DefaultTreeNode(actividad, root);
                    }
                }
                for (Actividad actividad : actividades) {
                    for (TreeNode nodoPadre : root.getChildren()) {
                        if (actividad.getActividadId() != actividad.getId()) {
                            Actividad actividadPadre = (Actividad) nodoPadre.getData();
                            if (actividadPadre.getId() == actividad.getActividadId()) {
                                nodoPadre.getChildren().add(new DefaultTreeNode(actividad));
                            }
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para listar Actividades. Consulte con el Administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarPorDirector(String criterio, Usuario usuario, Proyecto proyecto) {
        this.actividadesPorDirectorProyecto = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_actividad");
            if (tienePermiso == 1) {
                for (Actividad actividad : actividadFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                    if (actividad.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                        actividadesPorDirectorProyecto.add(actividad);
                    }
                }
                rootPorDirectorProyecto = new DefaultTreeNode("Root", null);
                for (Actividad actividad : actividadesPorDirectorProyecto) {
                    TreeNode node = null;
                    if (actividad.getActividadId() == actividad.getId()) {
                        node = new DefaultTreeNode(actividad, rootPorDirectorProyecto);
                    }
                }
                for (Actividad actividad : actividadesPorDirectorProyecto) {
                    for (TreeNode nodoPadre : rootPorDirectorProyecto.getChildren()) {
                        if (actividad.getActividadId() != actividad.getId()) {
                            Actividad actividadPadre = (Actividad) nodoPadre.getData();
                            if (actividadPadre.getId() == actividad.getActividadId()) {
                                nodoPadre.getChildren().add(new DefaultTreeNode(actividad));
                            }
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedCrear(Usuario usuario, Proyecto proyecto) {
        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_actividad");
            if (tienePermiso == 1) {
                renderedCrearPorAutorProyecto = true;
                renderedCrear = true;
            } else {
                renderedCrearPorAutorProyecto = false;
                renderedCrear = false;
            }
        } else {
            renderedCrearPorAutorProyecto = false;
            renderedCrear = false;
        }
    }

    public boolean renderedCrearSubActividad(Actividad actividad, Proyecto proyecto) {
        boolean var = false;
        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
            if (actividad.getId() == actividad.getActividadId()) {
                var = true;
            }
        }
        return var;
    }

    public boolean renderedEliminarSubActividad(Actividad actividad, Proyecto proyecto) {
        boolean var = false;
        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
            if (actividad.getId() == actividad.getActividadId()) {
                var = true;
            }
        }
        return var;
    }

    public boolean renderedEliminar(Actividad actividad, Usuario usuario, Proyecto proyecto) {
        boolean var = false;
        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_actividad");
            if (tienePermiso == 1 && actividad.getEsActivo()) {
                var = true;
            }
        }
        return var;
    }

    public boolean renderedSubirNivel(Actividad actividad) {
        boolean var = false;
        try {
            if (actividad.getActividadId() != actividad.getId()) {
                var = true;
            }
        } catch (Exception e) {
        }
        return var;
    }

    public boolean renderedBajarNivel(Actividad actividad) {
        boolean var = false;
        try {
            if (actividad.getActividadId() == actividad.getId()) {
                var = true;
            }
        } catch (Exception e) {
        }
        return var;
    }

    public void renderedEditar(Usuario usuario, Proyecto proyecto) {
        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_actividad");
            if (tienePermiso == 1) {
                renderedEditar = true;
                renderedEditarPorAutorProyecto = true;
                renderedNoEditar = false;
                renderedNoEditarPorAutorProyecto = false;
                noEditado = false;
            } else {
                renderedEditar = false;
                noEditado = true;
                renderedNoEditarPorAutorProyecto = false;
                renderedEditarPorAutorProyecto = true;
                renderedNoEditar = true;
            }
        } else {
            renderedEditar = false;
            noEditado = true;
            renderedNoEditarPorAutorProyecto = false;
            renderedEditarPorAutorProyecto = true;
            renderedNoEditar = true;
        }
    }

    public void renderedBuscar(Usuario usuario, Proyecto proyecto) {
        if (proyecto.getEstadoProyectoId().getId() == 2 || proyecto.getEstadoProyectoId().getId() == 3) {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_actividad");
            if (tienePermiso == 1) {
                renderedBuscar = true;
            } else {
                renderedBuscar = false;
            }
        } else {
            renderedBuscar = false;
        }
    }

    public boolean renderedHabilitar(Actividad actividad, Usuario usuario) {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_actividad");
        if (tienePermiso == 1 && actividad.getEsActivo() == false) {
            var = true;
        }
        return var;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public TreeNode getRootPorDirectorProyecto() {
        return rootPorDirectorProyecto;
    }

    public void setRootPorDirectorProyecto(TreeNode rootPorDirectorProyecto) {
        this.rootPorDirectorProyecto = rootPorDirectorProyecto;
    }

    public ScheduleModel getEventModelDirector() {
        return eventModelDirector;
    }

    public void setEventModelDirector(ScheduleModel eventModelDirector) {
        this.eventModelDirector = eventModelDirector;
    }

    public ScheduleModel getEventModelAutorProyecto() {
        return eventModelAutorProyecto;
    }

    public void setEventModelAutorProyecto(ScheduleModel eventModelAutorProyecto) {
        this.eventModelAutorProyecto = eventModelAutorProyecto;
    }

    public boolean isRenderedDlgRevisar() {
        return renderedDlgRevisar;
    }

    public void setRenderedDlgRevisar(boolean renderedDlgRevisar) {
        this.renderedDlgRevisar = renderedDlgRevisar;
    }

    public String getCriterioConsulta() {
        return criterioConsulta;
    }

    public void setCriterioConsulta(String criterioConsulta) {
        this.criterioConsulta = criterioConsulta;
    }

    public List<Actividad> getActividadesConsulta() {
        return actividadesConsulta;
    }

    public void setActividadesConsulta(List<Actividad> actividadesConsulta) {
        this.actividadesConsulta = actividadesConsulta;
    }

    public Date getFechaInicioLimit() {
        return fechaInicioLimit;
    }

    public void setFechaInicioLimit(Date fechaInicioLimit) {
        this.fechaInicioLimit = fechaInicioLimit;
    }

    public Date getFechaFinLimit() {
        return fechaFinLimit;
    }

    public void setFechaFinLimit(Date fechaFinLimit) {
        this.fechaFinLimit = fechaFinLimit;
    }

    public SessionActividad getSessionActividad() {
        return sessionActividad;
    }

    public void setSessionActividad(SessionActividad sessionActividad) {
        this.sessionActividad = sessionActividad;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public boolean isNoEditado() {
        return noEditado;
    }

    public void setNoEditado(boolean noEditado) {
        this.noEditado = noEditado;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public String getActividadPadre() {
        return actividadPadre;
    }

    public void setActividadPadre(String actividadPadre) {
        this.actividadPadre = actividadPadre;
    }

    public String getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(String estadoActividad) {
        this.estadoActividad = estadoActividad;
    }

    public boolean isEstadoEsSeleccionado() {
        return estadoEsSeleccionado;
    }

    public void setEstadoEsSeleccionado(boolean estadoEsSeleccionado) {
        this.estadoEsSeleccionado = estadoEsSeleccionado;
    }

    public SessionProyecto getSessionProyecto() {
        return sessionProyecto;
    }

    public void setSessionProyecto(SessionProyecto sessionProyecto) {
        this.sessionProyecto = sessionProyecto;
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

    public Date getFechaInicioActividadPadre() {
        return fechaInicioActividadPadre;
    }

    public void setFechaInicioActividadPadre(Date fechaInicioActividadPadre) {
        this.fechaInicioActividadPadre = fechaInicioActividadPadre;
    }

    public Date getFechaFinActividadPadre() {
        return fechaFinActividadPadre;
    }

    public void setFechaFinActividadPadre(Date fechaFinActividadPadre) {
        this.fechaFinActividadPadre = fechaFinActividadPadre;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<Actividad> getActividadesPorDirectorProyecto() {
        return actividadesPorDirectorProyecto;
    }

    public void setActividadesPorDirectorProyecto(List<Actividad> actividadesPorDirectorProyecto) {
        this.actividadesPorDirectorProyecto = actividadesPorDirectorProyecto;
    }

    public String getCriterioPorDirectorProyecto() {
        return criterioPorDirectorProyecto;
    }

    public void setCriterioPorDirectorProyecto(String criterioPorDirectorProyecto) {
        this.criterioPorDirectorProyecto = criterioPorDirectorProyecto;
    }

    public String getCriterioPorAutorProyecto() {
        return criterioPorAutorProyecto;
    }

    public void setCriterioPorAutorProyecto(String criterioPorAutorProyecto) {
        this.criterioPorAutorProyecto = criterioPorAutorProyecto;
    }

    public TreeNode getRootPorAutorProyecto() {
        return rootPorAutorProyecto;
    }

    public void setRootPorAutorProyecto(TreeNode rootPorAutorProyecto) {
        this.rootPorAutorProyecto = rootPorAutorProyecto;
    }

    public boolean isRenderedEditarPorAutorProyecto() {
        return renderedEditarPorAutorProyecto;
    }

    public void setRenderedEditarPorAutorProyecto(boolean renderedEditarPorAutorProyecto) {
        this.renderedEditarPorAutorProyecto = renderedEditarPorAutorProyecto;
    }

    public boolean isRenderedNoEditarPorAutorProyecto() {
        return renderedNoEditarPorAutorProyecto;
    }

    public void setRenderedNoEditarPorAutorProyecto(boolean renderedNoEditarPorAutorProyecto) {
        this.renderedNoEditarPorAutorProyecto = renderedNoEditarPorAutorProyecto;
    }

    public boolean isRenderedCrearPorAutorProyecto() {
        return renderedCrearPorAutorProyecto;
    }

    public void setRenderedCrearPorAutorProyecto(boolean renderedCrearPorAutorProyecto) {
        this.renderedCrearPorAutorProyecto = renderedCrearPorAutorProyecto;
    }

    public boolean isRenderedEliminarPorAutorProyecto() {
        return renderedEliminarPorAutorProyecto;
    }

    public void setRenderedEliminarPorAutorProyecto(boolean renderedEliminarPorAutorProyecto) {
        this.renderedEliminarPorAutorProyecto = renderedEliminarPorAutorProyecto;
    }

    public List<Actividad> getActividadesPorAutor() {
        return actividadesPorAutor;
    }

    public void setActividadesPorAutor(List<Actividad> actividadesPorAutor) {
        this.actividadesPorAutor = actividadesPorAutor;
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

    public AdministrarDocumentosActividad getAdministrarDocumentosActividad() {
        return administrarDocumentosActividad;
    }

    public void setAdministrarDocumentosActividad(AdministrarDocumentosActividad administrarDocumentosActividad) {
        this.administrarDocumentosActividad = administrarDocumentosActividad;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionDocenteUsuario getSessionDocenteUsuario() {
        return sessionDocenteUsuario;
    }

    public void setSessionDocenteUsuario(SessionDocenteUsuario sessionDocenteUsuario) {
        this.sessionDocenteUsuario = sessionDocenteUsuario;
    }

    public SessionEstudianteUsuario getSessionEstudianteUsuario() {
        return sessionEstudianteUsuario;
    }

    public void setSessionEstudianteUsuario(SessionEstudianteUsuario sessionEstudianteUsuario) {
        this.sessionEstudianteUsuario = sessionEstudianteUsuario;
    }

//</editor-fold>
}
