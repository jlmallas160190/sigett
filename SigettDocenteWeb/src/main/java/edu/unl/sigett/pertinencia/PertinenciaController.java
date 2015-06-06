/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.pertinencia;

import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.docenteProyecto.DocenteProyectoDM;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.entity.DocumentoCarrera;
import edu.unl.sigett.entity.Pertinencia;
import edu.unl.sigett.enumeration.CatalogoDocumentoCarreraEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.reporte.ReporteController;
import edu.unl.sigett.service.DocumentoCarreraService;
import edu.unl.sigett.service.PertinenciaService;
import edu.unl.sigett.service.ProyectoService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.jlmallas.seguridad.dao.LogDao;

/**
 *
 * @author jorge-luis
 */
@Named(value = "pertinenciaController")
@SessionScoped
public class PertinenciaController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private PertinenciaDM pertinenciaDM;
    @Inject
    private DocenteProyectoDM docenteProyectoDM;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private PertinenciaService pertinenciaService;
    @EJB
    private LogDao logDao;
    @EJB
    private ItemService itemService;
    @EJB
    private ProyectoService proyectoService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private DocumentoCarreraService documentoCarreraService;
    @EJB
    private CarreraService carreraService;
    @EJB
    private ConfiguracionDao configuracionDao;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(PertinenciaController.class.getName());

    public PertinenciaController() {
    }

    public void preRenderView() {
        this.buscar();
    }

    public void crear() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (docenteProyectoDM.getEstadoActualProyecto().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
                    || docenteProyectoDM.getEstadoActualProyecto().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {

                pertinenciaDM.setPertinencia(new Pertinencia(null, null, "S/N", Boolean.FALSE, Boolean.TRUE,
                        docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto()));
                pertinenciaDM.setRenderedDialogoCrud(Boolean.TRUE);
            } else {
                this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString(
                        "lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void editar(Pertinencia pertinencia) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (docenteProyectoDM.getEstadoActualProyecto().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
                    || docenteProyectoDM.getEstadoActualProyecto().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                pertinenciaDM.setPertinencia(pertinencia);
                pertinenciaDM.setRenderedDialogoCrud(Boolean.TRUE);
            } else {
                this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString(
                        "lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void grabar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!(docenteProyectoDM.getEstadoActualProyecto().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
                    || docenteProyectoDM.getEstadoActualProyecto().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo()))) {
                return;
            }
            if (pertinenciaDM.getPertinencia().getId() == null) {
                this.pertinenciaService.guardar(pertinenciaDM.getPertinencia());
                logDao.create(logDao.crearLog("Pertinencia", pertinenciaDM.getPertinencia().getId() + "", "CREAR", "|Docente= "
                        + pertinenciaDM.getPertinencia().getDocenteProyectoId().getDocenteCarreraId() + "|Proyecto= " + pertinenciaDM.getPertinencia().
                        getDocenteProyectoId().getProyectoId().getId() + "|Fecha= " + pertinenciaDM.getPertinencia().getFecha()
                        + "|EsActivo= " + pertinenciaDM.getPertinencia().getEsActivo(), docenteUsuarioDM.getDocenteUsuarioDTO().getUsuario()));
                actualizarEstadoProyecto();
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                pertinenciaDM.setPertinencia(new Pertinencia());
                pertinenciaDM.setRenderedDialogoCrud(Boolean.FALSE);
                return;
            }
            this.pertinenciaService.actualizar(pertinenciaDM.getPertinencia());
            logDao.create(logDao.crearLog("Pertinencia", pertinenciaDM.getPertinencia().getId() + "", "CREAR", "|Docente= "
                    + pertinenciaDM.getPertinencia().getDocenteProyectoId().getDocenteCarreraId() + "|Proyecto= " + pertinenciaDM.getPertinencia().
                    getDocenteProyectoId().getProyectoId().getId() + "|Fecha= " + pertinenciaDM.getPertinencia().getFecha()
                    + "|EsActivo= " + pertinenciaDM.getPertinencia().getEsActivo(), docenteUsuarioDM.getDocenteUsuarioDTO().getUsuario()));
            actualizarEstadoProyecto();
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
            pertinenciaDM.setPertinencia(new Pertinencia());
            pertinenciaDM.setRenderedDialogoCrud(Boolean.FALSE);
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }

    }

    /**
     * ACTUALIZAR ESTADO DE PROYECTO
     */
    private void actualizarEstadoProyecto() {
        if (pertinenciaDM.getPertinencia().getEsAceptado()) {
            Item pertinente = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.PERTINENTE.getTipo());
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().setEstadoProyectoId(pertinente.getId());
        } else {
            Item inicio = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.INICIO.getTipo());
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().setEstadoProyectoId(inicio.getId());
        }
        proyectoService.actualizar(docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId());
    }

    public void cancelar() {
        this.pertinenciaDM.setPertinencia(new Pertinencia());
        this.pertinenciaDM.setRenderedDialogoCrud(Boolean.FALSE);
    }

    public void remover(Pertinencia pertinencia) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!(docenteProyectoDM.getEstadoActualProyecto().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
                    || docenteProyectoDM.getEstadoActualProyecto().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo()))) {
                return;
            }
            pertinencia.setEsActivo(Boolean.FALSE);
            pertinenciaService.actualizar(pertinencia);
            logDao.create(logDao.crearLog("Pertinencia", pertinencia.getId() + "", "EDITAR", "|Docente= "
                    + pertinencia.getDocenteProyectoId().getDocenteCarreraId() + "|Proyecto= " + pertinencia.getDocenteProyectoId().getProyectoId().getId()
                    + "|Fecha= " + pertinencia.getFecha() + "|EsActivo= " + pertinencia.getEsActivo(),
                    docenteUsuarioDM.getDocenteUsuarioDTO().getUsuario()));
            if (!existePertinencias()) {
                Item inicio = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.INICIO.getTipo());
                docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().setEstadoProyectoId(inicio.getId());
                proyectoService.actualizar(docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId());
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    /**
     * DETERMINAR SI EXISTEN PERTINENCIAS; SI NO EXISTIERA AUTOMATICAMENTE EL
     * PROYECTO TOMA EL ESTADO DE INICIO
     *
     * @return
     */
    public Boolean existePertinencias() {
        List<Pertinencia> pertinencias = pertinenciaService.buscar(new Pertinencia(
                null, null, null, null, Boolean.TRUE, docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto()));
        if (pertinencias == null) {
            return Boolean.FALSE;
        }
        if (pertinencias.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void buscar() {
        this.pertinenciaDM.getPertinencias().clear();
        this.pertinenciaDM.getFilterPertinencias().clear();
        try {
            List<Pertinencia> pertinencias = pertinenciaService.buscar(new Pertinencia(null, null, null, null, Boolean.TRUE,
                    docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto()));
            if (pertinencias == null) {
                return;
            }
            this.pertinenciaDM.getPertinencias().addAll(pertinencias);
            this.pertinenciaDM.setFilterPertinencias(this.pertinenciaDM.getPertinencias());
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void imprimirOficio(Pertinencia pertinencia) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (pertinencia.getId() != null) {
                Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOINFORME.getTipo(),
                        CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo());
                this.pertinenciaDM.setRenderedDialogoOficio(Boolean.TRUE);
                Documento documentoBuscar = documentoService.buscarPorCatalogo(new Documento(null, null, item.getId(), null, null, null, null, null));
                List<DocumentoCarrera> documentoCarreras = documentoCarreraService.buscar(new DocumentoCarrera(
                        null, documentoBuscar != null ? documentoBuscar.getId() : null, Boolean.TRUE, null, pertinencia.getId()));
                if (documentoCarreras == null) {
//                    generarOficioPertinencia(docenteProyectoDTO);
                    return;
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    private void generarInformePertinencia(Pertinencia pertinencia) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        ReporteController reporteController = new ReporteController();
        Carrera carrera = carreraService.find(docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteCarrera().getCarreraId());
        Calendar fechaActual = Calendar.getInstance();

        String rutaReporte = request.getRealPath("/") + configuracionDao.buscar(new Configuracion(
                ConfiguracionEnum.RUTAINFORMEPERTINENCIA.getTipo())).get(0).getValor();

        byte[] resultado = reporteController.informePertinencia(new ReporteInformePertinencia(null, carrera.getLogo() != null ? carrera.getLogo() : null,
                request.getRealPath("/") + "" + configuracionDao.buscar(new Configuracion(ConfiguracionEnum.RUTALOGOINSTITUCION.getTipo())).get(0).getValor(),
                carrera.getNombre(), carrera.getAreaId().getNombre(), carrera.getSigla(), null, cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "nombre_institucion"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio") + " " + carrera.getSigla() + "-" + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "sigla_institucion"), carrera.getLugar(), cabeceraController.getUtilService().formatoFecha(
                        fechaActual.getTime(), "EEEEE dd MMMMM yyyy"), null, cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "docente_carrera") + " "
                + carrera.getNombre(), docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion() + "<br/>"
                + docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getPersona().getNombres() + " " + docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getPersona().getApellidos(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "coordinador_carrera") + " " + carrera.getNombre(), docenteProyectoDM.getCoordinadorPeriodoDTO().getDocente().getTituloDocenteId().
                getTituloId().getAbreviacion() + "<br/>" + docenteProyectoDM.getCoordinadorPeriodoDTO().getPersona().getNombres() + " " + docenteProyectoDM.getCoordinadorPeriodoDTO().getPersona().getApellidos(), rutaReporte,
                rutaReporte, rutaReporte, rutaReporte, rutaReporte, rutaReporte, rutaReporte, rutaReporte));
        if (resultado == null) {
            return;
        }
//        Integer numeracionNext = Integer.parseInt(numeracion) + 1;
//        String ruta = configuracionDao.buscar(new Configuracion(ConfiguracionEnum.RUTAOFICIO.getTipo())).get(0).getValor() + "/oficio" + docenteProyectoDTO.getDocenteProyecto().getId() + ".pdf";
//        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
//                CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(), resultado, null, "pdf");
//        documentoService.guardar(documento);
//        sessionDocenteProyecto.setDocumentoCarreraDTO(new DocumentoCarreraDTO(new DocumentoCarrera(
//                numeracionNext + "", documento.getId(), Boolean.TRUE, carrera.getId(), docenteProyectoDTO.getDocenteProyecto().getId()), documento, carrera));
//        documentoCarreraService.guardar(sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumentoCarrera());
//        sessionDocenteProyecto.setRenderedMediaOficio(Boolean.TRUE);
//        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
  
    }

}
