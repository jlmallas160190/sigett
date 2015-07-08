/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.controlador;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.academico.managed.session.SessionCoordinadorPeriodo;
import edu.jlmallas.academico.entity.Coordinador;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import com.jlmallas.comun.entity.Persona;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import com.jlmallas.comun.service.PersonaService;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.service.CoordinadorPeriodoService;
import edu.jlmallas.academico.service.CoordinadorService;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.DocenteService;
import edu.jlmallas.academico.service.PeriodoCoordinacionService;
import edu.unl.sigett.academico.converter.DocenteCarreraDTOConverter;
import edu.unl.sigett.academico.dto.CoordinadorPeriodoDTO;
import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import java.util.ArrayList;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarCoordinador",
            pattern = "/editarCoordinador/#{sessionCoordinadorPeriodo.coordinadorPeriodoDTO.coordinadorPeriodo.id}",
            viewId = "/faces/pages/academico/coordinadores/editarCoordinadorPeriodo.xhtml"
    ),
    @URLMapping(
            id = "crearCoordinador",
            pattern = "/crearCoordinador/",
            viewId = "/faces/pages/academico/coordinadores/editarCoordinadorPeriodo.xhtml"
    ),
    @URLMapping(
            id = "coordinadores",
            pattern = "/coordinadores/",
            viewId = "/faces/pages/academico/coordinadores/index.xhtml"
    )})
