/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.actividad;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.autorProyecto.SessionAutorProyecto;
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
import java.math.MathContext;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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

    //</editor-fold>
    public ActividadController() {
    }

    public void preRenderView() {
        this.buscar();
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
                BigDecimal.ZERO, BigDecimal.ZERO, null, objetivo.getId(), estado.getId(), null));
        if (param.equalsIgnoreCase(TipoActividadEnum.TAREA.getTipo())) {
            Item tarea = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOACTIVIDAD.getTipo(), TipoActividadEnum.TAREA.getTipo());
            sessionActividad.getActividad().setTipoId(tarea.getId());
        }
    }

    public void editar(Actividad actividad) {
        sessionActividad.setActividad(actividad);
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
            Calendar fechaActual = Calendar.getInstance();
            sessionActividad.getActividad().setFechaInicio(fechaActual.getTime());
            sessionActividad.getActividad().setFechaCulminacion(fechaActual.getTime());
            sessionActividad.getActividad().setDuracion(BigDecimal.ZERO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("fechas_invalidas"), "");
        }
        duracionDias = cabeceraController.getUtilService().calculaDuracion(sessionActividad.getActividad().getFechaInicio(),
                sessionActividad.getActividad().getFechaCulminacion(), Integer.parseInt(ValorEnum.DIASSEMANA.getTipo()) - calculaDiasSemanaTrabajoProyecto());
        sessionActividad.getActividad().setDuracion(new BigDecimal(duracionDias));
        sessionActividad.getActividad().setPorcentajeDuracion(sessionActividad.getActividad().getDuracion().divide(
                sessionActividad.getActividad().getDuracion().add(sumatoriaActividades())).multiply(new BigDecimal(100.0)));
        if (sessionActividad.getActividad().getPadreId() != null) {
            sessionActividad.getActividad().setPorcentajeDuracion(sessionActividad.getActividad().getDuracion().divide(
                    sessionActividad.getActividad().getDuracion().add(sumatoriaSubActividades())).multiply(new BigDecimal(100.0)));
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
                sessionActividad.getActividad().getCronogramaId().getProyecto(), null, null,
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

        }
    }

    public void grabar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (sessionActividad.getActividad().getId() == null) {
            actividadService.guardar(sessionActividad.getActividad());
            grabarDocumentos();
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("grabar"), "");
            return;
        }
        actividadService.actualizar(sessionActividad.getActividad());
        grabarDocumentos();
    }

    private void buscar() {
        sessionActividad.getActividades().clear();
        sessionActividad.getFilterActividades().clear();
        Actividad actividadBuscar = new Actividad();
        actividadBuscar.setCronogramaId(sessionAutorProyecto.getAutorProyectoDTO().getAutorProyecto().getProyectoId().getCronograma());
        sessionActividad.getActividades().addAll(actividadService.buscar(actividadBuscar));
        sessionActividad.setFilterActividades(sessionActividad.getActividades());
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
