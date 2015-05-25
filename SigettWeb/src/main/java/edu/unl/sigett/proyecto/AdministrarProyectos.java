/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.proyecto;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.ItemDao;
import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.TipoValorEnum;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.academico.controlador.AdministrarEstudiantesCarrera;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.adjudicacion.controlador.AdministrarDirectoresProyecto;
import edu.unl.sigett.adjudicacion.controlador.AdministrarProrrogas;
import edu.unl.sigett.comun.controlador.AdministrarCatalogoDuracion;
import edu.unl.sigett.comun.controlador.AdministrarNotificaciones;
import edu.unl.sigett.academico.managed.session.SessionPeriodoAcademico;
import edu.unl.sigett.finalizacion.controlador.AdministrarActas;
import edu.unl.sigett.finalizacion.controlador.AdministrarEvaluacionesTribunal;
import edu.unl.sigett.finalizacion.controlador.AdministrarMiembrosTribunal;
import edu.unl.sigett.finalizacion.controlador.AdministrarTribunales;
import edu.unl.sigett.reportes.AdministrarReportes;
import edu.unl.sigett.seguimiento.controlador.AdministrarActividades;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.CatalogoDocumentoExpediente;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.DocumentoExpediente;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.Expediente;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.unl.sigett.entity.Prorroga;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.entity.RenunciaAutor;
import edu.unl.sigett.entity.RenunciaDirector;
import edu.unl.sigett.entity.TemaProyecto;
import org.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.CatalogoDocumentoExpedienteFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.ConfiguracionProyectoDao;
import edu.unl.sigett.dao.CronogramaFacadeLocal;
import edu.unl.sigett.dao.DirectorProyectoFacadeLocal;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.unl.sigett.dao.DocenteProyectoFacadeLocal;
import edu.unl.sigett.dao.DocumentoProyectoFacadeLocal;
import edu.unl.sigett.dao.ExpedienteFacadeLocal;
import edu.unl.sigett.dao.LineaInvestigacionCarreraDao;
import edu.unl.sigett.dao.LineaInvestigacionDao;
import edu.unl.sigett.dao.LineaInvestigacionProyectoDao;
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.dao.ProyectoDao;
import edu.unl.sigett.dao.TemaProyectoDao;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoDirectorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.dto.ProyectoDTO;
import edu.unl.sigett.entity.Tema;
import edu.unl.sigett.enumeration.ConfiguracionProyectoEnum;
import edu.unl.sigett.postulacion.controlador.AutorProyectoPostulacionController;
import edu.unl.sigett.autor.dto.AutorProyectoDTO;
import edu.unl.sigett.postulacion.controlador.AdministrarConfiguracionesProyecto;
import edu.unl.sigett.postulacion.controlador.AdministrarCronograma;
import edu.unl.sigett.postulacion.controlador.AdministrarDocentesProyecto;
import edu.unl.sigett.postulacion.controlador.AdministrarDocumentosExpediente;
import edu.unl.sigett.postulacion.controlador.AdministrarDocumentosProyecto;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.LineaInvestigacionProyectoService;
import edu.unl.sigett.service.LineaInvestigacionService;
import edu.unl.sigett.service.ProyectoCarreraOfertaService;
import edu.unl.sigett.service.ProyectoService;
import edu.unl.sigett.util.MessageView;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
 class AdministrarProyectos implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    SessionProyecto sessionProyecto;
    @Inject
    private SessionPeriodoAcademico sessionPeriodoAcademico;
    @Inject
    private AdministrarProrrogas administrarProrrogas;
    @Inject
    private AdministrarConfiguracionesProyecto administrarConfiguracionesProyecto;
    @Inject
    private AdministrarCatalogoDuracion administrarCatalogoDuracion;
    @Inject
    private AdministrarDocentesProyecto administrarDocentesProyecto;
    @Inject
    private AdministrarDirectoresProyecto administrarDirectoresProyecto;
    @Inject
    AutorProyectoPostulacionController administrarAutoresProyecto;
    @Inject
    private AdministrarDocumentosProyecto administrarDocumentosProyecto;
    @Inject
    private AdministrarActividades administrarActividades;
    @Inject
    private AdministrarTribunales administrarTribunales;
    @Inject
    private AdministrarEvaluacionesTribunal administrarEvaluacionesTribunal;
    @Inject
    private AdministrarEstudiantesCarrera administrarEstudiantesCarrera;
    @Inject
    private AdministrarDocumentosExpediente administrarDocumentosExpediente;
    @Inject
    private AdministrarCronograma administrarCronograma;
    @Inject
    private AdministrarNotificaciones administrarNotificaciones;
    @Inject
    private AdministrarMiembrosTribunal administrarMiembrosTribunal;
    @Inject
    private AdministrarActas administrarActas;
//</editor-fold>
    private AdministrarReportes administrarReportes;
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ProyectoDao proyectoDao;
    @EJB
    private ProyectoService proyectoService;
    @EJB
    private ProyectoCarreraOfertaService proyectoCarreraOfertaService;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private LineaInvestigacionDao lineaInvestigacionFacadeLocal;
    @EJB
    private LineaInvestigacionProyectoService lineaInvestigacionProyectoService;
    @EJB
    private DocumentoProyectoFacadeLocal documentoProyectoFacadeLocal;
    @EJB
    private LineaInvestigacionCarreraDao lineaInvestigacionCarreraDao;
    @EJB
    private DocenteProyectoFacadeLocal docenteProyectoFacadeLocal;
    @EJB
    private UsuarioCarreraDao usuarioCarreraDao;
    @EJB
    private DocenteCarreraDao docenteCarreraFacadeLocal;
    @EJB
    private TemaProyectoDao temaProyectoDao;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private ConfiguracionProyectoDao configuracionProyectoDao;
    @EJB
    private CarreraService carreraService;
    @EJB
    private DirectorProyectoFacadeLocal directorProyectoFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private ProyectoOfertaCarreraDao proyectoCarreraOfertaFacadeLocal;
    @EJB
    private CatalogoDocumentoExpedienteFacadeLocal catalogoDocumentoExpedienteFacadeLocal;
    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    private ExpedienteFacadeLocal expedienteFacadeLocal;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private CronogramaFacadeLocal cronogramaFacadeLocal;
    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteDao;
    @EJB
    private EstudianteCarreraDao estudianteCarreraDao;
    @EJB
    private PersonaDao personaDao;
    @EJB
    private DocenteDao docenteDao;
    @EJB
    private AutorProyectoService autorProyectoService;
    @EJB
    private ItemService itemService;
    @EJB
    private LineaInvestigacionService lineaInvestigacionService;
    //</editor-fold>
    private MessageView messageView;
    private DualListModel<LineaInvestigacion> lineasInvestigacionDualList;
    private List<LineaInvestigacionProyecto> lineaInvestigacionProyectosRemovidos;
    private List<LineaInvestigacionProyecto> lineaInvestigacionProyectosAgregados;
    private LineaInvestigacion li;
    private List<Carrera> carreras;
    private List<LineaInvestigacion> lineaInvestigacions;
    private DualListModel<Carrera> carrerasDualList;
    private List<ProyectoCarreraOferta> proyectoCarreraOfertasRemovidos;
    private List<ProyectoCarreraOferta> proyectoCarreraOfertasAgregados;
    private List<Docente> docentesTarget = new ArrayList<>();
    private List<Docente> docenteProyectosFuente = new ArrayList<>();
    private List<LineaInvestigacionCarrera> lineaInvestigacionCarreras;

    private List<Proyecto> proyectosPorWS = new ArrayList<>();
    private List<Proyecto> proyectosInicio;
    private List<Proyecto> proyectosAdjudicacionDirector;
    private List<Proyecto> proyectosPorCulminar;
    private List<Proyecto> proyectosCaducados;
    private List<Proyecto> proyectosEnSustentacionPublica;
    private List<Proyecto> proyectosEnSustentacionPrivada;

    private boolean renderedCaducado;
    private boolean renderedEditarDatosProyecto;

    private String area;
    private String tipoProyecto;
    private String catalogoProyecto;
    private String estadoProyecto;
    private String autorRequisitos;

    public void AdministrarProyectos() {
        this.carrerasDualList = new DualListModel<>();
    }

    public void init() {
//        this.buscar();
        this.listadoCarreras();
//        this.listadoOfertasAcademicas();
        this.renderedCrear();
        this.renderedEditar();
    }

    public void initEditar() {
        this.buscarAutores(sessionProyecto.getProyectoSeleccionado());
//        pickListLineasInvestigacionProyecto(sessionProyecto.getProyectoSeleccionado());
        pickListCarreras(sessionProyecto.getProyectoSeleccionado());
        this.listadoConfiguracionesProyecto();
        this.agregarConfiguracionesProyecto();
        this.editarTema();
        this.listadoTipos();
        this.listadoCategorias();
        this.renderedEditarDatosProyecto();
    }

    //<editor-fold defaultstate="collapsed" desc="METODOS DE PROYECTO">
    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_proyecto");
            if (tienePermiso == 1) {
                Calendar fechaActual = Calendar.getInstance();
                sessionProyecto.setProyectoSeleccionado(new Proyecto(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE, "NINGUNA", fechaActual.getTime(), ""));
                sessionProyecto.getProyectoSeleccionado().getCronograma().setProyecto(sessionProyecto.getProyectoSeleccionado());
                sessionProyecto.getProyectoSeleccionado().getCronograma().setFechaInicio(fechaActual.getTime());
                sessionProyecto.getProyectoSeleccionado().getCronograma().setFechaFin(fechaActual.getTime());
                sessionProyecto.getProyectoSeleccionado().getCronograma().setFechaProrroga(fechaActual.getTime());
                sessionProyecto.getProyectoSeleccionado().getCronograma().setDuracion(0.0);
                /*----------------------------------RENDERED-----------------------------------------------------*/
//                administrarTipoProyectos.renderedCrear();
                administrarAutoresProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                administrarDocumentosProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
//                administrarTemaProyectos.renderedBuscar(sessionUsuario.getUsuario());
                administrarTribunales.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                administrarDocentesProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarConfiguracionesProyecto.renderedBuscar(sessionUsuario.getUsuario());
//                administrarCronograma.renderedCronograma(sessionProyecto.getProyectoSeleccionado());
                administrarProrrogas.tieneProrroga(sessionProyecto.getProyectoSeleccionado());
                /*LISTADOS*/
                this.lineaInvestigacionProyectosRemovidos = new ArrayList<>();
                this.lineaInvestigacionProyectosAgregados = new ArrayList<>();
                this.proyectoCarreraOfertasRemovidos = new ArrayList<>();
                this.proyectoCarreraOfertasAgregados = new ArrayList<>();
                docentesTarget = new ArrayList<>();
                docenteProyectosFuente = new ArrayList<>();
                this.lineaInvestigacions = new ArrayList<>();
                navegacion = "pretty:crearProyecto";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void editarPeriodo(SelectEvent event) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            OfertaAcademica of = (OfertaAcademica) event.getObject();
            String fechaCreate = formatoFecha.format(of.getFechaInicio());
//            buscar(fechaCreate, sessionUsuario.getUsuario());
        } catch (Exception e) {
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
            this.lineaInvestigacionCarreras = new ArrayList<>();
            List<LineaInvestigacionCarrera> lineasInvestigacionCarrera = lineaInvestigacionCarreraDao.buscar(
                    new LineaInvestigacionCarrera(null, sessionProyecto.getCarreraSeleccionada().getId()));
            if (lineasInvestigacionCarrera == null) {
                return;
            }
            for (LineaInvestigacionCarrera lc : lineasInvestigacionCarrera) {
                LineaInvestigacionProyecto lp = lineaInvestigacionProyectoService.buscarPorId(
                        new LineaInvestigacionProyecto(null, lc.getLineaInvestigacionId(), null));
                lp.setCount(lineaInvestigacionProyectoService.count(new LineaInvestigacionProyecto(null, lc.getLineaInvestigacionId(), null)));
                if (!sessionProyecto.getLineasInvestigacionProyecto().contains(lp)) {
                    sessionProyecto.getLineasInvestigacionProyecto().add(lp);
                }
            }
            sessionProyecto.setFilterLineaInvestigacionProyecto(sessionProyecto.getLineasInvestigacionProyecto());
//            buscar();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void seleccionarOfertaAcademica(SelectEvent event) {
        sessionProyecto.setOfertaAcademicaSeleccionada((OfertaAcademica) event.getObject());
//        buscar();
    }

    public void seleccionarLineaInvestigacion(SelectEvent event) {
        sessionProyecto.setLineaInvestigacionProyectoSeleccionada((LineaInvestigacionProyecto) event.getObject());
//        buscar();
    }

    public void abandonar(Proyecto proyecto, Usuario usuario) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            int tienePermiso = usuarioDao.tienePermiso(usuario, "editar_proyecto");
