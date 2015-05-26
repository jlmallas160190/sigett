/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.DirectorDao;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import edu.unl.sigett.entity.OficioCarrera;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.enumeration.CatalogoOficioEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.DocenteProyectoService;
import edu.unl.sigett.service.OficioCarreraService;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.entity.Usuario;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "docenteProyectoController")
@SessionScoped
public class DocenteProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionDocenteProyecto sessionDocenteProyecto;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private CabeceraController cabeceraController;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralDao;
    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteDao;
    @EJB
    private DirectorDao directorDao;
    @EJB
    private PersonaDao personaDao;
    @EJB
    private OficioCarreraService oficioCarreraService;
    @EJB
    private ItemService itemService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DocenteProyectoController.class.getName());

    public DocenteProyectoController() {
    }

    public void init() {
        this.renderedBuscar();
        this.renderedBuscarEspecialista();
        this.renderedEliminar();
        this.renderedImprimirOficio();
        this.renderedSeleccionarDocenteEspecialista();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    public void renderedBuscar() {
        sessionDocenteProyecto.setRenderedBuscar(Boolean.FALSE);
        int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "buscar_docente_proyecto");
        if (tienePermiso == 1) {
            sessionDocenteProyecto.setRenderedBuscar(Boolean.TRUE);
        }
    }

    public void renderedImprimirOficio() {
        try {
            sessionDocenteProyecto.setRenderedImprimiOficio(Boolean.FALSE);
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "imprimir_docente_proyecto");
            if (tienePermiso == 1) {
                sessionDocenteProyecto.setRenderedImprimiOficio(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }

    public void renderedSeleccionarDocenteEspecialista() {
        try {
            sessionDocenteProyecto.setRenderedSeleccionarEspecialista(Boolean.FALSE);
            if (sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "select_docente_especialista");
                if (tienePermiso == 1 && permitirAgregarDocente()) {
                    sessionDocenteProyecto.setRenderedSeleccionarEspecialista(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
        }

    }

    public void renderedBuscarEspecialista() {
        try {
            sessionDocenteProyecto.setRenderedBuscarEspecialista(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "buscar_docente_especialista");
            if (tienePermiso == 1) {
                sessionDocenteProyecto.setRenderedBuscarEspecialista(Boolean.TRUE);
            }
        } catch (Exception e) {
        }

    }

    public void renderedEliminar() {
        try {
            sessionDocenteProyecto.setRenderedEliminar(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            int tienePermiso = usuarioDao.tienePermiso(sessionUsuario.getUsuario(), "eliminar_docente_proyecto");
            if (tienePermiso == 1) {
                sessionDocenteProyecto.setRenderedEliminar(Boolean.TRUE);
            }

        } catch (Exception e) {
        }

    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="POSTULACIÓN">

    /**
     * IMPRIMIR OFICIO AL DOCENTE NOTIFICANDO SU DESIGNACIÓN PARA DAR
     * PERTIENENCIA AL PROYECTO SELECCIONADO.
     *
     * @param docenteProyectoDTO
     */
    public void imprimirOficioDocenteProyecto(DocenteProyectoDTO docenteProyectoDTO) {
        try {
            if (docenteProyectoDTO.getDocenteProyecto().getId() != null) {
                sessionDocenteProyecto.setDocenteProyectoId(docenteProyectoDTO.getDocenteProyecto().getDocenteId());
                Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(), CatalogoOficioEnum.DOCENTEPROYECTO.getTipo());
                for (ProyectoCarreraOferta pco : docenteProyectoDTO.getDocenteProyecto().getProyectoId().getProyectoCarreraOfertaList()) {
                    if (pco.getEsActivo()) {
                        sessionDocenteProyecto.setCarreraId(pco.getId());
                        break;
                    }
                }
                OficioCarrera oficioCarrera = oficioCarreraService.buscar(new OficioCarrera(
                        item.getId(), null, null, null, Boolean.TRUE, Integer.MIN_VALUE, docenteProyectoDTO.getDocenteProyecto().getId())).get(0);

                if (oficioCarrera != null) {
                    sessionDocenteProyecto.setOficioCarrera(oficioCarrera);
                } else {
                    sessionDocenteProyecto.setOficioCarrera(new OficioCarrera());
                }
                sessionDocenteProyecto.setRenderedDialogoOficio(Boolean.TRUE);
                RequestContext.getCurrentInstance().execute("PF('dlgOficioDocenteProyecto').show()");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean permitirAgregarDocente() {
        boolean var = false;
        String val = configuracionGeneralDao.find((int) 19).getValor();
        if (sessionProyecto.getDocentesProyectoDTO().isEmpty()) {
            return false;
        }
        for (DocenteProyectoDTO docenteProyectoDTO : sessionProyecto.getDocentesProyectoDTO()) {
            if (docenteProyectoDTO.getDocenteProyecto().getEsActivo()) {
                if (val.equalsIgnoreCase("SI")) {
                    var = true;
                } else {
                    var = false;
                    break;
                }
            } else {
                var = true;
            }
        }
        return var;
    }

    /**
     * LISTADO DE DIRECTORES DISPONIBLES DE ACUERDO A LAS LINEAS DE
     * INVESTIGACIÓN SELECCIONADAS EN EL PROYECTO.
     */
    public void listadoDirectoresDiponibles() {
        try {
            this.sessionDocenteProyecto.getDirectoresDTO().clear();
            this.sessionDocenteProyecto.getFilterDirectoresDTO().clear();
            for (DocenteCarreraDTO docenteCarreraDTO : sessionUsuarioCarrera.getDocentesCarreraDTO()) {
                List<LineaInvestigacionDocente> lineaInvestigacionDocentes = lineaInvestigacionDocenteDao.buscar(
                        new LineaInvestigacionDocente(docenteCarreraDTO.getDocenteCarrera().getDocenteId().getId(), null));
                if (lineaInvestigacionDocentes.isEmpty()) {
                    continue;
                }
                for (LineaInvestigacion lineaInvestigacion :sessionProyecto.getLineasInvestigacionSeleccionadas()) {
                    for (LineaInvestigacionDocente lid : lineaInvestigacionDocentes) {
                        if (lineaInvestigacion.equals(lid.getLineaInvestigacionId())) {
                            DirectorDTO directorDTO = new DirectorDTO(directorDao.find(
                                    docenteCarreraDTO.getDocenteCarrera().getId()), docenteCarreraDTO.getDocenteCarrera(), personaDao.find(
                                            docenteCarreraDTO.getDocenteCarrera().getDocenteId().getId()));
                            sessionDocenteProyecto.getDirectoresDTO().add(directorDTO);
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * AGREGAR DIRECTOR COMO DOCENTE DEL PROYECTO SELECCIONADO
     *
     * @param directorDTO
     */
    public void agregarDocenteProyecto(DirectorDTO directorDTO) {
        Calendar fecha = Calendar.getInstance();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_crear") + ". "
                        + bundle.getString("lbl.msm_consulte"), "");
                return;
            }
            if (permitirAgregarDocente()) {
                DocenteProyectoDTO dp = devuelveDocenteProyecto(directorDTO);
                if (dp == null) {
                    DocenteProyecto docenteProyecto = new DocenteProyecto(sessionProyecto.getProyectoSeleccionado(), fecha.getTime(),
                            directorDTO.getDocenteCarrera().getDocenteId().getId(), Boolean.TRUE);
                    DocenteProyectoDTO docenteProyectoDTO = new DocenteProyectoDTO(docenteProyecto, directorDTO.getPersona(), directorDTO.getDirector(),
                            directorDTO.getDocenteCarrera());
                    sessionProyecto.getDocentesProyectoDTO().add(docenteProyectoDTO);
                    RequestContext.getCurrentInstance().execute("PF('dlgBuscarDocentesDisponibles').hide()");
                    cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.docente") + " "
                            + bundle.getString("lbl.msm_agregar"), "");
                }
            } else {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe") + " "
                        + bundle.getString("lbl.docente"), "");
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private DocenteProyectoDTO devuelveDocenteProyecto(DirectorDTO directorDTO) {
        DocenteProyectoDTO dTO = null;
        try {
            for (DocenteProyectoDTO docenteProyectoDTO : sessionProyecto.getDocentesProyectoDTO()) {
                if (docenteProyectoDTO.getDocenteCarrera().getDocenteId().equals(directorDTO.getDocenteCarrera().getDocenteId())) {
                    dTO = docenteProyectoDTO;
                    break;
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return dTO;
    }

    public void removerDocenteProyecto(DocenteProyectoDTO docenteProyectoDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
                return;
            }
            if (docenteProyectoDTO.getDocenteProyecto().getId() != null) {
                docenteProyectoDTO.getDocenteProyecto().setEsActivo(Boolean.FALSE);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                sessionProyecto.getDocentesProyectoDTO().remove(docenteProyectoDTO);
            } else {
                sessionProyecto.getDocentesProyectoDTO().remove(docenteProyectoDTO);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
            }
            sessionProyecto.setFilterDocentesProyectoDTO(sessionProyecto.getDocentesProyectoDTO());
        } catch (Exception e) {
        }
    }
    //</editor-fold>
}