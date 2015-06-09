/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.adjudicacion.session.SessionDirectorProyecto;
import edu.unl.sigett.adjudicacion.session.SessionRenunciaDirector;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.Director;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.DocumentoCarrera;
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
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.dao.CoordinadorPeriodoDao;
import edu.unl.sigett.dao.DirectorDao;
import edu.unl.sigett.dao.DirectorProyectoFacadeLocal;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.dao.DocumentoCarreraDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.enumeration.CatalogoDocumentoCarreraEnum;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarDirectoresProyecto implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionDirectorProyecto sessionDirectorProyecto;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionRenunciaDirector sessionRenunciaDirector;


    @EJB
    private DirectorDao directorFacadeLocal;
    @EJB
    private DirectorProyectoFacadeLocal directorProyectoFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private DocumentoCarreraDao oficioCarreraFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteFacadeLocal;
    @EJB
    private CoordinadorPeriodoDao coordinadorPeriodoFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private UsuarioCarreraDao usuarioCarreraFacadeLocal;
    @EJB
    private DocenteCarreraDao docenteCarreraFacadeLocal;

    private List<DirectorProyecto> directorProyectos;
    private List<Director> directoresDisponibles;
    private List<DirectorProyecto> historialDirectores;

    private String criterioDirectorProyecto;
    private String criterioDirector;
    private String criterioHistorialDirectorProyecto;
    private Integer carreraId;
    private DirectorProyecto directorProyecto;
    private Long directorProyectoId;

    private boolean renderedNoEditar;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private boolean renderedEliminar;
    private boolean renderedSeleccionar;
    private boolean renderedBuscarDirector;
    private boolean renderedSortearDirectorProyecto;
    private boolean renderedBuscar;
    private boolean renderedDlgBuscarDirectoresDisponibles;
    private boolean renderedImprimirOficio;
    private boolean renderedDlgOficio;
    private boolean renderedDlgEditar;

    public AdministrarDirectoresProyecto() {
        this.renderedDlgBuscarDirectoresDisponibles = false;
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedBuscarDirectorDisponible(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_director");
//                if (tienePermiso == 1) {
//                    renderedBuscarDirector = true;
//                } else {
//                    renderedBuscarDirector = false;
//                }
//            } else {
//                renderedBuscarDirector = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedImprimirOficio(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_director_proyecto");
            if (tienePermiso == 1) {
                renderedImprimirOficio = true;
            } else {
                renderedImprimirOficio = false;
            }
        } catch (Exception e) {
        }
    }

    public void renderedBuscar(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_director_proyecto");
//                if (tienePermiso == 1) {
//                    renderedBuscar = true;
//                } else {
//                    renderedBuscar = false;
//                }
//            } else {
//                renderedBuscar = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedEliminar(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo()) || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_director_proyecto");
//                if (tienePermiso == 1) {
//                    renderedEliminar = true;
//                } else {
//                    renderedEliminar = false;
//                }
//            } else {
//                renderedEliminar = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedEditar(Usuario usuario) {
        try {
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_director_proyecto");
            if (tienePermiso == 1) {
                renderedEditar = true;
                renderedNoEditar = false;
            } else {
                renderedEditar = false;
                renderedNoEditar = true;
            }
        } catch (Exception e) {
        }
    }

    public void renderedSeleccionar(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "select_director_proyecto");
