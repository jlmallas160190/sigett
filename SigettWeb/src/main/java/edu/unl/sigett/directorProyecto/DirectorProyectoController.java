/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import edu.unl.sigett.director.DirectorDTO;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.director.DirectorDM;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.Renuncia;
import edu.unl.sigett.entity.RenunciaDirector;
import edu.unl.sigett.enumeration.EstadoDirectorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.renunciaDirectorProyecto.RenunciaDirectorProyectoDM;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.seguridad.service.UsuarioService;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "directorProyectoController")
@SessionScoped
public class DirectorProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private DirectorProyectoDM directorProyectoDM;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private RenunciaDirectorProyectoDM renunciaDirectorProyectoDM;
    @Inject
    private DirectorDM directorDM;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private ItemService itemService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DirectorProyectoController.class.getName());

    public DirectorProyectoController() {
    }

    public void preRenderView() {
        this.renderedBuscar();
        this.renderedBuscarDirectorDisponible();
        this.renderedEliminar();
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">
    private void renderedBuscarDirectorDisponible() {
        try {
            directorProyectoDM.setRenderedBuscarDirectorDisponible(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                return;
            }
            int permiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "seleccionar_director_proyecto"));
            if (permiso == 1) {
                directorProyectoDM.setRenderedBuscarDirectorDisponible(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
        }

    }

    private void renderedBuscar() {
        try {
            directorProyectoDM.setRenderedBuscar(Boolean.FALSE);
            if (sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                directorProyectoDM.setRenderedBuscar(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
        }

    }

    private void renderedEliminar() {
        try {
            directorProyectoDM.setRenderedEliminar(Boolean.FALSE);
            if (!sessionProyecto.getEstadoActual().getCodigo().equals(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                return;
            }
            int permiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), cabeceraController.getValueFromProperties(
                    PropertiesFileEnum.PERMISOS, "eliminar_director_proyecto"));
            if (permiso == 1) {
                directorProyectoDM.setRenderedEliminar(Boolean.TRUE);
            }
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
        }

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CRUD">
    /**
     * DETERMINAR SI SE PUEE AGREGAR MAS DE DOS DIRECTORES A UN PROYECTO
     *
     * @return
     */
    private Boolean permiteAgregarDirector() {
        String value = configuracionService.buscar(new Configuracion(ConfiguracionEnum.AGREGARDIRECTORPROYECTO.getTipo())).get(0).getValor();
        if (sessionProyecto.getProyectoSeleccionado().getDirectorProyectoList().isEmpty()) {
            return Boolean.TRUE;
        }
        if (value.equals(ValorEnum.SI.getTipo())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void agregar(final DirectorDTO directorDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                return;
            }
            if (!permiteAgregarDirector()) {
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_existe") + " "
                        + bundle.getString("lbl.director"), "");
            }
            DirectorProyectoDTO dp = devuelveDirectorProyecto(directorDTO);
            if (dp != null) {
                return;
            }
            Item estadoDirector = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADODIRECTOR.getTipo(), EstadoDirectorEnum.INICIO.getTipo());
            DirectorProyectoDTO directorProyectoDTO = new DirectorProyectoDTO(new DirectorProyecto(null, sessionProyecto.getCronograma().getFechaInicio(),
                    sessionProyecto.getCronograma().getFechaProrroga(), sessionProyecto.getProyectoSeleccionado(), estadoDirector.getId(),
                    directorDTO.getDirector()), directorDTO);
            sessionProyecto.getDirectoresProyectoDTO().add(directorProyectoDTO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.director") + " "
                    + bundle.getString("lbl.msm_agregar"), "");
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private DirectorProyectoDTO devuelveDirectorProyecto(DirectorDTO directorDTO) {
        DirectorProyectoDTO dTO = null;
        try {
            for (DirectorProyectoDTO directorProyectoDTO : sessionProyecto.getDirectoresProyectoDTO()) {
                if (directorProyectoDTO.getDirectorDTO().getDocenteCarrera().getDocenteId().equals(directorDTO.getDocenteCarrera().getDocenteId())) {
                    dTO = directorProyectoDTO;
                    break;
                }
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
        return dTO;
    }

    public void remover(DirectorProyectoDTO directorProyectoDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (!sessionProyecto.getEstadoActual().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                return;
            }
            if (directorProyectoDTO.getDirectorProyecto().getId() != null) {
                directorProyectoDTO.getDirectorProyecto().setEstadoDirectorId(Long.MIN_VALUE);
                Calendar fechaActual = Calendar.getInstance();
                renunciaDirectorProyectoDM.setRenunciaDirector(new RenunciaDirector(null, new Renuncia(null, fechaActual.getTime(), "S/N", "S/N"),
                        directorProyectoDTO.getDirectorProyecto()));
                RequestContext.getCurrentInstance().execute("PF('dlgEditarRenunciaDirectorProyecto').show()");
                return;
            }
            sessionProyecto.getDirectoresProyectoDTO().remove(directorProyectoDTO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void cancelarDirectoresDisponibles() {
        this.directorDM.getDirectoresDTO().clear();
        this.directorDM.getFilterDirectoresDTO().clear();
        directorProyectoDM.setRenderedPnlDirectoresDisponibles(Boolean.FALSE);
    }
//</editor-fold>
}
