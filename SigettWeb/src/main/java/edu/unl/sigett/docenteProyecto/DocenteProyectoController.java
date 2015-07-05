/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import edu.unl.sigett.director.DirectorDTO;
import edu.unl.sigett.documentoCarrera.DocumentoCarreraDTO;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.director.DirectorDM;
import edu.unl.sigett.documentoCarrera.DocumentoCarreraDM;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.DocumentoCarrera;
import edu.unl.sigett.enumeration.CatalogoDocumentoCarreraEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.reporte.ReporteController;
import edu.unl.sigett.reporte.ReporteFePresentacion;
import edu.unl.sigett.reporte.ReporteOficio;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.DocumentoCarreraService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import java.io.File;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author jorge-luis
 */
@Named(value = "docenteProyectoController")
@SessionScoped
public class DocenteProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionDocenteProyecto sessionDocenteProyecto;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private DirectorDM directorDM;
    @Inject
    private DocumentoCarreraDM documentoCarreraDM;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private DocumentoCarreraService documentoCarreraService;
    @EJB
    private ItemService itemService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private ConfiguracionCarreraService configuracionCarreraService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DocenteProyectoController.class.getName());

    public DocenteProyectoController() {
    }

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    /**
     * IMPRIMIR OFICIO AL DOCENTE NOTIFICANDO SU DESIGNACIÓN PARA DAR
     * PERTIENENCIA AL PROYECTO SELECCIONADO.
     *
     * @param docenteProyectoDTO
     */
    public void imprimirOficioPertinencia(DocenteProyectoDTO docenteProyectoDTO) {
        try {
            sessionDocenteProyecto.setDocenteProyectoDTO(docenteProyectoDTO);
            Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                    CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo());
            Documento documentoBuscar = documentoService.buscarPorCatalogo(new Documento(null, null, item.getId(), null, null, null, null, null));
            List<DocumentoCarrera> documentoCarreras = documentoCarreraService.buscar(new DocumentoCarrera(
                    null, documentoBuscar != null ? documentoBuscar.getId() : null, Boolean.TRUE, null, docenteProyectoDTO.getDocenteProyecto().getId()));
            if (documentoCarreras == null) {
                generarOficioPertinencia();
                return;
            }
            DocumentoCarrera documentoCarrera = !documentoCarreras.isEmpty() ? documentoCarreras.get(0) : null;
            if (documentoCarrera != null) {
                documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(
                        documentoCarrera, documentoService.buscarPorId(new Documento(documentoCarrera.getDocumentoId())), sessionProyecto.getCarreraSeleccionada()));
                File file = new File(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getRuta());
                documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
                sessionDocenteProyecto.setRenderedMediaOficio(Boolean.TRUE);
                return;
            }
            generarOficioPertinencia();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * GENERA OFICIO DE PERTINENCIA
     *
     */
    public void generarOficioPertinencia() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        ReporteController reporteController = new ReporteController();
        Carrera carrera = sessionProyecto.getCarreraSeleccionada();
        Calendar fechaActual = Calendar.getInstance();
        ConfiguracionCarrera configuracionCarrera = configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "NO"));
        if (configuracionCarrera == null) {
            return;
        }
        String numeracion = configuracionCarrera.getValor();
        String rutaReporte = request.getRealPath("/") + configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAREPORTEOFICIO.getTipo())).get(0).getValor();
        byte[] resultado = reporteController.oficio(new ReporteOficio(carrera.getLogo() != null ? carrera.getLogo() : null,
                request.getRealPath("/") + "" + configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTALOGOINSTITUCION.getTipo())).get(0).getValor(), carrera.getNombre(),
                carrera.getAreaId().getNombre(), carrera.getSigla(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "nro_oficio"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "nombre_institucion"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio") + " " + carrera.getSigla() + "-" + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "sigla_institucion"), carrera.getLugar(), cabeceraController.getUtilService().formatoFecha(
                        fechaActual.getTime(), "EEEEE dd MMMMM yyyy"), numeracion, cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "docente_carrera") + " "
                + carrera.getNombre(), sessionDocenteProyecto.getDocenteProyectoDTO().getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion()
                + "<br/>" + sessionDocenteProyecto.getDocenteProyectoDTO().getPersona().getNombres() + " " + sessionDocenteProyecto.getDocenteProyectoDTO().getPersona().getApellidos(),
                cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "coordinador_carrera") + " " + carrera.getNombre(),
                sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getDocente().getTituloDocenteId().getTituloId().getAbreviacion() + "<br/>"
                + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getNombres() + " " + sessionProyecto.
                getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getApellidos(), generarCuerpoOficioPertinencia(sessionDocenteProyecto.getDocenteProyectoDTO(),
                        carrera), "", "", cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "despedida_pertinencia"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "saludo"), "", sessionUsuario.getUsuario().getNombres().toUpperCase() + " "
                + sessionUsuario.getUsuario().getApellidos().toUpperCase(), rutaReporte));
        if (resultado == null) {
            return;
        }
        Integer numeracionNext = Integer.parseInt(numeracion) + 1;
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAOFICIO.getTipo())).get(0).getValor() + "/oficio" + numeracionNext + ".pdf";
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(), resultado, null, "pdf");
        documentoService.guardar(documento);
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(new DocumentoCarrera(
                numeracionNext + "", documento.getId(), Boolean.TRUE, carrera.getId(), sessionDocenteProyecto.getDocenteProyectoDTO().getDocenteProyecto().getId()), documento, carrera));
        documentoCarreraService.guardar(documentoCarreraDM.getDocumentoCarreraDTO().getDocumentoCarrera());
        sessionDocenteProyecto.setRenderedMediaOficio(Boolean.TRUE);
        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
        configuracionCarrera.setValor(numeracionNext + 1 + "");
        configuracionCarreraService.actualizar(configuracionCarrera);
    }

    private String generarCuerpoOficioPertinencia(final DocenteProyectoDTO docenteProyectoDTO, final Carrera carrera) {
        String tiempoMaximo = configuracionService.buscar(new Configuracion(ConfiguracionEnum.TIEMPOPERTINENCIA.getTipo())).get(0).getValor() + " "
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "dias_laborables");
        return (cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "articulos_pertinencia") + ", " + cabeceraController.
                getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "pre_plazo") + " " + tiempoMaximo + ", " + cabeceraController.
                getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "asunto_pertinencia") + ", " + cabeceraController.
                getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "pre_temaProyecto") + ": <b>" + docenteProyectoDTO.
                getDocenteProyecto().getProyectoId().getTemaActual() + "<b/> " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "pre_datosAutor") + " <b>" + sessionProyecto.getProyectoSeleccionado().getAutores().toUpperCase()
                + "</b> " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "aspirante") + " <b>"
                + carrera.getNombreTitulo().toUpperCase() + "<b/>");
    }

    /**
     * GENERAR FE DE PRESENTACIÓN
     *
     * @param docenteProyectoDTO
     */
    public void imprimirFePresentacion(DocenteProyectoDTO docenteProyectoDTO) {
        try {
            if (docenteProyectoDTO.getDocenteProyecto().getId() == null) {
                return;
            }
            sessionDocenteProyecto.setDocenteProyectoDTO(docenteProyectoDTO);
            Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.FEPRESENTACION.getTipo(),
                    CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo());
            Documento documentoBuscar = documentoService.buscarPorCatalogo(new Documento(null, null, item.getId(), null, null, null, null, null));
            List<DocumentoCarrera> documentoCarreras = documentoCarreraService.buscar(new DocumentoCarrera(
                    null, documentoBuscar != null ? documentoBuscar.getId() : null, Boolean.TRUE, null, docenteProyectoDTO.getDocenteProyecto().getId()));
            if (documentoCarreras == null) {
                generarFePertinencia();
                return;
            }
            DocumentoCarrera documentoCarrera = !documentoCarreras.isEmpty() ? documentoCarreras.get(0) : null;
            if (documentoCarrera != null) {
                documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(
                        documentoCarrera, documentoService.buscarPorId(new Documento(documentoCarrera.getDocumentoId())), sessionProyecto.getCarreraSeleccionada()));
                File file = new File(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getRuta());
                documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
                sessionDocenteProyecto.setRenderedMediaFePresentacion(Boolean.TRUE);
                return;
            }
            generarFePertinencia();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Generar oficio de pertienencia de asigancion de director de tarbajo de
     * titulacion.
     */
    public void generarFePertinencia() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        ReporteController reporteController = new ReporteController();
        Carrera carrera = sessionProyecto.getCarreraSeleccionada();
        Calendar fechaActual = Calendar.getInstance();
        String rutaReporte = request.getRealPath("/") + configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAREPORTEFEPRESENTACION.getTipo())).get(0).getValor();

        byte[] resultado = reporteController.fePresentacion(new ReporteFePresentacion(generaReferenciaFePresentacion(fechaActual, carrera),
                generaCuerpoFePresentacion(sessionDocenteProyecto.getDocenteProyectoDTO(), fechaActual, carrera),
                generaFirmasInvolucrados(sessionDocenteProyecto.getDocenteProyectoDTO(), carrera), generaFinalFePresentacion(sessionDocenteProyecto.getDocenteProyectoDTO(), fechaActual, carrera),
                sessionUsuario.getUsuario().getNombres().toUpperCase() + " " + sessionUsuario.getUsuario().getApellidos(), rutaReporte));
        if (resultado == null) {
            return;
        }
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAFEPRESENTACION.getTipo())).get(0).getValor() + "/fePertinencia"
                + sessionDocenteProyecto.getDocenteProyectoDTO().getDocenteProyecto().getId() + ".pdf";
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.FEPRESENTACION.getTipo(),
                CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(),
                resultado, null, "pdf");
        documentoService.guardar(documento);
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(new DocumentoCarrera("", documento.getId(), Boolean.TRUE,
                carrera.getId(), sessionDocenteProyecto.getDocenteProyectoDTO().getDocenteProyecto().getId()), documento, carrera));
        documentoCarreraDM.getDocumentoCarreraDTO().getDocumentoCarrera().setNumeracion("");
        documentoCarreraService.guardar(documentoCarreraDM.getDocumentoCarreraDTO().getDocumentoCarrera());
        sessionDocenteProyecto.setRenderedMediaFePresentacion(Boolean.TRUE);
        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
    }

    /**
     *
     * @param fechaActual
     * @param carrera
     * @return
     */
    private String generaReferenciaFePresentacion(Calendar fechaActual, Carrera carrera) {
        return (cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_ref_a") + " " + cabeceraController.getUtilService().
                formatoFecha(fechaActual.getTime(), "dd MMMM yyyy") + ", " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "fper_ref_b") + " " + cabeceraController.getUtilService().formatoFecha(
                        fechaActual.getTime(), "HH:mm") + ".-" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "fper_ref_c") + "<br/><br/><br/><br/>" + carrera.getAreaId().getSecretario() + "<br/> <b>"
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_ref_c") + "<b/>");
    }

    /**
     *
     * @param docenteProyectoDTO
     * @param fechaActual
     * @param carrera
     * @return
     */
    private String generaCuerpoFePresentacion(final DocenteProyectoDTO docenteProyectoDTO, final Calendar fechaActual, final Carrera carrera) {
        return ("<b>" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_cu_a").toUpperCase() + " "
                + carrera.getNombre().toUpperCase() + "<b/>.-" + cabeceraController.getUtilService().formatoFecha(fechaActual.getTime(), "dd MMMM yyyy")
                + ", " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_cu_b") + ".-" + cabeceraController.
                getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_cu_c") + " " + docenteProyectoDTO.getDocenteCarrera().
                getDocenteId().getTituloDocenteId().getTituloId().getNombre().toUpperCase() + " " + "" + docenteProyectoDTO.getPersona().
                getNombres().toUpperCase() + " " + docenteProyectoDTO.getPersona().getApellidos().toUpperCase() + " "
                + "<b>" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_cu_d") + " "
                + cabeceraController.getConfiguracionGeneralUtil().getTiempoMaximoPertinencia() + " " + "" + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "fper_cu_e") + "<b/> " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_cu_f"));
    }

    /**
     *
     * @param docenteProyectoDTO
     * @param fechaActual
     * @param carrera
     * @return
     */
    private String generaFinalFePresentacion(final DocenteProyectoDTO docenteProyectoDTO, final Calendar fechaActual, final Carrera carrera) {
        return (sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getDocente().getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase()
                + " <br/><b>" + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getNombres().toUpperCase() + " " + sessionProyecto.
                getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getApellidos().toUpperCase() + "<b/>"
                + "<p><b>" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_final_a").toUpperCase() + " " + carrera.getNombre()
                .toUpperCase() + " " + "<b/><p/><br/><p>" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "fper_final_b") + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getDocente().getTituloDocenteId().
                getTituloId().getAbreviacion().toUpperCase() + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().
                getPersona().getNombres().toUpperCase() + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getApellidos().toUpperCase() + ", "
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_final_c")
                + " " + carrera.getNombre().toUpperCase() + ", " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "fper_final_d") + "<p/><br/><p>" + carrera.getAreaId().getSecretario() + "<p/><b><p>" + cabeceraController.
                getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_final_e") + "<p/><b/><br/><p>" + carrera.getLugar() + ", " + cabeceraController.getUtilService().
                formatoFecha(fechaActual.getTime(), "dd MMMM yyyy") + ", " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "fper_final_f") + " " + cabeceraController.getUtilService().formatoFecha(fechaActual.getTime(), "HH:mm") + ".-"
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_final_g") + " " + docenteProyectoDTO.
                getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase() + " " + ""
                + docenteProyectoDTO.getPersona().getNombres().toUpperCase() + " " + docenteProyectoDTO.getPersona().getApellidos().toUpperCase() + " "
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_final_h") + ".<p/>");
    }

    /**
     *
     * @param docenteProyectoDTO
     * @param carrera
     * @return
     */
    private String generaFirmasInvolucrados(final DocenteProyectoDTO docenteProyectoDTO, final Carrera carrera) {
        return (docenteProyectoDTO.getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase() + " " + docenteProyectoDTO.
                getPersona().getNombres().toUpperCase() + " " + docenteProyectoDTO.getPersona().getApellidos().toUpperCase() + "</p><p>" + cabeceraController
                .getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_firma_a") + "<p/><br/><br/><p>" + carrera.getAreaId().getSecretario().toUpperCase() + "<p/>"
                + "<b><p>" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fper_firma_b") + "<p/><b/>");
    }

    public void cancelarImprimirOficio() {
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO());
        sessionDocenteProyecto.setDocenteProyectoDTO(new DocenteProyectoDTO());
        sessionDocenteProyecto.setRenderedMediaOficio(Boolean.FALSE);
    }

    public void cancelarImprimirFe() {
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO());
        sessionDocenteProyecto.setDocenteProyectoDTO(new DocenteProyectoDTO());
        sessionDocenteProyecto.setRenderedMediaFePresentacion(Boolean.FALSE);
    }

    public void cancelarDocentesDisponibles() {
        this.directorDM.getDirectoresDTO().clear();
        this.directorDM.getFilterDirectoresDTO().clear();
        sessionDocenteProyecto.setRenderedPnlDocentesDisponibles(Boolean.FALSE);
        RequestContext.getCurrentInstance().execute("PF('dlgDocentesDisponibles').hide()");
    }

    /**
     * SUBIR Y ACTUALIZAR DOCUMENTO YA SEA DE OFICIO, FE DE PRESENTACIÓN
     * PERTINENCIA
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setContents(event.getFile().getContents());
            Long size = event.getFile().getSize();
            documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setTamanio(size.doubleValue());
            cabeceraController.getUtilService().generaDocumento(new File(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getRuta()),
                    documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getContents());
            documentoService.actualizar(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.uploaded"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        }
    }

    /**
     * DETERMINAR SI SE PUEDEN AGREGAR MAS DE 2 DOCENTES AL PROYECTO
     * SELECCIONADO
     *
     * @return
     */
    public Boolean permitirAgregarDocente() {
        String val = configuracionService.buscar(new Configuracion(ConfiguracionEnum.AGREGARDOCENTEPROYECTO.getTipo())).get(0).getValor();
        if (sessionProyecto.getDocentesProyectoDTO().isEmpty()) {
            return Boolean.TRUE;
        }
        if (val.equals(ValorEnum.SI.getTipo())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * AGREGAR DIRECTOR COMO DOCENTE DEL PROYECTO SELECCIONADO
     *
     * @param directorDTO
     */
    public void agregar(DirectorDTO directorDTO) {
        Calendar fecha = Calendar.getInstance();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                return;
            }
            if (!permitirAgregarDocente()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe") + " "
                        + bundle.getString("lbl.docente"), "");
                return;
            }
            DocenteProyectoDTO dp = devuelveDocenteProyecto(directorDTO);
            if (dp != null) {
                return;
            }
            DocenteProyecto docenteProyecto = new DocenteProyecto(sessionProyecto.getProyectoSeleccionado(), fecha.getTime(),
                    directorDTO.getDocenteCarrera().getId(), Boolean.TRUE, null);
            DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, directorDTO.getPersona(),
                    directorDTO.getDocenteCarrera());
            sessionProyecto.getDocentesProyectoDTO().add(docenteProyectoDTO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " "
                    + bundle.getString("lbl.msm_agregar"), "");
            cancelarDocentesDisponibles();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private DocenteProyectoDTO devuelveDocenteProyecto(DirectorDTO directorDTO) {
        DocenteProyectoDTO dTO = null;
        try {
            for (DocenteProyectoDTO docenteProyectoDTO : sessionProyecto.getDocentesProyectoDTO()) {
                if (docenteProyectoDTO.getDocenteCarrera().getDocenteId().equals(directorDTO.getDocenteCarrera().getDocenteId())) {
                    dTO = docenteProyectoDTO;
                    break;
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return dTO;
    }

    public void remover(DocenteProyectoDTO docenteProyectoDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            if (docenteProyectoDTO.getDocenteProyecto().getId() != null) {
                docenteProyectoDTO.getDocenteProyecto().setEsActivo(Boolean.FALSE);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                sessionProyecto.getDocentesProyectoDTO().remove(docenteProyectoDTO);
            } else {
                sessionProyecto.getDocentesProyectoDTO().remove(docenteProyectoDTO);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
            }
            sessionProyecto.setFilterDocentesProyectoDTO(sessionProyecto.getDocentesProyectoDTO());
        } catch (Exception e) {
        }
    }

//</editor-fold>
}