//                if (tienePermiso == 1 && permiteAgregarDirector(proyecto)) {
//                    renderedSeleccionar = true;
//                } else {
//                    renderedSeleccionar = false;
//                }
//            } else {
//                renderedSeleccionar = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedSortearDirectorProyecto(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sortear_director_proyecto");
//                if (tienePermiso == 1 && permiteAgregarDirector(proyecto)) {
//                    renderedSortearDirectorProyecto = true;
//                } else {
//                    renderedSortearDirectorProyecto = false;
//                }
//            } else {
//                renderedSortearDirectorProyecto = false;
//            }
        } catch (Exception e) {
        }

    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public void viewDirectorProyecto(DirectorProyecto dp) {
        try {
            this.directorProyecto = dp;
            RequestContext.getCurrentInstance().execute("PF('dlgViewDirector').show()");
        } catch (Exception e) {
        }
    }

    public void imprimirOficio(DirectorProyecto directorProyecto, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (directorProyecto.getId() != null) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_director_proyecto");
                if (tienePermiso == 1) {
                    directorProyectoId = directorProyecto.getId();
                    for (ProyectoCarreraOferta pco : directorProyecto.getProyectoId().getProyectoCarreraOfertaList()) {
                        if (pco.getEsActivo()) {
                            carreraId = carreraFacadeLocal.find(pco.getCarreraId()).getId();
                            break;
                        }
                    }
//                    DocumentoCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(directorProyecto.getId(), CatalogoDocumentoCarreraEnum.DIRECTORPROYECTO.getTipo());
//                    if (oficioCarrera != null) {
//                        sessionOficioCarrera.setOficioCarrera(oficioCarrera);
//                    } else {
//                        sessionOficioCarrera.setOficioCarrera(new DocumentoCarrera());
//                    }
                    renderedDlgOficio = true;
                    RequestContext.getCurrentInstance().execute("PF('dlgOficioDirectorProyecto').show()");
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void descargarOficio(Usuario user) {
        
        Map datosReporte = new HashMap();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String path = request.getRealPath("/");
        String pathSetting = request.getRealPath("/settings.txt");
        Carrera carrera = carreraFacadeLocal.find(carreraId);
        String secretario = "";
        DirectorProyecto dp = directorProyectoFacadeLocal.find(directorProyectoId);
        DocenteCarrera docenteCarrera = docenteCarreraFacadeLocal.find(dp.getDirectorId().getId());
        Persona personaDirector = personaFacadeLocal.find(docenteCarrera.getDocenteId().getId());
//        CoordinadorPeriodo coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
//        Persona personaCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
//        ConfiguracionCarrera configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carreraId, "NO");
//        Integer nOficio = Integer.parseInt(configuracionCarrera.getValor());
        secretario = user.getNombres().toUpperCase() + " " + user.getApellidos().toUpperCase();
//        DocumentoCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(dp.getId(), CatalogoDocumentoCarreraEnum.DIRECTORPROYECTO.getTipo());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Calendar fechaActual = Calendar.getInstance();
        String fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());
        datosReporte.put("docente", personaDirector.getNombres().toUpperCase() + " " + personaDirector.getApellidos().toUpperCase());
//        datosReporte.put("coordinador", personaCoordinador.getNombres().toUpperCase() + " " + personaCoordinador.getApellidos().toUpperCase());
        datosReporte.put("temaProyecto", directorProyecto.getProyectoId().getTemaActual());
        datosReporte.put("tituloDocente", docenteCarrera.getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
        datosReporte.put("tituloCoordinador", docenteCarrera.getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion());
        datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(dp.getProyectoId().getId())));
        datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
        datosReporte.put("articulos_adjudicacion_tt", resourceBundle.getString("lbl.articulos_pertinencia_tt"));
        datosReporte.put("asunto_adjudicacion_tt", resourceBundle.getString("lbl.asunto_adjudciacion_tt"));
        datosReporte.put("nota_adjudicacion_tt", resourceBundle.getString("lbl.nota_adjudicacion_tt"));
//        reportes.oficioDirectorProyecto(response, datosReporte, fechaFormateada, configuracionCarreraFacadeLocal, configuracionCarrera, "pdf", oficioCarrera, directorProyectoId, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, nOficio, secretario, path, pathSetting);
    }

    public String editar(DirectorProyecto directorProyecto, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_director_proyecto");
            if (tienePermiso == 1) {
//                directorProyecto.setEsEditado(true);
                sessionDirectorProyecto.setDirectorProyecto(directorProyecto);
                if (param.equalsIgnoreCase("editar-dlg")) {
                    renderedDlgEditar = true;
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarDirectorProyecto').show()");
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }

        return navegacion;
    }

    public boolean permiteAgregarDirector(Proyecto proyecto) {
        boolean var = false;
//        String val = configuracionGeneralFacadeLocal.find((int) 20).getValor();
//        if (!proyecto.getDirectorProyectoList().isEmpty()) {
//            for (DirectorProyecto dp : proyecto.getDirectorProyectoList()) {
//                if (dp.getEstadoDirectorId().getId() == 1) {
//                    if (val.equalsIgnoreCase("SI")) {
//                        var = true;
//                    } else {
//                        var = false;
//                        break;
//                    }
//                } else {
//                    var = true;
//                }
//            }
//        } else {
//            var = true;
//        }
        return var;
    }

    public void agregarDirectorProyecto(Director director, Usuario usuario, Proyecto proyecto) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "select_director_proyecto");
