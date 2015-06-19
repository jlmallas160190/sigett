/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.actividad;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
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
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.entity.DocumentoActividad;
import edu.unl.sigett.enumeration.ConfiguracionProyectoEnum;
import edu.unl.sigett.enumeration.EstadoActividadEnum;
import edu.unl.sigett.enumeration.TipoActividadEnum;
import edu.unl.sigett.service.ActividadService;
import edu.unl.sigett.service.ConfiguracionProyectoService;
import edu.unl.sigett.service.CronogramaService;
import edu.unl.sigett.service.DocumentoActividadService;
import edu.unl.sigett.util.CabeceraController;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
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
    @EJB
    private CronogramaService cronogramaService;
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
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "lbl");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo");
        String padreId = (String) facesContext.getExternalContext().getRequestParameterMap().get("padreId");
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.DESARROLLO.getTipo());
        Item objetivo = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOACTIVIDAD.getTipo(), TipoActividadEnum.OBJETIVO.getTipo());
        sessionActividad.setActividad(new Actividad(null, null, null, BigDecimal.ZERO, padreId != null ? Long.parseLong(padreId) : null, Boolean.TRUE, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, null, objetivo.getId(), estado.getId(),
                sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma()));
        sessionActividad.setTitulo(bundle.getString("objetivo"));
        if (param.equalsIgnoreCase(TipoActividadEnum.TAREA.getTipo())) {
            sessionActividad.setTitulo(bundle.getString("tarea"));
            Item tarea = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOACTIVIDAD.getTipo(), TipoActividadEnum.TAREA.getTipo());
            sessionActividad.getActividad().setTipoId(tarea.getId());
        }
        fijarFechasLimite();
        sessionActividad.setRenderedCrud(Boolean.TRUE);
        listadoDocumentos();
        RequestContext.getCurrentInstance().execute("PF('dlgCrudActividad').show()");

    }

    private void fijarFechasLimite() {
        Actividad actividadPadre = actividadService.buscarPorId(new Actividad(sessionActividad.getActividad().getPadreId() != null ? sessionActividad.getActividad().getPadreId() : 0));
        if (actividadPadre != null) {
            sessionActividad.setFechaInicioLimite(cabeceraController.getUtilService().formatoFecha(actividadPadre.getFechaInicio(), "EEEEE dd MMMMM yyyy"));
            sessionActividad.setFechaFinLimite(cabeceraController.getUtilService().formatoFecha(actividadPadre.getFechaCulminacion(), "EEEEE dd MMMMM yyyy"));
        } else {
            sessionActividad.setFechaInicioLimite(cabeceraController.getUtilService().formatoFecha(
                    sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma().getFechaInicio(), "EEEEE dd MMMMM yyyy"));
            sessionActividad.setFechaFinLimite(cabeceraController.getUtilService().formatoFecha(
                    sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma().getFechaProrroga(), "EEEEE dd MMMMM yyyy"));
        }
    }

    public void editar(Actividad actividad) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "lbl");
        Item tipo = itemService.buscarPorId(actividad.getTipoId());
        sessionActividad.setTitulo(bundle.getString("objetivo"));
        if (tipo.getCodigo().equals(TipoActividadEnum.TAREA.getTipo())) {
            sessionActividad.setTitulo(bundle.getString("tarea"));
        }
        sessionActividad.setActividad(actividad);
        fijarFechasLimite();
        sessionActividad.setRenderedCrud(Boolean.TRUE);
        listadoDocumentos();
        RequestContext.getCurrentInstance().execute("PF('dlgCrudActividad').show()");
    }

    public void cancelarEdicion() {
        sessionActividad.setActividad(new Actividad());
        sessionActividad.setRenderedCrud(Boolean.FALSE);
        RequestContext.getCurrentInstance().execute("PF('dlgCrudActividad').hide()");

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
        sessionActividad.getActividad().setCronogramaId(null);
        Long estadoId = sessionActividad.getActividad().getEstadoId();
        Long tipoId = sessionActividad.getActividad().getTipoId();
        sessionActividad.getActividad().setEstadoId(null);
        sessionActividad.getActividad().setTipoId(null);
        BigDecimal resultado = actividadService.sumatoriaDuracion(sessionActividad.getActividad());
        sessionActividad.getActividad().setEstadoId(estadoId);
        sessionActividad.getActividad().setTipoId(tipoId);
        sessionActividad.getActividad().setCronogramaId(sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma());
        if (resultado.equals(BigDecimal.ZERO)) {
            sessionActividad.getActividad().setPorcentajeDuracion(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()));
            return;
        }
        sessionActividad.getActividad().setPorcentajeDuracion(sessionActividad.getActividad().getDuracion().divide(
                resultado.add(sessionActividad.getActividad().getDuracion()), 2, RoundingMode.HALF_UP).multiply(
                        new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo())));
        if (sessionActividad.getActividad().getPadreId() != null) {
            sessionActividad.getActividad().setPorcentajeDuracion(sessionActividad.getActividad().getDuracion().divide(
                    resultado.add(sessionActividad.getActividad().getDuracion()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo())));
        }
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

    private void actualizarPorcentajes() {
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        List<Actividad> actividades = actividadService.buscar(new Actividad(null, null, null, null, null, Boolean.TRUE, null, null, null, null, null, null,
                sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma()));
        for (Actividad actividad : actividades) {
            Cronograma cronogramaTemp = actividad.getCronogramaId();
            Long estadoId = actividad.getEstadoId();
            Long tipoId = actividad.getTipoId();
            actividad.setEstadoId(null);
            actividad.setTipoId(null);
            if (actividad.getPadreId() != null) {
                actividad.setCronogramaId(null);
            }
            BigDecimal resultado = actividadService.sumatoriaDuracion(actividad);
            actividad.setCronogramaId(cronogramaTemp);
            actividad.setEstadoId(estadoId);
            actividad.setTipoId(tipoId);
            if (actividad.getEstadoId().equals(estado.getId())) {
                actividad.setPorcentajeAvance(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()));
                actividad.setPorcentajeFaltante(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()).subtract(actividad.getPorcentajeAvance()));
            }
            if (resultado.equals(BigDecimal.ZERO)) {
                actividad.setPorcentajeDuracion(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()));
                actividadService.actualizar(actividad);
                continue;
            }
            actividad.setPorcentajeDuracion(actividad.getDuracion().divide(
                    resultado.add(actividad.getDuracion()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo())));
            actividadService.actualizar(actividad);
        }
    }

    private void enviar(String param) {
        if (param == null) {
            return;
        }
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.ENVIADO.getTipo());
        sessionActividad.getActividad().setPorcentajeAvance(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()));
        sessionActividad.getActividad().setPorcentajeFaltante(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()).subtract(
                sessionActividad.getActividad().getPorcentajeDuracion()));
        sessionActividad.getActividad().setEstadoId(estado.getId());
    }

    private void calculosObjetivo() {
        if (sessionActividad.getActividad().getPadreId() == null) {
            return;
        }
        List<Actividad> actividads = actividadService.buscar(new Actividad(null, null, null, null, sessionActividad.getActividad().getPadreId(), Boolean.TRUE, null, null, null, null, null, null,
                null));
        BigDecimal sum = BigDecimal.ZERO;
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        for (Actividad a : actividads) {
            if (a.getEstadoId().equals(estado.getId())) {
                sum = sum.add(a.getPorcentajeDuracion());
            }
        }
        Actividad actividadPadre = actividadService.buscarPorId(new Actividad(sessionActividad.getActividad().getPadreId()));
        actividadPadre.setPorcentajeAvance(sum);
        actividadPadre.setPorcentajeFaltante(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()).subtract(sum));
        if (actividadPadre.getPorcentajeAvance().equals(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()))) {
            actividadPadre.setEstadoId(estado.getId());
        }
        actividadService.actualizar(actividadPadre);

    }

    private void calculosCronograma() {
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.ENVIADO.getTipo());
        List<Actividad> actividads = actividadService.buscar(new Actividad(null, null, null, null, null, Boolean.TRUE, null, null, null, null, null,
                estado.getId(), sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma()));
        BigDecimal sum = BigDecimal.ZERO;
        for (Actividad a : actividads) {
            sum = sum.add(a.getPorcentajeDuracion());
        }
        sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma().setAvance(sum.doubleValue());
        sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma().setFaltante(
                Double.parseDouble(ValorEnum.DIVISORPORCENTAJE.getTipo()) - sum.doubleValue());
        cronogramaService.actualizar(sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma());
    }

    public void grabar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo");
        try {
            if (sessionActividad.getActividad().getId() == null) {
                enviar(param);
                actividadService.guardar(sessionActividad.getActividad());
                actualizarPorcentajes();
                calculosObjetivo();
                calculosCronograma();
                grabarEventosAutor();
                grabarDocumentos();
                grabarEventosDirector();
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("grabar"), "");
                sessionActividad.setRenderedCrud(Boolean.FALSE);
                cancelarEdicion();
                return;
            }
            enviar(param);
            actividadService.actualizar(sessionActividad.getActividad());
            actualizarPorcentajes();
            calculosObjetivo();
            calculosCronograma();
            grabarEventosAutor();
            grabarDocumentos();
            grabarEventosDirector();
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("editar"), "");
            sessionActividad.setRenderedCrud(Boolean.FALSE);
            cancelarEdicion();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }

    }

    public void remover(Actividad actividad) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        actividad.setEsActivo(Boolean.FALSE);
        actividadService.actualizar(actividad);
        actualizarPorcentajes();
        calculosObjetivo();
        calculosCronograma();
        cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("eliminar"), "");
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
        sessionActividad.getActividadesPadre().clear();
        Actividad actividadBuscar = new Actividad();
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        actividadBuscar.setCronogramaId(sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto() != null
                ? sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma() : null);
        actividadBuscar.setTipoId(itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOACTIVIDAD.getTipo(), TipoActividadEnum.OBJETIVO.getTipo()).getId());
        actividadBuscar.setEsActivo(Boolean.TRUE);
        List<Actividad> actividades = actividadService.buscar(actividadBuscar);
        for (Actividad actividad : actividades) {
            actividad.setEstiloEstado("actividad_desarrollo");
            if (actividad.getEstadoId().equals(estado.getId())) {
                actividad.setEstiloEstado("actividad_revisada");
            }
            sessionActividad.getActividadesPadre().add(actividad);
        }
    }

    private void generaArbol() {
        this.sessionActividad.setRootActividades(new DefaultTreeNode("Root", null));
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        for (Actividad actividad : sessionActividad.getActividadesPadre()) {
            TreeNode node = null;
            node = new DefaultTreeNode(actividad, sessionActividad.getRootActividades());
        }
        for (TreeNode nodoPadre : sessionActividad.getRootActividades().getChildren()) {
            Actividad actividadPadre = (Actividad) nodoPadre.getData();
            Actividad actividadBuscar = new Actividad();
            actividadBuscar.setPadreId(actividadPadre.getId());
            actividadBuscar.setEsActivo(Boolean.TRUE);
            List<Actividad> actividadesHijos = actividadService.buscar(actividadBuscar);
            if (actividadesHijos == null) {
                continue;
            }
            for (Actividad actividad : actividadesHijos) {
                actividad.setEstiloEstado("actividad_desarrollo");
                if (actividad.getEstadoId().equals(estado.getId())) {
                    actividad.setEstiloEstado("actividad_revisada");
                }
                nodoPadre.getChildren().add(new DefaultTreeNode(actividad));
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="DOCUMENTOS">
    private void listadoDocumentos() {
        sessionActividad.getDocumentosActividadDTO().clear();
        sessionActividad.getFilterDocumentosActividadDTO().clear();
        List<DocumentoActividad> documentoActividads = documentoActividadService.buscar(new DocumentoActividad(null, null, null, sessionActividad.getActividad()));
        for (DocumentoActividad documentoActividad : documentoActividads) {
            DocumentoActividadDTO documentoActividadDTO = new DocumentoActividadDTO(
                    documentoService.buscarPorId(new Documento(documentoActividad.getDocumentoId())), documentoActividad);
            documentoActividadDTO.getDocumento().setCatalogo(itemService.buscarPorId(documentoActividadDTO.getDocumento().getCatalogoId()).getNombre());
            sessionActividad.getDocumentosActividadDTO().add(documentoActividadDTO);
        }
        sessionActividad.setFilterDocumentosActividadDTO(sessionActividad.getDocumentosActividadDTO());
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
