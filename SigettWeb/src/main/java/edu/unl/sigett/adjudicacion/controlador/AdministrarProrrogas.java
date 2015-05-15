/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.adjudicacion.controlador;

import org.jlmallas.api.date.DateResource;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.adjudicacion.session.SessionProrroga;
import edu.unl.sigett.postulacion.managed.session.SessionProyecto;
import edu.unl.sigett.reportes.AdministrarReportes;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.OficioCarrera;
import edu.unl.sigett.entity.Prorroga;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import edu.unl.sigett.dao.ActividadFacadeLocal;
import edu.unl.sigett.dao.AutorProyectoFacadeLocal;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.CatalogoOficioFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.dao.implement.CoordinadorPeriodoFacadeLocal;
import edu.unl.sigett.dao.DirectorProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import edu.unl.sigett.dao.OficioCarreraFacadeLocal;
import edu.unl.sigett.dao.ProrrogaFacadeLocal;
import edu.unl.sigett.dao.ProyectoFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.dao.implement.EstudianteCarreraFacadeLocal;
import edu.unl.sigett.comun.managed.session.SessionOficioCarrera;
import edu.unl.sigett.enumeration.CatalogoOficioEnum;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarProrrogas implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionProrroga sessionProrroga;
    @Inject
    private SessionOficioCarrera sessionOficioCarrera;

    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private AutorProyectoFacadeLocal autorProyectoFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private OficioCarreraFacadeLocal oficioCarreraFacadeLocal;
    @EJB
    private CatalogoOficioFacadeLocal catalogoOficioFacadeLocal;
    @EJB
    private ActividadFacadeLocal actividadFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ProyectoFacadeLocal proyectoFacadeLocal;
    @EJB
    private ProrrogaFacadeLocal prorrogaFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private DirectorProyectoFacadeLocal directorProyectoFacadeLocal;
    @EJB
    private CoordinadorPeriodoFacadeLocal coordinadorPeriodoFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private EstudianteCarreraFacadeLocal estudianteCarreraFacadeLocal;
    @EJB
    private DocenteCarreraDao docenteCarreraFacadeLocal;
    @EJB
    private DocenteDao docenteFacadeLocal;

    private boolean renderedDlgEditar;
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedEliminar;
    private boolean renderedBuscar;
    private boolean renderedImprimirOficio;
    private boolean renderedDlgOficio;
    private boolean renderedAceptarProrroga;
    private boolean renderedDlgInforme;
    private boolean renderedDlgRespuestaAutorProyecto;
    private boolean tieneProrroga;

    private List<Prorroga> prorrogas;
    private String criterio;
    private Integer carreraId;
    private Long prorrogaId;

    public AdministrarProrrogas() {
        this.renderedDlgEditar = false;
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedBuscar(Usuario usuario, Proyecto proyecto) {
        if (proyecto.getEstadoProyectoId() != null) {
            if (!proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
                    && !proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_prorroga");
                if (tienePermiso == 1) {
                    renderedBuscar = true;
                } else {
                    renderedBuscar = false;
                }
            } else {
                renderedBuscar = false;
            }
        }
    }

    public void renderedImprimirOficio(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_prorroga");
            if (tienePermiso == 1) {
                renderedImprimirOficio = true;
            } else {
                renderedImprimirOficio = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedEditar(Usuario usuario, Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_prorroga");
                if (tienePermiso == 1) {
                    renderedEditar = true;
                    renderedNoEditar = false;
                } else {
                    renderedEditar = false;
                    renderedNoEditar = true;
                }
            } else {
                renderedEditar = false;
                renderedNoEditar = true;
            }
        } catch (Exception e) {
        }
    }

    public void renderedCrear(Usuario usuario, Proyecto proyecto) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())
                    && fechaActual.getTime().after(proyecto.getCronograma().getFechaProrroga())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_prorroga");
                if (tienePermiso == 1) {
                    renderedCrear = true;
                } else {
                    renderedCrear = false;
                }
            } else {
                renderedCrear = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedEliminar(Usuario usuario, Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_prorroga");
                if (tienePermiso == 1) {
                    renderedEliminar = true;
                } else {
                    renderedEliminar = false;
                }
            } else {
                renderedEliminar = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedAceptar(Usuario usuario, Proyecto proyecto) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "aceptar_prorroga");
                if (tienePermiso == 1) {
                    renderedAceptarProrroga = true;
                } else {
                    renderedAceptarProrroga = false;
                }
            } else {
                renderedAceptarProrroga = false;
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public void imprimirInformeOficioRespuesta(Prorroga prorroga, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (prorroga.getId() != null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_prorroga");
                if (tienePermiso == 1) {
                    prorrogaId = prorroga.getId();
                    for (ProyectoCarreraOferta pco : prorroga.getCronogramaId().getProyecto().getProyectoCarreraOfertaList()) {
                        if (pco.getEsActivo()) {
                            carreraId = pco.getCarreraId();
                            break;
                        }
                    }
                    OficioCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(prorroga.getId(),
                            CatalogoOficioEnum.RESPUESTAPRORROGAAUTOR.getTipo());
                    if (oficioCarrera != null) {
                        sessionOficioCarrera.setOficioCarrera(oficioCarrera);
                    } else {
                        sessionOficioCarrera.setOficioCarrera(new OficioCarrera());
                    }
                    if (param.equalsIgnoreCase("oficio")) {
                        renderedDlgOficio = true;
                        RequestContext.getCurrentInstance().execute("PF('dlgOficioDirectorProrroga').show()");
                    } else {
                        if (param.equalsIgnoreCase("respuesta")) {
                            renderedDlgRespuestaAutorProyecto = true;
                            RequestContext.getCurrentInstance().execute("PF('dlgRespuestaProrrogaAutorProyecto´).show()");
                        } else {
                            renderedDlgInforme = true;
                            RequestContext.getCurrentInstance().execute("PF('dlgInformeDirectorProrroga').show()");

                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void downloadInformeDocx(Usuario user) {
//        Map datosReporte = new HashMap();
//        AdministrarReportes reportes = new AdministrarReportes();
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
//        String path = request.getRealPath("/");
//        String pathSetting = request.getRealPath("/settings.txt");
//        Carrera carrera = carreraFacadeLocal.find(carreraId);
//        ConfiguracionCarrera configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carreraId, "NO");
//        Prorroga prorroga = prorrogaFacadeLocal.find(prorrogaId);
//        String secretario = "";
//        String objetivos = "";
//
//        for (Actividad objetivo : actividadFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getId())) {
//            if (objetivo.getTipoActividadId().getId() == 1) {
//                double valor = (objetivo.getAvance() * objetivo.getPorcentajeDuracion()) / 100;
//                valor = Math.round(valor * 100);
//                valor = valor / 100;
//                objetivos += "" + valor + "% " + objetivo.getNombre() + "\n";
//            }
//        }
//        DirectorProyecto dp = null;
//        for (DirectorProyecto directorProyecto : directorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())) {
//            if (directorProyecto.getEstadoDirectorId().getId() != 2) {
//                dp = directorProyecto;
//                break;
//            }
//        }
//        Docente docenteDirectorInforme = docenteCarreraFacadeLocal.find(dp.getDirectorId().getId()).getDocenteId();
//        Persona datosDocenteDirectorInforme = personaFacadeLocal.find(docenteDirectorInforme.getId());
//
//        CoordinadorPeriodo coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
//        Persona personaCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
//        Docente docenteCoordinador = docenteFacadeLocal.find(personaCoordinador.getId());
//        secretario = user.getNombres().toUpperCase() + " " + user.getApellidos().toUpperCase();
//        OficioCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(prorrogaId, CatalogoOficioEnum.INFORMEDIRECTORPRORROGA.getTipo());
//        Calendar fechaActual = Calendar.getInstance();
//        String fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());
//        String fechOficioFormat = configuracionGeneralFacadeLocal.dateFormat(oficioCarrera.getFecha());
//        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//        datosReporte.put("tituloDocente", docenteDirectorInforme.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
//        datosReporte.put("docente", datosDocenteDirectorInforme.getNombres().toUpperCase() + " " + datosDocenteDirectorInforme.getApellidos().toUpperCase());
//        datosReporte.put("coordinador", personaCoordinador.getNombres().toUpperCase() + " " + personaCoordinador.getApellidos().toUpperCase());
//        datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
//        datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())));
//        reportes.informeDirectorProrroga("docx", fechOficioFormat + "", objetivos, prorroga.getCronogramaId().getAvance()
//                + "%", prorroga.getObservacion(), fechaFormateada, response, datosReporte, configuracionCarreraFacadeLocal, configuracionCarrera,
//                oficioCarrera, prorrogaId, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, Integer.parseInt(oficioCarrera.getNumeroOficio()), secretario, prorrogaId, path, pathSetting);

    }

    private String getAutores(List<AutorProyecto> autorProyectos) {
        String datosAutores = "";
        int cont = 0;
        try {
            for (AutorProyecto autorProyecto : autorProyectos) {
                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
                    Persona personaAutor = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
                    if (cont == 0) {
                        datosAutores += "" + estudianteCarrera.getEstadoId().getNombre() + " " + personaAutor.getNombres().toUpperCase() + " " + personaAutor.getApellidos().toUpperCase() + "";
                        cont++;
                    } else {
                        datosAutores += ", " + estudianteCarrera.getEstadoId().getNombre() + " " + personaAutor.getNombres().toUpperCase() + " " + personaAutor.getApellidos().toUpperCase();
                        cont++;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datosAutores;
    }

    public void downloadResponseDocx(Usuario user) {
        AdministrarReportes reportes = new AdministrarReportes();
        Map datosReporte = new HashMap();
        Carrera carrera = carreraFacadeLocal.find(carreraId);
        String secretario = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String path = request.getRealPath("/");
        String pathSetting = request.getRealPath("/settings.txt");
        Prorroga prorroga = prorrogaFacadeLocal.find(prorrogaId);

        String resolucion = "";
        String fechaEmision = "";
        if (prorroga.getEsAceptado()) {
            int tiempo = prorroga.getFecha().getMonth() - prorroga.getFechaInicial().getMonth();
            resolucion = "acepta la prorroga de " + tiempo + " meses";
            fechaEmision = configuracionGeneralFacadeLocal.dateFormat(prorroga.getFecha());
            fechaEmision = "a partir del " + fechaEmision + ", fecha en que el director emite su informe";
        } else {
            resolucion = "no acepta la prorroga";
        }
        CoordinadorPeriodo coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
        Persona datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
        Docente docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
        Calendar fechaActual = Calendar.getInstance();
        String fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());
//        ConfiguracionCarrera configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carreraId, "NO");
//        Integer nOficio = Integer.parseInt(configuracionCarrera.getValor());
        secretario = user.getNombres().toUpperCase() + " " + user.getApellidos().toUpperCase();
        OficioCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(prorroga.getId(), CatalogoOficioEnum.RESPUESTAPRORROGAAUTOR.getTipo());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
        datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
        datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())));
        datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));

//        reportes.responseProrrogaAutorProyecto("docx", fechaFormateada, response, datosReporte, configuracionCarreraFacadeLocal, configuracionCarrera,
//                oficioCarrera, prorroga.getId(), oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, nOficio, secretario,
//                fechaEmision, resolucion, path, pathSetting);
    }

    public void downloadDocx(Usuario user) {
        AdministrarReportes reportes = new AdministrarReportes();
        Map datosReporte = new HashMap();
        Carrera carrera = carreraFacadeLocal.find(carreraId);
        String secretario = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        String path = request.getRealPath("/");
        String pathSetting = request.getRealPath("/settings.txt");
        Prorroga prorroga = prorrogaFacadeLocal.find(prorrogaId);

        DirectorProyecto dp = null;
        for (DirectorProyecto directorProyecto : directorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())) {
            if (directorProyecto.getEstadoDirectorId().getId() != 2) {
                dp = directorProyecto;
                break;
            }
        }
        Docente docenteDirectorProyecto = docenteCarreraFacadeLocal.find(dp.getDirectorId().getId()).getDocenteId();
        Persona datosDocenteDirector = personaFacadeLocal.find(docenteDirectorProyecto.getId());
        CoordinadorPeriodo coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
        Persona datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
        Docente docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
//        ConfiguracionCarrera configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carreraId, "NO");
//        Integer nOficio = Integer.parseInt(configuracionCarrera.getValor());
        secretario = user.getNombres() + " " + user.getApellidos();
        OficioCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(prorroga.getId(), CatalogoOficioEnum.PRORROGA.getTipo());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Calendar fechaActual = Calendar.getInstance();
        String fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());
        datosReporte.put("tituloDocente", docenteDirectorProyecto.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
        datosReporte.put("docente", datosDocenteDirector.getNombres().toUpperCase() + " " + datosDocenteDirector.getApellidos().toUpperCase());
        datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
        datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
        datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())));
        datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
        datosReporte.put("asunto_prorroga_tt", resourceBundle.getString("lbl.asunto_prorroga_tt"));
        datosReporte.put("nota_prorroga_tt", resourceBundle.getString("nota_prorroga_tt"));

