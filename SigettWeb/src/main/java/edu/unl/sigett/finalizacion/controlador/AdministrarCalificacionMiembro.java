/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import com.jlmallas.comun.dao.ConfiguracionDao;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.finalizacion.managed.session.SessionCalificacionMiembro;
import edu.unl.sigett.seguridad.managed.session.SessionDocenteUsuario;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.CalificacionParametro;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.Miembro;
import edu.unl.sigett.entity.RangoEquivalencia;
import edu.unl.sigett.entity.RangoNota;
import edu.unl.sigett.entity.Tribunal;
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
import edu.unl.sigett.dao.CalificacionMiembroFacadeLocal;
import edu.unl.sigett.dao.CalificacionParametroFacadeLocal;
import edu.unl.sigett.dao.EvaluacionTribunalFacadeLocal;
import edu.unl.sigett.dao.MiembroFacadeLocal;
import edu.unl.sigett.dao.RangoEquivalenciaFacadeLocal;

/**
 *
 * @author jorge-luis
 */
@Named(value = "administrarCalificacionMiembro")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarCalificacionMiembro",
            pattern = "/editarCalificacionMiembro/#{sessionCalificacionMiembro.calificacionMiembro.id}",
            viewId = "/faces/pages/sigett/editarCalificacionMiembro.xhtml"
    )
})
public class AdministrarCalificacionMiembro implements Serializable {

    @Inject
    private SessionCalificacionMiembro sessionCalificacionMiembro;
    @Inject
    private AdministrarCalificacionParametro administrarCalificacionParametro;
    @Inject
    private AdministrarTribunales administrarTribunales;
    @Inject
    private AdministrarEvaluacionesTribunal administrarEvaluacionesTribunal;
    @Inject
    private SessionDocenteUsuario sessionDocenteUsuario;
    @Inject
    private AdministrarSugerenciaCalificacionMiembro administrarSugerenciaCalificacionMiembro;

    @EJB
    private CalificacionMiembroFacadeLocal calificacionMiembroFacadeLocal;
    @EJB
    private MiembroFacadeLocal miembroFacadeLocal;
    @EJB
    private CalificacionParametroFacadeLocal calificacionParametroFacadeLocal;
    @EJB
    private EvaluacionTribunalFacadeLocal evaluacionTribunalFacadeLocal;
    @EJB
    private RangoEquivalenciaFacadeLocal rangoEquivalenciaFacadeLocal;
    @EJB
    private ConfiguracionDao configuracionFacadeLocal;

    private List<CalificacionMiembro> calificacionMiembros;
    private List<CalificacionMiembro> calificacionMiembrosConsulta;

    private boolean renderedEditar;

