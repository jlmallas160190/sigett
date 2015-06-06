/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.pertinencia;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.Pertinencia;
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
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.dao.CoordinadorPeriodoDao;
import org.jlmallas.seguridad.dao.LogDao;
import edu.unl.sigett.dao.DocumentoCarreraDao;
import edu.unl.sigett.dao.PertinenciaDao;
import edu.unl.sigett.dao.ProyectoDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarPertinencias implements Serializable {

    @Inject
    private PertinenciaDM sessionPertinencia;

    @EJB
    private LogDao logDao;
    @EJB
    private PertinenciaDao pertinenciaFacadeLocal;
    private List<Pertinencia> pertinenciasGrabar;
    private List<Pertinencia> pertinenciasRemover;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private ProyectoDao proyectoFacadeLocal;
    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    UsuarioDao usuarioFacadeLocal;
    @EJB
    private CoordinadorPeriodoDao coordinadorPeriodoFacadeLocal;

    private List<Pertinencia> pertinencias;
    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedEliminar;
    private boolean renderedBuscar;
    private boolean renderedAceptar;
    private boolean renderedDlgEditar;
    private boolean renderedDlgInforme;
    private boolean renderedImprimirInforme;
    private Integer carreraId;
    private Long pertinenciaId;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private DocumentoCarreraDao oficioCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private DocenteDao docenteFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;

    public AdministrarPertinencias() {
        this.renderedDlgEditar = false;
        this.renderedDlgInforme = false;

    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedCrear(Usuario usuario, DocenteProyecto docenteProyecto) {
//        if (docenteProyecto.getProyectoId().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
//                || docenteProyecto.getProyectoId().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_pertinencia");
//            if (tienePermiso == 1) {
//                renderedCrear = true;
//            } else {
//                renderedCrear = false;
//            }
//        } else {
//            renderedCrear = false;
//        }
    }

    public void renderedEditar(Usuario usuario, DocenteProyecto docenteProyecto) {
//        if (docenteProyecto.getProyectoId().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
//                || docenteProyecto.getProyectoId().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_pertinencia");
//            if (tienePermiso == 1) {
//                renderedEditar = true;
//                renderedNoEditar = false;
//            } else {
//                renderedEditar = false;
//                renderedNoEditar = true;
//            }
//        } else {
//            renderedEditar = false;
//            renderedNoEditar = true;
//        }
    }

    public void renderedAceptar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "aceptar_pertinencia");
        try {
            if (tienePermiso == 1) {
                renderedAceptar = true;
            } else {
                renderedAceptar = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_pertinencia");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedEliminar(Usuario usuario, DocenteProyecto docenteProyecto) {
//        if (docenteProyecto.getProyectoId().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
//                || docenteProyecto.getProyectoId().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_pertinencia");
//            if (tienePermiso == 1) {
//                renderedEliminar = true;
//            } else {
//                renderedEliminar = false;
//            }
//        } else {
//            renderedEliminar = false;
//        }
    }

    public void renderedImprimirInforme(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_pertinencia");
            if (tienePermiso == 1) {
                renderedImprimirInforme = true;
            } else {
                renderedImprimirInforme = false;
            }
        } catch (Exception e) {
        }
    }

    public boolean renderedSubirDocumento(Pertinencia pertinencia) {
        boolean var = false;
        if (pertinencia.getId() != null) {
            var = true;
        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public void imprimirOficio(Pertinencia pertinencia, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (pertinencia.getId() != null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_pertinencia");
                if (tienePermiso == 1) {
                    pertinenciaId = pertinencia.getId();
                    for (ProyectoCarreraOferta pco : pertinencia.getDocenteProyectoId().getProyectoId().getProyectoCarreraOfertaList()) {
                        if (pco.getEsActivo()) {
                            carreraId = pco.getCarreraId();
                            break;
                        }
                    }
//                    DocumentoCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(pertinencia.getId(), CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo());
//                    if (oficioCarrera != null) {
//                        sessionOficioCarrera.setOficioCarrera(oficioCarrera);
//                    } else {
//                        sessionOficioCarrera.setOficioCarrera(new DocumentoCarrera());
//                    }
                    renderedDlgInforme = true;
                    RequestContext.getCurrentInstance().execute("PF('dlgInformeDocenteProyecto').show()");
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

    public void buscar(Usuario usuario, DocenteProyecto docenteProyecto) {
        this.pertinencias = new ArrayList<>();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        int tienePermiso = 0;
        try {
            if (usuario.getId() != null) {
                tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_pertinencia");
            }
            if (tienePermiso == 1) {
//                pertinencias = pertinenciaFacadeLocal.buscarPertinenciasPorDocenteProyecto(docenteProyecto.getId());
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }

    }

    public void crear(Usuario usuario, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_pertinencia");
//                if (tienePermiso == 1) {
//                    sessionPertinencia.setPertinencia(new Pertinencia());
//                    sessionPertinencia.getPertinencia().setObservacion("Ninguna");
//                    renderedDlgEditar = true;
//                    RequestContext.getCurrentInstance().execute("PF('dlgEditarPertinencia').show()");
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public void editar(Usuario usuario, Pertinencia pertinencia, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_pertinencia");
//                if (tienePermiso == 1) {
//                    sessionPertinencia.setPertinencia(pertinencia);
//                    renderedDlgEditar = true;
//                    RequestContext.getCurrentInstance().execute("PF('dlgEditarPertinencia').show()");
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public void downloadInformeDocx(Usuario user) {
//        AdministrarReportes reportes = new AdministrarReportes();
        Map datosReporte = new HashMap();
        Carrera carrera = carreraFacadeLocal.find(carreraId);
//        ConfiguracionCarrera configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carreraId, "NO");
        Pertinencia pertinencia = pertinenciaFacadeLocal.find(pertinenciaId);
        Docente docente = docenteFacadeLocal.find(pertinencia.getDocenteProyectoId().getDocenteCarreraId());
        Persona datosDocente = personaFacadeLocal.find(docente.getId());
//        DocumentoCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(pertinencia.getId(), CatalogoDocumentoCarreraEnum.PERTINENCIA.getTipo());
        Calendar fechaActual = Calendar.getInstance();
        String fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());
//        String fechOficioFormat = configuracionGeneralFacadeLocal.dateFormat(oficioCarrera.getFecha());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        CoordinadorPeriodo coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
//        Persona datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
//        Docente docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
        String resolucion = "";
        if (pertinencia.getEsAceptado()) {
            resolucion = "Por consiguiente <b>SE OTORGA LA PERTINENCIA</b>  para el desarrollo de este proyecto.";
        } else {
            resolucion = "Por consiguiente <b> NO SE OTORGA LA PERTINENCIA</b>  para el desarrollo de este proyecto.";
        }
        datosReporte.put("docente", datosDocente.getNombres().toUpperCase() + " " + datosDocente.getApellidos().toUpperCase());
//        datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
        datosReporte.put("temaProyecto", pertinencia.getDocenteProyectoId().getProyectoId().getTemaActual());
        datosReporte.put("tituloDocente", docente.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
//        datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
        datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(pertinencia.getDocenteProyectoId().getProyectoId().getId())));

//        reportes.informeDocenteProyecto("docx", fechaFormateada, fechOficioFormat, pertinencia, response, datosReporte, configuracionCarreraFacadeLocal,
//                configuracionCarrera, oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, Integer.parseInt(oficioCarrera.getNumeroOficio()), request.getRealPath("/"), resolucion);
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

    public void cambiarEstadoAutoresProyecto(Proyecto proyecto) {
//        EstadoAutor estadoAutor = null;
        try {
//            for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
//                if (autorProyecto.getId() != null) {
//                    if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                        estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.INICIO.getTipo());
//                    } else {
//                        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                            estadoAutor = estadoAutorFacadeLocal.buscarPorCodigo(EstadoAutorEnum.PERTINENTE.getTipo());
//                        }
//                    }
//                    autorProyecto.setEstadoAutorId(estadoAutor);
//                    autorProyectoFacadeLocal.edit(autorProyecto);
//                }
//            }
        } catch (Exception e) {
        }
    }

    public String grabar(Usuario usuario, Pertinencia pertinencia, DocenteProyecto docenteProyecto) {
        String navegacion = "";
        try {
//            Proyecto p = proyectoFacadeLocal.find(docenteProyecto.getProyectoId().getId());
//            EstadoProyecto estadoProyecto = null;
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
//            if (p.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
//                    || p.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                if (pertinencia.getId() == null) {
//                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_pertinencia");
//                    if (tienePermiso == 1) {
//                        pertinencia.setDocenteProyectoId(docenteProyecto);
//                        pertinenciaFacadeLocal.create(pertinencia);
//                        logDao.create(logDao.crearLog("Pertinencia", pertinencia.getId() + "", "CREAR", "|Docente= " + pertinencia.getDocenteProyectoId().getDocenteId()
//                                + "|Proyecto= " + pertinencia.getDocenteProyectoId().getProyectoId().getId() + "|Fecha= " + pertinencia.getFecha() + "|EsActivo= " + pertinencia.getEsActivo(), usuario));
//                        if (pertinencia.getEsAceptado()) {
//                            estadoProyecto = estadoProyectoFacadeLocal.find(2);
//                        } else {
//                            estadoProyecto = estadoProyectoFacadeLocal.find(1);
//                        }
//                        docenteProyecto.getProyectoId().setEstadoProyectoId(estadoProyecto);
//                        cambiarEstadoAutoresProyecto(docenteProyecto.getProyectoId());
//                        proyectoFacadeLocal.edit(docenteProyecto.getProyectoId());
//                        if (param.equalsIgnoreCase("grabar-dlg")) {
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarPertinencia').hide()");
//                            sessionPertinencia.setPertinencia(new Pertinencia());
//                        } else {
//                            if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        }
//                    } else {
//                        if (tienePermiso == 2) {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    }
//                    buscar(usuario, docenteProyecto);
//                    renderedEliminar(usuario, docenteProyecto);
//                    renderedCrear(usuario, docenteProyecto);
//                    renderedEditar(usuario, docenteProyecto);
//                } else {
//                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_pertinencia");
//                    if (tienePermiso == 1) {
//                        pertinenciaFacadeLocal.edit(pertinencia);
//                        logDao.create(logDao.crearLog("Pertinencia", pertinencia.getId() + "", "EDITAR", "|Docente= " + pertinencia.getDocenteProyectoId().getDocenteId() + "|Proyecto= "
//                                + pertinencia.getDocenteProyectoId().getProyectoId().getId() + "|Fecha= " + pertinencia.getFecha() + "|EsActivo= " + pertinencia.getEsActivo(), usuario));
//                        if (pertinencia.getEsAceptado()) {
//                            estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.PERTINENTE.getTipo());
//                        } else {
//                            estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.INICIO.getTipo());;
//                        }
//                        docenteProyecto.getProyectoId().setEstadoProyectoId(estadoProyecto);
//                        proyectoFacadeLocal.edit(sessionDocenteProyecto.getDocenteProyecto().getProyectoId());
//                        cambiarEstadoAutoresProyecto(sessionDocenteProyecto.getDocenteProyecto().getProyectoId());
//
//                        if (param.equalsIgnoreCase("grabar-dlg")) {
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarPertinencia').hide()");
//                            sessionPertinencia.setPertinencia(new Pertinencia());
//                        } else {
//                            if (param.equalsIgnoreCase("grabar-editar-dlg")) {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        }
//                        buscar(usuario, docenteProyecto);
//                        renderedEliminar(usuario, docenteProyecto);
//                        renderedCrear(usuario, docenteProyecto);
//                        renderedEditar(usuario, docenteProyecto);
//                    } else {
//                        if (tienePermiso == 2) {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return navegacion;
    }

    public void remover(Usuario usuario, Pertinencia pertinencia, DocenteProyecto docenteProyecto) {
        try {
//            Proyecto p = proyectoFacadeLocal.find(docenteProyecto.getProyectoId().getId());
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (p.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())
//                    || p.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_pertinencia");
//                if (tienePermiso == 1) {
//                    pertinencia.setEsActivo(false);
//                    pertinenciaFacadeLocal.edit(pertinencia);
//                    logDao.create(logDao.crearLog("Pertinencia", pertinencia.getId() + "", "EDITAR", "|Docente= " + pertinencia.getDocenteProyectoId().getDocenteId()
//                            + "|Proyecto= " + pertinencia.getDocenteProyectoId().getProyectoId().getId() + "|Fecha= " + pertinencia.getFecha() + "|EsActivo= " + pertinencia.getEsActivo(), usuario));
//                    if (!existePertinencias(docenteProyecto)) {
//                        EstadoProyecto estadoProyecto = estadoProyectoFacadeLocal.buscarPorCodigo(EstadoProyectoEnum.INICIO.getTipo());
//                        if (estadoProyecto != null) {
//                            sessionDocenteProyecto.getDocenteProyecto().getProyectoId().setEstadoProyectoId(estadoProyecto);
//                            proyectoFacadeLocal.edit(sessionDocenteProyecto.getDocenteProyecto().getProyectoId());
//                        }
//                    }
//                    buscar(usuario, docenteProyecto);
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public boolean existePertinencias(DocenteProyecto docenteProyecto) {
        boolean var = false;
//        for (Pertinencia pertinencia : pertinenciaFacadeLocal.buscarPertinenciasPorDocenteProyecto(docenteProyecto.getId())) {
//            var = true;
//            break;
//        }
        return var;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public PertinenciaDM getSessionPertinencia() {
        return sessionPertinencia;
    }

    public void setSessionPertinencia(PertinenciaDM sessionPertinencia) {
        this.sessionPertinencia = sessionPertinencia;
    }

    public List<Pertinencia> getPertinenciasGrabar() {
        return pertinenciasGrabar;
    }

    public void setPertinenciasGrabar(List<Pertinencia> pertinenciasGrabar) {
        this.pertinenciasGrabar = pertinenciasGrabar;
    }

    public List<Pertinencia> getPertinenciasRemover() {
        return pertinenciasRemover;
    }

    public void setPertinenciasRemover(List<Pertinencia> pertinenciasRemover) {
        this.pertinenciasRemover = pertinenciasRemover;
    }

    public List<Pertinencia> getPertinencias() {
        return pertinencias;
    }

    public void setPertinencias(List<Pertinencia> pertinencias) {
        this.pertinencias = pertinencias;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
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

    public boolean isRenderedAceptar() {
        return renderedAceptar;
    }

    public void setRenderedAceptar(boolean renderedAceptar) {
        this.renderedAceptar = renderedAceptar;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }

    public boolean isRenderedDlgInforme() {
        return renderedDlgInforme;
    }

    public void setRenderedDlgInforme(boolean renderedDlgInforme) {
        this.renderedDlgInforme = renderedDlgInforme;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public boolean isRenderedImprimirInforme() {
        return renderedImprimirInforme;
    }

    public void setRenderedImprimirInforme(boolean renderedImprimirInforme) {
        this.renderedImprimirInforme = renderedImprimirInforme;
    }

    public Long getPertinenciaId() {
        return pertinenciaId;
    }

    public void setPertinenciaId(Long pertinenciaId) {
        this.pertinenciaId = pertinenciaId;
    }
//</editor-fold>

}
