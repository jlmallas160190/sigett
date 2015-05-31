/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.academico.dto.DocenteCarreraDTO;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.DirectorDao;
import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.DocumentoCarrera;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import edu.unl.sigett.enumeration.CatalogoDocumentoCarreraEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.service.DocumentoCarreraService;
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
    private DocumentoCarreraService documentoCarreraService;
    @EJB
    private ItemService itemService;
    @EJB
    private DocumentoService documentoService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DocenteProyectoController.class.getName());
    
    public DocenteProyectoController() {
    }
    
    public void init() {
//        this.iniciarPermisos();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    private void iniciarPermisos() {
        sessionDocenteProyecto.setRenderedBuscar(cabeceraController.getPermisoAdministrarProyecto().getRenderedBuscarDocenteProyecto());
        sessionDocenteProyecto.setRenderedBuscarEspecialista(cabeceraController.getPermisoAdministrarProyecto().getRenderedBuscarEspecialista());
        sessionDocenteProyecto.setRenderedEliminar(cabeceraController.getPermisoAdministrarProyecto().getRenderedEliminarDocenteProyecto());
        sessionDocenteProyecto.setRenderedImprimirOficio(cabeceraController.getPermisoAdministrarProyecto().getRenderedImprimirOficioDocenteProyecto());
        sessionDocenteProyecto.setRenderedSeleccionarEspecialista(cabeceraController.getPermisoAdministrarProyecto().getRenderedSeleccionarEspecialista());
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
                Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGOOFICIO.getTipo(),
                        CatalogoDocumentoCarreraEnum.DOCENTEPROYECTO.getTipo());
                Documento documentoBuscar = documentoService.buscar(new Documento(null, null, item.getId(), null, null, null, null));
                DocumentoCarrera documentoCarrera = documentoCarreraService.buscar(new DocumentoCarrera(
                        null, documentoBuscar.getId(), Boolean.TRUE, Integer.MIN_VALUE, sessionDocenteProyecto.getDocenteProyectoId())).get(0);
                sessionDocenteProyecto.setOficioPertinenciaDTO(new OficioPertinenciaDTO(new DocumentoCarrera(), new Documento(), sessionProyecto.getCarreras().get(0)));
                if (documentoCarrera != null) {
                    
                    sessionDocenteProyecto.setOficioPertinenciaDTO(new OficioPertinenciaDTO(
                            documentoCarrera, documentoService.buscarPorId(new Documento(documentoCarrera.getDocumentoId())), sessionProyecto.getCarreras().get(0)));
                }
                sessionDocenteProyecto.setRenderedDialogoOficio(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }
    
    public void cancelarImprimirOficio() {
        sessionDocenteProyecto.setRenderedDialogoOficio(Boolean.FALSE);
    }

    /**
     * DETERMINAR SI SE PUEDEN AGREGAR MAS DE 2 DOCENTES AL PROYECTO
     * SELECCIONADO
     *
     * @return
     */
    public boolean permitirAgregarDocente() {
        boolean var = false;
        String val = configuracionGeneralDao.find((int) 19).getValor();
        if (sessionProyecto.getDocentesProyectoDTO().isEmpty()) {
            return true;
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
                for (LineaInvestigacion lineaInvestigacion : sessionProyecto.getLineasInvestigacionSeleccionadas()) {
                    for (LineaInvestigacionDocente lid : lineaInvestigacionDocentes) {
                        if (lineaInvestigacion.equals(lid.getLineaInvestigacionId())) {
                            DirectorDTO directorDTO = new DirectorDTO(directorDao.find(
                                    docenteCarreraDTO.getDocenteCarrera().getId()), docenteCarreraDTO.getDocenteCarrera(), personaDao.find(
                                            docenteCarreraDTO.getDocenteCarrera().getDocenteId().getId()));
                            if (!sessionDocenteProyecto.getDirectoresDTO().contains(directorDTO)) {
                                sessionDocenteProyecto.getDirectoresDTO().add(directorDTO);
                            }
                        }
                    }
                }
            }
            sessionDocenteProyecto.setFilterDirectoresDTO(sessionDocenteProyecto.getDirectoresDTO());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * AGREGAR DIRECTOR COMO DOCENTE DEL PROYECTO SELECCIONADO
     *
     * @param directorDTO
     */
    public void agregar(DirectorDTO directorDTO) {
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
    
    public void remover(DocenteProyectoDTO docenteProyectoDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.INICIO.getTipo())) {
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