    public AdministrarCalificacionMiembro() {
    }

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS CRUD">
    public String editar(CalificacionMiembro calificacionMiembro, Docente docente, EvaluacionTribunal evaluacionTribunal) {
        String navegacion = "";
        try {
            boolean encontrado = false;
            if (calificacionMiembro.getId() != null) {
                calificacionMiembro = calificacionMiembroFacadeLocal.find(calificacionMiembro.getId());
                sessionCalificacionMiembro.setCalificacionMiembro(calificacionMiembro);
            }
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String param = (String) facesContext.getExternalContext().getRequestParameterMap().get("1");
            List<Miembro> miembros = new ArrayList<>();
            miembros = miembroFacadeLocal.buscarPorDocente(docente.getId());
            if (miembros != null && evaluacionTribunal != null) {
                for (Miembro miembro : miembros) {
                    if (encontrado == false) {
                        for (CalificacionMiembro cm : calificacionMiembroFacadeLocal.buscarPorMiembro(configuracionFacadeLocal.encriptaClave(miembro.getId() + ""))) {
                            if (cm.getMiembroId().equals(calificacionMiembro.getMiembroId())) {
                                sessionCalificacionMiembro.setCalificacionMiembro(calificacionMiembro);
                                administrarSugerenciaCalificacionMiembro.buscar(calificacionMiembro, "");
                                Calendar fechaActual = Calendar.getInstance();
                                if (evaluacionTribunal != null) {
                                    if (evaluacionTribunal.getFechaInicio().before(fechaActual.getTime()) && evaluacionTribunal.getFechaPlazo().after(fechaActual.getTime())) {
                                        administrarCalificacionParametro.crear(evaluacionTribunal.getCatalogoEvaluacionId(), calificacionMiembro);
                                        administrarCalificacionParametro.buscar(calificacionMiembro);
                                        administrarCalificacionParametro.renderedRangoCalificacion(evaluacionTribunal);
                                        if (param.equals("editar")) {
                                            navegacion = "pretty:editarCalificacionMiembro";
                                        }
                                        encontrado = true;
                                        break;
                                    } else {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_editar"), "");
                                        FacesContext.getCurrentInstance().addMessage(null, message);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
        }
        return navegacion;
    }

    public void buscar(EvaluacionTribunal evaluacionTribunal) {
        this.calificacionMiembros = new ArrayList<>();
        try {
            for (CalificacionMiembro cm : calificacionMiembroFacadeLocal.buscarPorEvaluacionTribunal(evaluacionTribunal.getId())) {
                if (!calificacionMiembros.contains(cm)) {
                    calificacionMiembros.add(cm);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<CalificacionMiembro> calificaciones(EvaluacionTribunal evaluacionTribunal) {
        try {
            List<CalificacionMiembro> calificaciones = new ArrayList<>();
            for (CalificacionMiembro cm : calificacionMiembroFacadeLocal.buscarPorEvaluacionTribunal(evaluacionTribunal.getId())) {
                calificaciones.add(cm);
            }
            return calificaciones;
        } catch (Exception e) {
        }
        return null;
    }

    public String grabar(CalificacionMiembro calificacionMiembro, String param) {
        String navegacion = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (calificacionMiembro.getId() != null) {
                administrarCalificacionParametro.grabarListado(param, calificacionMiembro);
                calculaNota(calificacionMiembro);
                calificacionMiembroFacadeLocal.edit(calificacionMiembro);
                administrarEvaluacionesTribunal.calculaNota(calificacionMiembro.getEvaluacionTribunalId().getTribunalId(), calificacionMiembro.getEvaluacionTribunalId());
                RangoNota rn = null;
                if (calificacionMiembro.getEvaluacionTribunalId().getRangoNotaId() != null) {
                    rn = calificacionMiembro.getEvaluacionTribunalId().getRangoNotaId();
                }
                if (rn != null) {
                    calificacionMiembro.getEvaluacionTribunalId().setRangoNotaId(rn);
                    for (RangoEquivalencia rangoEquivalencia : rangoEquivalenciaFacadeLocal.buscarPorRangoNota(rn.getId())) {
                        if (calificacionMiembro.getEvaluacionTribunalId().getNota() >= rangoEquivalencia.getNotaInicio() && calificacionMiembro.getEvaluacionTribunalId().getNota() <= rangoEquivalencia.getNotaFin()) {
                            calificacionMiembro.getEvaluacionTribunalId().setRangoEquivalenciaId(rangoEquivalencia);
                            break;
                        }
                    }
                }
                evaluacionTribunalFacadeLocal.edit(calificacionMiembro.getEvaluacionTribunalId());
                if (sessionDocenteUsuario.getDocenteUsuario().getDocenteId() != null) {
                    administrarTribunales.buscarPorDocente(sessionDocenteUsuario.getDocenteUsuario().getDocenteId(), "");
                }
                if (param.equalsIgnoreCase("grabar")) {
                    navegacion = "pretty:tribunalesDocente";
                    sessionCalificacionMiembro.setCalificacionMiembro(new CalificacionMiembro());
                } else {
                    if (param.equalsIgnoreCase("grabar-editar")) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_editar"), "");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }
        } catch (Exception e) {
        }
        return navegacion;
    }

    public void grabarCalificacionesMiembro(Tribunal tribunal, EvaluacionTribunal evaluacionTribunal) {
        try {
            for (Miembro miembro : miembroFacadeLocal.buscarPorTribunal(tribunal.getId())) {
                List<CalificacionMiembro> calificacionMiembros = calificacionMiembroFacadeLocal.buscarPorMiembroEvaluacionTribunal(configuracionFacadeLocal.encriptaClave(miembro.getId() + ""), evaluacionTribunal.getId());
                CalificacionMiembro calificacionMiembro = null;
                if (calificacionMiembros != null) {
                    calificacionMiembro = !calificacionMiembros.isEmpty() ? calificacionMiembros.get(0) : null;
                }
                if (calificacionMiembro == null) {
                    calificacionMiembro = new CalificacionMiembro();
                    calificacionMiembro.setEsActivo(true);
                    calificacionMiembro.setMiembroId(configuracionFacadeLocal.encriptaClave(miembro.getId() + ""));
                    calificacionMiembro.setNota(0.0);
                    calificacionMiembro.setEvaluacionTribunalId(evaluacionTribunal);
                    calificacionMiembro.setObservacion("Ninguna");
                    calificacionMiembroFacadeLocal.create(calificacionMiembro);
                }
            }
        } catch (Exception e) {
        }
    }

    public void calculaNota(CalificacionMiembro calificacionMiembro) {
        try {
            double nota = 0.0;
            int numeroParametros = 0;
            for (CalificacionParametro cp : calificacionParametroFacadeLocal.buscarPorCalificacionMiembro(calificacionMiembro.getId())) {
                numeroParametros++;
                nota += cp.getNota();
            }
            nota = nota / numeroParametros;
            nota = Math.round(nota * 100);
            nota = nota / 100;
            calificacionMiembro.setNota(nota);
            sessionCalificacionMiembro.setCalificacionMiembro(calificacionMiembro);
        } catch (Exception e) {
        }
    }

    public void calculaNotaView(CalificacionMiembro calificacionMiembro) {
        try {
            double nota = 0.0;
            int numeroParametros = 0;
            for (CalificacionParametro cp : administrarCalificacionParametro.getCalificacionParametros()) {
                numeroParametros++;
                nota += cp.getNota();
            }
            nota = nota / numeroParametros;
            nota = Math.round(nota * 100);
            nota = nota / 100;
            calificacionMiembro.setNota(nota);
            sessionCalificacionMiembro.setCalificacionMiembro(calificacionMiembro);
        } catch (Exception e) {
        }
    }

    public void buscarConsulta(EvaluacionTribunal evaluacionTribunal) {
        this.calificacionMiembrosConsulta = new ArrayList<>();
        try {
            for (CalificacionMiembro cm : calificacionMiembroFacadeLocal.buscarPorEvaluacionTribunal(evaluacionTribunal.getId())) {
                if (!calificacionMiembrosConsulta.contains(cm)) {
                    calificacionMiembrosConsulta.add(cm);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS RENDERED">

    public boolean renderedEditar(CalificacionMiembro calificacionMiembro, Docente docente, EvaluacionTribunal evaluacionTribunal) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            List<Miembro> miembros = new ArrayList<>();
            boolean encontrado = false;
            miembros = miembroFacadeLocal.buscarPorDocente(docente.getId());

            if (miembros != null && evaluacionTribunal != null) {
                for (Miembro miembro : miembros) {
                    if (encontrado == false) {
                        for (CalificacionMiembro cm : calificacionMiembroFacadeLocal.buscarPorMiembro(configuracionFacadeLocal.encriptaClave(miembro.getId() + ""))) {
                            if (cm.getMiembroId().equals(calificacionMiembro.getMiembroId()) && evaluacionTribunal.getFechaInicio().before(fechaActual.getTime()) && evaluacionTribunal.getFechaPlazo().after(fechaActual.getTime())) {
                                renderedEditar = true;
                                encontrado = true;
                                break;
                            } else {
                                renderedEditar = false;
                            }
                        }
                    }
                }

            } else {
                renderedEditar = false;
            }
        } catch (Exception e) {
        }
        return renderedEditar;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MÉTODOS SET Y GET">
    public List<CalificacionMiembro> getCalificacionMiembrosConsulta() {
        return calificacionMiembrosConsulta;
    }

    public void setCalificacionMiembrosConsulta(List<CalificacionMiembro> calificacionMiembrosConsulta) {
        this.calificacionMiembrosConsulta = calificacionMiembrosConsulta;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public List<CalificacionMiembro> getCalificacionMiembros() {
        return calificacionMiembros;
    }

    public void setCalificacionMiembros(List<CalificacionMiembro> calificacionMiembros) {
        this.calificacionMiembros = calificacionMiembros;
    }
//</editor-fold>
}
