/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.cronograma;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.enumeration.ConfiguracionProyectoEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.api.date.DateResource;

/**
 *
 * @author jorge-luis
 */
@Named(value = "cronogramaController")
@SessionScoped
public class CronogramaController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionProyecto sessionProyecto;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ItemService itemService;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralDao;

    //</editor-fold>
    public CronogramaController() {
    }

    /**
     * EDITAR
     */
    public void editarCronograma() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("2");
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        double var = Double.parseDouble(configuracionGeneralDao.find((int) 22).getValor());
        int varMaxProrroga = Integer.parseInt(configuracionGeneralDao.find((int) 23).getValor());
        double var1 = (var / ((1 / (double) varMaxProrroga) * 100));
        double duracionDias = 0;
        double horasTrabajo = 0;
        DateResource calculo = new DateResource();
        Item estado = itemService.buscarPorId(sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId());
        if (sessionProyecto.getCronograma().getFechaInicio() == null || sessionProyecto.getCronograma().getFechaFin() == null) {
            return;
        }
        if (param != null) {
            if (param.equalsIgnoreCase("agregar-cronograma")) {
                sessionProyecto.getCronograma().setFechaProrroga(sessionProyecto.getCronograma().getFechaFin());
            }
        }
        if (sessionProyecto.getCronograma().getFechaInicio().before(sessionProyecto.getCronograma().getFechaProrroga())
                || sessionProyecto.getCronograma().getFechaInicio().equals(sessionProyecto.getCronograma().getFechaProrroga())) {
            duracionDias = calculo.calculaDuracionEnDias(sessionProyecto.getCronograma().getFechaInicio(),
                    sessionProyecto.getCronograma().getFechaProrroga(), 7 - calculaDiasSemanaTrabajoProyecto());
            horasTrabajo = duracionDias * calculahorasTrabajoProyecto();
            if (estado.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                if (horasTrabajo <= var) {
                    sessionProyecto.getCronograma().setDuracion(duracionDias);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString(
                            "lbl.msm_fechas_cronograma_limit") + ".", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    if (param.equals("agregar-cronograma")) {
                        sessionProyecto.getCronograma().setFechaFin(null);
                        sessionProyecto.getCronograma().setFechaProrroga(null);
                        sessionProyecto.getCronograma().setDuracion(0.0);
                    }
                }
            } else {
                if (horasTrabajo <= (var + var1)) {
                    sessionProyecto.getCronograma().setDuracion(duracionDias);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString(
                            "lbl.msm_fechas_cronograma_limit") + ".", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    if (param.equals("agregar-cronograma")) {
                        sessionProyecto.getCronograma().setFechaFin(null);
                        sessionProyecto.getCronograma().setFechaProrroga(null);
                        sessionProyecto.getCronograma().setDuracion(0.0);
                    }
                }
            }
            return;
        } 
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            sessionProyecto.getCronograma().setFechaFin(null);
            sessionProyecto.getCronograma().setFechaProrroga(null);
            sessionProyecto.getCronograma().setDuracion(0.0);
    }

    public int calculaDiasSemanaTrabajoProyecto() {
        int dias = 0;
        for (ConfiguracionProyecto cf : sessionProyecto.getConfiguracionProyectos()) {
            if (cf.getCodigo().equals(ConfiguracionProyectoEnum.DIASSEMANA.getTipo())) {
                dias = Integer.parseInt(cf.getValor());
                break;
            }
        }
        return dias;
    }

    public int calculahorasTrabajoProyecto() {
        int dias = 0;
        for (ConfiguracionProyecto cf : sessionProyecto.getConfiguracionProyectos()) {
            if (cf.getCodigo().equals(ConfiguracionProyectoEnum.HORASDIARIAS.getTipo())) {
                dias = Integer.parseInt(cf.getValor());
                break;
            }
        }
        return dias;
    }

    /**
     * RENDERIZAR DATOS DE CRONOGRAMA SOLO CUANDO EL PROYECTO ESTÉ EN
     * PERTINENCIA
     *
     */
    public void renderedCronograma() {
        try {
            sessionProyecto.setRenderedPertinente(Boolean.FALSE);
            Item item = itemService.buscarPorId(sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId() != null
                    ? sessionProyecto.getProyectoSeleccionado().getEstadoProyectoId() : null);
            if (item.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                sessionProyecto.setRenderedPertinente(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }
}
