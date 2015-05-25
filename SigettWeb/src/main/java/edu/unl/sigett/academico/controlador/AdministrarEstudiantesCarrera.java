/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import org.jlmallas.api.date.DateResource;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.dao.NacionalidadFacadeLocal;
import com.jlmallas.comun.dao.PaisFacadeLocal;
import com.jlmallas.comun.entity.Item;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionEstudianteCarrera;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.seguridad.managed.session.SessionUsuarioCarrera;
import edu.unl.sigett.entity.Aspirante;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.Estudiante;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import edu.jlmallas.academico.entity.ReporteMatricula;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.jlmallas.academico.dao.EstudianteDao;
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Nacionalidad;
import com.jlmallas.comun.entity.Pais;
import com.jlmallas.comun.enumeration.GeneroEnum;
import com.jlmallas.comun.enumeration.TipoDocIdentEnum;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.dao.ReporteMatriculaDao;
import edu.jlmallas.academico.enumeration.EstadoEstudianteCarreraEnum;
import edu.jlmallas.academico.enumeration.EstadoMatriculaEnum;
import edu.unl.sigett.academico.dto.EstudianteCarreraDTO;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.EstudianteUsuario;
import edu.unl.sigett.service.AspiranteService;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.EstudianteUsuarioService;
import edu.unl.sigett.util.MessageView;
import org.jlmallas.api.http.UrlConexion;
import org.jlmallas.api.http.dto.SeguridadHttp;
import org.jlmallas.seguridad.dao.RolDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.RolUsuario;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarEstudianteCarrera",
            pattern = "/editarEstudianteCarrera/estudiantesCarrera/#{sessionEstudianteCarrera.estudianteCarreraDTO.estudianteCarrera.id}",
            viewId = "/faces/pages/academico/estudiantesCarrera/editarEstudianteCarrera.xhtml"
    ),
    @URLMapping(
            id = "crearEstudianteCarrera",
            pattern = "/crearEstudianteCarrera/",
            viewId = "/faces/pages/academico/estudiantesCarrera/editarEstudianteCarrera.xhtml"
    ),
    @URLMapping(
            id = "estudiantesCarrera",
            pattern = "/estudiantesCarrera/",
            viewId = "/faces/pages/academico/estudiantesCarrera/index.xhtml"
    )})
public class AdministrarEstudiantesCarrera implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE MANAGED BEANS">
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionEstudianteCarrera sessionEstudianteCarrera;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE SERVICIOS">
    @EJB
    private PersonaDao personaDao;
    @EJB
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB
    private EstudianteUsuarioService estudianteUsuarioService;
    @EJB
    private EstudianteDao estudianteDao;
    @EJB
    private LogDao logFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraDao;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private RolDao rolDao;
    @EJB
    private RolUsuarioDao rolUsuarioDao;
    @EJB
    private AspiranteService aspiranteService;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraDao;
    @EJB
    private ConfiguracionDao configuracionDao;
    @EJB
    private ReporteMatriculaDao reporteMatriculaDao;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralDao;

    @EJB
    private ItemService itemService;
    @EJB
    private PaisFacadeLocal paisFacadeLocal;
    @EJB
    private NacionalidadFacadeLocal nacionalidadFacadeLocal;
