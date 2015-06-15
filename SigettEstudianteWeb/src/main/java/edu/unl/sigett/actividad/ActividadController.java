/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.actividad;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Evento;
import com.jlmallas.comun.entity.EventoPersona;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.EventoEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.EventoPersonaService;
import com.jlmallas.comun.service.EventoService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.autorProyecto.SessionAutorProyecto;
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import edu.unl.sigett.documentoActividad.DocumentoActividadDTO;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.enumeration.ConfiguracionProyectoEnum;
import edu.unl.sigett.enumeration.EstadoActividadEnum;
import edu.unl.sigett.enumeration.TipoActividadEnum;
import edu.unl.sigett.service.ActividadService;
import edu.unl.sigett.service.ConfiguracionProyectoService;
import edu.unl.sigett.service.DocumentoActividadService;
import edu.unl.sigett.util.CabeceraController;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jorge-luis
 */
@Named(value = "actividadController")
@SessionScoped
public class ActividadController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionActividad sessionActividad;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionAutorProyecto sessionAutorProyecto;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ItemService itemService;
    @EJB
    private ConfiguracionProyectoService configuracionProyectoService;
    @EJB
    private ActividadService actividadService;
    @EJB
    private DocumentoActividadService documentoActividadService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private EventoService eventoService;
    @EJB
    private EventoPersonaService eventoPersonaService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(ActividadController.class.getName());

    public ActividadController() {
    }

    public void preRenderView() {
        this.buscar();
        this.generaArbol();
    }
    //<editor-fold defaultstate="collapsed" desc="CRUD">
    /**
     * CREAR
     */
    public void crear() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo");
        String padreId = (String) facesContext.getExternalContext().getRequestParameterMap().get("padreId");
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.DESARROLLO.getTipo());
        Item objetivo = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOACTIVIDAD.getTipo(), TipoActividadEnum.OBJETIVO.getTipo());
        sessionActividad.setActividad(new Actividad(null, null, null, BigDecimal.ZERO, padreId != null ? Long.parseLong(padreId) : null, Boolean.TRUE, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, null, objetivo.getId(), estado.getId(),
                sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma()));
        if (param.equalsIgnoreCase(TipoActividadEnum.TAREA.getTipo())) {
            Item tarea = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOACTIVIDAD.getTipo(), TipoActividadEnum.TAREA.getTipo());
            sessionActividad.getActividad().setTipoId(tarea.getId());
        }
        sessionActividad.setRenderedCrud(Boolean.TRUE);
    }

    public void editar(Actividad actividad) {
        sessionActividad.setActividad(actividad);
    }

    public void cancelarEdicion() {
        sessionActividad.setActividad(new Actividad());
        sessionActividad.setRenderedCrud(Boolean.FALSE);
    }

    private Boolean validarFechas() {
        if (sessionActividad.getActividad().getFechaInicio() == null || sessionActividad.getActividad().getFechaCulminacion() == null) {
            return Boolean.FALSE;
        }
        if (sessionActividad.getActividad().getFechaInicio().after(sessionActividad.getActividad().getFechaCulminacion())) {
            return Boolean.FALSE;
        }
        if (sessionActividad.getActividad().getPadreId() != null) {
            Actividad actividadPadre = actividadService.buscarPorId(new Actividad(sessionActividad.getActividad().getPadreId()));
            if (actividadPadre == null) {
                return Boolean.FALSE;
            }
            if (!((sessionActividad.getActividad().getFechaInicio().equals(actividadPadre.getFechaInicio()) || sessionActividad.getActividad().
                    getFechaInicio().after(actividadPadre.getFechaInicio())) && (sessionActividad.getActividad().getFechaCulminacion().equals(
                            actividadPadre.getFechaCulminacion()) || sessionActividad.getActividad().getFechaCulminacion().before(actividadPadre.getFechaCulminacion())))) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        if (!((sessionActividad.getActividad().getFechaInicio().equals(sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().
                getProyectoId().getCronograma().getFechaInicio()) || sessionActividad.getActividad().getFechaInicio().after(
                        sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma().getFechaInicio()))
                && (sessionActividad.getActividad().getFechaCulminacion().equals(sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().
                        getProyectoId().getCronograma().getFechaProrroga()) || sessionActividad.getActividad().getFechaCulminacion().before(
                        sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma().getFechaProrroga())))) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void calcularDuracion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        @SuppressWarnings("UnusedAssignment")
        Double duracionDias = 0.0;
        if (!validarFechas()) {
            sessionActividad.getActividad().setFechaCulminacion(null);
            sessionActividad.getActividad().setDuracion(BigDecimal.ZERO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("fechas_invalidas"), "");
            return;
        }
        duracionDias = cabeceraController.getUtilService().calculaDuracion(sessionActividad.getActividad().getFechaInicio(),
                sessionActividad.getActividad().getFechaCulminacion(), Integer.parseInt(ValorEnum.DIASSEMANA.getTipo()) - calculaDiasSemanaTrabajoProyecto());
        sessionActividad.getActividad().setDuracion(new BigDecimal(duracionDias));
        sessionActividad.getActividad().setPorcentajeDuracion(sessionActividad.getActividad().getDuracion().divide(
                sessionActividad.getActividad().getDuracion().add(sumatoriaActividades())).multiply(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo())));
        if (sessionActividad.getActividad().getPadreId() != null) {
            sessionActividad.getActividad().setPorcentajeDuracion(sessionActividad.getActividad().getDuracion().divide(
                    sessionActividad.getActividad().getDuracion().add(sumatoriaSubActividades())).multiply(new BigDecimal(100.0)));
        }
        sessionActividad.getActividad().setPorcentajeAvance(BigDecimal.ZERO);
    }

    /**
     * OBTENER DE LAS CONFIGURACIONES DEL PROYECTO LOS DIAS QUE SE TRABAJA A LA
     * SEMANA
     *
     * @return
     */
    private Integer calculaDiasSemanaTrabajoProyecto() {
        Integer dias = 0;
        List<ConfiguracionProyecto> configuracionProyectos = configuracionProyectoService.buscar(new ConfiguracionProyecto(
                sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId(), null, null,
                ConfiguracionProyectoEnum.DIASSEMANA.getTipo(), null));
        for (ConfiguracionProyecto cf : configuracionProyectos) {
            dias = Integer.parseInt(cf.getValor());
            break;
        }
        return dias;
    }

    private BigDecimal sumatoriaActividades() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Actividad actividad : sessionActividad.getActividades()) {
            if (actividad.getPadreId() == null) {
                sum = sum.add(actividad.getDuracion());
            }
        }
        return sum;
    }

    private BigDecimal sumatoriaSubActividades() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Actividad actividad : sessionActividad.getActividades()) {
            if (actividad.getPadreId() != null) {
                sum = sum.add(actividad.getDuracion());
            }
        }
        return sum;
    }

    private void actualizarCalculos() {
        for (Actividad actividad : sessionActividad.getActividades()) {
            if (actividad.getPadreId() == null) {
                actividad.setPorcentajeDuracion(actividad.getDuracion().divide(
                        actividad.getDuracion().add(sumatoriaActividades())).multiply(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo())));
                actividadService.actualizar(actividad);
                continue;
            }
            actividad.setPorcentajeDuracion(actividad.getDuracion().divide(
                    actividad.getDuracion().add(sumatoriaSubActividades())).multiply(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo())));
            actividadService.actualizar(actividad);
        }
    }

    public void grabar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (sessionActividad.getActividad().getId() == null) {
                actividadService.guardar(sessionActividad.getActividad());
                actualizarCalculos();
                grabarEventosAutor();
                grabarDocumentos();
                grabarEventosDirector();
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("grabar"), "");
                return;
            }
            actividadService.actualizar(sessionActividad.getActividad());
            actualizarCalculos();
            grabarEventosAutor();
            grabarDocumentos();
            grabarEventosDirector();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }

    }

    private void grabarEventosAutor() {
        List<EventoPersona> eventosAutor = eventoPersonaService.buscar(new EventoPersona(null, sessionAutorProyecto.getAutorProyectoDTO().getPersona(),
                sessionActividad.getActividad().getId()));
        EventoPersona eventoAutor = !eventosAutor.isEmpty() ? eventosAutor.get(0) : null;
        if (eventoAutor == null) {
            Evento evento = new Evento(null, sessionActividad.getActividad().getNombre(), sessionActividad.getActividad().getFechaInicio(),
                    sessionActividad.getActividad().getFechaCulminacion(),
                    itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOEVENTO.getTipo(), EventoEnum.ACTIVIDAD.getTipo()).getId());
            eventoService.guardar(evento);
            eventoAutor = new EventoPersona(evento.getId(), sessionAutorProyecto.getAutorProyectoDTO().getPersona(), sessionActividad.getActividad().getId());
            eventoPersonaService.guardar(eventoAutor);
        } else {
            Evento evento = eventoService.buscarPorId(new Evento(eventoAutor.getId()));
            evento.setFechaInicio(sessionActividad.getActividad().getFechaInicio());
            evento.setFechaFin(sessionActividad.getActividad().getFechaCulminacion());
            evento.setNombre(sessionActividad.getActividad().getNombre());
            eventoService.actualizar(evento);
        }
    }

    private void grabarEventosDirector() {
        for (DirectorProyectoDTO directorProyectoDTO : sessionAutorProyecto.getDirectoresProyectoDTO()) {
            List<EventoPersona> eventoDocentes = eventoPersonaService.buscar(new EventoPersona(null, directorProyectoDTO.getDirectorDTO().getPersona(), sessionActividad.getActividad().getId()));
            EventoPersona eventoDocente = !eventoDocentes.isEmpty() ? eventoDocentes.get(0) : null;
            if (eventoDocente == null) {
                Evento evento = new Evento(null, sessionActividad.getActividad().getNombre(), sessionActividad.getActividad().getFechaInicio(),
                        sessionActividad.getActividad().getFechaCulminacion(),
                        itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOEVENTO.getTipo(), EventoEnum.ACTIVIDAD.getTipo()).getId());
                eventoService.guardar(evento);
                eventoDocente = new EventoPersona(evento.getId(), directorProyectoDTO.getDirectorDTO().getPersona(), sessionActividad.getActividad().getId());
                eventoPersonaService.guardar(eventoDocente);
                continue;
            }
            Evento evento = eventoService.buscarPorId(new Evento(eventoDocente.getId()));
            evento.setFechaInicio(sessionActividad.getActividad().getFechaInicio());
            evento.setFechaFin(sessionActividad.getActividad().getFechaCulminacion());
            evento.setNombre(sessionActividad.getActividad().getNombre());
            eventoService.actualizar(evento);
        }
    }

    private void buscar() {
        sessionActividad.getActividades().clear();
        Actividad actividadBuscar = new Actividad();
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        actividadBuscar.setCronogramaId(sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma());
        List<Actividad> actividades = actividadService.buscar(actividadBuscar);
        for (Actividad actividad : actividades) {
            actividad.setEstiloEstado("actividad_desarrollo");
            if (actividad.getEstadoId().equals(estado.getId())) {
                actividad.setEstiloEstado("actividad_revisada");
            }
            sessionActividad.getActividades().add(actividad);
        }
    }

    private void generaArbol() {
        this.sessionActividad.setRootActividades(new DefaultTreeNode("Root", null));
        TreeNode node = null;
        for (Actividad actividad : sessionActividad.getActividades()) {
            node = new DefaultTreeNode(actividad, sessionActividad.getRootActividades());
        }
        for (Actividad actividad : sessionActividad.getActividades()) {
            for (TreeNode nodoPadre : sessionActividad.getRootActividades().getChildren()) {
                if (actividad.getPadreId() != actividad.getId()) {
                    Actividad actividadPadre = (Actividad) nodoPadre.getData();
                    if (actividadPadre.getId() == actividad.getPadreId()) {
                        nodoPadre.getChildren().add(new DefaultTreeNode(actividad));
                    }
                }
            }
        }
    }

    private void grabarDocumentos() {
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTADOCUMENTOACTIVIDAD.getTipo())).get(0).getValor();
        for (DocumentoActividadDTO documentoActividadDTO : sessionActividad.getDocumentosActividadDTO()) {
            if (documentoActividadDTO.getDocumentoActividad().getId() == null) {
                documentoActividadDTO.getDocumento().setRuta("S/N");
                documentoService.guardar(documentoActividadDTO.getDocumento());
                documentoActividadDTO.getDocumentoActividad().setDocumentoId(documentoActividadDTO.getDocumento().getId());
                documentoActividadService.guardar(documentoActividadDTO.getDocumentoActividad());
                documentoActividadDTO.getDocumento().setRuta(ruta + "/documento" + documentoActividadDTO.getDocumento().getId() + ".pdf");
                cabeceraController.getUtilService().generaDocumento(new File(documentoActividadDTO.getDocumento().getRuta()),
                        documentoActividadDTO.getDocumento().getContents());
                documentoService.actualizar(documentoActividadDTO.getDocumento());
                return;
            }
            documentoActividadService.actualizar(documentoActividadDTO.getDocumentoActividad());
        }
    }
    //</editor-fold>
}
