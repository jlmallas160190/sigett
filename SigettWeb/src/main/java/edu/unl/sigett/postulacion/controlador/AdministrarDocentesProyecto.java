/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import edu.unl.sigett.autor.controlador.AdministrarAutoresProyecto;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.postulacion.managed.session.SessionDocenteProyecto;
import edu.unl.sigett.proyecto.managed.session.SessionProyecto;
import edu.unl.sigett.reportes.AdministrarReportes;
import edu.unl.sigett.comun.controlador.AdministrarConfiguraciones;
import edu.unl.sigett.seguridad.managed.session.SessionDocenteUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import edu.unl.sigett.entity.OficioCarrera;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import org.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.CatalogoOficioFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.dao.CoordinadorPeriodoDao;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.unl.sigett.dao.DocenteProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import edu.unl.sigett.dao.OficioCarreraFacadeLocal;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.comun.managed.session.SessionOficioCarrera;
import edu.unl.sigett.enumeration.CatalogoOficioEnum;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.util.MessageView;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "docenteProyectos",
            pattern = "/docenteProyectos/",
            viewId = "/faces/pages/sigett/buscarDocentesProyecto.xhtml"
    ),
    @URLMapping(
            id = "editarDocenteProyecto",
            pattern = "/editarDocenteProyecto/#{sessionDocenteProyecto.docenteProyecto.id}",
            viewId = "/faces/pages/sigett/editarDocenteProyecto.xhtml"
    )
})
public class AdministrarDocentesProyecto implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionOficioCarrera sessionOficioCarrera;
    @Inject
    private SessionDocenteProyecto sessionDocenteProyecto;
    @Inject
    private SessionDocenteUsuario sessionDocenteUsuario;
    @Inject
    private AdministrarAutoresProyecto administrarAutoresProyecto;
    @Inject
    private AdministrarDocumentosProyecto administrarDocumentosProyecto;
    @Inject
    private AdministrarPertinencias administrarPertinencias;
    @Inject
    private AdministrarConfiguraciones administrarConfiguraciones;
    @Inject
    private AdministrarCronograma administrarCronograma;

    private MessageView view;

    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    private OficioCarreraFacadeLocal oficioCarreraFacadeLocal;
    @EJB
    private CatalogoOficioFacadeLocal catalogoOficioFacadeLocal;
    @EJB
    private DocenteCarreraDao docenteCarreraFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private DocenteProyectoFacadeLocal docenteProyectoFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private UsuarioCarreraDao usuarioCarreraFacadeLocal;
    @EJB
    private CoordinadorPeriodoDao coordinadorPeriodoFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
    @EJB
    private DocenteDao docenteFacadeLocal;
    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteFacadeLocal;

    private String criterioDocenteDisponible;
    private String criterioDocenteProyecto;
    private String criterioDocente;
    private String contenidoOficio;
    private Integer carreraId;
    private Long docenteProyectoId;
    private int intervalo;

    private List<DocenteProyecto> docenteProyectosPorDocente;
    private List<Docente> docentesDisponibles;
    private List<DocenteProyecto> docenteProyectos;
    private List<DocenteProyecto> docenteProyectosNoPertinencia;
    private List<DocenteProyecto> docentesProyectosParaPertinencia;

    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedEliminar;
    private boolean renderedSeleccionarEspecialista;
    private boolean renderedBuscarEspecialista;
    private boolean renderedSortearDocente;
    private boolean renderedBuscar;
    private boolean renderedDlgBuscarDocentesDisponibles;
    private boolean renderedDlgOficioDocenteProyecto;
    private boolean renderedDlgEditar;
    private boolean renderedImprimirOficio;

    public AdministrarDocentesProyecto() {
        this.renderedDlgBuscarDocentesDisponibles = false;
        this.renderedDlgOficioDocenteProyecto = false;
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente_proyecto");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedImprimirOficio(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_docente_proyecto");
            if (tienePermiso == 1) {
                renderedImprimirOficio = true;
            } else {
                renderedImprimirOficio = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedSeleccionarDocenteEspecialista(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getId() == 1) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "select_docente_especialista");
