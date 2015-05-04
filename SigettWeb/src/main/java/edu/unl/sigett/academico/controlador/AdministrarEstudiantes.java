/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.jlmallas.comun.entity.Item;
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
import edu.unl.sigett.academico.managed.session.SessionEstudiante;
import edu.unl.sigett.seguridad.controlador.AdministrarUsuarios;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.Aspirante;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.EstadoEstudianteCarrera;
import edu.jlmallas.academico.entity.Estudiante;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import com.jlmallas.comun.entity.Nacionalidad;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemFacadeLocal;
import org.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import edu.unl.sigett.session.AspiranteFacadeLocal;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.session.ConfiguracionGeneralFacadeLocal;
import edu.jlmallas.academico.service.EstadoEstudianteCarreraFacadeLocal;
import edu.jlmallas.academico.service.EstudianteCarreraFacadeLocal;
import edu.jlmallas.academico.service.EstudianteFacadeLocal;
import org.jlmallas.seguridad.dao.LogDao;
import com.jlmallas.comun.service.NacionalidadFacadeLocal;
import com.jlmallas.comun.service.PersonaFacadeLocal;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.unl.sigett.session.UsuarioCarreraFacadeLocal;
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
            id = "editarEstudiante",
            pattern = "/editarEstudiante/#{sessionEstudiante.estudiante.id}",
            viewId = "/faces/pages/academico/editarEstudiante.xhtml"
    ),
    @URLMapping(
            id = "crearEstudiante",
            pattern = "/crearEstudiante/",
            viewId = "/faces/pages/academico/editarEstudiante.xhtml"
    ),
    @URLMapping(
            id = "estudiantes",
            pattern = "/estudiantes/",
            viewId = "/faces/pages/academico/buscarEstudiantes.xhtml"
    )})
public class AdministrarEstudiantes implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionEstudiante sessionEstudiante;
    @Inject
    private AdministrarEstudiantesCarrera administrarEstudiantesCarrera;
    @Inject
    private AdministrarUsuarios administrarUsuarios;

    @EJB
    private PersonaFacadeLocal personaFacadeLocal;
    @EJB
    private EstudianteFacadeLocal estudianteFacadeLocal;
    @EJB
    private ItemFacadeLocal itemFacadeLocal;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private EstudianteCarreraFacadeLocal estudianteCarreraFacadeLocal;
    @EJB
    private AspiranteFacadeLocal aspiranteFacadeLocal;
    @EJB
    private EstadoEstudianteCarreraFacadeLocal estadoEstudianteCarreraFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ConfiguracionGeneralFacadeLocal configuracionGeneralFacadeLocal;
    @EJB
    private NacionalidadFacadeLocal nacionalidadFacadeLocal;
    @EJB
    private UsuarioCarreraFacadeLocal usuarioCarreraFacadeLocal;