//                Calendar fecha = Calendar.getInstance();
//                if (tienePermiso == 1) {
//                    if (proyecto.getCronograma().getFechaInicio() != null && proyecto.getCronograma().getFechaFin() != null) {
//                        DirectorProyecto dp = new DirectorProyecto();
//                        dp.setDirectorId(director);
//                        dp.setProyectoId(proyecto);
//                        dp.setFechaInicio(fecha.getTime());
//                        dp.setFechaCulminacion(proyecto.getCronograma().getFechaFin());
//                        EstadoDirector estadoDirector = estadoDirectorFacadeLocal.find((int) 1);
//                        if (estadoDirector != null) {
//                            dp.setEstadoDirectorId(estadoDirector);
//                        } else {
//                            estadoDirector = new EstadoDirector();
//                            estadoDirector.setNombre("ASIGNADO");
//                            estadoDirector.setDescripcion("ASIGNADO");
//                            estadoDirectorFacadeLocal.create(estadoDirector);
//                            dp.setEstadoDirectorId(estadoDirector);
//                        }
//                        DirectorProyecto directoProy = contieneDirectorProyecto(proyecto, dp);
//                        if (directoProy == null) {
//                            if (permiteAgregarDirector(proyecto)) {
//                                proyecto.getDirectorProyectoList().add(dp);
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                                buscar("", usuario, sessionProyecto.getProyecto());
//                                renderedBuscarDirectorDisponible(usuario, proyecto);
//                                renderedSortearDirectorProyecto(usuario, proyecto);
//                                renderedSeleccionar(usuario, proyecto);
//                            } else {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        } else {
//                            directoProy.setDirectorId(director);
//                            directoProy.setEsEditado(true);
//                            estadoDirector = estadoDirectorFacadeLocal.find((int) 1);
//                            if (estadoDirector != null) {
//                                directoProy.setEstadoDirectorId(estadoDirector);
//                            }
//                            if (directoProy.getFechaInicio() == null) {
//                                directoProy.setFechaInicio(proyecto.getCronograma().getFechaInicio());
//                            }
//                            directoProy.setFechaCulminacion(proyecto.getCronograma().getFechaFin());
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                            buscar("", usuario, proyecto);
//                            renderedBuscarDirectorDisponible(usuario, proyecto);
//                            renderedSortearDirectorProyecto(usuario, proyecto);
//                            renderedSeleccionar(usuario, proyecto);
//                        }
//                        RequestContext.getCurrentInstance().execute("PF('dlgBuscarDirectoresDisponibles').hide()");
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void removerDirectorProyecto(DirectorProyecto directorProyecto, Usuario usuario, Proyecto proyecto) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_director_proyecto");
//            if (tienePermiso == 1) {
//                if (directorProyecto.getId() != null) {
//                    directorProyecto.setEsEditado(true);
//                    if (!proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
//                        EstadoDirector estadoDirector = estadoDirectorFacadeLocal.find((int) 2);
//                        if (estadoDirector != null) {
//                            directorProyecto.setEstadoDirectorId(estadoDirector);
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                            buscar("", usuario, proyecto);
//                            renderedBuscarDirectorDisponible(usuario, proyecto);
//                            renderedSortearDirectorProyecto(usuario, proyecto);
//                            renderedSeleccionar(usuario, proyecto);
//                        }
//                    } else {
//                        if (!proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SEGUIMIENTO.getTipo())) {
//                            DirectorProyecto dp = directorProyectoFacadeLocal.find(directorProyecto.getId());
//                            if (dp.getEstadoDirectorId().getId() != 2) {
//                                int tienePermiso1 = usuarioFacadeLocal.tienePermiso(usuario, "crear_renuncia_director");
//                                if (tienePermiso1 == 1) {
//                                    sessionDirectorProyecto.setDirectorProyecto(directorProyecto);
//                                    sessionRenunciaDirector.setRenunciaDirector(new RenunciaDirector());
//                                    RequestContext.getCurrentInstance().execute("PF('dlgEditarRenunciaDirectorProyecto').show()");
//                                } else {
//                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                                    FacesContext.getCurrentInstance().addMessage(null, message);
//                                }
//                            }
//                        } else {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    }
//                } else {
//                    proyecto.getDirectorProyectoList().remove(directorProyecto);
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                    buscar("", usuario, proyecto);
//                    renderedBuscarDirectorDisponible(usuario, proyecto);
//                    renderedSortearDirectorProyecto(usuario, proyecto);
//                    renderedSeleccionar(usuario, proyecto);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public DirectorProyecto contieneDirectorProyecto(Proyecto proyecto, DirectorProyecto directorProyecto) {
        DirectorProyecto dp = null;
        try {
            for (DirectorProyecto director : proyecto.getDirectorProyectoList()) {
                if (director.getDirectorId().equals(directorProyecto.getDirectorId())) {
                    dp = director;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return dp;
    }

    public String dialogoBuscarDirectoresDisponibles(List<LineaInvestigacion> lineaInvestigacions, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            listadoDirectoresAptos("", lineaInvestigacions, usuario);
            if (param.equalsIgnoreCase("buscar-directores-dlg")) {
                renderedDlgBuscarDirectoresDisponibles = true;
                RequestContext.getCurrentInstance().execute("PF('dlgBuscarDirectoresDisponibles').show()");
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void sortearDirector(List<LineaInvestigacion> lineaInvestigacions, Usuario usuario, Proyecto proyecto) {
        Random random = new Random();
        try {
            List<Director> directores = new ArrayList<>();
            directores.addAll(listadoDirectoresSorteo(lineaInvestigacions));
            int numeroProbabilidades = directores.size();
            int pos = random.nextInt(numeroProbabilidades);
            Director director = directores.get(pos);
            agregarDirectorProyecto(director, usuario, proyecto);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Director> listadoDirectoresSorteo(List<LineaInvestigacion> lineaInvestigacions) {
        List<Director> directors = new ArrayList<>();
        try {
//            for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                for (Director director : directorFacadeLocal.buscarAptos(usuarioCarrera.getCarreraId())) {
//                    DocenteCarrera dc = docenteCarreraFacadeLocal.find(director.getId());
//                    for (LineaInvestigacion li : lineaInvestigacions) {
////                        for (LineaInvestigacionDocente lid : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(dc.getDocenteId().getId())) {
////                            if (lid.getLineaInvestigacionId().equals(li)) {
////                                directors.add(director);
////                            }
////                        }
//                    }
//                }
//            }

        } catch (Exception e) {
        }
        return directors;
    }

    public void listadoDirectoresAptos(String criterio, List<LineaInvestigacion> lineaInvestigacions, Usuario usuario) {
//        this.directoresDisponibles = new ArrayList<>();
//        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_director");
//            if (tienePermiso == 1) {
//                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
//                    for (Director director : directorFacadeLocal.buscarAptos(usuarioCarrera.getCarreraId())) {
//                        DocenteCarrera dc = docenteCarreraFacadeLocal.find(director.getId());
//                        Persona p = personaFacadeLocal.find(dc.getDocenteId().getId());
//                        for (LineaInvestigacion li : lineaInvestigacions) {
////                            for (LineaInvestigacionDocente lid : lineaInvestigacionDocenteFacadeLocal.buscarPorDocenteId(dc.getDocenteId().getId())) {
////                                if (lid.getLineaInvestigacionId().equals(li)) {
////                                    if (p.getApellidos().toUpperCase().contains(criterio.toUpperCase()) || p.getNombres().toUpperCase().contains(criterio.toUpperCase())
////                                            || p.getNumeroIdentificacion().contains(criterio)) {
////                                        if (!directoresDisponibles.contains(director)) {
////                                            directoresDisponibles.add(director);
////                                        }
////                                    }
////                                }
////                            }
//                        }
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void buscar(String criterio, Usuario usuario, Proyecto proyecto) {
        this.directorProyectos = new ArrayList<>();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_director_proyecto");
            if (tienePermiso == 1) {
                for (DirectorProyecto dp : proyecto.getDirectorProyectoList()) {
                    DocenteCarrera dc = docenteCarreraFacadeLocal.find(dp.getDirectorId().getId());
                    Persona p = personaFacadeLocal.find(dc.getDocenteId().getId());
                    if (p.getApellidos().toUpperCase().contains(criterio.toUpperCase())
                            || p.getNombres().toUpperCase().contains(criterio.toUpperCase())
                            || p.getNumeroIdentificacion().contains(criterio)) {
                        if (!directorProyectos.contains(dp)) {
                            directorProyectos.add(dp);
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

    public void historialDirectoresProyecto(String criterio, Proyecto proyecto) {
        this.historialDirectores = new ArrayList<>();
        try {
            for (DirectorProyecto dp : proyecto.getDirectorProyectoList()) {
                DocenteCarrera dc = docenteCarreraFacadeLocal.find(dp.getDirectorId().getId());
                Persona p = personaFacadeLocal.find(dc.getDocenteId().getId());
                if (p.getApellidos().toUpperCase().contains(criterio.toUpperCase())
                        || p.getNombres().toUpperCase().contains(criterio.toUpperCase())
                        || p.getNumeroIdentificacion().contains(criterio)) {
                    historialDirectores.add(dp);
                }
            }

        } catch (Exception e) {
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public DirectorProyecto getDirectorProyecto() {
        return directorProyecto;
    }

    public void setDirectorProyecto(DirectorProyecto directorProyecto) {
        this.directorProyecto = directorProyecto;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionDirectorProyecto getSessionDirectorProyecto() {
        return sessionDirectorProyecto;
    }

    public void setSessionDirectorProyecto(SessionDirectorProyecto sessionDirectorProyecto) {
        this.sessionDirectorProyecto = sessionDirectorProyecto;
    }

    public SessionProyecto getSessionProyecto() {
        return sessionProyecto;
    }

    public void setSessionProyecto(SessionProyecto sessionProyecto) {
        this.sessionProyecto = sessionProyecto;
    }

    public String getCriterioDirectorProyecto() {
        return criterioDirectorProyecto;
    }

    public void setCriterioDirectorProyecto(String criterioDirectorProyecto) {
        this.criterioDirectorProyecto = criterioDirectorProyecto;
    }

    public String getCriterioDirector() {
        return criterioDirector;
    }

    public void setCriterioDirector(String criterioDirector) {
        this.criterioDirector = criterioDirector;
    }

    public SessionRenunciaDirector getSessionRenunciaDirector() {
        return sessionRenunciaDirector;
    }

    public void setSessionRenunciaDirector(SessionRenunciaDirector sessionRenunciaDirector) {
        this.sessionRenunciaDirector = sessionRenunciaDirector;
    }

    public List<DirectorProyecto> getDirectorProyectos() {
        return directorProyectos;
    }

    public void setDirectorProyectos(List<DirectorProyecto> directorProyectos) {
        this.directorProyectos = directorProyectos;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public List<Director> getDirectoresDisponibles() {
        return directoresDisponibles;
    }

    public void setDirectoresDisponibles(List<Director> directoresDisponibles) {
        this.directoresDisponibles = directoresDisponibles;
    }

    public List<DirectorProyecto> getHistorialDirectores() {
        return historialDirectores;
    }

    public void setHistorialDirectores(List<DirectorProyecto> historialDirectores) {
        this.historialDirectores = historialDirectores;
    }

    public String getCriterioHistorialDirectorProyecto() {
        return criterioHistorialDirectorProyecto;
    }

    public void setCriterioHistorialDirectorProyecto(String criterioHistorialDirectorProyecto) {
        this.criterioHistorialDirectorProyecto = criterioHistorialDirectorProyecto;
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

    public boolean isRenderedSeleccionar() {
        return renderedSeleccionar;
    }

    public void setRenderedSeleccionar(boolean renderedSeleccionar) {
        this.renderedSeleccionar = renderedSeleccionar;
    }

    public boolean isRenderedBuscarDirector() {
        return renderedBuscarDirector;
    }

    public void setRenderedBuscarDirector(boolean renderedBuscarDirector) {
        this.renderedBuscarDirector = renderedBuscarDirector;
    }

    public boolean isRenderedSortearDirectorProyecto() {
        return renderedSortearDirectorProyecto;
    }

    public void setRenderedSortearDirectorProyecto(boolean renderedSortearDirectorProyecto) {
        this.renderedSortearDirectorProyecto = renderedSortearDirectorProyecto;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public boolean isRenderedDlgBuscarDirectoresDisponibles() {
        return renderedDlgBuscarDirectoresDisponibles;
    }

    public void setRenderedDlgBuscarDirectoresDisponibles(boolean renderedDlgBuscarDirectoresDisponibles) {
        this.renderedDlgBuscarDirectoresDisponibles = renderedDlgBuscarDirectoresDisponibles;
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

    public Long getDirectorProyectoId() {
        return directorProyectoId;
    }

    public void setDirectorProyectoId(Long directorProyectoId) {
        this.directorProyectoId = directorProyectoId;
    }

    public boolean isRenderedDlgOficio() {
        return renderedDlgOficio;
    }

    public void setRenderedDlgOficio(boolean renderedDlgOficio) {
        this.renderedDlgOficio = renderedDlgOficio;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }
//</editor-fold>

}