//</editor-fold>

    public AdministrarEstudiantesCarrera() {
    }

    public void init() {
        this.buscar();
        this.renderedCrear();
        this.renderedEditar();
    }

    public void initEditar() {
        this.listadoGeneros();
        this.listadoTiposDocumentos();
        this.obtenerMatriculaUltima();
        this.obtenerPrimerMatricula();
        this.listadoMatriculasEstudiante();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_estudiante_carrera");
            if (tienePermiso == 1) {
                sessionEstudianteCarrera.setEstudianteCarreraDTO(new EstudianteCarreraDTO(new EstudianteCarrera(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), new Estudiante(Boolean.TRUE), Boolean.TRUE, itemService.buscarPorCatalogoCodigo(
                        CatalogoEnum.ESTADOESTUDIANTECARRERA.getTipo(), EstadoEstudianteCarreraEnum.ESTUDIANTE.getTipo()).getId()), new Persona(), new Aspirante(null, Boolean.FALSE)));
                sessionEstudianteCarrera.setRenderedInformacionEstudio(false);
                habilitaCampoEsAptoAspirante(sessionUsuario.getUsuario());
                return "pretty:crearEstudianteCarrera";
            }
            if (tienePermiso == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear")
                        + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
            return "pretty:login";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String editar(EstudianteCarreraDTO estudianteCarreraDTO) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_estudiante_carrera");
            if (tienePermiso == 1) {
                sessionEstudianteCarrera.setEstudianteCarreraDTO(estudianteCarreraDTO);
                habilitaCampoEsAptoAspirante(sessionUsuario.getUsuario());
                this.renderedInformacionEstudio(estudianteCarreraDTO.getEstudianteCarrera().getEstudianteId());
                sessionEstudianteCarrera.setTipoDocumento(itemService.buscarPorId(estudianteCarreraDTO.getPersona().
                        getTipoDocumentoIdentificacionId()).toString());
                sessionEstudianteCarrera.setGenero(itemService.buscarPorId(estudianteCarreraDTO.getPersona().
                        getGeneroId()).toString());
                navegacion = "pretty:editarEstudianteCarrera";
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    navegacion = "pretty:login";
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void listadoGeneros() {
        try {
            sessionEstudianteCarrera.setGeneros(itemService.buscarPorCatalogo(CatalogoEnum.GENERO.getTipo()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listadoTiposDocumentos() {
        try {
            sessionEstudianteCarrera.setTiposDocumento(itemService.buscarPorCatalogo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscar() {
        this.sessionEstudianteCarrera.getEstudiantesCarreraDTO().clear();
        this.sessionEstudianteCarrera.getFilterEstudiantesCarreraDTO().clear();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "buscar_estudiante_carrera");
            if (tienePermiso == 1) {
                List<EstudianteCarrera> estudianteCarreras = this.estudianteCarreraDao.buscar(new EstudianteCarrera(
                        sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), null, null, null));
                if (estudianteCarreras == null) {
                    return;
                }
                for (EstudianteCarrera estudianteCarrera : estudianteCarreras) {
                    estudianteCarrera.setEstado(itemService.buscarPorId(estudianteCarrera.getEstadoId()).getNombre());
                    EstudianteCarreraDTO estudianteCarreraDTO = new EstudianteCarreraDTO(estudianteCarrera,
                            personaDao.find(estudianteCarrera.getEstudianteId().getId()),
                            aspiranteService.buscarPorId(estudianteCarrera.getId()));
                    sessionEstudianteCarrera.getEstudiantesCarreraDTO().add(estudianteCarreraDTO);
                }
                sessionEstudianteCarrera.setFilterEstudiantesCarreraDTO(sessionEstudianteCarrera.getEstudiantesCarreraDTO());
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * FIJAR EL ESTADO DE UN ESTUDIANTE POR PIRMEA VEZ POSCONDICIÓN: DEBE EXISTE
     * ESTUDIANTE
     */
    public void fijarEstadoEstudianteCarrera() {
        if (sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstadoId() == null) {
            sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().setEstadoId(itemService.buscarPorCatalogoCodigo(
                    CatalogoEnum.ESTADOESTUDIANTECARRERA.getTipo(), EstadoEstudianteCarreraEnum.ESTUDIANTE.getTipo()).getId());
        }
    }
//
//    public void activarDesactivar(EstudianteCarrera estudianteCarrera) {
//        try {
//            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "eliminar_estudiante_carrera");
//            if (tienePermiso == 1) {
//                estudianteCarreraDao.edit(estudianteCarrera);
//                logFacadeLocal.create(logFacadeLocal.crearLog("EstudianteCarrera", estudianteCarrera.getId() + "", "EDITAR", "|Carrera=" + estudianteCarrera.getCarreraId() + "|Estudiante=" + estudianteCarrera.getEstudianteId() + "|EsActivo= " + sessionEstudianteCarrera.getEstudianteCarrera().getEsActivo(), sessionUsuario.getUsuario()));
//            }
//        } catch (Exception e) {
//        }
//    }

    public String grabar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            /**
             * GENERO
             */
            Item g = itemService.buscarPorCatalogoCodigo(CatalogoEnum.GENERO.getTipo(), sessionEstudianteCarrera.getGenero());
            if (g != null) {
                sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().
                        setGeneroId(g.getId());
            }
            /**
             * Tipo de documento de identificación
             */
            Item tdi = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(),
                    sessionEstudianteCarrera.getTipoDocumento());
            if (tdi != null) {
                sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().
                        setTipoDocumentoIdentificacionId(tdi.getId());
            }
            if (sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getId() == null) {
                int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_estudiante_carrera");
                if (tienePermiso == 1) {
                    if (personaDao.esUnico(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion(),
                            sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getId())) {
                        /**
                         * GRABAR PERSONA
                         */
                        personaDao.create(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona());
                        /**
                         * GRABAR ESTUDIANTE
                         */
                        sessionEstudianteCarrera.getEstudianteCarreraDTO().
                                getEstudianteCarrera().getEstudianteId().setId(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getId());
                        estudianteDao.create(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId());

                        logFacadeLocal.create(logFacadeLocal.crearLog("Estudiante", sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId().getId()
                                + "", "CREAR", "|Nombres= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNombres()
                                + "|Apellidos= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getApellidos()
                                + "|Cédula= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion() + "|Email= "
                                + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getEmail(), sessionUsuario.getUsuario()));
                        /**
                         * GRABAR ESTUDIANTE CARRERA
                         */
                        fijarEstadoEstudianteCarrera();
                        estudianteCarreraDao.create(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera());
                        /**
                         * GRABAR ASPIRANTE
                         */
                        sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().setId(
                                sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getId());
                        aspiranteService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante());
                        /**
                         * GRABAR USUARIO
                         */
                        this.grabarUsuarioEstudiante(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId());
                        this.renderedInformacionEstudio(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId());
                        logFacadeLocal.create(logFacadeLocal.crearLog("EstudianteCarrera",
                                sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getId() + "", "CREAR", "|Carrera="
                                + sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getCarreraId().getId()
                                + "|Estudiante=" + sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId().getId()
                                + "|EsActivo= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEsActivo(),
                                sessionUsuario.getUsuario()));
                        if (param.equalsIgnoreCase("grabar")) {
                            sessionEstudianteCarrera.setEstudianteCarreraDTO(new EstudianteCarreraDTO(new EstudianteCarrera(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), new Estudiante(), Boolean.TRUE, null), new Persona(), new Aspirante()));
                            return "pretty:estudiantesCarrera";
                        }
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            return "";
                        }
                        return "";
                    }
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_existe"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
            }
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_estudiante_carrera");
            if (tienePermiso == 1) {
                if (personaDao.esUnico(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion(),
                        sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getId())) {
                    /**
                     * EDITAR PERSONA
                     */
                    personaDao.edit(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona());
                    /**
                     * EDITAR ESTUDIANTE
                     */
                    sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId().
                            setId(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getId());
                    estudianteDao.edit(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId());
                    logFacadeLocal.create(logFacadeLocal.crearLog("Estudiante",
                            sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId().getId() + "", "EDITAR", "|Nombres= "
                            + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNombres()
                            + "|Apellidos= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getApellidos()
                            + "|Cédula= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion() + "|Email= "
                            + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getEmail(), sessionUsuario.getUsuario()));
                    fijarEstadoEstudianteCarrera();
                    estudianteCarreraDao.edit(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera());
                    this.renderedInformacionEstudio(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId());
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionEstudianteCarrera.setEstudianteCarreraDTO(new EstudianteCarreraDTO(new EstudianteCarrera(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), new Estudiante(), Boolean.TRUE, null), new Persona(), new Aspirante()));
                        return "pretty:estudiantesCarrera";
                    }
                    if (param.equalsIgnoreCase("grabar-editar")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " "
                                + bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        return "";
                    }
                    return "";
                }
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString(
                        "lbl.msm_existe"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
            if (tienePermiso == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }

        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return "";
    }

    /**
     * GRABAR USUARIO PARA ESTUDIANTE
     *
     * @param estudiante
     */
    public void grabarUsuarioEstudiante(Estudiante estudiante) {
        try {
            Usuario usuario = null;
            Persona personaEstudiante = personaDao.find(estudiante.getId());
            EstudianteUsuario eu = estudianteUsuarioService.buscarPorEstudiante(new EstudianteUsuario(estudiante.getId()));
            if (eu != null) {
                usuario = usuarioDao.find(eu.getId());
            }
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setApellidos(personaEstudiante.getApellidos());
                usuario.setNombres(personaEstudiante.getNombres());
                usuario.setEmail(personaEstudiante.getEmail());
                usuario.setEsSuperuser(false);
                usuario.setEsActivo(true);
                usuario.setPassword(configuracionDao.encriptaClave(personaEstudiante.getNumeroIdentificacion()));
                usuario.setUsername(personaEstudiante.getNumeroIdentificacion());
                if (usuarioDao.unicoUsername(usuario.getUsername()) == false) {
                    usuarioDao.create(usuario);
                    EstudianteUsuario estudianteUsuario = new EstudianteUsuario();
                    estudianteUsuario.setEstudianteId(estudiante.getId());
                    estudianteUsuario.setId(usuario.getId());
                    estudianteUsuario.setId(usuario.getId());
                    estudianteUsuarioService.guardar(estudianteUsuario);
                    Rol rol = rolDao.find((long) 2);
                    if (rol != null) {
                        RolUsuario rolUsuario = new RolUsuario();
                        rolUsuario.setRolId(rol);
                        rolUsuario.setUsuarioId(usuario);
                        rolUsuarioDao.create(rolUsuario);
                    }
                }
            } else {
                if (usuarioDao.unicoUsername(usuario.getUsername()) == false) {
                    usuario.setPassword(configuracionDao.encriptaClave(personaEstudiante.getNumeroIdentificacion()));
                    usuarioDao.edit(usuario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ACTUALIZAR ESTADO DE ASPIRANTE
     */
    public void esAptoEstudiante() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().getEsApto()) {
                sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().setEsApto(false);
            } else {
                sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().setEsApto(true);
            }
            if (sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().getId() != null) {
                aspiranteService.actualizar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante());
                logFacadeLocal.create(logFacadeLocal.crearLog("Aspirante", sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().getId()
                        + "", "EDITAR", "|EsApto=" + sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().getEsApto(),
                        sessionUsuario.getUsuario()));
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_editar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerMatriculaUltima() {
        ReporteMatricula rm = new ReporteMatricula();
        try {
            rm = reporteMatriculaDao.buscarUltimaMatriculaEstudiante(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getId());
            if (rm == null) {
                sessionEstudianteCarrera.setReporteMatriculaUltimo(new ReporteMatricula());
                return;
            }
            sessionEstudianteCarrera.setReporteMatriculaUltimo(rm);
        } catch (Exception e) {
        }
    }

    public void obtenerPrimerMatricula() {
        ReporteMatricula rm = new ReporteMatricula();
        try {
            rm = reporteMatriculaDao.buscarPrimerMatriculaEstudiante(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getId());
            if (rm == null) {
                sessionEstudianteCarrera.setReporteMatriculaPrimer(new ReporteMatricula());
                return;
            }
            sessionEstudianteCarrera.setReporteMatriculaPrimer(rm);
        } catch (Exception e) {
        }
    }

    /**
     * PERMITIR HABILITA CHECKBOX PARA HABILITAR COMO APTO O NO A ESTUDIANTE
     *
     * @param usuario
     */
    public void habilitaCampoEsAptoAspirante(Usuario usuario) {
        try {
            sessionEstudianteCarrera.setRenderedEsAptoAspirante(Boolean.FALSE);
            if (usuarioDao.tienePermiso(usuario, "habilitar_apto_aspirante") == 1) {
                sessionEstudianteCarrera.setRenderedEsAptoAspirante(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * GRABAR REPORTES DE MATRICULAS
     */
    public void grabarReporteMatriculas() {
        try {
            for (ReporteMatricula rm : sessionEstudianteCarrera.getReporteMatriculasWS()) {
                rm.setEstudianteCarreraId(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera());
                ReporteMatricula reporte = reporteMatriculaDao.buscarPorMatriculaId(rm.getMatriculaId());
                if (reporte == null) {
                    sgaWebServicesReporteMatricula(rm);
                    reporteMatriculaDao.create(rm);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ReporteMatricula", rm.getId() + "", "CREAR", "NUEVO: |MatriculaId"
                            + rm.getMatriculaId() + "|NombreModulo= " + rm.getModuloMatriculado() + "|Oferta= " + rm.getOfertaAcademicaId().getId()
                            + "|Estado= " + rm.getEstadoMatriculaId(), sessionUsuario.getUsuario()));
                } else {
                    reporte.setEsAprobado(rm.isEsAprobado());
                    reporte.setMatriculaId(rm.getMatriculaId());
                    reporte.setEstudianteCarreraId(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera());
                    reporte.setEstadoMatriculaId(rm.getEstadoMatriculaId());
                    reporte.setModuloMatriculado(rm.getModuloMatriculado());
                    reporte.setOfertaAcademicaId(rm.getOfertaAcademicaId());
                    reporte.setParalelo(rm.getParalelo());
                    sgaWebServicesReporteMatricula(reporte);
                    reporteMatriculaDao.edit(reporte);
                    logFacadeLocal.create(logFacadeLocal.crearLog("ReporteMatricula", reporte.getId() + "", "CREAR", "NUEVO: |MatriculaId"
                            + reporte.getMatriculaId() + "|NombreModulo= " + reporte.getModuloMatriculado() + "|Oferta= "
                            + reporte.getOfertaAcademicaId().getId() + "|Estado= " + reporte.getEstadoMatriculaId(), sessionUsuario.getUsuario()));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * LISTAR LAS MATRICULAS DE UN ESTUDIANTE
     */
    public void listadoMatriculasEstudiante() {
        sessionEstudianteCarrera.getReporteMatriculas().clear();
        List<ReporteMatricula> reporteMatriculas = reporteMatriculaDao.buscarPorEstudianteCarrera(
                sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getId());
        if (reporteMatriculas == null) {
            return;
        }
        for (ReporteMatricula reporteMatricula : reporteMatriculas) {
            reporteMatricula.setEstado(itemService.buscarPorId(reporteMatricula.getEstadoMatriculaId()).getNombre());
            sessionEstudianteCarrera.getReporteMatriculas().add(reporteMatricula);
        }
    }

    public void compruebaEsAspiranteApto() {
        boolean esApto = false;
        String mensaje = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        DateResource calculo = new DateResource();
        if (sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getCarreraId() != null) {
            String moduloMaxAprobado = configuracionCarreraDao.buscar(new ConfiguracionCarrera(sessionEstudianteCarrera.getEstudianteCarreraDTO()
                    .getEstudianteCarrera().getCarreraId().getId(), "MA")).get(0).getValor();
            String moduloEgresado = configuracionCarreraDao.buscar(new ConfiguracionCarrera(sessionEstudianteCarrera.getEstudianteCarreraDTO()
                    .getEstudianteCarrera().getCarreraId().getId(), "ME")).get(0).getValor();
            int tiempoGracia = Integer.parseInt(configuracionGeneralDao.find((int) 21).getValor());
            ReporteMatricula reporte = sessionEstudianteCarrera.getReporteMatriculaUltimo();
            if (reporte.getNumeroModuloMatriculado() != null) {
                if (Integer.parseInt(reporte.getNumeroModuloMatriculado()) > Integer.parseInt(moduloMaxAprobado)) {
                    Calendar fechaActual = Calendar.getInstance();
                    int tiempo = calculo.calculaDuracionEnDias(reporte.getOfertaAcademicaId().getFechaFin(), fechaActual.getTime(), 0);
                    if (tiempo >= 0) {
                        if (tiempo <= tiempoGracia) {
                            esApto = true;
                        } else {
                            mensaje = bundle.getString("lbl.msm_tiempo_apto_aspirante");
                        }
                    }
                } else {
                    mensaje = bundle.getString("lbl.msm_modulo_apto_aspirante");
                }

                if (Integer.parseInt(reporte.getNumeroModuloMatriculado()) >= Integer.parseInt(moduloEgresado)) {
                    sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().setEstadoId(itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOESTUDIANTECARRERA.getTipo(), EstadoEstudianteCarreraEnum.ESTUDIANTE.getTipo()).getId());
                }
                if (esApto) {
                    sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().setEsApto(Boolean.TRUE);
                    sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().setEstadoId(itemService.buscarPorCatalogoCodigo(
                            CatalogoEnum.ESTADOESTUDIANTECARRERA.getTipo(), EstadoEstudianteCarreraEnum.EGRESADO.getTipo()).getId());
                    aspiranteService.actualizar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante());
                    estudianteCarreraDao.edit(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera());
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_es_apto_aspirante"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    logFacadeLocal.create(logFacadeLocal.crearLog("Aspirante", sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante()
                            .getId() + "", "EDITAR", "NUEVO: |EsApto=" + sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().
                            getEsApto(), sessionUsuario.getUsuario()));
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.reporte_matriculas"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.estudiante") + " " + bundle.getString("lbl.msm_no_pertenece_carrera"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

//    public void crearPdf(Carrera carrera) throws IOException, DocumentException {
//        Document pdf = new Document();
//        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//        String logo = servletContext.getRealPath("") + File.separator + "resources/img" + File.separator + "selloInstitucion.png";
//        Image image = Image.getInstance(logo);
//        image.scaleToFit(50, 50);
//        image.setAbsolutePosition(50f, 775f);
//        Image carreraLogo = Image.getInstance(carrera.getLogo());
//        carreraLogo.scaleToFit(50, 50);
//        carreraLogo.setAbsolutePosition(450f, 775f);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfWriter.getInstance(pdf, baos);
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
//        response.setContentType("application/pdf");
//        response.addHeader("Content-disposition", "attachment; filename=Estudiantes.pdf");
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        pdf.setPageSize(PageSize.A4.rotate());
//        pdf.setMargins(20f, 20f, 20f, 20f);
//        pdf.setPageSize(PageSize.A4);
//        Font fontHeader = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
//        Font fontTitle = FontFactory.getFont("Times-Roman", 18, Font.BOLD);
//        Paragraph title = new Paragraph(bundle.getString("lbl.listado") + " " + bundle.getString("lbl.de") + " " + bundle.getString("lbl.estudiantes"), fontTitle);
//        Paragraph titleCarrera = new Paragraph(bundle.getString("lbl.carrera") + " " + bundle.getString("lbl.de") + " " + carrera.getNombre(), fontTitle);
//        Paragraph titleArea = new Paragraph(carrera.getAreaId().getNombre(), fontTitle);
//        title.setSpacingAfter(20);
//        title.setAlignment(1);
//        titleArea.setSpacingAfter(20);
//        titleArea.setAlignment(1);
//        titleCarrera.setSpacingAfter(20);
//        titleCarrera.setAlignment(1);
//        pdf.open();
//        pdf.add(image);
//        pdf.add(carreraLogo);
//        pdf.add(title);
//        pdf.add(titleArea);
//        pdf.add(titleCarrera);
//        PdfPTable pdfTable = new PdfPTable(4);
//        pdfTable.setWidthPercentage(100f);
//        pdfTable.setHorizontalAlignment(0);
//        PdfPCell cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.nombres"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.apellidos"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.ci"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//
//        cell1 = new PdfPCell(new Phrase(bundle.getString("lbl.email"), fontHeader));
//        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pdfTable.addCell(cell1);
//        pdfTable.setHeaderRows(1);
//        for (EstudianteCarrera estudianteCarrera : estudianteCarreras) {
//            Persona persona = personaDao.find(estudianteCarrera.getEstudianteId().getId());
//            pdfTable.addCell(persona.getNombres());
//            pdfTable.addCell(persona.getApellidos());
//            pdfTable.addCell(persona.getNumeroIdentificacion());
//            pdfTable.addCell(persona.getEmail());
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
//    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedInformacionEstudio(Estudiante estudiante) {
        try {
            this.sessionEstudianteCarrera.setRenderedInformacionEstudio(false);
            for (EstudianteCarrera ec : estudianteCarreraDao.buscar(new EstudianteCarrera(null, estudiante, Boolean.TRUE, null))) {
                if (ec.getEsActivo()) {
                    this.sessionEstudianteCarrera.setRenderedInformacionEstudio(true);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void renderedEditar() {
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "editar_estudiante_carrera");
        if (tienePermiso == 1) {
            sessionEstudianteCarrera.setRenderedEditar(true);
        } else {
            sessionEstudianteCarrera.setRenderedEditar(false);
        }
    }

    public void renderedCrear() {
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "crear_estudiante_carrera");
        sessionEstudianteCarrera.setRenderedEditar(false);
        if (tienePermiso == 1) {
            sessionEstudianteCarrera.setRenderedCrear(true);
            return;
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WEB SERVICES">

    /**
     * BUSCAR POR MEDIO DEL SERVICIO WEB LOS ESTUDIANTES QUE SE ENCUENTRAN
     * MATRICULADOS ACTUALMENTE EN UN PARALELO
     *
     * @param c
     */
    public void sgaWebServicesParalelosCarrera(Carrera c) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        try {
            ConfiguracionCarrera configuracionCarrera = configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(c.getId(), "OA"));
            if (configuracionCarrera == null) {
                return;
            }
            String ofertaIdActual = configuracionCarrera.getValor();
            String serviceUrl = configuracionGeneralDao.find((int) 41).getValor();
            String s = serviceUrl + "?id_oferta=" + ofertaIdActual + ";id_carrera=" + c.getIdSga();
            SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralDao.find((int) 5).getValor(),
                    s, configuracionGeneralDao.find((int) 6).getValor());
            UrlConexion conexion = new UrlConexion();
            String datosJson = conexion.conectar(seguridad);
            if (!datosJson.equalsIgnoreCase("")) {
                JsonParser parser = new JsonParser();
                JsonElement datos = parser.parse(datosJson);
                parserParalelosCarrera(datos, c);
            }
        } catch (Exception e) {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
        }
    }

    /**
     * PARSER PARALELOS CARRRERA
     *
     * @param elemento
     * @param c
     * @throws Exception
     */
    private void parserParalelosCarrera(JsonElement elemento, Carrera c) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parserParalelosCarrera(jsonElement, c);

                    } catch (Exception e) {
                        parserParalelosCarrera(entrada.getValue(), c);
                    }

                }

            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionEstudianteCarrera.setKeyWsParalelosCarreraEntero(0);
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserParalelosCarrera(entrada, c);
                }
            } else if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (valor.isNumber()) {
                    if (sessionEstudianteCarrera.getKeyWsParalelosCarreraEntero() == 0) {
                        String paraleloId = valor.getAsInt() + "";
                        this.sgaWebServicesEstadoEstudiantesParalelo(paraleloId, c);
                        sessionEstudianteCarrera.setKeyWsParalelosCarreraEntero(sessionEstudianteCarrera.getKeyWsParalelosCarreraEntero() + 1);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * PERMITE GUARDAR EN LA BD LOS ESTUDIANTES MATRICULADOS EN UN PARALELO DE
     * UNA DETERMINADA CARRERA Y OFERTA ACADÉMICA
     */
    public void grabarDesdeWebService() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        if (usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                String serviceUrl = configuracionGeneralDao.find((int) 25).getValor();
                String s = serviceUrl + "?cedula=" + sessionEstudianteCarrera.getEstudianteCarreraDTOWS().
                        getPersona().getNumeroIdentificacion();
                SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralDao.find((int) 5).getValor(),
                        s, configuracionGeneralDao.find((int) 6).getValor());
                UrlConexion conexion = new UrlConexion();
                String datosJson = conexion.conectar(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    parserEstudianteJson(datos, sessionEstudianteCarrera.getEstudianteCarreraDTOWS());
                    if (sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId().getId() == null) {
                        Calendar fechaActual = Calendar.getInstance();
                        if (sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getTipoDocumentoIdentificacionId() == null) {
                            Item itemT = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(),
                                    TipoDocIdentEnum.CEDULA.getTipo());
                            sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().setTipoDocumentoIdentificacionId(itemT.getId());
                        }
                        if (sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getGeneroId() == null) {
                            Item itemG = itemService.buscarPorCatalogoCodigo(CatalogoEnum.GENERO.getTipo(), GeneroEnum.MASCULINO.getTipo());
                            sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().setGeneroId(itemG.getId());
                        }
                        if (sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getNacionalidadId() == null) {
                            Nacionalidad nacionalidad = nacionalidadFacadeLocal.find(1);
                            sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().setNacionalidadId(nacionalidad);
                        }
                        if (sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getFechaNacimiento() == null) {
                            sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona()
                                    .setFechaNacimiento(fechaActual.getTime());
                        }
                        if (sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getId() == null) {
                            personaDao.create(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona());
                        } else {
                            personaDao.edit(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona());
                        }
                        sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId()
                                .setId(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getId());
                        estudianteDao.create(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId());
                        this.grabarUsuarioEstudiante(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId());
                        estudianteCarreraDao.create(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera());
                        sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getAspirante().
                                setId(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getId());
                        aspiranteService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getAspirante());
                    } else {
                        Persona datosDocente = personaDao.find(
                                sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId().getId());
                        personaDao.edit(datosDocente);
                        estudianteDao.edit(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId());
                    }
                }
            } catch (Exception e) {
                messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
            }
        } else {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
        }
    }

    public void sgaWebServicesDatosEstudiante() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        if (usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                String serviceUrl = configuracionGeneralDao.find((int) 25).getValor();
                String s = serviceUrl + "?cedula=" + sessionEstudianteCarrera.getEstudianteCarreraDTO().
                        getPersona().getNumeroIdentificacion();
                SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralDao.find((int) 5).getValor(),
                        s, configuracionGeneralDao.find((int) 6).getValor());
                UrlConexion conexion = new UrlConexion();
                String datosJson = conexion.conectar(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    parserEstudianteJson(datos, sessionEstudianteCarrera.getEstudianteCarreraDTO());
                    messageView.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
                }
            } catch (Exception e) {
                messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
            }
        } else {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
        }
    }

    /**
     * PARSER ESTUDIANTE
     *
     * @param elemento
     * @throws Exception
     */
    private void parserEstudianteJson(JsonElement elemento, EstudianteCarreraDTO estudianteCarreraDTO) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    sessionEstudianteCarrera.setKey(entrada.getKey());
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parserEstudianteJson(jsonElement, estudianteCarreraDTO);

                    } catch (Exception e) {
                        parserEstudianteJson(entrada.getValue(), estudianteCarreraDTO);
                    }
                }
                return;
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionEstudianteCarrera.setKeyEntero(0);
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserEstudianteJson(entrada, estudianteCarreraDTO);
                }
                return;
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (sessionEstudianteCarrera.getKeyEntero() == 1) {
                    estudianteCarreraDTO.getPersona().setNombres(valor.getAsString());
                    sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEntero() == 2) {
                    estudianteCarreraDTO.getPersona().setApellidos(valor.getAsString());
                    sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEntero() == 3) {
                    estudianteCarreraDTO.getPersona().setFechaNacimiento(configuracionGeneralDao.DeStringADate(valor.getAsString()));
                    sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEntero() == 7) {
                    Pais pais = paisFacadeLocal.buscarPorNombre(valor.getAsString());
                    if (pais != null) {
                        estudianteCarreraDTO.getPersona().setNacionalidadId(pais.getNacionalidadList().get(0));
                    } else {
                        Nacionalidad nacionalidad = nacionalidadFacadeLocal.find(1);
                        estudianteCarreraDTO.getPersona().setNacionalidadId(nacionalidad);
                    }
                    sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEntero() == 9) {
                    if (valor == null) {
                        estudianteCarreraDTO.getPersona().setEmail("S/N");
                        sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                        return;
                    }
                    if (valor.getAsString().equals("")) {
                        estudianteCarreraDTO.getPersona().setEmail("S/N");
                        sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                        return;
                    }
                    estudianteCarreraDTO.getPersona().setEmail(valor.getAsString());
                    sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEntero() == 10) {
                    estudianteCarreraDTO.getPersona().setGeneroId(itemService.buscarPorCatalogoCodigo(
                            CatalogoEnum.GENERO.getTipo(), valor.getAsString().toUpperCase()).getId());
                    sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                    return;
                }
                sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sgaWebServicesEstudianteMatriculas() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        if (usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_estudiante_carrera") == 1) {
            try {
                String serviceUrl = configuracionGeneralDao.find((int) 16).getValor();
                String s = serviceUrl + "?id_carrera=" + sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().
                        getCarreraId().getIdSga() + ";cedula="
                        + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion();
                SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralDao.find((int) 5).getValor(),
                        s, configuracionGeneralDao.find((int) 6).getValor());
                UrlConexion conexion = new UrlConexion();
                String datosJson = conexion.conectar(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    sessionEstudianteCarrera.getReporteMatriculasWS().clear();
                    parserMatriculasJson(datos);
                    grabarReporteMatriculas();
                    messageView.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
                }
            } catch (Exception e) {
                messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
            }
        } else {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
        }
    }

    public void sgaWebServicesReporteMatricula(ReporteMatricula rm) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        if (usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_estudiante_carrera") == 1) {
            try {
                Persona p = personaDao.find(rm.getEstudianteCarreraId().getEstudianteId().getId());
                String serviceUrl = configuracionGeneralDao.find((int) 44).getValor();
                String s = serviceUrl + "?cedula=" + p.getNumeroIdentificacion() + ";id_carrera="
                        + rm.getEstudianteCarreraId().getCarreraId().getIdSga() + ";id_oferta=" + rm.getOfertaAcademicaId().getIdSga();

                SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralDao.find((int) 5).getValor(),
                        s, configuracionGeneralDao.find((int) 6).getValor());
                UrlConexion conexion = new UrlConexion();
                String datosJson = conexion.conectar(seguridad);
                if (!datosJson.equalsIgnoreCase("")) {
                    JsonParser parser = new JsonParser();
                    JsonElement datos = parser.parse(datosJson);
                    parserReporteMatricula(datos, rm);
                    messageView.message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.sincronizado"), "");
                }
            } catch (Exception e) {
                messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
            }
        } else {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.permiso_denegado_sincronizar") + ". " + bundle.getString("lbl.msm_consulte"), "");
        }

    }

    /**
     * PARSER REPORTE MATRICULA
     *
     * @param elemento
     * @param rm
     * @throws Exception
     */
    private void parserReporteMatricula(JsonElement elemento, ReporteMatricula rm) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parserReporteMatricula(jsonElement, rm);

                    } catch (Exception e) {
                        parserReporteMatricula(entrada.getValue(), rm);
                    }
                }
            } else if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionEstudianteCarrera.setKeyEnteroRm(0);

                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserReporteMatricula(entrada, rm);
                }
            } else if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (sessionEstudianteCarrera.getKeyEnteroRm() == 3) {
                    double nota = Math.round(valor.getAsDouble() * 100);
                    nota = nota / 100;
                    rm.setNota(nota);
                }
                sessionEstudianteCarrera.setKeyEnteroRm(sessionEstudianteCarrera.getKeyEnteroRm() + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parserMatriculasJson(JsonElement elemento) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                sessionEstudianteCarrera.setReporteMatricula(new ReporteMatricula());
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parserMatriculasJson(jsonElement);

                    } catch (Exception e) {
                        parserMatriculasJson(entrada.getValue());
                    }
                }
                return;
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionEstudianteCarrera.setKeyEnteroMatricula(0);
                sessionEstudianteCarrera.setReporteMatricula(new ReporteMatricula(Boolean.FALSE));

                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserMatriculasJson(entrada);
                }
                return;
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (sessionEstudianteCarrera.getKeyEnteroMatricula() == 0) {
                    OfertaAcademica ofertaAcademica = ofertaAcademicaService.buscarPorIdSga(valor.getAsString());
                    if (ofertaAcademica != null) {
                        sessionEstudianteCarrera.getReporteMatricula().setOfertaAcademicaId(ofertaAcademica);
                    }
                    sessionEstudianteCarrera.setKeyEnteroMatricula(sessionEstudianteCarrera.getKeyEnteroMatricula() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEnteroMatricula() == 1) {
                    sessionEstudianteCarrera.getReporteMatricula().setMatriculaId(Long.parseLong(valor.getAsString()));
                    sessionEstudianteCarrera.setKeyEnteroMatricula(sessionEstudianteCarrera.getKeyEnteroMatricula() + 1);
                    return;
                }

                if (sessionEstudianteCarrera.getKeyEnteroMatricula() == 2) {
                    sessionEstudianteCarrera.getReporteMatricula().setParalelo(valor.getAsString());
                    sessionEstudianteCarrera.setKeyEnteroMatricula(sessionEstudianteCarrera.getKeyEnteroMatricula() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEnteroMatricula() == 3) {
                    sessionEstudianteCarrera.getReporteMatricula().setNumeroModuloMatriculado(valor.getAsString());
                    sessionEstudianteCarrera.setKeyEnteroMatricula(sessionEstudianteCarrera.getKeyEnteroMatricula() + 1);
                    return;
                }

                if (sessionEstudianteCarrera.getKeyEnteroMatricula() == 4) {
                    sessionEstudianteCarrera.getReporteMatricula().setModuloMatriculado(valor.getAsString());
                    sessionEstudianteCarrera.setKeyEnteroMatricula(sessionEstudianteCarrera.getKeyEnteroMatricula() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEnteroMatricula() == 5) {
                    Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOMATRICULA.getTipo(), valor.getAsString());
                    sessionEstudianteCarrera.getReporteMatricula().setEstadoMatriculaId(item.getId());
                    if (item.getCodigo().equals(EstadoMatriculaEnum.APROBADA.getTipo())) {
                        sessionEstudianteCarrera.getReporteMatricula().setEsAprobado(true);
                    }
                    sessionEstudianteCarrera.setKeyEnteroMatricula(sessionEstudianteCarrera.getKeyEnteroMatricula() + 1);
                }
                sessionEstudianteCarrera.setKeyEnteroMatricula(sessionEstudianteCarrera.getKeyEnteroMatricula() + 1);
            }

            sessionEstudianteCarrera.getReporteMatriculasWS().add(sessionEstudianteCarrera.getReporteMatricula());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * BUSCAR EN EL SERVICO WEB EL ESTADO DE LOS ESTUDIANTES, LO IMPORTANTE AQUI
     * ES ENCONTRAR LA CÉDULA DE CADA ESTUDIANTE EN EL PARALELO
     *
     * @param paraleloId
     * @param carrera
     */
    public void sgaWebServicesEstadoEstudiantesParalelo(String paraleloId, Carrera carrera) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        MessageView messageView = new MessageView();
        try {
            String serviceUrl = configuracionGeneralDao.find((int) 42).getValor();
            String s = serviceUrl + "?id_paralelo=" + paraleloId;

            SeguridadHttp seguridad = new SeguridadHttp(configuracionGeneralDao.find((int) 5).getValor(),
                    s, configuracionGeneralDao.find((int) 6).getValor());
            UrlConexion conexion = new UrlConexion();
            String datosJson = conexion.conectar(seguridad);
            if (!datosJson.equalsIgnoreCase("")) {
                JsonParser parser = new JsonParser();
                JsonElement datos = parser.parse(datosJson);
                parserEstadoEstudiantesParalelo(datos, carrera);
            }
        } catch (Exception e) {
            messageView.message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.no_sincronizar_web_services"), "");
        }
    }

    /**
     *
     * @param elemento
     * @param carrera
     * @throws Exception
     */
    private void parserEstadoEstudiantesParalelo(JsonElement elemento, Carrera carrera) throws Exception {
        try {
            if (elemento.isJsonObject()) {
                sessionEstudianteCarrera.setContadorEstadoEP(0);
                JsonObject obj = elemento.getAsJsonObject();
                java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
                java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                    try {
                        String e = new String(entrada.getValue().getAsString().getBytes(), "UTF-8");
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(e);
                        parserEstadoEstudiantesParalelo(jsonElement, carrera);
                    } catch (Exception e) {
                        parserEstadoEstudiantesParalelo(entrada.getValue(), carrera);
                    }
                }
                return;
            }
            if (elemento.isJsonArray()) {
                JsonArray array = elemento.getAsJsonArray();
                sessionEstudianteCarrera.setKeyEnteroEstadoEstudiantesParelelo(0);
                java.util.Iterator<JsonElement> iter = array.iterator();
                while (iter.hasNext()) {
                    JsonElement entrada = iter.next();
                    parserEstadoEstudiantesParalelo(entrada, carrera);
                }
                return;
            }
            if (elemento.isJsonPrimitive()) {
                JsonPrimitive valor = elemento.getAsJsonPrimitive();
                if (sessionEstudianteCarrera.getContadorEstadoEP() > 4) {
                    if (valor.isString()) {
                        if (sessionEstudianteCarrera.getKeyEnteroEstadoEstudiantesParelelo() == 3) {
                            Persona persona = personaDao.buscarPorNumeroIdentificacion(valor.getAsString());
                            Estudiante estudiante = null;
                            if (persona != null) {
                                estudiante = estudianteDao.find(persona.getId());
                            } else {
                                persona = new Persona();
                                persona.setNumeroIdentificacion(valor.getAsString());
                            }
                            if (estudiante == null) {
                                estudiante = new Estudiante(Boolean.TRUE);
                            }
                            sessionEstudianteCarrera.setEstudianteCarreraDTOWS(new EstudianteCarreraDTO(
                                    new EstudianteCarrera(carrera, estudiante, Boolean.TRUE, itemService.buscarPorCatalogoCodigo(
                                                    CatalogoEnum.ESTADOESTUDIANTECARRERA.getTipo(), EstadoEstudianteCarreraEnum.ESTUDIANTE.getTipo()).getId()), persona, new Aspirante(null, Boolean.FALSE)));
                            this.grabarDesdeWebService();
                        }
                    }
                    sessionEstudianteCarrera.setKeyEnteroEstadoEstudiantesParelelo(sessionEstudianteCarrera.getKeyEnteroEstadoEstudiantesParelelo() + 1);
                }
                sessionEstudianteCarrera.setContadorEstadoEP(sessionEstudianteCarrera.getContadorEstadoEP() + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">
    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionEstudianteCarrera getSessionEstudianteCarrera() {
        return sessionEstudianteCarrera;
    }

    public void setSessionEstudianteCarrera(SessionEstudianteCarrera sessionEstudianteCarrera) {
        this.sessionEstudianteCarrera = sessionEstudianteCarrera;
    }

    public SessionUsuarioCarrera getSessionUsuarioCarrera() {
        return sessionUsuarioCarrera;
    }

    public void setSessionUsuarioCarrera(SessionUsuarioCarrera sessionUsuarioCarrera) {
        this.sessionUsuarioCarrera = sessionUsuarioCarrera;
    }

//</editor-fold>
}
