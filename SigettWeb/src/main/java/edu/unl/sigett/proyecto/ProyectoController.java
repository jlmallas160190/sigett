package edu.unl.sigett.proyecto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.TipoConfiguracionEnum;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.jlmallas.academico.dao.PeriodoCoordinacionDao;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.entity.Nivel;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.entity.PeriodoAcademico;
import edu.jlmallas.academico.service.CarreraService;
import edu.jlmallas.academico.service.CoordinadorPeriodoService;
import edu.jlmallas.academico.service.DocenteService;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.unl.sigett.academico.dto.CoordinadorPeriodoDTO;
import edu.unl.sigett.autorProyecto.AutorProyectoDTO;
import edu.unl.sigett.dao.DirectorDao;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.docenteProyecto.DocenteProyectoDTO;
import edu.unl.sigett.documentoProyecto.DocumentoProyectoDTO;
import edu.unl.sigett.dto.ProyectoDTO;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.entity.Tema;
import edu.unl.sigett.entity.TemaProyecto;
import edu.unl.sigett.enumeration.ConfiguracionCarreraEnum;
import edu.unl.sigett.enumeration.ConfiguracionProyectoEnum;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.ConfiguracionProyectoService;
import edu.unl.sigett.service.CronogramaService;
import edu.unl.sigett.service.DocenteProyectoService;
import edu.unl.sigett.service.DocumentoProyectoService;
import edu.unl.sigett.service.LineaInvestigacionProyectoService;
import edu.unl.sigett.service.LineaInvestigacionService;
import edu.unl.sigett.service.ProyectoCarreraOfertaService;
import edu.unl.sigett.service.ProyectoService;
import edu.unl.sigett.service.TemaProyectoService;
import edu.unl.sigett.service.TemaService;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import edu.unl.sigett.webSemantica.dto.AreaAcademicaOntDTO;
import edu.unl.sigett.webSemantica.dto.AutorOntDTO;
import edu.unl.sigett.webSemantica.dto.AutorProyectoOntDTO;
import edu.unl.sigett.webSemantica.dto.CarreraOntDTO;
import edu.unl.sigett.webSemantica.dto.LineaInvestigacionOntDTO;
import edu.unl.sigett.webSemantica.dto.LineaInvestigacionProyectoOntDTO;
import edu.unl.sigett.webSemantica.dto.NivelAcademicoOntDTO;
import edu.unl.sigett.webSemantica.dto.OfertaAcademicaOntDTO;
import edu.unl.sigett.webSemantica.dto.PeriodoAcademicoOntDTO;
import edu.unl.sigett.webSemantica.dto.ProyectoCarreraOfertaOntDTO;
import edu.unl.sigett.webSemantica.dto.ProyectoOntDTO;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jfree.util.Log;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author jorge-luis
 */
