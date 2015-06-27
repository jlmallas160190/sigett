/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.CalificacionParametro;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.ParametroCatalogoEvaluacion;
import edu.unl.sigett.entity.RangoEquivalencia;
import edu.unl.sigett.entity.RangoNota;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import edu.unl.sigett.dao.CalificacionParametroFacadeLocal;
import edu.unl.sigett.dao.EvaluacionTribunalFacadeLocal;
import edu.unl.sigett.dao.ParametroCatalogoEvaluacionFacadeLocal;
import edu.unl.sigett.dao.RangoEquivalenciaFacadeLocal;

/**
 *
 * @author jorge-luis
 */
@Named(value = "administrarCalificacionParametro")
@SessionScoped
public class AdministrarCalificacionParametro implements Serializable {

    @Inject
    private AdministrarEvaluacionesTribunal administrarEvaluacionesTribunal;
    @Inject
    private AdministrarCalificacionMiembro administrarCalificacionMiembro;

    @EJB
    private CalificacionParametroFacadeLocal calificacionParametroFacadeLocal;
    @EJB
    private ParametroCatalogoEvaluacionFacadeLocal parametroCatalogoEvaluacionFacadeLocal;
//    @EJB
//    private CalificacionMiembroFacadeLocal calificacionMiembroFacadeLocal;
    @EJB
    private EvaluacionTribunalFacadeLocal evaluacionTribunalFacadeLocal;
    @EJB
    private RangoEquivalenciaFacadeLocal rangoEquivalenciaFacadeLocal;

    private List<CalificacionParametro> calificacionParametros;

    private boolean renderedRangoCalificacionSobre10;

    public AdministrarCalificacionParametro() {
    }

    public void crear() {
        try {
//            List<ParametroCatalogoEvaluacion> parametroCatalogoEvaluacions = new ArrayList<>();
//            parametroCatalogoEvaluacions = parametroCatalogoEvaluacionFacadeLocal.buscarPorCatalogoEvaluacion(catalogoEvaluacion.getId());
//            if (parametroCatalogoEvaluacions != null) {
//                for (ParametroCatalogoEvaluacion parametroCatalogoEvaluacion : parametroCatalogoEvaluacions) {
//                    CalificacionParametro cp = new CalificacionParametro();
//                    cp = calificacionParametroFacadeLocal.buscarPorCalificacionMiembroParametro(calificacionMiembro.getId(), parametroCatalogoEvaluacion.getId());
//                    if (parametroCatalogoEvaluacion.getParametroId().getEsActivo()) {
//                        if (cp == null) {
//                            cp = new CalificacionParametro();
//                            cp.setCalificacionMiembroId(calificacionMiembro);
////                            cp.setNota(0.0);
//                            cp.setParametroCatEvId(parametroCatalogoEvaluacion);
//                            calificacionParametroFacadeLocal.create(cp);
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
        }
    }

    public void grabarListado(String param, CalificacionMiembro calificacionMiembro) {
        try {
            for (CalificacionParametro calificacionParametro : calificacionParametros) {
                grabar(calificacionParametro, calificacionMiembro, param);
            }
        } catch (Exception e) {
        }
    }

    public void grabar(CalificacionParametro calificacionParametro, CalificacionMiembro calificacionMiembro, String param) {
        try {
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            Calendar fechaActual = Calendar.getInstance();
//            if (calificacionParametro.getId() == null) {
//                calificacionParametroFacadeLocal.create(calificacionParametro);
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            } else {
//
//                if (calificacionMiembro.getEvaluacionTribunalId() != null) {
//                    if (calificacionMiembro.getEvaluacionTribunalId().getFechaInicio().before(fechaActual.getTime()) && 
//                            calificacionMiembro.getEvaluacionTribunalId().getFechaPlazo().after(fechaActual.getTime())) {
//                        calificacionParametroFacadeLocal.edit(calificacionParametro);
//                        if (param.equalsIgnoreCase("grabar-cp")) {
//                            administrarCalificacionMiembro.calculaNota(calificacionMiembro);
//                            calificacionMiembroFacadeLocal.edit(calificacionMiembro);
//                            if (calificacionMiembro.getEvaluacionTribunalId()!= null) {
//                                administrarEvaluacionesTribunal.calculaNota(calificacionMiembro.getEvaluacionTribunalId().getTribunalId(), 
//                                        calificacionMiembro.getEvaluacionTribunalId());
//                                RangoNota rn = null;
//                                if (calificacionMiembro.getEvaluacionTribunalId().getRangoNotaId() != null) {
//                                    rn = calificacionMiembro.getEvaluacionTribunalId().getRangoNotaId();
//                                }
//                                if (rn != null) {
//                                    calificacionMiembro.getEvaluacionTribunalId().setRangoNotaId(rn);
//                                    for (RangoEquivalencia rangoEquivalencia : rangoEquivalenciaFacadeLocal.buscarPorRangoNota(rn.getId())) {
//                                        if (calificacionMiembro.getEvaluacionTribunalId().getNota() >= rangoEquivalencia.getNotaInicio() &&
//                                                calificacionMiembro.getEvaluacionTribunalId().getNota() <= rangoEquivalencia.getNotaFin()) {
//                                            calificacionMiembro.getEvaluacionTribunalId().setRangoEquivalenciaId(rangoEquivalencia);
//                                            break;
//                                        }
//                                    }
//                                }
//                                evaluacionTribunalFacadeLocal.edit(calificacionMiembro.getEvaluacionTribunalId());
//                            }
//                        }
//                        if (param.equalsIgnoreCase("grabar-cp")) {
//                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
//                    } else {
//                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar"), "");
//                        FacesContext.getCurrentInstance().addMessage(null, message);
//                    }
//                }
//            }
        } catch (Exception e) {
        }
    }

    public void buscar(CalificacionMiembro calificacionMiembro) {
        try {
            this.calificacionParametros = new ArrayList<>();
            for (CalificacionParametro cp : calificacionParametroFacadeLocal.buscarPorCalificacionMiembro(calificacionMiembro.getId())) {
                this.calificacionParametros.add(cp);
            }
        } catch (Exception e) {
        }
    }

    public void renderedRangoCalificacion(EvaluacionTribunal evaluacionTribunal) {
        try {
            if (evaluacionTribunal != null) {
                if (evaluacionTribunal.getRangoNotaId().getId() == 1) {
                    renderedRangoCalificacionSobre10 = true;
                } else {
                    if (evaluacionTribunal.getRangoNotaId().getId() == 2) {
                        renderedRangoCalificacionSobre10 = false;
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    public List<CalificacionParametro> getCalificacionParametros() {
        return calificacionParametros;
    }

    public void setCalificacionParametros(List<CalificacionParametro> calificacionParametros) {
        this.calificacionParametros = calificacionParametros;
    }

    public boolean isRenderedRangoCalificacionSobre10() {
        return renderedRangoCalificacionSobre10;
    }

    public void setRenderedRangoCalificacionSobre10(boolean renderedRangoCalificacionSobre10) {
        this.renderedRangoCalificacionSobre10 = renderedRangoCalificacionSobre10;
    }

}
