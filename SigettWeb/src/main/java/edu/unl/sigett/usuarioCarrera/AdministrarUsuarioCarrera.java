/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.usuarioCarrera;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.CarreraService;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import edu.unl.sigett.academico.dto.EstudianteCarreraDTO;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import org.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.UsuarioCarrera;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.Director;
import edu.unl.sigett.service.AspiranteService;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.DirectorService;
import org.jlmallas.seguridad.service.UsuarioService;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarUsuarioCarrera",
            pattern = "/editarUsuarioCarrera/#{sessionUsuarioCarrera.usuarioCarreraDTO.carrera.id}",
            viewId = "/faces/pages/sigett/usuarioCarrera/editarUsuarioCarrera.xhtml"
    ),
    @URLMapping(
            id = "carrerasUsuario",
            pattern = "/carrerasUsuario/",
            viewId = "/faces/faces/pages/sigett/buscarCarrerasUsuario.xhtml"
    )})
public class AdministrarUsuarioCarrera implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE MANAGED BEANS">
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private SessionUsuario sessionUsuario;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INYECCIÓN DE SERVICIOS">
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/SigettService/UsuarioCarreraDaoImplement!edu.unl.sigett.dao.UsuarioCarreraDao")
    private UsuarioCarreraDao usuarioCarreraDao;
    @EJB(lookup = "java:global/ComunService/CarreraService Implement!com.jlmallas.comun.service.CarreraService ")
    private CarreraService carreraService;
     @EJB(lookup = "java:global/SigettService/ConfiguracionCarreraServiceImplement!edu.unl.sigett.service.ConfiguracionCarreraService")
    private ConfiguracionCarreraService configuracionCarreraService;
   @EJB(lookup = "java:global/SigettService/EstudianteCarreraServiceImplement!edu.unl.sigett.service.EstudianteCarreraService")
    private EstudianteCarreraService estudianteCarreraService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
  @EJB(lookup = "java:global/SigettService/AspiranteServiceImplement!edu.unl.sigett.service.AspiranteService")
    private AspiranteService aspiranteService;
    @EJB(lookup = "java:global/AcademicoService/DocenteCarreraServiceImplement!edu.jlmallas.academico.service.DocenteCarreraService")
    private DocenteCarreraService docenteCarreraService;
   @EJB(lookup = "java:global/SigettService/DirectorServiceImplement!edu.unl.sigett.service.DirectorService")
    private DirectorService directorService;
    //</editor-fold>

    public AdministrarUsuarioCarrera() {
    }

    public void init() {
        buscar();
        this.listadoCarreras();
        this.listasDocentes();
        this.listasEstudiantes();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedEditar() {
        int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "editar_usuario_carrera");
        if (tienePermiso == 1) {
            sessionUsuarioCarrera.setRenderedEditar(true);
            sessionUsuarioCarrera.setRenderedEditar(false);
            return;
        }
        sessionUsuarioCarrera.setRenderedEditar(false);
        sessionUsuarioCarrera.setRenderedEditar(true);
    }

    public void renderedBuscar(Usuario usuario) {
        int tienePermiso = usuarioService.tienePermiso(usuario, "buscar_docente");
        if (tienePermiso == 1) {
            sessionUsuarioCarrera.setRenderedBuscar(true);
            return;
        }
        sessionUsuarioCarrera.setRenderedBuscar(false);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">

    public String editar(UsuarioCarreraDTO usuarioCarreraAux) {
        String navegacion = "";
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "editar_usuario_carrera");
            if (tienePermiso == 1) {
                usuarioCarreraAux.setCarrera(carreraService.find(usuarioCarreraAux.getCarrera().getId()));
                sessionUsuarioCarrera.setUsuarioCarreraDTO(usuarioCarreraAux);
                navegacion = "pretty:editarUsuarioCarrera";
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

    public void buscar() {
        try {
            sessionUsuarioCarrera.getUsuarioCarrerasDTOS().clear();
            sessionUsuarioCarrera.getFilterUsuarioCarrerasDTO().clear();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "buscar_usuario_carrera");
            if (tienePermiso == 1) {
                UsuarioCarrera usuarioCarrera = new UsuarioCarrera();
                usuarioCarrera.setUsuarioId(sessionUsuario.getUsuario().getId());
                List<UsuarioCarrera> usuarioCarreras = usuarioCarreraDao.buscar(usuarioCarrera);
                if (usuarioCarreras == null) {
                    return;
                }
                for (UsuarioCarrera uc : usuarioCarreras) {
                    UsuarioCarreraDTO usuarioCarreraAux = new UsuarioCarreraDTO(uc, usuarioService.buscarPorId(new Usuario(uc.getUsuarioId())),
                            carreraService.find(uc.getCarreraId()));
                    sessionUsuarioCarrera.getUsuarioCarrerasDTOS().add(usuarioCarreraAux);
                }
                sessionUsuarioCarrera.setFilterUsuarioCarrerasDTO(sessionUsuarioCarrera.getUsuarioCarrerasDTOS());
                return;
            }
            if (tienePermiso == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_buscar") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * GRABAR CARRERA
     *
     */
    public void grabarCarrera() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getIdSga() == null) {
                sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().setIdSga("");
            }
            if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(
                    sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId(), "MA")) == null) {
                ConfiguracionCarrera configuracionCarrera1 = new ConfiguracionCarrera();
                configuracionCarrera1.setNombre("Número de Módulo Aprobado por el estudiante para ser Apto a realizar un trabajo de titulación");
                configuracionCarrera1.setCodigo("MA");
                configuracionCarrera1.setValor("1");
                configuracionCarrera1.setObservacion("S/N");
                configuracionCarrera1.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId());
                configuracionCarrera1.setTipo("numerico");
                configuracionCarreraService.guardar(configuracionCarrera1);
            }
            if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(
                    sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId(), "ME")) == null) {
                ConfiguracionCarrera configuracionCarrera2 = new ConfiguracionCarrera();
                configuracionCarrera2.setNombre("Número de Modulo aprobado para ser egresado");
                configuracionCarrera2.setCodigo("ME");
                configuracionCarrera2.setValor("1");
                configuracionCarrera2.setObservacion("S/N");
                configuracionCarrera2.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId());
                configuracionCarrera2.setTipo("numerico");
                configuracionCarreraService.guardar(configuracionCarrera2);
            }
            if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(
                    sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId(), "OA")) == null) {
                ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera();
                configuracionCarrera.setNombre("ID de Oferta Academica Actual de la Carrera");
                configuracionCarrera.setCodigo("OA");
                configuracionCarrera.setValor("1");
                configuracionCarrera.setObservacion("S/N");
                configuracionCarrera.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId());
                configuracionCarrera.setTipo("boton");
                configuracionCarreraService.guardar(configuracionCarrera);
            }
            if (configuracionCarreraService.buscarPrimero(new ConfiguracionCarrera(
                    sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId(), "NO")) == null) {
                ConfiguracionCarrera configuracionCarrera3 = new ConfiguracionCarrera();
                configuracionCarrera3.setNombre("Número de Oficio");
                configuracionCarrera3.setCodigo("NO");
                configuracionCarrera3.setValor("1");
                configuracionCarrera3.setObservacion("Número de Oficio");
                configuracionCarrera3.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId());
                configuracionCarrera3.setTipo("numerico");
                configuracionCarreraService.guardar(configuracionCarrera3);
            }
            if (configuracionCarreraService.buscar(new ConfiguracionCarrera(
                    sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId(), "NA")) == null) {
                ConfiguracionCarrera configuracionCarrera4 = new ConfiguracionCarrera();
                configuracionCarrera4.setNombre("Número de Acta de tesis");
                configuracionCarrera4.setCodigo("NA");
                configuracionCarrera4.setValor("1");
                configuracionCarrera4.setObservacion("S/N");
                configuracionCarrera4.setCarreraId(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getId());
                configuracionCarrera4.setTipo("numerico");
                configuracionCarreraService.guardar(configuracionCarrera4);
            }
            carreraService.edit(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.carrera") + " "
                    + bundle.getString("lbl.msm_editar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.buscar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SUBIR LOGO
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().setLogo(event.getFile().getContents());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //</editor-fold>
    /**
     * LISTAR ESTUDIANTES QUE PERTENECEN A LAS CARRERAS DE SESSION USUARIO
     */
    private void listasEstudiantes() {
        this.sessionUsuarioCarrera.getEstudiantesCarreraDTO().clear();
        for (Carrera carrera : sessionUsuarioCarrera.getCarreras()) {
            List<EstudianteCarrera> estudiantesCarreras = estudianteCarreraService.buscar(new EstudianteCarrera(carrera, null, null, null));
            if (estudiantesCarreras.isEmpty()) {
                continue;
            }
            for (EstudianteCarrera estudianteCarrera : estudiantesCarreras) {
                EstudianteCarreraDTO estudianteCarreraDTO = new EstudianteCarreraDTO(estudianteCarrera,
                        personaService.buscarPorId(new Persona(estudianteCarrera.getEstudianteId().getId())), aspiranteService.buscarPorId(estudianteCarrera.getId()));
                sessionUsuarioCarrera.getEstudiantesCarreraDTO().add(estudianteCarreraDTO);
            }
        }
    }

    /**
     * LISTAR DOCENTES QUE PERTENECEN A LAS CARRERAS DE SESSION USUARIO
     */
    private void listasDocentes() {
        this.sessionUsuarioCarrera.getDocentesCarreraDTO().clear();
        for (Carrera carrera : sessionUsuarioCarrera.getCarreras()) {
            List<DocenteCarrera> docenteCarreras = docenteCarreraService.buscar(new DocenteCarrera(null, null, carrera, null));
            if (docenteCarreras.isEmpty()) {
                continue;
            }
            for (DocenteCarrera docenteCarrera : docenteCarreras) {
                DocenteCarreraDTO docenteCarreraDTO = new DocenteCarreraDTO(docenteCarrera,
                        personaService.buscarPorId(new Persona(docenteCarrera.getDocenteId().getId())), directorService.buscarPorId(new Director(docenteCarrera.getId())));
                sessionUsuarioCarrera.getDocentesCarreraDTO().add(docenteCarreraDTO);
            }
        }
    }

    /**
     * LISTAR LAS CARRERAS QUE ADMINISTRA LA SESSION DE USUARIO
     */
    private void listadoCarreras() {
        this.sessionUsuarioCarrera.getCarreras().clear();
        List<UsuarioCarrera> usuarioCarreras = usuarioCarreraDao.buscar(new UsuarioCarrera(sessionUsuario.getUsuario().getId(), null));
        if (usuarioCarreras.isEmpty()) {
            return;
        }
        for (UsuarioCarrera usuarioCarrera : usuarioCarreras) {
            sessionUsuarioCarrera.getCarreras().add(carreraService.find(usuarioCarrera.getCarreraId()));
        }
    }
}