@Named(value = "proyectoController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarProyecto",
            pattern = "/editarProyecto/#{sessionProyecto.proyectoSeleccionado.id}",
            viewId = "/faces/pages/sigett/proyectos/editarProyecto.xhtml"
    ),
    @URLMapping(
            id = "crearProyecto",
            pattern = "/crearProyecto/",
            viewId = "/faces/pages/sigett/proyectos/editarProyecto.xhtml"
    ),
    @URLMapping(
            id = "proyectos",
            pattern = "/proyectos/",
            viewId = "/faces/pages/sigett/proyectos/index.xhtml"
    )
})
public class ProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private CabeceraController cabeceraController;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ProyectoService proyectoService;
    @EJB
    private ProyectoCarreraOfertaService proyectoCarreraOfertaService;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB
    private LogDao logDao;
    @EJB
    private LineaInvestigacionProyectoService lineaInvestigacionProyectoService;
    @EJB
    private ConfiguracionProyectoService configuracionProyectoService;
    @EJB
    private CarreraService carreraService;
    @EJB
    private ProyectoOfertaCarreraDao proyectoCarreraOfertaDao;
    @EJB
    private PersonaDao personaDao;
    @EJB
    private AutorProyectoService autorProyectoService;
    @EJB
    private ItemService itemService;
    @EJB
    private LineaInvestigacionService lineaInvestigacionService;
    @EJB
    private EstudianteCarreraDao estudianteCarreraDao;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB
    private DocenteProyectoService docenteProyectoService;
    @EJB
    private DirectorDao directorDao;
    @EJB
    private DocenteCarreraDao docenteCarreraDao;
    @EJB
    private TemaProyectoService temaProyectoService;
    @EJB
    private DocumentoProyectoService documentoProyectoService;
    @EJB
    private CronogramaService cronogramaService;
    @EJB
    private TemaService temaService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private ConfiguracionDao configuracionDao;
    @EJB
    private PeriodoCoordinacionDao periodoCoordinacionDao;
    @EJB
    private CoordinadorPeriodoService coordinadorPeriodoService;
    @EJB
    private DocenteService docenteService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(ProyectoController.class.getName());
    
    public ProyectoController() {
    }
    
    public void preRenderView() {
        this.buscar();
        this.listadoCarreras();
        this.listadoOfertasAcademicas();
        this.renderedCrear();
        this.renderedEditar();
    }
    
    public void preRenderViewEdit() {
        estadoActual();
        this.renderedInicio();
        this.listadoConfiguracionesProyecto();
        this.listadoTipos();
        this.listadoCategorias();
    }
    //<editor-fold defaultstate="collapsed" desc="PROYECTO">

    public String crear() {
        try {
            Calendar fechaActual = Calendar.getInstance();
            sessionProyecto.setProyectoSeleccionado(new Proyecto(itemService.buscarPorCatalogoCodigo(
                    CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.INICIO.getTipo()).getId(),
                    Long.MIN_VALUE, Long.MIN_VALUE, "NINGUNA", fechaActual.getTime(), ""));
            sessionProyecto.getCronograma().setProyecto(sessionProyecto.getProyectoSeleccionado());
            sessionProyecto.getCronograma().setFechaInicio(fechaActual.getTime());
            sessionProyecto.getCronograma().setFechaFin(fechaActual.getTime());
            sessionProyecto.getCronograma().setFechaProrroga(fechaActual.getTime());
            sessionProyecto.getCronograma().setDuracion(0.0);
            iniciar();
            crearTema();
            this.agregarConfiguracionesProyecto();
            return "pretty:crearProyecto";
        } catch (Exception e) {
            Log.info(e.getMessage());
        }
        return "";
    }

    /**
     * LISTAR CATEGORIAS DE PROYECTO
     */
    private void listadoCategorias() {
        sessionProyecto.getCategorias().clear();
        sessionProyecto.setCategorias(itemService.buscarPorCatalogo(CatalogoEnum.CATALOGOPROYECTO.getTipo()));
    }
    
    private void iniciar() {
        sessionProyecto.getAutoresProyectoDTONuevos().clear();
        sessionProyecto.getCarrerasRemovidasTransfer().clear();
        sessionProyecto.getCarrerasSeleccionadasTransfer().clear();
        sessionProyecto.getLineasInvestigacionRemovidosTransfer().clear();
        sessionProyecto.getLineasInvestigacionSeleccionadasTransfer().clear();
        sessionProyecto.getLineasInvestigacionSeleccionadas().clear();
        sessionProyecto.getDocumentosProyectosDTOAgregados().clear();
        this.sessionProyecto.getDocentesProyectoDTO().clear();
        this.sessionProyecto.getFilterDocentesProyectoDTO().clear();
        this.sessionProyecto.getDocumentosProyectoDTO().clear();
        this.sessionProyecto.getFilterDocumentosProyectoDTO().clear();
        sessionProyecto.setProyectoOntDTO(new ProyectoOntDTO());
        sessionProyecto.setCategoriaSeleccionada(new Item());
        sessionProyecto.setTipoSeleccionado(new Item());
        pickListLineasInvestigacionProyecto(sessionProyecto.getProyectoSeleccionado());
        pickListCarreras(sessionProyecto.getProyectoSeleccionado());
    }
    
    public String editar(final Proyecto proyecto) {
        try {
            sessionProyecto.setProyectoSeleccionado(proyecto);
            sessionProyecto.setCronograma(proyecto.getCronograma());
            iniciar();
            sessionProyecto.setCategoriaSeleccionada(itemService.buscarPorId(proyecto.getCatalogoProyectoId()));
            sessionProyecto.setTipoSeleccionado(itemService.buscarPorId(proyecto.getTipoProyectoId()));
            editarTema();
            buscarDocentes();
            this.buscarDocumentos();
            this.agregarConfiguracionesProyecto();
            return "pretty:editarProyecto";
        } catch (Exception e) {
            Log.info(e.getMessage());
        }
        return "";
    }
    
    public String grabar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo-grabado");
        if (sessionProyecto.getAutoresProyectoDTO().isEmpty()) {
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " "
                    + bundle.getString("lbl.autor"), "");
            return "";
        }
        if (sessionProyecto.getCarrerasDualList().getTarget().isEmpty()) {
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " "
                    + bundle.getString("lbl.carrera"), "");
            return "";
        }
        sessionProyecto.getProyectoSeleccionado().setCatalogoProyectoId(sessionProyecto.getCategoriaSeleccionada().getId());
        sessionProyecto.getProyectoSeleccionado().setTipoProyectoId(sessionProyecto.getTipoSeleccionado().getId());
        if (sessionProyecto.getProyectoSeleccionado().getId() == null) {
            sessionProyecto.getProyectoSeleccionado().setTemaActual(sessionProyecto.getTemaProyecto().getTemaId().getNombre());
            this.proyectoService.guardar(sessionProyecto.getProyectoSeleccionado());
            sessionProyecto.getCronograma().setId(sessionProyecto.getProyectoSeleccionado().getId());
            this.cronogramaService.guardar(sessionProyecto.getCronograma());
            this.temaService.guardar(sessionProyecto.getTemaProyecto().getTemaId());
            this.temaProyectoService.guardar(sessionProyecto.getTemaProyecto());
            this.grabarIndividuoProyecto();
            grabarAutores();
            grabarDocentes();
            grabarLineasInvestigacionProyecto();
            grabarProyectoCarrerasOferta();
            grabarDocumentos();
            grabarConfiguraciones();
            
            logDao.create(logDao.crearLog("Proyecto", sessionProyecto.getProyectoSeleccionado().getId() + "", "EDITAR", "|Tema= "
                    + sessionProyecto.getProyectoSeleccionado().getTemaActual() + "|Descripción= " + sessionProyecto.getProyectoSeleccionado().getDescripcion()
                    + "|Tipo de Proyecto= " + sessionProyecto.getProyectoSeleccionado().getTipoProyectoId() + "" + "CatalogoProyecto= "
                    + sessionProyecto.getProyectoSeleccionado().getCatalogoProyectoId() + "|Estado= " + sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId(),
                    sessionUsuario.getUsuario()));
            if (param.equalsIgnoreCase("guardar")) {
                sessionProyecto.setProyectoSeleccionado(new Proyecto());
                return "pretty:proyectos";
            }
            if (param.equalsIgnoreCase("guardar-editar")) {
                this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.proyecto") + " "
                        + bundle.getString("lbl.msm_egrabar"), "");
                return "";
            }
            return "";
        }
        sessionProyecto.getProyectoSeleccionado().setTemaActual(sessionProyecto.getTemaProyecto().getTemaId().getNombre());
        this.proyectoService.actualizar(sessionProyecto.getProyectoSeleccionado());
        this.cronogramaService.actualizar(sessionProyecto.getCronograma());
        this.temaService.actualizar(sessionProyecto.getTemaProyecto().getTemaId());
        this.temaProyectoService.actualizar(sessionProyecto.getTemaProyecto());
        grabarAutores();
        grabarDocentes();
        grabarLineasInvestigacionProyecto();
        eliminarLineasInvestigacionProyecto();
        grabarProyectoCarrerasOferta();
        eliminarProyectoCarreras();
        grabarDocumentos();
        grabarConfiguraciones();
        if (param.equalsIgnoreCase("guardar")) {
            sessionProyecto.setProyectoSeleccionado(new Proyecto());
            return "pretty:proyectos";
        }
        if (param.equalsIgnoreCase("guardar-editar")) {
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.proyecto") + " "
                    + bundle.getString("lbl.msm_editar"), "");
            return "";
        }
        return "";
    }

    /**
     * LISTAR TIPOS DE PROYECTO
     */
    private void listadoTipos() {
        sessionProyecto.getTipos().clear();
        sessionProyecto.setTipos(itemService.buscarPorCatalogo(CatalogoEnum.TIPOPROYECTO.getTipo()));
    }

    /**
     * GRABAR INDIVIUDUO ONTOLOGÍA PROYECTO EN RDF
     */
    private void grabarIndividuoProyecto() {
        ProyectoOntDTO proyectoOntDTO = new ProyectoOntDTO(sessionProyecto.getProyectoSeleccionado().getId(), sessionProyecto.getProyectoSeleccionado().getTemaActual(),
                sessionProyecto.getProyectoSeleccionado().getFechaCreated().toString(), sessionProyecto.getTipoSeleccionado().getNombre(), sessionProyecto.getEstadoActual().getNombre(),
                cabeceraController.getValueFromProperties(PropertiesFileEnum.URI, "proyecto"));
        cabeceraController.getOntologyService().getProyectoOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getProyectoOntService().write(proyectoOntDTO);
        sessionProyecto.setProyectoOntDTO(proyectoOntDTO);
    }
    
    private void estadoActual() {
        if (sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId() == null) {
            return;
        }
        Item estado = itemService.buscarPorId(sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId());
        sessionProyecto.setEstadoActual(estado);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="FILTROS BUSQUEDA DE PROYECTOS">
    /**
     * BUSCAR PROYECTOS POR CARRERA, OFERTA y LINEA DE INVESTIGACION
     */
    public void buscar() {
        sessionProyecto.getProyectos().clear();
        sessionProyecto.getFilterProyectos().clear();
        try {
            List<Proyecto> proyectosEncontrados = proyectoService.buscar(
                    new ProyectoDTO(new Proyecto(sessionProyecto.getEstadoSeleccionado() != null ? sessionProyecto.getEstadoSeleccionado().getId() : null, null, null, null, null, null),
                            new ProyectoCarreraOferta(null, sessionProyecto.getCarreraSeleccionada() != null ? sessionProyecto.getCarreraSeleccionada().getId() : null,
                                    sessionProyecto.getOfertaAcademicaSeleccionada().getId() != null
                                    ? sessionProyecto.getOfertaAcademicaSeleccionada().getId() : null, Boolean.TRUE),
                            sessionProyecto.getLineaInvestigacionProyectoSeleccionada()));
            
            if (proyectosEncontrados == null) {
                return;
            }
            for (Proyecto proyecto : proyectosEncontrados) {
                proyecto.setEstado(itemService.buscarPorId(proyecto.getEstadoProyectoId()).getNombre());
                proyecto.setCatalogo(itemService.buscarPorId(proyecto.getCatalogoProyectoId()).getNombre());
                proyecto.setTipo(itemService.buscarPorId(proyecto.getTipoProyectoId()).getNombre());
                proyecto.setAutores(autores(proyecto));
                if (!this.sessionProyecto.getProyectos().contains(proyecto)) {
                    this.sessionProyecto.getProyectos().add(proyecto);
                }
            }
            sessionProyecto.setFilterProyectos(sessionProyecto.getProyectos());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * DEVOLVER LOS AUTORES DE UN PROYECTO EN LA TABLA PROYECTOS
     *
     * @param proyecto
     * @return
     */
    private String autores(Proyecto proyecto) {
        String resultado = "";
        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo());
        List<AutorProyecto> autorProyectos = autorProyectoService.buscar(new AutorProyecto(proyecto, null, null, null, null));
        if (autorProyectos == null) {
            return "";
        }
        int contador = 0;
        for (AutorProyecto autorProyecto : autorProyectos) {
            if (estadoRenunciado.getId().equals(autorProyecto.getEstadoAutorId())) {
                continue;
            }
            EstudianteCarrera estudianteCarrera = estudianteCarreraDao.find(autorProyecto.getAspiranteId().getId());
            Persona persona = personaDao.find(estudianteCarrera.getEstudianteId().getId());
            if (contador == 0) {
                if (persona == null) {
                    continue;
                }
                resultado = (persona.getApellidos() + " " + persona.getNombres());
            } else {
                resultado = (resultado + ", " + persona.getApellidos() + " " + persona.getNombres());
            }
            contador++;
        }
        return resultado;
    }

    /**
     * LISTADO DE CARRERAS QUE ADMINISTRA EL USUARIO PARA FILTRAR PROYECTOS
     */
    public void listadoCarreras() {
        try {
            this.sessionProyecto.getCarreras().clear();
            this.sessionProyecto.getFilterCarreras().clear();
            this.sessionProyecto.getCarreras().addAll(sessionUsuarioCarrera.getCarreras());
            this.sessionProyecto.setFilterCarreras(this.sessionProyecto.getCarreras());
        } catch (Exception e) {
        }
    }

    /**
     * LISTADO DE OFERTAS ACADEMICAS DE LA CARRERAS ADMINISTRADAS POR EL USUARIO
     * PARA FILTRAR PROYECTOS
     */
    public void listadoOfertasAcademicas() {
        this.sessionProyecto.getOfertaAcademicas().clear();
        this.sessionProyecto.getFilterOfertaAcademicas().clear();
        try {
            for (Carrera carrera : sessionProyecto.getCarreras()) {
                for (ProyectoCarreraOferta pco : proyectoCarreraOfertaService.buscar(
                        new ProyectoCarreraOferta(null, carrera.getId(), null, Boolean.TRUE))) {
                    OfertaAcademica ofertaAcademica = ofertaAcademicaService.find(pco.getOfertaAcademicaId());
                    if (!this.sessionProyecto.getOfertaAcademicas().contains(ofertaAcademica)) {
                        this.sessionProyecto.getOfertaAcademicas().add(ofertaAcademica);
                    }
                }
            }
            sessionProyecto.setFilterOfertaAcademicas(this.sessionProyecto.getOfertaAcademicas());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PERMITE SELECCIONAR CARRERA DE UNA FILA DE LA TABLA CARRERAS Y BUSCAR LAS
     * LINEAS DE INVESTIGACÓN POR LA CARRERA SELECCIONADA QUE TENGAN PROYECTOS
     *
     * @param event
     */
    public void seleccionarCarrera(SelectEvent event) {
        try {
            sessionProyecto.setCarreraSeleccionada((Carrera) event.getObject());
            
            List<CoordinadorPeriodo> coordinadores = coordinadorPeriodoService.buscar(new CoordinadorPeriodo(Boolean.TRUE, null, null,
                    sessionProyecto.getCarreraSeleccionada()));
            if (coordinadores == null) {
                return;
            }
            CoordinadorPeriodo coordinadorPeriodo = !coordinadores.isEmpty() ? coordinadores.get(0) : null;
            CoordinadorPeriodoDTO coordinadorPeriodoDTO = new CoordinadorPeriodoDTO(coordinadorPeriodo, personaDao.find(
                    coordinadorPeriodo.getCoordinadorId().getId()), null);
            coordinadorPeriodoDTO.setDocente(docenteService.buscarPorId(new Docente(coordinadorPeriodoDTO.getPersona().getId())));
            sessionProyecto.setCoordinadorPeriodoDTOCarreraSeleccionada(coordinadorPeriodoDTO);
            buscar();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void seleccionarOfertaAcademica(SelectEvent event) {
        sessionProyecto.setOfertaAcademicaSeleccionada((OfertaAcademica) event.getObject());
        buscar();
    }
    
    public void seleccionarLineaInvestigacion(SelectEvent event) {
        sessionProyecto.setLineaInvestigacionProyectoSeleccionada((LineaInvestigacionProyecto) event.getObject());
        buscar();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="LINEAS DE INVESTIGACION">
    /**
     * BUSCAR LA LINEAS DE INVESTIGACION DEL PROYECTO Y DE LAS CARRERAS DEL
     * USUARIO PARA VISUALIZAR EN UN PICKLIST
     *
     * @param proyecto
     */
    private void pickListLineasInvestigacionProyecto(Proyecto proyecto) {
        this.sessionProyecto.getLineasInvestigacionDualList().getSource().clear();
        this.sessionProyecto.getLineasInvestigacionDualList().getTarget().clear();
        List<LineaInvestigacion> lineasInvestigacionProyecto = new ArrayList<>();
        List<LineaInvestigacion> lineasInvestigacionCarrera = new ArrayList<>();
        try {
            lineasInvestigacionProyecto = lineaInvestigacionProyectoService.buscarLineaInvestigacion(
                    new LineaInvestigacionProyecto(proyecto.getId() != null ? proyecto : null, null, null));
            sessionProyecto.getLineasInvestigacionSeleccionadas().addAll(lineasInvestigacionProyecto);
            for (Carrera carrera : sessionProyecto.getCarreras()) {
                List<LineaInvestigacion> lics = lineaInvestigacionService.buscarPorCarrera(new LineaInvestigacionCarrera(null, carrera.getId()));
                if (lics == null) {
                    continue;
                }
                for (LineaInvestigacion lic : lics) {
                    if (!lineasInvestigacionCarrera.contains(lic)) {
                        lineasInvestigacionCarrera.add(lic);
                    }
                }
            }
            sessionProyecto.setLineasInvestigacionDualList(new DualListModel<>(lineaInvestigacionService.diferenciaCarreraProyecto(
                    lineasInvestigacionCarrera, lineasInvestigacionProyecto), lineasInvestigacionProyecto));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * SELECCIONAR Y QUITAR LINEAS DE INVESTIGACION DE PICKLIST
     *
     * @param event
     */
    public void transferLineasInvestigacion(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                LineaInvestigacion li = lineaInvestigacionService.buscarPorId(new LineaInvestigacion(id));
                LineaInvestigacionProyecto lp = new LineaInvestigacionProyecto();
                if (li != null) {
                    lp.setLineaInvestigacionId(li);
                }
                if (event.isRemove()) {
                    sessionProyecto.getLineasInvestigacionSeleccionadasTransfer().remove(lp);
                    sessionProyecto.getLineasInvestigacionRemovidosTransfer().add(lp);
                    int pos = 0;
                    for (LineaInvestigacionProyecto lip : sessionProyecto.getLineasInvestigacionProyecto()) {
                        if (!lip.getLineaInvestigacionId().equals(lp.getLineaInvestigacionId())) {
                            pos++;
                        } else {
                            break;
                        }
                    }
                    sessionProyecto.getLineasInvestigacionSeleccionadas().remove(pos);
                } else {
                    if (event.isAdd()) {
                        if (contieneLineaInvestigacion(sessionProyecto.getLineasInvestigacionProyecto(), lp)) {
                            sessionProyecto.getLineasInvestigacionRemovidosTransfer().add(lp);
                        }
                        sessionProyecto.getLineasInvestigacionSeleccionadas().add(li);
                        sessionProyecto.getLineasInvestigacionSeleccionadasTransfer().add(lp);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * DETERMINAR SI UNA LISTA DE LINEAS DE INVESTIGACION PROYECTO CONTIENE UN
     * LINEA DE INVESTIGACION PROYECTO
     *
     * @param lineaInvestigacionProyectos
     * @param lineaInvestigacionProyecto
     * @return
     */
    public boolean contieneLineaInvestigacion(List<LineaInvestigacionProyecto> lineaInvestigacionProyectos,
            LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        boolean var = false;
        for (LineaInvestigacionProyecto lp : lineaInvestigacionProyectos) {
            if (lp.getLineaInvestigacionId().equals(lineaInvestigacionProyecto.getLineaInvestigacionId())) {
                var = true;
            }
        }
        return var;
    }

    /**
     * PERMITE GRABAR LINEAS DE INVESTIGACION PERMITE CREAR UNA ONTOLOGIA Y
     * ALMACENAR EN UN ARCHIVO FORMATO RDF
     */
    public void grabarLineasInvestigacionProyecto() {
        try {
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            for (LineaInvestigacionProyecto lineaInvestigacionProyecto : sessionProyecto.getLineasInvestigacionSeleccionadasTransfer()) {
                if (lineaInvestigacionProyecto.getId() == null) {
                    lineaInvestigacionProyecto.setProyectoId(sessionProyecto.getProyectoSeleccionado());
                    lineaInvestigacionProyectoService.guardar(lineaInvestigacionProyecto);
                    grabarIndividuoLP(lineaInvestigacionProyecto);
                    logDao.create(logDao.crearLog("LineaInvestigacionProyecto", lineaInvestigacionProyecto.getId() + "",
                            "CREAR", "Proyecto=" + sessionProyecto.getProyectoSeleccionado().getId() + "|LineaInvestigacion="
                            + lineaInvestigacionProyecto.getLineaInvestigacionId().getId(), sessionUsuario.getUsuario()));
                    continue;
                }
                lineaInvestigacionProyectoService.actulizar(lineaInvestigacionProyecto);
            }
        } catch (Exception e) {
        }
        
    }
    
    private void grabarIndividuoLP(final LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        LineaInvestigacionOntDTO lineaInvestigacionOntDTO = new LineaInvestigacionOntDTO(lineaInvestigacionProyecto.getLineaInvestigacionId().getId(),
                lineaInvestigacionProyecto.getLineaInvestigacionId().getNombre(),
                cabeceraController.getValueFromProperties(PropertiesFileEnum.URI, "linea_investigacion"));
        cabeceraController.getOntologyService().getLineaInvestigacionOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getLineaInvestigacionOntService().write(lineaInvestigacionOntDTO);
        cabeceraController.getOntologyService().getLineaInvestigacionProyectoOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getLineaInvestigacionProyectoOntService().write(new LineaInvestigacionProyectoOntDTO(
                lineaInvestigacionProyecto.getId(), lineaInvestigacionOntDTO, sessionProyecto.getProyectoOntDTO(),
                cabeceraController.getValueFromProperties(PropertiesFileEnum.URI, "linea_investigacion_proyecto")));
    }
    
    public void eliminarLineasInvestigacionProyecto() {
        if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
            return;
        }
        for (LineaInvestigacionProyecto lp : sessionProyecto.getLineasInvestigacionRemovidosTransfer()) {
            Long id = devuelveLineaInvestigacion(lp);
            LineaInvestigacionProyecto lineaInvestigacionProyectoBuscar = new LineaInvestigacionProyecto();
            lineaInvestigacionProyectoBuscar.setId(id);
            LineaInvestigacionProyecto lineaInvestigacionProyecto = lineaInvestigacionProyectoService.buscarPorId(lineaInvestigacionProyectoBuscar);
            if (lineaInvestigacionProyecto != null) {
                logDao.create(logDao.crearLog("LineaInvestigacionProyecto", lineaInvestigacionProyecto.getId() + "", "CREAR", "NUEVO: |Proyecto="
                        + sessionProyecto.getProyectoSeleccionado().getId() + "|LineaInvestigacion="
                        + lineaInvestigacionProyecto.getLineaInvestigacionId().getId(), sessionUsuario.getUsuario()));
                lineaInvestigacionProyectoService.eliminar(lineaInvestigacionProyecto);
            }
        }
    }
    
    public Long devuelveLineaInvestigacion(final LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        for (LineaInvestigacionProyecto lp : sessionProyecto.getLineasInvestigacionRemovidosTransfer()) {
            if (lp.getLineaInvestigacionId().equals(lineaInvestigacionProyecto.getLineaInvestigacionId())) {
                return lp.getId();
            }
        }
        return null;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CARRERAS">

    /**
     * CARRERAS QUE PERTENECEN AL PROYECTO SELECCIONADO Y A LAS CARRERAS DEL
     * USUARIO PARA VISUALIZARLO EN UN PICKLIST
     *
     * @param proyecto
     */
    private void pickListCarreras(Proyecto proyecto) {
        sessionProyecto.getCarrerasProyecto().clear();
        sessionProyecto.getFilterCarrerasProyecto().clear();
        List<Carrera> carrerasProyecto = new ArrayList<>();
        List<Carrera> usuarioCarreras = new ArrayList<>();
        try {
            List<ProyectoCarreraOferta> lips = proyectoCarreraOfertaService.buscar(new ProyectoCarreraOferta(proyecto, null, null, Boolean.TRUE));
            if (lips != null) {
                for (ProyectoCarreraOferta pco : lips) {
                    Carrera c = carreraService.find(pco.getCarreraId());
                    if (!carrerasProyecto.contains(c)) {
                        carrerasProyecto.add(c);
                    }
                }
            }
            
            for (Carrera carrera : sessionProyecto.getCarreras()) {
                if (!usuarioCarreras.contains(carrera)) {
                    usuarioCarreras.add(carrera);
                }
            }
            sessionProyecto.setCarrerasDualList(new DualListModel<>(carreraService.diferenciaProyectoCarrera(
                    usuarioCarreras, carrerasProyecto), carrerasProyecto));
            sessionProyecto.setCarrerasProyecto(carrerasProyecto);
            sessionProyecto.setFilterCarrerasProyecto(sessionProyecto.getCarrerasProyecto());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * TRANSFERIR CARRERA EN EL PICKLIST
     *
     * @param event
     */
    public void transferCarrera(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Integer id = Integer.parseInt(item.toString().substring(0, v));
                Carrera c = carreraService.find(id);
                ProyectoCarreraOferta proyectoCarreraOferta = new ProyectoCarreraOferta();
                proyectoCarreraOferta.setCarreraId(c.getId());
                if (event.isRemove()) {
                    sessionProyecto.getCarrerasSeleccionadasTransfer().remove(proyectoCarreraOferta);
                    sessionProyecto.getCarrerasRemovidasTransfer().add(proyectoCarreraOferta);
                } else {
                    sessionProyecto.getCarrerasSeleccionadasTransfer().add(proyectoCarreraOferta);
                    if (contieneCarrera(sessionProyecto.getCarrerasRemovidasTransfer(), proyectoCarreraOferta)) {
                        sessionProyecto.getCarrerasRemovidasTransfer().remove(proyectoCarreraOferta);
                    }
                }
            }
        } catch (NumberFormatException e) {
            Log.info(e.getMessage());
        }
    }

    /**
     * COMPROBAR SI LISTADO DE CARRERAS PROYECTO YA CONTIENE UNA CARRERA
     * PROYECTO SELECCIONADA EN EL PICKLIST CARRERAS
     *
     * @param carrerasProyecto
     * @param carreraProyecto
     * @return
     */
    private boolean contieneCarrera(List<ProyectoCarreraOferta> carrerasProyecto, ProyectoCarreraOferta carreraProyecto) {
        boolean var = false;
        for (ProyectoCarreraOferta pco : carrerasProyecto) {
            if (pco.getCarreraId().equals(carreraProyecto.getCarreraId())) {
                var = true;
            }
        }
        return var;
    }

    /**
     * GRABAR CARRERAS DE PROYECTO
     */
    private void grabarProyectoCarrerasOferta() {
        try {
         if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
                for (ProyectoCarreraOferta proyectoCarreraOferta : sessionProyecto.getCarrerasSeleccionadasTransfer()) {
                    Carrera c = carreraService.find(proyectoCarreraOferta.getCarreraId());
                    List<ProyectoCarreraOferta> proyectoCarreraOfertas = proyectoCarreraOfertaService.buscar(
                            new ProyectoCarreraOferta(sessionProyecto.getProyectoSeleccionado(), null, null, Boolean.TRUE));
                    
                    Long pcoId = devuelveProyectoCarreraId(proyectoCarreraOfertas, proyectoCarreraOferta);
                    proyectoCarreraOferta = proyectoCarreraOfertaService.buscarPorId(new ProyectoCarreraOferta(pcoId));
                    if (proyectoCarreraOferta == null) {
                        ConfiguracionCarrera configuracion = configuracionCarreraService.buscar(new ConfiguracionCarrera(
                                c.getId(), ConfiguracionCarreraEnum.OFERTAACADEMICA.getTipo())).get(0);
                        String idSga = configuracion != null ? configuracion.getValor() : null;
                        if (idSga == null) {
                            return;
                        }
                        OfertaAcademica ofertaAcademica = ofertaAcademicaService.buscarPorIdSga(idSga);
                        if (ofertaAcademica == null) {
                            return;
                        }
                        proyectoCarreraOferta = new ProyectoCarreraOferta(sessionProyecto.getProyectoSeleccionado(), c.getId(), ofertaAcademica.getId(),
                                Boolean.TRUE);
                        
                        if (contieneCarrera(proyectoCarreraOfertas, proyectoCarreraOferta) == false) {
                            proyectoCarreraOfertaService.guardar(proyectoCarreraOferta);
                            this.grabarIndividuoPCO(proyectoCarreraOferta);
                            logDao.create(logDao.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "CREAR", "Carrera="
                                    + proyectoCarreraOferta.getCarreraId() + "|Oferta=" + proyectoCarreraOferta.getOfertaAcademicaId() + "|Proyecto= "
                                    + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
                        }
                    }
                    proyectoCarreraOferta.setEsActivo(true);
                    proyectoCarreraOfertaDao.edit(proyectoCarreraOferta);
                    logDao.create(logDao.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "EDITAR", "Carrera="
                            + proyectoCarreraOferta.getCarreraId() + "|Oferta=" + proyectoCarreraOferta.getOfertaAcademicaId()
                            + "|Proyecto= " + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
                }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * DEVUELVE EL ID DE UN PROYECTO CARRERA EXISTENTE
     *
     * @param proyectoCarreras
     * @param pc
     * @return
     */
    private Long devuelveProyectoCarreraId(List<ProyectoCarreraOferta> proyectoCarreras, ProyectoCarreraOferta pc) {
        Long var = 0L;
        for (ProyectoCarreraOferta pco : proyectoCarreras) {
            if (pco.getCarreraId().equals(pc.getCarreraId())) {
                var = pco.getId();
                break;
            }
        }
        return var;
    }
    
    public void eliminarProyectoCarreras() {
        try {
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            for (ProyectoCarreraOferta pco : sessionProyecto.getCarrerasRemovidasTransfer()) {
                Long id = devuelveProyectoCarreraId(sessionProyecto.getCarrerasRemovidasTransfer(), pco);
                ProyectoCarreraOferta proyectoCarreraOfertaBuscar = new ProyectoCarreraOferta(id);
                ProyectoCarreraOferta proyectoCarreraOferta = proyectoCarreraOfertaService.buscarPorId(proyectoCarreraOfertaBuscar);
                if (proyectoCarreraOferta != null) {
                    proyectoCarreraOferta.setEsActivo(false);
                    proyectoCarreraOfertaService.editar(proyectoCarreraOferta);
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * PERMITIRE ALMACENAR LA ONTOLOGIAS RESPECTO A LOS PROYECTO CARRERA OFERTA
     *
     * @param proyectoCarreraOferta
     */
    private void grabarIndividuoPCO(final ProyectoCarreraOferta proyectoCarreraOferta) {
        /**
         * PERIODO ACADEMICO ONTOLOGÍA
         */
        OfertaAcademica ofertaAcademica = ofertaAcademicaService.find(proyectoCarreraOferta.getOfertaAcademicaId());
        PeriodoAcademico periodoAcademico = ofertaAcademica.getPeriodoAcademicoId();
        PeriodoAcademicoOntDTO periodoAcademicoOntDTO = new PeriodoAcademicoOntDTO(periodoAcademico.getId(), "S/N",
                periodoAcademico.getFechaInicio(), periodoAcademico.getFechaFin());
        cabeceraController.getOntologyService().getPeriodoAcademicoOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getPeriodoAcademicoOntService().write(periodoAcademicoOntDTO);
        /**
         * OFERTA ACADEMICA ONTOLOGÍA
         */
        OfertaAcademicaOntDTO ofertaAcademicaOntDTO = new OfertaAcademicaOntDTO(ofertaAcademica.getId(), ofertaAcademica.getNombre(),
                ofertaAcademica.getFechaInicio(), ofertaAcademica.getFechaFin(), periodoAcademicoOntDTO);
        cabeceraController.getOntologyService().getOfertaAcademicoOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getOfertaAcademicoOntService().write(ofertaAcademicaOntDTO);

        /**
         * NIVEL ACADEMICO ONTOLOGIA
         */
        Carrera carrera = carreraService.find(proyectoCarreraOferta.getCarreraId());
        Nivel nivel = carrera.getNivelId();
        NivelAcademicoOntDTO nivelAcademicoOntDTO = new NivelAcademicoOntDTO(nivel.getId(), nivel.getNombre(),
                cabeceraController.getValueFromProperties(PropertiesFileEnum.URI, "nivel_academico"));
        cabeceraController.getOntologyService().getNivelAcademicoOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getNivelAcademicoOntService().write(nivelAcademicoOntDTO);
        /**
         * AREA ACADEMICA ONTOLOGIA
         */
        AreaAcademicaOntDTO areaAcademicaOntDTO = new AreaAcademicaOntDTO(carrera.getAreaId().getId(), "UNIVERSIDAD NACIONAL DE LOJA",
                carrera.getAreaId().getNombre(), carrera.getAreaId().getSigla(),
                cabeceraController.getValueFromProperties(PropertiesFileEnum.URI, "area_academica"));
        cabeceraController.getOntologyService().getAreaAcademicaOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getAreaAcademicaOntService().write(areaAcademicaOntDTO);
        /**
         * CARRERA ONTOLOGÍA
         */
        CarreraOntDTO carreraOntDTO = new CarreraOntDTO(carrera.getId(), carrera.getNombre(), carrera.getSigla(), nivelAcademicoOntDTO,
                areaAcademicaOntDTO);
        cabeceraController.getOntologyService().getCarreraOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getCarreraOntService().write(carreraOntDTO);
        /**
         * PROYECTO CARRERA OFERTA ONTOLOGY
         */
        
        ProyectoCarreraOfertaOntDTO proyectoCarreraOfertaOntDTO = new ProyectoCarreraOfertaOntDTO(proyectoCarreraOferta.getId(),
                ofertaAcademicaOntDTO, sessionProyecto.getProyectoOntDTO(), carreraOntDTO);
        cabeceraController.getOntologyService().getProyectoCarreraOfertaOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getProyectoCarreraOfertaOntService().write(proyectoCarreraOfertaOntDTO);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="AUTORES">
    /**
     * PERMITE GRABAR AUTORES PERMITE CREAR ONTOLOGÍA Y ALMACENARLAS EN UN
     * ARCHIVO FORMATO RDF
     */
    public void grabarAutores() {
        try {
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            for (AutorProyectoDTO autorProyectoDTO : sessionProyecto.getAutoresProyectoDTONuevos()) {
                if (autorProyectoDTO.getAutorProyecto().getId() == null) {
                    autorProyectoService.guardar(autorProyectoDTO.getAutorProyecto());
                    cabeceraController.getOntologyService().getAutorOntService().read(cabeceraController.getCabeceraWebSemantica());
                    grabarIndividuoAutor(autorProyectoDTO);
                    logDao.create(logDao.crearLog("AutorProyecto", autorProyectoDTO.getAutorProyecto().getId() + "", "CREAR",
                            "|Aspirante= " + autorProyectoDTO.getAutorProyecto().getAspiranteId().getId() + "|Proyecto= "
                            + sessionProyecto.getProyectoSeleccionado().getId(), sessionUsuario.getUsuario()));
                    continue;
                }
                autorProyectoService.editar(autorProyectoDTO.getAutorProyecto());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void grabarIndividuoAutor(final AutorProyectoDTO autorProyectoDTO) {
        AutorOntDTO autorOntDTO = new AutorOntDTO(autorProyectoDTO.getAspirante().getId(),
                autorProyectoDTO.getPersona().getNombres(), autorProyectoDTO.getPersona().getApellidos(), autorProyectoDTO.getPersona().getFechaNacimiento(),
                itemService.buscarPorId(autorProyectoDTO.getPersona().getGeneroId()).getNombre(), autorProyectoDTO.getPersona().getEmail());
        cabeceraController.getOntologyService().getAutorOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getAutorOntService().write(autorOntDTO);
        cabeceraController.getOntologyService().getAutorProyectoOntService().read(cabeceraController.getCabeceraWebSemantica());
        cabeceraController.getOntologyService().getAutorProyectoOntService().write(new AutorProyectoOntDTO(
                autorProyectoDTO.getAspirante().getId(), sessionProyecto.getProyectoOntDTO(), autorOntDTO));
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CONFIGURACIONES">
    private void listadoConfiguracionesProyecto() {
        this.sessionProyecto.getConfiguracionProyectos().clear();
        this.sessionProyecto.setConfiguracionProyectos(this.configuracionProyectoService.buscar(
                new ConfiguracionProyecto(sessionProyecto.getProyectoSeleccionado().getId() != null ? sessionProyecto.getProyectoSeleccionado() : null,
                        null, null, null, null)));
    }

    /**
     * AGREGAR CONFIGURACIONES A PROYECTO
     *
     */
    private void agregarConfiguracionesProyecto() {
        List<ConfiguracionProyecto> configuracionProyectos = configuracionProyectoService.buscar(
                new ConfiguracionProyecto(sessionProyecto.getProyectoSeleccionado().getId() != null ? sessionProyecto.getProyectoSeleccionado() : null,
                        null, null, null, null));
        if (configuracionProyectos != null) {
            return;
        }
        if (!configuracionProyectos.isEmpty()) {
            return;
        }
        ConfiguracionProyecto configuracionProyectoDS = new ConfiguracionProyecto(
                sessionProyecto.getProyectoSeleccionado(), ConfiguracionProyectoEnum.DIASSEMANA.getTipo(), "7",
                ConfiguracionProyectoEnum.DIASSEMANA.getTipo(), TipoConfiguracionEnum.NUMERICO.getTipo());
        sessionProyecto.getConfiguracionProyectos().add(configuracionProyectoDS);
        ConfiguracionProyecto configuracionProyectoHD = new ConfiguracionProyecto(
                sessionProyecto.getProyectoSeleccionado(), ConfiguracionProyectoEnum.HORASDIARIAS.getTipo(), "8",
                ConfiguracionProyectoEnum.HORASDIARIAS.getTipo(), TipoConfiguracionEnum.NUMERICO.getTipo());
        sessionProyecto.getConfiguracionProyectos().add(configuracionProyectoHD);
        ConfiguracionProyecto configuracionProyectoCD = new ConfiguracionProyecto(
                sessionProyecto.getProyectoSeleccionado(), ConfiguracionProyectoEnum.CATALOGODURACION.getTipo(), "1",
                ConfiguracionProyectoEnum.CATALOGODURACION.getTipo(), TipoConfiguracionEnum.SELECCIONMULTIPLE.getTipo());
        sessionProyecto.getConfiguracionProyectos().add(configuracionProyectoCD);
    }
    
    private void grabarConfiguraciones() {
        for (ConfiguracionProyecto configuracionProyecto : sessionProyecto.getConfiguracionProyectos()) {
            if (configuracionProyecto.getId() == null) {
                configuracionProyectoService.guardar(configuracionProyecto);
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TEMAS">
    private void crearTema() {
        sessionProyecto.setTemaProyecto(new TemaProyecto(sessionProyecto.getProyectoSeleccionado(), new Tema(null, null, "Ninguna"), Boolean.TRUE));
    }
    
    private void editarTema() {
        List<TemaProyecto> temaProyectos = temaProyectoService.buscar(new TemaProyecto(sessionProyecto.getProyectoSeleccionado().getId() != null
                ? sessionProyecto.getProyectoSeleccionado() : null, null, Boolean.TRUE));
        if (temaProyectos == null) {
            sessionProyecto.setTemaProyecto(new TemaProyecto(sessionProyecto.getProyectoSeleccionado(), new Tema(), Boolean.TRUE));
            return;
        }
        TemaProyecto temaProyecto = !temaProyectos.isEmpty() ? temaProyectos.get(0) : null;
        if (temaProyecto == null) {
            sessionProyecto.setTemaProyecto(new TemaProyecto(sessionProyecto.getProyectoSeleccionado(), new Tema(), Boolean.TRUE));
            return;
        }
        sessionProyecto.setTemaProyecto(temaProyecto);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="DOCENTES">
    /**
     * BUSCAR LOS DOCENTES QUE PERTENECEN AL PROYECTO SELECCIONADO
     */
    public void buscarDocentes() {
        try {
            List<DocenteProyecto> docenteProyectos = docenteProyectoService.buscar(new DocenteProyecto(
                    sessionProyecto.getProyectoSeleccionado(), null, null, Boolean.TRUE, null));
            if (docenteProyectos.isEmpty()) {
                return;
            }
            for (DocenteProyecto docenteProyecto : docenteProyectos) {
                List<DocenteCarrera> docenteCarreras = docenteCarreraDao.buscar(new DocenteCarrera(null, docenteService.buscarPorId(new Docente(docenteProyecto.getDocenteCarreraId())),
                        null, Boolean.TRUE));
                if (docenteCarreras.isEmpty()) {
                    continue;
                }
                DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, personaDao.find(docenteProyecto.getDocenteCarreraId()),
                        docenteCarreras.get(0));
                sessionProyecto.getDocentesProyectoDTO().add(docenteProyectoDTO);
            }
            sessionProyecto.setFilterDocentesProyectoDTO(sessionProyecto.getDocentesProyectoDTO());
        } catch (Exception e) {
        }
    }

    /**
     * GRABAR DOCENTES
     */
    public void grabarDocentes() {
        if (!sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.INICIO.getTipo())) {
            return;
        }
        if (sessionProyecto.getDocentesProyectoDTO().isEmpty()) {
            return;
        }
        for (DocenteProyectoDTO docenteProyectoDTO : sessionProyecto.getDocentesProyectoDTO()) {
            if (docenteProyectoDTO.getDocenteProyecto().getId() == null) {
                docenteProyectoService.guardar(docenteProyectoDTO.getDocenteProyecto());
                enviarEmailDocente(docenteProyectoDTO);
                continue;
            }
            docenteProyectoService.actualizar(docenteProyectoDTO.getDocenteProyecto());
            enviarEmailDocente(docenteProyectoDTO);
        }
    }
    
    private void enviarEmailDocente(final DocenteProyectoDTO docenteProyectoDTO) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        cabeceraController.getMailDTO().setMensaje(bundle.getString("lbl.estimado") + ": "
                + docenteProyectoDTO.getPersona().getNombres() + " " + docenteProyectoDTO.getPersona().getApellidos() + " "
                + bundle.getString("lbl.msm_asignacion_docente") + " " + docenteProyectoDTO.getDocenteProyecto().getProyectoId().getTemaActual() + ""
                + "  " + ";" + bundle.getString("lbl.msm_nota_asignacion_docente") + " "
                + cabeceraController.getConfiguracionGeneralDTO().getTiempoMaximoPertinencia() + " " + bundle.getString("lbl.dias"));
        cabeceraController.getMailDTO().setArchivo("");
        cabeceraController.getMailDTO().setDestino(docenteProyectoDTO.getPersona().getEmail());
        cabeceraController.getMailDTO().setDatosDestinario(docenteProyectoDTO.getPersona().getNombres() + " "
                + docenteProyectoDTO.getPersona().getApellidos());
        cabeceraController.getMailService().enviar(cabeceraController.getMailDTO());
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="DOCUMENTOS">
    private void buscarDocumentos() {
        try {
            if (sessionProyecto.getProyectoSeleccionado().getId() == null) {
                return;
            }
            List<DocumentoProyecto> documentoProyectos = this.documentoProyectoService.buscar(
                    new DocumentoProyecto(Boolean.TRUE, null, sessionProyecto.getProyectoSeleccionado()));
            for (DocumentoProyecto documentoProyecto : documentoProyectos) {
                DocumentoProyectoDTO documentoProyectoDTO = new DocumentoProyectoDTO(
                        documentoProyecto, documentoService.buscarPorId(new Documento(documentoProyecto.getDocumentoId())));
                documentoProyectoDTO.getDocumento().setCatalogo(itemService.buscarPorId(documentoProyectoDTO.getDocumento().getCatalogoId()).getNombre());
                sessionProyecto.getDocumentosProyectoDTO().add(documentoProyectoDTO);
            }
            
            sessionProyecto.setFilterDocumentosProyectoDTO(sessionProyecto.getDocumentosProyectoDTO());
            sessionProyecto.getDocumentosProyectosDTOAgregados().addAll(sessionProyecto.getDocumentosProyectoDTO());
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }
    
    private void grabarDocumentos() {
        try {
            String ruta = configuracionDao.buscar(new Configuracion(ConfiguracionEnum.RUTADOCUMENTOPROYECTO.getTipo())).get(0).getValor();
            for (DocumentoProyectoDTO documentoProyectoDTO : sessionProyecto.getDocumentosProyectosDTOAgregados()) {
                if (documentoProyectoDTO.getDocumentoProyecto().getId() == null) {
                    documentoProyectoDTO.getDocumento().setRuta("S/N");
                    documentoService.guardar(documentoProyectoDTO.getDocumento());
                    documentoProyectoDTO.getDocumentoProyecto().setDocumentoId(documentoProyectoDTO.getDocumento().getId());
                    documentoProyectoService.guardar(documentoProyectoDTO.getDocumentoProyecto());
                    documentoProyectoDTO.getDocumento().setRuta(ruta + "/documento" + documentoProyectoDTO.getDocumento().getId() + ".pdf");
                    cabeceraController.getUtilService().generaDocumento(new File(documentoProyectoDTO.getDocumento().getRuta()),
                            documentoProyectoDTO.getDocumento().getContents());
                    documentoService.actualizar(documentoProyectoDTO.getDocumento());
                    continue;
                }
                documentoProyectoService.actualizar(documentoProyectoDTO.getDocumentoProyecto());
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedCrear() {
        sessionProyecto.setRenderedCrear(Boolean.FALSE);
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_proyecto");
        if (tienePermiso == 1) {
            sessionProyecto.setRenderedCrear(Boolean.TRUE);
        }
    }
    
    public void renderedEditar() {
        sessionProyecto.setRenderedEditar(Boolean.FALSE);
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_proyecto");
        if (tienePermiso == 1) {
            sessionProyecto.setRenderedEditar(Boolean.TRUE);
        }
    }

    /**
     * PERMITIR RENDERIZAR DATOS DE PROYECTO SI EL PROYECTO ESTA EN INICIO
     */
    public void renderedInicio() {
        sessionProyecto.setRenderedInicio(Boolean.FALSE);
        if (sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
            sessionProyecto.setRenderedInicio(Boolean.TRUE);
        }
    }

    //</editor-fold>
}
