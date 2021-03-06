/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.cronograma;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.enumeration.ValorEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import edu.unl.sigett.docenteProyecto.DocenteProyectoDM;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.enumeration.ConfiguracionProyectoEnum;
import edu.unl.sigett.pertinencia.PertinenciaDM;
import edu.unl.sigett.service.ConfiguracionProyectoService;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "cronogramaController")
@SessionScoped
public class CronogramaController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private DocenteProyectoDM docenteProyectoDM;
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private PertinenciaDM pertinenciaDM;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;
    @EJB(lookup = "java:global/SigettService/ConfiguracionProyectoServiceImplement!edu.unl.sigett.service.ConfiguracionProyectoService")
    private ConfiguracionProyectoService configuracionProyectoService;

    //</editor-fold>
    public CronogramaController() {
    }

    private Boolean validarFechas() {
        if (pertinenciaDM.getPertinencia().getFecha() == null || docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().
                getProyectoId().getCronograma().getFechaFin() == null) {
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().
                    setFechaFin(pertinenciaDM.getPertinencia().getFecha());
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().
                    setFechaProrroga(pertinenciaDM.getPertinencia().getFecha());
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().setDuracion(0.0);
            return Boolean.FALSE;
        }
        if (pertinenciaDM.getPertinencia().getFecha().after(docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().
                getProyectoId().getCronograma().getFechaFin())) {
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().
                    setFechaFin(pertinenciaDM.getPertinencia().getFecha());
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().
                    setFechaProrroga(pertinenciaDM.getPertinencia().getFecha());
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().setDuracion(0.0);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * CALCULAR DURACIÓN DE CRONOGRAMA
     */
    public void calcularDuracion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        Double tiempoMax = Double.parseDouble(configuracionService.buscar(new Configuracion(ConfiguracionEnum.TIEMPODESARROLLOTT.getTipo())).get(0).getValor());
        @SuppressWarnings("UnusedAssignment")
        Double duracionDias = 0.0;
        if (!validarFechas()) {
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas"), "");
        }
        duracionDias = cabeceraController.getUtilService().calculaDuracion(pertinenciaDM.getPertinencia().getFecha(),
                docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().getFechaFin(),
                Integer.parseInt(ValorEnum.DIASSEMANA.getTipo()) - calculaDiasSemanaTrabajoProyecto());
        if (tiempoMax < duracionDias) {
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().
                    setFechaFin(pertinenciaDM.getPertinencia().getFecha());
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().
                    setFechaProrroga(pertinenciaDM.getPertinencia().getFecha());
            docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().setDuracion(0.0);
            this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString(
                    "lbl.msm_fechas_cronograma_limit") + ".", "");
            return;
        }
        docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId().getCronograma().setDuracion(duracionDias);
    }

    /**
     * OBTENER DE LAS CONFIGURACIONES DEL PROYECTO LOS DIAS QUE SE TRABAJA A LA
     * SEMANA
     *
     * @return
     */
    private Integer calculaDiasSemanaTrabajoProyecto() {
        Integer dias = 0;
        List<ConfiguracionProyecto> configuracionProyectos = configuracionProyectoService.buscar(new ConfiguracionProyecto(
                docenteProyectoDM.getDocenteProyectoDTOSeleccionado().getDocenteProyecto().getProyectoId(), null, null,
                ConfiguracionProyectoEnum.DIASSEMANA.getTipo(), null));
        for (ConfiguracionProyecto cf : configuracionProyectos) {
            dias = Integer.parseInt(cf.getValor());
            break;
        }
        return dias;
    }

}
