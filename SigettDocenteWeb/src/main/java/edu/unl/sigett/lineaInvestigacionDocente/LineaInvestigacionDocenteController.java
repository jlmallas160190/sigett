/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lineaInvestigacionDocente;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import edu.unl.sigett.service.LineaInvestigacionDocenteService;
import edu.unl.sigett.service.LineaInvestigacionService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.SessionSelectItems;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author jorge-luis
 */
@Named(value = "lineaInvestigacionDocenteController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "lineasInvestigacion",
            pattern = "/lineasInvestigacion",
            viewId = "/faces/pages/sigett/lineaInvestigacionDocente/index.xhtml"
    ),})
public class LineaInvestigacionDocenteController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionLineaInvestigacionDocente sessionLineaInvestigacionDocente;
    @Inject
    private SessionSelectItems sessionSelectItems;
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    @Inject
    private CabeceraController cabeceraController;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private LineaInvestigacionDocenteService lineaInvestigacionDocenteService;
    @EJB
    private LineaInvestigacionService lineaInvestigacionService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(LineaInvestigacionDocenteController.class.getName());

    public LineaInvestigacionDocenteController() {
    }

    public void preRenderView() {
        sessionLineaInvestigacionDocente.getLineaInvestigacionDocentesRemovidos().clear();
        listadoLineasInvestigacion();
    }

    public void listadoLineasInvestigacion() {
        @SuppressWarnings("UnusedAssignment")
        List<LineaInvestigacion> lineaInvestigacionDocentes = new ArrayList<>();
        List<LineaInvestigacion> lineaInvestigacionDiferencia = new ArrayList<>();
        try {
            lineaInvestigacionDocentes = lineaInvestigacionService.buscarPorDocente(new LineaInvestigacionDocente(
                    docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId(), null));
            for (DocenteCarrera docenteCarrera : sessionSelectItems.getDocenteCarreras()) {
                @SuppressWarnings("UnusedAssignment")
                List<LineaInvestigacion> lista = new ArrayList<>();
                lista = lineaInvestigacionService.buscarDiferenciaDocenteCarrera(new LineaInvestigacionCarrera(
                        null, docenteCarrera.getCarreraId().getId()),
                        new LineaInvestigacionDocente(docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId(), null));
                for (LineaInvestigacion li : lista) {
                    if (!lineaInvestigacionDiferencia.contains(li)) {
                        lineaInvestigacionDiferencia.add(li);
                    }
                }
            }
            sessionLineaInvestigacionDocente.setLineasInvestigacionDualList(new DualListModel<>(lineaInvestigacionDiferencia, lineaInvestigacionDocentes));
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void transferLineaInvestigacion(TransferEvent event) {
        try {
            for (Object item : event.getItems()) {
                int v = item.toString().indexOf(":");
                Long id = Long.parseLong(item.toString().substring(0, v));
                LineaInvestigacion li = lineaInvestigacionService.buscarPorId(new LineaInvestigacion(id));
                LineaInvestigacionDocente lid = new LineaInvestigacionDocente();
                lid.setLineaInvestigacionId(li);
                if (event.isRemove()) {
                    sessionLineaInvestigacionDocente.getLineaInvestigacionDocentesRemovidos().add(lid);
                } else {
                    sessionLineaInvestigacionDocente.getLineaInvestigacionDocentesRemovidos().remove(lid);
                }
            }
        } catch (NumberFormatException e) {
            LOG.warning(e.getMessage());
        }
    }

    public Long devuelveLineaInvestigacionEliminar(List<LineaInvestigacionDocente> docenteLineasInv, LineaInvestigacionDocente ld) {
        Long var = (long) 0;
        for (LineaInvestigacionDocente lineaInvestigacionDocente : docenteLineasInv) {
            if (lineaInvestigacionDocente.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
                var = lineaInvestigacionDocente.getId();
            }
        }
        return var;
    }

    public void removerLineasInvestigacion() {
        try {
            LineaInvestigacionDocente lineaInvestigacionDocenteBuscar = new LineaInvestigacionDocente();
            lineaInvestigacionDocenteBuscar.setDocenteId(docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId());
            for (LineaInvestigacionDocente ld : sessionLineaInvestigacionDocente.getLineaInvestigacionDocentesRemovidos()) {
                Long id = devuelveLineaInvestigacionEliminar(lineaInvestigacionDocenteService.buscar(lineaInvestigacionDocenteBuscar), ld);
                @SuppressWarnings("UnusedAssignment")
                LineaInvestigacionDocente lid = null;
                lid = lineaInvestigacionDocenteService.buscarPorId(new LineaInvestigacionDocente(id));
                lineaInvestigacionDocenteService.eliminar(lid);
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public String grabar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        grabarLineasInvestigacion();
        removerLineasInvestigacion();
        this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar") + "", "");
        return "pretty:inicioDocente";
    }

    private void grabarLineasInvestigacion() {
        List<LineaInvestigacionDocente> lids = new ArrayList<>();
        for (Object o : sessionLineaInvestigacionDocente.getLineasInvestigacionDualList().getTarget()) {
            int v = o.toString().indexOf(":");
            Long id = Long.parseLong(o.toString().substring(0, v));
            LineaInvestigacion li = lineaInvestigacionService.buscarPorId(new LineaInvestigacion(id));
            LineaInvestigacionDocente ld = new LineaInvestigacionDocente();
            ld.setDocenteId(docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId());
            ld.setLineaInvestigacionId(li);
            lids.add(ld);
        }
        for (LineaInvestigacionDocente lineaInvestigacionDocente : lids) {
            LineaInvestigacionDocente lineaInvestigacionDocenteBuscar = new LineaInvestigacionDocente();
            lineaInvestigacionDocenteBuscar.setDocenteId(docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId());
            if (contieneLineaInvestigacion(lineaInvestigacionDocenteService.buscar(lineaInvestigacionDocente), lineaInvestigacionDocente) == false) {
                lineaInvestigacionDocenteService.guardar(lineaInvestigacionDocente);
            }
        }
    }

    public boolean contieneLineaInvestigacion(List<LineaInvestigacionDocente> docenteLineasInv, LineaInvestigacionDocente ld) {
        boolean var = false;
        for (LineaInvestigacionDocente lineaInvestigacionDocente : docenteLineasInv) {
            if (lineaInvestigacionDocente.getLineaInvestigacionId().equals(ld.getLineaInvestigacionId())) {
                var = true;
                break;
            }
        }
        return var;
    }
}