//                if (tienePermiso == 1 && permiteAgregarDocente(proyecto)) {
//                    renderedSeleccionarEspecialista = true;
//                } else {
//                    renderedSeleccionarEspecialista = false;
//                }
//            } else {
//                renderedSeleccionarEspecialista = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedBuscarEspecialista(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getId() == 1) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente_especialista");
//                if (tienePermiso == 1) {
//                    renderedBuscarEspecialista = true;
//                } else {
//                    renderedBuscarEspecialista = false;
//                }
//            } else {
//                renderedSeleccionarEspecialista = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedEliminar(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getId() == 1) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_docente_proyecto");
//                if (sessionProyecto.getProyecto().getEstadoProyectoId() != null) {
//                    if (tienePermiso == 1) {
//                        renderedEliminar = true;
//                    } else {
//                        renderedEliminar = false;
//                    }
//                }
//            } else {
//                renderedEliminar = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedSortearDocente(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getId() == 1) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sortear_docente_especialista");
//                if (tienePermiso == 1 && permiteAgregarDocente(proyecto)) {
//                    renderedSortearDocente = true;
//                } else {
//                    renderedSortearDocente = false;
//                }
//            } else {
//                renderedSortearDocente = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedEditar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_docente_proyecto");
        if (tienePermiso == 1) {
            renderedEditar = true;
            renderedNoEditar = false;
        } else {
            renderedEditar = false;
            renderedNoEditar = true;
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void imprimirOficioDocenteProyecto(DocenteProyecto docenteProyecto, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (docenteProyecto.getId() != null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_docente_proyecto");
                if (tienePermiso == 1) {
                    docenteProyectoId = docenteProyecto.getId();
                    for (ProyectoCarreraOferta pco : docenteProyecto.getProyectoId().getProyectoCarreraOfertaList()) {
                        if (pco.getEsActivo()) {
                            carreraId = pco.getCarreraId();
                            break;
                        }
                    }
                    OficioCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(docenteProyecto.getId(), CatalogoOficioEnum.DOCENTEPROYECTO.getTipo());
                    if (oficioCarrera != null) {
                        sessionOficioCarrera.setOficioCarrera(oficioCarrera);
                    } else {
                        sessionOficioCarrera.setOficioCarrera(new OficioCarrera());
                    }
                    renderedDlgOficioDocenteProyecto = true;
                    RequestContext.getCurrentInstance().execute("PF('dlgOficioDocenteProyecto').show()");
                } else {
                    view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
                }
            } else {
                view.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadDocx(Usuario user) {
        Map datosReporte = new HashMap();
        AdministrarReportes reportes = new AdministrarReportes();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String path = request.getRealPath("/");
        String pathSetting = request.getRealPath("/settings.txt");
        Carrera carrera = carreraFacadeLocal.find(carreraId);
//        ConfiguracionCarrera configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carreraId, "NO");
//        Integer nOficio = Integer.parseInt(configuracionCarrera.getValor());
        String plazo = configuracionGeneralFacadeLocal.find(3).getValor() + " días laborables";
        DocenteProyecto docenteProyecto = docenteProyectoFacadeLocal.find(docenteProyectoId);
        Docente docente = docenteFacadeLocal.find(docenteProyecto.getDocenteId());
        Persona datosDocente = personaFacadeLocal.find(docente.getId());
        String secretario = "";
        CoordinadorPeriodo coordinadorPeriodo = null;
//        coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
        Persona datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
        Docente docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
        secretario = user.getNombres().toUpperCase() + " " + user.getApellidos().toUpperCase();
        OficioCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(docenteProyecto.getId(), CatalogoOficioEnum.DOCENTEPROYECTO.getTipo());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Calendar fechaActual = Calendar.getInstance();
        String fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());
        datosReporte.put("docente", datosDocente.getNombres().toUpperCase() + " " + datosDocente.getApellidos().toUpperCase());
        datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
        datosReporte.put("temaProyecto", docenteProyecto.getProyectoId().getTemaActual());
        datosReporte.put("tituloDocente", docente.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
        datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
        datosReporte.put("abreviacion_oficio", bundle.getString("lbl.abreviacion_oficio"));
        datosReporte.put("articulos_pertinencia_tt", bundle.getString("lbl.articulos_pertinencia_tt"));
        datosReporte.put("asunto_pertinencia_tt", bundle.getString("lbl.asunto_pertinencia_tt"));
        datosReporte.put("nota_pertinencia_tt", bundle.getString("lbl.nota_pertinencia_tt"));
        datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(docenteProyecto.getProyectoId().getId())));
//        reportes.oficioDocenteProyecto(response, datosReporte, oficioCarrera, path, pathSetting, "docx", nOficio, carrera, fechaFormateada,
//                plazo, secretario, configuracionCarreraFacadeLocal, configuracionCarrera, docenteProyectoId, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal);
    }

    private String getAutores(List<AutorProyecto> autorProyectos) {
        String datosAutores = "";
        int cont = 0;
        try {
            for (AutorProyecto autorProyecto : autorProyectos) {
//                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
//                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//                    Persona datosAutor = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
//                    if (cont == 0) {
////                        datosAutores += "" + estudianteCarrera.getEstadoId().getNombre() + " " + datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase() + "";
//                        cont++;
//                    } else {
////                        datosAutores += ", " + estudianteCarrera.getEstadoId().getNombre() + " " + datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase();
//                        cont++;
//                    }
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datosAutores;
    }

    public void sortearDocente(List<LineaInvestigacion> lineaInvestigacions, Usuario usuario, Proyecto proyecto) {
        Random random = new Random();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sortear_docente_especialista");
            if (tienePermiso == 1 && permiteAgregarDocente(proyecto)) {
                List<Docente> docentes = new ArrayList<>();
                docentes.addAll(listadoDocentesSorteo(lineaInvestigacions));
                int numeroProbabilidades = docentes.size();
                int pos = random.nextInt(numeroProbabilidades);
                Docente docente = docentes.get(pos);
                agregarDocenteProyectoSorteo(docente, usuario, proyecto);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void onTabChange(TabChangeEvent event) {
        Usuario usuario=usuarioFacadeLocal.find(sessionDocenteUsuario.getDocenteUsuario().getId());
        switch (event.getTab().getId()) {
            case "pertinencias":
                administrarPertinencias.renderedCrear(usuario, sessionDocenteProyecto.getDocenteProyecto());
                administrarPertinencias.renderedEditar(usuario, sessionDocenteProyecto.getDocenteProyecto());
                administrarPertinencias.renderedEliminar(usuario, sessionDocenteProyecto.getDocenteProyecto());
                administrarPertinencias.renderedImprimirInforme(usuario);
                administrarPertinencias.buscar(usuario, sessionDocenteProyecto.getDocenteProyecto());
                administrarPertinencias.renderedAceptar(usuario);
                break;
            case "autores":
//                administrarAutoresProyecto.buscar("", sessionDocenteProyecto.getDocenteProyecto().getProyectoId(),usuario);
                break;
            case "inicio":
                administrarCronograma.renderedCronograma(sessionDocenteProyecto.getDocenteProyecto().getProyectoId());
        }
    }

    public String editar(DocenteProyecto docenteProyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionDocenteUsuario.getUsuario(), "editar_docente_proyecto");
            if (tienePermiso == 1) {
                sessionDocenteProyecto.setDocenteProyecto(docenteProyecto);
                administrarDocumentosProyecto.buscarAnteproyectos(docenteProyecto.getProyectoId(), sessionDocenteUsuario.getUsuario());
                administrarAutoresProyecto.buscarAutoresDesdeDocenteProyecto("", docenteProyecto.getProyectoId());
//                intervalo = administrarConfiguraciones.intervaloActualizaciones();
                administrarPertinencias.renderedCrear(sessionDocenteUsuario.getUsuario(), docenteProyecto);
                administrarPertinencias.renderedEditar(sessionDocenteUsuario.getUsuario(), docenteProyecto);
                administrarPertinencias.renderedEliminar(sessionDocenteUsuario.getUsuario(), docenteProyecto);
                administrarPertinencias.renderedImprimirInforme(sessionDocenteUsuario.getUsuario());
                administrarPertinencias.buscar(sessionDocenteUsuario.getUsuario(), docenteProyecto);
                administrarPertinencias.renderedAceptar(sessionDocenteUsuario.getUsuario());
                administrarCronograma.renderedCronograma(sessionDocenteProyecto.getDocenteProyecto().getProyectoId());
                if (param.equalsIgnoreCase("editar-dlg")) {
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarDocenteProyecto').show()");
                } else {
                    if (param.equalsIgnoreCase("editar")) {
                        navegacion = "pretty:editarDocenteProyecto";
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void view(DocenteProyecto docenteProyecto) {
        try {
            sessionDocenteProyecto.setDocenteProyecto(docenteProyecto);
            renderedDlgEditar = true;
            RequestContext.getCurrentInstance().execute("PF('dlgEditarDocenteProyecto').show()");
        } catch (Exception e) {
        }
    }

    public String openbuscarDocentesDisponible(String criterio, List<LineaInvestigacion> lineaInvestigacions) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            listadoDocentesDiponibles(criterio, lineaInvestigacions);
            if (param.equalsIgnoreCase("buscar-especialistas-dlg")) {
                renderedDlgBuscarDocentesDisponibles = true;
                RequestContext.getCurrentInstance().execute("PF('dlgBuscarDocentesDisponibles').show()");
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void agregarDocenteProyecto(Docente docente, Usuario usuario, Proyecto proyecto) {
        Calendar fecha = Calendar.getInstance();
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "select_docente_especialista");
//                if (tienePermiso == 1) {
//                    if (permiteAgregarDocente(proyecto)) {
//                        DocenteProyecto docenteProyecto = new DocenteProyecto();
//                        docenteProyecto.setDocenteId(docente.getId());
//                        docenteProyecto.setEsActivo(true);
//                        docenteProyecto.setFecha(fecha.getTime());
//                        docenteProyecto.setProyectoId(proyecto);
//                        DocenteProyecto dp = devuelveDocenteProyecto(proyecto, docenteProyecto);
//                        if (dp == null) {
//                            docenteProyecto.setEsEditado(true);
//                            proyecto.getDocenteProyectoList().add(docenteProyecto);
//                        } else {
//                            dp.setDocenteId(docente.getId());
//                            dp.setEsEditado(true);
//                            dp.setEsActivo(true);
//                            dp.setFecha(fecha.getTime());
//
//                        }
//                        buscar("", usuario, proyecto);
//                        if (param.equalsIgnoreCase("seleccionar-dlg")) {
//                            renderedBuscarEspecialista(usuario, proyecto);
//                            renderedSortearDocente(usuario, proyecto);
//                            renderedSeleccionarDocenteEspecialista(usuario, proyecto);
//                            RequestContext.getCurrentInstance().execute("PF('dlgBuscarDocentesDisponibles').hide()");
//                            sessionDocenteProyecto.setDocenteProyecto(new DocenteProyecto());
//                        }
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe") + " " + bundle.getString("lbl.docente"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public boolean permiteAgregarDocente(Proyecto proyecto) {
        boolean var = false;
        String val = configuracionGeneralFacadeLocal.find((int) 19).getValor();
        if (!proyecto.getDocenteProyectoList().isEmpty()) {
            for (DocenteProyecto directorProyecto : proyecto.getDocenteProyectoList()) {
                if (directorProyecto.getEsActivo()) {
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
        } else {
            var = true;
        }
        return var;
    }

    public void agregarDocenteProyectoSorteo(Docente docente, Usuario usuario, Proyecto proyecto) {
        Calendar fecha = Calendar.getInstance();
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "sortear_docente_especialista");
//                if (tienePermiso == 1) {
//                    if (permiteAgregarDocente(proyecto)) {
//                        DocenteProyecto docenteProyecto = new DocenteProyecto();
//                        docenteProyecto.setDocenteId(docente.getId());
//                        docenteProyecto.setEsActivo(true);
//                        docenteProyecto.setFecha(fecha.getTime());
//                        docenteProyecto.setProyectoId(sessionProyecto.getProyecto());
//                        DocenteProyecto dp = devuelveDocenteProyecto(sessionProyecto.getProyecto(), docenteProyecto);
//                        docenteProyecto.setEsEditado(true);
//                        if (dp == null) {
//                            sessionProyecto.getProyecto().getDocenteProyectoList().add(docenteProyecto);
//                        } else {
//                            dp.setDocenteId(docente.getId());
//                            dp.setEsActivo(true);
//                            dp.setEsEditado(true);
//                            dp.setFecha(fecha.getTime());
//
//                        }
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        renderedBuscarEspecialista(usuario, proyecto);
//                        renderedSortearDocente(usuario, proyecto);
//                        renderedSeleccionarDocenteEspecialista(usuario, proyecto);
//                        buscar("", usuario, proyecto);
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe") + " " + bundle.getString("lbl.docente"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public DocenteProyecto devuelveDocenteProyecto(Proyecto proyecto, DocenteProyecto dp) {
        DocenteProyecto docente = null;
        try {
            for (DocenteProyecto docenteProyecto : proyecto.getDocenteProyectoList()) {
                if (docenteProyecto.getDocenteId().equals(dp.getDocenteId())) {
                    docente = docenteProyecto;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return docente;
    }

    public void removerDocenteProyecto(DocenteProyecto docenteProyecto, Usuario usuario, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_docente_proyecto");
//                if (tienePermiso == 1) {
//                    if (docenteProyecto.getId() != null) {
//                        docenteProyecto.setEsActivo(false);
//                        docenteProyecto.setEsEditado(true);
//                        logFacadeLocal.create(logFacadeLocal.crearLog("DocenteProyecto", docenteProyecto.getId() + "", "DESACTIVAR", "|Docente= " + docenteProyecto.getDocenteId() + "|Proyecto= " + docenteProyecto.getProyectoId().getId() + "|EsActivo= " + docenteProyecto.getEsActivo(), sessionUsuario.getUsuario()));
//                    } else {
//                        proyecto.getDocenteProyectoList().remove(docenteProyecto);
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                    buscar("", usuario, proyecto);
//                    renderedBuscarEspecialista(usuario, proyecto);
//                    renderedSortearDocente(usuario, proyecto);
//                    renderedSeleccionarDocenteEspecialista(usuario, proyecto);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public List<Docente> listadoDocentesSorteo(List<LineaInvestigacion> lineaInvestigacions) {
        List<Docente> docentesDisponiblesSorteo = new ArrayList<>();
        try {
            for (UsuarioCarrera usuarioCarrera :usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                for (DocenteCarrera docenteCarrera : docenteCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
//                    for (LineaInvestigacion li : lineaInvestigacions) {
//                        for (LineaInvestigacionDocente lid : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docenteCarrera.getDocenteId().getId())) {
//                            if (lid.getLineaInvestigacionId().equals(li)) {
//                                docentesDisponiblesSorteo.add(docenteCarrera.getDocenteId());
//                            }
//                        }
//                    }
//                }
            }
        } catch (Exception e) {
        }
        return docentesDisponiblesSorteo;
    }

    public void listadoDocentesDiponibles(String criterio, List<LineaInvestigacion> lineaInvestigacions) {
        docentesDisponibles = new ArrayList<>();
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_docente_especialista");
            if (tienePermiso == 1) {
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                    for (DocenteCarrera docenteCarrera : docenteCarreraFacadeLocal.buscarPorCarrera(usuarioCarrera.getCarreraId())) {
//                        if (docenteCarrera.isEsActivo()) {
//                            Persona persona=personaFacadeLocal.find(docenteCarrera.getDocenteId());
//                            for (LineaInvestigacion li : lineaInvestigacions) {
//                                for (LineaInvestigacionDocente lid : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(docenteCarrera.getDocenteId().getId())) {
//                                    if (lid.getLineaInvestigacionId().equals(li)) {
//                                        if (persona.getApellidos().toLowerCase().contains(criterio.toLowerCase()) || 
//                                                persona.getNombres().toLowerCase().contains(criterio.toLowerCase())
//                                                ||persona.getNumeroIdentificacion().contains(criterio)) {
//                                            if (!docentesDisponibles.contains(docenteCarrera.getDocenteId())) {
//                                                docentesDisponibles.add(docenteCarrera.getDocenteId());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para buscar Docentes Especialistas. Consulte con el administrador del Sistema.", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void buscar(String criterio, Usuario usuario, Proyecto proyecto) {
        this.docenteProyectos = new ArrayList<>();
        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente_proyecto");
            if (tienePermiso == 1) {
                for (DocenteProyecto docenteProyecto : proyecto.getDocenteProyectoList()) {
                    Persona persona=personaFacadeLocal.find(docenteProyecto.getDocenteId());
                    if (persona.getNombres().contains(criterio) || persona.getApellidos().contains(criterio)
                            ||persona.getNumeroIdentificacion().contains(criterio)) {
                        if (docenteProyecto.getEsActivo()) {
                            docenteProyectos.add(docenteProyecto);
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void buscarProyectosPorDocente(String criterio, Usuario usuario) {
        docenteProyectosPorDocente = new ArrayList<DocenteProyecto>();
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente_proyecto");
            if (tienePermiso == 1) {
                for (DocenteProyecto docenteProyecto : docenteProyectoFacadeLocal.buscarProyectosPorDocente(sessionDocenteUsuario.getDocenteUsuario().getDocenteId())) {
                    if (docenteProyecto.getProyectoId().getTemaActual().toLowerCase().contains(criterio.toLowerCase())) {
                        if (!docenteProyectosPorDocente.contains(docenteProyecto)) {
                            docenteProyectosPorDocente.add(docenteProyecto);
                        }
                    }
                }
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para listar Proyectos. Consulte con el administrador del Sistema", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public void buscarProyectosPorDocenteTodo(String criterio, Usuario usuario) {
        docenteProyectosPorDocente = new ArrayList<DocenteProyecto>();
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente_proyecto");
            if (tienePermiso == 1) {
                docenteProyectosPorDocente = docenteProyectoFacadeLocal.buscarProyectosPorDocente(sessionDocenteUsuario.getDocenteUsuario().getDocenteId());
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para listar Proyectos. Consulte con el administrador del Sistema", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public void docenteProyectosSinPertinencia(Usuario usuario) {
        this.docenteProyectosNoPertinencia = new ArrayList<>();
        for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(usuario.getId())) {
            for (DocenteProyecto docenteProyecto : docenteProyectoFacadeLocal.buscarSinPertinencia(usuarioCarrera.getCarreraId())) {
                if (!docenteProyectosNoPertinencia.contains(docenteProyecto)) {
                    docenteProyectosNoPertinencia.add(docenteProyecto);
                }
            }
        }
    }

    public void docenteProyectosParaPertinencia(Docente docente) {
        this.docentesProyectosParaPertinencia = new ArrayList<>();
        Persona persona=personaFacadeLocal.find(docente.getId());
        try {
            for (DocenteProyecto docenteProyecto : docenteProyectoFacadeLocal.buscarDocenteProyectoParaPertinencia(persona.getNumeroIdentificacion())) {
                if (!docentesProyectosParaPertinencia.contains(docenteProyecto)) {
                    docentesProyectosParaPertinencia.add(docenteProyecto);
                }
            }
        } catch (Exception e) {
        }
    }

//    public void grabarOficio(String contenido) {
//        try {
//            OficioCarrera oficioCarrera = new OficioCarrera();
//            Carrera carrera = carreraFacadeLocal.find(carreraId);
//            oficioCarrera.setCarreraId(carrera);
//            CatalogoOficio catalogoOficio = new CatalogoOficio();
//            catalogoOficio = catalogoOficioFacadeLocal.find(1);
//            oficioCarrera.setCatalogoOficioId(catalogoOficio);
//            oficioCarrera.setEsActivo(true);
//            Calendar calendar = Calendar.getInstance();
//            oficioCarrera.setFecha(calendar.getTime());
//            oficioCarrera.setNumeroOficio("1");
//            oficioCarrera.setTablaOficioId(docenteProyectoId);
//            oficioCarrera.setOficio(contenido.getBytes("ISO-8859-1"));
//            oficioCarreraFacadeLocal.create(oficioCarrera);
//        } catch (Exception e) {
//        }
//    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public void setContenidoOficio(String contenidoOficio) {
        this.contenidoOficio = contenidoOficio;
    }

    public String getContenidoOficio() {
        return contenidoOficio;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }

    public List<DocenteProyecto> getDocentesProyectosParaPertinencia() {
        return docentesProyectosParaPertinencia;
    }

    public void setDocentesProyectosParaPertinencia(List<DocenteProyecto> docentesProyectosParaPertinencia) {
        this.docentesProyectosParaPertinencia = docentesProyectosParaPertinencia;
    }

    public List<DocenteProyecto> getDocenteProyectosNoPertinencia() {
        return docenteProyectosNoPertinencia;
    }

    public void setDocenteProyectosNoPertinencia(List<DocenteProyecto> docenteProyectosNoPertinencia) {
        this.docenteProyectosNoPertinencia = docenteProyectosNoPertinencia;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
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

    public SessionDocenteProyecto getSessionDocenteProyecto() {
        return sessionDocenteProyecto;
    }

    public void setSessionDocenteProyecto(SessionDocenteProyecto sessionDocenteProyecto) {
        this.sessionDocenteProyecto = sessionDocenteProyecto;
    }

    public String getCriterioDocenteDisponible() {
        return criterioDocenteDisponible;
    }

    public void setCriterioDocenteDisponible(String criterioDocenteDisponible) {
        this.criterioDocenteDisponible = criterioDocenteDisponible;
    }

    public String getCriterioDocenteProyecto() {
        return criterioDocenteProyecto;
    }

    public void setCriterioDocenteProyecto(String criterioDocenteProyecto) {
        this.criterioDocenteProyecto = criterioDocenteProyecto;
    }

    public SessionDocenteUsuario getSessionDocenteUsuario() {
        return sessionDocenteUsuario;
    }

    public void setSessionDocenteUsuario(SessionDocenteUsuario sessionDocenteUsuario) {
        this.sessionDocenteUsuario = sessionDocenteUsuario;
    }

    public String getCriterioDocente() {
        return criterioDocente;
    }

    public void setCriterioDocente(String criterioDocente) {
        this.criterioDocente = criterioDocente;
    }

    public List<DocenteProyecto> getDocenteProyectosPorDocente() {
        return docenteProyectosPorDocente;
    }

    public void setDocenteProyectosPorDocente(List<DocenteProyecto> docenteProyectosPorDocente) {
        this.docenteProyectosPorDocente = docenteProyectosPorDocente;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<Docente> getDocentesDisponibles() {
        return docentesDisponibles;
    }

    public void setDocentesDisponibles(List<Docente> docentesDisponibles) {
        this.docentesDisponibles = docentesDisponibles;
    }

    public List<DocenteProyecto> getDocenteProyectos() {
        return docenteProyectos;
    }

    public void setDocenteProyectos(List<DocenteProyecto> docenteProyectos) {
        this.docenteProyectos = docenteProyectos;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public boolean isRenderedSeleccionarEspecialista() {
        return renderedSeleccionarEspecialista;
    }

    public void setRenderedSeleccionarEspecialista(boolean renderedSeleccionarEspecialista) {
        this.renderedSeleccionarEspecialista = renderedSeleccionarEspecialista;
    }

    public boolean isRenderedBuscarEspecialista() {
        return renderedBuscarEspecialista;
    }

    public void setRenderedBuscarEspecialista(boolean renderedBuscarEspecialista) {
        this.renderedBuscarEspecialista = renderedBuscarEspecialista;
    }

    public boolean isRenderedSortearDocente() {
        return renderedSortearDocente;
    }

    public void setRenderedSortearDocente(boolean renderedSortearDocente) {
        this.renderedSortearDocente = renderedSortearDocente;
    }

    public AdministrarAutoresProyecto getAdministrarAutoresProyecto() {
        return administrarAutoresProyecto;
    }

    public void setAdministrarAutoresProyecto(AdministrarAutoresProyecto administrarAutoresProyecto) {
        this.administrarAutoresProyecto = administrarAutoresProyecto;
    }

    public AdministrarDocumentosProyecto getAdministrarDocumentosProyecto() {
        return administrarDocumentosProyecto;
    }

    public void setAdministrarDocumentosProyecto(AdministrarDocumentosProyecto administrarDocumentosProyecto) {
        this.administrarDocumentosProyecto = administrarDocumentosProyecto;
    }

    public AdministrarPertinencias getAdministrarPertinencias() {
        return administrarPertinencias;
    }

    public void setAdministrarPertinencias(AdministrarPertinencias administrarPertinencias) {
        this.administrarPertinencias = administrarPertinencias;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public boolean isRenderedDlgBuscarDocentesDisponibles() {
        return renderedDlgBuscarDocentesDisponibles;
    }

    public void setRenderedDlgBuscarDocentesDisponibles(boolean renderedDlgBuscarDocentesDisponibles) {
        this.renderedDlgBuscarDocentesDisponibles = renderedDlgBuscarDocentesDisponibles;
    }

    public boolean isRenderedDlgOficioDocenteProyecto() {
        return renderedDlgOficioDocenteProyecto;
    }

    public void setRenderedDlgOficioDocenteProyecto(boolean renderedDlgOficioDocenteProyecto) {
        this.renderedDlgOficioDocenteProyecto = renderedDlgOficioDocenteProyecto;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public Long getDocenteProyectoId() {
        return docenteProyectoId;
    }

    public void setDocenteProyectoId(Long docenteProyectoId) {
        this.docenteProyectoId = docenteProyectoId;
    }

    public boolean isRenderedImprimirOficio() {
        return renderedImprimirOficio;
    }

    public void setRenderedImprimirOficio(boolean renderedImprimirOficio) {
        this.renderedImprimirOficio = renderedImprimirOficio;
    }
//</editor-fold>

}
