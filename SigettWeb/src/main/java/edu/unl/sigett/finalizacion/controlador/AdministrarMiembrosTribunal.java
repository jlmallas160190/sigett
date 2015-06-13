/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.finalizacion.managed.session.SessionMiembro;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.CargoMiembro;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.Miembro;
import edu.unl.sigett.entity.DocumentoCarrera;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.entity.Tribunal;
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
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.unl.sigett.dao.CargoMiembroFacadeLocal;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.dao.CoordinadorPeriodoDao;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.unl.sigett.dao.MiembroFacadeLocal;
import edu.unl.sigett.dao.DocumentoCarreraDao;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.dao.ProyectoDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.enumeration.CatalogoDocumentoCarreraEnum;
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
public class AdministrarMiembrosTribunal implements Serializable {

    @Inject
    private SessionMiembro sessionMiembro;
    @Inject
    private AdministrarEvaluacionesTribunal administrarEvaluacionesTribunal;
    @Inject
    private SessionProyecto sessionProyecto;

    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private DocenteCarreraDao docenteCarreraFacadeLocal;
    @EJB
    private ProyectoOfertaCarreraDao proyectoCarreraOfertaFacadeLocal;
    @EJB
    private CargoMiembroFacadeLocal cargoMiembroFacadeLocal;
    @EJB
    private DocenteDao docenteFacadeLocal;
    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private DocumentoCarreraDao oficioCarreraFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private MiembroFacadeLocal miembroFacadeLocal;
    @EJB
    private ProyectoDao proyectoFacadeLocal;
    @EJB
    private CoordinadorPeriodoDao coordinadorPeriodoFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;

    private boolean renderedEditar;
    private boolean renderedNoEditar;
    private boolean renderedEliminar;
    private boolean renderedCrear;
    private boolean renderedBuscar;
    private boolean renderedDlgEditar;
    private boolean renderedDlgDocentesDisponibles;
    private boolean renderedImprimirOficioSustentacionPrivada;
    private boolean renderedImprimirOficioSustentacionPublica;
    private boolean renderedDlgOficio;

    private List<Miembro> miembros;
    private List<Docente> docentes;
    private List<Miembro> consultaMiembros;

    private String cargo;
    private String criterioDocente;
    private String criterio;
    private Integer carreraId;
    private Long miembroId;