public class AdministrarCoordinadoresPeriodos implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionCoordinadorPeriodo sessionCoordinadorPeriodo;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private SessionUsuario sessionUsuario;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/AcademicoService/CoordinadorPeriodoServiceImplement!edu.jlmallas.academico.service.CoordinadorPeriodoService")
    private CoordinadorPeriodoService coordinadorPeriodoService;
    @EJB(lookup = "java:global/AcademicoService/CoordinadorServiceImplement!edu.jlmallas.academico.service.CoordinadorService")
    private CoordinadorService coordinadorDao;
    @EJB(lookup = "java:global/AcademicoService/DocenteCarreraServiceImplement!edu.jlmallas.academico.service.DocenteCarreraService")
    private DocenteCarreraService docenteCarreraService;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/AcademicoService/PeriodoCoordinacionServiceImplement!edu.jlmallas.academico.service.PeriodoCoordinacionService")
    private PeriodoCoordinacionService periodoCoordinacionService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/AcademicoService/DocenteServiceImplement!edu.jlmallas.academico.service.DocenteService")
    private DocenteService docenteService;
    //</editor-fold>

    public AdministrarCoordinadoresPeriodos() {
    }

    public void init() {
        this.buscar();
        this.renderedCrear(sessionUsuario.getUsuario());
        this.renderedEditar(sessionUsuario.getUsuario());
        this.renderedEliminar(sessionUsuario.getUsuario());

    }

    public void initEditar() {
        this.listadoPeriodos();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String crear(Usuario usuario) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "crear_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionCoordinadorPeriodo.setCoordinadorPeriodoDTO(new CoordinadorPeriodoDTO(new CoordinadorPeriodo(
                        Boolean.FALSE, new Coordinador(Boolean.TRUE), null, null), new Persona(), null));
                if (param.equalsIgnoreCase("crear")) {
                    navegacion = "pretty:crearCoordinador";
                } else {
                    if (param.equalsIgnoreCase("crear-dlg")) {
                        RequestContext.getCurrentInstance().execute("PF('dlgEditarPeriodoCoordinacion').show()");
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String editar(Usuario usuario, CoordinadorPeriodoDTO coordinadorPeriodo) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "editar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                sessionCoordinadorPeriodo.setCoordinadorPeriodoDTO(coordinadorPeriodo);
                if (param.equalsIgnoreCase("editar")) {
                    navegacion = "pretty:editarCoordinador";
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navegacion;
    }

    /**
     * BUSCAR DOCENTES CARRERA PARA SELECCIONARLO COMO COORDINADOR DE CARRERA
     *
     * @param query
     * @return
     */
    public List<DocenteCarreraDTO> completarDocentesCarrera(final String query) {
        List<DocenteCarreraDTO> results = new ArrayList<>();
        List<DocenteCarrera> docentesCarrera = new ArrayList<>();
        List<Docente> docentes = new ArrayList<>();
        @SuppressWarnings("UnusedAssignment")
        List<Persona> personas = new ArrayList<>();
        if (!"".equals(query.trim())) {
            personas = personaService.buscar(new Persona(null, query.trim(), null, null, null, null));
            if (personas == null) {
                return new ArrayList<>();
            }
            for (Persona persona : personas) {
                Docente docente = docenteService.buscarPorId(new Docente(persona.getId()));
                if (docente == null) {
                    continue;
                }
                docentes.add(docente);
            }

            for (Docente docente : docentes) {
                List<DocenteCarrera> docenteCarreras = docenteCarreraService.buscar(new DocenteCarrera(null, docente,
                        sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), Boolean.TRUE));
                if (docenteCarreras == null) {
                    continue;
                }
                DocenteCarrera docenteCarrera = !docenteCarreras.isEmpty() ? docenteCarreras.get(0) : null;
                if (docenteCarrera == null) {
                    continue;
                }
                docentesCarrera.add(docenteCarrera);
            }
        }

        for (DocenteCarrera dc : docentesCarrera) {
            results.add(new DocenteCarreraDTO(dc, personaService.buscarPorId(new Persona(dc.getDocenteId().getId())), null));
        }
        DocenteCarreraDTOConverter.setDocenteCarreraDTOs(results);
        return results;
    }

    /**
     * BUSCAR COORDINADORES
     */
    public void buscar() {
        this.sessionCoordinadorPeriodo.getCoordinadorPeriodos().clear();
        this.sessionCoordinadorPeriodo.getFilterCoordinadorPeriodos().clear();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "buscar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                List<CoordinadorPeriodo> coordinadorPeriodos = coordinadorPeriodoService.buscar(new CoordinadorPeriodo(
                        null, null, null, sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera()));
                if (coordinadorPeriodos == null) {
                    return;
                }
                for (CoordinadorPeriodo coordinadorPeriodo : coordinadorPeriodos) {
                    CoordinadorPeriodoDTO coordinadorPeriodoDTO = new CoordinadorPeriodoDTO(coordinadorPeriodo,
                            personaService.buscarPorId(new Persona(coordinadorPeriodo.getCoordinadorId().getId())), null);
                    sessionCoordinadorPeriodo.getCoordinadorPeriodos().add(coordinadorPeriodoDTO);
                }
                this.sessionCoordinadorPeriodo.setFilterCoordinadorPeriodos(sessionCoordinadorPeriodo.getCoordinadorPeriodos());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void listadoPeriodos() {
        sessionCoordinadorPeriodo.getCoordinadorPeriodos().clear();
        List<PeriodoCoordinacion> periodoCoordinacions = periodoCoordinacionService.buscar(new PeriodoCoordinacion(
                sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), Boolean.TRUE));
        if (periodoCoordinacions == null) {
            return;
        }
        sessionCoordinadorPeriodo.setPeriodosCoordinacion(periodoCoordinacions);
    }

    /**
     * ENCARGAR COORDINACIÓN
     *
     * @param coordinadorPeriodoDTO
     * @param usuario
     */
    public void encargarCoordinacion(CoordinadorPeriodoDTO coordinadorPeriodoDTO, Usuario usuario) {
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "editar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                if (coordinadorPeriodoDTO.getCoordinadorPeriodo().getId() != null) {
                    if (coordinadorPeriodoDTO.getCoordinadorPeriodo().getEsVigente()) {
                        List<CoordinadorPeriodo> coordinadorPeriodos = coordinadorPeriodoService.buscar(new CoordinadorPeriodo(
                                Boolean.TRUE, null, new PeriodoCoordinacion(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera(), null), null));
                        for (CoordinadorPeriodo c : coordinadorPeriodos) {
                            if (!c.equals(coordinadorPeriodoDTO.getCoordinadorPeriodo())) {
                                c.setEsVigente(Boolean.FALSE);
                                coordinadorPeriodoService.editar(c);
                            }
                        }
                    }
                    coordinadorPeriodoService.editar(coordinadorPeriodoDTO.getCoordinadorPeriodo());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String grabar(Usuario usuario) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
        int pos = sessionCoordinadorPeriodo.getPeriodoCoordinacion().indexOf(":");
        PeriodoCoordinacion pc = periodoCoordinacionService.buscarPorId(new PeriodoCoordinacion(Long.parseLong(sessionCoordinadorPeriodo.getPeriodoCoordinacion().substring(0, pos))));
        if (pc != null) {
            sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getCoordinadorPeriodo().setPeriodoId(pc);
        }
        try {
            if (sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getCoordinadorPeriodo().getId() == null) {
                int tienePermiso = usuarioService.tienePermiso(usuario, "crear_coordinador_periodo_coordinacion");
                if (tienePermiso == 1) {
                    if (sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getPersona() != null) {
                        sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getCoordinadorPeriodo().getCoordinadorId().setId(
                                sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getPersona().getId());
                        coordinadorDao.guardar(sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getCoordinadorPeriodo().getCoordinadorId());
                        coordinadorPeriodoService.guardar(sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getCoordinadorPeriodo());
                        if (param.equalsIgnoreCase("grabar")) {
                            sessionCoordinadorPeriodo.setCoordinadorPeriodoDTO(new CoordinadorPeriodoDTO());
                            return "pretty:coordinadores";
                        }
                        if (param.equalsIgnoreCase("grabar-editar")) {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.coordinador") + " "
                                    + bundle.getString("lbl.msm_grabar"), "");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            return "";
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.coordinador") + " " + bundle.getString("lbl.no_encontrado"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    return "";
                }
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return "";
            }

            int tienePermiso = usuarioService.tienePermiso(usuario, "editar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                if (sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getPersona() != null) {
                    coordinadorDao.actualizar(sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getCoordinadorPeriodo().getCoordinadorId());
                    coordinadorPeriodoService.editar(sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().getCoordinadorPeriodo());
                    if (param.equalsIgnoreCase("grabar")) {
                        sessionCoordinadorPeriodo.setCoordinadorPeriodoDTO(new CoordinadorPeriodoDTO());
                        return "pretty:coordinadores";
                    }
                    if (param.equalsIgnoreCase("grabar-editar")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.coordinador") + " "
                                + bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                        return "";
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.coordinador") + ". " + bundle.getString("lbl.no_encontrado"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                return "";
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return "";
    }

    /**
     * REMOVER
     *
     * @param coordinadorPeriodoDTO
     * @param usuario
     */
    public void remover(CoordinadorPeriodoDTO coordinadorPeriodoDTO, Usuario usuario) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(usuario, "eliminar_coordinador_periodo_coordinacion");
            if (tienePermiso == 1) {
                coordinadorPeriodoService.eliminar(coordinadorPeriodoDTO.getCoordinadorPeriodo());
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.coordinador") + " "
                        + bundle.getString("lbl.msm_eliminar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                if (tienePermiso == 2) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". "
                            + bundle.getString("lbl.msm_consulte"), "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * AGREGAR COORDINADOR
     *
     */
    public void agregarCoordinador() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (sessionCoordinadorPeriodo.getDocenteCarreraDTOSeleccionado().getPersona() != null) {
                sessionCoordinadorPeriodo.getCoordinadorPeriodoDTO().setPersona(sessionCoordinadorPeriodo.getDocenteCarreraDTOSeleccionado().getPersona());
                return;
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " "
                    + bundle.getString("lbl.no_encontrado"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public void renderedCrear(Usuario usuario) {
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "crear_coordinador_periodo_coordinacion");
            sessionCoordinadorPeriodo.setRenderedCrear(Boolean.FALSE);
            if (tienePermiso == 1) {
                sessionCoordinadorPeriodo.setRenderedCrear(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }

    public void renderedEditar(Usuario usuario) {
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "editar_coordinador_periodo_coordinacion");
            sessionCoordinadorPeriodo.setRenderedEditar(Boolean.FALSE);
            if (tienePermiso == 1) {
                sessionCoordinadorPeriodo.setRenderedEditar(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }

    public void renderedEliminar(Usuario usuario) {
        try {
            int tienePermiso = usuarioService.tienePermiso(usuario, "eliminar_coordinador_periodo_coordinacion");
            sessionCoordinadorPeriodo.setRenderedEliminar(Boolean.FALSE);
            if (tienePermiso == 1) {
                sessionCoordinadorPeriodo.setRenderedEliminar(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionCoordinadorPeriodo getSessionCoordinadorPeriodo() {
        return sessionCoordinadorPeriodo;
    }

    public void setSessionCoordinadorPeriodo(SessionCoordinadorPeriodo sessionCoordinadorPeriodo) {
        this.sessionCoordinadorPeriodo = sessionCoordinadorPeriodo;
    }

//</editor-fold>
}
