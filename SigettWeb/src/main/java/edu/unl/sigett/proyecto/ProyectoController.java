package edu.unl.sigett.proyecto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.TipoValorEnum;
import com.jlmallas.comun.service.ItemService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.service.CarreraService;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.unl.sigett.autor.dto.AutorProyectoDTO;
import edu.unl.sigett.dao.ConfiguracionProyectoDao;
import edu.unl.sigett.dao.DirectorDao;
import edu.unl.sigett.dao.DocenteProyectoDao;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.dao.TemaProyectoDao;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import edu.unl.sigett.docenteProyecto.DocenteProyectoDTO;
import edu.unl.sigett.dto.ProyectoDTO;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.DocenteProyecto;
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
import edu.unl.sigett.service.DocenteProyectoService;
import edu.unl.sigett.service.LineaInvestigacionProyectoService;
import edu.unl.sigett.service.LineaInvestigacionService;
import edu.unl.sigett.service.ProyectoCarreraOfertaService;
import edu.unl.sigett.service.ProyectoService;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.entity.Usuario;
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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ProyectoService proyectoService;
    @EJB
    private ProyectoCarreraOfertaService proyectoCarreraOfertaService;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private LineaInvestigacionProyectoService lineaInvestigacionProyectoService;
    @EJB
    private ConfiguracionProyectoDao configuracionProyectoDao;
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
    private DocenteDao docenteDao;
    @EJB
    private TemaProyectoDao temaProyectoDao;

    //</editor-fold>
    public ProyectoController() {
    }

    public void init() {
        this.buscar();
        this.listadoCarreras();
        this.listadoOfertasAcademicas();
        this.renderedCrear();
        this.renderedEditar();
    }

    public void initEditar() {
        sessionProyecto.getCarrerasRemovidasTransfer().clear();
        sessionProyecto.getCarrerasSeleccionadasTransfer().clear();
        sessionProyecto.getLineasInvestigacionRemovidosTransfer().clear();
        sessionProyecto.getLineasInvestigacionSeleccionadasTransfer().clear();
        estadoActual();
        this.renderedPostulacion();
        pickListLineasInvestigacionProyecto(sessionProyecto.getProyectoSeleccionado());
        pickListCarreras(sessionProyecto.getProyectoSeleccionado());
        this.listadoConfiguracionesProyecto();
        this.agregarConfiguracionesProyecto();
        this.listadoTipos();
        this.listadoCategorias();
    }
    //<editor-fold defaultstate="collapsed" desc="PROYECTO">

    public String crear() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_proyecto");
            if (tienePermiso == 1) {
                Calendar fechaActual = Calendar.getInstance();
                sessionProyecto.setProyectoSeleccionado(new Proyecto(itemService.buscarPorCatalogoCodigo(
                        CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.INICIO.getTipo()).getId(),
                        Long.MIN_VALUE, Long.MIN_VALUE, "NINGUNA", fechaActual.getTime(), ""));
                sessionProyecto.getCronograma().setProyecto(sessionProyecto.getProyectoSeleccionado());
                sessionProyecto.getCronograma().setFechaInicio(fechaActual.getTime());
                sessionProyecto.getCronograma().setFechaFin(fechaActual.getTime());
                sessionProyecto.getCronograma().setFechaProrroga(fechaActual.getTime());
                sessionProyecto.getCronograma().setDuracion(0.0);
                sessionProyecto.getAutoresProyectoDTONuevos().clear();
                crearTema();
                buscarDocentes();
                return "pretty:crearProyecto";
            }
            if (tienePermiso == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * LISTAR TIPOS DE PROYECTO
     */
    private void listadoTipos() {
        sessionProyecto.getTipos().clear();
        sessionProyecto.setTipos(itemService.buscarPorCatalogo(CatalogoEnum.TIPOPROYECTO.getTipo()));
    }

    /**
     * SELECCIONAR TIPO DE PROYECTO PARA REALIZAR OPERACIONES EN LA VISTA:
     * DETERMINAR SI UN ASPIRANTE ES APTO A REALIZAR UN TT SIEMPRE Y CUANDO EL
     * TIPO DEL PROYECTO SELECCIONADO SEA TRABAJO DE TITULACION
     */
    public void seleccionarTipoProyecto() {
        Item tipo = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPOPROYECTO.getTipo(), sessionProyecto.getTipo());
        sessionProyecto.setTipoSeleccionado(tipo);
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
                    new ProyectoDTO(new Proyecto(sessionProyecto.getEstadoSeleccionado().getId(), null, null, null, null, null),
                            new ProyectoCarreraOferta(null, sessionProyecto.getCarreraSeleccionada().getId() != null
                                    ? sessionProyecto.getCarreraSeleccionada().getId() : null,
                                    sessionProyecto.getOfertaAcademicaSeleccionada().getId() != null
                                    ? sessionProyecto.getOfertaAcademicaSeleccionada().getId() : null, Boolean.TRUE),
                            sessionProyecto.getLineaInvestigacionProyectoSeleccionada()));

            if (proyectosEncontrados == null) {
                return;
            }
            for (Proyecto proyecto : proyectosEncontrados) {
                proyecto.setEstado(itemService.buscarPorId(proyecto.getEstadoProyectoId()).toString());
                proyecto.setCatalogo(itemService.buscarPorId(proyecto.getCatalogoProyectoId()).toString());
                proyecto.setTipo(itemService.buscarPorId(proyecto.getTipoProyectoId()).toString());
                proyecto.setAutores(autores(proyecto));
                if (!this.sessionProyecto.getProyectos().contains(proyecto)) {
                    this.sessionProyecto.getProyectos().add(proyecto);
                }
            }
            sessionProyecto.setFilterProyectos(sessionProyecto.getProyectos());
        } catch (Exception e) {
            e.printStackTrace();
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
        List<AutorProyecto> autorProyectos = autorProyectoService.buscar(new AutorProyecto(proyecto, null,
                itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo()).getId(), null, null));
        if (autorProyectos == null) {
            return "";
        }
        int contador = 0;
        for (AutorProyecto autorProyecto : autorProyectos) {
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
            this.sessionProyecto.setCarreras(sessionUsuarioCarrera.getCarreras());
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
            sessionProyecto.getLineasInvestigacionProyecto().clear();
            sessionProyecto.getFilterLineasInvestigacionProyecto().clear();
            List<LineaInvestigacion> lineasInvestigacionCarrera = lineaInvestigacionService.buscarPorCarrera(
                    new LineaInvestigacionCarrera(null, sessionProyecto.getCarreraSeleccionada().getId()));
            if (lineasInvestigacionCarrera == null) {
                return;
            }
            for (LineaInvestigacion lc : lineasInvestigacionCarrera) {
                LineaInvestigacionProyecto lp = lineaInvestigacionProyectoService.buscarPorId(
                        new LineaInvestigacionProyecto(null, lc, null));
                lp.setCount(lineaInvestigacionProyectoService.count(new LineaInvestigacionProyecto(null, lc, null)));
                if (!sessionProyecto.getLineasInvestigacionProyecto().contains(lp)) {
                    sessionProyecto.getLineasInvestigacionProyecto().add(lp);
                }
            }
            sessionProyecto.setFilterLineaInvestigacionProyecto(sessionProyecto.getLineasInvestigacionProyecto());
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
    //<editor-fold defaultstate="collapsed" desc="LINEAS DE INVESTIGACION PROYECTO">
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
        } catch (Exception e) {
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
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CARRERA DE PROYECTO">

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
            e.printStackTrace();
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
            if (sessionProyecto.getProyectoSeleccionado().getId() != null) {
                sessionProyecto.setProyectoSeleccionado(proyectoService.buscarPorId(sessionProyecto.getProyectoSeleccionado()));
            }
            Item estado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.INICIO.getTipo());

            if (sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId().equals(estado.getId())) {
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
//                            grabarIndividuoProyectoOfertaCarrera(proyecto, proyectoCarreraOferta);
                            logFacadeLocal.create(logFacadeLocal.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "CREAR", "Carrera="
                                    + proyectoCarreraOferta.getCarreraId() + "|Oferta=" + proyectoCarreraOferta.getOfertaAcademicaId() + "|Proyecto= "
                                    + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
                        }
                    }
                    proyectoCarreraOferta.setEsActivo(true);
                    proyectoCarreraOfertaDao.edit(proyectoCarreraOferta);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "EDITAR", "Carrera="
                            + proyectoCarreraOferta.getCarreraId() + "|Oferta=" + proyectoCarreraOferta.getOfertaAcademicaId()
                            + "|Proyecto= " + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                        grabarIndividuoProyectoOfertaCarrera(proyecto, proyectoCarreraOferta);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="AUTORES">
    public void grabarAutores() {
        try {
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            for (AutorProyectoDTO autorProyectoDTO : sessionProyecto.getAutoresProyectoDTONuevos()) {
                if (autorProyectoDTO.getAutorProyecto().getId() == null) {
                    autorProyectoService.guardar(autorProyectoDTO.getAutorProyecto());
//                        grabarIndividuoInvestigadorAutor(proyecto, autorProyecto);
                    logFacadeLocal.create(logFacadeLocal.crearLog("AutorProyecto", autorProyectoDTO.getAutorProyecto().getId() + "", "CREAR",
                            "|Aspirante= " + autorProyectoDTO.getAutorProyecto().getAspiranteId().getId() + "|Proyecto= "
                            + sessionProyecto.getProyectoSeleccionado().getId(), sessionUsuario.getUsuario()));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CONFIGURACIONES PROYECTO">
    private void listadoConfiguracionesProyecto() {
        this.sessionProyecto.getConfiguracionProyectos().clear();
        this.sessionProyecto.setConfiguracionProyectos(this.configuracionProyectoDao.buscar(
                new ConfiguracionProyecto(sessionProyecto.getProyectoSeleccionado().getId() != null ? sessionProyecto.getProyectoSeleccionado() : null,
                        null, null, null, null)));
    }

    /**
     * AGREGAR CONFIGURACIONES A PROYECTO
     *
     */
    private void agregarConfiguracionesProyecto() {
        List<ConfiguracionProyecto> configuracionProyectos = configuracionProyectoDao.buscar(
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
                ConfiguracionProyectoEnum.DIASSEMANA.getTipo(), TipoValorEnum.NUMERICO.getTipo());
        sessionProyecto.getConfiguracionProyectos().add(configuracionProyectoDS);
        ConfiguracionProyecto configuracionProyectoHD = new ConfiguracionProyecto(
                sessionProyecto.getProyectoSeleccionado(), ConfiguracionProyectoEnum.HORASDIARIAS.getTipo(), "8",
                ConfiguracionProyectoEnum.HORASDIARIAS.getTipo(), TipoValorEnum.NUMERICO.getTipo());
        sessionProyecto.getConfiguracionProyectos().add(configuracionProyectoHD);
        ConfiguracionProyecto configuracionProyectoCD = new ConfiguracionProyecto(
                sessionProyecto.getProyectoSeleccionado(), ConfiguracionProyectoEnum.CATALOGODURACION.getTipo(), "1",
                ConfiguracionProyectoEnum.CATALOGODURACION.getTipo(), TipoValorEnum.SELECCIONMULTIPLE.getTipo());
        sessionProyecto.getConfiguracionProyectos().add(configuracionProyectoCD);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TEMAS">
    private void crearTema() {
        sessionProyecto.setTemaProyecto(new TemaProyecto(sessionProyecto.getProyectoSeleccionado(), new Tema(), Boolean.TRUE));
    }

    private void editarTema() {
        List<TemaProyecto> temaProyectos = temaProyectoDao.buscar(new TemaProyecto(sessionProyecto.getProyectoSeleccionado().getId() != null
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
        this.sessionProyecto.getDocentesProyectoDTO().clear();
        try {
            List<DocenteProyecto> docenteProyectos = docenteProyectoService.buscar(new DocenteProyecto(
                    sessionProyecto.getProyectoSeleccionado(), null, null, Boolean.TRUE));
            if (docenteProyectos.isEmpty()) {
                return;
            }
            for (DocenteProyecto docenteProyecto : docenteProyectos) {
                List<DocenteCarrera> docenteCarreras = docenteCarreraDao.buscar(new DocenteCarrera(null, docenteDao.find(docenteProyecto.getDocenteId()),
                        null, Boolean.TRUE));
                if (docenteCarreras.isEmpty()) {
                    continue;
                }
                DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, personaDao.find(docenteProyecto.getDocenteId()),
                        null, docenteCarreras.get(0));
                docenteProyectoDTO.setDirector(directorDao.find(docenteProyectoDTO.getDirector().getId()));
                sessionProyecto.getDocentesProyectoDTO().add(docenteProyectoDTO);
            }
            sessionProyecto.setFilterDocentesProyectoDTO(sessionProyecto.getDocentesProyectoDTO());
        } catch (Exception e) {
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
    public void renderedPostulacion() {
        sessionProyecto.setRenderedInicio(Boolean.FALSE);
        Item item = itemService.buscarPorId(sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId() != null
                ? sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId() : null);
        if (item == null) {
            sessionProyecto.setRenderedInicio(Boolean.TRUE);
            return;
        }
        if (item.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
            sessionProyecto.setRenderedInicio(Boolean.TRUE);
        }
    }
    //</editor-fold>
}
