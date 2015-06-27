/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import com.jlmallas.comun.dao.PersonaDao;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.finalizacion.managed.session.SessionEvaluacionTribunal;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.RangoEquivalencia;
import edu.unl.sigett.entity.RangoNota;
import edu.unl.sigett.entity.Tribunal;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.dao.EvaluacionTribunalFacadeLocal;
import edu.unl.sigett.dao.MiembroFacadeLocal;
import edu.unl.sigett.dao.ProyectoDao;
import edu.unl.sigett.dao.RangoEquivalenciaFacadeLocal;
import edu.unl.sigett.dao.RangoNotaFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.unl.sigett.enumeration.CatalogoEvaluacionEnum;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "consultaSustentaciones",
            pattern = "/consultaSustentaciones/",
            viewId = "/faces/pages/sigett/consultarSustentaciones.xhtml"
    )
})
public class AdministrarEvaluacionesTribunal implements Serializable {

    @Inject
    private SessionEvaluacionTribunal sessionEvaluacionTribunal;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private AdministrarMiembrosTribunal administrarMiembrosTribunal;
    @Inject
    private AdministrarCalificacionMiembro administrarCalificacionMiembro;

    @EJB
    private UsuarioDao usuarioFacadeLocal;

    @EJB
    private RangoEquivalenciaFacadeLocal rangoEquivalenciaFacadeLocal;
    @EJB
    private RangoNotaFacadeLocal rangoNotaFacadeLocal;
    @EJB
    private EvaluacionTribunalFacadeLocal evaluacionTribunalFacadeLocal;
    @EJB
    private ProyectoDao proyectoFacadeLocal;
    @EJB
    private MiembroFacadeLocal miembroFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private DocenteDao docenteFacadeLocal;

    private boolean renderedEditar;
    private boolean renderedNoEditar;
    private boolean renderedEliminar;
    private boolean renderedCrear;
    private boolean renderedBuscar;
    private boolean renderedDlgEditar;
    private boolean renderedCalificarPublica;
    private boolean renderedCalificarPrivada;
    private boolean renderedAceptarNota;

    private List<EvaluacionTribunal> evaluacionTribunals;
    private List<EvaluacionTribunal> evaluacionTribunalesByCarrera;
    private Docente docenteEncontrado;
    private String rangoNota;
    private ScheduleModel eventModel;
    private ScheduleModel eventModelConsulta;
    private EvaluacionTribunal consultaEvaluacionTribunal;

