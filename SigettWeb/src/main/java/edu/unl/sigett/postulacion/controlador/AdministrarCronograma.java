/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.controlador;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.CatalogoDuracion;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.entity.Cronograma;
import edu.unl.sigett.entity.DuracionCronograma;
import edu.unl.sigett.entity.Proyecto;
import org.jlmallas.seguridad.entity.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.dao.CatalogoDuracionFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.CronogramaFacadeLocal;
import edu.unl.sigett.dao.ProyectoDao;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.proyecto.managed.session.SessionProyecto;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "administrarCronograma")
@SessionScoped
public class AdministrarCronograma implements Serializable {

    @Inject
    private SessionProyecto sessionProyecto;
    @EJB
    private ItemService itemService;
    @EJB
    private CatalogoDuracionFacadeLocal catalogoDuracionFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private CronogramaFacadeLocal cronogramaFacadeLocal;
    @EJB
    private ProyectoDao proyectoFacadeLocal;

    private boolean renderedEditarDatosCronograma;
    private boolean renderedNoEditarDatosCronograma;

    public AdministrarCronograma() {
    }

    public void setFechaInicio(Cronograma cronograma) {
        if (cronograma != null) {
            cronograma.setFechaInicio(cronograma.getFechaInicio());
        }
    }

    /**
     * RENDERIZAR DATOS DE CRONOGRAMA SOLO CUANDO EL PROYECTO ESTÉ EN
     * PERTINENCIA
     *
     * @param proyecto
     */
    public void renderedCronograma(Proyecto proyecto) {
        try {
            sessionProyecto.setRenderedEditarCronograma(Boolean.FALSE);
            Item item = itemService.buscarPorId(sessionProyecto.getProyecto().getEstadoProyectoId() != null
                    ? sessionProyecto.getProyecto().getEstadoProyectoId() : null);
            if (item.getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
                sessionProyecto.setRenderedEditarCronograma(Boolean.TRUE);
            }
        } catch (Exception e) {
        }
    }

    public void abrirDialogoDuraciones(Cronograma cronograma) {
        try {
            editarCronograma(cronograma);
            RequestContext.getCurrentInstance().execute("PF('dlgBuscarDuraciones').show()");
        } catch (Exception e) {
        }
    }

