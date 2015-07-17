/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.evaluacionTribunal;

import com.jlmallas.comun.entity.EventoPersona;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.EventoPersonaService;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.entity.RangoEquivalencia;
import edu.unl.sigett.entity.RangoNota;
import edu.unl.sigett.entity.Tribunal;
import edu.unl.sigett.enumeration.CatalogoEvaluacionEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.miembroTribunal.MiembroTribunalDTO;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.CalificacionMiembroTribunalService;
import edu.unl.sigett.service.EvaluacionTribunalService;
import edu.unl.sigett.service.MiembroTribunalService;
import edu.unl.sigett.service.ProyectoCarreraOfertaService;
import edu.unl.sigett.service.RangoEquivalenciaService;
import edu.unl.sigett.service.RangoNotaService;
import edu.unl.sigett.tribunal.SessionTribunal;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.seguridad.service.UsuarioService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author jorge-luis
 */
@Named(value = "evaluacionTribunalController")
@SessionScoped
public class EvaluacionTribunalController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionEvaluacionTribunal sessionEvaluacionTribunal;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionTribunal sessionTribunal;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/SigettService/EvaluacionTribunalServiceImplement!edu.unl.sigett.service.EvaluacionTribunalService")
    private EvaluacionTribunalService evaluacionTribunalService;
    @EJB(lookup = "java:global/ComunService/EventoPersonaServiceImplement!com.jlmallas.comun.service.EventoPersonaService")
    private EventoPersonaService eventoPersonaService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/SigettService/RangoNotaServiceImplement!edu.unl.sigett.service.RangoNotaService")
    private RangoNotaService rangoNotaService;
    @EJB(lookup = "java:global/SigettService/RangoEquivalenciaServiceImplement!edu.unl.sigett.service.RangoEquivalenciaService")
    private RangoEquivalenciaService rangoEquivalenciaService;
    @EJB(lookup = "java:global/SigettService/MiembroTribunalServiceImplement!edu.unl.sigett.service.MiembroTribunalService")
    private MiembroTribunalService miembroTribunalService;
    @EJB(lookup = "java:global/SigettService/CalificacionMiembroTribunalServiceImplement!edu.unl.sigett.service.CalificacionMiembroTribunalService")
    private CalificacionMiembroTribunalService calificacionMiembroTribunalService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(EvaluacionTribunalController.class.getName());

    public EvaluacionTribunalController() {
    }

    public void preRenderView() {
        renderedCalificar();
        renderedCrear();
        renderedEditar();
        renderedEliminar();
        buscar();
        
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void crear() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "crear_evalulacion_tribunal").trim());
            if (tienePermiso == 1) {
                sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
                sessionEvaluacionTribunal.getEvaluacionTribunal().setLugar("ninguno");
                sessionEvaluacionTribunal.getEvaluacionTribunal().setObservacion("ninguno");
                sessionEvaluacionTribunal.getEvaluacionTribunal().setSugerencia("ninguno");
                sessionEvaluacionTribunal.getEvaluacionTribunal().setFechaInicio(sessionEvaluacionTribunal.getFechaInicioSeleccionada());
                sessionEvaluacionTribunal.getEvaluacionTribunal().setFechaFin(sessionEvaluacionTribunal.getFechaFinSeleccionada());
                fijarCatalogoEvaluacion(sessionEvaluacionTribunal.getEvaluacionTribunal());
                listadoRangos();
                sessionEvaluacionTribunal.setRenderedDlgCrud(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').show()");
                return;
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_crear"), "");
        } catch (Exception e) {
        }
    }

    public void cancelarEdicion() {
        sessionEvaluacionTribunal.setRenderedDlgCrud(Boolean.FALSE);
        sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
        RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').hide()");
    }

    public void editar(EvaluacionTribunal evaluacionTribunal) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "crear_evalulacion_tribunal").trim());
            if (tienePermiso == 1) {
                listadoRangos();
                sessionEvaluacionTribunal.setEvaluacionTribunal(evaluacionTribunal);
                fijarCatalogoEvaluacion(sessionEvaluacionTribunal.getEvaluacionTribunal());
                sessionEvaluacionTribunal.setRenderedDlgCrud(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').show()");
                return;
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_editar"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void calculaNota(EvaluacionTribunal evaluacionTribunal) {
        try {
            BigDecimal nota = BigDecimal.ZERO;
            Integer numeroMiembros = 0;
            MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
            miembroTribunalBuscar.setTribunalId(sessionTribunal.getTribunal());
            List<MiembroTribunal> miembros = miembroTribunalService.buscar(miembroTribunalBuscar);
            numeroMiembros = !miembros.isEmpty() ? miembros.size() : 0;
            CalificacionMiembro calificacionMiembroBuscar = new CalificacionMiembro();
            calificacionMiembroBuscar.setEvaluacionTribunalId(evaluacionTribunal);
            List<CalificacionMiembro> calificacionMiembros = calificacionMiembroTribunalService.buscar(null);
            for (CalificacionMiembro cm : calificacionMiembros) {
                if (cm.getEsActivo()) {
                    nota = nota.add(cm.getNota());
                }
            }
            nota = nota.divide(new BigDecimal(numeroMiembros));
            evaluacionTribunal.setNota(nota);
        } catch (Exception e) {
        }
    }
//
//    public boolean existeSustentacionPublica(Tribunal tribunal) {
//        boolean var = false;
////        try {
////            for (EvaluacionTribunal evaluacionTribunal : evaluacionTribunalFacadeLocal.buscarPorTribunal(tribunal.getId())) {
////                if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo())) {
////                    var = true;
////                    break;
////                }
////            }
////        } catch (Exception e) {
////        }
//        return var;
//    }
//
////    public void actualizaEstadoAutores(EvaluacionTribunal evaluacionTribunal, EstadoAutor estadoAutor, EstadoEstudianteCarrera estadoEstudianteCarrera) {
////        try {
////            for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(evaluacionTribunal.getTribunalId().getProyectoId().getId())) {
////                EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
////                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
////                    autorProyecto.setEstadoAutorId(estadoAutor);
////                    autorProyectoFacadeLocal.edit(autorProyecto);
////                    estudianteCarrera.setEstadoId(estadoEstudianteCarrera);
////                    estudianteCarreraFacadeLocal.edit(estudianteCarrera);
////                }
////            }
////        } catch (Exception e) {
////        }
////    }
//
//    public void grabar(EvaluacionTribunal evaluacionTribunal, Tribunal tribunal, Proyecto proyecto, Usuario usuario) {
//        try {
////            CatalogoEvento catalogoEvento = catalogoEventoFacadeLocal.buscarPorCodigo(CatalogoEventoEnum.SUSTENTACION.getTipo());
////            Evento evento = null;
////            EstadoProyecto estadoProyecto = null;
//////            EstadoEstudianteCarrera estadoEstudianteCarrera = null;
////            FacesContext facesContext = FacesContext.getCurrentInstance();
////            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
////            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
////            int posRangoNota = 0;
////            RangoNota rn = null;
////            if (rangoNota != null) {
////                posRangoNota = rangoNota.indexOf(":");
////                rn = rangoNotaFacadeLocal.find(Integer.parseInt(rangoNota.substring(0, posRangoNota)));
////            } else {
////                if (evaluacionTribunal.getRangoNotaId() != null) {
////                    rn = evaluacionTribunal.getRangoNotaId();
////                }
////            }
////            if (rn != null) {
////                evaluacionTribunal.setRangoNotaId(rn);
////                for (RangoEquivalencia rangoEquivalencia : rangoEquivalenciaFacadeLocal.buscarPorRangoNota(rn.getId())) {
////                    if (evaluacionTribunal.getNota() >= rangoEquivalencia.getNotaInicio() && evaluacionTribunal.getNota() <= rangoEquivalencia.getNotaFin()) {
////                        evaluacionTribunal.setRangoEquivalenciaId(rangoEquivalencia);
////                        break;
////                    }
////                }
////            }
////            if (administrarMiembrosTribunal.existeUnPresidente(tribunal)) {
////                if (existeMiembroOtraSustentacion(evaluacionTribunal, usuario, proyecto) == false) {
////                    if (tribunal.getProyectoId().getId() != null) {
////                        proyecto = proyectoFacadeLocal.find(tribunal.getProyectoId().getId());
////                    }
////                    if (evaluacionTribunal.getId() == null) {
////                        if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
////                            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_evaluacion_tribunal");
////                            if (tienePermiso == 1) {
////                                evaluacionTribunal.setTribunalId(tribunal);
////                                calculaNota(tribunal, evaluacionTribunal);
////                                evaluacionTribunal.setFechaPlazo(evaluacionTribunal.getFechaFin());
////                                evaluacionTribunalFacadeLocal.create(evaluacionTribunal);
////                                evento = new Evento(null, evaluacionTribunal.getCatalogoEvaluacionId().getNombre(), evaluacionTribunal.getFechaInicio(), evaluacionTribunal.getFechaFin(), evaluacionTribunal.getId() + "");
////                                eventoFacadeLocal.edit(evento);
////                                administrarCalificacionMiembro.grabarCalificacionesMiembro(tribunal, evaluacionTribunal);
////                                listadoSustentacionesPorUsuarioCarrera(usuario, tribunal.getProyectoId());
////                                if (param.equalsIgnoreCase("grabar-dlg")) {
////                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').hide()");
////                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
////                                    FacesContext.getCurrentInstance().addMessage(null, message);
////                                    sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
////                                } else {
////                                    if (param.equalsIgnoreCase("grabar-editar-dlg")) {
////                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
////                                        FacesContext.getCurrentInstance().addMessage(null, message);
////                                    }
////                                }
////                            } else {
////                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                                FacesContext.getCurrentInstance().addMessage(null, message);
////                            }
////                        } else {
////                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                            FacesContext.getCurrentInstance().addMessage(null, message);
////                        }
////                    } else {
////                        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_evaluacion_tribunal");
////                        int tienePermiso1 = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_privada");
////                        int tienePermiso2 = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_publica");
////                        EstadoAutor estadoAutor = null;
////                        if (tienePermiso == 1 || tienePermiso1 == 1 || tienePermiso2 == 1) {
////                            evaluacionTribunal.setTribunalId(tribunal);
////                            calculaNota(tribunal, evaluacionTribunal);
////                            evaluacionTribunalFacadeLocal.edit(evaluacionTribunal);
////                            evento = eventoFacadeLocal.buscarPorCategoriaEvento(catalogoEvento.getId(), evaluacionTribunal.getId() + "");
////                            if (evento == null) {
////                                evento = new Evento(null, evaluacionTribunal.getCatalogoEvaluacionId().getNombre(), evaluacionTribunal.getFechaInicio(), evaluacionTribunal.getFechaFin(), evaluacionTribunal.getId() + "");
////                                eventoFacadeLocal.edit(evento);
////                            } else {
////                                evento.setTitulo(evaluacionTribunal.getCatalogoEvaluacionId().getNombre());
////                                evento.setFechaInicio(evaluacionTribunal.getFechaInicio());
////                                evento.setFechaFin(evaluacionTribunal.getFechaFin());
////                                eventoFacadeLocal.edit(evento);
////                            }
////                            administrarCalificacionMiembro.grabarCalificacionesMiembro(tribunal, evaluacionTribunal);
////                            if (evaluacionTribunal.getEsAptoCalificar()) {
////                                /*Privada*/
////                                if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo())) {
////                                    if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())) {
////                                        if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
////                                            /*APROBADA*/
////                                            estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo());
////                                            estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.SUSTENTACIONPUBLICA.getTipo());
//////                                            estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.buscarPorCodigo(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
////                                        } else {
////                                            if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
////                                                /*RECUPERACIÓN*/
////                                                estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo());
////                                                estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.RECUPERACIONPUBLICA.getTipo());
//////                                                estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.buscarPorCodigo(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
////                                            } else {
////                                                /*REPROBADO*/
////                                                estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
////                                                estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.REPROBADO.getTipo());
//////                                                estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
////                                            }
////                                        }
////                                    } else {
////                                        /*RECUPERACIÓN PRIVADA*/
////                                        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())) {
////                                            if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
////                                                /*APROBADA*/
////                                                estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.APROBADO.getTipo());
////                                                estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.APROBADO.getTipo());
//////                                                estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(3);
////                                            } else {
////                                                if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
////                                                    /*RECUPERACIÓN PRIVADA*/
////                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo());
////                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.RECUPERACIONPRIVADA.getTipo());
//////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(2);
////                                                } else {
////                                                    /*REPROBADO*/
////                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
////                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
//////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(2);
////                                                }
////                                            }
////                                        }
////                                    }
////                                } else {
////                                    /*PÚBLICA*/
////                                    if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo())) {
////                                        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())) {
////                                            if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
////                                                estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.APROBADO.getTipo());
////                                                estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.APROBADO.getTipo());
//////                                                estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(3);
////                                            } else {
////                                                if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
////                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo());
////                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.RECUPERACIONPUBLICA.getTipo());
//////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(2);
////                                                } else {
////                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
////                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.REPROBADO.getTipo());
//////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.buscarPorCodigo(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
////                                                }
////                                            }
////                                        } else {
////                                            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
////                                                if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
////                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.APROBADO.getTipo());
////                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.APROBADO.getTipo());
//////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(EstadoEstudianteCarreraEnum.TITULADO.getTipo());
////                                                } else {
////                                                    if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
////                                                        estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo());
////                                                        estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.RECUPERACIONPUBLICA.getTipo());
//////                                                        estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
////                                                    } else {
////                                                        estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
////                                                        estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
//////                                                        estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
////                                                    }
////                                                }
////                                            }
////                                        }
////                                    }
////                                }
////                                tribunal.getProyectoId().setEstadoProyectoId(estadoProyecto);
////                                proyectoFacadeLocal.edit(tribunal.getProyectoId());
//////                                actualizaEstadoAutores(evaluacionTribunal, estadoAutor, estadoEstudianteCarrera);
////                            }
////                            listadoSustentacionesPorUsuarioCarrera(usuario, tribunal.getProyectoId());
////                            if (param != null) {
////                                if (param.equalsIgnoreCase("grabar-dlg")) {
////                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').hide()");
////                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
////                                    FacesContext.getCurrentInstance().addMessage(null, message);
////                                    sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
////                                } else {
////                                    if (param.equalsIgnoreCase("grabar-editar-dlg")) {
////                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
////                                        FacesContext.getCurrentInstance().addMessage(null, message);
////                                    }
////                                }
////                            } else {
////                                sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
////                            }
////                        } else {
////                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                            FacesContext.getCurrentInstance().addMessage(null, message);
////                        }
////                    }
////
////                } else {
////                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.miembro") + " " + bundle.getString("lbl.miembro_encontrado"), "");
////                    FacesContext.getCurrentInstance().addMessage(null, message);
////                }
////            } else {
////                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + "" + bundle.getString("lbl.presidente") + ". " + bundle.getString("lbl.msm_consulte"), "");
////                FacesContext.getCurrentInstance().addMessage(null, message);
////            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//

    /**
     * VAIDAR NOTA DE EVALUACION
     *
     * @param evaluacionTribunal
     */
    public void validarNota(EvaluacionTribunal evaluacionTribunal) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (evaluacionTribunal.getId() != null) {
                RangoNota rn = null;
                if (evaluacionTribunal.getRangoNotaId() != null) {
                    rn = evaluacionTribunal.getRangoNotaId();
                }
                if (rn == null) {
                    return;
                }
                evaluacionTribunal.setRangoNotaId(rn);
                RangoEquivalencia rangoEquivalenciaBuscar = new RangoEquivalencia();
                rangoEquivalenciaBuscar.setRangoNotaId(rn);
                List<RangoEquivalencia> rangoEquivalencias = rangoEquivalenciaService.buscar(rangoEquivalenciaBuscar);
                for (RangoEquivalencia rangoEquivalencia : rangoEquivalencias) {
                    if (evaluacionTribunal.getNota().floatValue() >= rangoEquivalencia.getNotaInicio().floatValue()
                            && evaluacionTribunal.getNota().floatValue() <= rangoEquivalencia.getNotaFin().floatValue()) {
                        evaluacionTribunal.setRangoEquivalenciaId(rangoEquivalencia);
                        break;
                    }
                }
                evaluacionTribunalService.actualizar(evaluacionTribunal);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void buscar() {
        try {
            this.sessionEvaluacionTribunal.getEvaluacionesTribunal().clear();
            EvaluacionTribunal evaluacionTribunalBuscar = new EvaluacionTribunal();
            evaluacionTribunalBuscar.setTribunalId(sessionTribunal.getTribunal());
            evaluacionTribunalBuscar.setEsActivo(Boolean.TRUE);
            sessionEvaluacionTribunal.setEvaluacionesTribunal(evaluacionTribunalService.buscar(evaluacionTribunalBuscar));
        } catch (Exception e) {
        }
    }

    public void remover(EvaluacionTribunal evaluacionTribunal) {
        try {
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "eliminar_evalulacion_tribunal").trim());
            if (tienePermiso == 1) {
                evaluacionTribunal.setEsActivo(Boolean.FALSE);
                evaluacionTribunalService.actualizar(evaluacionTribunal);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    /**
     * PERMITE FIJAR UN CATALOGO A UNA EVALAUACIÓN DE TRIBUNAL
     *
     * @param evaluacionTribunal
     */
    public void fijarCatalogoEvaluacion(EvaluacionTribunal evaluacionTribunal) {
        try {
            if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())) {
                Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOEVALUACIONTRIBUNAL.getTipo(), CatalogoEvaluacionEnum.SUSTENTACIONPRIVADA.getTipo());
                evaluacionTribunal.setCatalogoEvaluacionId(item.getId());
                return;
            }
            if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOEVALUACIONTRIBUNAL.getTipo(), CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo());
                evaluacionTribunal.setCatalogoEvaluacionId(item.getId());
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    @SuppressWarnings("UnusedAssignment")
    public Boolean validaDocenteDisponible(Persona persona) {
        Date fechaInicioEvaluacion = null;
        Date fechaFinEvaluacion = null;
        Date fechaInicioEvento = null;
        Date fechaFinEvento = null;
        Date horaInicioEvaluacion = null;
        Date horaFinEvaluacion = null;
        Date horaInicioEvento = null;
        Date horaFinEvento = null;
        List<EventoPersona> eventoPersonas = eventoPersonaService.buscar(new EventoPersona(null, persona, null));
        for (EventoPersona eventoPersona : eventoPersonas) {
            fechaInicioEvento = cabeceraController.getUtilService().parserFecha(
                    cabeceraController.getUtilService().formatoFecha(eventoPersona.getEvento().getFechaInicio(), "yyyy-MMM-dd"), "yyyy-MMM-dd");
            fechaFinEvento = cabeceraController.getUtilService().parserFecha(
                    cabeceraController.getUtilService().formatoFecha(eventoPersona.getEvento().getFechaFin(), "yyyy-MMM-dd"), "yyyy-MMM-dd");
            horaInicioEvento = cabeceraController.getUtilService().parserFecha(
                    cabeceraController.getUtilService().formatoFecha(eventoPersona.getEvento().getFechaInicio(), "HH:mm:ssZ"), "HH:mm:ssZ");
            horaFinEvento = cabeceraController.getUtilService().parserFecha(
                    cabeceraController.getUtilService().formatoFecha(eventoPersona.getEvento().getFechaFin(), "HH:mm:ssZ"), "HH:mm:ssZ");
            for (EvaluacionTribunal evaluacionTribunal : sessionTribunal.getTribunal().getEvaluacionTribunalList()) {
                fechaInicioEvaluacion = cabeceraController.getUtilService().parserFecha(
                        cabeceraController.getUtilService().formatoFecha(evaluacionTribunal.getFechaInicio(), "yyyy-MMM-dd"), "yyyy-MMM-dd");
                fechaFinEvaluacion = cabeceraController.getUtilService().parserFecha(
                        cabeceraController.getUtilService().formatoFecha(evaluacionTribunal.getFechaFin(), "yyyy-MMM-dd"), "yyyy-MMM-dd");
                horaInicioEvaluacion = cabeceraController.getUtilService().parserFecha(
                        cabeceraController.getUtilService().formatoFecha(evaluacionTribunal.getFechaInicio(), "HH:mm:ssZ"), "HH:mm:ssZ");
                horaFinEvaluacion = cabeceraController.getUtilService().parserFecha(
                        cabeceraController.getUtilService().formatoFecha(evaluacionTribunal.getFechaFin(), "HH:mm:ssZ"), "HH:mm:ssZ");
                if ((fechaInicioEvaluacion.equals(fechaInicioEvento) || fechaInicioEvaluacion.equals(fechaFinEvento)
                        || (fechaInicioEvaluacion.after(fechaInicioEvento) && fechaInicioEvaluacion.before(fechaFinEvento)))
                        || (fechaFinEvaluacion.equals(fechaInicioEvento) || fechaFinEvaluacion.equals(fechaFinEvento)
                        || (fechaFinEvaluacion.after(fechaFinEvento) && fechaFinEvaluacion.before(fechaFinEvento)))) {
                    if ((horaInicioEvaluacion.equals(horaInicioEvento) || horaInicioEvaluacion.equals(horaFinEvento)
                            || (horaInicioEvaluacion.after(horaInicioEvento) && horaInicioEvaluacion.before(horaFinEvento)))
                            || (horaFinEvaluacion.equals(horaInicioEvento) || horaFinEvaluacion.equals(horaFinEvento)
                            || (horaFinEvaluacion.after(horaInicioEvento) && horaFinEvaluacion.before(horaFinEvento)))) {
                        return Boolean.FALSE;
                    }
                }
            }
        }
        return Boolean.TRUE;
    }

    public Boolean validaMiembrosTribunal() {
        for (MiembroTribunal miembroTribunal : sessionTribunal.getTribunal().getMiembroList()) {
            Persona persona = personaService.buscarPorId(new Persona(miembroTribunal.getDocenteId()));
            if (!validaDocenteDisponible(persona)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void listadoRangos() {
        sessionEvaluacionTribunal.setRangoNotas(rangoNotaService.buscar(new RangoNota()));
    }

    /**
     * GENERAR CRONOGRAMA CON TODAS LA EVALUACIONES DE TRIBUNALES DE PROYECTOS
     * DE LA CARRERA ADMINISTRADA POR EL USUARIO
     */
    public void schudele() {
        try {
            this.sessionEvaluacionTribunal.setEventModel(new DefaultScheduleModel());
            for (Carrera carrera : sessionProyecto.getCarreras()) {
                List<EvaluacionTribunal> evaluacionTribunals = evaluacionTribunalService.buscarPorCarrera(carrera.getId());
                for (EvaluacionTribunal evaluacionTribunal : evaluacionTribunals) {
                    DefaultScheduleEvent evento = new DefaultScheduleEvent();
                    evento.setData(evaluacionTribunal);
                    evento.setStartDate(evaluacionTribunal.getFechaInicio());
                    evento.setEndDate(evaluacionTribunal.getFechaFin());
                    Item item = itemService.buscarPorId(evaluacionTribunal.getCatalogoEvaluacionId());
                    if (evaluacionTribunal.getTribunalId().getProyectoId().getId() == sessionProyecto.getProyectoSeleccionado().getId()) {
                        evento.setTitle(item.getNombre());
                        evento.setStyleClass("propio");
                        continue;
                    }
                    evento.setTitle(item.getNombre() + ": " + sessionProyecto.getProyectoSeleccionado().getTemaActual());
                    evento.setStyleClass("otro");
                    sessionEvaluacionTribunal.getEventModel().addEvent(evento);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        try {
            ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
            sessionEvaluacionTribunal.setFechaInicioSeleccionada(event.getStartDate());
            Calendar fechaFin = Calendar.getInstance();
            fechaFin.setTime(event.getStartDate());
            fechaFin.add(Calendar.HOUR_OF_DAY, 1);
            sessionEvaluacionTribunal.setFechaFinSeleccionada(fechaFin.getTime());
            editar((EvaluacionTribunal) event.getData());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onEventCrear(SelectEvent selectEvent) {
        try {
            ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
            sessionEvaluacionTribunal.setFechaInicioSeleccionada(event.getStartDate());
            Calendar fechaFin = Calendar.getInstance();
            fechaFin.setTime(event.getStartDate());
            fechaFin.add(Calendar.HOUR_OF_DAY, 1);
            sessionEvaluacionTribunal.setFechaFinSeleccionada(fechaFin.getTime());
            crear();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void onEventMove(ScheduleEntryMoveEvent moveEvent) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            ScheduleEvent event = (ScheduleEvent) moveEvent.getScheduleEvent();
            EvaluacionTribunal ev = ((EvaluacionTribunal) event.getData());
            ev.setFechaInicio(event.getStartDate());
            ev.setFechaFin(event.getEndDate());
            ev.setFechaPlazo(event.getEndDate());
            if (ev.getEsAptoCalificar()) {
                return;
            }
            if (!validaMiembrosTribunal()) {
                return;
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("miembro_ocupado"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void onEventResize(ScheduleEntryResizeEvent resizeEvent) {
        ScheduleEvent event = (ScheduleEvent) resizeEvent.getScheduleEvent();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        EvaluacionTribunal ev = ((EvaluacionTribunal) event.getData());
        ev.setFechaInicio(event.getStartDate());
        ev.setFechaFin(event.getEndDate());
        ev.setFechaPlazo(event.getEndDate());
        if (ev.getEsAptoCalificar()) {
            return;
        }
        if (!validaMiembrosTribunal()) {
            return;
        }
        cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("miembro_ocupado"), "");
    }

//
//    public boolean permitirCrearOtraSustentacion(EvaluacionTribunal evaluacionTribunal) {
//        boolean var = false;
////        int numero = Integer.parseInt(configuracionGeneralFacadeLocal.find(45).getValor());
////        int contador = 0;
//        try {
////            for (EvaluacionTribunal eva : evaluacionTribunalFacadeLocal.buscarPorTribunal(evaluacionTribunal.getTribunalId().getId())) {
////                if (eva.getCatalogoEvaluacionId().getId() == evaluacionTribunal.getCatalogoEvaluacionId().getId()) {
////                    contador++;
////                }
////            }
////            if (contador < numero) {
////                var = true;
////            } else {
////                var = false;
////            }
//        } catch (Exception e) {
//        }
//        return var;
//    }
////</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
//
//    public boolean renderedImprimirActa(EvaluacionTribunal evaluacionTribunal, Usuario usuario) {
//        boolean var = false;
////        int tienePermiso = 0;
////        try {
////            if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPRIVADA.getTipo())) {
////                tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_privada");
////                if (tienePermiso == 1) {
////                    var = true;
////                } else {
////                    var = false;
////                }
////            } else {
////                if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo())) {
////                    tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_publica");
////                    if (tienePermiso == 1) {
////                        var = true;
////                    } else {
////                        var = false;
////                    }
////                }
////            }
////        } catch (Exception e) {
////        }
//        return var;
//    }
//
    public void renderedCalificar() {
        try {
            sessionEvaluacionTribunal.setRenderedCalificarPrivada(Boolean.FALSE);
            sessionEvaluacionTribunal.setRenderedCalificarPublica(Boolean.FALSE);
            if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())) {
                Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.PERMISOS, "calificar_evaluacion_tribunal_privada").trim());
                if (tienePermiso == 1) {
                    sessionEvaluacionTribunal.setRenderedCalificarPrivada(Boolean.TRUE);
                }
                return;
            }
            if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.PERMISOS, "calificar_evaluacion_tribunal_publica").trim());
                if (tienePermiso == 1) {
                    sessionEvaluacionTribunal.setRenderedCalificarPublica(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
        }
    }
//

    public void renderedCrear() {
        try {
            sessionEvaluacionTribunal.setRenderedCrear(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "crear_evalulacion_tribunal").trim());
            if (tienePermiso == 1) {
                sessionEvaluacionTribunal.setRenderedCrear(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void renderedEditar() {
        try {
            sessionEvaluacionTribunal.setRenderedEditar(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "editar_evalulacion_tribunal").trim());
            if (tienePermiso == 1) {
                sessionEvaluacionTribunal.setRenderedEditar(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void renderedEliminar() {
        try {
            sessionEvaluacionTribunal.setRenderedEliminar(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "eliminar_evalulacion_tribunal").trim());
            if (tienePermiso == 1) {
                sessionEvaluacionTribunal.setRenderedEliminar(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }
//
//    public void renderedAceptarNota(Docente docente) {
//        try {
////            MiembroTribunal miembro = null;
////            List<MiembroTribunal> miembros = new ArrayList<>();
////            miembros = miembroFacadeLocal.buscarPorDocente(docente.getId());
////            if (miembros != null) {
////                miembro = !miembros.isEmpty() ? miembros.get(0) : null;
////                if (miembro != null) {
////                    if (miembro.getCargoId().getId() == 1) {
////                        renderedAceptarNota = true;
////                    } else {
////                        renderedAceptarNota = false;
////                    }
////                } else {
////                    renderedAceptarNota = false;
////                }
////            } else {
////                renderedAceptarNota = false;
////            }
//        } catch (Exception e) {
//        }
//    }
//
////</editor-fold>
}
