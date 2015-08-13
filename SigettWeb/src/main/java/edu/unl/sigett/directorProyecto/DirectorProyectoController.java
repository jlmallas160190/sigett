/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import edu.unl.sigett.director.DirectorDTO;
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
import edu.unl.sigett.autorProyecto.AutorProyectoDTO;
import edu.unl.sigett.director.DirectorDM;
import edu.unl.sigett.documentoCarrera.DocumentoCarreraDM;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.DocumentoCarrera;
import edu.unl.sigett.entity.Renuncia;
import edu.unl.sigett.entity.RenunciaDirector;
import edu.unl.sigett.enumeration.CatalogoDocumentoCarreraEnum;
import edu.unl.sigett.enumeration.EstadoDirectorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.renunciaDirectorProyecto.SessionRenunciaDirectorProyecto;
import edu.unl.sigett.reporte.ReporteController;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.DocumentoCarreraService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.documentoCarrera.DocumentoCarreraDTO;
import edu.unl.sigett.renuncia.SessionRenuncia;
import edu.unl.sigett.reporte.ReporteFePresentacion;
import edu.unl.sigett.reporte.ReporteOficio;
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
import org.jlmallas.seguridad.service.UsuarioService;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "directorProyectoController")
@SessionScoped
public class DirectorProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private DirectorProyectoDM directorProyectoDM;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionRenunciaDirectorProyecto sessionRenunciaDirectorProyecto;
    @Inject
    private DirectorDM directorDM;
    @Inject
    private DocumentoCarreraDM documentoCarreraDM;
    @Inject
    private SessionRenuncia sessionRenuncia;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/ComunService/DocumentoServiceImplement!com.jlmallas.comun.service.DocumentoService")
    private DocumentoService documentoService;
    @EJB(lookup = "java:global/SigettService/DocumentoCarreraServiceImplement!edu.unl.sigett.service.DocumentoCarreraService")
    private DocumentoCarreraService documentoCarreraService;
    @EJB(lookup = "java:global/SigettService/ConfiguracionCarreraServiceImplement!edu.unl.sigett.service.ConfiguracionCarreraService")
    private ConfiguracionCarreraService configuracionCarreraService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DirectorProyectoController.class.getName());

    public DirectorProyectoController() {
    }

    public void preRenderView() {
        this.renderedBuscar();
        this.renderedBuscarDirectorDisponible();
        this.renderedEliminar();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    private void renderedBuscarDirectorDisponible() {
        try {
            directorProyectoDM.setRenderedBuscarDirectorDisponible(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.PERTINENTE.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SEGUIMIENTO.getTipo()))) {
                return;
            }
            int permiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "seleccionar_director_proyecto").trim());
            if (permiso == 1) {
                directorProyectoDM.setRenderedBuscarDirectorDisponible(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
        }

    }

    private void renderedBuscar() {
        try {
            directorProyectoDM.setRenderedBuscar(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.INICIO.getTipo())) {
                directorProyectoDM.setRenderedBuscar(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
        }

    }

    private void renderedEliminar() {
        try {
            directorProyectoDM.setRenderedEliminar(Boolean.FALSE);
            if (!(sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.PERTINENTE.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SEGUIMIENTO.getTipo()))) {
                return;
            }
            int permiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "eliminar_director_proyecto").trim());
            if (permiso == 1) {
                directorProyectoDM.setRenderedEliminar(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
        }

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CRUD">
    /**
     * DETERMINAR SI SE PUEE AGREGAR MAS DE DOS DIRECTORES A UN PROYECTO
     *
     * @return
     */
    private Boolean permiteAgregarDirector() {
        String value = configuracionService.buscar(new Configuracion(ConfiguracionEnum.AGREGARDIRECTORPROYECTO.getTipo())).get(0).getValor();
        if (sessionProyecto.getDirectoresProyectoDTO().isEmpty()) {
            return Boolean.TRUE;
        }
        if (value.equals(ValorEnum.SI.getTipo())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void agregar(final DirectorDTO directorDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!(sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.PERTINENTE.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SEGUIMIENTO.getTipo()))) {
                return;
            }
            if (!permiteAgregarDirector()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe") + " "
                        + bundle.getString("lbl.director"), "");
                return;
            }
            DirectorProyectoDTO dp = devuelveDirectorProyecto(directorDTO);
            if (dp != null) {
                return;
            }
            Item estadoDirector = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADODIRECTOR.getTipo(), EstadoDirectorEnum.INICIO.getTipo());
            DirectorProyectoDTO directorProyectoDTO = new DirectorProyectoDTO(new DirectorProyecto(null, sessionProyecto.getCronograma().getFechaInicio(),
                    sessionProyecto.getCronograma().getFechaProrroga(), sessionProyecto.getProyectoSeleccionado(), estadoDirector.getId(),
                    directorDTO.getDirector()), directorDTO);
            sessionProyecto.getDirectoresProyectoDTO().add(directorProyectoDTO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.director") + " "
                    + bundle.getString("lbl.msm_agregar"), "");
            cancelarDirectoresDisponibles();
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private DirectorProyectoDTO devuelveDirectorProyecto(DirectorDTO directorDTO) {
        DirectorProyectoDTO dTO = null;
        try {
            for (DirectorProyectoDTO directorProyectoDTO : sessionProyecto.getDirectoresProyectoDTO()) {
                if (directorProyectoDTO.getDirectorDTO().getDocenteCarrera().getDocenteId().equals(directorDTO.getDocenteCarrera().getDocenteId())) {
                    dTO = directorProyectoDTO;
                    break;
                }
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
        return dTO;
    }

    public void remover(DirectorProyectoDTO directorProyectoDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!(sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.PERTINENTE.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.SEGUIMIENTO.getTipo()))) {
                return;
            }
            if (directorProyectoDTO.getDirectorProyecto().getId() != null) {
                directorProyectoDM.setDirectorProyectoDTO(directorProyectoDTO);
                Calendar fechaActual = Calendar.getInstance();
                sessionRenunciaDirectorProyecto.setRenunciaDirector(new RenunciaDirector(null, null, directorProyectoDTO.getDirectorProyecto()));
                sessionRenuncia.setRenuncia(new Renuncia(null, fechaActual.getTime(), "S/N", "S/N"));
                sessionRenunciaDirectorProyecto.setRenderedCrud(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgEditarRenunciaDirector').show()");
                return;
            }
            sessionProyecto.getDirectoresProyectoDTO().remove(directorProyectoDTO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void cancelarDirectoresDisponibles() {
        this.directorDM.getDirectoresDTO().clear();
        this.directorDM.getFilterDirectoresDTO().clear();
        directorProyectoDM.setRenderedPnlDirectoresDisponibles(Boolean.FALSE);
        RequestContext.getCurrentInstance().execute("PF('dlgDirectoresDisponibles').hide()");
    }

    public void imprimirOficio(DirectorProyectoDTO directorProyectoDTO) {
        try {
            directorProyectoDM.setDirectorProyectoDTO(directorProyectoDTO);
            Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                    CatalogoDocumentoCarreraEnum.DIRECTORPROYECTO.getTipo());
            Documento documentoBuscar = documentoService.buscarPorCatalogo(new Documento(null, null, item.getId(), null, null, null, null, null, directorProyectoDTO.getDirectorProyecto().getId()));
            List<DocumentoCarrera> documentoCarreras = documentoCarreraService.buscar(new DocumentoCarrera(
                    null, documentoBuscar != null ? documentoBuscar.getId() : null, Boolean.TRUE, null));
            if (documentoCarreras == null) {
                generarOficio();
                return;
            }
            DocumentoCarrera documentoCarrera = !documentoCarreras.isEmpty() ? documentoCarreras.get(0) : null;
            if (documentoCarrera != null) {
                documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(
                        documentoCarrera, documentoService.buscarPorId(new Documento(documentoCarrera.getDocumentoId())), sessionProyecto.getCarreraSeleccionada()));
                File file = new File(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getRuta());
                documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
                directorProyectoDM.setRenderedMediaOficio(Boolean.TRUE);
                return;
            }
            generarOficio();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public void generarOficio() {
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
                + carrera.getNombre(), directorProyectoDM.getDirectorProyectoDTO().getDirectorDTO().getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion() + "<br/>"
                + directorProyectoDM.getDirectorProyectoDTO().getDirectorDTO().getPersona().getNombres() + " " + directorProyectoDM.getDirectorProyectoDTO().getDirectorDTO().getPersona().getApellidos(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "coordinador_carrera") + " " + carrera.getNombre(), sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getDocente().getTituloDocenteId().
                getTituloId().getAbreviacion() + "<br/>" + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getNombres() + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getApellidos(),
                generarCuerpoOficio(directorProyectoDM.getDirectorProyectoDTO()), "", "", cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_director_despedida"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_director_saludo"), "", cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "desarrollado_por")+": " + sessionUsuario.getUsuario().getNombres().toUpperCase() + " "
                + sessionUsuario.getUsuario().getApellidos().toUpperCase(),
                rutaReporte));
        if (resultado == null) {
            return;
        }
        grabarOficio(resultado, numeracion, fechaActual, configuracionCarrera, carrera);
    }

    private void grabarOficio(byte[] resultado, String numeracion, Calendar fechaActual, ConfiguracionCarrera configuracionCarrera,
            Carrera carrera) {
        Integer numeracionNext = Integer.parseInt(numeracion) + 1;
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAOFICIO.getTipo())).get(0).getValor()
                + "/oficio" + numeracionNext + ".pdf";
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                CatalogoDocumentoCarreraEnum.DIRECTORPROYECTO.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(), resultado,
                null, "pdf", directorProyectoDM.getDirectorProyectoDTO().getDirectorProyecto().getId());
        documentoService.guardar(documento);
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(new DocumentoCarrera(
                numeracionNext + "", documento.getId(), Boolean.TRUE, carrera.getId()), documento, carrera));
        documentoCarreraService.guardar(documentoCarreraDM.getDocumentoCarreraDTO().getDocumentoCarrera());
        directorProyectoDM.setRenderedMediaOficio(Boolean.TRUE);
        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
        configuracionCarrera.setValor(numeracionNext + 1 + "");
        configuracionCarreraService.actualizar(configuracionCarrera);
    }

    private String generarCuerpoOficio(final DirectorProyectoDTO directorProyectoDTO) {
        return (cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_director_cuerpo_a") + " " + directorProyectoDTO.
                getDirectorProyecto().getProyectoId().getTemaActual() + " " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "oficio_director_cuerpo_b") + " <b>" + sessionProyecto.getProyectoSeleccionado().getAutores()
                + "<b/> " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "oficio_director_cuerpo_c"));
    }

    /**
     * GENERAR FE DE PRESENTACIÓN
     *
     * @param directorProyectoDTO
     */
    public void imprimirFePresentacion(DirectorProyectoDTO directorProyectoDTO) {
        try {
            directorProyectoDM.setDirectorProyectoDTO(directorProyectoDTO);
            Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.FEPRESENTACION.getTipo(),
                    CatalogoDocumentoCarreraEnum.DIRECTORPROYECTO.getTipo());
            Documento documentoBuscar = documentoService.buscarPorCatalogo(new Documento(null, null, item.getId(), null, null, null, null, null, directorProyectoDTO.getDirectorProyecto().getId()));
            List<DocumentoCarrera> documentoCarreras = documentoCarreraService.buscar(new DocumentoCarrera(
                    null, documentoBuscar != null ? documentoBuscar.getId() : null, Boolean.TRUE, null));
            if (documentoCarreras == null) {
                generarFePresentacion();
                return;
            }
            DocumentoCarrera documentoCarrera = !documentoCarreras.isEmpty() ? documentoCarreras.get(0) : null;
            if (documentoCarrera != null) {
                documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(
                        documentoCarrera, documentoService.buscarPorId(new Documento(documentoCarrera.getDocumentoId())), sessionProyecto.getCarreraSeleccionada()));
                File file = new File(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getRuta());
                documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
                directorProyectoDM.setRenderedMediaFePresentacion(Boolean.TRUE);
                return;
            }
            generarFePresentacion();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     *
     */
    public void generarFePresentacion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        ReporteController reporteController = new ReporteController();
        Carrera carrera = sessionProyecto.getCarreraSeleccionada();
        Calendar fechaActual = Calendar.getInstance();
        String rutaReporte = request.getRealPath("/") + configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAREPORTEFEPRESENTACION.getTipo())).get(0).getValor();

        byte[] resultado = reporteController.fePresentacion(new ReporteFePresentacion(generaReferenciaFePresentacion(fechaActual, carrera), generaCuerpoFePresentacion(directorProyectoDM.getDirectorProyectoDTO(), fechaActual, carrera),
                generaFirmasInvolucrados(directorProyectoDM.getDirectorProyectoDTO(), carrera), generaFinalFePresentacion(directorProyectoDM.getDirectorProyectoDTO(), fechaActual, carrera),
                sessionUsuario.getUsuario().getNombres().toUpperCase() + " " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "desarrollado_por")+": " + sessionUsuario.getUsuario().getNombres().toUpperCase() + " " + sessionUsuario.getUsuario().getApellidos().toUpperCase(), rutaReporte));
        if (resultado == null) {
            return;
        }
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTAFEPRESENTACION.getTipo())).get(0).getValor() + "/fePertinencia"
                + directorProyectoDM.getDirectorProyectoDTO().getDirectorProyecto().getId() + ".pdf";
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.FEPRESENTACION.getTipo(),
                CatalogoDocumentoCarreraEnum.DIRECTORPROYECTO.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(),
                resultado, null, "pdf", directorProyectoDM.getDirectorProyectoDTO().getDirectorProyecto().getId());
        documentoService.guardar(documento);
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO(new DocumentoCarrera("", documento.getId(), Boolean.TRUE,
                carrera.getId()), documento, carrera));
        documentoCarreraDM.getDocumentoCarreraDTO().getDocumentoCarrera().setNumeracion("");
        documentoCarreraService.guardar(documentoCarreraDM.getDocumentoCarreraDTO().getDocumentoCarrera());
        directorProyectoDM.setRenderedMediaFePresentacion(Boolean.TRUE);
        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
    }

    /**
     *
     * @param fechaActual
     * @param carrera
     * @return
     */
    private String generaReferenciaFePresentacion(Calendar fechaActual, Carrera carrera) {
        return (cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_ref_a") + " " + cabeceraController.getUtilService().
                formatoFecha(fechaActual.getTime(), "dd MMMM yyyy") + ", " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_ref_b") + " " + cabeceraController.getUtilService().formatoFecha(
                        fechaActual.getTime(), "HH:mm") + ".-" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "fe_director_ref_c") + "<br/><br/>" + carrera.getAreaId().getSecretario() + "<br/> <b>"
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_ref_d").toUpperCase() + "<b/>");
    }

    /**
     *
     * @param directorProyectoDTO
     * @param fechaActual
     * @param carrera
     * @return
     */
    private String generaCuerpoFePresentacion(final DirectorProyectoDTO directorProyectoDTO, final Calendar fechaActual, final Carrera carrera) {
        return ("<b>" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_cu_a").toUpperCase() + " "
                + carrera.getNombre().toUpperCase() + " " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_cu_h").toUpperCase()
                + " " + carrera.getAreaId().getNombre().toUpperCase() + "<b/>.-" + carrera.getLugar() + ", " + cabeceraController.getUtilService().formatoFecha(
                        fechaActual.getTime(), "dd MMMM yyyy") + ", " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_cu_b")
                + " " + cabeceraController.getUtilService().formatoFecha(fechaActual.getTime(), "HH:mm") + ".-" + cabeceraController.
                getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_cu_c") + " " + directorProyectoDTO.getDirectorProyecto().getProyectoId().getTemaActual()
                + " " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_cu_d") + " " + carrera.getNombreTitulo().toUpperCase()
                + " " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_cu_e") + " " + sessionProyecto.
                getProyectoSeleccionado().getAutores() + " " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_cu_f")
                + " " + cabeceraController.getUtilService().formatoFecha(directorProyectoDTO.getDirectorProyecto().getFechaInicio(), "dd MMMM yyyy") + " "
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_cu_g"));
    }

    /**
     *
     * @param directorProyectoDTO
     * @param fechaActual
     * @param carrera
     * @return
     */
    private String generaFinalFePresentacion(final DirectorProyectoDTO directorProyectoDTO, final Calendar fechaActual, final Carrera carrera) {
        return (sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getDocente().getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase()
                + " <br/><b>" + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getNombres().toUpperCase() + " " + sessionProyecto.
                getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getApellidos().toUpperCase() + "<b/>"
                + "<p><b>" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_final_a").toUpperCase() + " "
                + carrera.getNombre().toUpperCase() + " " + "</b><p/><br/>" + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "fe_director_final_b") + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getDocente().getTituloDocenteId().
                getTituloId().getAbreviacion().toUpperCase() + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().
                getPersona().getNombres().toUpperCase() + " " + sessionProyecto.getCoordinadorPeriodoDTOCarreraSeleccionada().getPersona().getApellidos().toUpperCase() + ", "
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_final_c")
                + " " + carrera.getNombre().toUpperCase() + " " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "fe_director_final_d").toUpperCase() + " " + carrera.getAreaId().getNombre().toUpperCase() + " " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_final_e")
                + "<br/><p>" + carrera.getAreaId().getSecretario() + "<p/><b><p>" + cabeceraController.
                getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_final_f") + "<p/><b/><br/><br/>" + carrera.getLugar() + ", " + cabeceraController.getUtilService().
                formatoFecha(fechaActual.getTime(), "dd MMMM yyyy") + ", " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE,
                        "fe_director_final_g") + " " + cabeceraController.getUtilService().formatoFecha(fechaActual.getTime(), "HH:mm") + ".-"
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_final_h") + " " + sessionProyecto.getProyectoSeleccionado().getAutores().toUpperCase()
                + " " + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_final_i") + " " + directorProyectoDTO.getDirectorDTO().
                getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase() + " " + ""
                + directorProyectoDTO.getDirectorDTO().getPersona().getNombres().toUpperCase() + " " + directorProyectoDTO.getDirectorDTO().getPersona().getApellidos().toUpperCase() + " "
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_final_j") + ".");
    }

    /**
     *
     * @param directorProyectoDTO
     * @param carrera
     * @return
     */
    private String generaFirmasInvolucrados(final DirectorProyectoDTO directorProyectoDTO, final Carrera carrera) {
        return ("<p>" + directorProyectoDTO.getDirectorDTO().getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase()
                + " " + directorProyectoDTO.getDirectorDTO().getPersona().getNombres().toUpperCase() + " " + directorProyectoDTO.getDirectorDTO().
                getPersona().getApellidos().toUpperCase() + "<p/><p>" + cabeceraController
                .getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_firma_a") + "<p/><br/><br/>"
                + firmaPeticionarios()
                + "<p>" + carrera.getAreaId().getSecretario().toUpperCase() + "<p/>" + "<b><p>" + cabeceraController
                .getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_firma_b") + "<p/><b/>");
    }

    private String firmaPeticionarios() {
        StringBuilder firmaAutoresBuilder = new StringBuilder();
        for (AutorProyectoDTO autorProyectoDTO : sessionProyecto.getAutoresProyectoDTO()) {
            firmaAutoresBuilder.append(autorProyectoDTO.getPersona().getNombres()).append(" ").append(autorProyectoDTO.getPersona().getApellidos()).
                    append("<br/>" + "" + "").append(cabeceraController.getValueFromProperties(
                                    PropertiesFileEnum.CONTENIDOREPORTE, "fe_director_firma_perticionario")).append("<br/><br/>");
        }
        return firmaAutoresBuilder.toString();
    }

    public void cancelarImprimirOficio() {
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO());
        directorProyectoDM.setDirectorProyectoDTO(new DirectorProyectoDTO());
        directorProyectoDM.setRenderedMediaOficio(Boolean.FALSE);
    }

    public void cancelarImprimirFe() {
        documentoCarreraDM.setDocumentoCarreraDTO(new DocumentoCarreraDTO());
        directorProyectoDM.setDirectorProyectoDTO(new DirectorProyectoDTO());
        directorProyectoDM.setRenderedMediaFePresentacion(Boolean.FALSE);
    }
//</editor-fold>
}
