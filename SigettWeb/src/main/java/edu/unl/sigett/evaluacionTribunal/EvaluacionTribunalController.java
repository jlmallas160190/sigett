/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.evaluacionTribunal;

import com.jlmallas.comun.entity.Evento;
import com.jlmallas.comun.entity.EventoPersona;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.EquivalenciaEnum;
import com.jlmallas.comun.enumeration.EventoEnum;
import com.jlmallas.comun.service.EventoPersonaService;
import com.jlmallas.comun.service.EventoService;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.enumeration.EstadoEstudianteCarreraEnum;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.entity.RangoEquivalencia;
import edu.unl.sigett.entity.RangoNota;
import edu.unl.sigett.enumeration.CargoMiembroEnum;
import edu.unl.sigett.enumeration.CatalogoEvaluacionEnum;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.miembroTribunal.MiembroTribunalDTO;
import edu.unl.sigett.miembroTribunal.SessionMiembroTribunal;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.CalificacionMiembroService;
import edu.unl.sigett.service.EvaluacionTribunalService;
import edu.unl.sigett.service.MiembroTribunalService;
import edu.unl.sigett.service.ProyectoService;
import edu.unl.sigett.service.RangoEquivalenciaService;
import edu.unl.sigett.service.RangoNotaService;
import edu.unl.sigett.tribunal.SessionTribunal;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.secure.Secure;
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
    @Inject
    private SessionMiembroTribunal sessionMiembroTribunal;
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
    @EJB(lookup = "java:global/ComunService/EventoServiceImplement!com.jlmallas.comun.service.EventoService")
    private EventoService eventoService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/SigettService/RangoNotaServiceImplement!edu.unl.sigett.service.RangoNotaService")
    private RangoNotaService rangoNotaService;
    @EJB(lookup = "java:global/SigettService/RangoEquivalenciaServiceImplement!edu.unl.sigett.service.RangoEquivalenciaService")
    private RangoEquivalenciaService rangoEquivalenciaService;
    @EJB(lookup = "java:global/SigettService/MiembroTribunalServiceImplement!edu.unl.sigett.service.MiembroTribunalService")
    private MiembroTribunalService miembroTribunalService;
    @EJB(lookup = "java:global/SigettService/ProyectoServiceImplement!edu.unl.sigett.service.ProyectoService")
    private ProyectoService proyectoService;
    @EJB(lookup = "java:global/AcademicoService/EstudianteCarreraServiceImplement!edu.jlmallas.academico.service.EstudianteCarreraService")
    private EstudianteCarreraService estudianteCarreraService;
    @EJB(lookup = "java:global/SigettService/AutorProyectoServiceImplement!edu.unl.sigett.service.AutorProyectoService")
    private AutorProyectoService autorProyectoService;
    @EJB(lookup = "java:global/SigettService/CalificacionMiembroServiceImplement!edu.unl.sigett.service.CalificacionMiembroService")
    private CalificacionMiembroService calificacionMiembroService;
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
        schudele();

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
                    PropertiesFileEnum.PERMISOS, "crear_evaluacion_tribunal").trim());
            if (tienePermiso == 1) {
                sessionEvaluacionTribunal.setEvaluacionTribunal(new EvaluacionTribunal(null, BigDecimal.ZERO, "Ninguno", "Ninguno", "Ninguno",
                        sessionEvaluacionTribunal.getFechaInicioSeleccionada(), sessionEvaluacionTribunal.getFechaFinSeleccionada(),
                        Boolean.FALSE, sessionEvaluacionTribunal.getFechaFinSeleccionada(), sessionTribunal.getTribunal(), null, null, Boolean.TRUE));
                fijarCatalogoEvaluacion(sessionEvaluacionTribunal.getEvaluacionTribunal());
                sessionEvaluacionTribunal.getEvaluacionTribunal().setTribunalId(sessionTribunal.getTribunal());
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

    /**
     * PERMITE BUSCAR LOS MIEMBROS DE TRIBUNAL DE UN TRIBUNAL QUE NO PERTENECE
     * AL PROYECTO SELECCIONADO
     */
    private void miembrosTribunal() {
        sessionEvaluacionTribunal.getMiembrosTribunalDTO().clear();
        if (!sessionEvaluacionTribunal.getEvaluacionTribunal().getTribunalId().equals(sessionTribunal.getTribunal())) {
            MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
            miembroTribunalBuscar.setTribunalId(sessionEvaluacionTribunal.getEvaluacionTribunal().getTribunalId());
            List<MiembroTribunal> miembroTribunals = miembroTribunalService.buscar(miembroTribunalBuscar);
            for (MiembroTribunal miembroTribunal : miembroTribunals) {
                MiembroTribunalDTO miembroTribunalDTO = new MiembroTribunalDTO(miembroTribunal, null,
                        personaService.buscarPorId(new Persona(miembroTribunal.getDocenteId())));
                sessionEvaluacionTribunal.getMiembrosTribunalDTO().add(miembroTribunalDTO);
            }
        }
    }

    public void editar(EvaluacionTribunal evaluacionTribunal) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "crear_evaluacion_tribunal").trim());
            if (tienePermiso == 1) {
                listadoRangos();
                sessionEvaluacionTribunal.setEvaluacionTribunal(evaluacionTribunal);
                miembrosTribunal();
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
            List<CalificacionMiembro> calificacionMiembros = calificacionMiembroService.buscar(calificacionMiembroBuscar);
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

    /**
     * CREAR EVENTO PARA LOS MIEMBROS DE TRIBUANAL
     */
    private void grabarEventosDirector() {
        Item tipoEvento = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOEVENTO.getTipo(), EventoEnum.MIEMBROTRIBUNAL.getTipo());
        for (MiembroTribunalDTO miembroTribunalDTO : sessionMiembroTribunal.getMiembrosTribunalDTO()) {
            if (!miembroTribunalDTO.getMiembroTribunal().getEsActivo()) {
                continue;
            }
            Evento eventoBuscar = new Evento();
            eventoBuscar.setTablaId(sessionEvaluacionTribunal.getEvaluacionTribunal().getId());
            Calendar fechaCulminacion = Calendar.getInstance();
            fechaCulminacion.setTime(sessionEvaluacionTribunal.getEvaluacionTribunal().getFechaPlazo());
            fechaCulminacion.add(Calendar.HOUR_OF_DAY, 1);
            List<Evento> eventos = eventoService.buscar(eventoBuscar);
            Evento evento = !eventos.isEmpty() ? eventos.get(0) : null;
            if (evento == null) {
                evento = new Evento(null, sessionEvaluacionTribunal.getEvaluacionTribunal().getCatalogoEvaluacion() + ": "
                        + sessionProyecto.getProyectoSeleccionado().getTemaActual(), sessionEvaluacionTribunal.getEvaluacionTribunal().getLugar(), sessionEvaluacionTribunal.getEvaluacionTribunal().getFechaInicio(),
                        fechaCulminacion.getTime(), tipoEvento.getId(), sessionEvaluacionTribunal.getEvaluacionTribunal().getId());
                eventoService.guardar(evento);
                EventoPersona eventoMiembro = new EventoPersona(null, miembroTribunalDTO.getPersona(), evento);
                eventoPersonaService.guardar(eventoMiembro);
                continue;
            }
            evento.setLugar(sessionEvaluacionTribunal.getEvaluacionTribunal().getLugar());
            evento.setFechaInicio(sessionEvaluacionTribunal.getEvaluacionTribunal().getFechaInicio());
            evento.setFechaFin(sessionEvaluacionTribunal.getEvaluacionTribunal().getFechaPlazo());
            evento.setNombre(sessionEvaluacionTribunal.getEvaluacionTribunal().getCatalogoEvaluacion() + ": "
                    + sessionProyecto.getProyectoSeleccionado().getTemaActual());
            eventoService.actualizar(evento);
            EventoPersona eventoPersonaBuscar = new EventoPersona();
            eventoPersonaBuscar.setEvento(evento);
            eventoPersonaBuscar.setPersonaId(miembroTribunalDTO.getPersona());
            List<EventoPersona> eventoPersonas = eventoPersonaService.buscar(eventoPersonaBuscar);
            EventoPersona eventoPersona = !eventoPersonas.isEmpty() ? eventoPersonas.get(0) : null;
            if (eventoPersona == null) {
                eventoPersona = new EventoPersona(null, miembroTribunalDTO.getPersona(), evento);
                eventoPersonaService.guardar(eventoPersona);
            }
        }
    }

    private void actualizarEstadoProyecto() {
        if (!sessionEvaluacionTribunal.getEvaluacionTribunal().getEsAptoCalificar()) {
            return;
        }
        if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())) {
            Item sustententacionPublica = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo());
            sessionProyecto.getProyectoSeleccionado().setEstadoProyectoId(sustententacionPublica.getId());
            sessionProyecto.setEstadoActual(sustententacionPublica);
            if (sessionEvaluacionTribunal.getEquivalencia().getCodigo().equals(EquivalenciaEnum.REPROBADO.getTipo())) {
                Item recuperacion = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo());
                sessionProyecto.getProyectoSeleccionado().setEstadoProyectoId(recuperacion.getId());
                sessionProyecto.setEstadoActual(recuperacion);
            }
            return;
        }
        if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())) {
            Item sustententacionPublica = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo());
            sessionProyecto.getProyectoSeleccionado().setEstadoProyectoId(sustententacionPublica.getId());
            sessionProyecto.setEstadoActual(sustententacionPublica);
            if (sessionEvaluacionTribunal.getEquivalencia().getCodigo().equals(EquivalenciaEnum.REPROBADO.getTipo())) {
                Item reprobado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.REPROBADO.getTipo());
                sessionProyecto.getProyectoSeleccionado().setEstadoProyectoId(reprobado.getId());
                sessionProyecto.setEstadoActual(reprobado);
            }
            return;
        }
        if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())) {
            Item aprobado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.APROBADO.getTipo());
            sessionProyecto.getProyectoSeleccionado().setEstadoProyectoId(aprobado.getId());
            sessionProyecto.setEstadoActual(aprobado);
            if (sessionEvaluacionTribunal.getEquivalencia().getCodigo().equals(EquivalenciaEnum.REPROBADO.getTipo())) {
                Item recuperacion = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo());
                sessionProyecto.getProyectoSeleccionado().setEstadoProyectoId(recuperacion.getId());
                sessionProyecto.setEstadoActual(recuperacion);
            }
            return;
        }
        if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
            Item aprobado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.APROBADO.getTipo());
            sessionProyecto.getProyectoSeleccionado().setEstadoProyectoId(aprobado.getId());
            sessionProyecto.setEstadoActual(aprobado);
            if (sessionEvaluacionTribunal.getEquivalencia().getCodigo().equals(EquivalenciaEnum.REPROBADO.getTipo())) {
                Item reprobado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.REPROBADO.getTipo());
                sessionProyecto.getProyectoSeleccionado().setEstadoProyectoId(reprobado.getId());
                sessionProyecto.setEstadoActual(reprobado);
            }
        }
    }

    private void actualizarEstadoAutores() {
        if (!sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.APROBADO.getTipo())) {
            return;
        }
        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo());
        Item estadoFinalizado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.FINALIZADO.getTipo());
        Item titulado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOESTUDIANTECARRERA.getTipo(), EstadoEstudianteCarreraEnum.TITULADO.getTipo());
        for (AutorProyecto autorProyecto : sessionProyecto.getProyectoSeleccionado().getAutorProyectoList()) {
            if (autorProyecto.getEstadoAutorId().equals(estadoRenunciado.getId())) {
                continue;
            }
            EstudianteCarrera estudianteCarreraBuscar = new EstudianteCarrera();
            estudianteCarreraBuscar.setId(autorProyecto.getAspiranteId().getId());
            EstudianteCarrera estudianteCarrera = estudianteCarreraService.buscarPorId(estudianteCarreraBuscar);
            estudianteCarrera.setEstadoId(titulado.getId());
            autorProyecto.setEstadoAutorId(estadoFinalizado.getId());
            estudianteCarreraService.actualizar(estudianteCarrera);
            autorProyectoService.editar(autorProyecto);
        }
    }

    private void fijarEquivalencia() {
        RangoEquivalencia rangoEquivalenciaBuscar = new RangoEquivalencia();
        rangoEquivalenciaBuscar.setRangoNotaId(sessionEvaluacionTribunal.getEvaluacionTribunal().getRangoNotaId());
        List<RangoEquivalencia> rangoEquivalencias = rangoEquivalenciaService.buscar(rangoEquivalenciaBuscar);
        for (RangoEquivalencia rangoEquivalencia : rangoEquivalencias) {
            if (sessionEvaluacionTribunal.getEvaluacionTribunal().getNota().floatValue() >= rangoEquivalencia.getNotaInicio().floatValue()
                    && sessionEvaluacionTribunal.getEvaluacionTribunal().getNota().floatValue() <= rangoEquivalencia.getNotaFin().floatValue()) {
                sessionEvaluacionTribunal.getEvaluacionTribunal().setRangoEquivalenciaId(rangoEquivalencia);
                break;
            }
        }
    }

    public void grabar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo-grabado");
        try {
            calculaNota(sessionEvaluacionTribunal.getEvaluacionTribunal());
            fijarEquivalencia();
            Item equivalencia = itemService.buscarPorId(sessionEvaluacionTribunal.getEvaluacionTribunal().getRangoEquivalenciaId().getEquivalenciaId());
            sessionEvaluacionTribunal.setEquivalencia(equivalencia);
            if (!existeUnPresidente()) {
                return;
            }
            if (sessionEvaluacionTribunal.getEvaluacionTribunal().getId() == null) {
                evaluacionTribunalService.guardar(sessionEvaluacionTribunal.getEvaluacionTribunal());
                grabarEventosDirector();
                actualizarEstadoProyecto();
                actualizarEstadoAutores();
                grabarCalificacionesMiembrosTribunal();
                proyectoService.actualizar(sessionProyecto.getProyectoSeleccionado());
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("grabar"), "");
                if (param.equalsIgnoreCase("grabar")) {
                    cancelarEdicion();
                }
                return;
            }
            evaluacionTribunalService.actualizar(sessionEvaluacionTribunal.getEvaluacionTribunal());
            grabarEventosDirector();
            actualizarEstadoProyecto();
            actualizarEstadoAutores();
            grabarCalificacionesMiembrosTribunal();
            proyectoService.actualizar(sessionProyecto.getProyectoSeleccionado());
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("editar"), "");
            if (param.equalsIgnoreCase("grabar")) {
                cancelarEdicion();
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    /**
     * GRABAR CALIFICACIONES DE CADA MIEMBRO DEL TRIBUNAL
     */
    private void grabarCalificacionesMiembrosTribunal() {
        for (MiembroTribunalDTO miembroTribunalDTO : sessionMiembroTribunal.getMiembrosTribunalDTO()) {
            CalificacionMiembro calificacionMiembroBuscar = new CalificacionMiembro();
            calificacionMiembroBuscar.setMiembroId(cabeceraController.getSecureService().encrypt(
                    new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), miembroTribunalDTO.getMiembroTribunal().getId() + "")));
            CalificacionMiembro calificacionMiembro = calificacionMiembroService.buscarPorMiembro(calificacionMiembroBuscar);
            if (calificacionMiembro == null) {
                calificacionMiembro = new CalificacionMiembro(null, BigDecimal.ZERO, "Ninguno", cabeceraController.getSecureService().encrypt(
                        new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), miembroTribunalDTO.getMiembroTribunal().getId() + "")), Boolean.TRUE, sessionEvaluacionTribunal.getEvaluacionTribunal());
                calificacionMiembroService.guardar(calificacionMiembro);
            }
        }
    }

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

    private void buscar() {
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
                    PropertiesFileEnum.PERMISOS, "eliminar_evaluacion_tribunal").trim());
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
                evaluacionTribunal.setCatalogoEvaluacion(item.getNombre());
                return;
            }
            if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOEVALUACIONTRIBUNAL.getTipo(), CatalogoEvaluacionEnum.SUSTENTACIONPUBLICA.getTipo());
                evaluacionTribunal.setCatalogoEvaluacionId(item.getId());
                evaluacionTribunal.setCatalogoEvaluacion(item.getNombre());
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
    private void schudele() {
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
                        sessionEvaluacionTribunal.getEventModel().addEvent(evento);
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

    public Boolean existeUnPresidente() {
        try {
            Item presidente = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CARGOMIEMBROTRIBUNAL.getTipo(), CargoMiembroEnum.PRESIDENTE.getTipo());
            MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
            miembroTribunalBuscar.setTribunalId(sessionTribunal.getTribunal());
            List<MiembroTribunal> miembroTribunales = miembroTribunalService.buscar(miembroTribunalBuscar);
            for (MiembroTribunal miembro : miembroTribunales) {
                if (miembro.getCargoId().equals(presidente.getId())) {
                    return Boolean.TRUE;
                }
            }
        } catch (Exception e) {
        }
        return Boolean.FALSE;
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
                    PropertiesFileEnum.PERMISOS, "crear_evaluacion_tribunal").trim());
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
                    PropertiesFileEnum.PERMISOS, "editar_evaluacion_tribunal").trim());
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
                    PropertiesFileEnum.PERMISOS, "eliminar_evaluacion_tribunal").trim());
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