    public AdministrarEvaluacionesTribunal() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear(Usuario usuario, Date fechaInicio, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_evaluacion_tribunal");
//                if (tienePermiso == 1) {
//                    sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
//                    sessionEvaluacionTribunal.getEvaluacionTribunal().setLugar("ninguno");
//                    sessionEvaluacionTribunal.getEvaluacionTribunal().setObservacion("ninguno");
//                    sessionEvaluacionTribunal.getEvaluacionTribunal().setSugerencia("ninguno");
//                    sessionEvaluacionTribunal.getEvaluacionTribunal().setFechaInicio(fechaInicio);
//                    Calendar fechaFin = Calendar.getInstance();
//                    fechaFin.setTime(fechaInicio);
//                    fechaFin.add(Calendar.HOUR_OF_DAY, 1);
//                    sessionEvaluacionTribunal.getEvaluacionTribunal().setFechaFin(fechaFin.getTime());
//                    fijarCatalogoEvaluación(sessionEvaluacionTribunal.getEvaluacionTribunal(), proyecto);
//                    renderedCalificar(usuario, proyecto);
//                    renderedDlgEditar = true;
//                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').show()");
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
//        } catch (Exception e) {
//        }
//        return navegacion;
//    }
//
//    public String editar(Usuario usuario, EvaluacionTribunal evaluacionTribunal, Proyecto proyecto) {
//        String navegacion = "";
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        try {
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//            }
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_evaluacion_tribunal");
//            if (tienePermiso == 1) {
//                sessionEvaluacionTribunal.setEvaluacionTribunal(evaluacionTribunal);
//                renderedDlgEditar = true;
//                renderedCalificar(usuario, proyecto);
//                renderedEliminar(usuario);
//                if (evaluacionTribunal.getTribunalId().getProyectoId().getId() == proyecto.getId()) {
//                    if (evaluacionTribunal.getEsAptoCalificar()) {
//                        administrarCalificacionMiembro.buscarConsulta(evaluacionTribunal);
//                    }
//                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').show()");
//                } else {
//                    RequestContext.getCurrentInstance().execute("PF('dlgViewInfoTribunal').show()");
//                }
//
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }

        } catch (Exception e) {
        }
        return navegacion;
    }

    public void calculaNota(Tribunal tribunal, EvaluacionTribunal evaluacionTribunal) {
        try {
//            double nota = 0.0;
//            int numeroMiembros = 0;
//            List<Miembro> miembros = new ArrayList<>();
//            miembros = miembroFacadeLocal.buscarPorTribunal(tribunal.getId());
//            if (miembros != null) {
//                numeroMiembros = !miembros.isEmpty() ? miembros.size() : 0;
//            }
//            for (CalificacionMiembro cm : calificacionMiembroFacadeLocal.buscarPorEvaluacionTribunal(evaluacionTribunal.getId())) {
//                if (cm.getEsActivo()) {
//                    nota += cm.getNota();
//                }
//            }
//            nota = nota / numeroMiembros;
//            nota = Math.round(nota * 100);
//            nota = nota / 100;
//            evaluacionTribunal.setNota(nota);
        } catch (Exception e) {
        }
    }

    public boolean existeSustentacionPublica(Tribunal tribunal) {
        boolean var = false;
//        try {
//            for (EvaluacionTribunal evaluacionTribunal : evaluacionTribunalFacadeLocal.buscarPorTribunal(tribunal.getId())) {
//                if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo())) {
//                    var = true;
//                    break;
//                }
//            }
//        } catch (Exception e) {
//        }
        return var;
    }

