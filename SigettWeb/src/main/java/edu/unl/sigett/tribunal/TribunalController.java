/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.tribunal;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import edu.jlmallas.academico.dao.ReporteMatriculaDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.entity.ReporteMatricula;
import edu.jlmallas.academico.enumeration.EstadoEstudianteCarreraEnum;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.Tribunal;
import edu.unl.sigett.enumeration.ConfiguracionCarreraEnum;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.TribunalService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.service.UsuarioService;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "tribunalController")
@SessionScoped
public class TribunalController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionTribunal sessionTribunal;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionUsuario sessionUsuario;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SigettService/AutorProyectoServiceImplement!edu.unl.sigett.service.AutorProyectoService")
    private AutorProyectoService autorProyectoService;
    @EJB(lookup = "java:global/AcademicoService/ReporteMatriculaDaoImplement!edu.jlmallas.academico.dao.ReporteMatriculaDao")
    private ReporteMatriculaDao reporteMatriculaDao;
    @EJB(lookup = "java:global/SigettService/TribunalServiceImplement!edu.unl.sigett.service.TribunalService")
    private TribunalService tribunalService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/AcademicoService/EstudianteCarreraServiceImplement!edu.jlmallas.academico.service.EstudianteCarreraService")
    private EstudianteCarreraService estudianteCarreraService;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logDao;
    @EJB(lookup = "java:global/SigettService/ConfiguracionCarreraServiceImplement!edu.unl.sigett.service.ConfiguracionCarreraService")
    private ConfiguracionCarreraService configuracionCarreraService;
    @EJB(lookup = "java:global/AcademicoService/OfertaAcademicaServiceImplement!edu.jlmallas.academico.service.OfertaAcademicaService")
    private OfertaAcademicaService ofertaAcademicaService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(TribunalController.class.getName());

    public TribunalController() {
    }

    public void preRenderView() {
        buscar();
        renderedBuscar();
        renderedCrear();
        renderedEditar();
        renderedEliminar();
    }
    //<editor-fold defaultstate="collapsed" desc="CRUD">
    /**
     * COMPROBAR SI LOS AUTORES DEL PROYECTO SELECCIONADO SON APTOS A DAR
     * SUSTÉNTACIÓN DE TRABAJO DE TITULACIÓN DE ACUERDO A LOS SIGUIENTE: QUE EL
     * AUTOR NO HAYA RENUNCIADO QUE EL AUTOR SEA EGRESADO
     *
     * @return
     */
    public Boolean comprobarAutoresAptosSustentacion() {
        try {
            String moduloEgresado = configuracionCarreraService.buscar(new ConfiguracionCarrera(sessionProyecto.getCarreraSeleccionada().getId(), "ME")).get(0).getValor();
            Item estadoAutorRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo());
            Item estadoEstudianteEgresado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOESTUDIANTECARRERA.getTipo(),
                    EstadoEstudianteCarreraEnum.EGRESADO.getTipo());
            AutorProyecto autorProyectoBuscar = new AutorProyecto();
            autorProyectoBuscar.setProyectoId(sessionProyecto.getProyectoSeleccionado());
            List<AutorProyecto> autorProyectos = autorProyectoService.buscar(autorProyectoBuscar);
            ConfiguracionCarrera configuracionCarrera = configuracionCarreraService.buscarPrimero(
                    new ConfiguracionCarrera(sessionProyecto.getCarreraSeleccionada().getId(), ConfiguracionCarreraEnum.OFERTAACADEMICA.getTipo()));
            OfertaAcademica ofertaAcademicaActual = ofertaAcademicaService.find(Long.parseLong(configuracionCarrera.getValor()));
            for (AutorProyecto autorProyecto : autorProyectos) {
                EstudianteCarrera estudianteCarrera = estudianteCarreraService.buscarPorId(new EstudianteCarrera(autorProyecto.getAspiranteId().getId()));
                if (!autorProyecto.getEstadoAutorId().equals(estadoAutorRenunciado.getId())) {
                    return Boolean.TRUE;
                }
                ReporteMatricula rm = reporteMatriculaDao.buscarUltimaMatriculaEstudiante(estudianteCarrera.getId());
                if (estudianteCarrera.getEstadoId().equals(estadoEstudianteEgresado.getId())
                        || (Integer.parseInt(rm.getNumeroModuloMatriculado()) >= Integer.parseInt(moduloEgresado)
                        && rm.getOfertaAcademicaId().equals(ofertaAcademicaActual))) {
                    return Boolean.TRUE;
                }
            }
        } catch (NumberFormatException e) {
            LOG.warning(e.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * VERIFICAR SI EXITE OTRO TRIBUNAL PARA EL PROYECTO SELECCIONADO
     *
     * @return
     */
    public Boolean comprobarTribunalesActivos() {
        Tribunal tribunalBuscar = new Tribunal();
        tribunalBuscar.setProyectoId(sessionProyecto.getProyectoSeleccionado());
        tribunalBuscar.setEsActivo(Boolean.TRUE);
        List<Tribunal> tribunals = tribunalService.buscar(tribunalBuscar);
        if (!tribunals.isEmpty()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void crear() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (!comprobarAutoresAptosSustentacion()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("autor_no_apto"), "");
                return;
            }
            if (!comprobarTribunalesActivos()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
                return;
            }
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "crear_tribunal").trim());
            if (tienePermiso == 1) {
                sessionTribunal.setTribunal(new Tribunal());
                sessionTribunal.getTribunal().setProyectoId(sessionProyecto.getProyectoSeleccionado());
                sessionTribunal.setRenderedCrud(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgEditarTribunal').show()");
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void editar(Tribunal tribunal) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (!comprobarAutoresAptosSustentacion()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("autor_no_apto"), "");
                return;
            }
            if (!comprobarTribunalesActivos()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_editar"), "");
                return;
            }
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "editar_tribunal").trim());
            if (tienePermiso == 1) {
                sessionTribunal.setTribunal(tribunal);
                sessionTribunal.getTribunal().setProyectoId(sessionProyecto.getProyectoSeleccionado());
                sessionTribunal.setRenderedCrud(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgEditarTribunal').show()");
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void grabar(Tribunal tribunal) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("tipo-grabado");
        try {
            if (!comprobarAutoresAptosSustentacion()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("autor_no_apto"), "");
                return;
            }
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            if (tribunal.getId() == null) {
                Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.PERMISOS, "crear_tribunal").trim());
                if (tienePermiso == 1) {
                    tribunalService.guardar(tribunal);
                }
                logDao.create(logDao.crearLog("Tribunal", tribunal.getId() + "", "CREAR", "|Descripcion " + tribunal.getDescripcion() + "|Es Activo= " + tribunal.getEsActivo() + "|Proyecto= " + tribunal.getProyectoId().getId(), sessionUsuario.getUsuario()));
                if (param.equalsIgnoreCase("grabar")) {
                    RequestContext.getCurrentInstance().execute("PF('dlgEditarTribunal').hide()");
                    cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                    sessionTribunal.setTribunal(new Tribunal());
                    return;
                }
                if (param.equalsIgnoreCase("grabar-editar")) {
                    cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                    return;
                }
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "crear_tribunal").trim());
            if (tienePermiso == 1) {
                tribunalService.guardar(tribunal);
                tribunalService.actualizar(tribunal);
                logDao.create(logDao.crearLog("Tribunal", tribunal.getId() + "", "EDITAR", "|Descripcion " + tribunal.getDescripcion() + "|Es Activo= " + tribunal.getEsActivo() + "|Proyecto= " + tribunal.getProyectoId().getId(), sessionUsuario.getUsuario()));
                if (param.equalsIgnoreCase("grabar")) {
                    cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                    cancelarEdicion();
                    return;
                }
                if (param.equalsIgnoreCase("grabar-editar")) {
                    cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                }
                return;
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". " + bundle.getString("lbl.msm_consulte"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void remover(Tribunal tribunal) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
                return;
            }
            Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "eliminar_tribunal").trim());
            if (tienePermiso == 1) {
                tribunal.setEsActivo(Boolean.FALSE);
                tribunalService.actualizar(tribunal);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                return;
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("denegado_eliminar"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void cancelarEdicion() {
        sessionTribunal.setRenderedCrud(Boolean.FALSE);
        sessionTribunal.setTribunal(new Tribunal());
        RequestContext.getCurrentInstance().execute("PF('dlgEditarTribunal').hide()");
    }

    public void buscar() {
        try {
            sessionTribunal.getTribunales().clear();
            sessionTribunal.getFilterTribunales().clear();
            if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())
                    || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.APROBADO.getTipo())) {
                return;
            }
            Tribunal tribunalBuscar = new Tribunal();
            tribunalBuscar.setProyectoId(sessionProyecto.getProyectoSeleccionado());
            tribunalBuscar.setEsActivo(Boolean.TRUE);
            sessionTribunal.setTribunales(tribunalService.buscar(tribunalBuscar));
            sessionTribunal.setFilterTribunales(sessionTribunal.getTribunales());
        } catch (Exception e) {
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RENDERED">

    public void renderedCrear() {
        sessionTribunal.setRenderedCrear(Boolean.FALSE);
        if (!comprobarAutoresAptosSustentacion()) {
            return;
        }
        if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
            return;
        }
        Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                PropertiesFileEnum.PERMISOS, "crear_tribunal").trim());
        if (tienePermiso == 1) {
            sessionTribunal.setRenderedCrear(Boolean.TRUE);
        }

    }

    public void renderedBuscar() {
        sessionTribunal.setRenderedBuscar(Boolean.FALSE);
        if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
            return;
        }
        Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                PropertiesFileEnum.PERMISOS, "buscar_tribunal").trim());
        if (tienePermiso == 1) {
            sessionTribunal.setRenderedBuscar(Boolean.TRUE);
        }
    }

    public void renderedEditar() {
        sessionTribunal.setRenderedEditar(Boolean.FALSE);
        if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
            return;
        }
        Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                PropertiesFileEnum.PERMISOS, "editar_tribunal").trim());
        if (tienePermiso == 1) {
            sessionTribunal.setRenderedEditar(Boolean.TRUE);
        }
    }

    public void renderedEliminar() {
        sessionTribunal.setRenderedEliminar(Boolean.FALSE);
        if (!(sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.SUSTENTACIONPUBLICA.getTipo()))
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPRIVADA.getTipo())
                || sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.RECUPERACIONPUBLICA.getTipo())) {
            return;
        }
        Integer tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                PropertiesFileEnum.PERMISOS, "eliminar_tribunal").trim());
        if (tienePermiso == 1) {
            sessionTribunal.setRenderedEliminar(Boolean.TRUE);
        }
    }
    //</editor-fold>
}