    public AdministrarMiembrosTribunal() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public void imprimirOficioMiembroTribunal(Miembro miembro, Tribunal tribunal, Proyecto proyecto, Usuario usuario) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (miembro.getId() != null) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_miembro_tribunal");
//                if (tienePermiso == 1) {
//                    miembroId = miembro.getId();
//                    for (ProyectoCarreraOferta pco : proyecto.getProyectoCarreraOfertaList()) {
//                        if (pco.getEsActivo()) {
//                            carreraId = pco.getCarreraId();
//                            break;
//                        }
//                    }
//                    if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                            || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())) {
//                        renderedDlgOficio = true;
//                        RequestContext.getCurrentInstance().execute("PF('dlgOficioMiembroTribunalSprivada').show()");
//                    } else {
//                        if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                                || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                            if (administrarEvaluacionesTribunal.existeSustentacionPublica(tribunal)) {
//                                renderedDlgOficio = true;
//                                RequestContext.getCurrentInstance().execute("PF('dlgOficioMiembroTribunalSpublica').show()");
//                            } else {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_existe_sp") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        }
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_imprimir") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
    }

    public void descargarOficioMiembroTribunalPublica(Usuario user) {
        try {
            Map datosReporte = new HashMap();
//            AdministrarReportes reportes = new AdministrarReportes();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            String path = request.getRealPath("/");
            String pathSetting = request.getRealPath("/settings.txt");
            Carrera carrera = carreraFacadeLocal.find(carreraId);
            String secretario = "";
            int contador = 0;
            Miembro miembro = miembroFacadeLocal.find(miembroId);
            String presidente = "";
            String miembros = "";
//            ConfiguracionCarrera configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carreraId, "NO");
//            int nOficio = Integer.parseInt(configuracionCarrera.getValor());
            Docente docenteMiembroPublica = docenteFacadeLocal.find(miembro.getDocenteId());
            Persona datosMiembroPublica = personaFacadeLocal.find(docenteMiembroPublica.getId());

            for (Miembro m : miembroFacadeLocal.buscarPorTribunal(miembro.getTribunalId().getId())) {
                Docente docente = docenteFacadeLocal.find(m.getDocenteId());
                Persona personaDocente = personaFacadeLocal.find(docente.getId());
                if (m.getCargoId().getId() == 1) {
                    presidente += docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaDocente.getNombres() + " "
                            + personaDocente.getApellidos() + " ";
                } else {
                    if (contador == 0) {
                        miembros += "" + docente.getTituloDocenteId().getTituloId().getAbreviacion() + " "
                                + personaDocente.getNombres() + " " + personaDocente.getApellidos() + " ";
                        contador++;
                    } else {
                        miembros += ", " + docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaDocente.getNombres() + " "
                                + personaDocente.getApellidos();;
                        contador++;
                    }
                }
            }
            String lugarSustentacion = "";
            String fechaSustentacion = "";
            for (EvaluacionTribunal evaluacionTribunal : miembro.getTribunalId().getEvaluacionTribunalList()) {
                if (evaluacionTribunal.getCatalogoEvaluacionId().getId() == 2) {
                    lugarSustentacion = evaluacionTribunal.getLugar();
                    int tiempo = evaluacionTribunal.getFechaFin().getHours() - evaluacionTribunal.getFechaInicio().getHours();
                    fechaSustentacion = configuracionGeneralFacadeLocal.dateFormat(evaluacionTribunal.getFechaInicio()) + " a partir de " + configuracionGeneralFacadeLocal.timeFormat(evaluacionTribunal.getFechaInicio()) + ""
                            + " a efecto para que se cumpla por un espacio  máximo de " + tiempo + " hora(s) con la sustentación pública.";
                }
            }
//            CoordinadorPeriodo coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
//            Persona datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
//            Docente docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
            secretario = user.getNombres().toUpperCase() + " " + user.getApellidos().toUpperCase();
//            DocumentoCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(miembroId, CatalogoDocumentoCarreraEnum.MIEMBROTRIBUNALPUBLICA.getTipo());
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            Calendar fechaActual = Calendar.getInstance();
            String fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());

            datosReporte.put("miembro", datosMiembroPublica.getNombres().toUpperCase() + " " + datosMiembroPublica.getApellidos().toUpperCase());
//            datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
            datosReporte.put("temaProyecto", miembro.getTribunalId().getProyectoId().getTemaActual());
            datosReporte.put("cargoMiembro", miembro.getCargoId().getNombre().toUpperCase());
            datosReporte.put("tituloMiembro", docenteMiembroPublica.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
//            datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
//            datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(miembro.getTribunalId().getProyectoId().getId())));
            datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
            datosReporte.put("articulos_sustentacion_publica", resourceBundle.getString("lbl.articulos_sustentacion_publica"));

//            reportes.oficioMiembroTribunalSpublica("pdf", fechaFormateada, fechaSustentacion, lugarSustentacion, response, datosReporte,
//                    configuracionCarreraFacadeLocal, configuracionCarrera, oficioCarrera, presidente, miembros, miembroId, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera,
//                    nOficio, secretario, path, pathSetting);
        } catch (Exception e) {
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

    public void descargarOficioMiembroTribunalPrivada(Usuario user) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            Map datosReporte = new HashMap();
//            AdministrarReportes reportes = new AdministrarReportes();
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            String path = request.getRealPath("/");
            String pathSetting = request.getRealPath("/settings.txt");
            Carrera carrera = carreraFacadeLocal.find(carreraId);
            String secretario = "";
            int contador = 0;
            Miembro miembro = miembroFacadeLocal.find(miembroId);
            String presidente = "";
            String miembros = "";
            secretario = "";
//            ConfiguracionCarrera configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carreraId, "NO");
//            int nOficio = Integer.parseInt(configuracionCarrera.getValor());
            Docente docenteMiembro = docenteFacadeLocal.find(miembro.getDocenteId());
            Persona datosMiembro = personaFacadeLocal.find(docenteMiembro.getId());
            for (Miembro m : miembroFacadeLocal.buscarPorTribunal(miembro.getTribunalId().getId())) {
                Docente docente = docenteFacadeLocal.find(m.getDocenteId());
                Persona personaDocente = personaFacadeLocal.find(docente.getId());
                if (m.getCargoId().getId() == 1) {
                    presidente += docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaDocente.getNombres() + " "
                            + personaDocente.getApellidos() + " ";
                } else {
                    if (contador == 0) {
                        miembros += "" + docente.getTituloDocenteId().getTituloId().getAbreviacion() + " "
                                + personaDocente.getNombres() + " " + personaDocente.getApellidos() + " ";
                        contador++;
                    } else {
                        miembros += ", " + docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaDocente.getNombres() + " "
                                + personaDocente.getApellidos();;
                        contador++;
                    }
                }
            }
//            CoordinadorPeriodo coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
//            Persona datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
//            Docente docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
            secretario = user.getNombres().toUpperCase() + " " + user.getApellidos().toUpperCase();
//            DocumentoCarrera oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(miembroId, CatalogoDocumentoCarreraEnum.MIEMBROTRIBUNALPRIVADA.getTipo());
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            Calendar fechaActual = Calendar.getInstance();
            String fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());
            datosReporte.put("miembro", datosMiembro.getNombres().toUpperCase() + " " + datosMiembro.getApellidos().toUpperCase());
//            datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
            datosReporte.put("temaProyecto", miembro.getTribunalId().getProyectoId().getTemaActual());
            datosReporte.put("tituloMiembro", docenteMiembro.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
            datosReporte.put("cargoMiembro", miembro.getCargoId().getNombre().toUpperCase());
//            datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
//            datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(miembro.getTribunalId().getProyectoId().getId())));
            datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
            datosReporte.put("articulos_designar_miembro_tribunal", resourceBundle.getString("lbl.articulos_designar_miembro_tribunal"));
            datosReporte.put("asunto_designar_miembro_tribunal", resourceBundle.getString("lbl.asunto_designar_miembro_tribunal"));
            datosReporte.put("nota_designar_miembro_tribunal", resourceBundle.getString("lbl.designar_miembro_tribunal"));

//            reportes.oficioMiembroTribunalSprivada("docx", fechaFormateada, response, datosReporte, configuracionCarreraFacadeLocal, configuracionCarrera,
//                    oficioCarrera, presidente, miembros, miembroId, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, nOficio, secretario,
//                    path, pathSetting);
        } catch (Exception e) {
        }
    }

    public String crear(Usuario usuario, Tribunal tribunal, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//                sessionProyecto.setProyecto(proyecto);
//            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_miembro_tribunal");
//                if (tienePermiso == 1) {
//                    sessionMiembro.setMiembro(new Miembro());
//                    sessionMiembro.setPersona(new Persona());
//                    sessionMiembro.getMiembro().setTribunalId(tribunal);
//                    sessionMiembro.getMiembro().setEsActivo(true);
//                    if (param.equals("crear")) {
//                        navegacion = "editarMiembro?faces-redirect=true";
//                    } else {
//                        if (param.equals("crear-dlg")) {
//                            renderedDlgEditar = true;
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarMiembro').show()");
//                        }
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
        return navegacion;
    }

    public void abrirVistaDocentesDisponibles(Proyecto proyecto, Tribunal tribunal) {
        try {
            renderedDlgDocentesDisponibles = true;
            buscarDocentesDisponibles("", proyecto, tribunal);
            RequestContext.getCurrentInstance().execute("PF('dlgBuscarDocentesMiembrosTribunal').show()");
        } catch (Exception e) {
        }
    }

    public String editar(Usuario usuario, Miembro miembro, Proyecto proyecto) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//                sessionProyecto.setProyecto(proyecto);
