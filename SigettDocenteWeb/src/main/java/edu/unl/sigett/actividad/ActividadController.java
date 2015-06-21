/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.actividad;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.directorProyecto.SessionDirectorProyecto;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.documentoActividad.DocumentoActividadDTO;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.entity.DocumentoActividad;
import edu.unl.sigett.enumeration.EstadoActividadEnum;
import edu.unl.sigett.enumeration.EstiloTreeNodeEnum;
import edu.unl.sigett.enumeration.TipoActividadEnum;
import edu.unl.sigett.service.ActividadService;
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
@URLMappings(mappings = {
    @URLMapping(
            id = "editarActividad",
            pattern = "/editarActividad",
            viewId = "/faces/pages/sigett/actividad/crudActividad.xhtml"
    )
})
public class ActividadController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionActividad sessionActividad;
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionDirectorProyecto sessionDirectorProyecto;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ActividadService actividadService;
    @EJB
    private ItemService itemService;
    @EJB
    private CronogramaService cronogramaService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private DocumentoActividadService documentoActividadService;
//</editor-fold>
    private static final Logger LOG = Logger.getLogger(ActividadController.class.getName());

    public ActividadController() {
    }

    public void preRenderView() {
        this.buscar();
        this.generaArbol();
    }
    //<editor-fold defaultstate="collapsed" desc="ACTIVIDAD">

    public void editar(Actividad actividad) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "lbl");
        Item tipo = itemService.buscarPorId(actividad.getTipoId());
        sessionActividad.setTitulo(bundle.getString("objetivo"));
        if (tipo.getCodigo().equals(TipoActividadEnum.TAREA.getTipo())) {
            sessionActividad.setTitulo(bundle.getString("tarea"));
        }
        sessionActividad.setActividad(actividad);
        listadoDocumentos();
        sessionActividad.setRenderedCrud(Boolean.TRUE);
        RequestContext.getCurrentInstance().execute("PF('dlgCrudActividad').show()");
    }

    public void cancelarEdicion() {
        sessionActividad.setActividad(new Actividad());
        sessionActividad.setRenderedCrud(Boolean.FALSE);
        RequestContext.getCurrentInstance().execute("PF('dlgCrudActividad').hide()");

    }

    private void revisar(String param) {
        Item estadoDesarrollo = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.ENVIADO.getTipo());
        sessionActividad.getActividad().setEstadoId(estadoDesarrollo.getId());
        if (param == null) {
            return;
        }
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        sessionActividad.getActividad().setPorcentajeAvance(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()));
        sessionActividad.getActividad().setPorcentajeFaltante(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()).subtract(
                sessionActividad.getActividad().getPorcentajeDuracion()));
        sessionActividad.getActividad().setEstadoId(estado.getId());
    }

    private void actualizarPorcentajes() {
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        List<Actividad> actividades = actividadService.buscar(new Actividad(null, null, null, null, null, Boolean.TRUE, null, null, null, null, null, null,
                sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getCronograma()));
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
            if (resultado.equals(BigDecimal.ZERO)) {
                actividad.setPorcentajeDuracion(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()));
                actividadService.actualizar(actividad);
                continue;
            }
            actividad.setPorcentajeDuracion(actividad.getDuracion().divide(
                    resultado.add(actividad.getDuracion()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo())));
            if (actividad.getEstadoId().equals(estado.getId())) {
                actividad.setPorcentajeAvance(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()));
                actividad.setPorcentajeFaltante(new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo()).subtract(actividad.getPorcentajeAvance()));
            }
            actividadService.actualizar(actividad);
        }
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
        if (actividadPadre.getPorcentajeAvance().floatValue() == (new BigDecimal(ValorEnum.DIVISORPORCENTAJE.getTipo())).floatValue()) {
            actividadPadre.setEstadoId(estado.getId());
        }
        actividadService.actualizar(actividadPadre);

    }

    private void calculosCronograma() {
        Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        List<Actividad> actividads = actividadService.buscar(new Actividad(null, null, null, null, null, Boolean.TRUE, null, null, null, null,
                itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOACTIVIDAD.getTipo(), TipoActividadEnum.OBJETIVO.getTipo()).getId(),
                estado.getId(), sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getCronograma()));
        BigDecimal sum = BigDecimal.ZERO;
        for (Actividad a : actividads) {
            sum = sum.add(a.getPorcentajeDuracion());
        }
        sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getCronograma().setAvance(sum.doubleValue());
        sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getCronograma().setFaltante(
                Double.parseDouble(ValorEnum.DIVISORPORCENTAJE.getTipo()) - sum.doubleValue());
        cronogramaService.actualizar(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getCronograma());
    }

    public void grabar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo");
        try {
            revisar(param);
            actividadService.actualizar(sessionActividad.getActividad());
            actualizarPorcentajes();
            calculosObjetivo();
            calculosCronograma();
            grabarDocumentos();
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("editar"), "");
            sessionActividad.setRenderedCrud(Boolean.FALSE);
            cancelarEdicion();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }

    }

    private void buscar() {
        sessionActividad.getActividadesPadre().clear();
        Actividad actividadBuscar = new Actividad();
        Item estadoRevisado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOACTIVIDAD.getTipo(), EstadoActividadEnum.REVISADO.getTipo());
        actividadBuscar.setCronogramaId(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto() != null
                ? sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getCronograma() : null);
        actividadBuscar.setTipoId(itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOACTIVIDAD.getTipo(), TipoActividadEnum.OBJETIVO.getTipo()).getId());
        actividadBuscar.setEsActivo(Boolean.TRUE);
        List<Actividad> actividades = actividadService.buscar(actividadBuscar);
        for (Actividad actividad : actividades) {
            actividad.setEstiloEstado(EstiloTreeNodeEnum.ACTIVIDADDESARROLLO.getTipo());
            if (actividad.getEstadoId().equals(estadoRevisado.getId())) {
                actividad.setEstiloEstado(EstiloTreeNodeEnum.ACTIVIDADREVISADA.getTipo());
            } else {
                actividad.setEstiloEstado(EstiloTreeNodeEnum.ACTIVIDADENVIADA.getTipo());
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
                actividad.setEstiloEstado(EstiloTreeNodeEnum.ACTIVIDADDESARROLLO.getTipo());
                if (actividad.getEstadoId().equals(estado.getId())) {
                    actividad.setEstiloEstado(EstiloTreeNodeEnum.ACTIVIDADREVISADA.getTipo());
                } else {
                    actividad.setEstiloEstado(EstiloTreeNodeEnum.ACTIVIDADENVIADA.getTipo());
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
        for (DocumentoActividad documentoActividad : sessionActividad.getActividad().getDocumentoActividadList()) {
            DocumentoActividadDTO documentoActividadDTO = new DocumentoActividadDTO(
                    documentoService.buscarPorId(new Documento(documentoActividad.getDocumentoId())), documentoActividad);
            documentoActividadDTO.getDocumento().setCatalogo(itemService.buscarPorId(documentoActividadDTO.getDocumento().getCatalogoId()).getNombre());
            File file = new File(documentoActividadDTO.getDocumento().getRuta());
            byte[] contents = cabeceraController.getUtilService().obtenerBytes(file);
            documentoActividadDTO.getDocumento().setContents(contents != null ? contents : null);
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
            cabeceraController.getUtilService().generaDocumento(new File(documentoActividadDTO.getDocumento().getRuta()),
                    documentoActividadDTO.getDocumento().getContents());
            documentoService.guardar(documentoActividadDTO.getDocumento());
            documentoActividadService.actualizar(documentoActividadDTO.getDocumentoActividad());
        }
    }
     //</editor-fold>
}
