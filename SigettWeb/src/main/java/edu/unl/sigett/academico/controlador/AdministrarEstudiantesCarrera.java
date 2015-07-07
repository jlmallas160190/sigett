/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jlmallas.comun.dao.NacionalidadDao;
import com.jlmallas.comun.dao.PaisDao;
import com.jlmallas.comun.entity.Item;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionEstudianteCarrera;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
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
import org.jlmallas.seguridad.dao.LogDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Nacionalidad;
import com.jlmallas.comun.entity.Pais;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.GeneroEnum;
import com.jlmallas.comun.enumeration.TipoDocIdentEnum;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.dao.ReporteMatriculaDao;
import edu.jlmallas.academico.enumeration.EstadoEstudianteCarreraEnum;
import edu.jlmallas.academico.enumeration.EstadoMatriculaEnum;
import edu.unl.sigett.academico.dto.EstudianteCarreraDTO;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.EstudianteUsuario;
import com.jlmallas.comun.enumeration.URLWSEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.PersonaService;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.jlmallas.academico.service.EstudianteService;
import edu.unl.sigett.service.AspiranteService;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.EstudianteUsuarioService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.MessageView;
import org.jlmallas.httpClient.NetClientServiceImplement;
import org.jlmallas.httpClient.ConexionDTO;
import org.jlmallas.httpClient.NetClientService;
import org.jlmallas.secure.Secure;
import org.jlmallas.seguridad.dao.RolDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.RolUsuario;
import org.jlmallas.seguridad.service.UsuarioService;

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
    @Inject
    private CabeceraController cabeceraController;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE SERVICIOS">
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/SigettService/ConfiguracionCarreraServiceImplement!edu.unl.sigett.service.ConfiguracionCarreraService")
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB(lookup = "java:global/SigettService/EstudianteUsuarioServiceImplement!edu.unl.sigett.service.EstudianteUsuarioService")
    private EstudianteUsuarioService estudianteUsuarioService;
    @EJB(lookup = "java:global/AcademicoService/EstudianteServiceImplement!edu.jlmallas.academico.service.EstudianteService")
    private EstudianteService estudianteService;
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logFacadeLocal;
    @EJB(lookup = "java:global/SigettService/EstudianteCarreraServiceImplement!edu.unl.sigett.service.EstudianteCarreraService")
    private EstudianteCarreraService estudianteCarreraService;
    @EJB(lookup = "java:global/SeguridadService/RolDaoImplement!org.jlmallas.seguridad.dao.RolDao")
    private RolDao rolDao;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/SeguridadService/RolUsuarioDaoImplement!org.jlmallas.seguridad.dao.RolUsuarioDao")
    private RolUsuarioDao rolUsuarioDao;
    @EJB(lookup = "java:global/SigettService/AspiranteServiceImplement!edu.unl.sigett.service.AspiranteService")
    private AspiranteService aspiranteService;
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;
    @EJB(lookup = "java:global/SigettService/ReporteMatriculaDaoImplement!edu.unl.sigett.dao.ReporteMatriculaDao")
    private ReporteMatriculaDao reporteMatriculaDao;
    @EJB(lookup = "java:global/AcademicoService/OfertaAcademicaServiceImplement!edu.jlmallas.academico.service.OfertaAcademicaService")
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/ComunService/PaisDaoImplement!com.jlmallas.comun.dao.PaisDao")
    private PaisDao paisDao;
    @EJB(lookup = "java:global/ComunService/NacionalidadDaoImplement!com.jlmallas.comun.dao.NacionalidadDao")
    private NacionalidadDao nacionalidadDao;
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
        sessionEstudianteCarrera.setEstudianteCarreraDTO(new EstudianteCarreraDTO(new EstudianteCarrera(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), new Estudiante(Boolean.TRUE), Boolean.TRUE, itemService.buscarPorCatalogoCodigo(
                CatalogoEnum.ESTADOESTUDIANTECARRERA.getTipo(), EstadoEstudianteCarreraEnum.ESTUDIANTE.getTipo()).getId()), new Persona(), new Aspirante(null, Boolean.FALSE)));
        sessionEstudianteCarrera.setRenderedInformacionEstudio(false);
        habilitaCampoEsAptoAspirante(sessionUsuario.getUsuario());
        return "pretty:crearEstudianteCarrera";

    }

    public String editar(EstudianteCarreraDTO estudianteCarreraDTO) {
        sessionEstudianteCarrera.setEstudianteCarreraDTO(estudianteCarreraDTO);
        habilitaCampoEsAptoAspirante(sessionUsuario.getUsuario());
        this.renderedInformacionEstudio(estudianteCarreraDTO.getEstudianteCarrera().getEstudianteId());
        sessionEstudianteCarrera.setTipoDocumento(itemService.buscarPorId(estudianteCarreraDTO.getPersona().
                getTipoDocumentoIdentificacionId()).toString());
        sessionEstudianteCarrera.setGenero(itemService.buscarPorId(estudianteCarreraDTO.getPersona().
                getGeneroId()).toString());
        return "pretty:editarEstudianteCarrera";
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
            List<EstudianteCarrera> estudianteCarreras = this.estudianteCarreraService.buscar(new EstudianteCarrera(
                    sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), null, null, null));
            if (estudianteCarreras == null) {
                return;
            }
            for (EstudianteCarrera estudianteCarrera : estudianteCarreras) {
                estudianteCarrera.setEstado(itemService.buscarPorId(estudianteCarrera.getEstadoId()).getNombre());
                EstudianteCarreraDTO estudianteCarreraDTO = new EstudianteCarreraDTO(estudianteCarrera,
                        personaService.buscarPorId(new Persona(estudianteCarrera.getEstudianteId().getId())),
                        aspiranteService.buscarPorId(estudianteCarrera.getId()));
                sessionEstudianteCarrera.getEstudiantesCarreraDTO().add(estudianteCarreraDTO);
            }
            sessionEstudianteCarrera.setFilterEstudiantesCarreraDTO(sessionEstudianteCarrera.getEstudiantesCarreraDTO());
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

    public String grabar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            Item g = itemService.buscarPorCatalogoCodigo(CatalogoEnum.GENERO.getTipo(), sessionEstudianteCarrera.getGenero());
            if (g != null) {
                sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().
                        setGeneroId(g.getId());
            }
            Item tdi = itemService.buscarPorCatalogoCodigo(CatalogoEnum.TIPODOCUMENTOIDENTIFICACION.getTipo(),
                    sessionEstudianteCarrera.getTipoDocumento());
            if (tdi != null) {
                sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().
                        setTipoDocumentoIdentificacionId(tdi.getId());
            }
            if (sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getId() == null) {
                if (personaService.esUnico(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion(),
                        sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getId())) {

                    personaService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona());
                    sessionEstudianteCarrera.getEstudianteCarreraDTO().
                            getEstudianteCarrera().getEstudianteId().setId(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getId());
                    estudianteService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId());

                    logFacadeLocal.create(logFacadeLocal.crearLog("Estudiante", sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId().getId()
                            + "", "CREAR", "|Nombres= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNombres()
                            + "|Apellidos= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getApellidos()
                            + "|Cédula= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion() + "|Email= "
                            + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getEmail(), sessionUsuario.getUsuario()));

                    fijarEstadoEstudianteCarrera();
                    estudianteCarreraService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera());
                    sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante().setId(
                            sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getId());
                    aspiranteService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getAspirante());
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
            if (personaService.esUnico(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion(),
                    sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getId())) {
                personaService.actualizar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona());
                sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId().
                        setId(sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getId());
                estudianteService.actualizar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId());
                logFacadeLocal.create(logFacadeLocal.crearLog("Estudiante",
                        sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getEstudianteId().getId() + "", "EDITAR", "|Nombres= "
                        + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNombres()
                        + "|Apellidos= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getApellidos()
                        + "|Cédula= " + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion() + "|Email= "
                        + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getEmail(), sessionUsuario.getUsuario()));
                fijarEstadoEstudianteCarrera();
                estudianteCarreraService.actualizar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera());
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
            Persona personaEstudiante = personaService.buscarPorId(new Persona(estudiante.getId()));
            EstudianteUsuario eu = estudianteUsuarioService.buscarPorEstudiante(new EstudianteUsuario(estudiante.getId()));
            if (eu != null) {
                usuario = usuarioService.buscarPorId(new Usuario(eu.getId()));
            }
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setApellidos(personaEstudiante.getApellidos());
                usuario.setNombres(personaEstudiante.getNombres());
                usuario.setEmail(personaEstudiante.getEmail());
                usuario.setEsSuperuser(false);
                usuario.setEsActivo(true);
                usuario.setPassword(cabeceraController.getSecureService().encrypt(
                        new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), personaEstudiante.getNumeroIdentificacion())));
                usuario.setUsername(personaEstudiante.getNumeroIdentificacion());
                if (usuarioService.unicoUsername(usuario.getUsername()) == false) {
                    usuarioService.guardar(usuario);
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
                if (usuarioService.unicoUsername(usuario.getUsername()) == false) {
                    usuario.setPassword(cabeceraController.getSecureService().encrypt(
                            new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(), personaEstudiante.getNumeroIdentificacion())));
                    usuarioService.actualizar(usuario);
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
        @SuppressWarnings("UnusedAssignment")
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
        @SuppressWarnings("UnusedAssignment")
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
    @SuppressWarnings("CallToThreadDumpStack")
    public void habilitaCampoEsAptoAspirante(Usuario usuario) {
        try {
            sessionEstudianteCarrera.setRenderedEsAptoAspirante(Boolean.FALSE);
            if (usuarioService.tienePermiso(usuario, "habilitar_apto_aspirante") == 1) {
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
        if (sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().getCarreraId() != null) {
            String moduloMaxAprobado = configuracionCarreraService.buscar(new ConfiguracionCarrera(sessionEstudianteCarrera.getEstudianteCarreraDTO()
                    .getEstudianteCarrera().getCarreraId().getId(), "MA")).get(0).getValor();
            String moduloEgresado = configuracionCarreraService.buscar(new ConfiguracionCarrera(sessionEstudianteCarrera.getEstudianteCarreraDTO()
                    .getEstudianteCarrera().getCarreraId().getId(), "ME")).get(0).getValor();
            int tiempoGracia = Integer.parseInt(configuracionCarreraService.buscarPorId(new ConfiguracionCarrera((int) 21)).getValor());
            ReporteMatricula reporte = sessionEstudianteCarrera.getReporteMatriculaUltimo();
            if (reporte.getNumeroModuloMatriculado() != null) {
                if (Integer.parseInt(reporte.getNumeroModuloMatriculado()) > Integer.parseInt(moduloMaxAprobado)) {
                    Calendar fechaActual = Calendar.getInstance();
                    Double tiempo = cabeceraController.getUtilService().calculaDuracion(reporte.getOfertaAcademicaId().getFechaFin(), fechaActual.getTime(), 0);
                    if (tiempo >= 0.0) {
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
                    estudianteCarreraService.actualizar(sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera());
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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedInformacionEstudio(Estudiante estudiante) {
        try {
            this.sessionEstudianteCarrera.setRenderedInformacionEstudio(false);
            for (EstudianteCarrera ec : estudianteCarreraService.buscar(new EstudianteCarrera(null, estudiante, Boolean.TRUE, null))) {
                if (ec.getEsActivo()) {
                    this.sessionEstudianteCarrera.setRenderedInformacionEstudio(true);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void renderedEditar() {
        int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "editar_estudiante_carrera");
        if (tienePermiso == 1) {
            sessionEstudianteCarrera.setRenderedEditar(true);
        } else {
            sessionEstudianteCarrera.setRenderedEditar(false);
        }
    }

    public void renderedCrear() {
        int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "crear_estudiante_carrera");
        sessionEstudianteCarrera.setRenderedEditar(false);
        if (tienePermiso == 1) {
            sessionEstudianteCarrera.setRenderedCrear(true);
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
            String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                    configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
            String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
            String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.PARALELOCARRERA.getTipo())).get(0).getValor();
            String ofertaIdActual = configuracionCarrera.getValor();
            String s = serviceUrl + "?id_oferta=" + ofertaIdActual + ";id_carrera=" + c.getIdSga();
            ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
            NetClientService conexion = new NetClientServiceImplement();
            String datosJson = conexion.response(seguridad);
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
        if (usuarioService.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.DATOSESTUDIANTE.getTipo())).get(0).getValor();
                String s = serviceUrl + "?cedula=" + sessionEstudianteCarrera.getEstudianteCarreraDTOWS().
                        getPersona().getNumeroIdentificacion();
                ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
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
                            Nacionalidad nacionalidad = nacionalidadDao.find(1);
                            sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().setNacionalidadId(nacionalidad);
                        }
                        if (sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getFechaNacimiento() == null) {
                            sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona()
                                    .setFechaNacimiento(fechaActual.getTime());
                        }
                        if (sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getId() == null) {
                            personaService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona());
                        } else {
                            personaService.actualizar(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona());
                        }
                        sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId()
                                .setId(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getPersona().getId());
                        estudianteService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId());
                        this.grabarUsuarioEstudiante(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId());
                        estudianteCarreraService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera());
                        sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getAspirante().
                                setId(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getId());
                        aspiranteService.guardar(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getAspirante());
                    } else {
                        Persona datosDocente = personaService.buscarPorId(
                                new Persona(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId().getId()));
                        personaService.actualizar(datosDocente);
                        estudianteService.actualizar(sessionEstudianteCarrera.getEstudianteCarreraDTOWS().getEstudianteCarrera().getEstudianteId());
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
        if (usuarioService.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_datos_docente") == 1) {
            try {
                String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.DATOSESTUDIANTE.getTipo())).get(0).getValor();
                String s = serviceUrl + "?cedula=" + sessionEstudianteCarrera.getEstudianteCarreraDTO().
                        getPersona().getNumeroIdentificacion();
                ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
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
                    estudianteCarreraDTO.getPersona().setFechaNacimiento(cabeceraController.getUtilService().parserFecha(valor.getAsString(), "yyyy-MM-dd"));
                    sessionEstudianteCarrera.setKeyEntero(sessionEstudianteCarrera.getKeyEntero() + 1);
                    return;
                }
                if (sessionEstudianteCarrera.getKeyEntero() == 7) {
                    Pais paisBuscar = new Pais();
                    paisBuscar.setNombre(valor.getAsString());
                    List<Pais> paises = paisDao.buscar(paisBuscar);
                    Pais pais = !paises.isEmpty() ? paises.get(0) : null;
                    if (pais != null) {
                        estudianteCarreraDTO.getPersona().setNacionalidadId(pais.getNacionalidadList().get(0));
                    } else {
                        Nacionalidad nacionalidad = nacionalidadDao.find(1);
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
        if (usuarioService.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_estudiante_carrera") == 1) {
            try {
                String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.MATRICULAESTUDIANTE.getTipo())).get(0).getValor();
                String s = serviceUrl + "?id_carrera=" + sessionEstudianteCarrera.getEstudianteCarreraDTO().getEstudianteCarrera().
                        getCarreraId().getIdSga() + ";cedula="
                        + sessionEstudianteCarrera.getEstudianteCarreraDTO().getPersona().getNumeroIdentificacion();
                ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
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
        if (usuarioService.tienePermiso(sessionUsuario.getUsuario(), "sga_ws_estudiante_carrera") == 1) {
            try {
                Persona p = personaService.buscarPorId(new Persona(rm.getEstudianteCarreraId().getEstudianteId().getId()));
                String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                        configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
                String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
                String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.REPORTEMATRICULAESTUDIANTE.getTipo())).get(0).getValor();
                String s = serviceUrl + "?cedula=" + p.getNumeroIdentificacion() + ";id_carrera="
                        + rm.getEstudianteCarreraId().getCarreraId().getIdSga() + ";id_oferta=" + rm.getOfertaAcademicaId().getIdSga();

                ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
                NetClientService conexion = new NetClientServiceImplement();
                String datosJson = conexion.response(seguridad);
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
            String passwordService = this.cabeceraController.getSecureService().decrypt(new Secure(cabeceraController.getConfiguracionGeneralUtil().getSecureKey(),
                    configuracionService.buscar(new Configuracion(ConfiguracionEnum.CLAVEWS.getTipo())).get(0).getValor()));
            String userService = configuracionService.buscar(new Configuracion(ConfiguracionEnum.USUARIOWS.getTipo())).get(0).getValor();
            String serviceUrl = configuracionService.buscar(new Configuracion(URLWSEnum.ESTADOESTUDIANTEPARALELO.getTipo())).get(0).getValor();
            String s = serviceUrl + "?id_paralelo=" + paraleloId;

            ConexionDTO seguridad = new ConexionDTO(passwordService, s, userService);
            NetClientService conexion = new NetClientServiceImplement();
            String datosJson = conexion.response(seguridad);
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
                            Persona persona = personaService.buscarPorNumeroIdentificacion(valor.getAsString());
                            Estudiante estudiante = null;
                            if (persona != null) {
                                estudiante = estudianteService.buscarPorId(new Estudiante(persona.getId()));
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