//        reportes.oficioDirectorProrroga("docx", fechaFormateada, response, datosReporte, configuracionCarreraFacadeLocal, configuracionCarrera,
//                oficioCarrera, prorroga.getId(), oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, nOficio, secretario, path, pathSetting);
    }

    public String editar(Prorroga prorroga, Usuario usuario, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_prorroga");
                if (tienePermiso == 1) {
                    if (param.equalsIgnoreCase("editar-dlg")) {
                        sessionProrroga.setProrroga(prorroga);
                        this.renderedDlgEditar = true;
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarProrroga').show()");
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar" + ". " + bundle.getString("lbl.msm_consulte")), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void tieneProrroga(Proyecto proyecto) {
        try {
            tieneProrroga = false;
            for (Prorroga prorroga : prorrogaFacadeLocal.buscarPorProyecto(proyecto.getId())) {
                if (prorroga.getEsActivo()) {
                    tieneProrroga = true;
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void buscar(Cronograma cronograma, Usuario usuario, String criterio) {
        this.prorrogas = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_prorroga");
            if (tienePermiso == 1) {
                for (Prorroga prorroga : prorrogaFacadeLocal.buscarPorProyecto(cronograma.getId())) {
                    if (prorroga.getEsActivo()) {
                        if (prorroga.getMotivo().toLowerCase().contains(criterio.toLowerCase())) {
                            this.prorrogas.add(prorroga);
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar" + ". " + bundle.getString("lbl.msm_consulte")), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }

    }

    public String crear(Usuario usuario, Proyecto proyecto) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Calendar fechaActual = Calendar.getInstance();
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
            }
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())
                    && fechaActual.getTime().after(proyecto.getCronograma().getFechaProrroga())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_prorroga");
                if (tienePermiso == 1) {
                    sessionProrroga.setProrroga(new Prorroga());
                    sessionProrroga.getProrroga().setEsAceptado(false);
                    this.renderedDlgEditar = true;
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarProrroga').show()");
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear" + ". " + bundle.getString("lbl.msm_consulte")), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear" + ". " + bundle.getString("lbl.msm_consulte")), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public int calcularDiasSemanaTrabajo(Proyecto proyecto) {
        int dias = 0;
        for (ConfiguracionProyecto cf : proyecto.getConfiguracionProyectoList()) {
            if (cf.getCodigo().equalsIgnoreCase("DS")) {
                dias = Integer.parseInt(cf.getValor());
                break;
            }
        }
        return dias;
    }

    public int calculaHorasTrabajoProrroga(Proyecto proyecto) {
        int dias = 0;
        for (ConfiguracionProyecto cf : proyecto.getConfiguracionProyectoList()) {
            if (cf.getCodigo().equalsIgnoreCase("HD")) {
                dias = Integer.parseInt(cf.getValor());
                break;
            }
        }
        return dias;
    }

    public String grabar(Prorroga prorroga, Proyecto proyecto, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Calendar fechaActual = Calendar.getInstance();
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
            }

            double var = Double.parseDouble(configuracionGeneralFacadeLocal.find((int) 22).getValor());
            int varMaxProrroga = Integer.parseInt(configuracionGeneralFacadeLocal.find((int) 23).getValor());
            double var1 = (var / ((1 / (double) varMaxProrroga) * 100));
            double duracionDias = 0;
            double horasTrabajo = 0;
            DateResource calculo = new DateResource();
            if (prorroga.getId() == null) {
                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())
                        && fechaActual.getTime().after(proyecto.getCronograma().getFechaProrroga())) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_prorroga");
                    if (tienePermiso == 1) {
                        if (proyecto.getCronograma().getFechaFin() != null && proyecto.getCronograma().getFechaProrroga() != null) {
                            if (proyecto.getCronograma().getFechaProrroga().before(prorroga.getFecha()) || proyecto.getCronograma().getFechaProrroga().equals(prorroga.getFecha())) {
                                duracionDias = calculo.calculaDuracionEnDias(proyecto.getCronograma().getFechaInicio(), prorroga.getFecha(), 7 - calcularDiasSemanaTrabajo(proyecto));
                                horasTrabajo = duracionDias * calculaHorasTrabajoProrroga(proyecto);
                                if (horasTrabajo <= (var + var1)) {
                                    prorroga.setFechaInicial(proyecto.getCronograma().getFechaProrroga());
                                    prorroga.setCronogramaId(proyecto.getCronograma());
                                    prorroga.setEsActivo(true);
                                    if (prorroga.getEsAceptado()) {
                                        proyecto.getCronograma().setFechaProrroga(prorroga.getFecha());
                                        proyectoFacadeLocal.edit(proyecto);
                                    }
                                    prorrogaFacadeLocal.create(prorroga);
                                    logFacadeLocal.create(logFacadeLocal.crearLog("Prorroga", prorroga.getId() + "", "CREAR", "|Fecha de Prórroga= " + prorroga.getFecha() + "|Motivo= " + prorroga.getMotivo() + "|Cronograma= " + prorroga.getCronogramaId().getId(), usuario));
                                    buscar(proyecto.getCronograma(), usuario, criterio);
                                    if (param.equalsIgnoreCase("grabar")) {
                                        RequestContext.getCurrentInstance().execute("PF('dlgEditarProrroga').hide()");
                                        sessionProrroga.setProrroga(new Prorroga());
                                    } else {
                                        if (param.equalsIgnoreCase("grabar-editar")) {
                                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar") + ".", "");
                                            FacesContext.getCurrentInstance().addMessage(null, message);
                                        }
                                    }
                                } else {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_limit") + ".", "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                }
                            } else {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_limit") + ".", "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                                prorroga.setFecha(null);
                            }
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas") + ".", "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_prorroga");
                if (tienePermiso == 1) {
                    if (proyecto.getCronograma().getFechaFin() != null && proyecto.getCronograma().getFechaProrroga() != null) {
                        if (proyecto.getCronograma().getFechaProrroga().before(prorroga.getFecha()) || proyecto.getCronograma().getFechaProrroga().equals(prorroga.getFecha())) {
                            duracionDias = calculo.calculaDuracionEnDias(proyecto.getCronograma().getFechaInicio(), prorroga.getFecha(), 7 - calcularDiasSemanaTrabajo(proyecto));
                            horasTrabajo = duracionDias * calculaHorasTrabajoProrroga(proyecto);
                            if (horasTrabajo <= (var + var1)) {
                                Prorroga prorrogaUltima = new Prorroga();
                                prorrogaUltima = obtenerUltimaProrroga(prorrogaFacadeLocal.buscarPorProyecto(proyecto.getCronograma().getId()), prorroga);
                                if (prorrogaUltima != null) {
                                    prorroga.setFechaInicial(prorrogaUltima.getFecha());
                                } else {
                                    prorroga.setFechaInicial(proyecto.getCronograma().getFechaFin());
                                }
                                prorroga.setCronogramaId(proyecto.getCronograma());
                                prorroga.setEsActivo(true);
                                if (prorroga.getEsAceptado()) {
                                    proyecto.getCronograma().setFechaProrroga(prorroga.getFecha());
                                    proyectoFacadeLocal.edit(proyecto);
                                } else {
                                    proyecto.getCronograma().setFechaProrroga(prorroga.getFechaInicial());
                                    proyectoFacadeLocal.edit(proyecto);
                                }
                                prorrogaFacadeLocal.edit(prorroga);
                                logFacadeLocal.create(logFacadeLocal.crearLog("Prorroga", prorroga.getId() + "", "CREAR", "|Fecha de Prórroga= " + prorroga.getFecha() + "|Motivo= " + prorroga.getMotivo() + "|Cronograma= " + prorroga.getCronogramaId().getId(), usuario));
                                buscar(proyecto.getCronograma(), usuario, criterio);
                                if (param.equalsIgnoreCase("grabar")) {
                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarProrroga').hide()");
                                    sessionProrroga.setProrroga(new Prorroga());
                                } else {
                                    if (param.equalsIgnoreCase("grabar-editar")) {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar") + ".", "");
                                        FacesContext.getCurrentInstance().addMessage(null, message);
                                    }
                                }
                            } else {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_limit") + ".", "");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_limit") + ".", "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            prorroga.setFecha(null);
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas") + ".", "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }

        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public int buscarIndex(List<Prorroga> prorrogas, Prorroga prorroga) {
        int var = -1;
        for (Prorroga p : prorrogas) {
            var++;
            if (p.getFecha().equals(prorroga.getFecha())) {
                break;
            }

        }
        return var;
    }

    public void remover(Prorroga prorroga, Proyecto proyecto, Usuario usuario) {
        try {
            if (proyecto.getId() != null) {
                proyecto = proyectoFacadeLocal.find(proyecto.getId());
            }
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_prorroga");
                if (tienePermiso == 1) {
                    Prorroga prorrogaUltima = new Prorroga();
                    prorrogaUltima = obtenerUltimaProrroga(prorrogaFacadeLocal.buscarPorProyecto(proyecto.getCronograma().getId()), prorroga);
                    if (prorroga.getId() != null) {
                        prorroga.setEsActivo(false);
                        if (prorrogaUltima == null) {
                            proyecto.getCronograma().setFechaProrroga(proyecto.getCronograma().getFechaFin());
                            proyectoFacadeLocal.edit(proyecto);
                        } else {
                            proyecto.getCronograma().setFechaProrroga(prorrogaUltima.getFechaInicial());
                            proyectoFacadeLocal.edit(proyecto);
                        }
                        prorrogaFacadeLocal.edit(prorroga);
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        buscar(proyecto.getCronograma(), usuario, criterio);
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public boolean compruebaUltimaProrroga(List<Prorroga> prorrogas) {
        boolean var = false;
        if (!prorrogas.isEmpty()) {
            for (Prorroga prorroga : prorrogas) {
                if (prorroga.getEsActivo()) {
                    var = false;
                    break;
                } else {
                    var = true;
                }
            }
        } else {
            var = true;
        }
        return var;
    }

    public Prorroga obtenerUltimaProrroga(List<Prorroga> listProrrogas, Prorroga pr) {
        Prorroga prorroga = null;
        boolean primerVez = false;
        for (Prorroga p : listProrrogas) {
            if (p.getEsActivo()) {
                if (pr.getId() != p.getId()) {
                    if (primerVez == false) {
                        prorroga = p;
                        primerVez = true;
                    }
                    if (p.getFecha().after(prorroga.getFecha())) {
                        prorroga = p;
                    }
                }
            }
        }
        return prorroga;
    }

    public boolean isTieneProrroga() {
        return tieneProrroga;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public void setTieneProrroga(boolean tieneProrroga) {
        this.tieneProrroga = tieneProrroga;
    }

    public boolean isRenderedDlgRespuestaAutorProyecto() {
        return renderedDlgRespuestaAutorProyecto;
    }

    public void setRenderedDlgRespuestaAutorProyecto(boolean renderedDlgRespuestaAutorProyecto) {
        this.renderedDlgRespuestaAutorProyecto = renderedDlgRespuestaAutorProyecto;
    }

    public boolean isRenderedDlgInforme() {
        return renderedDlgInforme;
    }

    public void setRenderedDlgInforme(boolean renderedDlgInforme) {
        this.renderedDlgInforme = renderedDlgInforme;
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

    public SessionProrroga getSessionProrroga() {
        return sessionProrroga;
    }

    public void setSessionProrroga(SessionProrroga sessionProrroga) {
        this.sessionProrroga = sessionProrroga;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<Prorroga> getProrrogas() {
        return prorrogas;
    }

    public void setProrrogas(List<Prorroga> prorrogas) {
        this.prorrogas = prorrogas;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
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

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public boolean isRenderedImprimirOficio() {
        return renderedImprimirOficio;
    }

    public void setRenderedImprimirOficio(boolean renderedImprimirOficio) {
        this.renderedImprimirOficio = renderedImprimirOficio;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public Long getProrrogaId() {
        return prorrogaId;
    }

    public void setProrrogaId(Long prorrogaId) {
        this.prorrogaId = prorrogaId;
    }

    public boolean isRenderedDlgOficio() {
        return renderedDlgOficio;
    }

    public void setRenderedDlgOficio(boolean renderedDlgOficio) {
        this.renderedDlgOficio = renderedDlgOficio;
    }

    public boolean isRenderedAceptarProrroga() {
        return renderedAceptarProrroga;
    }

    public void setRenderedAceptarProrroga(boolean renderedAceptarProrroga) {
        this.renderedAceptarProrroga = renderedAceptarProrroga;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }
//</editor-fold>

}
