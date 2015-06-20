/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.prorroga;

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
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import edu.unl.sigett.documentoCarrera.DocumentoCarreraDM;
import edu.unl.sigett.documentoCarrera.DocumentoCarreraDTO;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.DocumentoCarrera;
import edu.unl.sigett.entity.Prorroga;
import edu.unl.sigett.enumeration.CatalogoDocumentoCarreraEnum;
import edu.unl.sigett.enumeration.ConfiguracionProyectoEnum;
import edu.unl.sigett.enumeration.EstadoDirectorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.reporte.ReporteController;
import edu.unl.sigett.reporte.ReporteOficio;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.CronogramaService;
import edu.unl.sigett.service.DocumentoCarreraService;
import edu.unl.sigett.service.ProrrogaService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.jlmallas.seguridad.service.UsuarioService;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "prorrogaController")
@SessionScoped
public class ProrrogaController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionProrroga sessionProrroga;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private DocumentoCarreraDM documentoCarreraDM;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ProrrogaService prorrogaService;
    @EJB
    private CronogramaService cronogramaService;
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private ItemService itemService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private DocumentoCarreraService documentoCarreraService;
    @EJB
    private ConfiguracionCarreraService configuracionCarreraService;

    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(ProrrogaController.class.getName());

    public ProrrogaController() {
    }

    public void preRenderView() {
        this.rendereBuscar();
        this.buscar();
        this.renderedCrear();
        this.renderedEditar();
        this.renderedEliminar();
    }

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    public void crear() {
        try {
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                return;
            }
            Calendar fechaActual = Calendar.getInstance();
            if (fechaActual.getTime().before(sessionProyecto.getCronograma().getFechaProrroga())) {
                return;
            }
            sessionProrroga.setRenderedCrud(Boolean.TRUE);
            sessionProrroga.setProrroga(new Prorroga(null, null, "S/N", null, Boolean.TRUE, Boolean.FALSE, "S/N", sessionProyecto.getCronograma()));
            RequestContext.getCurrentInstance().execute("PF('dlgEditarProrroga').show()");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void editar(Prorroga prorroga) {
        try {
            sessionProrroga.setProrroga(prorroga);
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    /**
     * CALCULAR FECHA DE PRORROGA
     */
    public void calculaFechas() {
        sessionProyecto.setCronograma(sessionProyecto.getProyectoSeleccionado().getCronograma());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        Double porcentejeProrroga = Double.parseDouble(configuracionService.buscar(new Configuracion(
                ConfiguracionEnum.PORCENTAJEPRORROGA.getTipo())).get(0).getValor());
        Double resultado = (sessionProyecto.getCronograma().getDuracion() / ((1 / porcentejeProrroga) * 100));
        if (sessionProrroga.getProrroga().getFecha() == null) {
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas"), "");
            return;
        }
        if (sessionProrroga.getProrroga().getFecha().before(sessionProyecto.getCronograma().getFechaProrroga())) {
            sessionProrroga.getProrroga().setFecha(null);
            sessionProrroga.getProrroga().setFechaInicial(null);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas"), "");
            return;
        }
        Double duracionDias = cabeceraController.getUtilService().calculaDuracion(sessionProyecto.getCronograma().getFechaProrroga(),
                sessionProrroga.getProrroga().getFecha(), Integer.parseInt(ValorEnum.DIASSEMANA.getTipo()) - calculaDiasSemanaTrabajoProyecto());
        if (duracionDias > resultado) {
            sessionProrroga.getProrroga().setFecha(null);
            sessionProrroga.getProrroga().setFechaInicial(null);
            sessionProyecto.getCronograma().setDuracion(0.0);
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString(
                    "lbl.msm_fechas_cronograma_limit") + ".", "");
            return;
        }
        sessionProrroga.getProrroga().setFechaInicial(sessionProyecto.getCronograma().getFechaProrroga());
        sessionProyecto.getCronograma().setDuracion(sessionProyecto.getCronograma().getDuracion() + duracionDias);
    }

    public void grabar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                return;
            }
            Calendar fechaActual = Calendar.getInstance();
            if (fechaActual.getTime().before(sessionProyecto.getCronograma().getFechaProrroga())) {
                return;
            }
            if (sessionProrroga.getProrroga().getEsAceptado()) {
                sessionProyecto.getCronograma().setFechaProrroga(sessionProrroga.getProrroga().getFecha());
            }
            if (sessionProrroga.getProrroga().getId() == null) {
                prorrogaService.guardar(sessionProrroga.getProrroga());
                cronogramaService.actualizar(sessionProyecto.getCronograma());
                this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar") + ".", "");
                cancelarEdicion();
                return;
            }
            prorrogaService.actualizar(sessionProrroga.getProrroga());
            cronogramaService.actualizar(sessionProyecto.getCronograma());
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar") + ".", "");
            sessionProrroga.setProrroga(new Prorroga());
            cancelarEdicion();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    private Integer calculaDiasSemanaTrabajoProyecto() {
        Integer dias = 0;
        for (ConfiguracionProyecto cf : sessionProyecto.getProyectoSeleccionado().getConfiguracionProyectoList()) {
            if (cf.getCodigo().equals(ConfiguracionProyectoEnum.DIASSEMANA.getTipo())) {
                dias = Integer.parseInt(cf.getValor());
                break;
            }
        }
        return dias;
    }

    public void eliminar(Prorroga prorroga) {
        if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
            return;
        }
        Calendar fechaActual = Calendar.getInstance();
        if (fechaActual.getTime().before(sessionProyecto.getCronograma().getFechaProrroga())) {
            return;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        prorroga.setEsActivo(Boolean.FALSE);
        prorrogaService.actualizar(prorroga);
        this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar") + ".", "");
        Prorroga resultado = prorrogaService.obtenerPorFechaMayor(new Prorroga(
                null, null, null, null, Boolean.TRUE, null, null, sessionProyecto.getCronograma()));
        if (resultado == null) {
            return;
        }
        sessionProyecto.getCronograma().setFechaProrroga(resultado.getFecha());
        cronogramaService.actualizar(sessionProyecto.getCronograma());
    }

    public void cancelarEdicion() {
        this.sessionProrroga.setRenderedCrud(Boolean.FALSE);
        this.sessionProrroga.setProrroga(new Prorroga());
    }

    private void buscar() {
        if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
            return;
        }
        sessionProrroga.getProrrogas().clear();
        sessionProrroga.getFilterProrrogas().clear();
        sessionProrroga.getProrrogas().addAll(prorrogaService.buscar(new Prorroga(
                null, null, null, null, Boolean.TRUE, null, null, sessionProyecto.getCronograma())));
        sessionProrroga.setFilterProrrogas(sessionProrroga.getProrrogas());
    }

    /**
     * IMPRIMIR OFICIO AL DOCENTE NOTIFICANDO PRORROGA.
     *
     * @param prorroga
     */
    public void imprimirOficio(Prorroga prorroga) {
        try {
            sessionProrroga.setProrroga(prorroga);
            Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                    CatalogoDocumentoCarreraEnum.PRORROGA.getTipo());
            Documento documentoBuscar = documentoService.buscarPorCatalogo(new Documento(null, null, item.getId(), null, null, null, null, null));
            List<DocumentoCarrera> documentoCarreras = documentoCarreraService.buscar(new DocumentoCarrera(
                    null, documentoBuscar != null ? documentoBuscar.getId() : null, Boolean.TRUE, null, prorroga.getId()));
            if (documentoCarreras == null) {
                generarOficio(prorroga);
                return;
            }
            DocumentoCarrera documentoCarrera = !documentoCarreras.isEmpty() ? documentoCarreras.get(0) : null;
            if (documentoCarrera != null) {
                documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(
                        documentoCarrera, documentoService.buscarPorId(new Documento(documentoCarrera.getDocumentoId())), sessionProyecto.getCarreraSeleccionada()));
                File file = new File(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getRuta());
                documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
                sessionProrroga.setRenderedMediaOficio(Boolean.TRUE);
                return;
            }
            generarOficio(prorroga);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * GENERA OFICIO DE PROORROGA
     *
     * @param docenteProyectoDTO
     */
    private void generarOficio(final Prorroga prorroga) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        ReporteController reporteController = new ReporteController();
        Carrera carrera = sessionProyecto.getCarreraSeleccionada();
        Calendar fechaActual = Calendar.getInstance();
        ConfiguracionCarrera configuracionCarrera = configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "NO"));
        if (configuracionCarrera == null) {
            return;
        }
        DirectorProyectoDTO directorProyectoDTO = obtenerDirectorActual();
        if (directorProyectoDTO == null) {
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
                + carrera.getNombre(),
                directorProyectoDTO.getDirectorDTO().getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion() + "<br/>"
                + directorProyectoDTO.getDirectorDTO().getPersona().getNombres() + " " + directorProyectoDTO.getDirectorDTO().getPersona().getApellidos(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "coordinador_carrera") + " " + carrera.getNombre(), sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getDocente().getTituloDocenteId().
                getTituloId().getAbreviacion() + "<br/>" + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getNombres() + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getApellidos(),
                generarCuerpoOficio(), "", "", cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_prorroga_despedida"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_prorroga_saludo"), "", sessionUsuario.getUsuario().getNombres().toUpperCase() + " "
                + sessionUsuario.getUsuario().getApellidos().toUpperCase(), rutaReporte));
        if (resultado == null) {
            return;
        }
        Integer numeracionNext = Integer.parseInt(numeracion) + 1;
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAOFICIO.getTipo())).get(0).getValor() + "/oficio" + numeracionNext + ".pdf";
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                CatalogoDocumentoCarreraEnum.PRORROGA.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(), resultado, null, "pdf");
        documentoService.guardar(documento);
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(new DocumentoCarrera(
                numeracionNext + "", documento.getId(), Boolean.TRUE, carrera.getId(), prorroga.getId()), documento, carrera));
        documentoCarreraService.guardar(documentoCarreraDM.getDocumentoCarreraDTO().getDocumentoCarrera());
        sessionProrroga.setRenderedMediaOficio(Boolean.TRUE);
        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
        configuracionCarrera.setValor(numeracionNext + 1 + "");
        configuracionCarreraService.actualizar(configuracionCarrera);
    }

    private String generarCuerpoOficio() {
        return (cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_prorroga_cu_a") + " " + sessionProyecto.
                getProyectoSeleccionado().getAutores().toUpperCase() + ", " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "oficio_prorroga_cu_b") + " <b>" + sessionProyecto.getProyectoSeleccionado().getTemaActual() + "<b/>, " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_prorroga_cu_c"));
    }

    private DirectorProyectoDTO obtenerDirectorActual() {
        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADODIRECTOR.getTipo(), EstadoDirectorEnum.RENUNCIADO.getTipo());
        for (DirectorProyectoDTO directorProyectoDTO : sessionProyecto.getDirectoresProyectoDTO()) {
            if (directorProyectoDTO.getDirectorProyecto().getEstadoDirectorId().equals(estadoRenunciado.getId())) {
                continue;
            }
            return directorProyectoDTO;
        }
        return null;
    }

    public void cancelarImprimirOficio() {
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO());
        sessionProrroga.setProrroga(new Prorroga());
        sessionProrroga.setRenderedMediaOficio(Boolean.FALSE);
    }

    /**
     * IMPRIMIR OFICIO AL AUTOR DEL PROYECTO SELECCIONADO NOTIFICANDO SI EL
     * DIRECTOR ACEPTA O NO LA PRORROGA
     *
     * @param prorroga
     */
    public void imprimirOficioInf(Prorroga prorroga) {
        try {
            sessionProrroga.setProrroga(prorroga);
            Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                    CatalogoDocumentoCarreraEnum.PRORROGAAUTOR.getTipo());
            Documento documentoBuscar = documentoService.buscarPorCatalogo(new Documento(null, null, item.getId(), null, null, null, null, null));
            List<DocumentoCarrera> documentoCarreras = documentoCarreraService.buscar(new DocumentoCarrera(
                    null, documentoBuscar != null ? documentoBuscar.getId() : null, Boolean.TRUE, null, prorroga.getId()));
            if (documentoCarreras == null) {
                generarOficioInf(prorroga);
                return;
            }
            DocumentoCarrera documentoCarrera = !documentoCarreras.isEmpty() ? documentoCarreras.get(0) : null;
            if (documentoCarrera != null) {
                documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(
                        documentoCarrera, documentoService.buscarPorId(new Documento(documentoCarrera.getDocumentoId())), sessionProyecto.getCarreraSeleccionada()));
                File file = new File(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getRuta());
                documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
                sessionProrroga.setRenderedMediaOficioInf(Boolean.TRUE);
                return;
            }
            generarOficioInf(prorroga);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * GENERA OFICIO DE PROORROGA
     *
     * @param docenteProyectoDTO
     */
    private void generarOficioInf(final Prorroga prorroga) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        ReporteController reporteController = new ReporteController();
        Carrera carrera = sessionProyecto.getCarreraSeleccionada();
        Calendar fechaActual = Calendar.getInstance();
        ConfiguracionCarrera configuracionCarrera = configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "NO"));
        if (configuracionCarrera == null) {
            return;
        }
        DirectorProyectoDTO directorProyectoDTO = obtenerDirectorActual();
        if (directorProyectoDTO == null) {
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
                        fechaActual.getTime(), "EEEEE dd MMMMM yyyy"), numeracion, cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_inf_prorroga_cargoDestinatario") + " "
                + carrera.getNombre(), sessionProyecto.getProyectoSeleccionado().getAutores().toUpperCase(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "coordinador_carrera") + " " + carrera.getNombre(), sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getDocente().getTituloDocenteId().
                getTituloId().getAbreviacion() + "<br/>" + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getNombres() + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getApellidos(),
                generarCuerpoOficioInf(), "", "", cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_prorroga_despedida"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_prorroga_saludo"), "", sessionUsuario.getUsuario().getNombres().toUpperCase() + " "
                + sessionUsuario.getUsuario().getApellidos().toUpperCase(), rutaReporte));
        if (resultado == null) {
            return;
        }
        Integer numeracionNext = Integer.parseInt(numeracion) + 1;
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAOFICIO.getTipo())).get(0).getValor() + "/oficio" + numeracionNext + ".pdf";
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                CatalogoDocumentoCarreraEnum.PRORROGAAUTOR.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(), resultado, null, "pdf");
        documentoService.guardar(documento);
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(new DocumentoCarrera(
                numeracionNext + "", documento.getId(), Boolean.TRUE, carrera.getId(), prorroga.getId()), documento, carrera));
        documentoCarreraService.guardar(documentoCarreraDM.getDocumentoCarreraDTO().getDocumentoCarrera());
        sessionProrroga.setRenderedMediaOficioInf(Boolean.TRUE);
        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
        configuracionCarrera.setValor(numeracionNext + 1 + "");
        configuracionCarreraService.actualizar(configuracionCarrera);
    }

    private String generarCuerpoOficioInf() {
        Calendar fechaActual = Calendar.getInstance();
        String resolucion = cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_inf_prorroga_cu_negar");
        int tiempo = sessionProrroga.getProrroga().getFecha().getMonth() - sessionProrroga.getProrroga().getFechaInicial().getMonth();
        String meses = "";
        if (sessionProrroga.getProrroga().getEsAceptado()) {
            resolucion = cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_inf_prorroga_cu_aceptar");
            meses = tiempo + "" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_inf_prorroga_cu_e");
        }
        return (cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_inf_prorroga_cu_a") + ", <b>" + resolucion + "<b/>"
                + " " + meses + " " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_inf_prorroga_cu_b") + " "
                + sessionProyecto.getProyectoSeleccionado().getTemaActual() + "<b/>, " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_prorroga_cu_c") + " " + cabeceraController.getUtilService().formatoFecha(
                        fechaActual.getTime(), "EEEEE dd MMMMM yyyy") + ", " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_inf_prorroga_cu_d"));
    }

    public void cancelarImprimirOficioInf() {
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO());
        sessionProrroga.setProrroga(new Prorroga());
        sessionProrroga.setRenderedMediaOficioInf(Boolean.FALSE);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RENDERED">
    public void renderedEditar() {
        try {
            sessionProrroga.setRenderedEditar(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                return;
            }
            Calendar fechaActual = Calendar.getInstance();
            if (fechaActual.getTime().before(sessionProyecto.getCronograma().getFechaProrroga())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "editar_prorroga").trim());
            if (tienePermiso == 1) {
                sessionProrroga.setRenderedEditar(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }

    public void renderedCrear() {
        try {
            sessionProrroga.setRenderedCrear(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                return;
            }
            Calendar fechaActual = Calendar.getInstance();
            if (fechaActual.getTime().before(sessionProyecto.getCronograma().getFechaProrroga())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "crear_prorroga").trim());
            if (tienePermiso == 1) {
                sessionProrroga.setRenderedCrear(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }

    public void renderedEliminar() {
        try {
            sessionProrroga.setRenderedEliminar(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                return;
            }
            Calendar fechaActual = Calendar.getInstance();
            if (fechaActual.getTime().before(sessionProyecto.getCronograma().getFechaProrroga())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "eliminar_prorroga").trim());
            if (tienePermiso == 1) {
                sessionProrroga.setRenderedEliminar(Boolean.TRUE);
            }
        } catch (Exception ex) {
        }
    }

    public void rendereBuscar() {
        try {
            sessionProrroga.setRenderedBuscar(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                return;
            }
            sessionProrroga.setRenderedBuscar(Boolean.TRUE);
        } catch (Exception e) {
        }
    }
    //</editor-fold>
}