//    public void actualizaEstadoAutores(EvaluacionTribunal evaluacionTribunal, EstadoAutor estadoAutor, EstadoEstudianteCarrera estadoEstudianteCarrera) {
//        try {
//            for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(evaluacionTribunal.getTribunalId().getProyectoId().getId())) {
//                EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
//                    autorProyecto.setEstadoAutorId(estadoAutor);
//                    autorProyectoFacadeLocal.edit(autorProyecto);
//                    estudianteCarrera.setEstadoId(estadoEstudianteCarrera);
//                    estudianteCarreraFacadeLocal.edit(estudianteCarrera);
//                }
//            }
//        } catch (Exception e) {
//        }
//    }

    public void grabar(EvaluacionTribunal evaluacionTribunal, Tribunal tribunal, Proyecto proyecto, Usuario usuario) {
        try {
//            CatalogoEvento catalogoEvento = catalogoEventoFacadeLocal.buscarPorCodigo(CatalogoEventoEnum.SUSTENTACION.getTipo());
//            Evento evento = null;
//            EstadoProyecto estadoProyecto = null;
////            EstadoEstudianteCarrera estadoEstudianteCarrera = null;
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            int posRangoNota = 0;
//            RangoNota rn = null;
//            if (rangoNota != null) {
//                posRangoNota = rangoNota.indexOf(":");
//                rn = rangoNotaFacadeLocal.find(Integer.parseInt(rangoNota.substring(0, posRangoNota)));
//            } else {
//                if (evaluacionTribunal.getRangoNotaId() != null) {
//                    rn = evaluacionTribunal.getRangoNotaId();
//                }
//            }
//            if (rn != null) {
//                evaluacionTribunal.setRangoNotaId(rn);
//                for (RangoEquivalencia rangoEquivalencia : rangoEquivalenciaFacadeLocal.buscarPorRangoNota(rn.getId())) {
//                    if (evaluacionTribunal.getNota() >= rangoEquivalencia.getNotaInicio() && evaluacionTribunal.getNota() <= rangoEquivalencia.getNotaFin()) {
//                        evaluacionTribunal.setRangoEquivalenciaId(rangoEquivalencia);
//                        break;
//                    }
//                }
//            }
//            if (administrarMiembrosTribunal.existeUnPresidente(tribunal)) {
//                if (existeMiembroOtraSustentacion(evaluacionTribunal, usuario, proyecto) == false) {
//                    if (tribunal.getProyectoId().getId() != null) {
//                        proyecto = proyectoFacadeLocal.find(tribunal.getProyectoId().getId());
//                    }
//                    if (evaluacionTribunal.getId() == null) {
//                        if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
//                            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_evaluacion_tribunal");
//                            if (tienePermiso == 1) {
//                                evaluacionTribunal.setTribunalId(tribunal);
//                                calculaNota(tribunal, evaluacionTribunal);
//                                evaluacionTribunal.setFechaPlazo(evaluacionTribunal.getFechaFin());
//                                evaluacionTribunalFacadeLocal.create(evaluacionTribunal);
//                                evento = new Evento(null, evaluacionTribunal.getCatalogoEvaluacionId().getNombre(), evaluacionTribunal.getFechaInicio(), evaluacionTribunal.getFechaFin(), evaluacionTribunal.getId() + "");
//                                eventoFacadeLocal.edit(evento);
//                                administrarCalificacionMiembro.grabarCalificacionesMiembro(tribunal, evaluacionTribunal);
//                                listadoSustentacionesPorUsuarioCarrera(usuario, tribunal.getProyectoId());
//                                if (param.equalsIgnoreCase("grabar-dlg")) {
//                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').hide()");
//                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                                    FacesContext.getCurrentInstance().addMessage(null, message);
//                                    sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
//                                } else {
//                                    if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                                        FacesContext.getCurrentInstance().addMessage(null, message);
//                                    }
//                                }
//                            } else {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        } else {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    } else {
//                        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_evaluacion_tribunal");
//                        int tienePermiso1 = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_privada");
//                        int tienePermiso2 = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_publica");
//                        EstadoAutor estadoAutor = null;
//                        if (tienePermiso == 1 || tienePermiso1 == 1 || tienePermiso2 == 1) {
//                            evaluacionTribunal.setTribunalId(tribunal);
//                            calculaNota(tribunal, evaluacionTribunal);
//                            evaluacionTribunalFacadeLocal.edit(evaluacionTribunal);
//                            evento = eventoFacadeLocal.buscarPorCategoriaEvento(catalogoEvento.getId(), evaluacionTribunal.getId() + "");
//                            if (evento == null) {
//                                evento = new Evento(null, evaluacionTribunal.getCatalogoEvaluacionId().getNombre(), evaluacionTribunal.getFechaInicio(), evaluacionTribunal.getFechaFin(), evaluacionTribunal.getId() + "");
//                                eventoFacadeLocal.edit(evento);
//                            } else {
//                                evento.setTitulo(evaluacionTribunal.getCatalogoEvaluacionId().getNombre());
//                                evento.setFechaInicio(evaluacionTribunal.getFechaInicio());
//                                evento.setFechaFin(evaluacionTribunal.getFechaFin());
//                                eventoFacadeLocal.edit(evento);
//                            }
//                            administrarCalificacionMiembro.grabarCalificacionesMiembro(tribunal, evaluacionTribunal);
//                            if (evaluacionTribunal.getEsAptoCalificar()) {
//                                /*Privada*/
//                                if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo())) {
//                                    if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())) {
//                                        if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
//                                            /*APROBADA*/
//                                            estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo());
//                                            estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.SUSTENTACIONPUBLICA.getTipo());
////                                            estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.buscarPorCodigo(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
//                                        } else {
//                                            if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
//                                                /*RECUPERACIÓN*/
//                                                estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo());
//                                                estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.RECUPERACIONPUBLICA.getTipo());
////                                                estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.buscarPorCodigo(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
//                                            } else {
//                                                /*REPROBADO*/
//                                                estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
//                                                estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.REPROBADO.getTipo());
////                                                estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
//                                            }
//                                        }
//                                    } else {
//                                        /*RECUPERACIÓN PRIVADA*/
//                                        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())) {
//                                            if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
//                                                /*APROBADA*/
//                                                estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.APROBADO.getTipo());
//                                                estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.APROBADO.getTipo());
////                                                estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(3);
//                                            } else {
//                                                if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
//                                                    /*RECUPERACIÓN PRIVADA*/
//                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo());
//                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.RECUPERACIONPRIVADA.getTipo());
////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(2);
//                                                } else {
//                                                    /*REPROBADO*/
//                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
//                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(2);
//                                                }
//                                            }
//                                        }
//                                    }
//                                } else {
//                                    /*PÚBLICA*/
//                                    if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo())) {
//                                        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())) {
//                                            if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
//                                                estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.APROBADO.getTipo());
//                                                estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.APROBADO.getTipo());
////                                                estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(3);
//                                            } else {
//                                                if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
//                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo());
//                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.RECUPERACIONPUBLICA.getTipo());
////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(2);
//                                                } else {
//                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
//                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.REPROBADO.getTipo());
////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.buscarPorCodigo(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
//                                                }
//                                            }
//                                        } else {
//                                            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                                                if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
//                                                    estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.APROBADO.getTipo());
//                                                    estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.APROBADO.getTipo());
////                                                    estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(EstadoEstudianteCarreraEnum.TITULADO.getTipo());
//                                                } else {
//                                                    if (permitirCrearOtraSustentacion(evaluacionTribunal)) {
//                                                        estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo());
//                                                        estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.RECUPERACIONPUBLICA.getTipo());
////                                                        estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
//                                                    } else {
//                                                        estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
//                                                        estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.REPROBADO.getTipo());
////                                                        estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                                tribunal.getProyectoId().setEstadoProyectoId(estadoProyecto);
//                                proyectoFacadeLocal.edit(tribunal.getProyectoId());
////                                actualizaEstadoAutores(evaluacionTribunal, estadoAutor, estadoEstudianteCarrera);
//                            }
//                            listadoSustentacionesPorUsuarioCarrera(usuario, tribunal.getProyectoId());
//                            if (param != null) {
//                                if (param.equalsIgnoreCase("grabar-dlg")) {
//                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEvaluacionTribunal').hide()");
//                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                                    FacesContext.getCurrentInstance().addMessage(null, message);
//                                    sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
//                                } else {
//                                    if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                                        FacesContext.getCurrentInstance().addMessage(null, message);
//                                    }
//                                }
//                            } else {
//                                sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal());
//                            }
//                        } else {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    }
//
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.miembro") + " " + bundle.getString("lbl.miembro_encontrado"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + "" + bundle.getString("lbl.presidente") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void aceptarAcentarNota(EvaluacionTribunal evaluacionTribunal) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (evaluacionTribunal.getId() != null) {
//                RangoNota rn = null;
//                if (evaluacionTribunal.getRangoNotaId() != null) {
//                    rn = evaluacionTribunal.getRangoNotaId();
//                }
//                if (rn != null) {
//                    evaluacionTribunal.setRangoNotaId(rn);
//                    for (RangoEquivalencia rangoEquivalencia : rangoEquivalenciaFacadeLocal.buscarPorRangoNota(rn.getId())) {
//                        if (evaluacionTribunal.getNota() >= rangoEquivalencia.getNotaInicio() && evaluacionTribunal.getNota() <= rangoEquivalencia.getNotaFin()) {
//                            evaluacionTribunal.setRangoEquivalenciaId(rangoEquivalencia);
//                            break;
//                        }
//                    }
//                }
//                evaluacionTribunalFacadeLocal.edit(evaluacionTribunal);
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public void buscar(Tribunal tribunal, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            this.evaluacionTribunals = new ArrayList<>();
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_evaluacion_tribunal");
            if (tienePermiso == 1) {
                for (EvaluacionTribunal ev : evaluacionTribunalFacadeLocal.buscarPorTribunal(tribunal.getId())) {
                    evaluacionTribunals.add(ev);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void remover(EvaluacionTribunal evaluacionTribunal, Usuario usuario, Tribunal tribunal, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_evaluacion_tribunal");
//                if (tienePermiso == 1) {
//                    evaluacionTribunalFacadeLocal.remove(evaluacionTribunal);
//                    buscar(tribunal, usuario);
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

    public void fijarCatalogoEvaluación(EvaluacionTribunal evaluacionTribunal, Proyecto proyecto) {
//        try {
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())) {
//                evaluacionTribunal.setCatalogoEvaluacionId(catalogoEvaluacionFacadeLocal.buscarPorCodigo(CatalogoEvaluacionEnum.SUSTENTACIONPRIVADA.getTipo()));
//            } else {
//                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                        || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                    evaluacionTribunal.setCatalogoEvaluacionId(catalogoEvaluacionFacadeLocal.buscarPorCodigo(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo()));
//                }
//            }
//        } catch (Exception e) {
//        }
    }

    public boolean existeMiembroOtraSustentacion(EvaluacionTribunal evaluacionTribunal, Usuario usuario, Proyecto proyecto) {
        boolean var = false;
        try {
            SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MMM-dd");
            SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ssZ");
            Date fechaInicioEvaluacion = null;
            Date fechaFinEvaluacion = null;

            Date fechaInicioOtraEvaluacion = null;
            Date fechaFinOtraEvaluacion = null;

            Date horaInicioEvaluacion = null;
            Date horaFinEvaluacion = null;
            Date horaInicioOtraEvaluacion = null;
            Date horaFinOtraEvaluacion = null;

            fechaInicioEvaluacion = (formatFecha.parse(formatFecha.format(evaluacionTribunal.getFechaInicio())));
            fechaFinEvaluacion = (formatFecha.parse(formatFecha.format(evaluacionTribunal.getFechaFin())));
            horaInicioEvaluacion = (formatHora.parse(formatHora.format(evaluacionTribunal.getFechaInicio())));
            horaFinEvaluacion = (formatHora.parse(formatHora.format(evaluacionTribunal.getFechaFin())));

            boolean encontrado = false;
            listadoSustentacionesPorUsuarioCarrera(usuario, proyecto);
            for (EvaluacionTribunal ev : evaluacionTribunalesByCarrera) {
                if (!ev.equals(evaluacionTribunal)) {
                    fechaInicioOtraEvaluacion = (formatFecha.parse(formatFecha.format(ev.getFechaInicio())));
                    fechaFinOtraEvaluacion = (formatFecha.parse(formatFecha.format(ev.getFechaFin())));
                    horaInicioOtraEvaluacion = (formatHora.parse(formatHora.format(ev.getFechaInicio())));
                    horaFinOtraEvaluacion = (formatHora.parse(formatHora.format(ev.getFechaFin())));
                    if (encontrado == false) {
                        if ((fechaInicioEvaluacion.equals(fechaInicioOtraEvaluacion) || fechaInicioEvaluacion.equals(fechaFinOtraEvaluacion)
                                || (fechaInicioEvaluacion.after(fechaInicioOtraEvaluacion) && fechaInicioEvaluacion.before(fechaFinOtraEvaluacion)))
                                || (fechaFinEvaluacion.equals(fechaInicioOtraEvaluacion) || fechaFinEvaluacion.equals(fechaFinOtraEvaluacion)
                                || (fechaFinEvaluacion.after(fechaInicioOtraEvaluacion) && fechaFinEvaluacion.before(fechaFinOtraEvaluacion)))) {
                            if ((horaInicioEvaluacion.equals(horaInicioOtraEvaluacion) || horaInicioEvaluacion.equals(horaFinOtraEvaluacion)
                                    || (horaInicioEvaluacion.after(horaInicioOtraEvaluacion) && horaInicioEvaluacion.before(horaFinOtraEvaluacion)))
                                    || (horaFinEvaluacion.equals(horaInicioOtraEvaluacion) || horaFinEvaluacion.equals(horaFinOtraEvaluacion)
                                    || (horaFinEvaluacion.after(horaInicioOtraEvaluacion) && horaFinEvaluacion.before(horaFinOtraEvaluacion)))) {
                                for (MiembroTribunal m : miembroFacadeLocal.buscarPorTribunal(ev.getTribunalId().getId())) {
                                    if (encontrado == false) {
                                        for (MiembroTribunal mi : miembroFacadeLocal.buscarPorTribunal(evaluacionTribunal.getTribunalId().getId())) {
                                            if (m.getDocenteId().equals(mi.getDocenteId())) {
                                                Docente docente = docenteFacadeLocal.find(m.getDocenteId());
                                                docenteEncontrado = docente;
                                                encontrado = true;
                                                var = true;
                                                break;
                                            }
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return var;
    }

    public List<RangoNota> listarRangos() {
        try {
            return rangoNotaFacadeLocal.findAll();
        } catch (Exception e) {
        }
        return null;
    }

    public void listadoSustentacionesPorUsuarioCarrera(Usuario usuario, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            this.evaluacionTribunalesByCarrera = new ArrayList<>();
//            eventModel = new DefaultScheduleModel();
//            for (EvaluacionTribunal ev : evaluacionTribunalFacadeLocal.buscarPorUsuarioCarrera(usuario.getId())) {
//                if (!evaluacionTribunalesByCarrera.contains(ev)) {
//                    DefaultScheduleEvent evento = new DefaultScheduleEvent();
//                    evento.setData(ev);
//                    evento.setStartDate(ev.getFechaInicio());
//                    evento.setEndDate(ev.getFechaFin());
//                    if (ev.getTribunalId().getProyectoId().getId() == proyecto.getId()) {
//                        evento.setTitle(ev.getCatalogoEvaluacionId().getNombre());
//                        evento.setStyleClass("propio");
//                    } else {
//                        evento.setTitle(ev.getCatalogoEvaluacionId().getNombre() + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.proyecto") + " " + bundle.getString("lbl.tema_proyecto") + ": " + proyecto.getTemaActual());
//                        evento.setStyleClass("otro");
//                    }
//                    eventModel.addEvent(evento);
//                    evaluacionTribunalesByCarrera.add(ev);
//                }
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void consultaSustentaciones(Usuario usuario) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            List<EvaluacionTribunal> ets = new ArrayList<>();
//            eventModelConsulta = new DefaultScheduleModel();
//            for (EvaluacionTribunal ev : evaluacionTribunalFacadeLocal.buscarPorUsuarioCarrera(usuario.getId())) {
//                if (!ets.contains(ev)) {
//                    DefaultScheduleEvent evento = new DefaultScheduleEvent();
//                    evento.setData(ev);
//                    evento.setStartDate(ev.getFechaInicio());
//                    evento.setEndDate(ev.getFechaFin());
//                    evento.setTitle(ev.getCatalogoEvaluacionId().getNombre() + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.proyecto") + ": " + ev.getTribunalId().getProyectoId().getTemaActual());
//                    if (ev.getCatalogoEvaluacionId().getId() == 1) {
//                        evento.setStyleClass("privada");
//                    } else {
//                        evento.setStyleClass("publica");
//                    }
//                    eventModelConsulta.addEvent(evento);
//                    ets.add(ev);
//                }
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        try {
            ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
//            editar(sessionUsuario.getUsuario(), (EvaluacionTribunal) event.getData(), sessionProyecto.getProyecto());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onEventSelectConsulta(SelectEvent selectEvent) {
        try {
            ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
            consultaEvaluacionTribunal = (EvaluacionTribunal) event.getData();
            administrarCalificacionMiembro.buscarConsulta(consultaEvaluacionTribunal);
            RequestContext.getCurrentInstance().execute("PF('dlgViewInfoTribunal').show()");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onEventCrear(SelectEvent selectEvent) {
        try {
            ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
            crear(sessionUsuario.getUsuario(), event.getStartDate(), sessionProyecto.getProyectoSeleccionado());
        } catch (Exception e) {
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
            if (ev.getEsAptoCalificar() == false) {
                if (existeMiembroOtraSustentacion(ev, sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado()) == false) {
                    grabar(ev, ev.getTribunalId(), sessionProyecto.getProyectoSeleccionado(), sessionUsuario.getUsuario());
                } else {
                    listadoSustentacionesPorUsuarioCarrera(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, docenteEncontrado.toString() + " " + bundle.getString("lbl.miembro_encontrado"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                listadoSustentacionesPorUsuarioCarrera(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            System.out.println(e);
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
        if (ev.getEsAptoCalificar() == false) {
            if (existeMiembroOtraSustentacion(ev, sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado()) == false) {
                grabar(ev, ev.getTribunalId(), sessionProyecto.getProyectoSeleccionado(), sessionUsuario.getUsuario());
            } else {
                listadoSustentacionesPorUsuarioCarrera(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, docenteEncontrado.toString() + " " + bundle.getString("lbl.miembro_encontrado"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            listadoSustentacionesPorUsuarioCarrera(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public String viewConsultaTribunales(Usuario usuario) {
        String navegacion = "";
        try {
            consultaSustentaciones(usuario);
            navegacion = "pretty:consultaSustentaciones";
        } catch (Exception e) {
        }
        return navegacion;
    }

    public boolean permitirCrearOtraSustentacion(EvaluacionTribunal evaluacionTribunal) {
        boolean var = false;
//        int numero = Integer.parseInt(configuracionGeneralFacadeLocal.find(45).getValor());
//        int contador = 0;
        try {
//            for (EvaluacionTribunal eva : evaluacionTribunalFacadeLocal.buscarPorTribunal(evaluacionTribunal.getTribunalId().getId())) {
//                if (eva.getCatalogoEvaluacionId().getId() == evaluacionTribunal.getCatalogoEvaluacionId().getId()) {
//                    contador++;
//                }
//            }
//            if (contador < numero) {
//                var = true;
//            } else {
//                var = false;
//            }
        } catch (Exception e) {
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public boolean renderedImprimirActa(EvaluacionTribunal evaluacionTribunal, Usuario usuario) {
        boolean var = false;
//        int tienePermiso = 0;
//        try {
//            if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPRIVADA.getTipo())) {
//                tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_privada");
//                if (tienePermiso == 1) {
//                    var = true;
//                } else {
//                    var = false;
//                }
//            } else {
//                if (evaluacionTribunal.getCatalogoEvaluacionId().getCodigo().equalsIgnoreCase(CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo())) {
//                    tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_publica");
//                    if (tienePermiso == 1) {
//                        var = true;
//                    } else {
//                        var = false;
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
        return var;
    }

    public void renderedCalificar(Usuario usuario, Proyecto proyecto) {
        try {
//            int tienePermiso = 0;
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())) {
//                tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_privada");
//                if (tienePermiso == 1) {
//                    renderedCalificarPublica = false;
//                    renderedCalificarPrivada = true;
//                } else {
//                    renderedCalificarPrivada = false;
//                    renderedCalificarPublica = false;
//                }
//            } else {
//                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                        || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                    tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "calificar_evaluacion_tribunal_publica");
//                    if (tienePermiso == 1) {
//                        renderedCalificarPublica = true;
//                        renderedCalificarPrivada = false;
//                    } else {
//                        renderedCalificarPrivada = false;
//                        renderedCalificarPublica = false;
//                    }
//                } else {
//                    renderedCalificarPrivada = false;
//                    renderedCalificarPublica = false;
//                }
//            }
        } catch (Exception e) {
        }
    }

    public void renderedCrear(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_evaluacion_tribunal");
        if (tienePermiso == 1) {
            renderedCrear = true;
        } else {
            renderedCrear = false;
        }
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_evaluacion_tribunal");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_evaluacion_tribunal");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
            renderedEditar = false;
        }
    }

    public void renderedEliminar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_evaluacion_tribunal");
        if (tienePermiso == 1) {
            renderedEliminar = true;
        } else {
            renderedEliminar = false;
        }
    }

    public void renderedAceptarNota(Docente docente) {
        try {
//            MiembroTribunal miembro = null;
//            List<MiembroTribunal> miembros = new ArrayList<>();
//            miembros = miembroFacadeLocal.buscarPorDocente(docente.getId());
//            if (miembros != null) {
//                miembro = !miembros.isEmpty() ? miembros.get(0) : null;
//                if (miembro != null) {
//                    if (miembro.getCargoId().getId() == 1) {
//                        renderedAceptarNota = true;
//                    } else {
//                        renderedAceptarNota = false;
//                    }
//                } else {
//                    renderedAceptarNota = false;
//                }
//            } else {
//                renderedAceptarNota = false;
//            }
        } catch (Exception e) {
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public EvaluacionTribunal getConsultaEvaluacionTribunal() {
        return consultaEvaluacionTribunal;
    }

    public void setConsultaEvaluacionTribunal(EvaluacionTribunal consultaEvaluacionTribunal) {
        this.consultaEvaluacionTribunal = consultaEvaluacionTribunal;
    }

    public ScheduleModel getEventModelConsulta() {
        return eventModelConsulta;
    }

    public void setEventModelConsulta(ScheduleModel eventModelConsulta) {
        this.eventModelConsulta = eventModelConsulta;
    }

    public boolean isRenderedAceptarNota() {
        return renderedAceptarNota;
    }

    public void setRenderedAceptarNota(boolean renderedAceptarNota) {
        this.renderedAceptarNota = renderedAceptarNota;
    }

    public boolean isRenderedCalificarPrivada() {
        return renderedCalificarPrivada;
    }

    public void setRenderedCalificarPrivada(boolean renderedCalificarPrivada) {
        this.renderedCalificarPrivada = renderedCalificarPrivada;
    }

    public Docente getDocenteEncontrado() {
        return docenteEncontrado;
    }

    public void setDocenteEncontrado(Docente docenteEncontrado) {
        this.docenteEncontrado = docenteEncontrado;
    }

    public List<EvaluacionTribunal> getEvaluacionTribunalesByCarrera() {
        return evaluacionTribunalesByCarrera;
    }

    public void setEvaluacionTribunalesByCarrera(List<EvaluacionTribunal> evaluacionTribunalesByCarrera) {
        this.evaluacionTribunalesByCarrera = evaluacionTribunalesByCarrera;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public boolean isRenderedCalificarPublica() {
        return renderedCalificarPublica;
    }

    public void setRenderedCalificarPublica(boolean renderedCalificarPublica) {
        this.renderedCalificarPublica = renderedCalificarPublica;
    }

    public SessionEvaluacionTribunal getSessionEvaluacionTribunal() {
        return sessionEvaluacionTribunal;
    }

    public void setSessionEvaluacionTribunal(SessionEvaluacionTribunal sessionEvaluacionTribunal) {
        this.sessionEvaluacionTribunal = sessionEvaluacionTribunal;
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

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }

    public List<EvaluacionTribunal> getEvaluacionTribunals() {
        return evaluacionTribunals;
    }

    public void setEvaluacionTribunals(List<EvaluacionTribunal> evaluacionTribunals) {
        this.evaluacionTribunals = evaluacionTribunals;
    }

    public String getRangoNota() {
        return rangoNota;
    }

    public void setRangoNota(String rangoNota) {
        this.rangoNota = rangoNota;
    }

    public void setSessionTribunal(Tribunal tribunal) {
        try {
            sessionEvaluacionTribunal.getEvaluacionTribunal().setTribunalId(tribunal);
        } catch (Exception e) {
        }
    }

    //</editor-fold>
}