//            Calendar fechaActual = Calendar.getInstance();
//            if (fechaActual.after(proyecto.getCronograma().getFechaProrroga())) {
//                if (tienePermiso == 1) {
//                    if (proyecto.getId() != null) {
//                        EstadoProyecto estadoProyecto = estadoProyectoFacadeLocal.find(10);
//                        EstadoAutor estadoAutor = estadoAutorFacadeLocal.find(4);
//                        proyecto.setEstadoProyectoId(estadoProyecto);
//                        proyectoDao.edit(proyecto);
//                        for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
//                            autorProyecto.setEstadoAutorId(estadoAutor);
//                            autorProyectoFacadeLocal.edit(autorProyecto);
//                        }
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            }
        } catch (Exception e) {
        }
    }

    public String editar(Proyecto proyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_proyecto");
            if (tienePermiso == 1) {
                if (proyecto.getId() != null) {
                    proyecto = proyectoDao.find(proyecto.getId());
                }
                sessionProyecto.setProyectoSeleccionado(proyecto);
                tipoProyecto = proyecto.getTipoProyectoId().toString();
                estadoProyecto = proyecto.getEstadoProyectoId().toString();
                catalogoProyecto = proyecto.getCatalogoProyectoId().toString();
                sessionProyecto.getProyectoSeleccionado().setCronograma(cronogramaFacadeLocal.find(sessionProyecto.getProyectoSeleccionado().getId()));
//                pickListLineasInvestigacionProyecto(proyecto);
                obtenerLineasInvestigacionProyecto(proyecto);
//                listadoDocentes(proyecto);
                pickListCarreras(proyecto);
                this.lineaInvestigacionProyectosRemovidos = new ArrayList<>();
                this.lineaInvestigacionProyectosAgregados = new ArrayList<>();
                this.proyectoCarreraOfertasRemovidos = new ArrayList<>();
                this.proyectoCarreraOfertasAgregados = new ArrayList<>();
                docentesTarget = new ArrayList<>();
                docenteProyectosFuente = new ArrayList<>();
                List<TemaProyecto> temas = new ArrayList<>();
                temas.addAll(temaProyectoDao.buscarPorProyecto(proyecto.getId()));
                if (temas != null) {
                    if (!temas.isEmpty()) {
//                        administrarTemaProyectos.editar(temas.get(0), sessionUsuario.getUsuario());
                    } else {
//                        administrarTemaProyectos.crear(sessionUsuario.getUsuario());
                    }
                } else {
//                    administrarTemaProyectos.crear(sessionUsuario.getUsuario());
                }
                /*Refrescar Documentos Proyecto*/
                sessionProyecto.getProyectoSeleccionado().setDocumentoProyectoList(new ArrayList<DocumentoProyecto>());
                sessionProyecto.getProyectoSeleccionado().getDocumentoProyectoList().addAll(documentoProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                /*Refrescar Docentes Proyecto*/
                sessionProyecto.getProyectoSeleccionado().setDocenteProyectoList(new ArrayList<DocenteProyecto>());
                sessionProyecto.getProyectoSeleccionado().getDocenteProyectoList().addAll(docenteProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                /*Refrescar Directores Proyecto*/
                sessionProyecto.getProyectoSeleccionado().setDirectorProyectoList(new ArrayList<DirectorProyecto>());
                sessionProyecto.getProyectoSeleccionado().getDirectorProyectoList().addAll(directorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                /*Refrescar Autores Proyecto*/
                sessionProyecto.getProyectoSeleccionado().setAutorProyectoList(new ArrayList<AutorProyecto>());
                sessionProyecto.getProyectoSeleccionado().getAutorProyectoList().addAll(autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                /*New Prorrogas*/
                sessionProyecto.getProyectoSeleccionado().getCronograma().setProrrogaList(new ArrayList<Prorroga>());
                /*---------------------------------------------------RENDERED--------------------------------------------------------*/
                administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), proyecto);
                administrarAutoresProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarDocumentosProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), proyecto);
                administrarTribunales.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                administrarDocentesProyecto.renderedBuscar(sessionUsuario.getUsuario());
//                administrarCronograma.renderedCronograma(sessionProyecto.getProyectoSeleccionado());
                administrarConfiguracionesProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarProrrogas.tieneProrroga(proyecto);
                renderedCaducado(proyecto);
                navegacion = "pretty:editarProyecto";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public void obtenerLineasInvestigacionProyecto(Proyecto proyecto) {
        this.lineaInvestigacions = new ArrayList<>();
        for (LineaInvestigacionProyecto lineaInvestigacionProyecto : proyecto.getLineaInvestigacionProyectoList()) {
            lineaInvestigacions.add(lineaInvestigacionProyecto.getLineaInvestigacionId());
        }
    }

    /**
     * DEVOLVER LOS AUTORES DE UN PROYECTO
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

    public boolean verificarAutores(Proyecto proyecto) {
        boolean var = true;
        for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
            if (autorProyecto.getAspiranteId().getEsApto() == false) {
                var = false;
                break;
            }
        }
        return var;
    }

//    public String confirmarGuardar(Proyecto proyecto, TemaProyecto temaProyecto) {
//        String navegacion = "";
//        autorRequisitos = "";
//        boolean var = false;
//        try {
//            if (comparaLineasInvestigacionProyectoYDocente(proyecto) == true) {
//                var = true;
//            } else {
//                var = false;
//                RequestContext.getCurrentInstance().execute("confirmLineasInvestigacion.show()");
//            }
//            if (var) {
//                if (compruebaRequisitosAutor(proyecto) == true) {
//                    var = true;
//                } else {
//                    var = false;
//                    RequestContext.getCurrentInstance().execute("confirmRequisitos.show()");
//                }
//            }
//            if (var) {
//                navegacion = grabar(proyecto, temaProyecto);
//            }
//        } catch (Exception e) {
//        }
//
//        return navegacion;
//    }
//    public String confirmarGuardarEditar(Proyecto proyecto, TemaProyecto temaProyecto) {
//        String navegacion = "";
//        autorRequisitos = "";
//        boolean var = false;
//        try {
//            if (comparaLineasInvestigacionProyectoYDocente(proyecto) == true) {
//                var = true;
//            } else {
//                var = false;
//                RequestContext.getCurrentInstance().execute("confirmLineasInvestigacion1.show()");
//            }
//            if (var) {
//                if (compruebaRequisitosAutor(proyecto) == true) {
//                    var = true;
//                } else {
//                    var = false;
//                    RequestContext.getCurrentInstance().execute("confirmRequisitos1.show()");
//                }
//            }
//            if (var) {
//                navegacion = grabar(proyecto, temaProyecto);
//            }
//        } catch (Exception e) {
//        }
//
//        return navegacion;
//    }
    public boolean comparaLineasInvestigacionProyectoYDocente(Proyecto proyecto) {
        boolean var = false;
        List<LineaInvestigacionProyecto> lips = new ArrayList<>();
        for (Object o : lineasInvestigacionDualList.getTarget()) {
            int v = o.toString().indexOf(":");
            Long id = Long.parseLong(o.toString().substring(0, v));
            LineaInvestigacion li = lineaInvestigacionFacadeLocal.find(id);
            LineaInvestigacionProyecto lp = new LineaInvestigacionProyecto();
            lp.setLineaInvestigacionId(li);
            lp.setProyectoId(sessionProyecto.getProyectoSeleccionado());
            lips.add(lp);
        }
        if (proyecto.getId() != null) {
            if (!proyecto.getDocenteProyectoList().isEmpty()) {
                for (LineaInvestigacionProyecto lineaInvestigacionProyecto : lips) {
                    for (DocenteProyecto docenteProyecto : proyecto.getDocenteProyectoList()) {
                        if (docenteProyecto.getEsActivo()) {
//                            for (LineaInvestigacionDocente ld : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docenteProyecto.getDocenteId())) {
//                                if (lineaInvestigacionProyecto.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
//                                    var = true;
//                                    break;
//                                }
//                            }
                        }
                    }
                }
            } else {
                var = true;
            }
        } else {
            var = true;
        }
        return var;
    }

    public void cambiarEstadoAutoresProyecto(Proyecto proyecto) {
        try {
//            for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
//                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                    if (autorProyecto.getId() != null) {
//                        EstadoAutor estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.SEGUIMIENTO.getTipo());
//                        autorProyecto.setEstadoAutorId(estadoAutor);
//                        autorProyectoFacadeLocal.edit(autorProyecto);
//                    }
//                }
//            }
        } catch (Exception e) {
        }
    }

    public void transferProyectoCarrera(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Integer id = Integer.parseInt(item.toString().substring(0, v));
                Carrera c = carreraService.find(id);
                ProyectoCarreraOferta proyectoCarreraOferta = new ProyectoCarreraOferta();
                proyectoCarreraOferta.setCarreraId(c.getId());
                if (event.isRemove()) {
                    proyectoCarreraOfertasAgregados.remove(proyectoCarreraOferta);
                    proyectoCarreraOfertasRemovidos.add(proyectoCarreraOferta);
                } else {
                    proyectoCarreraOfertasAgregados.add(proyectoCarreraOferta);
                    if (contieneCarrera(proyectoCarreraOfertasRemovidos, proyectoCarreraOferta)) {
                        proyectoCarreraOfertasRemovidos.remove(proyectoCarreraOferta);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Long devuelveProyectoCarrera(List<ProyectoCarreraOferta> proyectoCarreras, ProyectoCarreraOferta pc) {
        Long var = (long) 0;
        for (ProyectoCarreraOferta pco : proyectoCarreras) {
            if (pco.getCarreraId().equals(pc.getCarreraId())) {
                var = pco.getId();
                break;
            }
        }
        return var;
    }

    public boolean contieneCarrera(List<ProyectoCarreraOferta> carrerasProyecto, ProyectoCarreraOferta carreraProyecto) {
        boolean var = false;
        for (ProyectoCarreraOferta pco : carrerasProyecto) {
            if (pco.getCarreraId().equals(carreraProyecto.getCarreraId())) {
                var = true;
            }
        }
        return var;
    }

    public boolean contieneLineaInvestigacion(List<LineaInvestigacionProyecto> lineaInvestigacionProyectos, LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        boolean var = false;
        for (LineaInvestigacionProyecto lp : lineaInvestigacionProyectos) {
            if (lp.getLineaInvestigacionId().equals(lineaInvestigacionProyecto.getLineaInvestigacionId())) {
                var = true;
            }
        }
        return var;
    }

    public Long devuelveDocenteProyecto(List<DocenteProyecto> docenteProyectos, DocenteProyecto docenteProyecto) {
        Long var = (long) 0;
        for (DocenteProyecto dp : docenteProyectos) {
            if (dp.getDocenteId().equals(docenteProyecto.getDocenteId())) {
                var = dp.getId();
            }
        }
        return var;
    }

    public Long devuelveLineaInvestigacion(List<LineaInvestigacionProyecto> lineaInvestigacionProyectos, LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        Long var = (long) 0;
        for (LineaInvestigacionProyecto lp : lineaInvestigacionProyectos) {
            if (lp.getLineaInvestigacionId().equals(lineaInvestigacionProyecto.getLineaInvestigacionId())) {
                var = lp.getId();
            }
        }
        return var;
    }

    public void transferLineasInvestigacion(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                LineaInvestigacion li = lineaInvestigacionFacadeLocal.find(id);
                LineaInvestigacionProyecto lp = new LineaInvestigacionProyecto();
                if (li != null) {
                    lp.setLineaInvestigacionId(li);
                }
                if (event.isRemove()) {
                    lineaInvestigacionProyectosAgregados.remove(lp);
                    lineaInvestigacionProyectosRemovidos.add(lp);
                    int pos = 0;
                    for (LineaInvestigacionProyecto lip : sessionProyecto.getProyectoSeleccionado().getLineaInvestigacionProyectoList()) {
                        if (!lip.getLineaInvestigacionId().equals(lp.getLineaInvestigacionId())) {
                            pos++;
                        } else {
                            break;
                        }
                    }
                    sessionProyecto.getProyectoSeleccionado().getLineaInvestigacionProyectoList().remove(pos);
                    lineaInvestigacions.remove(pos);
//                    listadoDocentes(sessionProyecto.getProyecto());

                } else {
                    if (event.isAdd()) {
                        if (contieneLineaInvestigacion(sessionProyecto.getProyectoSeleccionado().getLineaInvestigacionProyectoList(), lp)) {
                            lineaInvestigacionProyectosRemovidos.remove(lp);
                        }
                        sessionProyecto.getProyectoSeleccionado().getLineaInvestigacionProyectoList().add(lp);
                        lineaInvestigacions.add(lp.getLineaInvestigacionId());
                        lineaInvestigacionProyectosAgregados.add(lp);
//                        listadoDocentes(sessionProyecto.getProyecto());
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }

    }

    public void onTabChange(TabChangeEvent event) {
        if (sessionProyecto.getProyectoSeleccionado() != null) {
            switch (event.getTab().getId()) {
                case "tabPropiedades":
                    /*-----------------------Configuraciones Proyecto----------------------------------------------------*/
                    administrarConfiguracionesProyecto.buscar(administrarConfiguracionesProyecto.getCriterio(), sessionProyecto.getProyectoSeleccionado());
                    administrarConfiguracionesProyecto.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Documentos Proyecto-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*------------------------------------------------------Docentes Proyecto----------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*---------------------------------------------Autores------------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*------------------------------------------MÉTODOS PRÓRROGAS---------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*-----------------------------------Documentos Expediente-------------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    break;
                case "tabAutores":
                    /*------------------------------------------Autores------------------------------------------------------------------------------*/
//                    administrarAutoresProyecto.buscar("", sessionProyecto.getProyecto(), sessionUsuario.getUsuario());
                    administrarAutoresProyecto.renderedBuscarAspirantes(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarAutoresProyecto.renderedCrear(sessionUsuario.getUsuario());
                    administrarAutoresProyecto.renderedEditar(sessionUsuario.getUsuario());
                    administrarAutoresProyecto.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarAutoresProyecto.renderedSeleccionar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*------------------------------------------------------Docentes Proyecto----------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    /*-------------------------------------------Tribunales--------------------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*--------------------------------------------MÉTODOS PRÓRROGAS--------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*------------------------------------------------------Documentos Expediente----------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    break;
                case "tabDocentesProyecto":
                    /*------------------------------DOCENTES---------------------------------------------------*/
                    administrarDocentesProyecto.buscar("", sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDocentesProyecto.renderedBuscar(sessionUsuario.getUsuario());
                    administrarDocentesProyecto.renderedEditar(sessionUsuario.getUsuario());
                    administrarDocentesProyecto.renderedBuscarEspecialista(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDocentesProyecto.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDocentesProyecto.renderedSeleccionarDocenteEspecialista(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDocentesProyecto.renderedSortearDocente(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    administrarDocentesProyecto.renderedImprimirOficio(sessionUsuario.getUsuario());
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*--------------------------------PRÓRROGAS----------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*------------------------------------------------------Documentos Expediente----------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    break;
                case "tabDocumentosProyecto":
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.buscar(sessionProyecto.getProyectoSeleccionado());
                    administrarDocumentosProyecto.renderedCrear(sessionUsuario.getUsuario());
                    administrarDocumentosProyecto.renderedEditar(sessionUsuario.getUsuario());
                    administrarDocumentosProyecto.renderedEliminar(sessionUsuario.getUsuario());
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*-----------------------------------------METODOS PRORROGAS-------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    break;
                case "tabDirectoresProyecto":
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.buscar("", sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDirectoresProyecto.renderedBuscarDirectorDisponible(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDirectoresProyecto.renderedEditar(sessionUsuario.getUsuario());
                    administrarDirectoresProyecto.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDirectoresProyecto.renderedSeleccionar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDirectoresProyecto.renderedSortearDirectorProyecto(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedImprimirOficio(sessionUsuario.getUsuario());
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*----------------------------------------------Documentos Expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*------------------------------------METODOS PRORROGAS-------------------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    break;
                case "tabProrrogas":
                    /*-------------------------------------------------MÉTODOS PRÓRROGAS---------------------------------------------------------*/
                    administrarProrrogas.buscar(sessionProyecto.getProyectoSeleccionado().getCronograma(), sessionUsuario.getUsuario(), "");
                    administrarProrrogas.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarProrrogas.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarProrrogas.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarProrrogas.renderedImprimirOficio(sessionUsuario.getUsuario());
                    administrarProrrogas.renderedAceptar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Documentos Expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*----------------------------------------------Docentes Proyecto-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    break;
                case "tabActividades":
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.buscarPorCronograma(administrarActividades.getCriterio(), sessionProyecto.getProyectoSeleccionado().getCronograma(), sessionUsuario.getUsuario());
                    administrarActividades.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarActividades.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarActividades.renderedCrearSubActividad(null, null);
                    administrarActividades.setRenderedDlgEditar(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Documentos expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*----------------------------------------------Prorrogas-------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    break;

                case "tabHistorialDirectores":
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.historialDirectoresProyecto(administrarDirectoresProyecto.getCriterioHistorialDirectorProyecto(), sessionProyecto.getProyectoSeleccionado());
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedImprimirOficio(sessionUsuario.getUsuario());
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Prorrogas-------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Documentos expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                case "tabDatosInicio":
                    /*----------------------------------------------Datos Proyectos-------------------------------------------------------*/
//                    renderedEditarDatosProyecto(sessionProyecto.getProyecto());
                    /*----------------------------------------------Cronograma-------------------------------------------------------*/
//                    administrarCronograma.renderedCronograma(sessionProyecto.getProyectoSeleccionado());
                    administrarCronograma.editarCronograma(sessionProyecto.getProyectoSeleccionado().getCronograma());
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*----------------------------------------------Documentos expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*----------------------------------------------Prorrogas-------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.tieneProrroga(sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    break;
                case "tabTribunales":
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.buscar(sessionProyecto.getProyectoSeleccionado(), sessionUsuario.getUsuario());
                    administrarTribunales.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarTribunales.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarTribunales.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    administrarEvaluacionesTribunal.renderedEditar(sessionUsuario.getUsuario());
                    administrarEvaluacionesTribunal.renderedEliminar(sessionUsuario.getUsuario());
                    administrarEvaluacionesTribunal.renderedCalificar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarEvaluacionesTribunal.listadoSustentacionesPorUsuarioCarrera(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarActas.setRenderedDlgActaGrado(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarMiembrosTribunal.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarMiembrosTribunal.renderedImprimirOficio(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarMiembrosTribunal.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Prorrogas-------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Docuementos Expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    break;
                case "tabLineasInvestigacion":
//                    pickListLineasInvestigacionProyecto(sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
//                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*----------------------------------------------Documentos expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*----------------------------------------------Prorrogas-------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyectoSeleccionado());
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);

                    break;
            }
        }
    }

    public void crearPDF() throws IOException, DocumentException {
//        Document pdf = new Document(PageSize.LEGAL.rotate());
//        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//        String logo = servletContext.getRealPath("") + File.separator + "resources/img" + File.separator + "selloInstitucion.png";
//        Image image = Image.getInstance(logo);
//        image.scaleToFit(50, 50);
//        image.setAbsolutePosition(50f, 550f);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfWriter.getInstance(pdf, baos);
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
//        response.setContentType("application/pdf");
//        response.addHeader("Content-disposition", "attachment; filename=Proyectos.pdf");
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        pdf.setMargins(20f, 20f, 20f, 20f);
//        Font fontHeader = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
//        Font fontTitle = FontFactory.getFont("Times-Roman", 18, Font.BOLD);
//        Paragraph title = new Paragraph(bundle.getString("lbl.listado") + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.proyectos"), fontTitle);
//        title.setSpacingAfter(20);
//        title.setAlignment(1);
//        pdf.open();
//        pdf.add(image);
//        pdf.add(title);
//        PdfPTable pdfTable = new PdfPTable(7);
//        pdfTable.setWidthPercentage(100f);
//        pdfTable.setHorizontalAlignment(0);
//        PdfPCell cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.tema"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.fecha") + " " + bundle.getString("lbl.inicio"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.fechaCulminación"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.categoria"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.estado"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.tipo"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.autores"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//        pdfTable.setHeaderRows(1);
//        for (Proyecto proyecto : proyectosPorWS) {
//            pdfTable.addCell(proyecto.getTemaActual());
//            pdfTable.addCell(configuracionGeneralFacadeLocal.dateFormat(proyecto.getCronograma().getFechaInicio()) + "");
//            pdfTable.addCell(configuracionGeneralFacadeLocal.dateFormat(proyecto.getCronograma().getFechaProrroga()) + "");
//            pdfTable.addCell(proyecto.getCatalogoProyectoId().getNombre());
//            pdfTable.addCell(estadoProyecto(proyecto));
//            pdfTable.addCell(proyecto.getTipoProyectoId().getNombre());
//            String autores = "";
//            int contador = 0;
//            for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
//                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
//                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//                    Persona persona = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
//                    if (contador == 0) {
////                        autores += "" + estudianteCarrera.getEstadoId().getNombre() + " " + persona.getNombres() + " " + persona.getApellidos() + " ";
//                        contador++;
//                    } else {
////                        autores += ", " + estudianteCarrera.getEstadoId().getNombre() + " " + persona.getNombres() + " " + persona.getApellidos();
//                        contador++;
//                    }
//                }
//            }
//            pdfTable.addCell(autores);
//        }
//        pdf.add(pdfTable);
//        pdf.close();
//        // the contentlength
//        response.setContentLength(baos.size());
//        // write ByteArrayOutputStream to the ServletOutputStream
//        OutputStream os = response.getOutputStream();
//        baos.writeTo(os);
//        os.flush();
//        os.close();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="METODOS DE GRABRADO EN CASCADA">
    public String grabar(Proyecto proyecto, TemaProyecto temaProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            Calendar fechaActual = Calendar.getInstance();
            /*Documentos Proyecto Temporal*/
            List<DocumentoProyecto> documentoProyectos = new ArrayList<>();
            documentoProyectos.addAll(proyecto.getDocumentoProyectoList());
            proyecto.setDocumentoProyectoList(new ArrayList<DocumentoProyecto>());
            /*Docentes Proyecto Temporal*/
            List<DocenteProyecto> docenteProyectos = new ArrayList<>();
            docenteProyectos.addAll(proyecto.getDocenteProyectoList());
            proyecto.setDocenteProyectoList(new ArrayList<DocenteProyecto>());
            /*Directores Proyecto Temporal*/
            List<DirectorProyecto> directorProyectos = new ArrayList<>();
            directorProyectos.addAll(proyecto.getDirectorProyectoList());
            proyecto.setDirectorProyectoList(new ArrayList<DirectorProyecto>());
            /*Autor Proyecto*/
            List<AutorProyecto> autorProyectos = new ArrayList<>();
            autorProyectos.addAll(proyecto.getAutorProyectoList());
            proyecto.setAutorProyectoList(new ArrayList<AutorProyecto>());
            /*Lineas de Investigacion Proyecto*/
            List<LineaInvestigacionProyecto> lineaInvestigacionProyectos = new ArrayList<>();
            lineaInvestigacionProyectos.addAll(proyecto.getLineaInvestigacionProyectoList());
            proyecto.setLineaInvestigacionProyectoList(new ArrayList<LineaInvestigacionProyecto>());
            /* Configuraciones Proyecto*/
            List<ConfiguracionProyecto> configuracionProyectos = new ArrayList<>();
            configuracionProyectos.addAll(proyecto.getConfiguracionProyectoList());
            proyecto.setConfiguracionProyectoList(new ArrayList<ConfiguracionProyecto>());
            /*Actividades*/
            proyecto.getCronograma().setActividadList(new ArrayList<Actividad>());
            /**/
            if (!autorProyectos.isEmpty()) {
                if (!carrerasDualList.getTarget().isEmpty()) {
                    if (proyecto.getId() == null) {
                        Cronograma cronograma = new Cronograma();
                        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_proyecto");
                        if (tienePermiso == 1) {
//                            EstadoProyecto ep = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.INICIO.getTipo());
//                            if (ep != null) {
//                                proyecto.setEstadoProyectoId(ep);
//                            }
                            proyecto.setFechaCreated(fechaActual.getTime());
                            if (proyecto.getCronograma() != null) {
                                cronograma = proyecto.getCronograma();
                            }
                            proyecto.setCronograma(null);
                            proyecto.setTemaActual(temaProyecto.getTemaId().getNombre());
                            /*Grabar Proyecto*/
                            proyectoDao.create(proyecto);
                            cronograma.setId(proyecto.getId());
                            /*Grabar Cronograma*/
                            cronogramaFacadeLocal.create(cronograma);
                            proyecto.setCronograma(cronograma);
                            /*Grabar Tema Proyecto*/
//                            administrarTemaProyectos.grabar(temaProyecto);
                            /*web semantica*/
//                            grabarIndividuoProyecto(proyecto);
                            /*GRABAR DOCUMENTOS PROYECTO*/
                            grabarDocumentosProyecto(proyecto, documentoProyectos);
                            proyecto.getDocumentoProyectoList().addAll(documentoProyectos);
                            /*GRABAR DOCENTES PROYECTO*/
                            grabarDocenteProyecto(proyecto, docenteProyectos, autorProyectos);
                            proyecto.getDocenteProyectoList().addAll(docenteProyectos);
                            /*GRABAR DIRECTORES PROYECTO*/
                            grabarDirectoresProyecto(directorProyectos, autorProyectos, proyecto);
                            proyecto.getDirectorProyectoList().addAll(directorProyectos);
                            /*Grabar Proyecto Carreras Oferta*/
                            grabarProyectoCarrerasOferta(proyecto);
                            /*Grabar Lineas de Investigacion Proyecto*/
                            grabarLineasInvestigacionProyecto(proyecto);
                            proyecto.getLineaInvestigacionProyectoList().addAll(lineaInvestigacionProyectos);
                            /*Grabar Autores Proyecto*/
                            grabarAutoresProyecto(autorProyectos, proyecto);
                            proyecto.setAutorProyectoList(autorProyectos);
                            /*Grabar Configuraciones*/
//                            grabarConfiguracionesProyecto(configuracionProyectos);
                            proyecto.setConfiguracionProyectoList(configuracionProyectos);
                            logFacadeLocal.create(logFacadeLocal.crearLog("Proyecto", proyecto.getId() + "", "CREAR", "|Tema= " + proyecto.getTemaActual() + "|Descripción= " + proyecto.getDescripcion() + "|Tipo de Proyecto= " + proyecto.getTipoProyectoId() + ""
                                    + "CatalogoProyecto= " + proyecto.getCatalogoProyectoId() + "|Estado= " + proyecto.getEstadoProyectoId(), sessionUsuario.getUsuario()));

                            if (param.equalsIgnoreCase("guardar")) {
                                sessionProyecto.setProyectoSeleccionado(new Proyecto());
                                navegacion = "pretty:proyectos";
                            } else {
                                if (param.equalsIgnoreCase("guardar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.proyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    sessionProyecto.setProyectoSeleccionado(new Proyecto());
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.proyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                }
                            }
                        } else {
                            if (tienePermiso == 2) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            } else {
                                navegacion = "login?faces-redirect=true";
                            }
                        }
                    } else {
                        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_proyecto");
                        if (tienePermiso == 1) {
                            Cronograma cr = proyecto.getCronograma();
                            proyecto.setCronograma((Cronograma) null);
                            /*Editar Proyecto*/
                            proyectoDao.edit(proyecto);
                            /*Editar Cronograma*/
//                            if (proyecto.getEstadoProyectoId().getId() == 2) {
//                                cronogramaFacadeLocal.edit(cr);
//                            }
                            proyecto.setCronograma(cr);
                            /*Editar Tema Proyecto*/
//                            administrarTemaProyectos.grabar(temaProyecto);
                            /*Web Semantica*/
//                            grabarIndividuoProyecto(proyecto);
                            /*GRABAR-ELIMINAR DOCUMENTOS PROYECTO*/
                            grabarDocumentosProyecto(proyecto, documentoProyectos);
                            removerDocumentosProyecto(proyecto, documentoProyectos);
                            proyecto.getDocumentoProyectoList().addAll(documentoProyectos);
                            /*Grabar-Eliminar Lineas de Investigacion Proyecto*/
                            removerLineasInvestigacionProyecto(proyecto);
                            grabarLineasInvestigacionProyecto(proyecto);
                            proyecto.getLineaInvestigacionProyectoList().addAll(lineaInvestigacionProyectos);
                            /*Grabar-Eliminar Proyecto Carrera Oferta*/
                            grabarProyectoCarrerasOferta(proyecto);
                            removerProyectoCarreras(proyecto);
                            /*GRABAR DOCENTES PROYECTO*/
                            grabarDocenteProyecto(proyecto, docenteProyectos, autorProyectos);
                            proyecto.getDocenteProyectoList().addAll(docenteProyectos);
                            proyecto.setDocenteProyectoList(docenteProyectos);
                            /*GRABAR DIRECTORES PROYECTO*/
                            grabarDirectoresProyecto(directorProyectos, autorProyectos, proyecto);
                            proyecto.getDirectorProyectoList().addAll(directorProyectos);
                            proyecto.setDirectorProyectoList(directorProyectos);
                            /*Grabar Autores Proyecto*/
                            grabarAutoresProyecto(autorProyectos, proyecto);
                            proyecto.setAutorProyectoList(autorProyectos);
                            /*Grabar Configuraciones*/
//                            grabarConfiguracionesProyecto(configuracionProyectos);
                            proyecto.setConfiguracionProyectoList(configuracionProyectos);
                            logFacadeLocal.create(logFacadeLocal.crearLog("Proyecto", proyecto.getId() + "", "EDITAR", "|Tema= " + proyecto.getTemaActual() + "|Descripción= " + proyecto.getDescripcion() + "|Tipo de Proyecto= " + proyecto.getTipoProyectoId() + ""
                                    + "CatalogoProyecto= " + proyecto.getCatalogoProyectoId() + "|Estado= " + proyecto.getEstadoProyectoId(), sessionUsuario.getUsuario()));

                            if (param.equalsIgnoreCase("guardar")) {
                                sessionProyecto.setProyectoSeleccionado(new Proyecto());
                                navegacion = "pretty:proyectos";
                            } else {
                                if (param.equalsIgnoreCase("guardar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.proyecto") + " " + bundle.getString("lbl.msm_editar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                    sessionProyecto.getProyectoSeleccionado().setDocumentoProyectoList(documentoProyectos);

                                } else {
                                    sessionProyecto.setProyectoSeleccionado(new Proyecto());
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.proyecto") + " " + bundle.getString("lbl.msm_editar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                }
                            }
                        } else {
                            if (tienePermiso == 2) {
                                navegacion = "";
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        }
                    }
                } else {
                    proyecto.setConfiguracionProyectoList(configuracionProyectos);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " " + bundle.getString("lbl.carrera"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                proyecto.setConfiguracionProyectoList(configuracionProyectos);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " " + bundle.getString("lbl.autor"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e + "", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public void grabarLineasInvestigacionProyecto(Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoDao.find(proyecto.getId());
                sessionProyecto.setProyectoSeleccionado(proyecto);
            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                for (LineaInvestigacionProyecto lineaInvestigacionProyecto : lineaInvestigacionProyectosAgregados) {
//                    if (lineaInvestigacionProyecto.getId() == null) {
//                        lineaInvestigacionProyecto.setProyectoId(proyecto);
//                        lineaInvestigacionProyectoFacadeLocal.create(lineaInvestigacionProyecto);
////                        grabarIndividuoLineaInvestigacionProyecto(proyecto, lineaInvestigacionProyecto);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionProyecto", lineaInvestigacionProyecto.getId() + "", "CREAR", "Proyecto=" + proyecto.getId() + "|LineaInvestigacion=" + lineaInvestigacionProyecto.getLineaInvestigacionId().getId(), sessionUsuario.getUsuario()));
//                    } else {
//                        lineaInvestigacionProyectoFacadeLocal.edit(lineaInvestigacionProyecto);
////                        grabarIndividuoLineaInvestigacionProyecto(proyecto, lineaInvestigacionProyecto);
//                    }
//                }
//            }
        } catch (Exception e) {
        }

    }

    public void grabarProyectoCarrerasOferta(Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoDao.find(proyecto.getId());
                sessionProyecto.setProyectoSeleccionado(proyecto);
            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                for (ProyectoCarreraOferta proyectoCarreraOferta : proyectoCarreraOfertasAgregados) {
//                    Carrera c = carreraService.find(proyectoCarreraOferta.getCarreraId());
//                    Long pcoId = devuelveProyectoCarrera(proyectoCarreraOfertaFacadeLocal.buscarPorProyecto(proyecto.getId()), proyectoCarreraOferta);
//                    proyectoCarreraOferta = proyectoCarreraOfertaFacadeLocal.find(pcoId);
//                    if (proyectoCarreraOferta == null) {
//                        proyectoCarreraOferta = new ProyectoCarreraOferta();
//                        proyectoCarreraOferta.setEsActivo(true);
////                        String valor = configuracionCarreraFacadeLocal.buscarPorCarreraId(c.getId(), "OA").getValor();
//                        String idOferta = null;
////                        if (valor != null) {
////                            idOferta = (valor);
////                        }
//                        OfertaAcademica of = null;
//                        if (idOferta != null) {
//                            of = ofertaAcademicaFacadeLocal.buscarPorIdSga(idOferta);
//                        }
//                        if (of != null) {
//                            proyectoCarreraOferta.setOfertaAcademicaId(of.getId());
//                        }
//                        proyectoCarreraOferta.setProyectoId(proyecto);
//                        proyectoCarreraOferta.setCarreraId(c.getId());
//                        if (contieneCarrera(proyectoCarreraOfertaFacadeLocal.buscarPorProyecto(proyecto.getId()), proyectoCarreraOferta) == false) {
//                            proyectoCarreraOfertaFacadeLocal.create(proyectoCarreraOferta);
////                            grabarIndividuoProyectoOfertaCarrera(proyecto, proyectoCarreraOferta);
//                            logFacadeLocal.create(logFacadeLocal.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "CREAR", "Carrera="
//                                    + proyectoCarreraOferta.getCarreraId() + "|Oferta=" + proyectoCarreraOferta.getOfertaAcademicaId() + "|Proyecto= "
//                                    + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                        }
//                    } else {
//                        proyectoCarreraOferta.setEsActivo(true);
//                        proyectoCarreraOfertaFacadeLocal.edit(proyectoCarreraOferta);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "EDITAR", "Carrera=" + proyectoCarreraOferta.getCarreraId() + "|Oferta=" + proyectoCarreraOferta.getOfertaAcademicaId()
//                                + "|Proyecto= " + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
////                        grabarIndividuoProyectoOfertaCarrera(proyecto, proyectoCarreraOferta);
//                    }
//                }
//            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void grabarDocenteProyecto(Proyecto proyecto, List<DocenteProyecto> docenteProyectos, List<AutorProyecto> autorProyectos) {
        if (proyecto.getId() != null) {
            proyecto = proyectoDao.find(proyecto.getId());
            sessionProyecto.setProyectoSeleccionado(proyecto);
        }
//        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//            String tiempoMaxPertinencia = configuracionGeneralFacadeLocal.find((int) 3).getValor();
//            boolean enviaNotificacioAutores = false;
//            Map<String, String> map = new HashMap<String, String>();
//            for (DocenteProyecto docenteProyecto : docenteProyectos) {
//                Persona persona = personaFacadeLocal.find(docenteProyecto.getDocenteId());
//                if (docenteProyecto.getId() == null) {
//                    docenteProyectoFacadeLocal.create(docenteProyecto);
//                    logFacadeLocal.create(logFacadeLocal.crearLog("DocenteProyecto", docenteProyecto.getId() + "", "CREAR", "|Docente= " + docenteProyecto.getDocenteId() + "|Proyecto= " + docenteProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                    docenteProyecto.setEsEditado(false);
//                    administrarNotificaciones.notificarAsignacionDocenteProyecto(docenteProyecto, tiempoMaxPertinencia);
//                    enviaNotificacioAutores = true;
//                    map.put("Docente", persona.getNombres() + " " + persona.getApellidos());
//                } else {
//                    if (docenteProyecto.isEsEditado()) {
//                        docenteProyectoFacadeLocal.edit(docenteProyecto);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("DocenteProyecto", docenteProyecto.getId() + "", "EDITAR", "NUEVO: |Docente= " + docenteProyecto.getDocenteId() + "|Proyecto= " + docenteProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                        docenteProyecto.setEsEditado(false);
//                        administrarNotificaciones.notificarAsignacionDocenteProyecto(docenteProyecto, tiempoMaxPertinencia);
//                        enviaNotificacioAutores = true;
//                        map.put("Docente", persona.getNombres() + " " + persona.getApellidos());
//                    }
//                }
//            }

//            if (enviaNotificacioAutores) {
//                administrarNotificaciones.notificarAsignacionDocenteProyectoAutores(autorProyectos, map);
//            }
//        }
    }

    public void grabarDirectoresProyecto(List<DirectorProyecto> directorProyectos, List<AutorProyecto> autorProyectos, Proyecto proyecto) {
        boolean enviaNotificacioAutores = false;
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoDao.find(proyecto.getId());
                sessionProyecto.setProyectoSeleccionado(proyecto);
            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                Map<String, String> map = new HashMap<String, String>();
//
//                for (DirectorProyecto directorProyecto : directorProyectos) {
//                    DocenteCarrera docenteCarrera = docenteCarreraFacadeLocal.find(directorProyecto.getDirectorId().getId());
//                    Persona persona = personaFacadeLocal.find(docenteCarrera.getDocenteId().getId());
//                    if (directorProyecto.getId() == null) {
//                        cambiarEstadoAutoresProyecto(sessionProyecto.getProyecto());
//                        directorProyectoFacadeLocal.create(directorProyecto);
////                        grabarIndividuoInvestigadorDirector(proyecto, directorProyecto);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("DirectorProyecto", directorProyecto.getId() + "", "CREAR", "|Docente= " + docenteCarrera.getDocenteId() + "|Proyecto= " + directorProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                        directorProyecto.setEsEditado(false);
//                        enviaNotificacioAutores = true;
//                        map.put("Docente", persona.getNombres() + " " + persona.getApellidos());
//                        administrarNotificaciones.notificarAsignacionDirectorProyecto(persona, directorProyecto);
//                    } else {
//                        if (directorProyecto.isEsEditado()) {
//                            directorProyecto.setRenunciaDirectorList(new ArrayList<RenunciaDirector>());
//                            cambiarEstadoAutoresProyecto(sessionProyecto.getProyecto());
//                            directorProyectoFacadeLocal.edit(directorProyecto);
////                            grabarIndividuoInvestigadorDirector(proyecto, directorProyecto);
//                            logFacadeLocal.create(logFacadeLocal.crearLog("DirectorProyecto", directorProyecto.getId() + "", "EDITAR", "|Docente= " + docenteCarrera.getDocenteId() + "|Proyecto= " + directorProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                            directorProyecto.setEsEditado(false);
//                            enviaNotificacioAutores = true;
//                            map.put("Docente", persona.getNombres() + " " + persona.getApellidos());
//                            administrarNotificaciones.notificarAsignacionDirectorProyecto(persona, directorProyecto);
//                        }
//                    }
//                }
//                if (enviaNotificacioAutores) {
//                    EstadoProyecto estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.SEGUIMIENTO.getTipo());
//                    if (estadoProyecto != null) {
//                        proyecto.setEstadoProyectoId(estadoProyecto);
//                        proyectoDao.edit(proyecto);
//                    }
//                    administrarNotificaciones.notificarAsignacionDirectorProyectoAutores(autorProyectos, map);
//                }
//            }
        } catch (Exception e) {
        }

    }

    public void grabarAutoresProyecto(List<AutorProyecto> autorProyectos, Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoDao.find(proyecto.getId());
                sessionProyecto.setProyectoSeleccionado(proyecto);
            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                for (AutorProyecto autorProyecto : autorProyectos) {
//                    List<Expediente> expedientes = new ArrayList<>();
//                    expedientes.addAll(autorProyecto.getExpedienteList());
//                    autorProyecto.setExpedienteList(new ArrayList<Expediente>());
//                    autorProyecto.setRenunciaAutorList(new ArrayList<RenunciaAutor>());
//                    if (autorProyecto.getId() == null) {
//                        autorProyectoFacadeLocal.create(autorProyecto);
////                        grabarIndividuoInvestigadorAutor(proyecto, autorProyecto);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("AutorProyecto", autorProyecto.getId() + "", "CREAR", "|Aspirante= " + autorProyecto.getAspiranteId().getId() + "|Proyecto= " + autorProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                        autorProyecto.setEsEditado(false);
//                        grabarExpedientes(expedientes);
////                        grabarIndividuoInvestigadorAutor(proyecto, autorProyecto);
//                    } else {
//                        if (autorProyecto.isEsEditado()) {
//                            autorProyectoFacadeLocal.edit(autorProyecto);
////                            grabarIndividuoInvestigadorAutor(proyecto, autorProyecto);
//                            logFacadeLocal.create(logFacadeLocal.crearLog("AutorProyecto", autorProyecto.getId() + "", "EDITAR", "|Aspirante= " + autorProyecto.getAspiranteId().getId() + "|Proyecto= " + autorProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                            autorProyecto.setEsEditado(false);
//                            grabarExpedientes(expedientes);
//                        }
//                        autorProyecto.getExpedienteList().addAll(expedientes);
//                    }
//                }
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void grabarExpedientes(List<Expediente> expedientes) {
        try {
            for (Expediente expediente : expedientes) {
                if (expediente.getId() == null) {
                    expedienteFacadeLocal.create(expediente);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Expediente", expediente.getId() + "", "CREAR", "|Nombre= " + expediente.getNombre() + "|Autor Proyecto= " + expediente.getAutorProyectoId().getId() + "|EsActivo= " + expediente.getEsActivo(), sessionUsuario.getUsuario()));
                } else {
                    expedienteFacadeLocal.edit(expediente);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Expediente", expediente.getId() + "", "EDITAR", "|Nombre= " + expediente.getNombre() + "|Autor Proyecto= " + expediente.getAutorProyectoId().getId() + "|EsActivo= " + expediente.getEsActivo(), sessionUsuario.getUsuario()));

                }
            }
        } catch (Exception e) {
        }
    }

    public void grabarDocumentosProyecto(Proyecto proyecto, List<DocumentoProyecto> documentoProyectos) {
        try {
            if (documentoProyectos != null) {
                for (DocumentoProyecto documentoProyecto : documentoProyectos) {
                    documentoProyecto.setProyectoId(proyecto);
                    if (documentoProyecto.getId() == null) {
                        documentoProyectoFacadeLocal.create(documentoProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoProyecto", documentoProyecto.getId() + "", "CREAR", "|TipoArchivo= " + documentoProyecto.getTipoArchivo() + "|Tamaño=" + documentoProyecto.getTamanio() + "|CatalogoDocumentoProyecto= " + documentoProyecto.getCatalogoDocumentoProyectoId() + "|Proyecto= " + documentoProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
                        documentoProyecto.setEsEditado(false);
                    } else {
                        if (documentoProyecto.isEsEditado()) {
                            documentoProyectoFacadeLocal.edit(documentoProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoProyecto", documentoProyecto.getId() + "", "EDITAR", "|TipoArchivo= " + documentoProyecto.getTipoArchivo() + "|Tamaño=" + documentoProyecto.getTamanio() + "|CatalogoDocumentoProyecto= " + documentoProyecto.getCatalogoDocumentoProyectoId() + "|Proyecto= " + documentoProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
                            documentoProyecto.setEsEditado(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

    }

/// </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    /**
     * PERMITIR RENDERIZAR DATOS DE PROYECTO SI EL PROYECTO ESTA EN INICIO
     */
    public void renderedEditarDatosProyecto() {
        sessionProyecto.setRenderedInicio(Boolean.FALSE);
        Item item = itemService.buscarPorId(sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId() != null
                ? sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId() : null);
        if (item == null) {
            return;
        }
        if (item.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
            renderedEditarDatosProyecto = true;
        }
    }

    public boolean renderedGeneraIndividuosProyectos(Usuario usuario) {
        boolean var = false;
        try {
            int tienePermiso = usuarioDao.tienePermiso(usuario, "crear_rdf_proyecto");
            if (tienePermiso == 1) {
                var = true;
            } else {
                var = false;
            }
        } catch (Exception e) {
        }
        return var;
    }

    public void renderedCaducado(Proyecto proyecto) {
        Calendar fechaActual = Calendar.getInstance();
        if (fechaActual.after(proyecto.getCronograma().getFechaProrroga())) {
            renderedCaducado = true;
        } else {
            renderedCaducado = false;
        }
    }

    public boolean renderedImprimir(Proyecto proyecto) {
        boolean rendered = false;
        if (proyecto.getId() != null) {
            rendered = true;
        }
        return rendered;
    }

    public boolean renderedBuscarLineaInvestigacionProyecto() {
        boolean var = false;
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "buscar_linea_investigacion_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedBuscarCarreraProyecto() {
        boolean var = false;
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "buscar_carrera_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

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

    // </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS WEB SEMANTICA">
//    public void grabarIndividuoProyecto(Proyecto proyecto) {
//        try {
//            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
//            ProyectoResource proyectoResource = new ProyectoResource(configuracionGeneralFacadeLocal.find(17).getValor());
//            Map mapProyecto = new HashMap();
//            mapProyecto.put("tema", proyecto.getTemaActual());
//            mapProyecto.put("descripcion", proyecto.getDescripcion());
//            mapProyecto.put("id", proyecto.getId());
//            mapProyecto.put("fecha", formatoFecha.format(proyecto.getFechaCreated()));
//            mapProyecto.put("tipo", proyecto.getCatalogoProyectoId().getNombre());
//            Map mapEstado = new HashMap();
//            mapEstado.put("id", proyecto.getEstadoProyectoId().getId());
//            mapEstado.put("nombre", proyecto.getEstadoProyectoId().getNombre());
//            proyectoResource.write(mapProyecto, mapEstado);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void grabarIndividuoProyectoOfertaCarrera(Proyecto proyecto, ProyectoCarreraOferta proyectoCarreraOferta) {
//        try {
//            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
//            Map datos = new HashMap();
//            datos.put("proyectoId", proyecto.getId());
//            datos.put("carreraId", proyectoCarreraOferta.getCarreraId());
//            datos.put("areaId", carreraService.find(proyectoCarreraOferta.getCarreraId()).getAreaId().getId());
//            datos.put("areaNombre", carreraService.find(proyectoCarreraOferta.getCarreraId()).getAreaId().getNombre());
//            datos.put("areaSigla", carreraService.find(proyectoCarreraOferta.getCarreraId()).getAreaId().getSigla());
//            datos.put("nivelId", carreraService.find(proyectoCarreraOferta.getCarreraId()).getNivelId().getId());
//            datos.put("nivelNombre", carreraService.find(proyectoCarreraOferta.getCarreraId()).getNivelId().getNombre());
//            datos.put("carreraNombre", carreraService.find(proyectoCarreraOferta.getCarreraId()).getNombre());
//            datos.put("carreraSigla", carreraService.find(proyectoCarreraOferta.getCarreraId()).getSigla());
//            datos.put("ofertaId", proyectoCarreraOferta.getOfertaAcademicaId());
//            datos.put("periodoId", ofertaAcademicaFacadeLocal.find(proyectoCarreraOferta.getOfertaAcademicaId()).getPeriodoAcademicoId().getId());
//            datos.put("periodoFechaInicio", formatoFecha.format(ofertaAcademicaFacadeLocal.find(proyectoCarreraOferta.getOfertaAcademicaId()).getPeriodoAcademicoId().getFechaInicio()));
//            datos.put("periodoFechaFin", formatoFecha.format(ofertaAcademicaFacadeLocal.find(proyectoCarreraOferta.getOfertaAcademicaId()).getFechaFin()));
//            datos.put("ofertaFechaInicio", formatoFecha.format(ofertaAcademicaFacadeLocal.find(proyectoCarreraOferta.getOfertaAcademicaId()).getFechaFin()));
//            datos.put("ofertaFechaFin", formatoFecha.format(ofertaAcademicaFacadeLocal.find(proyectoCarreraOferta.getOfertaAcademicaId()).getFechaFin()));
//            datos.put("proyectoCarreraOfertaId", proyectoCarreraOferta.getId());
//            ProyectoCarreraOfertaResource proyectoCarreraOfertaResource = new ProyectoCarreraOfertaResource(configuracionGeneralFacadeLocal.find(17).getValor());
//            proyectoCarreraOfertaResource.write(datos);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void grabarIndividuoInvestigadorAutor(Proyecto proyecto, AutorProyecto autorProyecto) {
//        try {
//            Map datos = new HashMap();
//            EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//            Persona persona = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
//            AutorProyectoResource autorProyectoResource = new AutorProyectoResource(configuracionGeneralFacadeLocal.find(17).getValor());
//            datos.put("proyectoId", proyecto.getId());
//            datos.put("personaId", persona.getId());
//            datos.put("personaNombres", persona.getNombres());
//            datos.put("personaApellidos", persona.getApellidos());
//            datos.put("personaNumeroIdentificacion", persona.getNumeroIdentificacion());
//            datos.put("personaEmail", persona.getEmail());
//            datos.put("aspiranteId", autorProyecto.getAspiranteId().getId());
//            datos.put("autorProyectoId", autorProyecto.getId());
//            autorProyectoResource.write(datos);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void grabarIndividuoLineaInvestigacionProyecto(Proyecto proyecto, LineaInvestigacionProyecto lp) {
//        try {
//            Map datos = new HashMap();
//            datos.put("proyectoId", proyecto.getId());
//            datos.put("lineaInvestigacionId", lp.getLineaInvestigacionId().getId());
//            datos.put("lineaInvestigacionNombre", lp.getLineaInvestigacionId().getNombre());
//            datos.put("lineaInvestigacionProyectoId", lp.getId());
//            LineasInvestigacionProyectoResource lineasInvestigacionProyectoResource = new LineasInvestigacionProyectoResource();
//            lineasInvestigacionProyectoResource.write(datos);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void grabarIndividuoInvestigadorDirector(Proyecto proyecto, DirectorProyecto directorProyecto) {
//        try {
//            Map datos = new HashMap();
//            DocenteCarrera docenteCarrera = docenteCarreraFacadeLocal.find(directorProyecto.getDirectorId().getId());
//            Persona persona = personaFacadeLocal.find(docenteCarrera.getDocenteId().getId());
//            datos.put("proyectoId", proyecto.getId());
//            datos.put("personaId", persona.getId());
//            datos.put("personaNombres", persona.getNombres());
//            datos.put("personaApellidos", persona.getApellidos());
//            datos.put("personaNumeroIdentificacion", persona.getNumeroIdentificacion());
//            datos.put("personaEmail", persona.getEmail());
//            datos.put("directorId", directorProyecto.getDirectorId().getId());
//            datos.put("docenteId", docenteCarrera.getDocenteId().getId());
//            datos.put("docenteTitulo", docenteCarrera.getDocenteId().getTituloDocenteId().getTituloId().getNombre());
//            datos.put("directorProyectoId", directorProyecto.getId());
//            DirectorProyectoResource directorProyectoResource = new DirectorProyectoResource(configuracionGeneralFacadeLocal.find(17).getValor());
//            directorProyectoResource.write(datos);
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void buscar(String filtro, Usuario usuario) {
//        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            int tienePermiso = usuarioDao.tienePermiso(usuario, "buscar_proyecto");
//            if (tienePermiso == 1) {
//                this.proyectosPorWS = new ArrayList<>();
//                ProyectoResource proyectoResource = new ProyectoResource(configuracionGeneralFacadeLocal.find(17).getValor());
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(usuario.getId())) {
//                    Map parametros = new HashMap();
//                    parametros.put("carreraId", usuarioCarrera.getCarreraId());
//                    parametros.put("filtro", filtro);
//                    List<String> valores = proyectoResource.buscarPorCarrera(parametros);
//                    for (String valor : valores) {
//                        Proyecto proyecto = proyectoDao.find(Long.parseLong(valor));
//                        if (proyecto != null && !this.proyectosPorWS.contains(proyecto)) {
//                            this.proyectosPorWS.add(proyecto);
//                        }
//                    }
//                }
//            } else {
//                if (tienePermiso == 2) {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void generaIndividuosRdf(Usuario usuario) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        try {
//            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_rdf_proyecto");
//            if (tienePermiso == 1) {
//
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(usuario.getId())) {
//                    for (ProyectoCarreraOferta proyectoCarreraOferta : proyectoCarreraOfertaFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
//                        grabarIndividuoProyecto(proyectoCarreraOferta.getProyectoId());
//                        grabarIndividuoProyectoOfertaCarrera(proyectoCarreraOferta.getProyectoId(), proyectoCarreraOferta);
//                        for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(proyectoCarreraOferta.getProyectoId().getId())) {
//                            grabarIndividuoInvestigadorAutor(proyectoCarreraOferta.getProyectoId(), autorProyecto);
//                        }
//                        for (DirectorProyecto directorProyecto : directorProyectoFacadeLocal.buscarPorProyecto(proyectoCarreraOferta.getProyectoId().getId())) {
//                            grabarIndividuoInvestigadorDirector(proyectoCarreraOferta.getProyectoId(), directorProyecto);
//                        }
//                        for (LineaInvestigacionProyecto lineaInvestigacionProyecto : lineaInvestigacionProyectoFacadeLocal.buscarPorProyecto(proyectoCarreraOferta.getProyectoId().getId())) {
//                            grabarIndividuoLineaInvestigacionProyecto(proyectoCarreraOferta.getProyectoId(), lineaInvestigacionProyecto);
//                        }
//                    }
//                }
//            } else {
//                if (tienePermiso == 2) {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS REMOVER EN CASCADA">
    public void removerDocumentosProyecto(Proyecto proyecto, List<DocumentoProyecto> documentoProyectos) {
        if (documentoProyectos != null) {
            for (DocumentoProyecto documentoProyecto : documentoProyectos) {
                if (documentoProyecto.getEsActivo() == false) {
                    if (documentoProyecto.getId() != null) {
                        logFacadeLocal.create(logFacadeLocal.crearLog("DocumentoProyecto", documentoProyecto.getId() + "", "ELIMINAR", "|TipoArchivo= " + documentoProyecto.getTipoArchivo() + "|Catalogo= " + documentoProyecto.getCatalogoDocumentoProyectoId().getId(), sessionUsuario.getUsuario()));
                        documentoProyectoFacadeLocal.remove(documentoProyecto);
                    }
                }
            }
        }
    }

    public void removerProyectoCarreras(Proyecto proyecto) {
        try {
//            if (proyecto.getId() != null) {
//                proyecto = proyectoDao.find(proyecto.getId());
//                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                    for (ProyectoCarreraOferta pco : proyectoCarreraOfertasRemovidos) {
//                        Long id = devuelveProyectoCarrera(proyectoCarreraOfertaFacadeLocal.buscarPorProyecto(proyecto.getId()), pco);
//                        ProyectoCarreraOferta proyectoCarreraOferta = null;
//                        proyectoCarreraOferta = proyectoCarreraOfertaFacadeLocal.find(id);
//                        if (proyectoCarreraOferta != null) {
//                            proyectoCarreraOferta.setEsActivo(false);
//                            proyectoCarreraOfertaFacadeLocal.edit(proyectoCarreraOferta);
//                            logFacadeLocal.create(logFacadeLocal.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "DESACTIVAR",
//                                    "NUEVO: |Carrera=" + proyectoCarreraOferta.getCarreraId() + "|Oferta="
//                                    + proyectoCarreraOferta.getOfertaAcademicaId() + "|Proyecto= " + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
//
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void removerLineasInvestigacionProyecto(Proyecto proyecto) {
//        if (proyecto.getId() != null) {
//            proyecto = proyectoDao.find(proyecto.getId());
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                for (LineaInvestigacionProyecto lp : lineaInvestigacionProyectosRemovidos) {
//                    Long id = devuelveLineaInvestigacion(lineaInvestigacionProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()), lp);
//                    LineaInvestigacionProyecto lineaInvestigacionProyecto = lineaInvestigacionProyectoFacadeLocal.find(id);
//                    if (lineaInvestigacionProyecto != null) {
//                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionProyecto", lineaInvestigacionProyecto.getId() + "", "CREAR", "NUEVO: |Proyecto=" + proyecto.getId() + "|LineaInvestigacion=" + lineaInvestigacionProyecto.getLineaInvestigacionId().getId(), sessionUsuario.getUsuario()));
//                        lineaInvestigacionProyectoFacadeLocal.remove(lineaInvestigacionProyecto);
//                    }
//                }
//            }
//        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PICKLIST">
    /**
     * BUSCAR LA LINEAS DE INVESTIGACION DEL PROYECTO Y DE LAS CARRERAS DEL
     * USUARIO PARA VISUALIZAR EN UN PICKLIST
     *
     * @param proyecto
     */
//    public void pickListLineasInvestigacionProyecto(Proyecto proyecto) {
//        this.sessionProyecto.getLineasInvestigacionDualList().getSource().clear();
//        this.sessionProyecto.getLineasInvestigacionDualList().getTarget().clear();
//        List<LineaInvestigacion> lineasInvestigacionProyecto = new ArrayList<>();
//        List<LineaInvestigacion> lineasInvestigacionCarrera = new ArrayList<>();
//        try {
//            lineasInvestigacionProyecto = lineaInvestigacionProyectoService.buscarLineaInvestigacion(
//                    new LineaInvestigacionProyecto(proyecto.getId() != null ? proyecto : null, null, null));
//
//            for (Carrera carrera : sessionProyecto.getCarreras()) {
//                List<LineaInvestigacion> lics = lineaInvestigacionService.buscarPorCarrera(new LineaInvestigacionCarrera(null, carrera.getId()));
//                if (lics == null) {
//                    continue;
//                }
//                for (LineaInvestigacion lic : lics) {
//                    if (!lineasInvestigacionCarrera.contains(lic)) {
//                        lineasInvestigacionCarrera.add(lic);
//                    }
//                }
//            }
//            sessionProyecto.setLineasInvestigacionDualList(new DualListModel<>(lineaInvestigacionService.diferenciaCarreraProyecto(
//                    lineasInvestigacionCarrera, lineasInvestigacionProyecto), lineasInvestigacionProyecto));
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
    /**
     * CARRERAS QUE PERTENECEN AL PROYECTO SELECCIONADO Y A LAS CARRERAS DEL
     * USUARIO PARA VISUALIZARLO EN UN PICKLIST
     *
     * @param proyecto
     */
    public void pickListCarreras(Proyecto proyecto) {
        sessionProyecto.getCarrerasProyecto().clear();
        sessionProyecto.getFilterCarrerasProyecto().clear();
        List<Carrera> carrerasProyecto = new ArrayList<>();
        List<Carrera> usuarioCarreras = new ArrayList<>();
        try {
//            List<ProyectoCarreraOferta> lips = proyectoCarreraOfertaService.buscar(new ProyectoCarreraOferta(proyecto, null, null));
//            if (lips != null) {
//                for (ProyectoCarreraOferta pco : lips) {
//                    Carrera c = carreraService.find(pco.getCarreraId());
//                    if (!carrerasProyecto.contains(c)) {
//                        carrerasProyecto.add(c);
//                    }
//                }
//            }

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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS LISTAR">
    /**
     * BUSCAR PROYECTOS POR CARRERA, OFERTA y Linea de Investigacion
     */
//    public void buscar() {
//        sessionProyecto.getProyectos().clear();
//        sessionProyecto.getFilterProyectos().clear();
//        try {
//            List<Proyecto> proyectosEncontrados = proyectoService.buscar(
//                    new ProyectoDTO(new Proyecto(sessionProyecto.getEstadoSeleccionado().getId(), null, null, null, null, null),
//                            new ProyectoCarreraOferta(null, sessionProyecto.getCarreraSeleccionada().getId() != null
//                                    ? sessionProyecto.getCarreraSeleccionada().getId() : null,
//                                    sessionProyecto.getOfertaAcademicaSeleccionada().getId() != null
//                                    ? sessionProyecto.getOfertaAcademicaSeleccionada().getId() : null),
//                            sessionProyecto.getLineaInvestigacionProyectoSeleccionada()));
//
//            if (proyectosEncontrados == null) {
//                return;
//            }
//            for (Proyecto proyecto : proyectosEncontrados) {
//                proyecto.setEstado(itemService.buscarPorId(proyecto.getEstadoProyectoId()).toString());
//                proyecto.setCatalogo(itemService.buscarPorId(proyecto.getCatalogoProyectoId()).toString());
//                proyecto.setTipo(itemService.buscarPorId(proyecto.getTipoProyectoId()).toString());
//                proyecto.setAutores(autores(proyecto));
//                if (!this.sessionProyecto.getProyectos().contains(proyecto)) {
//                    this.sessionProyecto.getProyectos().add(proyecto);
//                }
//            }
//            sessionProyecto.setFilterProyectos(sessionProyecto.getProyectos());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * BUSCAR OFERTAS ACADEMICAS DE LA CARRERAS ADMINISTRADAS POR EL USUARIO
     */
//    public void listadoOfertasAcademicas() {
//        this.sessionProyecto.getOfertaAcademicas().clear();
//        this.sessionProyecto.getFilterOfertaAcademicas().clear();
//        try {
//            for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscar(new UsuarioCarrera(sessionUsuario.getUsuario().getId(), null))) {
//                for (ProyectoCarreraOferta pco : proyectoCarreraOfertaService.buscar(
//                        new ProyectoCarreraOferta(null, usuarioCarrera.getCarreraId(), null))) {
//                    OfertaAcademica ofertaAcademica = ofertaAcademicaService.find(pco.getOfertaAcademicaId());
//                    if (!this.sessionProyecto.getOfertaAcademicas().contains(ofertaAcademica)) {
//                        this.sessionProyecto.getOfertaAcademicas().add(ofertaAcademica);
//                    }
//                }
//            }
//            sessionProyecto.setFilterOfertaAcademicas(this.sessionProyecto.getOfertaAcademicas());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void listadoCategorias() {
        sessionProyecto.getCategorias().clear();
        sessionProyecto.setCategorias(itemService.buscarPorCatalogo(CatalogoEnum.CATALOGOPROYECTO.getTipo()));
    }

    public void listadoTipos() {
        sessionProyecto.getTipos().clear();
        sessionProyecto.setTipos(itemService.buscarPorCatalogo(CatalogoEnum.TIPOPROYECTO.getTipo()));
    }

    /**
     * LISTADO DE CARRERA QUE ADMINISTRA EL USUARIO
     */
    public void listadoCarreras() {
        try {
            this.sessionProyecto.getCarreras().clear();
            this.sessionProyecto.getFilterCarreras().clear();
            for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscar(new UsuarioCarrera(sessionUsuario.getUsuario().getId(), null))) {
                Carrera carrera = carreraService.find(usuarioCarrera.getCarreraId());
                if (!sessionProyecto.getCarreras().contains(carrera)) {
                    this.sessionProyecto.getCarreras().add(carrera);
                }
            }
            this.sessionProyecto.setFilterCarreras(this.sessionProyecto.getCarreras());
        } catch (Exception e) {
        }
    }

    public int countProyectosPeriodoCarrera(OfertaAcademica of, Usuario usuario) {
        int count = 0;
        try {
            for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(usuario.getId())) {
                for (ProyectoCarreraOferta pco : proyectoCarreraOfertaFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
                    if (pco.getOfertaAcademicaId().equals(of)) {
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    public void buscarProyectosInicio(Usuario usuario) {
        proyectosInicio = new ArrayList<>();
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(usuario.getId())) {
            for (Proyecto proyecto : proyectoDao.buscarPorCarreraEstado(usuarioCarrera.getCarreraId(), EstadoProyectoEnum.INICIO.getTipo())) {
                List<DocenteProyecto> docenteProyectos = new ArrayList<>();
                docenteProyectos = docenteProyectoFacadeLocal.buscarPorProyecto(proyecto.getId());
                boolean tieneDocenteAsignado = false;
                if (!docenteProyectos.isEmpty()) {
                    for (DocenteProyecto docenteProyecto : docenteProyectos) {
                        if (docenteProyecto.getEsActivo()) {
                            tieneDocenteAsignado = true;
                            break;
                        }
                    }
                    if (tieneDocenteAsignado == false) {
                        if (!proyectosInicio.contains(proyecto)) {
                            proyectosInicio.add(proyecto);
                        }
                    }
                } else {
                    if (!proyectosInicio.contains(proyecto)) {
                        proyectosInicio.add(proyecto);
                    }
                }
            }
        }
    }

    public void buscarProyectosAdjudicaciónDirector(Usuario usuario) {
        proyectosAdjudicacionDirector = new ArrayList<>();
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(usuario.getId())) {
            for (Proyecto proyecto : proyectoDao.buscarPorCarreraEstado(usuarioCarrera.getCarreraId(), EstadoProyectoEnum.PERTINENTE.getTipo())) {
                List<DirectorProyecto> directorProyectos = new ArrayList<>();
                directorProyectos = directorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId());
                boolean tieneDirectorAsignado = false;
                if (!directorProyectos.isEmpty()) {
                    for (DirectorProyecto directorProyecto : directorProyectos) {
                        if (directorProyecto.getEstadoDirectorId().getCodigo().equalsIgnoreCase(EstadoDirectorEnum.INICIO.getTipo())) {
                            tieneDirectorAsignado = true;
                            break;
                        }
                    }
                    if (tieneDirectorAsignado == false) {
                        proyectosAdjudicacionDirector.add(proyecto);
                    }
                } else {
                    proyectosAdjudicacionDirector.add(proyecto);
                }
            }
        }
    }

    public void buscarProyectosEnSustentacionPublica(Usuario usuario) {
        proyectosEnSustentacionPublica = new ArrayList<>();
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(usuario.getId())) {
            for (Proyecto proyecto : proyectoDao.buscarPorCarreraEstado(usuarioCarrera.getCarreraId(), EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())) {
                proyectosEnSustentacionPublica.add(proyecto);
            }
        }

    }

    public void buscarProyectosEnSustentacionPrivada(Usuario usuario) {
        proyectosEnSustentacionPrivada = new ArrayList<>();
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(usuario.getId())) {
            for (Proyecto proyecto : proyectoDao.buscarPorCarreraEstado(usuarioCarrera.getCarreraId(), EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())) {
                proyectosEnSustentacionPrivada.add(proyecto);
            }
        }
    }

    /**
     * LISTAR DOCENTES PARA SELECCIONAR DEL PROYECTO
     *
     * @param proyecto
     */
//    public void listadoDocentes(Proyecto proyecto) {
//        List<Docente> docentesProyectos = new ArrayList<>();
//        List<Docente> docentes = new ArrayList<>();
//        try {
//            if (proyecto.getId() != null) {
//                for (DocenteProyecto docenteProyecto : proyecto.getDocenteProyectoList()) {
//                    if (docenteProyecto.getEsActivo()) {
//                        Docente docente = docenteDao.find(docenteProyecto.getDocenteId());
//                        docentesProyectos.add(docente);
//                    }
//                }
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
////                    for (DocenteCarrera docenteCarrera : docenteCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
////                        Docente docente = docenteDao.find(docenteCarrera.getDocenteId());
////                        for (LineaInvestigacionDocente ld : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId())) {
////                            for (LineaInvestigacionProyecto lp : proyecto.getLineaInvestigacionProyectoList()) {
////                                if (lp.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
////                                    if (docenteCarrera.isEsActivo()) {
////                                        if (!docentesProyectos.contains(docenteCarrera.getDocenteId())) {
////                                            if (!docentes.contains(docenteCarrera.getDocenteId())) {
////                                                docentes.add(docenteCarrera.getDocenteId());
////                                            }
////                                        }
////                                    }
////                                }
////                            }
////                        }
////                    }
//                }
//            } else {
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
////                    for (DocenteCarrera docenteCarrera : docenteCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
////                        Docente docente = docenteDao.find(docenteCarrera.getDocenteId());
////                        for (LineaInvestigacionDocente ld : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId())) {
////                            for (LineaInvestigacionProyecto lp : proyecto.getLineaInvestigacionProyectoList()) {
////                                if (lp.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
////                                    if (docenteCarrera.isEsActivo()) {
////                                        if (!docentes.contains(docenteCarrera.getDocenteId())) {
////                                            docentes.add(docenteCarrera.getDocenteId());
////                                        }
////                                    }
////                                }
////                            }
////                        }
////                    }
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
    public void buscarProyectosPorCulminar(Usuario usuario) {
        try {
            proyectosPorCulminar = new ArrayList<>();
            for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                for (Proyecto proyecto : proyectoDao.buscarPorCulminar(usuarioCarrera.getCarreraId())) {
//                    if (!proyectosPorCulminar.contains(proyecto)) {
//                        proyectosPorCulminar.add(proyecto);
//                    }
//                }
            }
        } catch (Exception e) {
        }
    }

    public void buscarProyectosCaducados(Usuario usuario) {
        try {
            proyectosCaducados = new ArrayList<>();
            for (UsuarioCarrera usuarioCarrera : usuarioCarreraDao.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                for (Proyecto proyecto : proyectoDao.buscarCaducados(usuarioCarrera.getCarreraId())) {
//                    if (!proyectosCaducados.contains(proyecto)) {
//                        proyectosCaducados.add(proyecto);
//                    }
//                }
            }
        } catch (Exception e) {
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TEMAS PROYECTO">
    private void editarTema() {
        TemaProyecto temaProyecto = temaProyectoDao.buscar(new TemaProyecto(sessionProyecto.getProyectoSeleccionado().getId() != null
                ? sessionProyecto.getProyectoSeleccionado() : null, null, Boolean.TRUE)).get(0);
        if (temaProyecto == null) {
            sessionProyecto.setTemaProyecto(new TemaProyecto(sessionProyecto.getProyectoSeleccionado(), new Tema(), Boolean.TRUE));
            return;
        }
        sessionProyecto.setTemaProyecto(temaProyecto);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CONFIGURACIONES PROYECTO">
    private void listadoConfiguracionesProyecto() {
        this.sessionProyecto.getConfiguracionProyectos().clear();
        this.sessionProyecto.setConfiguracionProyectos(this.configuracionProyectoDao.buscar(
                new ConfiguracionProyecto(sessionProyecto.getProyectoSeleccionado(), null, null, null, null)));
    }

    /**
     * AGREGAR CONFIGURACIONES A PROYECTO
     *
     */
    public void agregarConfiguracionesProyecto() {
        List<ConfiguracionProyecto> configuracionProyectos = configuracionProyectoDao.buscar(
                new ConfiguracionProyecto(sessionProyecto.getProyectoSeleccionado(), null, null, null, null));
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

    public void grabarConfiguracionesProyecto() {
        try {
            for (ConfiguracionProyecto conf : sessionProyecto.getConfiguracionProyectos()) {
                if (conf.getId() == null) {
                    configuracionProyectoDao.create(conf);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionProyecto", conf.getId() + "", "CREAR", "|Nombre= "
                            + conf.getNombre() + "|Codigo= " + conf.getCodigo() + "|Valor=" + conf.getValor() + "|Proyecto= "
                            + conf.getProyectoId().getId(), sessionUsuario.getUsuario()));
                } else {
                    configuracionProyectoDao.edit(conf);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionProyecto", conf.getId() + "", "EDITAR", "|Nombre= "
                            + conf.getNombre() + "|Codigo= " + conf.getCodigo() + "|Valor=" + conf.getValor() + "|Proyecto= "
                            + conf.getProyectoId().getId(), sessionUsuario.getUsuario()));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="AUTORES PROYECTO">
    /**
     * EDITAR AUTORES DE PROYECTO
     *
     * @param autorProyectoDTO
     */
    public void editar(AutorProyectoDTO autorProyectoDTO) {
        try {
            this.messageView = new MessageView();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_autor_proyecto");
            if (tienePermiso == 1) {
                sessionProyecto.setAutorProyectoDTOSeleccionado(autorProyectoDTO);
                sessionProyecto.setRenderedDialogoAP(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgEditarAutor').show()");
            } else {
                if (tienePermiso == 2) {
                    messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". "
                            + bundle.getString("lbl.msm_consulte"), "");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * BUSCAR AUTORES POR PROYECTO
     *
     * @param proyecto
     */
    public void buscarAutores(Proyecto proyecto) {
        sessionProyecto.getAutoresProyectoDTO().clear();
        sessionProyecto.getFilterAutoresProyectoDTO().clear();
        try {
            List<AutorProyecto> autorProyectos = autorProyectoService.buscar(new AutorProyecto(proyecto, null, null, null, null));
            if (autorProyectos == null) {
                return;
            }

            for (AutorProyecto autorProyecto : autorProyectos) {
                AutorProyectoDTO autorProyectoDTO = new AutorProyectoDTO(autorProyecto, autorProyecto.getAspiranteId(),
                        estudianteCarreraDao.find(autorProyecto.getAspiranteId().getId()), null);
                autorProyectoDTO.setPersona(personaDao.find(autorProyectoDTO.getEstudianteCarrera().getEstudianteId().getId()));
                if (!sessionProyecto.getAutoresProyectoDTO().contains(autorProyectoDTO)) {
                    sessionProyecto.getAutoresProyectoDTO().add(autorProyectoDTO);
                }
            }
            sessionProyecto.setFilterAutoresProyectoDTO(sessionProyecto.getAutoresProyectoDTO());
        } catch (Exception e) {
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public List<Proyecto> getProyectosEnSustentacionPublica() {
        return proyectosEnSustentacionPublica;
    }

    public void setProyectosEnSustentacionPublica(List<Proyecto> proyectosEnSustentacionPublica) {
        this.proyectosEnSustentacionPublica = proyectosEnSustentacionPublica;
    }

    public List<Proyecto> getProyectosEnSustentacionPrivada() {
        return proyectosEnSustentacionPrivada;
    }

    public void setProyectosEnSustentacionPrivada(List<Proyecto> proyectosEnSustentacionPrivada) {
        this.proyectosEnSustentacionPrivada = proyectosEnSustentacionPrivada;
    }

    public boolean isRenderedCaducado() {
        return renderedCaducado;
    }

    public void setRenderedCaducado(boolean renderedCaducado) {
        this.renderedCaducado = renderedCaducado;
    }

    public List<Proyecto> getProyectosPorCulminar() {
        return proyectosPorCulminar;
    }

    public void setProyectosPorCulminar(List<Proyecto> proyectosPorCulminar) {
        this.proyectosPorCulminar = proyectosPorCulminar;
    }

    public List<Proyecto> getProyectosCaducados() {
        return proyectosCaducados;
    }

    public void setProyectosCaducados(List<Proyecto> proyectosCaducados) {
        this.proyectosCaducados = proyectosCaducados;
    }

    public List<Proyecto> getProyectosInicio() {
        return proyectosInicio;
    }

    public void setProyectosInicio(List<Proyecto> proyectosInicio) {
        this.proyectosInicio = proyectosInicio;
    }

    public boolean isRenderedEditarDatosProyecto() {
        return renderedEditarDatosProyecto;
    }

    public void setRenderedEditarDatosProyecto(boolean renderedEditarDatosProyecto) {
        this.renderedEditarDatosProyecto = renderedEditarDatosProyecto;
    }

    public List<LineaInvestigacionCarrera> getLineaInvestigacionCarreras() {
        return lineaInvestigacionCarreras;
    }

    public void setLineaInvestigacionCarreras(List<LineaInvestigacionCarrera> lineaInvestigacionCarreras) {
        this.lineaInvestigacionCarreras = lineaInvestigacionCarreras;
    }

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public LineaInvestigacion getLi() {
        return li;
    }

    public void setLi(LineaInvestigacion li) {
        this.li = li;
    }

    public List<LineaInvestigacionProyecto> getLineaInvestigacionProyectosAgregados() {
        return lineaInvestigacionProyectosAgregados;
    }

    public void setLineaInvestigacionProyectosAgregados(List<LineaInvestigacionProyecto> lineaInvestigacionProyectosAgregados) {
        this.lineaInvestigacionProyectosAgregados = lineaInvestigacionProyectosAgregados;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    

    public String getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(String tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public String getCatalogoProyecto() {
        return catalogoProyecto;
    }

    public void setCatalogoProyecto(String catalogoProyecto) {
        this.catalogoProyecto = catalogoProyecto;
    }

    public DualListModel<LineaInvestigacion> getLineasInvestigacionDualList() {
        return lineasInvestigacionDualList;
    }

    public void setLineasInvestigacionDualList(DualListModel<LineaInvestigacion> lineasInvestigacionDualList) {
        this.lineasInvestigacionDualList = lineasInvestigacionDualList;
    }

    public List<Docente> getDocentesTarget() {
        return docentesTarget;
    }

    public void setDocentesTarget(List<Docente> docentesTarget) {
        this.docentesTarget = docentesTarget;
    }

    public List<Docente> getDocenteProyectosFuente() {
        return docenteProyectosFuente;
    }

    public void setDocenteProyectosFuente(List<Docente> docenteProyectosFuente) {
        this.docenteProyectosFuente = docenteProyectosFuente;
    }

    public String getEstadoProyecto() {
        return estadoProyecto;
    }

    public void setEstadoProyecto(String estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
    }

    public List<LineaInvestigacion> getLineaInvestigacions() {
        return lineaInvestigacions;
    }

    public void setLineaInvestigacions(List<LineaInvestigacion> lineaInvestigacions) {
        this.lineaInvestigacions = lineaInvestigacions;
    }

    public DualListModel<Carrera> getCarrerasDualList() {
        return carrerasDualList;
    }

    public void setCarrerasDualList(DualListModel<Carrera> carrerasDualList) {
        this.carrerasDualList = carrerasDualList;
    }

    public String getAutorRequisitos() {
        return autorRequisitos;
    }

    public void setAutorRequisitos(String autorRequisitos) {
        this.autorRequisitos = autorRequisitos;
    }

    public SessionPeriodoAcademico getSessionPeriodoAcademico() {
        return sessionPeriodoAcademico;
    }

    public void setSessionPeriodoAcademico(SessionPeriodoAcademico sessionPeriodoAcademico) {
        this.sessionPeriodoAcademico = sessionPeriodoAcademico;
    }

    public List<Proyecto> getProyectosPorWS() {
        return proyectosPorWS;
    }

    public void setProyectosPorWS(List<Proyecto> proyectosPorWS) {
        this.proyectosPorWS = proyectosPorWS;
    }


    public AdministrarConfiguracionesProyecto getAdministrarConfiguracionesProyecto() {
        return administrarConfiguracionesProyecto;
    }

    public void setAdministrarConfiguracionesProyecto(AdministrarConfiguracionesProyecto administrarConfiguracionesProyecto) {
        this.administrarConfiguracionesProyecto = administrarConfiguracionesProyecto;
    }

    public AdministrarCatalogoDuracion getAdministrarCatalogoDuracion() {
        return administrarCatalogoDuracion;
    }

  

    public AdministrarDocentesProyecto getAdministrarDocentesProyecto() {
        return administrarDocentesProyecto;
    }

    public void setAdministrarDocentesProyecto(AdministrarDocentesProyecto administrarDocentesProyecto) {
        this.administrarDocentesProyecto = administrarDocentesProyecto;
    }

    public AdministrarDocumentosProyecto getAdministrarDocumentosProyecto() {
        return administrarDocumentosProyecto;
    }

    public void setAdministrarDocumentosProyecto(AdministrarDocumentosProyecto administrarDocumentosProyecto) {
        this.administrarDocumentosProyecto = administrarDocumentosProyecto;
    }

    public AdministrarDirectoresProyecto getAdministrarDirectoresProyecto() {
        return administrarDirectoresProyecto;
    }

    public void setAdministrarDirectoresProyecto(AdministrarDirectoresProyecto administrarDirectoresProyecto) {
        this.administrarDirectoresProyecto = administrarDirectoresProyecto;
    }

    public AdministrarProrrogas getAdministrarProrrogas() {
        return administrarProrrogas;
    }

    public void setAdministrarProrrogas(AdministrarProrrogas administrarProrrogas) {
        this.administrarProrrogas = administrarProrrogas;
    }

    public AdministrarActividades getAdministrarActividades() {
        return administrarActividades;
    }

    public void setAdministrarActividades(AdministrarActividades administrarActividades) {
        this.administrarActividades = administrarActividades;
    }

    public AdministrarTribunales getAdministrarTribunales() {
        return administrarTribunales;
    }

    public void setAdministrarTribunales(AdministrarTribunales administrarTribunales) {
        this.administrarTribunales = administrarTribunales;
    }

    public AdministrarReportes getAdministrarReportes() {
        return administrarReportes;
    }

    public void setAdministrarReportes(AdministrarReportes administrarReportes) {
        this.administrarReportes = administrarReportes;
    }

    public List<ProyectoCarreraOferta> getProyectoCarreraOfertasAgregados() {
        return proyectoCarreraOfertasAgregados;
    }

    public void setProyectoCarreraOfertasAgregados(List<ProyectoCarreraOferta> proyectoCarreraOfertasAgregados) {
        this.proyectoCarreraOfertasAgregados = proyectoCarreraOfertasAgregados;
    }

    public AdministrarEstudiantesCarrera getAdministrarEstudiantesCarrera() {
        return administrarEstudiantesCarrera;
    }

    public void setAdministrarEstudiantesCarrera(AdministrarEstudiantesCarrera administrarEstudiantesCarrera) {
        this.administrarEstudiantesCarrera = administrarEstudiantesCarrera;
    }

    public AdministrarDocumentosExpediente getAdministrarDocumentosExpediente() {
        return administrarDocumentosExpediente;
    }

    public void setAdministrarDocumentosExpediente(AdministrarDocumentosExpediente administrarDocumentosExpediente) {
        this.administrarDocumentosExpediente = administrarDocumentosExpediente;
    }
    //</editor-fold>

}
