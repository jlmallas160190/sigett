/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.ItemDao;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.academico.controlador.AdministrarEstudiantesCarrera;
import edu.unl.sigett.academico.controlador.AdministrarEstudiantes;
import edu.unl.sigett.comun.controlador.AdministrarCatalogoProyectos;
import edu.unl.sigett.comun.controlador.AdministrarEstadoProyecto;
import edu.unl.sigett.comun.controlador.AdministrarTipoProyectos;
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
import edu.unl.sigett.postulacion.managed.session.SessionProyecto;
import edu.unl.sigett.reportes.AdministrarReportes;
import edu.unl.sigett.seguimiento.controlador.AdministrarActividades;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.CatalogoDocumentoExpediente;
import edu.unl.sigett.entity.CatalogoProyecto;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.DocumentoExpediente;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.EstadoAutor;
import edu.unl.sigett.entity.EstadoProyecto;
import edu.unl.sigett.entity.Expediente;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.unl.sigett.entity.Prorroga;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.entity.RenunciaAutor;
import edu.unl.sigett.entity.RenunciaDirector;
import edu.unl.sigett.entity.TemaProyecto;
import edu.unl.sigett.entity.TipoProyecto;
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
import edu.unl.sigett.dao.AutorProyectoFacadeLocal;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.CatalogoDocumentoExpedienteFacadeLocal;
import edu.unl.sigett.dao.CatalogoProyectoFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.ConfiguracionProyectoFacadeLocal;
import edu.unl.sigett.dao.CronogramaFacadeLocal;
import edu.unl.sigett.dao.DirectorProyectoFacadeLocal;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.unl.sigett.dao.DocenteProyectoFacadeLocal;
import edu.unl.sigett.dao.DocumentoProyectoFacadeLocal;
import edu.unl.sigett.dao.EstadoAutorFacadeLocal;
import edu.unl.sigett.dao.EstadoProyectoFacadeLocal;
import edu.unl.sigett.dao.ExpedienteFacadeLocal;
import edu.unl.sigett.dao.LineaInvestigacionCarreraFacadeLocal;
import edu.unl.sigett.dao.LineaInvestigacionFacadeLocal;
import edu.unl.sigett.dao.LineaInvestigacionProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.unl.sigett.dao.ProyectoCarreraOfertaFacadeLocal;
import edu.unl.sigett.dao.ProyectoFacadeLocal;
import edu.unl.sigett.dao.TemaProyectoFacadeLocal;
import edu.unl.sigett.dao.TipoProyectoFacadeLocal;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.service.EstudianteCarreraFacadeLocal;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoDirectorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarProyecto",
            pattern = "/editarProyecto/#{sessionProyecto.proyecto.id}",
            viewId = "/faces/pages/sigett/editarProyecto.xhtml"
    ),
    @URLMapping(
            id = "crearProyecto",
            pattern = "/crearProyecto/",
            viewId = "/faces/pages/sigett/editarProyecto.xhtml"
    ),
    @URLMapping(
            id = "proyectos",
            pattern = "/proyectos/",
            viewId = "/faces/pages/sigett/buscarProyectos.xhtml"
    )
})
public class AdministrarProyectos implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private AdministrarEstadoProyecto administrarEstadoProyecto;
    @Inject
    private SessionPeriodoAcademico sessionPeriodoAcademico;
    @Inject
    private AdministrarProrrogas administrarProrrogas;
    @Inject
    private AdministrarTemaProyectos administrarTemaProyectos;
    @Inject
    private AdministrarConfiguracionesProyecto administrarConfiguracionesProyecto;
    @Inject
    private AdministrarCatalogoDuracion administrarCatalogoDuracion;
    @Inject
    private AdministrarDocentesProyecto administrarDocentesProyecto;
    @Inject
    private AdministrarDirectoresProyecto administrarDirectoresProyecto;
    @Inject
    private AdministrarAutoresProyecto administrarAutoresProyecto;
    @Inject
    private AdministrarDocumentosProyecto administrarDocumentosProyecto;
    @Inject
    private AdministrarActividades administrarActividades;
    @Inject
    private AdministrarTipoProyectos administrarTipoProyectos;
    @Inject
    private AdministrarCatalogoProyectos administrarCatalogoProyectos;
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
    private AdministrarEstudiantes administrarEstudiantes;
    @Inject
    private AdministrarNotificaciones administrarNotificaciones;
    @Inject
    private AdministrarMiembrosTribunal administrarMiembrosTribunal;
    @Inject
    private AdministrarActas administrarActas;

    private AdministrarReportes administrarReportes;

    @EJB
    private TipoProyectoFacadeLocal tipoProyectoFacadeLocal;
    @EJB
    private ProyectoFacadeLocal proyectoFacadeLocal;
    @EJB
    private EstadoProyectoFacadeLocal estadoProyectoFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private LineaInvestigacionFacadeLocal lineaInvestigacionFacadeLocal;
    @EJB
    private LineaInvestigacionProyectoFacadeLocal lineaInvestigacionProyectoFacadeLocal;
    @EJB
    private DocumentoProyectoFacadeLocal documentoProyectoFacadeLocal;
    @EJB
    private LineaInvestigacionCarreraFacadeLocal lineaInvestigacionCarreraFacadeLocal;
    @EJB
    private DocenteProyectoFacadeLocal docenteProyectoFacadeLocal;
    @EJB
    private UsuarioCarreraDao usuarioCarreraFacadeLocal;
    @EJB
    private DocenteCarreraDao docenteCarreraFacadeLocal;
    @EJB
    private TemaProyectoFacadeLocal temaProyectoFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private ConfiguracionProyectoFacadeLocal configuracionProyectoFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private DirectorProyectoFacadeLocal directorProyectoFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private ProyectoCarreraOfertaFacadeLocal proyectoCarreraOfertaFacadeLocal;
    @EJB
    private OfertaAcademicaService ofertaAcademicaFacadeLocal;
    @EJB
    private CatalogoDocumentoExpedienteFacadeLocal catalogoDocumentoExpedienteFacadeLocal;
    @EJB
    private AutorProyectoFacadeLocal autorProyectoFacadeLocal;
    @EJB
    private EstadoAutorFacadeLocal estadoAutorFacadeLocal;
    @EJB
    private ExpedienteFacadeLocal expedienteFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private CatalogoProyectoFacadeLocal catalogoProyectoFacadeLocal;
    @EJB
    private CronogramaFacadeLocal cronogramaFacadeLocal;
    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteFacadeLocal;
    @EJB
    private EstudianteCarreraFacadeLocal estudianteCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private DocenteDao docenteFacadeLocal;
    @EJB
    private ItemDao itemFacadeLocal;
    

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
    private List<Proyecto> proyectos;
    private List<Proyecto> proyectosPorCulminar;
    private List<Proyecto> proyectosCaducados;
    private List<Proyecto> proyectosEnSustentacionPublica;
    private List<Proyecto> proyectosEnSustentacionPrivada;

    private boolean renderedNoEditar;
    private boolean renderedCaducado;
    private boolean renderedEditarDatosProyecto;

    private String area;
    private String criterio;
    private String tipoProyecto;
    private String catalogoProyecto;
    private String estadoProyecto;
    private String autorRequisitos;

    @PostConstruct
    public void AdministrarProyectos() {
        this.lineasInvestigacionDualList = new DualListModel<>();
        this.carrerasDualList = new DualListModel<>();
    }

    //<editor-fold defaultstate="collapsed" desc="METODOS DE PROYECTO">
    public String crear() {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_proyecto");
            if (tienePermiso == 1) {
                Calendar fechaActual = Calendar.getInstance();
                sessionProyecto.setProyecto(new Proyecto());
                sessionProyecto.getProyecto().setDescripcion("Ninguna");
                tipoProyecto = "";
                catalogoProyecto = "";
                estadoProyecto = "";
                sessionProyecto.getProyecto().getCronograma().setProyecto(sessionProyecto.getProyecto());
                seleccionarEstadoProyecto(sessionProyecto.getProyecto());
                seleccionarTipoProyecto(sessionProyecto.getProyecto());
                seleccionarCatalogoProyecto(sessionProyecto.getProyecto());
                listadoLineasInvestigacionProyecto(sessionProyecto.getProyecto());
                listadoDocentes(sessionProyecto.getProyecto());
                listadoCarreras(sessionProyecto.getProyecto());
                administrarConfiguracionesProyecto.agregarConfiguracionesProyecto(sessionProyecto.getProyecto());
                administrarTemaProyectos.crear(sessionUsuario.getUsuario());
                sessionProyecto.getProyecto().getCronograma().setFechaInicio(fechaActual.getTime());
                sessionProyecto.getProyecto().getCronograma().setFechaFin(fechaActual.getTime());
                sessionProyecto.getProyecto().getCronograma().setFechaProrroga(fechaActual.getTime());
                sessionProyecto.getProyecto().getCronograma().setDuracion(0.0);
                /*----------------------------------RENDERED-----------------------------------------------------*/
                administrarTipoProyectos.renderedCrear();
                administrarAutoresProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarCatalogoProyectos.renderedCrear();
                administrarEstadoProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarDocumentosProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarTemaProyectos.renderedBuscar(sessionUsuario.getUsuario());
                administrarTribunales.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarDocentesProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarConfiguracionesProyecto.renderedBuscar(sessionUsuario.getUsuario());
                this.renderedEditarDatosProyecto(sessionProyecto.getProyecto());
                administrarCronograma.renderedCronograma(sessionProyecto.getProyecto());
                administrarProrrogas.tieneProrroga(sessionProyecto.getProyecto());
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

    public void onRowSelectCarrera(SelectEvent event) {
        try {
            UsuarioCarrera usuarioCarrera = ((UsuarioCarrera) event.getObject());
            Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
            this.lineaInvestigacionCarreras = new ArrayList<>();
            li = new LineaInvestigacion();
            for (LineaInvestigacionCarrera li : lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(carrera.getId())) {
                lineaInvestigacionCarreras.add(li);
            }
//            buscar(carrera.getNombre(), sessionUsuario.getUsuario());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void editarLineaInvestigacion(SelectEvent event) {
        try {
            LineaInvestigacionCarrera lc = (LineaInvestigacionCarrera) event.getObject();
//            buscar(lc.getLineaInvestigacionId().getNombre(), sessionUsuario.getUsuario());
        } catch (Exception e) {
        }
    }

    public void abandonar(Proyecto proyecto, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_proyecto");
            Calendar fechaActual = Calendar.getInstance();
            if (fechaActual.after(proyecto.getCronograma().getFechaProrroga())) {
                if (tienePermiso == 1) {
                    if (proyecto.getId() != null) {
                        EstadoProyecto estadoProyecto = estadoProyectoFacadeLocal.find(10);
                        EstadoAutor estadoAutor = estadoAutorFacadeLocal.find(4);
                        proyecto.setEstadoProyectoId(estadoProyecto);
                        proyectoFacadeLocal.edit(proyecto);
                        for (AutorProyecto autorProyecto : autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                            autorProyecto.setEstadoAutorId(estadoAutor);
                            autorProyectoFacadeLocal.edit(autorProyecto);
                        }
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public String editar(Proyecto proyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_proyecto");
            if (tienePermiso == 1) {
                if (proyecto.getId() != null) {
                    proyecto = proyectoFacadeLocal.find(proyecto.getId());
                }
                sessionProyecto.setProyecto(proyecto);
                tipoProyecto = proyecto.getTipoProyectoId().toString();
                estadoProyecto = proyecto.getEstadoProyectoId().toString();
                catalogoProyecto = proyecto.getCatalogoProyectoId().toString();
                sessionProyecto.getProyecto().setCronograma(cronogramaFacadeLocal.find(sessionProyecto.getProyecto().getId()));
                listadoLineasInvestigacionProyecto(proyecto);
                obtenerLineasInvestigacionProyecto(proyecto);
                listadoDocentes(proyecto);
                listadoCarreras(proyecto);
                this.lineaInvestigacionProyectosRemovidos = new ArrayList<>();
                this.lineaInvestigacionProyectosAgregados = new ArrayList<>();
                this.proyectoCarreraOfertasRemovidos = new ArrayList<>();
                this.proyectoCarreraOfertasAgregados = new ArrayList<>();
                docentesTarget = new ArrayList<>();
                docenteProyectosFuente = new ArrayList<>();
                List<TemaProyecto> temas = new ArrayList<>();
                temas.addAll(temaProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                if (temas != null) {
                    if (!temas.isEmpty()) {
                        administrarTemaProyectos.editar(temas.get(0), sessionUsuario.getUsuario());
                    } else {
                        administrarTemaProyectos.crear(sessionUsuario.getUsuario());
                    }
                } else {
                    administrarTemaProyectos.crear(sessionUsuario.getUsuario());
                }
                /*Refrescar Documentos Proyecto*/
                sessionProyecto.getProyecto().setDocumentoProyectoList(new ArrayList<DocumentoProyecto>());
                sessionProyecto.getProyecto().getDocumentoProyectoList().addAll(documentoProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                /*Refrescar Docentes Proyecto*/
                sessionProyecto.getProyecto().setDocenteProyectoList(new ArrayList<DocenteProyecto>());
                sessionProyecto.getProyecto().getDocenteProyectoList().addAll(docenteProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                /*Refrescar Directores Proyecto*/
                sessionProyecto.getProyecto().setDirectorProyectoList(new ArrayList<DirectorProyecto>());
                sessionProyecto.getProyecto().getDirectorProyectoList().addAll(directorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                /*Refrescar Autores Proyecto*/
                sessionProyecto.getProyecto().setAutorProyectoList(new ArrayList<AutorProyecto>());
                sessionProyecto.getProyecto().getAutorProyectoList().addAll(autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()));
                /*New Prorrogas*/
                sessionProyecto.getProyecto().getCronograma().setProrrogaList(new ArrayList<Prorroga>());
                /*---------------------------------------------------RENDERED--------------------------------------------------------*/
                administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), proyecto);
                administrarAutoresProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarDocumentosProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), proyecto);
                administrarTribunales.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                administrarDocentesProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarCronograma.renderedCronograma(sessionProyecto.getProyecto());
                administrarTipoProyectos.renderedCrear();
                administrarCatalogoProyectos.renderedCrear();
                administrarEstadoProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarConfiguracionesProyecto.renderedBuscar(sessionUsuario.getUsuario());
                administrarProrrogas.tieneProrroga(proyecto);
                renderedEditarDatosProyecto(sessionProyecto.getProyecto());
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

    public void seleccionarEstadoProyecto(Proyecto proyecto) {
        try {
            int posEp = estadoProyecto.indexOf(":");
            EstadoProyecto esp = null;
            if (!estadoProyecto.equalsIgnoreCase("")) {
                esp = estadoProyectoFacadeLocal.find(Integer.parseInt(estadoProyecto.substring(0, posEp)));
            } else {
                esp = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.INICIO.getTipo());
            }

            if (esp != null) {
                proyecto.setEstadoProyectoId(esp);
            }
        } catch (Exception e) {
        }

    }

    public void seleccionarCatalogoProyecto(Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
                sessionProyecto.setProyecto(proyecto);
            }
            if (proyecto.getEstadoProyectoId().getId() == 1) {
                int posEp = catalogoProyecto.indexOf(":");
                CatalogoProyecto cat = null;
                if (!catalogoProyecto.equalsIgnoreCase("")) {
                    cat = catalogoProyectoFacadeLocal.find(Integer.parseInt(catalogoProyecto.substring(0, posEp)));
                } else {
                    cat = catalogoProyectoFacadeLocal.find((int) 1);
                }

                if (cat != null) {
                    proyecto.setCatalogoProyectoId(cat);
                }
            }
        } catch (Exception e) {
        }

    }

    public void seleccionarTipoProyecto(Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
                sessionProyecto.setProyecto(proyecto);
            }
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (proyecto.getEstadoProyectoId().getId() == 1) {
                int posTipo = tipoProyecto.indexOf(":");
                TipoProyecto tp = null;
                if (!tipoProyecto.equalsIgnoreCase("")) {
                    tp = tipoProyectoFacadeLocal.find(Integer.parseInt(tipoProyecto.substring(0, posTipo)));
                } else {
                    tp = tipoProyectoFacadeLocal.find((int) 1);
                }

                if (tp != null) {
                    if (verificarAutores(proyecto) || tp.getId() != 1) {
                        proyecto.setTipoProyectoId(tp);
                    } else {
                        tipoProyecto = proyecto.getTipoProyectoId().toString();
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.autor") + " " + bundle.getString("lbl.msm_no_es_apto_tt"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        } catch (Exception e) {
        }

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

    public String confirmarGuardar(Proyecto proyecto, TemaProyecto temaProyecto) {
        String navegacion = "";
        autorRequisitos = "";
        boolean var = false;
        try {
            if (comparaLineasInvestigacionProyectoYDocente(proyecto) == true) {
                var = true;
            } else {
                var = false;
                RequestContext.getCurrentInstance().execute("confirmLineasInvestigacion.show()");
            }
            if (var) {
                if (compruebaRequisitosAutor(proyecto) == true) {
                    var = true;
                } else {
                    var = false;
                    RequestContext.getCurrentInstance().execute("confirmRequisitos.show()");
                }
            }
            if (var) {
                navegacion = grabar(proyecto, temaProyecto);
            }
        } catch (Exception e) {
        }

        return navegacion;
    }

    public String confirmarGuardarEditar(Proyecto proyecto, TemaProyecto temaProyecto) {
        String navegacion = "";
        autorRequisitos = "";
        boolean var = false;
        try {
            if (comparaLineasInvestigacionProyectoYDocente(proyecto) == true) {
                var = true;
            } else {
                var = false;
                RequestContext.getCurrentInstance().execute("confirmLineasInvestigacion1.show()");
            }
            if (var) {
                if (compruebaRequisitosAutor(proyecto) == true) {
                    var = true;
                } else {
                    var = false;
                    RequestContext.getCurrentInstance().execute("confirmRequisitos1.show()");
                }
            }
            if (var) {
                navegacion = grabar(proyecto, temaProyecto);
            }
        } catch (Exception e) {
        }

        return navegacion;
    }

    public boolean comparaLineasInvestigacionProyectoYDocente(Proyecto proyecto) {
        boolean var = false;
        List<LineaInvestigacionProyecto> lips = new ArrayList<>();
        for (Object o : lineasInvestigacionDualList.getTarget()) {
            int v = o.toString().indexOf(":");
            Long id = Long.parseLong(o.toString().substring(0, v));
            LineaInvestigacion li = lineaInvestigacionFacadeLocal.find(id);
            LineaInvestigacionProyecto lp = new LineaInvestigacionProyecto();
            lp.setLineaInvestigacionId(li);
            lp.setProyectoId(sessionProyecto.getProyecto());
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

    public boolean compruebaRequisitosAutor(Proyecto proyecto) {
        boolean var = false;
        boolean aux = true;
        try {
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                List<CatalogoDocumentoExpediente> catalogoDocumentoExpedientesObligatorios = new ArrayList<>();
                catalogoDocumentoExpedientesObligatorios = catalogoDocumentoExpedienteFacadeLocal.buscarObligatorios();
                if (!catalogoDocumentoExpedientesObligatorios.isEmpty()) {
                    for (CatalogoDocumentoExpediente catalogoDocumentoExpediente : catalogoDocumentoExpedientesObligatorios) {
                        if (!proyecto.getAutorProyectoList().isEmpty()) {
                            for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
                                EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                                autorRequisitos = estudianteCarrera.getEstudianteId().toString();
                                if (autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.INICIO.getTipo())) {
                                    if (!autorProyecto.getExpedienteList().isEmpty()) {
                                        for (Expediente expediente : autorProyecto.getExpedienteList()) {
                                            if (aux = true) {
                                                for (DocumentoExpediente documentoExpediente : expediente.getDocumentoExpedienteList()) {
                                                    if (documentoExpediente.getCatalogoDocumentoExpedienteId().equals(catalogoDocumentoExpediente)) {
                                                        var = true;
                                                        break;
                                                    } else {
                                                        var = false;
                                                        aux = false;

                                                    }
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    } else {
                                        var = false;
                                    }
                                } else {
                                    var = true;
                                }
                            }
                        } else {
                            var = true;
                        }
                    }
                } else {
                    var = true;
                }
            } else {
                var = true;
            }

        } catch (Exception e) {
        }
        return var;
    }

    public void cambiarEstadoAutoresProyecto(Proyecto proyecto) {
        try {
            for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                    if (autorProyecto.getId() != null) {
                        EstadoAutor estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.SEGUIMIENTO.getTipo());
                        autorProyecto.setEstadoAutorId(estadoAutor);
                        autorProyectoFacadeLocal.edit(autorProyecto);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void transferProyectoCarrera(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Integer id = Integer.parseInt(item.toString().substring(0, v));
                Carrera c = carreraFacadeLocal.find(id);
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
                    for (LineaInvestigacionProyecto lip : sessionProyecto.getProyecto().getLineaInvestigacionProyectoList()) {
                        if (!lip.getLineaInvestigacionId().equals(lp.getLineaInvestigacionId())) {
                            pos++;
                        } else {
                            break;
                        }
                    }
                    sessionProyecto.getProyecto().getLineaInvestigacionProyectoList().remove(pos);
                    lineaInvestigacions.remove(pos);
                    listadoDocentes(sessionProyecto.getProyecto());

                } else {
                    if (event.isAdd()) {
                        if (contieneLineaInvestigacion(sessionProyecto.getProyecto().getLineaInvestigacionProyectoList(), lp)) {
                            lineaInvestigacionProyectosRemovidos.remove(lp);
                        }
                        sessionProyecto.getProyecto().getLineaInvestigacionProyectoList().add(lp);
                        lineaInvestigacions.add(lp.getLineaInvestigacionId());
                        lineaInvestigacionProyectosAgregados.add(lp);
                        listadoDocentes(sessionProyecto.getProyecto());
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }

    }

    public void onTabChange(TabChangeEvent event) {
        if (sessionProyecto.getProyecto() != null) {
            switch (event.getTab().getId()) {
                case "tabPropiedades":
                    /*-----------------------Configuraciones Proyecto----------------------------------------------------*/
                    administrarConfiguracionesProyecto.buscar(administrarConfiguracionesProyecto.getCriterio(), sessionProyecto.getProyecto());
                    administrarConfiguracionesProyecto.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Documentos Proyecto-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*------------------------------------------------------Docentes Proyecto----------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*---------------------------------------------Autores------------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
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
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*-----------------------------------Documentos Expediente-------------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    break;
                case "tabAutores":
                    /*------------------------------------------Autores------------------------------------------------------------------------------*/
                    administrarAutoresProyecto.buscar("", sessionProyecto.getProyecto(), sessionUsuario.getUsuario());
                    administrarAutoresProyecto.renderedBuscarAspirantes(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarAutoresProyecto.renderedCrear(sessionUsuario.getUsuario());
                    administrarAutoresProyecto.renderedEditar(sessionUsuario.getUsuario());
                    administrarAutoresProyecto.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarAutoresProyecto.renderedSeleccionar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
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
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*------------------------------------------------------Documentos Expediente----------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    break;
                case "tabDocentesProyecto":
                    /*------------------------------DOCENTES---------------------------------------------------*/
                    administrarDocentesProyecto.buscar("", sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarDocentesProyecto.renderedBuscar(sessionUsuario.getUsuario());
                    administrarDocentesProyecto.renderedEditar(sessionUsuario.getUsuario());
                    administrarDocentesProyecto.renderedBuscarEspecialista(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarDocentesProyecto.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarDocentesProyecto.renderedSeleccionarDocenteEspecialista(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarDocentesProyecto.renderedSortearDocente(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
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
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
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
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*------------------------------------------------------Documentos Expediente----------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    break;
                case "tabDocumentosProyecto":
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.buscar(sessionProyecto.getProyecto());
                    administrarDocumentosProyecto.renderedCrear(sessionUsuario.getUsuario());
                    administrarDocumentosProyecto.renderedEditar(sessionUsuario.getUsuario());
                    administrarDocumentosProyecto.renderedEliminar(sessionUsuario.getUsuario());
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
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
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*-----------------------------------------METODOS PRORROGAS-------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    break;
                case "tabDirectoresProyecto":
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.buscar("", sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarDirectoresProyecto.renderedBuscarDirectorDisponible(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarDirectoresProyecto.renderedEditar(sessionUsuario.getUsuario());
                    administrarDirectoresProyecto.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarDirectoresProyecto.renderedSeleccionar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarDirectoresProyecto.renderedSortearDirectorProyecto(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
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
                    administrarEstudiantes.setRenderedDlgEditar(false);
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
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    break;
                case "tabProrrogas":
                    /*-------------------------------------------------MÉTODOS PRÓRROGAS---------------------------------------------------------*/
                    administrarProrrogas.buscar(sessionProyecto.getProyecto().getCronograma(), sessionUsuario.getUsuario(), "");
                    administrarProrrogas.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarProrrogas.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarProrrogas.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarProrrogas.renderedImprimirOficio(sessionUsuario.getUsuario());
                    administrarProrrogas.renderedAceptar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Documentos Expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*----------------------------------------------Docentes Proyecto-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
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
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    break;
                case "tabActividades":
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.buscarPorCronograma(administrarActividades.getCriterio(), sessionProyecto.getProyecto().getCronograma(), sessionUsuario.getUsuario());
                    administrarActividades.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarActividades.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarActividades.renderedCrearSubActividad(null, null);
                    administrarActividades.setRenderedDlgEditar(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Documentos expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*----------------------------------------------Prorrogas-------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    break;

                case "tabHistorialDirectores":
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.historialDirectoresProyecto(administrarDirectoresProyecto.getCriterioHistorialDirectorProyecto(), sessionProyecto.getProyecto());
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedImprimirOficio(sessionUsuario.getUsuario());
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Prorrogas-------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Documentos expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                case "tabDatosInicio":
                    /*----------------------------------------------Datos Proyectos-------------------------------------------------------*/
                    renderedEditarDatosProyecto(sessionProyecto.getProyecto());
                    /*----------------------------------------------Cronograma-------------------------------------------------------*/
                    administrarCronograma.renderedCronograma(sessionProyecto.getProyecto());
                    administrarCronograma.editarCronograma(sessionProyecto.getProyecto().getCronograma());
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
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
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.tieneProrroga(sessionProyecto.getProyecto());
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    break;
                case "tabTribunales":
                    /*----------------------------------------------Tribunales-------------------------------------------------------*/
                    administrarTribunales.buscar(sessionProyecto.getProyecto(), sessionUsuario.getUsuario());
                    administrarTribunales.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarTribunales.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarTribunales.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarTribunales.setRenderedDlgEditar(false);
                    /*----------------------------------------------Evaluaciones Tribunales-------------------------------------------------------*/
                    administrarEvaluacionesTribunal.setRenderedDlgEditar(false);
                    administrarEvaluacionesTribunal.renderedEditar(sessionUsuario.getUsuario());
                    administrarEvaluacionesTribunal.renderedEliminar(sessionUsuario.getUsuario());
                    administrarEvaluacionesTribunal.renderedCalificar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarEvaluacionesTribunal.listadoSustentacionesPorUsuarioCarrera(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarActas.setRenderedDlgActaGrado(false);
                    /*----------------------------------------------Miembros Tribunales-------------------------------------------------------*/
                    administrarMiembrosTribunal.renderedCrear(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarMiembrosTribunal.renderedEditar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarMiembrosTribunal.renderedImprimirOficio(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarMiembrosTribunal.renderedEliminar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarMiembrosTribunal.setRenderedDlgEditar(false);
                    administrarMiembrosTribunal.setRenderedDlgDocentesDisponibles(false);
                    administrarMiembrosTribunal.setRenderedDlgOficio(false);
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
                    /*----------------------------------------------Prorrogas-------------------------------------------------------*/
                    administrarProrrogas.setRenderedDlgOficio(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Docuementos Expediente-------------------------------------------------------*/
                    administrarDocumentosExpediente.setRenderedDlgEditar(false);
                    /*----------------------------------------------Actividades-------------------------------------------------------*/
                    administrarActividades.setRenderedDlgEditar(false);
                    administrarActividades.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    break;
                case "tabLineasInvestigacion":
                    listadoLineasInvestigacionProyecto(sessionProyecto.getProyecto());
                    /*----------------------------------------------Documentos-------------------------------------------------------*/
                    administrarDocumentosProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Directores-------------------------------------------------------*/
                    administrarDirectoresProyecto.setRenderedDlgBuscarDirectoresDisponibles(false);
                    administrarDirectoresProyecto.setRenderedDlgOficio(false);
                    administrarDirectoresProyecto.setRenderedDlgEditar(false);
                    administrarDirectoresProyecto.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    /*----------------------------------------------Docentes-------------------------------------------------------*/
                    administrarDocentesProyecto.setRenderedDlgBuscarDocentesDisponibles(false);
                    administrarDocentesProyecto.setRenderedDlgOficioDocenteProyecto(false);
                    administrarDocentesProyecto.setRenderedDlgEditar(false);
                    /*----------------------------------------------Autores-------------------------------------------------------*/
                    administrarAutoresProyecto.setRenderedDlgEditarAutorProyecto(false);
                    administrarEstudiantes.setRenderedDlgEditar(false);
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
                    administrarProrrogas.renderedBuscar(sessionUsuario.getUsuario(), sessionProyecto.getProyecto());
                    administrarProrrogas.setRenderedDlgEditar(false);
                    administrarProrrogas.setRenderedDlgRespuestaAutorProyecto(false);

                    break;
            }
        }
    }

    public String estadoProyecto(Proyecto proyecto) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String var = "";
        switch (proyecto.getEstadoProyectoId().getId()) {
            case 1:
                if (docenteProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()).isEmpty()) {
                    var = bundle.getString("lbl.dar_pertinencia");
                } else {
                    var = bundle.getString("lbl.en_pertinencia");;
                }
                break;
            case 2:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
            case 3:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
            case 4:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
            case 5:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
            case 6:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
            case 7:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
            case 8:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
            case 9:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
            case 10:
                var = proyecto.getEstadoProyectoId().getNombre();
                break;
        }
        return var;
    }

    public void crearPDF() throws IOException, DocumentException {
        Document pdf = new Document(PageSize.LEGAL.rotate());
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources/img" + File.separator + "selloInstitucion.png";
        Image image = Image.getInstance(logo);
        image.scaleToFit(50, 50);
        image.setAbsolutePosition(50f, 550f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(pdf, baos);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment; filename=Proyectos.pdf");
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        pdf.setMargins(20f, 20f, 20f, 20f);
        Font fontHeader = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
        Font fontTitle = FontFactory.getFont("Times-Roman", 18, Font.BOLD);
        Paragraph title = new Paragraph(bundle.getString("lbl.listado") + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.proyectos"), fontTitle);
        title.setSpacingAfter(20);
        title.setAlignment(1);
        pdf.open();
        pdf.add(image);
        pdf.add(title);
        PdfPTable pdfTable = new PdfPTable(7);
        pdfTable.setWidthPercentage(100f);
        pdfTable.setHorizontalAlignment(0);
        PdfPCell cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.tema"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.fecha") + " " + bundle.getString("lbl.inicio"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.fechaCulminación"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.categoria"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.estado"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.tipo"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.autores"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        pdfTable.setHeaderRows(1);
        for (Proyecto proyecto : proyectosPorWS) {
            pdfTable.addCell(proyecto.getTemaActual());
            pdfTable.addCell(configuracionGeneralFacadeLocal.dateFormat(proyecto.getCronograma().getFechaInicio()) + "");
            pdfTable.addCell(configuracionGeneralFacadeLocal.dateFormat(proyecto.getCronograma().getFechaProrroga()) + "");
            pdfTable.addCell(proyecto.getCatalogoProyectoId().getNombre());
            pdfTable.addCell(estadoProyecto(proyecto));
            pdfTable.addCell(proyecto.getTipoProyectoId().getNombre());
            String autores = "";
            int contador = 0;
            for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                    Persona persona = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
                    if (contador == 0) {
                        autores += "" + estudianteCarrera.getEstadoId().getNombre() + " " + persona.getNombres() + " " + persona.getApellidos() + " ";
                        contador++;
                    } else {
                        autores += ", " + estudianteCarrera.getEstadoId().getNombre() + " " + persona.getNombres() + " " + persona.getApellidos();
                        contador++;
                    }
                }
            }
            pdfTable.addCell(autores);
        }
        pdf.add(pdfTable);
        pdf.close();
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
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
                        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_proyecto");
                        if (tienePermiso == 1) {
                            EstadoProyecto ep = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.INICIO.getTipo());
                            if (ep != null) {
                                proyecto.setEstadoProyectoId(ep);
                            }
                            proyecto.setFechaCreated(fechaActual.getTime());
                            if (proyecto.getCronograma() != null) {
                                cronograma = proyecto.getCronograma();
                            }
                            proyecto.setCronograma(null);
                            proyecto.setTemaActual(temaProyecto.getTemaId().getNombre());
                            /*Grabar Proyecto*/
                            proyectoFacadeLocal.create(proyecto);
                            cronograma.setId(proyecto.getId());
                            /*Grabar Cronograma*/
                            cronogramaFacadeLocal.create(cronograma);
                            proyecto.setCronograma(cronograma);
                            /*Grabar Tema Proyecto*/
                            administrarTemaProyectos.grabar(temaProyecto);
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
                            grabarConfiguracionesProyecto(configuracionProyectos);
                            proyecto.setConfiguracionProyectoList(configuracionProyectos);
                            logFacadeLocal.create(logFacadeLocal.crearLog("Proyecto", proyecto.getId() + "", "CREAR", "|Tema= " + proyecto.getTemaActual() + "|Descripción= " + proyecto.getDescripcion() + "|Tipo de Proyecto= " + proyecto.getTipoProyectoId() + ""
                                    + "CatalogoProyecto= " + proyecto.getCatalogoProyectoId() + "|Estado= " + proyecto.getEstadoProyectoId(), sessionUsuario.getUsuario()));

                            if (param.equalsIgnoreCase("guardar")) {
                                sessionProyecto.setProyecto(new Proyecto());
                                navegacion = "pretty:proyectos";
                            } else {
                                if (param.equalsIgnoreCase("guardar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.proyecto") + " " + bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    sessionProyecto.setProyecto(new Proyecto());
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
                        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_proyecto");
                        if (tienePermiso == 1) {
                            Cronograma cr = proyecto.getCronograma();
                            proyecto.setCronograma((Cronograma) null);
                            /*Editar Proyecto*/
                            proyectoFacadeLocal.edit(proyecto);
                            /*Editar Cronograma*/
                            if (proyecto.getEstadoProyectoId().getId() == 2) {
                                cronogramaFacadeLocal.edit(cr);
                            }
                            proyecto.setCronograma(cr);
                            /*Editar Tema Proyecto*/
                            administrarTemaProyectos.grabar(temaProyecto);
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
                            grabarConfiguracionesProyecto(configuracionProyectos);
                            proyecto.setConfiguracionProyectoList(configuracionProyectos);
                            logFacadeLocal.create(logFacadeLocal.crearLog("Proyecto", proyecto.getId() + "", "EDITAR", "|Tema= " + proyecto.getTemaActual() + "|Descripción= " + proyecto.getDescripcion() + "|Tipo de Proyecto= " + proyecto.getTipoProyectoId() + ""
                                    + "CatalogoProyecto= " + proyecto.getCatalogoProyectoId() + "|Estado= " + proyecto.getEstadoProyectoId(), sessionUsuario.getUsuario()));

                            if (param.equalsIgnoreCase("guardar")) {
                                sessionProyecto.setProyecto(new Proyecto());
                                navegacion = "pretty:proyectos";
                            } else {
                                if (param.equalsIgnoreCase("guardar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.proyecto") + " " + bundle.getString("lbl.msm_editar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                    sessionProyecto.getProyecto().setDocumentoProyectoList(documentoProyectos);

                                } else {
                                    sessionProyecto.setProyecto(new Proyecto());
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
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
                sessionProyecto.setProyecto(proyecto);
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                for (LineaInvestigacionProyecto lineaInvestigacionProyecto : lineaInvestigacionProyectosAgregados) {
                    if (lineaInvestigacionProyecto.getId() == null) {
                        lineaInvestigacionProyecto.setProyectoId(proyecto);
                        lineaInvestigacionProyectoFacadeLocal.create(lineaInvestigacionProyecto);
//                        grabarIndividuoLineaInvestigacionProyecto(proyecto, lineaInvestigacionProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionProyecto", lineaInvestigacionProyecto.getId() + "", "CREAR", "Proyecto=" + proyecto.getId() + "|LineaInvestigacion=" + lineaInvestigacionProyecto.getLineaInvestigacionId().getId(), sessionUsuario.getUsuario()));
                    } else {
                        lineaInvestigacionProyectoFacadeLocal.edit(lineaInvestigacionProyecto);
//                        grabarIndividuoLineaInvestigacionProyecto(proyecto, lineaInvestigacionProyecto);
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    public void grabarConfiguracionesProyecto(List<ConfiguracionProyecto> configuraciones) {
        try {
            if (configuraciones != null) {
                for (ConfiguracionProyecto conf : configuraciones) {
                    if (conf.getId() == null) {
                        configuracionProyectoFacadeLocal.create(conf);
                        logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionProyecto", conf.getId() + "", "CREAR", "|Nombre= " + conf.getNombre() + "|Codigo= " + conf.getCodigo() + "|Valor=" + conf.getValor() + "|Proyecto= " + conf.getProyectoId().getId(), sessionUsuario.getUsuario()));
                        conf.setEsEditado(false);
                    } else {
                        if (conf.isEsEditado()) {
                            configuracionProyectoFacadeLocal.edit(conf);
                            logFacadeLocal.create(logFacadeLocal.crearLog("ConfiguracionProyecto", conf.getId() + "", "EDITAR", "|Nombre= " + conf.getNombre() + "|Codigo= " + conf.getCodigo() + "|Valor=" + conf.getValor() + "|Proyecto= " + conf.getProyectoId().getId(), sessionUsuario.getUsuario()));
                            conf.setEsEditado(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void grabarProyectoCarrerasOferta(Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
                sessionProyecto.setProyecto(proyecto);
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                for (ProyectoCarreraOferta proyectoCarreraOferta : proyectoCarreraOfertasAgregados) {
                    Carrera c = carreraFacadeLocal.find(proyectoCarreraOferta.getCarreraId());
                    Long pcoId = devuelveProyectoCarrera(proyectoCarreraOfertaFacadeLocal.buscarPorProyecto(proyecto.getId()), proyectoCarreraOferta);
                    proyectoCarreraOferta = proyectoCarreraOfertaFacadeLocal.find(pcoId);
                    if (proyectoCarreraOferta == null) {
                        proyectoCarreraOferta = new ProyectoCarreraOferta();
                        proyectoCarreraOferta.setEsActivo(true);
                        String valor = configuracionCarreraFacadeLocal.buscarPorCarreraId(c.getId(), "OA").getValor();
                        String idOferta = null;
                        if (valor != null) {
                            idOferta = (valor);
                        }
                        OfertaAcademica of = null;
                        if (idOferta != null) {
                            of = ofertaAcademicaFacadeLocal.buscarPorIdSga(idOferta);
                        }
                        if (of != null) {
                            proyectoCarreraOferta.setOfertaAcademicaId(of.getId());
                        }
                        proyectoCarreraOferta.setProyectoId(proyecto);
                        proyectoCarreraOferta.setCarreraId(c.getId());
                        if (contieneCarrera(proyectoCarreraOfertaFacadeLocal.buscarPorProyecto(proyecto.getId()), proyectoCarreraOferta) == false) {
                            proyectoCarreraOfertaFacadeLocal.create(proyectoCarreraOferta);
//                            grabarIndividuoProyectoOfertaCarrera(proyecto, proyectoCarreraOferta);
                            logFacadeLocal.create(logFacadeLocal.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "CREAR", "Carrera="
                                    + proyectoCarreraOferta.getCarreraId() + "|Oferta=" + proyectoCarreraOferta.getOfertaAcademicaId() + "|Proyecto= "
                                    + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
                        }
                    } else {
                        proyectoCarreraOferta.setEsActivo(true);
                        proyectoCarreraOfertaFacadeLocal.edit(proyectoCarreraOferta);
                        logFacadeLocal.create(logFacadeLocal.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "EDITAR", "Carrera=" + proyectoCarreraOferta.getCarreraId() + "|Oferta=" + proyectoCarreraOferta.getOfertaAcademicaId()
                                + "|Proyecto= " + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));
//                        grabarIndividuoProyectoOfertaCarrera(proyecto, proyectoCarreraOferta);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void grabarDocenteProyecto(Proyecto proyecto, List<DocenteProyecto> docenteProyectos, List<AutorProyecto> autorProyectos) {
        if (proyecto.getId() != null) {
            proyecto = proyectoFacadeLocal.find(proyecto.getId());
            sessionProyecto.setProyecto(proyecto);
        }
        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
            String tiempoMaxPertinencia = configuracionGeneralFacadeLocal.find((int) 3).getValor();
            boolean enviaNotificacioAutores = false;
            Map<String, String> map = new HashMap<String, String>();
            for (DocenteProyecto docenteProyecto : docenteProyectos) {
                Persona persona = personaFacadeLocal.find(docenteProyecto.getDocenteId());
                if (docenteProyecto.getId() == null) {
                    docenteProyectoFacadeLocal.create(docenteProyecto);
                    logFacadeLocal.create(logFacadeLocal.crearLog("DocenteProyecto", docenteProyecto.getId() + "", "CREAR", "|Docente= " + docenteProyecto.getDocenteId() + "|Proyecto= " + docenteProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
                    docenteProyecto.setEsEditado(false);
                    administrarNotificaciones.notificarAsignacionDocenteProyecto(docenteProyecto, tiempoMaxPertinencia);
                    enviaNotificacioAutores = true;
                    map.put("Docente", persona.getNombres() + " " + persona.getApellidos());
                } else {
                    if (docenteProyecto.isEsEditado()) {
                        docenteProyectoFacadeLocal.edit(docenteProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("DocenteProyecto", docenteProyecto.getId() + "", "EDITAR", "NUEVO: |Docente= " + docenteProyecto.getDocenteId() + "|Proyecto= " + docenteProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
                        docenteProyecto.setEsEditado(false);
                        administrarNotificaciones.notificarAsignacionDocenteProyecto(docenteProyecto, tiempoMaxPertinencia);
                        enviaNotificacioAutores = true;
                        map.put("Docente", persona.getNombres() + " " + persona.getApellidos());
                    }
                }
            }

            if (enviaNotificacioAutores) {
                administrarNotificaciones.notificarAsignacionDocenteProyectoAutores(autorProyectos, map);
            }
        }
    }

    public void grabarDirectoresProyecto(List<DirectorProyecto> directorProyectos, List<AutorProyecto> autorProyectos, Proyecto proyecto) {
        boolean enviaNotificacioAutores = false;
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
                sessionProyecto.setProyecto(proyecto);
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                Map<String, String> map = new HashMap<String, String>();

                for (DirectorProyecto directorProyecto : directorProyectos) {
                    DocenteCarrera docenteCarrera = docenteCarreraFacadeLocal.find(directorProyecto.getDirectorId().getId());
                    Persona persona = personaFacadeLocal.find(docenteCarrera.getDocenteId().getId());
                    if (directorProyecto.getId() == null) {
                        cambiarEstadoAutoresProyecto(sessionProyecto.getProyecto());
                        directorProyectoFacadeLocal.create(directorProyecto);
//                        grabarIndividuoInvestigadorDirector(proyecto, directorProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("DirectorProyecto", directorProyecto.getId() + "", "CREAR", "|Docente= " + docenteCarrera.getDocenteId() + "|Proyecto= " + directorProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
                        directorProyecto.setEsEditado(false);
                        enviaNotificacioAutores = true;
                        map.put("Docente", persona.getNombres() + " " + persona.getApellidos());
                        administrarNotificaciones.notificarAsignacionDirectorProyecto(persona, directorProyecto);
                    } else {
                        if (directorProyecto.isEsEditado()) {
                            directorProyecto.setRenunciaDirectorList(new ArrayList<RenunciaDirector>());
                            cambiarEstadoAutoresProyecto(sessionProyecto.getProyecto());
                            directorProyectoFacadeLocal.edit(directorProyecto);
//                            grabarIndividuoInvestigadorDirector(proyecto, directorProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("DirectorProyecto", directorProyecto.getId() + "", "EDITAR", "|Docente= " + docenteCarrera.getDocenteId() + "|Proyecto= " + directorProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
                            directorProyecto.setEsEditado(false);
                            enviaNotificacioAutores = true;
                            map.put("Docente", persona.getNombres() + " " + persona.getApellidos());
                            administrarNotificaciones.notificarAsignacionDirectorProyecto(persona, directorProyecto);
                        }
                    }
                }
                if (enviaNotificacioAutores) {
                    EstadoProyecto estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.SEGUIMIENTO.getTipo());
                    if (estadoProyecto != null) {
                        proyecto.setEstadoProyectoId(estadoProyecto);
                        proyectoFacadeLocal.edit(proyecto);
                    }
                    administrarNotificaciones.notificarAsignacionDirectorProyectoAutores(autorProyectos, map);
                }
            }
        } catch (Exception e) {
        }

    }

    public void grabarAutoresProyecto(List<AutorProyecto> autorProyectos, Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
                sessionProyecto.setProyecto(proyecto);
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                for (AutorProyecto autorProyecto : autorProyectos) {
                    List<Expediente> expedientes = new ArrayList<>();
                    expedientes.addAll(autorProyecto.getExpedienteList());
                    autorProyecto.setExpedienteList(new ArrayList<Expediente>());
                    autorProyecto.setRenunciaAutorList(new ArrayList<RenunciaAutor>());
                    if (autorProyecto.getId() == null) {
                        autorProyectoFacadeLocal.create(autorProyecto);
//                        grabarIndividuoInvestigadorAutor(proyecto, autorProyecto);
                        logFacadeLocal.create(logFacadeLocal.crearLog("AutorProyecto", autorProyecto.getId() + "", "CREAR", "|Aspirante= " + autorProyecto.getAspiranteId().getId() + "|Proyecto= " + autorProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
                        autorProyecto.setEsEditado(false);
                        grabarExpedientes(expedientes);
//                        grabarIndividuoInvestigadorAutor(proyecto, autorProyecto);
                    } else {
                        if (autorProyecto.isEsEditado()) {
                            autorProyectoFacadeLocal.edit(autorProyecto);
//                            grabarIndividuoInvestigadorAutor(proyecto, autorProyecto);
                            logFacadeLocal.create(logFacadeLocal.crearLog("AutorProyecto", autorProyecto.getId() + "", "EDITAR", "|Aspirante= " + autorProyecto.getAspiranteId().getId() + "|Proyecto= " + autorProyecto.getProyectoId().getId(), sessionUsuario.getUsuario()));
                            autorProyecto.setEsEditado(false);
                            grabarExpedientes(expedientes);
                        }
                        autorProyecto.getExpedienteList().addAll(expedientes);
                    }
                }
            }
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
    public void renderedEditarDatosProyecto(Proyecto proyecto) {
        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
            renderedEditarDatosProyecto = true;
        } else {
            renderedEditarDatosProyecto = false;
        }
    }

    public boolean renderedGeneraIndividuosProyectos(Usuario usuario) {
        boolean var = false;
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_rdf_proyecto");
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
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_linea_investigacion_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedBuscarCarreraProyecto() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_carrera_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_proyecto");
        if (tienePermiso == 1) {
            var = true;
        }
        return var;
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_proyecto");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
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
//            datos.put("areaId", carreraFacadeLocal.find(proyectoCarreraOferta.getCarreraId()).getAreaId().getId());
//            datos.put("areaNombre", carreraFacadeLocal.find(proyectoCarreraOferta.getCarreraId()).getAreaId().getNombre());
//            datos.put("areaSigla", carreraFacadeLocal.find(proyectoCarreraOferta.getCarreraId()).getAreaId().getSigla());
//            datos.put("nivelId", carreraFacadeLocal.find(proyectoCarreraOferta.getCarreraId()).getNivelId().getId());
//            datos.put("nivelNombre", carreraFacadeLocal.find(proyectoCarreraOferta.getCarreraId()).getNivelId().getNombre());
//            datos.put("carreraNombre", carreraFacadeLocal.find(proyectoCarreraOferta.getCarreraId()).getNombre());
//            datos.put("carreraSigla", carreraFacadeLocal.find(proyectoCarreraOferta.getCarreraId()).getSigla());
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
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_proyecto");
//            if (tienePermiso == 1) {
//                this.proyectosPorWS = new ArrayList<>();
//                ProyectoResource proyectoResource = new ProyectoResource(configuracionGeneralFacadeLocal.find(17).getValor());
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
//                    Map parametros = new HashMap();
//                    parametros.put("carreraId", usuarioCarrera.getCarreraId());
//                    parametros.put("filtro", filtro);
//                    List<String> valores = proyectoResource.buscarPorCarrera(parametros);
//                    for (String valor : valores) {
//                        Proyecto proyecto = proyectoFacadeLocal.find(Long.parseLong(valor));
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
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_rdf_proyecto");
//            if (tienePermiso == 1) {
//
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
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
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                    for (ProyectoCarreraOferta pco : proyectoCarreraOfertasRemovidos) {
                        Long id = devuelveProyectoCarrera(proyectoCarreraOfertaFacadeLocal.buscarPorProyecto(proyecto.getId()), pco);
                        ProyectoCarreraOferta proyectoCarreraOferta = null;
                        proyectoCarreraOferta = proyectoCarreraOfertaFacadeLocal.find(id);
                        if (proyectoCarreraOferta != null) {
                            proyectoCarreraOferta.setEsActivo(false);
                            proyectoCarreraOfertaFacadeLocal.edit(proyectoCarreraOferta);
                            logFacadeLocal.create(logFacadeLocal.crearLog("ProyectoCarreraOferta", proyectoCarreraOferta.getId() + "", "DESACTIVAR",
                                    "NUEVO: |Carrera=" + proyectoCarreraOferta.getCarreraId() + "|Oferta="
                                    + proyectoCarreraOferta.getOfertaAcademicaId() + "|Proyecto= " + proyectoCarreraOferta.getProyectoId().getId(), sessionUsuario.getUsuario()));

                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void removerLineasInvestigacionProyecto(Proyecto proyecto) {
        if (proyecto.getId() != null) {
            proyecto = proyectoFacadeLocal.find(proyecto.getId());
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                for (LineaInvestigacionProyecto lp : lineaInvestigacionProyectosRemovidos) {
                    Long id = devuelveLineaInvestigacion(lineaInvestigacionProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()), lp);
                    LineaInvestigacionProyecto lineaInvestigacionProyecto = lineaInvestigacionProyectoFacadeLocal.find(id);
                    if (lineaInvestigacionProyecto != null) {
                        logFacadeLocal.create(logFacadeLocal.crearLog("LineaInvestigacionProyecto", lineaInvestigacionProyecto.getId() + "", "CREAR", "NUEVO: |Proyecto=" + proyecto.getId() + "|LineaInvestigacion=" + lineaInvestigacionProyecto.getLineaInvestigacionId().getId(), sessionUsuario.getUsuario()));
                        lineaInvestigacionProyectoFacadeLocal.remove(lineaInvestigacionProyecto);
                    }
                }
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS LISTAR">
    public void buscar(List<Proyecto> proyectos) {
        try {
            this.proyectos = new ArrayList<>();
            if (proyectos != null) {
                this.proyectos = proyectos;
            }
        } catch (Exception e) {
        }
    }

    public List<OfertaAcademica> listadoPeriodosAcademicosCarrera(Usuario usuario) {
        List<OfertaAcademica> ofertaAcademicas = new ArrayList<>();
        try {
            for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
                for (ProyectoCarreraOferta pco : proyectoCarreraOfertaFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
                    OfertaAcademica ofertaAcademica = ofertaAcademicaFacadeLocal.find(pco.getOfertaAcademicaId());
                    if (!ofertaAcademicas.contains(ofertaAcademica)) {
                        ofertaAcademicas.add(ofertaAcademica);
                    }
                }
            }
        } catch (Exception e) {
        }
        return ofertaAcademicas;
    }

    public int countProyectosPeriodoCarrera(OfertaAcademica of, Usuario usuario) {
        int count = 0;
        try {
            for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
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
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
            for (Proyecto proyecto : proyectoFacadeLocal.buscarPorCarreraEstado(usuarioCarrera.getCarreraId(), EstadoProyectoEnum.INICIO.getTipo())) {
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
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
            for (Proyecto proyecto : proyectoFacadeLocal.buscarPorCarreraEstado(usuarioCarrera.getCarreraId(),EstadoProyectoEnum.PERTINENTE.getTipo())) {
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
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
            for (Proyecto proyecto : proyectoFacadeLocal.buscarPorCarreraEstado(usuarioCarrera.getCarreraId(), EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())) {
                proyectosEnSustentacionPublica.add(proyecto);
            }
        }

    }

    public void buscarProyectosEnSustentacionPrivada(Usuario usuario) {
        proyectosEnSustentacionPrivada = new ArrayList<>();
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
            for (Proyecto proyecto : proyectoFacadeLocal.buscarPorCarreraEstado(usuarioCarrera.getCarreraId(), EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())) {
                proyectosEnSustentacionPrivada.add(proyecto);
            }
        }
    }

    public void listadoDocentes(Proyecto proyecto) {
        List<Docente> docentesProyectos = new ArrayList<>();
        List<Docente> docentes = new ArrayList<>();
        try {
            if (proyecto.getId() != null) {
                for (DocenteProyecto docenteProyecto : proyecto.getDocenteProyectoList()) {
                    if (docenteProyecto.getEsActivo()) {
                        Docente docente = docenteFacadeLocal.find(docenteProyecto.getDocenteId());
                        docentesProyectos.add(docente);
                    }
                }
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                    for (DocenteCarrera docenteCarrera : docenteCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
//                        Docente docente = docenteFacadeLocal.find(docenteCarrera.getDocenteId());
//                        for (LineaInvestigacionDocente ld : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId())) {
//                            for (LineaInvestigacionProyecto lp : proyecto.getLineaInvestigacionProyectoList()) {
//                                if (lp.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
//                                    if (docenteCarrera.isEsActivo()) {
//                                        if (!docentesProyectos.contains(docenteCarrera.getDocenteId())) {
//                                            if (!docentes.contains(docenteCarrera.getDocenteId())) {
//                                                docentes.add(docenteCarrera.getDocenteId());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            } else {
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                    for (DocenteCarrera docenteCarrera : docenteCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
//                        Docente docente = docenteFacadeLocal.find(docenteCarrera.getDocenteId());
//                        for (LineaInvestigacionDocente ld : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docente.getId())) {
//                            for (LineaInvestigacionProyecto lp : proyecto.getLineaInvestigacionProyectoList()) {
//                                if (lp.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
//                                    if (docenteCarrera.isEsActivo()) {
//                                        if (!docentes.contains(docenteCarrera.getDocenteId())) {
//                                            docentes.add(docenteCarrera.getDocenteId());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listadoLineasInvestigacionProyecto(Proyecto proyecto) {
        List<LineaInvestigacion> lineasInvestigacionProyecto = new ArrayList<>();
        List<LineaInvestigacion> lineasInvestigacion = new ArrayList<>();
        try {
            if (proyecto.getId() != null) {
                for (LineaInvestigacionProyecto lp : lineaInvestigacionProyectoFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                    lineasInvestigacionProyecto.add(lp.getLineaInvestigacionId());
                }
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    for (LineaInvestigacionCarrera lc : lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
                        if (!lineasInvestigacionProyecto.contains(lc.getLineaInvestigacionId())) {
                            lineasInvestigacion.add(lc.getLineaInvestigacionId());
                        }
                    }
                }
            } else {
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    for (LineaInvestigacionCarrera lc : lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
                        lineasInvestigacion.add(lc.getLineaInvestigacionId());
                    }
                }
            }
            lineasInvestigacionDualList = new DualListModel<>(lineasInvestigacion, lineasInvestigacionProyecto);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listadoCarreras(Proyecto proyecto) {
        List<Carrera> carrerasProyecto = new ArrayList<>();
        List<Carrera> carreras = new ArrayList<>();
        try {
            if (proyecto.getId() != null) {
                for (ProyectoCarreraOferta pco : proyectoCarreraOfertaFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                    if (pco.getEsActivo()) {
                        carrerasProyecto.add(carreraFacadeLocal.find(pco.getCarreraId()));
                    }
                }
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
                    if (!carrerasProyecto.contains(carrera)) {
                        carreras.add(carrera);
                    }
                }
            } else {
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
                    carreras.add(carrera);
                }
            }
            carrerasDualList = new DualListModel<>(carreras, carrerasProyecto);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarProyectosPorCulminar(Usuario usuario) {
        try {
            proyectosPorCulminar = new ArrayList<>();
            for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                for (Proyecto proyecto : proyectoFacadeLocal.buscarPorCulminar(usuarioCarrera.getCarreraId())) {
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
            for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                for (Proyecto proyecto : proyectoFacadeLocal.buscarCaducados(usuarioCarrera.getCarreraId())) {
//                    if (!proyectosCaducados.contains(proyecto)) {
//                        proyectosCaducados.add(proyecto);
//                    }
//                }
            }
        } catch (Exception e) {
        }
    }

    public List<Proyecto> getProyectosAdjudicacionDirector() {
        return proyectosAdjudicacionDirector;
    }

    public void setProyectosAdjudicacionDirector(List<Proyecto> proyectosAdjudicacionDirector) {
        this.proyectosAdjudicacionDirector = proyectosAdjudicacionDirector;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

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

    public AdministrarTipoProyectos getAdministrarTipoProyectos() {
        return administrarTipoProyectos;
    }

    public void setAdministrarTipoProyectos(AdministrarTipoProyectos administrarTipoProyectos) {
        this.administrarTipoProyectos = administrarTipoProyectos;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionProyecto getSessionProyecto() {
        return sessionProyecto;
    }

    public void setSessionProyecto(SessionProyecto sessionProyecto) {
        this.sessionProyecto = sessionProyecto;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
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

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public AdministrarTemaProyectos getAdministrarTemaProyectos() {
        return administrarTemaProyectos;
    }

    public void setAdministrarTemaProyectos(AdministrarTemaProyectos administrarTemaProyectos) {
        this.administrarTemaProyectos = administrarTemaProyectos;
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

    public void setAdministrarCatalogoDuracion(AdministrarCatalogoDuracion administrarCatalogoDuracion) {
        this.administrarCatalogoDuracion = administrarCatalogoDuracion;
    }

    public AdministrarAutoresProyecto getAdministrarAutoresProyecto() {
        return administrarAutoresProyecto;
    }

    public void setAdministrarAutoresProyecto(AdministrarAutoresProyecto administrarAutoresProyecto) {
        this.administrarAutoresProyecto = administrarAutoresProyecto;
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

    public AdministrarCatalogoProyectos getAdministrarCatalogoProyectos() {
        return administrarCatalogoProyectos;
    }

    public void setAdministrarCatalogoProyectos(AdministrarCatalogoProyectos administrarCatalogoProyectos) {
        this.administrarCatalogoProyectos = administrarCatalogoProyectos;
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

    public AdministrarEstadoProyecto getAdministrarEstadoProyecto() {
        return administrarEstadoProyecto;
    }

    public void setAdministrarEstadoProyecto(AdministrarEstadoProyecto administrarEstadoProyecto) {
        this.administrarEstadoProyecto = administrarEstadoProyecto;
    }

    public AdministrarDocumentosExpediente getAdministrarDocumentosExpediente() {
        return administrarDocumentosExpediente;
    }

    public void setAdministrarDocumentosExpediente(AdministrarDocumentosExpediente administrarDocumentosExpediente) {
        this.administrarDocumentosExpediente = administrarDocumentosExpediente;
    }
    //</editor-fold>

}