    public void grabar(Cronograma cronograma) {
        try {
//            cronograma.setProyecto(proyectoFacadeLocal.find(cronograma.getId()));
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            if (cronograma.getProyecto().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                if (cronograma.getId() != null) {
//                    cronograma.getProyecto().setAutorProyectoList(new ArrayList<AutorProyecto>());
//                    cronogramaFacadeLocal.edit(cronograma);
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                }
//            }
        } catch (Exception e) {
        }
    }

    public void calculaAvanceFaltanteCronograma(Cronograma cronograma, List<Actividad> actividads, Usuario usuario) {
        double avance = 0;
        for (Actividad actividad : actividads) {
            if (actividad.getEsActivo()) {
                if (actividad.getTipoActividadId().getId() == 1) {
                    avance += (actividad.getPorcentajeDuracion() * actividad.getAvance()) / 100;
                }
            }
        }
        double valor = avance;
        valor = Math.round(valor * 100);
        valor = valor / 100;
        cronograma.setAvance(valor);
        cronograma.setFaltante(100 - valor);
        if (cronograma.getId() != null) {
            cronogramaFacadeLocal.edit(cronograma);
        }
    }

    public void editarCronograma(Cronograma cronograma) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("2");
//        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//        double var = Double.parseDouble(configuracionGeneralFacadeLocal.find((int) 22).getValor());
//        int varMaxProrroga = Integer.parseInt(configuracionGeneralFacadeLocal.find((int) 23).getValor());
//        double var1 = (var / ((1 / (double) varMaxProrroga) * 100));
//        double duracionDias = 0;
//        double horasTrabajo = 0;
//        DateResource calculo = new DateResource();
//        
//        if (cronograma.getFechaInicio() != null && cronograma.getFechaFin() != null) {
//            if (param != null) {
//                if (param.equalsIgnoreCase("agregar-cronograma")) {
//                    cronograma.setFechaProrroga(cronograma.getFechaFin());
//                }
//            }
//            if (cronograma.getFechaInicio().before(cronograma.getFechaProrroga()) || cronograma.getFechaInicio().equals(cronograma.getFechaProrroga())) {
//                duracionDias = calculo.calculaDuracionEnDias(cronograma.getFechaInicio(), cronograma.getFechaProrroga(), 7 - calculaDiasSemanaTrabajoProyecto(cronograma.getProyecto()));
//                horasTrabajo = duracionDias * calculahorasTrabajoProyecto(cronograma.getProyecto());
//                if (cronograma.getProyecto().getEstadoProyectoId().getCodigo().equalsIgnoreCase(EstadoProyectoEnum.PERTINENTE.getTipo())) {
//                    if (horasTrabajo <= var) {
//                        cronograma.setDuracion(0.0);
//                        agregarDuracionesCronograma(cronograma, duracionDias, horasTrabajo);
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_limit") + ".", "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        if (param.equalsIgnoreCase("agregar-cronograma")) {
//                            cronograma.setFechaFin(null);
//                            cronograma.setFechaProrroga(null);
//                            cronograma.setDuracion(0.0);
//                        }
//                    }
//                } else {
//                    if (horasTrabajo <= (var + var1)) {
//                        cronograma.setDuracion(0.0);
//                        agregarDuracionesCronograma(cronograma, duracionDias, horasTrabajo);
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_limit") + ".", "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                        if (param.equalsIgnoreCase("agregar-cronograma")) {
//                            cronograma.setFechaFin(null);
//                            cronograma.setFechaProrroga(null);
//                            cronograma.setDuracion(0.0);
//                        }
//                    }
//                }
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_fechas_cronograma_invalidas"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//                cronograma.setFechaFin(null);
//                cronograma.setFechaProrroga(null);
//                cronograma.setDuracion(0.0);
//            }
//        }
    }

    public int calculaDiasSemanaTrabajoProyecto(Proyecto proyecto) {
        int dias = 0;
        for (ConfiguracionProyecto cf : proyecto.getConfiguracionProyectoList()) {
            if (cf.getCodigo().equalsIgnoreCase("DS")) {
                dias = Integer.parseInt(cf.getValor());
                break;
            }
        }
        return dias;
    }

    public void agregarDuracionesCronograma(Cronograma cronograma, double duracionDias, double horasTrabajo) {
        if (cronograma.getDuracionCronogramaList() != null) {
            for (CatalogoDuracion cd : catalogoDuracionFacadeLocal.findAll()) {
                if (cd.getId() == 1) {
                    DuracionCronograma duracionCronograma = new DuracionCronograma();
                    duracionCronograma.setCatalogoDuracionId(cd);
                    duracionCronograma.setCronogramaId(cronograma);
                    duracionCronograma.setDuracion(duracionDias);
                    DuracionCronograma d = contieneDuracion(cronograma, duracionCronograma);
                    if (d == null) {
                        cronograma.getDuracionCronogramaList().add(duracionCronograma);
                    } else {
                        cronograma.getDuracionCronogramaList().remove(d);
                        d.setCronogramaId(duracionCronograma.getCronogramaId());
                        d.setDuracion(duracionCronograma.getDuracion());
                        d.setCatalogoDuracionId(duracionCronograma.getCatalogoDuracionId());
                        cronograma.getDuracionCronogramaList().add(d);
                    }
                } else {
                    if (cd.getId() == 2) {
                        DuracionCronograma duracionCronograma = new DuracionCronograma();
                        duracionCronograma.setCatalogoDuracionId(cd);
                        duracionCronograma.setCronogramaId(cronograma);
                        duracionCronograma.setDuracion(horasTrabajo);
                        DuracionCronograma d = contieneDuracion(cronograma, duracionCronograma);
                        if (d == null) {
                            cronograma.getDuracionCronogramaList().add(duracionCronograma);
                        } else {
                            cronograma.getDuracionCronogramaList().remove(d);
                            d.setCronogramaId(duracionCronograma.getCronogramaId());
                            d.setDuracion(duracionCronograma.getDuracion());
                            d.setCatalogoDuracionId(duracionCronograma.getCatalogoDuracionId());
                            cronograma.getDuracionCronogramaList().add(d);
                        }
                    }
                }
            }
        }
        boolean encontrado = false;
        for (DuracionCronograma d : cronograma.getDuracionCronogramaList()) {
            for (ConfiguracionProyecto configuracionProyecto : cronograma.getProyecto().getConfiguracionProyectoList()) {
                if (configuracionProyecto.getCodigo().equalsIgnoreCase("CD")) {
                    Integer id = Integer.parseInt(configuracionProyecto.getValor());
                    CatalogoDuracion c = catalogoDuracionFacadeLocal.find(id);
                    if (d.getCatalogoDuracionId().getId() == c.getId()) {
                        cronograma.setDuracion(d.getDuracion());
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado) {
                    break;
                }
            }
        }
    }

    public int calculahorasTrabajoProyecto(Proyecto proyecto) {
        int dias = 0;
        for (ConfiguracionProyecto cf : proyecto.getConfiguracionProyectoList()) {
            if (cf.getCodigo().equalsIgnoreCase("HD")) {
                dias = Integer.parseInt(cf.getValor());
                break;
            }
        }
        return dias;
    }

    public DuracionCronograma contieneDuracion(Cronograma cronograma, DuracionCronograma duracionCronograma) {
        DuracionCronograma d = null;
        for (DuracionCronograma dc : cronograma.getDuracionCronogramaList()) {
            if (dc.getCatalogoDuracionId().getId() == duracionCronograma.getCatalogoDuracionId().getId()) {
                d = dc;
                break;
            }
        }
        return d;
    }

    public boolean isRenderedEditarDatosCronograma() {
        return renderedEditarDatosCronograma;
    }

    public void setRenderedEditarDatosCronograma(boolean renderedEditarDatosCronograma) {
        this.renderedEditarDatosCronograma = renderedEditarDatosCronograma;
    }

    public boolean isRenderedNoEditarDatosCronograma() {
        return renderedNoEditarDatosCronograma;
    }

    public void setRenderedNoEditarDatosCronograma(boolean renderedNoEditarDatosCronograma) {
        this.renderedNoEditarDatosCronograma = renderedNoEditarDatosCronograma;
    }

}
