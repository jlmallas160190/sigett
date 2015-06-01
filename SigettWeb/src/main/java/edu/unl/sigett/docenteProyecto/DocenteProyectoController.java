/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.dao.PeriodoCoordinacionDao;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import edu.jlmallas.academico.service.CoordinadorPeriodoService;
import edu.jlmallas.academico.service.DocenteService;
import edu.unl.sigett.academico.dto.CoordinadorPeriodoDTO;
import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import edu.unl.sigett.autor.dto.AutorProyectoDTO;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.DirectorDao;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.DocumentoCarrera;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import edu.unl.sigett.enumeration.CatalogoDocumentoCarreraEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.reportes.ReporteController;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.DocumentoCarreraService;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
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
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;

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
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private CabeceraController cabeceraController;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ConfiguracionDao configuracionDao;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralDao;
    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteDao;
    @EJB
    private DirectorDao directorDao;
    @EJB
    private PersonaDao personaDao;
    @EJB
    private DocumentoCarreraService documentoCarreraService;
    @EJB
    private ItemService itemService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB
    private CoordinadorPeriodoService coordinadorPeriodoService;
    @EJB
    private PeriodoCoordinacionDao periodoCoordinacionDao;
    @EJB
    private DocenteService docenteService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DocenteProyectoController.class.getName());
    
    public DocenteProyectoController() {
    }

    //<editor-fold defaultstate="collapsed" desc="INICIO">
    /**
     * IMPRIMIR OFICIO AL DOCENTE NOTIFICANDO SU DESIGNACIÓN PARA DAR
     * PERTIENENCIA AL PROYECTO SELECCIONADO.
     *
     * @param docenteProyectoDTO
     */
    public void imprimirOficioPertinencia(DocenteProyectoDTO docenteProyectoDTO) {
        try {
            if (docenteProyectoDTO.getDocenteProyecto().getId() != null) {
                sessionDocenteProyecto.setDocenteProyectoId(docenteProyectoDTO.getDocenteProyecto().getDocenteId());
                Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                        CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo());
                Documento documentoBuscar = documentoService.buscar(new Documento(null, null, item.getId(), null, null, null, null));
                DocumentoCarrera documentoCarrera = documentoCarreraService.buscar(new DocumentoCarrera(
                        null, documentoBuscar.getId(), Boolean.TRUE, Integer.MIN_VALUE, sessionDocenteProyecto.getDocenteProyectoId())).get(0);
                sessionDocenteProyecto.setOficioPertinenciaDTO(new OficioPertinenciaDTO(new DocumentoCarrera(), new Documento(),
                        sessionProyecto.getCarreras().get(0)));
                if (documentoCarrera != null) {
                    sessionDocenteProyecto.setOficioPertinenciaDTO(new OficioPertinenciaDTO(
                            documentoCarrera, documentoService.buscarPorId(new Documento(documentoCarrera.getDocumentoId())), sessionProyecto.getCarreras().get(0)));
                    File file = new File(sessionDocenteProyecto.getOficioPertinenciaDTO().getDocumento().getRuta());
                    sessionDocenteProyecto.getOficioPertinenciaDTO().getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
                    sessionDocenteProyecto.setRenderedDialogoOficio(Boolean.TRUE);
                    return;
                }
                generarOficioPertinencia(docenteProyectoDTO);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * GENERA OFICIO DE PERTINENCIA
     *
     * @param docenteProyectoDTO
     */
    private void generarOficioPertinencia(final DocenteProyectoDTO docenteProyectoDTO) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getResponse();
        ReporteController reporteController = new ReporteController();
        Carrera carrera = sessionProyecto.getCarreras().get(0);
        List<PeriodoCoordinacion> periodoCoordinacions = periodoCoordinacionDao.buscar(new PeriodoCoordinacion(carrera, Boolean.TRUE));
        if (periodoCoordinacions == null) {
            return;
        }
        List<CoordinadorPeriodo> coordinadores = coordinadorPeriodoService.buscar(new CoordinadorPeriodo(Boolean.TRUE, null,
                !periodoCoordinacions.isEmpty() ? periodoCoordinacions.get(0) : null));
        if (coordinadores == null) {
            return;
        }
        CoordinadorPeriodo coordinadorPeriodo = !coordinadores.isEmpty() ? coordinadores.get(0) : null;
        CoordinadorPeriodoDTO coordinadorPeriodoDTO = new CoordinadorPeriodoDTO(coordinadorPeriodo, personaDao.find(coordinadorPeriodo.getCoordinadorId().getId()), null);
        coordinadorPeriodoDTO.setDocente(docenteService.buscarPorId(new Docente(coordinadorPeriodoDTO.getPersona().getId())));
        Calendar fechaActual = Calendar.getInstance();
        ConfiguracionCarrera configuracionCarrera = configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(carrera.getId(), "NO"));
        if (configuracionCarrera == null) {
            return;
        }
        String numeracion = configuracionCarrera.getValor();
        String rutaReporte = request.getRealPath("/") + configuracionDao.buscar(new Configuracion(ConfiguracionEnum.RUTAREPORTEPERTINENCIA.getTipo())).get(0).getValor();
        String tiempoMaximo = configuracionDao.buscar(new Configuracion(ConfiguracionEnum.TIEMPOPERTINENCIA.getTipo())).get(0).getValor() + " "
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.ETIQUETASREPORTE, "dias_laborables");
        byte[] resultado = reporteController.pertinencia(new ReporteOficioPertinenciaDTO(carrera.getLogo() != null ? carrera.getLogo() : null,
                configuracionDao.buscar(new Configuracion(ConfiguracionEnum.RUTALOGOINSTITUCION.getTipo())).get(0).getValor(), carrera.getNombre(),
                carrera.getAreaId().getNombre(), carrera.getSigla(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.ETIQUETASREPORTE, "nro_oficio"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.ETIQUETASREPORTE, "nombre_institucion"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.ETIQUETASREPORTE, "oficio") + " " + carrera.getSigla() + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.ETIQUETASREPORTE, "sigla_institucion"), carrera.getLugar(), fechaActual.toString(), numeracion,
                cabeceraController.getValueFromProperties(PropertiesFileEnum.ETIQUETASREPORTE, "docente_carrera") + " " + carrera.getNombre(),
                docenteProyectoDTO.getDocenteCarrera().getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion() + "<br/>"
                + docenteProyectoDTO.getPersona().getNombres() + " " + docenteProyectoDTO.getPersona().getApellidos(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.ETIQUETASREPORTE, "coordinador_carrera") + " " + carrera.getNombre(), coordinadorPeriodoDTO.getDocente().getTituloDocenteId().
                getTituloId().getAbreviacion() + "<br/>" + coordinadorPeriodoDTO.getPersona().getNombres() + " " + coordinadorPeriodoDTO.getPersona().getApellidos(),
                cabeceraController.getValueFromProperties(PropertiesFileEnum.ETIQUETASREPORTE, "articulos_pertinencia") + ", " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.ETIQUETASREPORTE, "pre_plazo") + " " + tiempoMaximo + ", " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.ETIQUETASREPORTE, "asunto_pertinencia") + ", " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.ETIQUETASREPORTE, "pre_temaProyecto") + ": <b>" + docenteProyectoDTO.getDocenteProyecto().getProyectoId().getTemaActual() + "</b>"
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.ETIQUETASREPORTE, "pre_datosAutor") + " " + autores()
                + cabeceraController.getValueFromProperties(PropertiesFileEnum.ETIQUETASREPORTE, "aspirante") + " " + carrera.getNombreTitulo(), "", "",
                cabeceraController.getValueFromProperties(PropertiesFileEnum.ETIQUETASREPORTE, "despedida_pertinencia"),
                cabeceraController.getValueFromProperties(PropertiesFileEnum.ETIQUETASREPORTE, "saludo"), "pdf", sessionUsuario.getUsuario().getNombres() + " "
                + sessionUsuario.getUsuario().getApellidos(), rutaReporte, response));
        if (resultado == null) {
            return;
        }
        Integer numeracionNext = Integer.parseInt(numeracion);
        String ruta = request.getRealPath("/") + configuracionDao.buscar(new Configuracion(ConfiguracionEnum.RUTAOFICIO.getTipo())).get(0).getValor() + "/oficio" + docenteProyectoDTO.getDocenteProyecto().getId();
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(), resultado, "");
        documentoService.guardar(documento);
        sessionDocenteProyecto.setOficioPertinenciaDTO(new OficioPertinenciaDTO(new DocumentoCarrera(
                numeracionNext + 1 + "", documento.getId(), Boolean.TRUE, carrera.getId(), docenteProyectoDTO.getDocenteProyecto().getId()), documento, carrera));
        documentoCarreraService.guardar(sessionDocenteProyecto.getOficioPertinenciaDTO().getDocumentoCarrera());
        sessionDocenteProyecto.setRenderedDialogoOficio(Boolean.TRUE);
    }
    
    private String autores() {
        String resultado = "";
        int contador = 0;
        for (AutorProyectoDTO autorProyecto : sessionProyecto.getAutoresProyectoDTO()) {
            if (contador == 0) {
                
                resultado = (autorProyecto.getPersona().getApellidos() + " " + autorProyecto.getPersona().getNombres());
            } else {
                resultado = (resultado + ", " + autorProyecto.getPersona().getApellidos() + " " + autorProyecto.getPersona().getNombres());
            }
            contador++;
        }
        return resultado;
    }
    
    public void imprimirFePresentacion(DocenteProyecto docenteProyecto) {
        
    }
    
    public void cancelarImprimirOficio() {
        sessionDocenteProyecto.setRenderedDialogoOficio(Boolean.FALSE);
    }

    /**
     * DETERMINAR SI SE PUEDEN AGREGAR MAS DE 2 DOCENTES AL PROYECTO
     * SELECCIONADO
     *
     * @return
     */
    public boolean permitirAgregarDocente() {
        boolean var = false;
        String val = configuracionGeneralDao.find((int) 19).getValor();
        if (sessionProyecto.getDocentesProyectoDTO().isEmpty()) {
            return true;
        }
        for (DocenteProyectoDTO docenteProyectoDTO : sessionProyecto.getDocentesProyectoDTO()) {
            if (docenteProyectoDTO.getDocenteProyecto().getEsActivo()) {
                if (val.equalsIgnoreCase("SI")) {
                    var = true;
                } else {
                    var = false;
                    break;
                }
            } else {
                var = true;
            }
        }
        return var;
    }

    /**
     * LISTADO DE DIRECTORES DISPONIBLES DE ACUERDO A LAS LINEAS DE
     * INVESTIGACIÓN SELECCIONADAS EN EL PROYECTO.
     */
    public void listadoDirectoresDiponibles() {
        try {
            this.sessionDocenteProyecto.getDirectoresDTO().clear();
            this.sessionDocenteProyecto.getFilterDirectoresDTO().clear();
            for (DocenteCarreraDTO docenteCarreraDTO : sessionUsuarioCarrera.getDocentesCarreraDTO()) {
                List<LineaInvestigacionDocente> lineaInvestigacionDocentes = lineaInvestigacionDocenteDao.buscar(
                        new LineaInvestigacionDocente(docenteCarreraDTO.getDocenteCarrera().getDocenteId().getId(), null));
                if (lineaInvestigacionDocentes.isEmpty()) {
                    continue;
                }
                for (LineaInvestigacion lineaInvestigacion : sessionProyecto.getLineasInvestigacionSeleccionadas()) {
                    for (LineaInvestigacionDocente lid : lineaInvestigacionDocentes) {
                        if (lineaInvestigacion.equals(lid.getLineaInvestigacionId())) {
                            DirectorDTO directorDTO = new DirectorDTO(directorDao.find(
                                    docenteCarreraDTO.getDocenteCarrera().getId()), docenteCarreraDTO.getDocenteCarrera(), personaDao.find(
                                            docenteCarreraDTO.getDocenteCarrera().getDocenteId().getId()));
                            if (!sessionDocenteProyecto.getDirectoresDTO().contains(directorDTO)) {
                                sessionDocenteProyecto.getDirectoresDTO().add(directorDTO);
                            }
                        }
                    }
                }
            }
            sessionDocenteProyecto.setFilterDirectoresDTO(sessionDocenteProyecto.getDirectoresDTO());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
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
            if (permitirAgregarDocente()) {
                DocenteProyectoDTO dp = devuelveDocenteProyecto(directorDTO);
                if (dp == null) {
                    DocenteProyecto docenteProyecto = new DocenteProyecto(sessionProyecto.getProyectoSeleccionado(), fecha.getTime(),
                            directorDTO.getDocenteCarrera().getDocenteId().getId(), Boolean.TRUE);
                    DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, directorDTO.getPersona(), directorDTO.getDirector(),
                            directorDTO.getDocenteCarrera());
                    sessionProyecto.getDocentesProyectoDTO().add(docenteProyectoDTO);
                    RequestContext.getCurrentInstance().execute("PF('dlgBuscarDocentesDisponibles').hide()");
                    cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " "
                            + bundle.getString("lbl.msm_agregar"), "");
                }
            } else {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe") + " "
                        + bundle.getString("lbl.docente"), "");
            }
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
