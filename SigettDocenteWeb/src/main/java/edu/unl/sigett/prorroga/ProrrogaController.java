/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.prorroga;

import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.directorProyecto.SessionDirectorProyecto;
import edu.unl.sigett.entity.Prorroga;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import edu.unl.sigett.service.CronogramaService;
import edu.unl.sigett.service.DocumentoCarreraService;
import edu.unl.sigett.service.ProrrogaService;
import edu.unl.sigett.util.CabeceraController;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.seguridad.service.UsuarioService;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "prorrogaController")
@SessionScoped
public class ProrrogaController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionProrroga sessionProrroga;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private SessionDirectorProyecto sessionDirectorProyecto;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ProrrogaService prorrogaService;
    @EJB
    private CronogramaService cronogramaService;
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private ItemService itemService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private DocumentoCarreraService documentoCarreraService;
    @EJB
    private ConfiguracionCarreraService configuracionCarreraService;

    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(ProrrogaController.class.getName());

    public ProrrogaController() {
    }

    public void preRenderView() {
        this.buscar();
    }

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    public void editar(Prorroga prorroga) {
        try {
            sessionProrroga.setProrroga(prorroga);
            sessionProrroga.setRenderedCrud(Boolean.TRUE);
            RequestContext.getCurrentInstance().execute("PF('dlgCrudProrroga').show()");
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void grabar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");

            Calendar fechaActual = Calendar.getInstance();
            if (fechaActual.getTime().before(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().
                    getCronograma().getFechaProrroga())) {
                return;
            }
            if (sessionProrroga.getProrroga().getEsAceptado()) {
                sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().
                        getCronograma().setFechaProrroga(sessionProrroga.getProrroga().getFecha());
            }
            prorrogaService.actualizar(sessionProrroga.getProrroga());
            cronogramaService.actualizar(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().
                    getCronograma());
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar") + ".", "");
            cancelarEdicion();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void cancelarEdicion() {
        this.sessionProrroga.setRenderedCrud(Boolean.FALSE);
        this.sessionProrroga.setProrroga(new Prorroga());
        RequestContext.getCurrentInstance().execute("PF('dlgCrudProrroga').hide()");
    }

    private void buscar() {
        sessionProrroga.getProrrogas().clear();
        sessionProrroga.getFilterProrrogas().clear();
        sessionProrroga.getProrrogas().addAll(prorrogaService.buscar(new Prorroga(
                null, null, null, null, Boolean.TRUE, null, null,
                sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getCronograma())));
        sessionProrroga.setFilterProrrogas(sessionProrroga.getProrrogas());
    }
    //</editor-fold>
}
