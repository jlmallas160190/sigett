/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.miembroTribunal;

import com.jlmallas.comun.entity.EventoPersona;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.EventoPersonaService;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.service.DocenteService;
import edu.unl.sigett.director.DirectorDM;
import edu.unl.sigett.director.DirectorDTO;
import edu.unl.sigett.director.DirectorDTOConverter;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.enumeration.CargoMiembroEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.MiembroTribunalService;
import edu.unl.sigett.tribunal.SessionTribunal;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
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
    private DirectorDM directorDM;
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
        try {
            if (!validaDocenteDisponible(directorDTO.getPersona())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("miembro_ocupado"), "");
                return;
            }
            sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().setDocente(directorDTO.getDocenteCarrera().getDocenteId());
            sessionMiembroTribunal.getMiembroTribunalDTOSeleccionado().setPersona(directorDTO.getPersona());
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_seleccionar"), "");
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
        for (MiembroTribunalDTO miembroTribunalDTO : sessionMiembroTribunal.getMiembrosTribunalDTO()) {
            if (!validaDocenteDisponible(miembroTribunalDTO.getPersona())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void grabar(MiembroTribunalDTO miembroTribunalDTO) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (miembroTribunalDTO.getMiembroTribunal().getDocenteId() == null) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("docente_no_seleccionado"), "");
                return;
            }
            if (!validaMiembrosTribunal()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("miembro_ocupado"), "");
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
                    if (param.equalsIgnoreCase("grabar")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarMiembroTribunal').hide()");
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
                miembroTribunalService.guardar(miembroTribunalDTO.getMiembroTribunal());
                if (param.equalsIgnoreCase("grabar")) {
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarMiembroTribunal').hide()");
                    cancelarEdicion();
                    return;
                }
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
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
        List<DirectorDTO> results = new ArrayList<>();
        try {
            if ("".equals(query.trim())) {
                return new ArrayList<>();
            }
            MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
            miembroTribunalBuscar.setTribunalId(sessionTribunal.getTribunal());
            List<MiembroTribunal> miembrosTribunal = miembroTribunalService.buscar(miembroTribunalBuscar);
            if (miembrosTribunal.isEmpty()) {
                sessionMiembroTribunal.setDirectoresDTO(directorDM.getDirectoresDTO());
            }
            for (DirectorDTO directorDTO : directorDM.getDirectoresDTO()) {
                for (MiembroTribunal miembroTribunal : miembrosTribunal) {
                    if (!miembroTribunal.getDocenteId().equals(directorDTO.getDocenteCarrera().getDocenteId().getId())) {
                        sessionMiembroTribunal.getDirectoresDTO().add(directorDTO);
                    }
                }
            }
            for (DirectorDTO directorDTO : sessionMiembroTribunal.getDirectoresDTO()) {
                if (directorDTO.getPersona().getApellidos().toLowerCase().contains(query.toLowerCase())
                        || directorDTO.getPersona().getNombres().toLowerCase().contains(query.toLowerCase())
                        || directorDTO.getPersona().getNumeroIdentificacion().toLowerCase().contains(query.toLowerCase())) {
                   results.add(directorDTO);
                }
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
        DirectorDTOConverter.setDirectoresDTO(results);
        return results;
    }

    public void buscar() {
        this.sessionMiembroTribunal.getMiembrosTribunalDTO().clear();
        this.sessionMiembroTribunal.getFilterMiembrosTribunalDTO().clear();
        for (MiembroTribunal miembroTribunal : sessionTribunal.getTribunal().getMiembroList()) {
            MiembroTribunalDTO miembroTribunalDTO = new MiembroTribunalDTO(miembroTribunal,
                    docenteService.buscarPorId(new Docente(miembroTribunal.getDocenteId())), null);
            miembroTribunalDTO.setPersona(personaService.buscarPorId(new Persona(miembroTribunalDTO.getDocente().getId())));
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
    public Boolean existePresidente(MiembroTribunal miembroTribunal) {
        try {
            Item presidente = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CARGOMIEMBROTRIBUNAL.getTipo(), CargoMiembroEnum.PRESIDENTE.getTipo());
            if (miembroTribunal.getCargoId().equals(presidente.getId())) {
                for (MiembroTribunalDTO miembroTribunalDTO : sessionMiembroTribunal.getMiembrosTribunalDTO()) {
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
