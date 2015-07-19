/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.miembroTribunal;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Evento;
import com.jlmallas.comun.entity.EventoPersona;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.EventoEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.EventoPersonaService;
import com.jlmallas.comun.service.EventoService;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.DocenteService;
import edu.unl.sigett.director.DirectorDTO;
import edu.unl.sigett.director.DirectorDTOConverter;
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.Director;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.enumeration.CargoMiembroEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.evaluacionTribunal.SessionEvaluacionTribunal;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.CalificacionMiembroService;
import edu.unl.sigett.service.DirectorService;
import edu.unl.sigett.service.MiembroTribunalService;
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
import org.jlmallas.secure.Secure;
import org.jlmallas.seguridad.service.UsuarioService;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "miembroTribunalController")
@SessionScoped
public class MiembroTribunalController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionMiembroTribunal sessionMiembroTribunal;
    @Inject
    private SessionTribunal sessionTribunal;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionEvaluacionTribunal sessionEvaluacionTribunal;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/SigettService/MiembroTribunalServiceImplement!edu.unl.sigett.service.MiembroTribunalService")
    private MiembroTribunalService miembroTribunalService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/AcademicoService/DocenteServiceImplement!edu.jlmallas.academico.service.DocenteService")
    private DocenteService docenteService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/ComunService/EventoPersonaServiceImplement!com.jlmallas.comun.service.EventoPersonaService")
    private EventoPersonaService eventoPersonaService;
    @EJB(lookup = "java:global/AcademicoService/DocenteCarreraServiceImplement!edu.jlmallas.academico.service.DocenteCarreraService")
    private DocenteCarreraService docenteCarreraService;
    @EJB(lookup = "java:global/SigettService/DirectorServiceImplement!edu.unl.sigett.service.DirectorService")
    private DirectorService directorService;
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;
    @EJB(lookup = "java:global/ComunService/EventoServiceImplement!com.jlmallas.comun.service.EventoService")
    private EventoService eventoService;
    @EJB(lookup = "java:global/SigettService/CalificacionMiembroServiceImplement!edu.unl.sigett.service.CalificacionMiembroService")
    private CalificacionMiembroService calificacionMiembroService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(MiembroTribunalController.class.getName());

    public MiembroTribunalController() {
    }

    public void preRenderView() {
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
                    PropertiesFileEnum.PERMISOS, "crear_miembro_tribunal").trim());
            if (tienePermiso == 1) {
                buscarCargos();
                sessionMiembroTribunal.setMiembroTribunalDTOSeleccionado(new MiembroTribunalDTO(new MiembroTribunal(), null, null));
                sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getMiembroTribunal().setEsActivo(Boolean.TRUE);
                sessionMiembroTribunal.setRenderedDlgCrud(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgCrudMiembroTribunal').show()");
                return;
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_crear"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void cancelarEdicion() {
        RequestContext.getCurrentInstance().execute("PF('dlgCrudMiembroTribunal').hide()");
        sessionMiembroTribunal.setMiembroTribunalDTOSeleccionado(new MiembroTribunalDTO());
        sessionMiembroTribunal.setRenderedDlgCrud(Boolean.FALSE);
    }

    public void editar(MiembroTribunalDTO miembroTribunal) {
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
                    PropertiesFileEnum.PERMISOS, "editar_miembro_tribunal").trim());
            if (tienePermiso == 1) {
                buscarCargos();
                sessionMiembroTribunal.setCargoSeleccionado(itemService.buscarPorId(miembroTribunal.getMiembroTribunal().getCargoId()));
                sessionMiembroTribunal.setMiembroTribunalDTOSeleccionado(miembroTribunal);
                sessionMiembroTribunal.setRenderedDlgCrud(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgCrudMiembroTribunal').show()");
                return;
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_editar"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void seleccionarDocente(DirectorDTO directorDTO) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        Boolean directorEncontrado = Boolean.FALSE;
        Configuracion configuracionBuscar = new Configuracion(ConfiguracionEnum.AGREGARMIEMBRODIRECTOR.getTipo());
        List<Configuracion> configuraciones = configuracionService.buscar(configuracionBuscar);
        Configuracion configuracion = !configuraciones.isEmpty() ? configuraciones.get(0) : null;
        try {
            if (!validaDocenteDisponible(directorDTO.getPersona())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("miembro_ocupado"), "");
                return;
            }
            if (!sessionProyecto.getDirectoresProyectoDTO().isEmpty() && configuracion != null) {
                if (configuracion.getValor().equals(ValorEnum.NO.getTipo())) {
                    for (DirectorProyectoDTO directorProyectoDTO : sessionProyecto.getDirectoresProyectoDTO()) {
                        if (directorProyectoDTO.getDirectorDTO().getDirector().equals(directorDTO.getDirector())) {
                            directorEncontrado = Boolean.TRUE;
                            break;
                        }
                    }
                }
            }

            if (directorEncontrado) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("miembro_es_director"), "");
                return;
            }
            sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().setDocente(directorDTO.getDocenteCarrera().getDocenteId());
            sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().setPersona(directorDTO.getPersona());
            sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getMiembroTribunal().setDocenteId(sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getDocente().getId());
            MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
            miembroTribunalBuscar.setDocenteId(sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getDocente().getId());
            miembroTribunalBuscar.setTribunalId(sessionTribunal.getTribunal());
            List<MiembroTribunal> miembroTribunals = miembroTribunalService.buscar(miembroTribunalBuscar);
            MiembroTribunal miembroTribunalEditado = !miembroTribunals.isEmpty() ? miembroTribunals.get(0) : null;
            if (miembroTribunalEditado != null) {
                miembroTribunalEditado.setEsActivo(Boolean.TRUE);
                sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().setMiembroTribunal(miembroTribunalEditado);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void buscarMiembrosDisponibles() {
        listarDirectoresCarrera();
        listadoMiembrosDisponibles();
        sessionMiembroTribunal.setRenderedDlgDocentesDisponibles(Boolean.TRUE);
        RequestContext.getCurrentInstance().execute("PF('dlgMiembrosDisponibles').show()");
    }

    public void cancelarMiembrosDisponibles() {
        sessionMiembroTribunal.setRenderedDlgDocentesDisponibles(Boolean.FALSE);
        RequestContext.getCurrentInstance().execute("PF('dlgMiembrosDisponibles').hide()");
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

    public void grabar(MiembroTribunalDTO miembroTribunalDTO) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo-grabado");
        try {
            miembroTribunalDTO.getMiembroTribunal().setCargoId(sessionMiembroTribunal.getCargoSeleccionado().getId());
            miembroTribunalDTO.getMiembroTribunal().setTribunalId(sessionTribunal.getTribunal());
            if (miembroTribunalDTO.getMiembroTribunal().getDocenteId() == null) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("docente_no_seleccionado"), "");
                return;
            }
            if (!validaDocenteDisponible(miembroTribunalDTO.getPersona())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("miembro_ocupado"), "");
                return;
            }
            if (existePresidente(miembroTribunalDTO.getMiembroTribunal())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("tribunal_existe_presidente"), "");
                return;
            }
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            if (miembroTribunalDTO.getMiembroTribunal().getId() == null) {
                Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.PERMISOS, "crear_miembro_tribunal").trim());
                if (tienePermiso == 1) {
                    miembroTribunalService.guardar(miembroTribunalDTO.getMiembroTribunal());
                    grabarEventosDirector();
                    grabarCalificacionesMiembrosTribunal();
                    if (param.equalsIgnoreCase("grabar")) {
                        cancelarEdicion();
                        return;
                    }
                    cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                }
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_crear"), "");
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "editar_miembro_tribunal").trim());
            if (tienePermiso == 1) {
                miembroTribunalService.actualizar(miembroTribunalDTO.getMiembroTribunal());
                grabarEventosDirector();
                grabarCalificacionesMiembrosTribunal();
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                if (param.equalsIgnoreCase("grabar")) {
                    cancelarEdicion();
                    return;
                }
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_editar"), "");
        } catch (NumberFormatException e) {
            LOG.warning(e.getMessage());
        }
    }

    /**
     * BUSCAR DOCENTES DISPONIBLES PARA AGREGARLOOS COMO MIEMBROS DE TRIBUNAL DE
     * PROYECTO SELECCIONADO
     *
     * @param query
     * @return
     */
    public List<DirectorDTO> completarDocentes(final String query) {
        sessionMiembroTribunal.getDirectoresDTO().clear();
        Boolean miembrosEmpty = Boolean.FALSE;
        List<DirectorDTO> results = new ArrayList<>();
        try {
            if ("".equals(query.trim())) {
                return new ArrayList<>();
            }
            MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
            miembroTribunalBuscar.setTribunalId(sessionTribunal.getTribunal());
            List<MiembroTribunal> miembrosTribunal = miembroTribunalService.buscar(miembroTribunalBuscar);
            if (miembrosTribunal.isEmpty()) {
                sessionMiembroTribunal.getDirectoresDTO().addAll(sessionMiembroTribunal.getDirectoresDTOAux());
                miembrosEmpty = Boolean.TRUE;
            }
            if (!miembrosEmpty) {
                for (DirectorDTO directorDTO : sessionMiembroTribunal.getDirectoresDTOAux()) {
                    for (MiembroTribunal miembroTribunal : miembrosTribunal) {
                        if (!miembroTribunal.getDocenteId().equals(directorDTO.getDocenteCarrera().getDocenteId().getId())) {
                            sessionMiembroTribunal.getDirectoresDTO().add(directorDTO);
                        }
                    }
                }
            }
            for (DirectorDTO directorDTO : sessionMiembroTribunal.getDirectoresDTO()) {
                if (directorDTO.getPersona().getApellidos().toLowerCase().startsWith(query.toLowerCase())
                        || directorDTO.getPersona().getNombres().toLowerCase().startsWith(query.toLowerCase())
                        || directorDTO.getPersona().getNumeroIdentificacion().toLowerCase().startsWith(query.toLowerCase())) {
                    results.add(directorDTO);
                }
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
        DirectorDTOConverter.setDirectoresDTO(results);
        return results;
    }

    /**
     * GRABAR CALIFICACIONES DE TRIBUNAL POR CADA EVALUACIÓN
     */
    private void grabarCalificacionesMiembrosTribunal() {
        for (EvaluacionTribunal evaluacionTribunal : sessionEvaluacionTribunal.getEvaluacionesTribunal()) {
            CalificacionMiembro calificacionMiembroBuscar = new CalificacionMiembro();
            calificacionMiembroBuscar.setMiembroId(cabeceraController.getSecureService().encrypt(
                    new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getMiembroTribunal().getId() + "")));
            CalificacionMiembro calificacionMiembro = calificacionMiembroService.buscarPorMiembro(calificacionMiembroBuscar);
            if (calificacionMiembro == null) {
                calificacionMiembro = new CalificacionMiembro(null, BigDecimal.ZERO, "Ninguno", cabeceraController.getSecureService().encrypt(
                        new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getMiembroTribunal().getId() + "")), Boolean.TRUE, evaluacionTribunal);
                calificacionMiembroService.guardar(calificacionMiembro);
            }
        }
    }

    /**
     * LISTAR LOS DIRECTORES DE PROYECTOS DE LAS CARRERAS ADMINISTRADAS POR EL
     * USUARIO DEL SISTEMA
     */
    private void listarDirectoresCarrera() {
        sessionMiembroTribunal.getDirectoresDTOAux().clear();
        for (Carrera carrera : sessionProyecto.getCarreras()) {
            DocenteCarrera docenteCarreraBuscar = new DocenteCarrera();
            docenteCarreraBuscar.setCarreraId(carrera);
            List<DocenteCarrera> docenteCarreras = docenteCarreraService.buscar(docenteCarreraBuscar);
            for (DocenteCarrera dc : docenteCarreras) {
                Director directorBuscar = new Director();
                directorBuscar.setId(dc.getId());
                DirectorDTO directorDTO = new DirectorDTO(directorService.buscarPorId(directorBuscar), dc,
                        personaService.buscarPorId(new Persona(dc.getDocenteId().getId())));
                sessionMiembroTribunal.getDirectoresDTOAux().add(directorDTO);
            }
        }
    }

    /**
     * CREAR EVENTO PARA LOS MIEMBROS DE TRIBUANAL
     */
    private void grabarEventosDirector() {
        Item tipoEvento = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOEVENTO.getTipo(), EventoEnum.MIEMBROTRIBUNAL.getTipo());
        for (EvaluacionTribunal evaluacionTribunal : sessionEvaluacionTribunal.getEvaluacionesTribunal()) {
            if (!evaluacionTribunal.getEsActivo()) {
                continue;
            }
            evaluacionTribunal.setCatalogoEvaluacion(itemService.buscarPorId(evaluacionTribunal.getCatalogoEvaluacionId()).getNombre());
            Evento eventoBuscar = new Evento();
            eventoBuscar.setTablaId(evaluacionTribunal.getId());
            Calendar fechaCulminacion = Calendar.getInstance();
            fechaCulminacion.setTime(evaluacionTribunal.getFechaPlazo());
            fechaCulminacion.add(Calendar.HOUR_OF_DAY, 1);
            List<Evento> eventos = eventoService.buscar(eventoBuscar);
            Evento evento = !eventos.isEmpty() ? eventos.get(0) : null;
            if (evento == null) {
                evento = new Evento(null, evaluacionTribunal.getCatalogoEvaluacion() + ": "
                        + sessionProyecto.getProyectoSeleccionado().getTemaActual(), evaluacionTribunal.getLugar(), evaluacionTribunal.getFechaInicio(),
                        fechaCulminacion.getTime(), tipoEvento.getId(), evaluacionTribunal.getId());
                eventoService.guardar(evento);
                EventoPersona eventoMiembro = new EventoPersona(null, sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getPersona(), evento);
                eventoPersonaService.guardar(eventoMiembro);
                continue;
            }
            evento.setLugar(evaluacionTribunal.getLugar());
            evento.setFechaInicio(evaluacionTribunal.getFechaInicio());
            evento.setFechaFin(evaluacionTribunal.getFechaPlazo());
            evento.setNombre(evaluacionTribunal.getCatalogoEvaluacion() + ": "
                    + evaluacionTribunal.getTribunalId().getProyectoId().getTemaActual());
            eventoService.actualizar(evento);
            EventoPersona eventoPersonaBuscar = new EventoPersona();
            eventoPersonaBuscar.setEvento(evento);
            eventoPersonaBuscar.setPersonaId(sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getPersona());
            List<EventoPersona> eventoPersonas = eventoPersonaService.buscar(eventoPersonaBuscar);
            EventoPersona eventoPersona = !eventoPersonas.isEmpty() ? eventoPersonas.get(0) : null;
            if (eventoPersona == null) {
                eventoPersona = new EventoPersona(null, sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().getPersona(), evento);
                eventoPersonaService.guardar(eventoPersona);
            }
        }
    }

    private void listadoMiembrosDisponibles() {
        sessionMiembroTribunal.getDirectoresDTO().clear();
        Boolean miembrosEmpty = Boolean.FALSE;
        MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
        miembroTribunalBuscar.setTribunalId(sessionTribunal.getTribunal());
        List<MiembroTribunal> miembrosTribunal = miembroTribunalService.buscar(miembroTribunalBuscar);
        if (miembrosTribunal.isEmpty()) {
            sessionMiembroTribunal.getDirectoresDTO().addAll(sessionMiembroTribunal.getDirectoresDTOAux());
            miembrosEmpty = Boolean.TRUE;
        }
        if (!miembrosEmpty) {
            for (DirectorDTO directorDTO : sessionMiembroTribunal.getDirectoresDTOAux()) {
                for (MiembroTribunal miembroTribunal : miembrosTribunal) {
                    if (!miembroTribunal.getDocenteId().equals(directorDTO.getDocenteCarrera().getDocenteId().getId())) {
                        if (!sessionMiembroTribunal.getDirectoresDTO().contains(directorDTO)) {
                            sessionMiembroTribunal.getDirectoresDTO().add(directorDTO);
                        }
                    }
                }
            }
        }
    }

    public void buscar() {
        this.sessionMiembroTribunal.getMiembrosTribunalDTO().clear();
        this.sessionMiembroTribunal.getFilterMiembrosTribunalDTO().clear();
        MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
        if (sessionTribunal.getTribunal().getId() == null) {
            return;
        }
        miembroTribunalBuscar.setTribunalId(sessionTribunal.getTribunal());
        miembroTribunalBuscar.setEsActivo(Boolean.TRUE);
        List<MiembroTribunal> miembrosTribunal = miembroTribunalService.buscar(miembroTribunalBuscar);
        for (MiembroTribunal miembroTribunal : miembrosTribunal) {
            MiembroTribunalDTO miembroTribunalDTO = new MiembroTribunalDTO(miembroTribunal,
                    docenteService.buscarPorId(new Docente(miembroTribunal.getDocenteId())), null);
            miembroTribunalDTO.setPersona(personaService.buscarPorId(new Persona(miembroTribunalDTO.getDocente().getId())));
            miembroTribunal.setCargo(itemService.buscarPorId(miembroTribunal.getCargoId()).getNombre());
            sessionMiembroTribunal.getMiembrosTribunalDTO().add(miembroTribunalDTO);
        }
        this.sessionMiembroTribunal.setFilterMiembrosTribunalDTO(sessionMiembroTribunal.getMiembrosTribunalDTO());
    }

    public void buscarCargos() {
        sessionMiembroTribunal.setCargos(itemService.buscarPorCatalogo(CatalogoEnum.CARGOMIEMBROTRIBUNAL.getTipo()));
    }

    /**
     * VERIFICAR SI EXISTE MAS DE UN PRESIDENTE DEL TRIBUNAL SELECCIONADO.
     *
     * @param miembroTribunal
     * @return
     */
    private Boolean existePresidente(MiembroTribunal miembroTribunal) {
        try {
            Item presidente = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CARGOMIEMBROTRIBUNAL.getTipo(), CargoMiembroEnum.PRESIDENTE.getTipo());
            if (miembroTribunal.getCargoId().equals(presidente.getId())) {
                for (MiembroTribunalDTO miembroTribunalDTO : sessionMiembroTribunal.getMiembrosTribunalDTO()) {
                    if (miembroTribunalDTO.getDocente().getId().equals(miembroTribunal.getDocenteId())) {
                        continue;
                    }
                    if (miembroTribunalDTO.getMiembroTribunal().getCargoId().equals(presidente.getId())
                            && miembroTribunal.getCargoId().equals(miembroTribunalDTO.getMiembroTribunal().getCargoId())) {
                        return Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
        return Boolean.FALSE;
    }

    public void remover(MiembroTribunalDTO miembro) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            sessionMiembroTribunal.setRenderedCrear(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "eliminar_miembro_tribunal").trim());
            if (tienePermiso == 1) {
                miembro.getMiembroTribunal().setEsActivo(Boolean.FALSE);
                miembroTribunalService.actualizar(miembro.getMiembroTribunal());
                return;
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_eliminar"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedImprimirOficio() {
//        try {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_miembro_tribunal");
//            if (tienePermiso == 1) {
//                if (proyecto.getId() != null) {
//                    proyecto = proyectoFacadeLocal.find(proyecto.getId());
//                }
//                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                        || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                        || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                        || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                    renderedImprimirOficioSustentacionPrivada = true;
//                    renderedImprimirOficioSustentacionPublica = false;
//                } else {
//                    if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                            || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                            || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                            || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                        renderedImprimirOficioSustentacionPublica = true;
//                        renderedImprimirOficioSustentacionPrivada = false;
//                    } else {
//                        renderedImprimirOficioSustentacionPublica = false;
//                        renderedImprimirOficioSustentacionPrivada = false;
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
    }

    public void renderedCrear() {
        try {
            sessionMiembroTribunal.setRenderedCrear(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "crear_miembro_tribunal").trim());
            if (tienePermiso == 1) {
                sessionMiembroTribunal.setRenderedCrear(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void renderedEditar() {
        try {
            sessionMiembroTribunal.setRenderedEditar(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "editar_miembro_tribunal").trim());
            if (tienePermiso == 1) {
                sessionMiembroTribunal.setRenderedEditar(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }

    }

    public void renderedEliminar() {
        try {
            sessionMiembroTribunal.setRenderedEliminar(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "eliminar_miembro_tribunal").trim());
            if (tienePermiso == 1) {
                sessionMiembroTribunal.setRenderedEliminar(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }
//
////</editor-fold>
}