//    @EJB(lookup = "java:global/SigettWebService/EstudianteConsumeServiceImplement!edu.unl.sigett.academico.service.EstudianteConsumeService")
//    @EJB
//    private EstudianteConsumeService estudianteConsumeService;

    private List<Estudiante> estudiantes;
    private List<EstudianteCarrera> estudianteCarrerasRemovidos;
    private DualListModel<Carrera> carrerasDualList;

    private String tipoDocumento;
    private String genero;
    private String criterio;

    private boolean renderedNoEditar;
    private boolean renderedBuscar;
    private boolean renderedFotos;
    private boolean renderedInformacionEstudio;
    private boolean renderedDlgEditar;

    public AdministrarEstudiantes() {
        this.carrerasDualList = new DualListModel<>();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            renderedBuscar = true;
        } else {
            renderedBuscar = false;
        }
    }

    public boolean renderedEditar() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_estudiante");
        if (tienePermiso == 1) {
            var = true;
            renderedNoEditar = false;
        } else {
            renderedNoEditar = true;
        }
        return var;
    }

    public boolean renderedCrear() {
        boolean var = false;
        int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_estudiante");
        if (tienePermiso == 1) {
            var = true;
        } else {
            var = false;
        }
        return var;
    }

    public void renderedFotos(Estudiante estudiante) {
        if (estudiante.getId() != null) {
            renderedFotos = true;
        } else {
            renderedFotos = false;
        }
    }

    public void renderedInformacionEstudio(Estudiante estudiante) {
        try {
            this.renderedInformacionEstudio = false;
            for (EstudianteCarrera ec : estudianteCarreraFacadeLocal.buscarPorEstudiante(estudiante.getId())) {
                if (ec.getEsActivo()) {
                    renderedInformacionEstudio = true;
                    break;
                }
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES">

    public void sgaWebServicesDatosEstudiante() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        if (usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_estudiante") == 1) {
            try {
                Map parametros = new HashMap();
                parametros.put("persona", sessionEstudiante.getPersona());
                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 25).getValor();
                String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
                String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
                String s = serviceUrl + "?cedula=" + sessionEstudiante.getPersona().getNumeroIdentificacion();
                parametros.put("url", s);
                parametros.put("clave", passwordService);
                parametros.put("usuario", userService);
                parametros.put("msm_sincronizado", bundle.getString("lbl.sincronizado"));
                parametros.put("msm_no_sincronizado", bundle.getString("lbl.no_sincronizar_web_services"));
//                Map resultado = estudianteConsumeService.getDatosEstudiante(parametros);
//                sessionEstudiante.setPersona((Persona) resultado.get("persona"));
                this.genero = sessionEstudiante.getPersona().getGeneroId().toString();
//                ConexionServicio conexionServicio = new ConexionServicio();
//                String serviceUrl = configuracionGeneralFacadeLocal.find((int) 25).getValor();
//                String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//                String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//                String s = serviceUrl + "?cedula=" + sessionEstudiante.getPersona().getNumeroIdentificacion();
//                String datosJson = conexionServicio.conectar(s, userService, passwordService);
//                if (!datosJson.equalsIgnoreCase("")) {
//                    JsonParser parser = new JsonParser();
//                    JsonElement datos = parser.parse(datosJson);
//                    recorrerElementosJson(datos);
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                } else {
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
            } catch (Exception e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, bundle.getString("lbl.no_sincronizar_web_services"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

//    public void sgaWebServicesDatosEstudiante1(Estudiante estudiante, Carrera carrera) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        try {
//            ConexionServicio conexionServicio = new ConexionServicio();
//            String serviceUrl = configuracionGeneralFacadeLocal.find((int) 25).getValor();
//            String passwordService = configuracionGeneralFacadeLocal.find((int) 5).getValor();
//            String userService = configuracionGeneralFacadeLocal.find((int) 6).getValor();
//            String s = serviceUrl + "?cedula=" + estudiante.getPersona().getNumeroIdentificacion();
//            String datosJson = conexionServicio.conectar(s, userService, passwordService);
//            estudiante.getPersona().setEmail("ejemplo@gmail.com");
//            if (!datosJson.equalsIgnoreCase("")) {
//                JsonParser parser = new JsonParser();
//                JsonElement datos = parser.parse(datosJson);
//                recorrerElementosJson(datos, estudiante);
//                if (estudiante.getId() == null) {
//                    Nacionalidad nacionalidad = nacionalidadFacadeLocal.find(1);
//                    TipoDocumentoIdentificacion tp = tipoDocumentoIdentificacionFacadeLocal.find(1);
//                    estudiante.getPersona().setTipoDocumentoIdentificacionId(tp);
//                    estudiante.getPersona().setEstudiante((Estudiante) null);
//                    estudiante.getPersona().setNacionalidadId(nacionalidad);
//                    personaFacadeLocal.create(estudiante.getPersona());
//                    estudiante.setId(estudiante.getPersona().getId());
//                    estudianteFacadeLocal.create(estudiante);
//                    administrarUsuarios.grabarUsuarioEstudiante(estudiante);
//                    EstudianteCarrera estudianteCarrera = new EstudianteCarrera();
//                    EstadoEstudianteCarrera estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find(1);
//                    estudianteCarrera.setCarreraId(carrera);
//                    estudianteCarrera.getAspirante().setEsApto(false);
//                    Aspirante aspirante = estudianteCarrera.getAspirante();
//                    estudianteCarrera.setAspirante((Aspirante) null);
//                    estudianteCarrera.setEsActivo(true);
//                    estudianteCarrera.setEstudianteId(estudiante);
//                    estudianteCarrera.setEstadoEstudianteCarrera(estadoEstudianteCarrera);
//                    estudianteCarreraFacadeLocal.create(estudianteCarrera);
//                    aspirante.setEstudianteCarrera(estudianteCarrera);
//                    aspirante.setId(estudianteCarrera.getId());
//                    aspiranteFacadeLocal.create(aspirante);
//                } else {
//                    personaFacadeLocal.edit(estudiante.getPersona());
//                    estudianteFacadeLocal.edit(estudiante);
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
//        } catch (Exception e) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, bundle.getString("lbl.no_sincronizar_web_services"), "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear(Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (param.equalsIgnoreCase("crear-dlg")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_estudiante");
                if (tienePermiso == 1) {
                    sessionEstudiante.setEstudiante(new Estudiante());
                    sessionEstudiante.setPersona(new Persona());
                    estudianteCarrerasRemovidos = new ArrayList<>();
                    listadoCarreras(new Estudiante());
                    tipoDocumento = "";
                    genero = "";
                    renderedDlgEditar = true;
                    Nacionalidad nacionalidad = nacionalidadFacadeLocal.find(1);
                    sessionEstudiante.getPersona().setNacionalidadId(nacionalidad);
                    renderedFotos(sessionEstudiante.getEstudiante());
                    renderedInformacionEstudio(sessionEstudiante.getEstudiante());
                    administrarEstudiantesCarrera.habilitaCampoEsAptoAspirante(usuario);
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEstudiante').show()");
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                return navegacion;
            }
            if (param.equalsIgnoreCase("crear")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "crear_estudiante");
                if (tienePermiso == 1) {
                    sessionEstudiante.setEstudiante(new Estudiante());
                    sessionEstudiante.setPersona(new Persona());
                    estudianteCarrerasRemovidos = new ArrayList<>();
                    listadoCarreras(new Estudiante());
                    tipoDocumento = "";
                    genero = "";
                    renderedFotos(sessionEstudiante.getEstudiante());
                    administrarEstudiantesCarrera.habilitaCampoEsAptoAspirante(usuario);
                    renderedInformacionEstudio(sessionEstudiante.getEstudiante());
                    navegacion = "pretty:crearEstudiante";
                } else {
                    if (tienePermiso == 2) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("pretty:login");
                    }
                }
                return navegacion;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public String grabar() {
        String navegacion = "";
        try {
            List<EstudianteCarrera> estudianteCarreras = new ArrayList<>();
            estudianteCarreras.addAll(sessionEstudiante.getEstudiante().getEstudianteCarreraList());
            sessionEstudiante.getEstudiante().setEstudianteCarreraList(new ArrayList<EstudianteCarrera>());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            int posTipoDoc = tipoDocumento.indexOf(":");
            int posGenero = genero.indexOf(":");
            Item g = itemFacadeLocal.buscarPorCodigo(CatalogoEnum.GENERO.getTipo(), genero);
            Item tdi = itemFacadeLocal.buscarPorCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(), tipoDocumento);
            if (g != null) {
                sessionEstudiante.getPersona().setGeneroId(g.getId());
            }
            if (tdi != null) {
                sessionEstudiante.getPersona().setTipoDocumentoIdentificacionId(tdi.getId());
            }
            if (!carrerasDualList.getTarget().isEmpty()) {
//               sessionEstudiante.getPersona().setEstudiante(estudiante);
                if (sessionEstudiante.getEstudiante().getId() == null) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "crear_estudiante");
                    if (tienePermiso == 1) {
                        if (personaFacadeLocal.esUnico(sessionEstudiante.getPersona().getNumeroIdentificacion(), sessionEstudiante.getPersona().getId())) {
//                            estudiante.getPersona().setEstudiante((Estudiante) null);
                            personaFacadeLocal.create(sessionEstudiante.getPersona());
                            sessionEstudiante.getEstudiante().setId(sessionEstudiante.getPersona().getId());
                            estudianteFacadeLocal.create(sessionEstudiante.getEstudiante());
                            administrarUsuarios.grabarUsuarioEstudiante(sessionEstudiante.getEstudiante());
                            logFacadeLocal.create(logFacadeLocal.crearLog("Estudiante", sessionEstudiante.getEstudiante().getId() + "", "CREAR", "|Nombres= " + sessionEstudiante.getPersona().getNombres()
                                    + "|Apellidos= " + sessionEstudiante.getPersona().getApellidos() + "|Cédula= " + sessionEstudiante.getPersona().getNumeroIdentificacion()
                                    + "|Email= " + sessionEstudiante.getPersona().getEmail(), sessionUsuario.getUsuario()));
                            grabarEstudianteCarreras(sessionEstudiante.getEstudiante());
                            removerEstudianteCarreras(sessionEstudiante.getEstudiante());
                            sessionEstudiante.getEstudiante().getEstudianteCarreraList().addAll(estudianteCarreras);
                            if (param.equalsIgnoreCase("grabar")) {
                                navegacion = "pretty:estudiantes";
                                sessionEstudiante.setEstudiante(new Estudiante());
                            } else {
                                if (param.equalsIgnoreCase("grabar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_grabar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    if (param.equalsIgnoreCase("grabar-dlg")) {
                                        RequestContext.getCurrentInstance().execute("PF('dlgEditarEstudiante').hide()");
                                        sessionEstudiante.setEstudiante(new Estudiante());
                                    } else {
                                        if (param.equalsIgnoreCase("grabar-editar-dlg")) {
                                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_grabar"), "");
                                            FacesContext.getCurrentInstance().addMessage(null, message);
                                        } else {

                                        }
                                    }
                                }
                            }
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_existe"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }

                    } else {
                        if (tienePermiso == 2) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + " " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                    listadoEstudiantes("");
                    renderedFotos(sessionEstudiante.getEstudiante());
                    renderedInformacionEstudio(sessionEstudiante.getEstudiante());
                } else {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "editar_estudiante");
                    if (tienePermiso == 1) {
                        if (personaFacadeLocal.esUnico(sessionEstudiante.getPersona().getNumeroIdentificacion(), sessionEstudiante.getPersona().getId())) {
                            personaFacadeLocal.edit(sessionEstudiante.getPersona());
                            estudianteFacadeLocal.edit(sessionEstudiante.getEstudiante());
                            logFacadeLocal.create(logFacadeLocal.crearLog("Estudiante", sessionEstudiante.getEstudiante().getId() + "", "EDITAR", "|Nombres= " + sessionEstudiante.getPersona().getNombres()
                                    + "|Apellidos= " + sessionEstudiante.getPersona().getApellidos() + "|Cédula= " + sessionEstudiante.getPersona().getNumeroIdentificacion()
                                    + "|Email= " + sessionEstudiante.getPersona().getEmail(), sessionUsuario.getUsuario()));
                            grabarEstudianteCarreras(sessionEstudiante.getEstudiante());
                            removerEstudianteCarreras(sessionEstudiante.getEstudiante());
                            sessionEstudiante.getEstudiante().getEstudianteCarreraList().addAll(estudianteCarreras);
                            if (param.equalsIgnoreCase("grabar")) {
                                navegacion = "pretty:estudiantes";
                                sessionEstudiante.setEstudiante(new Estudiante());
                            } else {
                                if (param.equalsIgnoreCase("grabar-editar")) {
                                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_editar"), "");
                                    FacesContext.getCurrentInstance().addMessage(null, message);
                                } else {
                                    if (param.equalsIgnoreCase("grabar-dlg")) {
                                        RequestContext.getCurrentInstance().execute("PF('dlgEditarEstudiante').hide()");
                                        sessionEstudiante.setEstudiante(new Estudiante());
                                    } else {
                                        if (param.equalsIgnoreCase("grabar-editar-dlg")) {
                                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_editar"), "");
                                            FacesContext.getCurrentInstance().addMessage(null, message);
                                        }
                                    }
                                }
                            }
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_existe"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                        listadoEstudiantes("");
                        renderedFotos(sessionEstudiante.getEstudiante());
                        renderedInformacionEstudio(sessionEstudiante.getEstudiante());
                    } else {
                        if (tienePermiso == 2) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        } else {
                            navegacion = "pretty:login";
                        }
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_select") + " " + bundle.getString("lbl.carrera"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public String editar(Estudiante estudiante, Usuario usuario) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            if (param.equalsIgnoreCase("editar-dlg")) {
                int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_estudiante");
                if (tienePermiso == 1) {
                    sessionEstudiante.setEstudiante(estudiante);
                    sessionEstudiante.setPersona(personaFacadeLocal.find(estudiante.getId()));
                    estudianteCarrerasRemovidos = new ArrayList<>();
                    listadoCarreras(estudiante);
                    tipoDocumento = itemFacadeLocal.find(sessionEstudiante.getPersona().getTipoDocumentoIdentificacionId()).toString();
                    genero = itemFacadeLocal.find(sessionEstudiante.getPersona().getGeneroId()).toString();
                    renderedFotos(sessionEstudiante.getEstudiante());
                    renderedInformacionEstudio(sessionEstudiante.getEstudiante());
                    renderedDlgEditar = true;
                    administrarEstudiantesCarrera.habilitaCampoEsAptoAspirante(usuario);
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarEstudiante').show()");
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                if (param.equalsIgnoreCase("editar")) {
                    int tienePermiso = usuarioFacadeLocal.tienePermiso(usuario, "editar_estudiante");
                    if (tienePermiso == 1) {
                        sessionEstudiante.setEstudiante(estudiante);
                        estudianteCarrerasRemovidos = new ArrayList<>();
                        listadoCarreras(estudiante);
                        tipoDocumento = sessionEstudiante.getPersona().getTipoDocumentoIdentificacionId().toString();
                        administrarEstudiantesCarrera.habilitaCampoEsAptoAspirante(usuario);
                        genero = sessionEstudiante.getPersona().getGeneroId().toString();
                        renderedFotos(sessionEstudiante.getEstudiante());
                        renderedInformacionEstudio(sessionEstudiante.getEstudiante());
                        navegacion = "pretty:editarEstudiante";
                    } else {
                        if (tienePermiso == 2) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return navegacion;
    }

    public List<Item> listadoTiposDocumentos() {
        try {
            return itemFacadeLocal.buscarPorCatalogo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Item> listadoGeneros() {
        try {
            return itemFacadeLocal.buscarPorCatalogo(CatalogoEnum.GENERO.getTipo());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void listadoEstudiantes(String criterio) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            this.estudiantes = new ArrayList<>();
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_estudiante");
            if (tienePermiso == 1) {
                estudiantes = estudianteFacadeLocal.buscarPorCriterio(criterio);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    public void listadoCarreras(Estudiante estudiante) {
        List<Carrera> estudianteCarreras = new ArrayList<>();
        List<Carrera> carreras = new ArrayList<>();
        try {
            if (estudiante.getId() != null) {
                for (EstudianteCarrera ec : estudiante.getEstudianteCarreraList()) {
                    if (ec.getEsActivo()) {
                        estudianteCarreras.add(ec.getCarreraId());
                    }
                }
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
                    if (!estudianteCarreras.contains(carrera)) {
                        carreras.add(carrera);
                    }
                }
            } else {
                for (UsuarioCarrera usuarioCarrera : usuarioCarreraFacadeLocal.buscarPorUsuario(sessionUsuario.getUsuario().getId())) {
                    Carrera carrera = carreraFacadeLocal.find(usuarioCarrera.getCarreraId());
                    carreras.add(carrera);
                }
            }
            carrerasDualList = new DualListModel<>(carreras, estudianteCarreras);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public EstudianteCarrera contieneCarrera(List<EstudianteCarrera> estudianteCarreras, EstudianteCarrera ec) {
        EstudianteCarrera estudianteCarrera = null;
        for (EstudianteCarrera esc : estudianteCarreras) {
            if (esc.getCarreraId().equals(ec.getCarreraId())) {
                estudianteCarrera = esc;
                break;
            }
        }
        return estudianteCarrera;
    }

    public List<EstudianteCarrera> buscarEstudianteCarreras(Estudiante estudiante) {
        List<EstudianteCarrera> estudianteCarreras = new ArrayList<>();
        for (EstudianteCarrera ec : estudianteCarreraFacadeLocal.buscarPorEstudiante(estudiante.getId())) {
            if (ec.getEsActivo()) {
                estudianteCarreras.add(ec);
            }
        }
        return estudianteCarreras;
    }

    public void grabarEstudianteCarreras(Estudiante estudiante) {
        List<EstudianteCarrera> estudianteCarreras = new ArrayList<>();
        for (Object o : carrerasDualList.getTarget()) {
            int v = o.toString().indexOf(":");
            Integer id = Integer.parseInt(o.toString().substring(0, v));
            Carrera c = carreraFacadeLocal.find(id);
            EstudianteCarrera ec = new EstudianteCarrera();
            ec.setCarreraId(c);
            Long ecId = devuelveEstudianteCarrera(estudianteCarreraFacadeLocal.buscarPorEstudiante(estudiante.getId()), ec);
            ec = estudianteCarreraFacadeLocal.find(ecId);
            if (ec == null) {
                ec = new EstudianteCarrera();
                ec.setCarreraId(c);
                ec.setEsActivo(true);
                ec.setEstudianteId(estudiante);
            }
            estudianteCarreras.add(ec);
        }
        for (EstudianteCarrera estudianteCarrera : estudianteCarreras) {
            EstudianteCarrera esc = contieneCarrera(estudianteCarreraFacadeLocal.buscarPorEstudiante(sessionEstudiante.getEstudiante().getId()), estudianteCarrera);
            if (esc == null) {
                Aspirante aspirante = new Aspirante();
                EstadoEstudianteCarrera estadoEstudianteCarrera = estadoEstudianteCarreraFacadeLocal.find((int) 1);
                if (estadoEstudianteCarrera != null) {
                    estudianteCarrera.setEstadoId(estadoEstudianteCarrera);
                } else {
                    estadoEstudianteCarrera = new EstadoEstudianteCarrera();
                    estadoEstudianteCarrera.setNombre("ESTUDIANTE");
                    estadoEstudianteCarrera.setCodigo("ESTUDIANTE");
                    estadoEstudianteCarreraFacadeLocal.create(estadoEstudianteCarrera);
                    estudianteCarrera.setEstadoId(estadoEstudianteCarrera);
                }
                aspirante.setEsApto(false);
//                estudianteCarrera.setAspirante((Aspirante) null);
                estudianteCarreraFacadeLocal.create(estudianteCarrera);
//                aspirante.setEstudianteCarrera(estudianteCarrera);
                aspirante.setId(estudianteCarrera.getId());
                aspiranteFacadeLocal.create(aspirante);
                logFacadeLocal.create(logFacadeLocal.crearLog("EstudianteCarrera", estudianteCarrera.getId() + "", "CREAR", "|Carrera=" + estudianteCarrera.getCarreraId() + "|Estudiante=" + estudianteCarrera.getEstudianteId() + "|EsActivo= " + estudianteCarrera.getEsActivo(), sessionUsuario.getUsuario()));
            } else {
                esc.setEsActivo(true);
                estudianteCarreraFacadeLocal.edit(esc);
                logFacadeLocal.create(logFacadeLocal.crearLog("EstudianteCarrera", esc.getId() + "", "EDITAR", "|Carrera=" + esc.getCarreraId() + "|Esudiante=" + esc.getEstudianteId() + "|EsActivo= " + esc.getEsActivo(), sessionUsuario.getUsuario()));

            }

        }
    }

    public void removerEstudianteCarreras(Estudiante esudiante) {
        try {
            if (esudiante.getId() != null) {
                for (EstudianteCarrera ec : estudianteCarrerasRemovidos) {
                    Long id = devuelveEstudianteCarrera(esudiante.getEstudianteCarreraList(), ec);
                    EstudianteCarrera estudianteCarrera = null;
                    estudianteCarrera = estudianteCarreraFacadeLocal.find(id);
                    Aspirante aspirante = aspiranteFacadeLocal.find(ec.getId());
                    if (estudianteCarrera != null) {
                        aspirante.setEsApto(false);
                        estudianteCarrera.setEsActivo(false);
                        estudianteCarreraFacadeLocal.edit(estudianteCarrera);
                        aspiranteFacadeLocal.edit(aspirante);
                        logFacadeLocal.create(logFacadeLocal.crearLog("EstudianteCarrera", estudianteCarrera.getId() + "", "DESACTIVAR", "DESACTIVAR: |Carrera=" + estudianteCarrera.getCarreraId() + "|Estudiante=" + estudianteCarrera.getEstudianteId() + "|Estado= " + estudianteCarrera.getEsActivo(), sessionUsuario.getUsuario()));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public Long devuelveEstudianteCarrera(List<EstudianteCarrera> estudianteCarreras, EstudianteCarrera ec) {
        Long var = (long) 0;
        for (EstudianteCarrera estudianteCarrera : estudianteCarreras) {
            if (estudianteCarrera.getCarreraId().equals(ec.getCarreraId())) {
                var = estudianteCarrera.getId();
                break;
            }
        }
        return var;
    }

    public void transferEstudianteCarrera(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Integer id = Integer.parseInt(item.toString().substring(0, v));
                Carrera c = carreraFacadeLocal.find(id);
                EstudianteCarrera estudianteCarrera = new EstudianteCarrera();
                estudianteCarrera.setCarreraId(c);
                if (event.isRemove()) {
                    estudianteCarrerasRemovidos.add(estudianteCarrera);
                } else {
                    if (contieneCarrera(estudianteCarrerasRemovidos, estudianteCarrera) != null) {
                        estudianteCarrerasRemovidos.remove(estudianteCarrera);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void crearPDF() throws IOException, DocumentException {
        Document pdf = new Document();
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources/img" + File.separator + "selloInstitucion.png";
        Image image = Image.getInstance(logo);
        image.scaleToFit(50, 50);
        image.setAbsolutePosition(50f, 775f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(pdf, baos);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment; filename=Estudiantes.pdf");
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.setMargins(20f, 20f, 20f, 20f);
        pdf.setPageSize(PageSize.A4);
        Font fontHeader = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
        Font fontTitle = FontFactory.getFont("Times-Roman", 18, Font.BOLD);
        Paragraph title = new Paragraph(bundle.getString("lbl.listado") + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.estudiantes"), fontTitle);
        title.setSpacingAfter(20);
        title.setAlignment(1);
        pdf.open();
        pdf.add(image);
        pdf.add(title);
        PdfPTable pdfTable = new PdfPTable(3);
        pdfTable.setWidthPercentage(100f);
        pdfTable.setHorizontalAlignment(0);
        PdfPCell cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.nombres"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.apellidos"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);

        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.ci"), fontHeader));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfTable.addCell(cell1);
        pdfTable.setHeaderRows(1);
        for (Estudiante estudiante : estudiantes) {
            Persona personaEstudiante = personaFacadeLocal.find(estudiante.getId());
            pdfTable.addCell(personaEstudiante.getNombres());
            pdfTable.addCell(personaEstudiante.getApellidos());
            pdfTable.addCell(personaEstudiante.getNumeroIdentificacion());
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
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public boolean isRenderedDlgEditar() {
        return renderedDlgEditar;
    }

    public void setRenderedDlgEditar(boolean renderedDlgEditar) {
        this.renderedDlgEditar = renderedDlgEditar;
    }

    public boolean isRenderedFotos() {
        return renderedFotos;
    }

    public void setRenderedFotos(boolean renderedFotos) {
        this.renderedFotos = renderedFotos;
    }

    public boolean isRenderedInformacionEstudio() {
        return renderedInformacionEstudio;
    }

    public void setRenderedInformacionEstudio(boolean renderedInformacionEstudio) {
        this.renderedInformacionEstudio = renderedInformacionEstudio;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionEstudiante getSessionEstudiante() {
        return sessionEstudiante;
    }

    public void setSessionEstudiante(SessionEstudiante sessionEstudiante) {
        this.sessionEstudiante = sessionEstudiante;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public DualListModel<Carrera> getCarrerasDualList() {
        return carrerasDualList;
    }

    public void setCarrerasDualList(DualListModel<Carrera> carrerasDualList) {
        this.carrerasDualList = carrerasDualList;
    }

    public List<EstudianteCarrera> getEstudianteCarrerasRemovidos() {
        return estudianteCarrerasRemovidos;
    }

    public void setEstudianteCarrerasRemovidos(List<EstudianteCarrera> estudianteCarrerasRemovidos) {
        this.estudianteCarrerasRemovidos = estudianteCarrerasRemovidos;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public AdministrarEstudiantesCarrera getAdministrarEstudiantesCarrera() {
        return administrarEstudiantesCarrera;
    }

    public void setAdministrarEstudiantesCarrera(AdministrarEstudiantesCarrera administrarEstudiantesCarrera) {
        this.administrarEstudiantesCarrera = administrarEstudiantesCarrera;
    }

//</editor-fold>
}