//            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_miembro_tribunal");
//                if (tienePermiso == 1) {
//                    sessionMiembro.setMiembro(miembro);
//                    sessionMiembro.setPersona(personaFacadeLocal.find(miembro.getDocenteId()));
//                    cargo = miembro.getCargoId().toString();
//                    if (param.equals("editar")) {
//                        navegacion = "editarMiembro?faces-redirect=true";
//                    } else {
//                        if (param.equals("editar-dlg")) {
//                            renderedDlgEditar = true;
//                            RequestContext.getCurrentInstance().execute("PF('dlgEditarMiembro').show()");
//                        }
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String seleccionarDocente(Miembro miembro, Docente docente) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            miembro.setDocenteId(docente.getId());
            if (param.equalsIgnoreCase("seleccionar-dlg")) {
                RequestContext.getCurrentInstance().execute("PF('dlgBuscarDocentesMiembrosTribunal').hide()");
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_seleccionar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
        }
        return navegacion;
    }

    public boolean existeMiembro(Tribunal tribunal, Usuario usuario, Proyecto proyecto) {
        boolean var = false;
        for (EvaluacionTribunal evaluacionTribunal : tribunal.getEvaluacionTribunalList()) {
            if (var == false) {
                var = administrarEvaluacionesTribunal.existeMiembroOtraSustentacion(evaluacionTribunal, usuario, proyecto);
            } else {
                break;
            }
        }
        return var;
    }

    public String grabar(Miembro miembro, Tribunal tribunal, Proyecto proyecto, Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
//            if (miembro.getDocenteId() != null) {
//                int pos = cargo.indexOf(":");
//                CargoMiembro cm = cargoMiembroFacadeLocal.find(Integer.parseInt(cargo.substring(0, pos)));
//                if (cm != null) {
//                    miembro.setCargoId(cm);
//                }
//                if (existeMiembro(tribunal, usuario, tribunal.getProyectoId()) == false) {
//                    if (existePresidente(miembro, tribunal) == false) {
//                        if (miembro.getId() == null) {
//                            if (proyecto.getId() != null) {
//                                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//                                sessionProyecto.setProyecto(proyecto);
//                            }
//                            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_miembro_tribunal");
//                                if (tienePermiso == 1) {
//                                    miembro.setTribunalId(tribunal);
//                                    miembroFacadeLocal.create(miembro);
//                                    administrarNotificaciones.notificarAsignacionMiembroTribunal();
//                                    buscar(tribunal, usuario, "");
//                                    if (param.equalsIgnoreCase("grabar-dlg")) {
//                                        RequestContext.getCurrentInstance().execute("PF('dlgEditarMiembro').hide()");
//                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                                        FacesContext.getCurrentInstance().addMessage(null, message);
//                                        sessionMiembro.setMiembro(new Miembro());
//                                        sessionMiembro.setPersona(new Persona());
//                                    }
//                                } else {
//                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                                    FacesContext.getCurrentInstance().addMessage(null, message);
//                                }
//                            } else {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        } else {
//                            if (proyecto.getId() != null) {
//                                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//                                sessionProyecto.setProyecto(proyecto);
//                            }
//                            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_miembro_tribunal");
//                                if (tienePermiso == 1) {
//                                    miembro.setTribunalId(tribunal);
//                                    miembroFacadeLocal.edit(miembro);
//                                    buscar(tribunal, usuario, "");
//                                    if (param.equalsIgnoreCase("grabar-dlg")) {
//                                        RequestContext.getCurrentInstance().execute("PF('dlgEditarMiembro').hide()");
//                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                                        FacesContext.getCurrentInstance().addMessage(null, message);
//                                        sessionMiembro.setMiembro(new Miembro());
//                                    }
//                                } else {
//                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                                    FacesContext.getCurrentInstance().addMessage(null, message);
//                                }
//                            } else {
//                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                                FacesContext.getCurrentInstance().addMessage(null, message);
//                            }
//                        }
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe") + " " + bundle.getString("lbl.presidente"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("llbl.miembro") + "" + bundle.getString("lbl.miembro_encontrado"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " " + bundle.getString("lbl.docente"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void buscarDocentesDisponibles(String criterio, Proyecto proyecto, Tribunal tribunal) {
        this.docentes = new ArrayList<>();
        List<Miembro> miembros1 = new ArrayList<>();
        try {
            miembros1 = miembroFacadeLocal.buscarPorTribunal(tribunal.getId());
            if (permiteAgregarDirectorTribunal()) {
                for (ProyectoCarreraOferta proyectoCarreraOferta : proyectoCarreraOfertaFacadeLocal.buscarPorProyecto(proyecto.getId())) {
//                    for (DocenteCarrera docenteCarrera : docenteCarreraFacadeLocal.buscarPorCarrera(proyectoCarreraOferta.getCarreraId())) {
//                        Docente docente = docenteFacadeLocal.find(docenteCarrera.getDocenteId());
//                        Persona personaDocente = personaFacadeLocal.find(docente.getId());
//                        if (personaDocente.getApellidos().toLowerCase().contains(criterio.toLowerCase())
//                                || personaDocente.getNombres().toLowerCase().contains(criterio.toLowerCase())
//                                || personaDocente.getNumeroIdentificacion().toLowerCase().contains(criterio.toLowerCase())) {
//                            if (!miembros1.isEmpty()) {
//                                for (Miembro miembro : miembros1) {
//                                    if (miembro.getDocenteId() != docenteCarrera.getDocenteId().getId()) {
//                                        if (!docentes.contains(docenteCarrera.getDocenteId())) {
//                                            docentes.add(docenteCarrera.getDocenteId());
//                                        }
//                                    }
//
//                                }
//                            } else {
//                                if (!docentes.contains(docenteCarrera.getDocenteId())) {
//                                    docentes.add(docenteCarrera.getDocenteId());
//                                }
//                            }
//                        }
//                    }
                }
            } else {

                for (Docente docente : docenteFacadeLocal.buscarDocentesDisponiblesMiembrosTribunal(proyecto.getId())) {
                    Persona personaDocente = personaFacadeLocal.find(docente.getId());
                    if (personaDocente.getApellidos().toLowerCase().contains(criterio.toLowerCase())
                            || personaDocente.getNombres().toLowerCase().contains(criterio.toLowerCase())
                            || personaDocente.getNumeroIdentificacion().toLowerCase().contains(criterio.toLowerCase())) {

                        if (docenteSeleccionado(docente, tribunal) == false) {
                            if (!docentes.contains(docente)) {
                                docentes.add(docente);
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
        }
    }

    public boolean docenteSeleccionado(Docente docente, Tribunal tribunal) {
        boolean var = false;
        try {
            for (Miembro miembro : miembroFacadeLocal.buscarPorTribunal(tribunal.getId())) {
                if (miembro.getDocenteId().equals(docente)) {
                    var = true;
                    break;
                } else {
                    var = false;
                }
            }
        } catch (Exception e) {
        }
        return var;
    }

    public boolean permiteAgregarDirectorTribunal() {
        boolean var = false;
        String val = configuracionGeneralFacadeLocal.find((int) 9).getValor();
        if (val.equalsIgnoreCase("SI")) {
            var = true;
        } else {
            var = false;
        }
        return var;
    }

    public void consultar(Tribunal tribunal, String criterio) {
        try {
            this.consultaMiembros = new ArrayList<>();
            for (Miembro miembro : miembroFacadeLocal.buscarPorTribunal(tribunal.getId())) {
                Persona personaDocente = personaFacadeLocal.find(miembro.getDocenteId());
                if (personaDocente.getApellidos().toLowerCase().contains(criterio.toLowerCase())
                        || personaDocente.getNombres().toLowerCase().contains(criterio.toLowerCase())
                        || personaDocente.getNumeroIdentificacion().toLowerCase().contains(criterio.toLowerCase())) {
                    consultaMiembros.add(miembro);
                }
            }

        } catch (Exception e) {
        }
    }

    public void buscar(Tribunal tribunal, Usuario usuario, String criterio) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            this.miembros = new ArrayList<>();
            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_miembro_tribunal");
            if (tienePermiso == 1) {
                for (Miembro miembro : miembroFacadeLocal.buscarPorTribunal(tribunal.getId())) {
                    Persona personaDocente = personaFacadeLocal.find(miembro.getDocenteId());
                    if (personaDocente.getApellidos().toLowerCase().contains(criterio.toLowerCase())
                            || personaDocente.getNombres().toLowerCase().contains(criterio.toLowerCase())
                            || personaDocente.getNumeroIdentificacion().toLowerCase().contains(criterio.toLowerCase())) {
                        miembros.add(miembro);
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public List<CargoMiembro> buscarCargos() {
        try {
            return cargoMiembroFacadeLocal.findAll();
        } catch (Exception e) {
        }
        return null;
    }

    public boolean existePresidente(Miembro m, Tribunal tribunal) {
        boolean var = false;
        try {
            if (m.getCargoId().getId() == 1) {
                for (Miembro miembro : miembroFacadeLocal.buscarPorTribunal(tribunal.getId())) {
                    if (miembro.getCargoId().getId() == 1 && m.getCargoId().equals(miembro.getCargoId())) {
                        if (miembro.getId() != m.getId()) {
                            var = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return var;
    }

    public boolean existeUnPresidente(Tribunal tribunal) {
        boolean var = false;
        try {
            for (Miembro miembro : miembroFacadeLocal.buscarPorTribunal(tribunal.getId())) {
                if (miembro.getCargoId().getId() == 1) {
                    var = true;
                    break;
                }
            }
        } catch (Exception e) {
        }
        return var;
    }

    public void remover(Miembro miembro, Tribunal tribunal, Proyecto proyecto, Usuario usuario) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//                sessionProyecto.setProyecto(proyecto);
//            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_miembro_tribunal");
//                if (tienePermiso == 1) {
//                    miembro.setEsActivo(false);
//                    miembroFacadeLocal.edit(miembro);
//                    buscar(tribunal, usuario, criterio);
//                } else {
//                    if (tienePermiso == 2) {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". " + bundle.getString("lbl.msm_consulte"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            }
        } catch (Exception e) {
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedImprimirOficio(Usuario usuario, Proyecto proyecto) {
        try {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "imprimir_miembro_tribunal");
//            if (tienePermiso == 1) {
//                if (proyecto.getId() != null) {
//                    proyecto = proyectoFacadeLocal.find(proyecto.getId());
//                }
//                if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                        || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                        || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                        || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                    renderedImprimirOficioSustentacionPrivada = true;
//                    renderedImprimirOficioSustentacionPublica = false;
//                } else {
//                    if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                            || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                            || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                            || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                        renderedImprimirOficioSustentacionPublica = true;
//                        renderedImprimirOficioSustentacionPrivada = false;
//                    } else {
//                        renderedImprimirOficioSustentacionPublica = false;
//                        renderedImprimirOficioSustentacionPrivada = false;
//                    }
//                }
//            }
        } catch (Exception e) {
        }
    }

    public void renderedCrear(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_miembro_tribunal");
//                if (tienePermiso == 1) {
//                    renderedCrear = true;
//                } else {
//                    renderedCrear = false;
//                }
//            } else {
//                renderedCrear = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_miembro_tribunal");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public void renderedEditar(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_miembro_tribunal");
//                if (tienePermiso == 1) {
//                    renderedEditar = true;
//                    renderedNoEditar = false;
//                } else {
//                    renderedNoEditar = true;
//                    renderedEditar = false;
//                }
//            } else {
//                renderedNoEditar = true;
//                renderedEditar = false;
//            }
        } catch (Exception e) {
        }

    }

    public void renderedEliminar(Usuario usuario, Proyecto proyecto) {
        try {
//            if (proyecto.getId() != null) {
//                proyecto = proyectoFacadeLocal.find(proyecto.getId());
//            }
//            if (proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo())
//                    || proyecto.getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
//                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "eliminar_miembro_tribunal");
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

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public boolean isRenderedImprimirOficioSustentacionPublica() {
        return renderedImprimirOficioSustentacionPublica;
    }

    public void setRenderedImprimirOficioSustentacionPublica(boolean renderedImprimirOficioSustentacionPublica) {
        this.renderedImprimirOficioSustentacionPublica = renderedImprimirOficioSustentacionPublica;
    }

    public SessionMiembro getSessionMiembro() {
        return sessionMiembro;
    }

    public void setSessionMiembro(SessionMiembro sessionMiembro) {
        this.sessionMiembro = sessionMiembro;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }

    public List<Miembro> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<Miembro> miembros) {
        this.miembros = miembros;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }

    public void setDocentes(List<Docente> docentes) {
        this.docentes = docentes;
    }

    public String getCriterioDocente() {
        return criterioDocente;
    }

    public void setCriterioDocente(String criterioDocente) {
        this.criterioDocente = criterioDocente;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public boolean isRenderedDlgDocentesDisponibles() {
        return renderedDlgDocentesDisponibles;
    }

    public void setRenderedDlgDocentesDisponibles(boolean renderedDlgDocentesDisponibles) {
        this.renderedDlgDocentesDisponibles = renderedDlgDocentesDisponibles;
    }

    public boolean isRenderedImprimirOficioSustentacionPrivada() {
        return renderedImprimirOficioSustentacionPrivada;
    }

    public void setRenderedImprimirOficioSustentacionPrivada(boolean renderedImprimirOficioSustentacionPrivada) {
        this.renderedImprimirOficioSustentacionPrivada = renderedImprimirOficioSustentacionPrivada;
    }

    public boolean isRenderedDlgOficio() {
        return renderedDlgOficio;
    }

    public void setRenderedDlgOficio(boolean renderedDlgOficio) {
        this.renderedDlgOficio = renderedDlgOficio;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public Long getMiembroId() {
        return miembroId;
    }

    public void setMiembroId(Long miembroId) {
        this.miembroId = miembroId;
    }

    public List<Miembro> getConsultaMiembros() {
        return consultaMiembros;
    }

    public void setConsultaMiembros(List<Miembro> consultaMiembros) {
        this.consultaMiembros = consultaMiembros;
    }
    //</editor-fold>

}
